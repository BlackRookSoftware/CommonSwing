/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.table;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import com.blackrook.commons.hash.HashMap;
import com.blackrook.commons.list.List;
import com.blackrook.swing.table.RTableModel.Column;

/**
 * A simple table with a row selection policy that displays annotated objects.
 * <p>RTables display and sort their data according to how the class that they store is 
 * annotated with {@link TableDescriptor} annotations.
 * <p>TableDescriptor annotations are placed on "getter" methods on the stored classes,
 * and only the ones with those annotations will be displayed. How the data is rendered and
 * sorted depends on the cell renderers attached to the columns. Whether or not the columns are
 * <b>editable</b> are if a corresponding "setter" method exists in that annotated class.
 * The setters are NOT annotated.
 * @author Matthew Tropiano
 * @since 2.5.0
 */
public class RTable<T extends Object> extends JPanel implements Iterable<T>, RTableUtils
{
	private static final long serialVersionUID = 6409313751355248586L;
	
	/**
	 * Selection policy.
	 */
	public static enum SelectPolicy
	{
		SINGLE(ListSelectionModel.SINGLE_SELECTION),
		SINGLE_INTERVAL(ListSelectionModel.SINGLE_INTERVAL_SELECTION),
		MULTIPLE_INTERVAL(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		private final int intern;
		private SelectPolicy(int intern)
		{
			this.intern = intern;
		}
	}
	
	/**
	 * Horizontal scrollbar policies.
	 */
	public static enum HPolicy
	{
		/** Scrollbar always appears. */
		ALWAYS(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS),
		/** Scrollbar appears as needed. */
		AS_NEEDED(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
		/** Scrollbar never appears. */
		NEVER(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		private final int intern;
		private HPolicy(int intern)
		{
			this.intern = intern;
		}
	}
	
	/**
	 * Vertical scrollbar policies.
	 */
	public static enum VPolicy
	{
		/** Scrollbar always appears. */
		ALWAYS(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS),
		/** Scrollbar appears as needed. */
		AS_NEEDED(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED),
		/** Scrollbar never appears. */
		NEVER(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		private final int intern;
		private VPolicy(int intern)
		{
			this.intern = intern;
		}
	}
		
	/** Table itself. */
	private JTable table;
	/** Table Model. */
	private RTableModel<T> tableModel;
	/** Encapsulating scroll pane. */
	private JScrollPane scrollPane;
	/** The model listener for this table. */
	private TableModelListener tableModelListener = new TableModelListener()
	{
		@Override
		public void tableChanged(TableModelEvent e)
		{
			if (e.getType() == TableModelEvent.UPDATE)
			{
				T item = getItem(e.getFirstRow());
				if (item != null)
					onUpdate(item);
			}
		}
	};

	/** Class comparator map. */
	private HashMap<Class<?>, Comparator<?>> classComparatorMap; 	
	/** Column comparator map. */
	private HashMap<Integer, Comparator<?>> columnComparatorMap; 	
		
	/**
	 * Creates a new RTable that stores a class. 
	 * @param classType the type of class that this stores.
	 */
	public RTable(Class<T> classType)
	{
		this(classType, SelectPolicy.SINGLE, VPolicy.ALWAYS, HPolicy.AS_NEEDED);
	}
		
	/**
	 * Creates a new RTable that stores a class. 
	 * @param classType the type of class that this stores.
	 * @param backingList the backing list - items are read from and written to the list.
	 * @since 2.7.0
	 */
	public RTable(Class<T> classType, List<T> backingList)
	{
		this(classType, new List<>(), SelectPolicy.SINGLE, VPolicy.ALWAYS, HPolicy.AS_NEEDED);
	}
		
	/**
	 * Creates a new RTable that stores a class. 
	 * @param classType the type of class that this stores.
	 * @param selectPolicy the selection policy to use.
	 */
	public RTable(Class<T> classType, SelectPolicy selectPolicy)
	{
		this(classType, new List<>(), selectPolicy, VPolicy.ALWAYS, HPolicy.AS_NEEDED);
	}
	
	/**
	 * Creates a new RTable that stores a class. 
	 * @param classType the type of class that this stores.
	 * @param backingList the backing list - items are read from and written to the list.
	 * @param selectPolicy the selection policy to use.
	 * @since 2.7.0
	 */
	public RTable(Class<T> classType, List<T> backingList, SelectPolicy selectPolicy)
	{
		this(classType, backingList, selectPolicy, VPolicy.ALWAYS, HPolicy.AS_NEEDED);
	}
	/**
	 * Creates a new RTable that stores a class. 
	 * @param classType the type of class that this stores.
	 * @param selectPolicy the selection policy to use.
	 * @param vpolicy the vertical scrollbar policy.
	 * @param hpolicy the horizontal scrollbar policy.
	 */
	public RTable(Class<T> classType, SelectPolicy selectPolicy, VPolicy vpolicy, HPolicy hpolicy)
	{
		this(classType, new List<>(), selectPolicy, vpolicy, hpolicy);
	}

	/**
	 * Creates a new RTable that stores a class. 
	 * @param classType the type of class that this stores.
	 * @param backingList the backing list - items are read from and written to the list.
	 * @param selectPolicy the selection policy to use.
	 * @param vpolicy the vertical scrollbar policy.
	 * @param hpolicy the horizontal scrollbar policy.
	 * @since 2.7.0
	 */
	public RTable(Class<T> classType, List<T> backingList, SelectPolicy selectPolicy, VPolicy vpolicy, HPolicy hpolicy)
	{
		columnComparatorMap = new HashMap<Integer, Comparator<?>>();
		classComparatorMap = new HashMap<Class<?>, Comparator<?>>();
		
		setClassComparator(Enum.class, ENUM_COMPARATOR);
		setClassComparator(Boolean.class, BOOLEAN_COMPARATOR);
		setClassComparator(Number.class, NUMBER_COMPARATOR);
		setClassComparator(Date.class, DATE_COMPARATOR);
		
		tableModel = new RTableModel<T>(classType, backingList);
		tableModel.addTableModelListener(tableModelListener);
		
		table = new RTableImpl(tableModel, selectPolicy);
		
		scrollPane = new JScrollPane(table, vpolicy.intern, hpolicy.intern);
		scrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	/**
	 * Returns a reference to the underlying table.
	 */
	public JTable getTable()
	{
		return table;
	}

	/**
	 * Returns a column index by its header name.
	 * @return a valid column index or -1 if not found.
	 * @since 2.5.2
	 */
	public int getColumnByName(String name)
	{
		int i = 0;
		for (@SuppressWarnings("rawtypes") Column c : tableModel.getColumnList())
		{
			if (c.name.equals(name))
				return i;
			i++;
		}
		return -1;
	}
	
	/**
	 * Returns a reference to the encapsulating scroll pane.
	 */
	public JScrollPane getScrollPane()
	{
		return scrollPane;
	}

	/**
	 * Sets if this list is enabled.
	 * @param enabled true if enabled, false otherwise.  
	 */
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		table.setEnabled(enabled);
		scrollPane.setEnabled(enabled);
	}

	/**
	 * Sets a comparator to use when sorting a column.
	 * <p>These comparators are resolved by the column's primary class first,
	 * and then its hierarchy is recursively searched if it is not found.
	 * @param clazz the class to assign a comparator to.
	 * @param comparator the comparator.
	 */
	public <E extends Object> void setClassComparator(Class<E> clazz, Comparator<E> comparator)
	{
		classComparatorMap.put(clazz, comparator);
	}
	
	/**
	 * Sets a comparator to use when sorting a <i>specific</i> column.
	 * <p>These comparators are resolved FIRST, before the class comparator is.
	 * @param columnIndex the column index to assign a comparator to.
	 * @param comparator the comparator.
	 */
	public void setColumnComparator(int columnIndex, Comparator<?> comparator)
	{
		columnComparatorMap.put(columnIndex, comparator);
	}
	
	/**
	 * Sets a renderer for item cells in the table for a particular column.
	 * See TableColumn.setCellRenderer().
	 */
	public void setColumnRenderer(int columnIndex, TableCellRenderer renderer)
	{
		TableColumn col = table.getColumnModel().getColumn(columnIndex);
		if (col != null)
			col.setCellRenderer(renderer);
	}

	/**
	 * Sets an editor for item cells in the table for a particular column.
	 * See TableColumn.setCellEditor().
	 */
	public void setColumnEditor(int columnIndex, TableCellEditor editor)
	{
		TableColumn col = table.getColumnModel().getColumn(columnIndex);
		if (col != null)
			col.setCellEditor(editor);
	}

	/**
	 * Sets a filter for rows in the table.
	 */
	@SuppressWarnings("unchecked")
	public void setRowFilter(RTableFilter<T> filter)
	{
		((TableRowSorter<RTableModel<T>>)table.getRowSorter()).setRowFilter(filter);
	}

	/** 
	 * Sets the selected row in the table.
	 * @param index the index to select in the table.
	 */
	public void setSelectedRow(int index)
	{
		table.clearSelection();
		table.setRowSelectionInterval(index, index);
	}

	/** 
	 * Sets the selected rows in the table.
	 * @param index the indices to select in the table.
	 */
	public void setSelectedRows(int ... index)
	{
		Arrays.sort(index);
		int start = -1;
		int end = -1;
		for (int i = 0; i < index.length; i++)
		{
			if (start < 0)
			{
				start = index[i];
				end = index[i];
			}
			else if (index[i] > end + 1)
			{
				table.setRowSelectionInterval(start, end);
				start = -1;
				end = -1;
			}
			else
			{
				index[i] = end;
			}
			
			if (i == index.length - 1 && start >= 0)
				table.setRowSelectionInterval(start, end);
		}
			
	}

	/**
	 * Clears all objects in this table.
	 */
	public void clear()
	{
		tableModel.clear();
		onClear();
	}
	
	/**
	 * Returns the item at a particular index in the table.
	 */
	public T getItem(int index)
	{
		return (T)tableModel.getRowAt(index);
	}

	/**
	 * Returns the item at a particular index in the table.
	 * This returns the correct item regardless of sorting or filtering.
	 * @since 2.5.2 
	 */
	public T getFilteredItem(int index)
	{
		if (index < 0 || index >= tableModel.getRowCount())
			return null;
		int modelIndex = table.convertRowIndexToModel(index);
		return (T)tableModel.getRowAt(modelIndex);
	}

	/**
	 * Returns the items between two particular indices in the table,
	 * exclusively.
	 */
	public List<T> getItems(int index0, int index1)
	{
		List<T> alist = new List<T>(index1 - index0);
		for (int i = index0; i < index1; i++)
			alist.add(getItem(i));
		return alist;
	}

	/**
	 * Returns the items between two particular indices in the table,
	 * exclusively, figuring out the actual items based on filtering and sorting.
	 * @since 2.5.2 
	 */
	public List<T> getFilteredItems(int index0, int index1)
	{
		List<T> alist = new List<T>(index1 - index0);
		for (int i = index0; i < index1; i++)
		{
			T item = getFilteredItem(i);
			if (item != null) 
				alist.add(item);
		}
		return alist;
	}

	/**
	 * Adds an item to the table.
	 * @param object the object to add to the table.
	 */
	@SuppressWarnings("unchecked")
	public void addItem(T object)
	{
		tableModel.addRows(object);
		onAdd(object);
	}

	/**
	 * Adds a bunch of items to the table.
	 * @param objects the objects to add to the table.
	 */
	public void addItems(T[] objects)
	{
		tableModel.addRows(objects);
		for (T obj : objects)
			onAdd(obj);
	}

	/**
	 * Adds an item to the table at a specific index.
	 * @param index the index at which to place the object.
	 * @param object the object to add to the table.
	 */
	@SuppressWarnings("unchecked")
	public void addItem(int index, T object)
	{
		tableModel.addRows(index, object);
		onAdd(object);
	}

	/**
	 * Removes an item from the table.
	 * @param object the object to remove from the table.
	 */
	public boolean removeItem(T object)
	{
		boolean out = tableModel.removeRow(object);
		if (out)
			onRemove(object);
		return out;
	}

	/**
	 * Removes an item from the table at a specific model index.
	 * @param index the index from which to remove the object.
	 */
	public T removeItem(int index)
	{
		T out = tableModel.removeRowAt(index);
		if (out != null)
			onRemove(out);
		return out;
	}

	/**
	 * Removes an item from the table at a visible index.
	 * This figures out the ACTUAL item to remove based 
	 * on current filtering and sorting.
	 * @param selectionIndex the index from which to remove the object.
	 * @since 2.5.2 
	 */
	public T removeFilteredItem(int selectionIndex)
	{
		if (selectionIndex < 0 || selectionIndex >= tableModel.getRowCount())
			return null;
		int modelIndex = table.convertRowIndexToModel(selectionIndex);
		T out = tableModel.removeRowAt(modelIndex);
		if (out != null)
			onRemove(out);
		return out;
	}

	/** 
	 * Returns the amount of selected items in the table.
	 * @since 2.5.2 
	 */
	public int getSelectedCount()
	{
		return table.getSelectedRowCount();
	}

	/**
	 * Returns the first selected index in the table.
	 * Returns -1 if nothing selected.
	 */
	public int getSelectedRow()
	{
		return table.getSelectedRow();
	}

	/**
	 * Returns all of the selected indices in the table.
	 */
	public int[] getSelectedRows()
	{
		return table.getSelectedRows();
	}

	/**
	 * Returns the first selected object in the table.
	 * Returns null if no object selected.
	 */
	public T getSelected()
	{
		int s = getSelectedRow();
		if (s < 0) return null;
		return getFilteredItem(s);
	}

	/**
	 * Returns the selected objects in the table.
	 */
	public List<T> getAllSelected()
	{
		int[] s = getSelectedRows();
		List<T> atable = new List<T>(Math.max(s.length, 1));
		for (int i : s)
			atable.add(getFilteredItem(i));
		return atable;
	}

	/**
	 * Removes the selected items from the table.
	 * @since 2.5.2 
	 */
	public void removeSelectedItems()
	{
		for (int rowIndex : getSelectedRows())
		{
			int modelIndex = table.convertRowIndexToModel(rowIndex);
			T out = tableModel.removeRowAt(modelIndex);
			if (out != null)
				onRemove(out);
		}
	}

	/**
	 * Returns all of the items in this table.
	 */
	public List<T> getAllItems()
	{
		List<T> atable = new List<T>(Math.max(getItemCount(), 1));
		for (T obj : this)
			atable.add(obj);
		return atable;
	}

	/** 
	 * Returns the amount of items in the table. 
	 */
	public int getItemCount()
	{
		return tableModel.getRowCount();
	}

	@Override
	public Iterator<T> iterator()
	{
		return tableModel.iterator();
	}

	/**
	 * Called when an object is added to the table.
	 * Does nothing unless overridden.
	 * @param object the added object.
	 */
	public void onAdd(T object)
	{
		// Do nothing.
	}
	
	/**
	 * Called when an object is removed from the table.
	 * Does nothing unless overridden.
	 * @param object the removed object.
	 */
	public void onRemove(T object)
	{
		// Do nothing.
	}
	
	/**
	 * Called when the table is cleared.
	 * Does nothing unless overridden.
	 */
	public void onClear()
	{
		// Do nothing.
	}
	
	/**
	 * Called when an object is updated in the table.
	 * Does nothing unless overridden.
	 * @param object the removed object.
	 */
	public void onUpdate(T object)
	{
		// Do nothing.
	}
	
	/**
	 * Called when a row is selected in the table.
	 * Does nothing unless overridden.
	 * @see #getSelected()
	 * @see #getAllSelected()
	 * @see #getSelectedRow()
	 * @see #getSelectedRows()
	 */
	public void onSelectRow()
	{
		// Do nothing.
	}
	
	/** 
	 * Implementation of some special things that the table does. 
	 */
	private class RTableImpl extends JTable
	{
		private static final long serialVersionUID = 8261354740182093448L;

		private RTableImpl(RTableModel<T> model, SelectPolicy policy)
		{
			super(model);
			setSelectionMode(policy.intern);
			getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				@Override
				public void valueChanged(ListSelectionEvent e)
				{
					if (!e.getValueIsAdjusting())
						onSelectRow();
				}
			});
			setUpdateSelectionOnSort(true);
			setColumnSelectionAllowed(false);
			getTableHeader().setReorderingAllowed(false);
			getTableHeader().setResizingAllowed(false);
			setRowSorter(new RTableRowSorterImpl(model));
			addComponentListener(new ComponentAdapter()
			{
				@Override
				public void componentResized(ComponentEvent e)
				{
					int width = getWidth();
					int wtotal = 0;
					for (RTableModel<T>.Column c : tableModel.getColumnList())
						wtotal += c.width;
					int i = 0;
					for (RTableModel<T>.Column c : tableModel.getColumnList())
						getColumnModel().getColumn(i++).setPreferredWidth((int)(((float)c.width/wtotal) * width));
				}
			});
		}
		
		@Override
		public String getToolTipText(MouseEvent event)
		{
			Point p = event.getPoint();
			int row = rowAtPoint(p);
			int col = columnAtPoint(p);
			if (row < 0 || col < 0)
				return null;
			int columnIndex = convertColumnIndexToModel(col);
			RTableModel<?>.Column column = tableModel.getColumnList().getByIndex(columnIndex);
			if (column != null)
				return column.tip;
			return null;
		}
		
	}
	
	/** Row sorter implementation. */
	private class RTableRowSorterImpl extends TableRowSorter<RTableModel<T>>
	{
		private RTableRowSorterImpl(RTableModel<T> model)
		{
			super(model);
		}
		
		@Override
		public boolean isSortable(int column)
		{
			RTableModel<?>.Column col = getModel().getColumnList().getByIndex(column);
			if (col != null)
				return col.sortable;
			return false;
		}
		
		@Override
		public Comparator<?> getComparator(int column)
		{
			if (columnComparatorMap.containsKey(column))
				return columnComparatorMap.get(column);
			
			Class<?> clazz = getModel().getColumnClass(column);
			Comparator<?> out = null;
			while (out == null && clazz != null)
			{
				out = classComparatorMap.get(clazz);
				if (out == null)
					clazz = clazz.getSuperclass();
			}
			return out;
		}

	}
	
}
