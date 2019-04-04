/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.table;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import com.blackrook.commons.Reflect;
import com.blackrook.commons.list.List;

/**
 * Table descriptor that describes how to present the data in a table.
 * @author Matthew Tropiano
 * @since 2.5.0
 */
public class RTableModel<T extends Object> extends AbstractTableModel implements Iterable<T>
{
	private static final long serialVersionUID = 8496763669872325993L;
	
	/** The list of columns. */
	private List<Column> columnList;
	/** Data set. */
	private List<T> data;

	/**
	 * Creates a table descriptor using a base class,
	 * inspecting its getter fields.
	 */
	RTableModel(Class<T> classType, List<T> backingList)
	{
		this.columnList = new List<Column>();
		this.data = backingList;
		
		for (Method method : classType.getMethods())
		{
			String fieldName = Reflect.getFieldName(method.getName());
			if (Reflect.isGetter(method))
			{
				TableDescriptor td = method.getAnnotation(TableDescriptor.class);
				if (td != null && !td.hidden())
				{
					Column col = new Column();
					col.dataType = upgradePrimitiveType(method.getReturnType());
					col.getterMethod = method;
					try {
						col.setterMethod = classType.getMethod(Reflect.getSetterName(fieldName), method.getReturnType());
					} catch (NoSuchMethodException ex) {
						col.setterMethod = null;
					}
					col.width = td.width();
					String tname = td.name().trim();
					col.name = tname.length() > 0 ? tname : fieldName;
					col.order = td.order();
					col.sortable = td.sortable();
					col.editable = td.editable() && col.setterMethod != null;
					col.tip = td.tip().trim().length() > 0 ? td.tip() : null;
					columnList.add(col);
				}
			}
		}
		
		columnList.sort(new Comparator<RTableModel<T>.Column>()
		{
			@Override
			public int compare(Column c1, Column c2)
			{
				return c1.order - c2.order;
			}
		});
		
	}
	
	// Some fields may be primitive. Swing JTable has no cell renderers
	// for these, so they need boxing.
	private Class<?> upgradePrimitiveType(Class<?> clazz)
	{
		if (Reflect.PRIMITIVE_TO_CLASS_MAP.containsKey(clazz))
			return Reflect.PRIMITIVE_TO_CLASS_MAP.get(clazz);
		return clazz;
	}

	/**
	 * Returns the column list.
	 */
	List<Column> getColumnList()
	{
		return columnList;
	}
	
	/**
	 * Clears this model of all data.
	 */
	public void clear()
	{
		data.clear();
		fireTableDataChanged();
	}
	
	/**
	 * Adds rows to this model and fires the appropriate method.
	 * @param row the row to add.
	 */
	@SuppressWarnings("unchecked")
	public void addRows(T ... row)
	{
		if (row.length == 0)
			return;
			
		int start = data.size();
		for (T r : row)
			data.add(r);
		fireTableRowsInserted(start, data.size() - 1);
	}
	
	/**
	 * Adds rows to this model and fires the appropriate method.
	 * @param row the row to add.
	 */
	@SuppressWarnings("unchecked")
	public void addRows(int start, T ... row)
	{
		if (row.length == 0)
			return;

		int i = start;
		for (T r : row)
			data.add(i++, r);
		fireTableDataChanged();
	}
	
	/**
	 * Removes a row from this model and fires the appropriate method.
	 * @param row the row to remove.
	 * @return the row removed or null if not removed.
	 */
	public boolean removeRow(T row)
	{
		int index = data.getIndexOf(row);
		if (index >= 0)
			return removeRowAt(index) != null;
		return false;
	}
	
	/**
	 * Removes a row from this model and fires the appropriate method.
	 * @param index the row index to remove.
	 * @return the row removed or null if not removed.
	 */
	public T removeRowAt(int index)
	{
		T out = null;
		out = data.removeIndex(index);
		if (out != null)
			fireTableRowsDeleted(index, index);
		return out;
	}
	
	/**
	 * Removes multiple rows from this model, but fires one table redraw event.
	 * @param indices the row indices to remove.
	 */
	public void removeMultipleRows(int ... indices)
	{
		for (int i : indices)
			data.removeIndex(i);
		fireTableDataChanged();
	}
	
	/**
	 * Gets a row at a particular index.
	 * @param index the index.
	 * @return the row at that index or null if none found.
	 */
	public T getRowAt(int index)
	{
		return data.getByIndex(index);
	}
	
	@Override
	public Iterator<T> iterator()
	{
		return data.iterator();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		T row = getRowAt(rowIndex);
		if (row == null)
			return null;
		
		Column col = columnList.getByIndex(columnIndex);
		if (col == null)
			return null;
		
		if (col.getterMethod == null)
			return null;
		else
		{
			Object out = Reflect.invokeBlind(col.getterMethod, row);
			return out;
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		T row = getRowAt(rowIndex);;
		if (row == null) return;
		
		Column col = columnList.getByIndex(columnIndex);
		if (col == null) return;
		
		if (col.setterMethod == null)
			return;
		else
		{
			Reflect.invokeBlind(col.setterMethod, row, aValue);
			fireTableCellUpdated(rowIndex, columnIndex);
		}
	}

	@Override
	public int getColumnCount()
	{
		return columnList.size();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		Column col = columnList.getByIndex(columnIndex);
		if (col == null)
			return null;
		return col.dataType;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		Column col = columnList.getByIndex(columnIndex);
		if (col == null)
			return null;
		return col.name;
	}
	
	@Override
	public int getRowCount()
	{
		return data.size();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		Column col = columnList.getByIndex(columnIndex);
		if (col == null)
			return false;
		return col.editable;
	}
	
	/**
	 * Descriptor column.
	 */
	public final class Column
	{
		/** Nice name. */
		String name;
		/** Width units. */
		int width;
		/** Order key. */
		int order;
		/** Column tooltip. */
		String tip;
		/** Sortable? */
		boolean sortable;
		/** Editable? */
		boolean editable;
		/** Data type class. */
		Class<?> dataType;
		/** Setter Method (can be null). */
		Method setterMethod;
		/** Getter Method. */
		Method getterMethod;
		
		private Column()
		{
			// blank. set fields in parent class.
		}
	}
}

