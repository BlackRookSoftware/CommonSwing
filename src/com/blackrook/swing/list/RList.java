/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.list;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.blackrook.commons.list.List;
import com.blackrook.swing.field.RListEventListener;

/**
 * List type for keeping track of a list of objects.
 * This is already enclosed in a JScrollPane which contains a JList object.
 * @author Matthew Tropiano
 */
public class RList<T extends Object> extends JPanel implements RListEventListener<T>
{
	private static final long serialVersionUID = 929879343687450071L;

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
	
	/** The list itself. */
	private JList<T> list;
	/** The list's data model. */
	private RListModel<T> dataModel;
	/** The scrolling pane for this list. */
	private JScrollPane scrollPane;

	/**
	 * Creates a new, empty RList with single selection 
	 * policy and both scrollbars appearing as needed.
	 */
	public RList()
	{
		this(SelectPolicy.SINGLE, VPolicy.AS_NEEDED, HPolicy.AS_NEEDED);
	}

	/**
	 * Creates a new, empty RList with single selection 
	 * policy and both scrollbars appearing as needed.
	 * @param backingList	the backing list - items are read from and written to the list.
	 * @since 2.7.0
	 */
	public RList(List<T> backingList)
	{
		this(backingList, SelectPolicy.SINGLE, VPolicy.AS_NEEDED, HPolicy.AS_NEEDED);
	}

	/**
	 * Creates a new, empty RList with both scrollbars appearing as needed.
	 * @param selectPolicy	selection policy for the list.
	 */
	public RList(SelectPolicy selectPolicy)
	{
		this(selectPolicy, VPolicy.AS_NEEDED, HPolicy.AS_NEEDED);
	}

	/**
	 * Creates a new RList with both scrollbars appearing as needed.
	 * @param backingList	the backing list - items are read from and written to the list.
	 * @param selectPolicy	selection policy for the list.
	 * @since 2.7.0
	 */
	public RList(List<T> backingList, SelectPolicy selectPolicy)
	{
		this(backingList, selectPolicy, VPolicy.AS_NEEDED, HPolicy.AS_NEEDED);
	}

	/**
	 * Creates a new, empty RList.
	 * @param selectPolicy	selection policy for the list.
	 * @param vsbPolicy		the vertical scrollbar policy.
	 * @param hsbPolicy		the horizontal scrollbar policy.
	 */
	public RList(SelectPolicy selectPolicy, VPolicy vsbPolicy, HPolicy hsbPolicy)
	{
		this(new List<>(), selectPolicy, vsbPolicy, hsbPolicy);
	}
	
	/**
	 * Creates a new RList.
	 * @param backingList	the backing list - items are read from and written to the list.
	 * @param selectPolicy	selection policy for the list.
	 * @param vsbPolicy		the vertical scrollbar policy.
	 * @param hsbPolicy		the horizontal scrollbar policy.
	 * @since 2.7.0
	 */
	public RList(List<T> backingList, SelectPolicy selectPolicy, VPolicy vsbPolicy, HPolicy hsbPolicy)
	{
		this.dataModel = new RListModel<T>(backingList);
		this.list = new JList<T>(dataModel);
		
		list.setSelectionMode(selectPolicy.intern);
		list.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if (!e.getValueIsAdjusting())
					onSelect();
			}
		});
		
		list.setCellRenderer(new ListCellRenderer<T>()
		{
			private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
			
			@Override
			public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus)
			{
				JLabel renderer = (JLabel)defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				renderer.setText(getItemString(value));
				return renderer;
			}
			
		});
		
		scrollPane = new JScrollPane(list, vsbPolicy.intern, hsbPolicy.intern);
		scrollPane.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}
	
	/**
	 * Clears all items in the list.
	 * @since 2.5.3
	 */
	public void clear()
	{
		dataModel.clear();
	}
	
	/**
	 * Sets if this list is enabled.
	 * @param enabled true if enabled, false otherwise.  
	 */
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		list.setEnabled(enabled);
		scrollPane.setEnabled(enabled);
	}
	
	/**
	 * Sets the item renderer for item cells in the list.
	 * See JList.setCellRenderer().
	 */
	public void setItemRenderer(ListCellRenderer<T> cellRenderer)
	{
		list.setCellRenderer(cellRenderer);
	}
	
	/** 
	 * Sets the selected item in the list.
	 * @param item the item to select in the list. Can be null for no selection.
	 * @since 2.6.2
	 */
	public void setSelected(T item)
	{
		if (item == null)
		{
			list.clearSelection();
			return;
		}
			
		for (int i = 0; i < dataModel.getSize(); i++)
		{
			if (getItem(i).equals(item))
				setSelectedIndex(i);
		}
		
	}
	
	/** 
	 * Sets the selected index in the list.
	 * @param index the index to select in the list.
	 */
	public void setSelectedIndex(int index)
	{
		list.setSelectedIndex(index);
	}
	
	/** 
	 * Sets the selected indices in the list.
	 * @param index the indices to select in the list.
	 */
	public void setSelectedIndices(int ... index)
	{
		list.setSelectedIndices(index);
	}
	
	/**
	 * Returns the first selected index in the list.
	 * Returns -1 if nothing selected.
	 */
	public int getSelectedIndex()
	{
		return list.getSelectedIndex();
	}

	/**
	 * Returns all of the selected indices in the list.
	 */
	public int[] getSelectedIndices()
	{
		return list.getSelectedIndices();
	}

	/**
	 * Returns the first selected object in the list.
	 * Returns null if no object selected.
	 */
	public T getSelected()
	{
		int s = getSelectedIndex();
		if (s < 0) return null;
		return (T)dataModel.getElementAt(s);
	}

	/**
	 * Returns the selected object in the list.
	 * Returns null if no object selected.
	 */
	public Iterable<T> getAllSelected()
	{
		int[] s = getSelectedIndices();
		List<T> alist = new List<T>(s.length);
		for (int i : s)
			alist.add(getItem(i));
		return alist;
	}

	/**
	 * Returns all of the items in this list.
	 */
	public Iterable<T> getAllItems()
	{
		List<T> alist = new List<T>(dataModel.getSize());
		for (T obj : dataModel)
			alist.add(obj);
		return alist;
	}

	/** Returns the amount of items in the list. */
	public int getItemCount()
	{
		return dataModel.getSize();
	}

	/**
	 * The default cell renderer uses this method to return
	 * what to display as a string. By default this is <code>item.toString()</code>.
	 * @return the string to use. Should not return null.
	 * @since 2.6.1
	 */
	public String getItemString(T item)
	{
		return item.toString();
	}

	/**
	 * Checks if this list contains an item.
	 * @param item the item to check for.
	 * @return true if so, false if not.
	 * @since 2.7.0
	 */
	public boolean containsItem(T item)
	{
		return dataModel.contains(item);
	}
	
	/**
	 * Returns the item at a particular index in the list.
	 */
	public T getItem(int index)
	{
		return dataModel.getElementAt(index);
	}
	
	/**
	 * Returns the items between two particular indices in the list,
	 * exclusively.
	 */
	public Iterable<T> getItems(int index0, int index1)
	{
		List<T> alist = new List<T>(index1 - index0);
		for (int i = index0; i < index1; i++)
			alist.add(getItem(i));
		return alist;
	}
	
	/**
	 * Adds an item to the list.
	 * @param object the object to add to the list.
	 */
	public void addItem(T object)
	{
		dataModel.add(object);
		onAdd(object);
	}
	
	/**
	 * Adds a bunch of items to the list.
	 * @param objects the objects to add to the list.
	 */
	public void addItems(T[] objects)
	{
		for (T obj : objects)
			addItem(obj);
	}
	
	/**
	 * Adds an item to the list at a specific index.
	 * @param index the index at which to place the object.
	 * @param object the object to add to the list.
	 */
	public void addItem(int index, T object)
	{
		dataModel.add(index, object);
		onAdd(object);
	}

	/**
	 * Removes an item from the list.
	 * @param object the object to remove from the list.
	 */
	public boolean removeItem(T object)
	{
		boolean b = dataModel.remove(object);
		if (b) onRemove(object);
		return b;
	}
	
	/**
	 * Removes an item from the list at a specific index.
	 * @param index the index from which to remove the object.
	 */
	public T removeItem(int index)
	{
		T out = (T)dataModel.removeIndex(index);
		onRemove(out);
		return out;
	}

	/**
	 * @return this list's data model.
	 */
	protected RListModel<T> getDataModel()
	{
		return dataModel;
	}
	
	@Override
	public void onAdd(T object)
	{
		// Do nothing.
	}

	@Override
	public void onRemove(T object)
	{
		// Do nothing.
	}

	@Override
	public void onSelect()
	{
		// Do nothing.
	}

}
