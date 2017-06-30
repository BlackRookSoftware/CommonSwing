/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
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

import com.blackrook.commons.ObjectPair;
import com.blackrook.commons.list.List;
import com.blackrook.commons.list.SortedMap;
import com.blackrook.swing.field.RListEventListener;

/**
 * List type for keeping track of a list of objects.
 * This is already enclosed in a JScrollPane which contains a JList object.
 * @author Matthew Tropiano
 * @since 2.7.0
 */
public class RSortedMapList<K extends Comparable<K>, V extends Object> extends JPanel implements RListEventListener<ObjectPair<K, V>>
{
	private static final long serialVersionUID = 3715137153694129808L;

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
	private JList<ObjectPair<K, V>> list;
	/** The list's data model. */
	private RSortedMapListModel<K, V> dataModel;
	/** The scrolling pane for this list. */
	private JScrollPane scrollPane;

	/**
	 * Creates a new, empty RList with single selection 
	 * policy and both scrollbars appearing as needed.
	 */
	public RSortedMapList()
	{
		this(SelectPolicy.SINGLE, VPolicy.AS_NEEDED, HPolicy.AS_NEEDED);
	}

	/**
	 * Creates a new, empty RList with single selection 
	 * policy and both scrollbars appearing as needed.
	 * @param backingList	the backing list - items are read from and written to the list.
	 */
	public RSortedMapList(SortedMap<K, V> backingList)
	{
		this(backingList, SelectPolicy.SINGLE, VPolicy.AS_NEEDED, HPolicy.AS_NEEDED);
	}

	/**
	 * Creates a new, empty RList with both scrollbars appearing as needed.
	 * @param selectPolicy	selection policy for the list.
	 */
	public RSortedMapList(SelectPolicy selectPolicy)
	{
		this(selectPolicy, VPolicy.AS_NEEDED, HPolicy.AS_NEEDED);
	}

	/**
	 * Creates a new RList with both scrollbars appearing as needed.
	 * @param backingList	the backing list - items are read from and written to the list.
	 * @param selectPolicy	selection policy for the list.
	 */
	public RSortedMapList(SortedMap<K, V> backingList, SelectPolicy selectPolicy)
	{
		this(backingList, selectPolicy, VPolicy.AS_NEEDED, HPolicy.AS_NEEDED);
	}

	/**
	 * Creates a new, empty RList.
	 * @param selectPolicy	selection policy for the list.
	 * @param vsbPolicy		the vertical scrollbar policy.
	 * @param hsbPolicy		the horizontal scrollbar policy.
	 */
	public RSortedMapList(SelectPolicy selectPolicy, VPolicy vsbPolicy, HPolicy hsbPolicy)
	{
		this(new SortedMap<>(), selectPolicy, vsbPolicy, hsbPolicy);
	}
	
	/**
	 * Creates a new RList.
	 * @param backingList	the backing list - items are read from and written to the list.
	 * @param selectPolicy	selection policy for the list.
	 * @param vsbPolicy		the vertical scrollbar policy.
	 * @param hsbPolicy		the horizontal scrollbar policy.
	 */
	public RSortedMapList(SortedMap<K, V> backingList, SelectPolicy selectPolicy, VPolicy vsbPolicy, HPolicy hsbPolicy)
	{
		this.dataModel = new RSortedMapListModel<>(backingList);
		this.list = new JList<ObjectPair<K, V>>(dataModel);
		
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
		
		list.setCellRenderer(new ListCellRenderer<ObjectPair<K, V>>()
		{
			private DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
			
			@Override
			public Component getListCellRendererComponent(JList<? extends ObjectPair<K, V>> list, ObjectPair<K, V> value, int index, boolean isSelected, boolean cellHasFocus)
			{
				JLabel renderer = (JLabel)defaultRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				renderer.setText(getItemString(value.getKey()));
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
	public void setItemRenderer(ListCellRenderer<ObjectPair<K, V>> cellRenderer)
	{
		list.setCellRenderer(cellRenderer);
	}
	
	/** 
	 * Sets the selected item in the list using the key.
	 * @param key the key of the item to select in the list. Can be null for no selection.
	 */
	public void setSelectedKey(K key)
	{
		if (key == null)
			list.clearSelection();
		else
			setSelectedIndex(dataModel.getIndexOf(key));
	}
	
	/** 
	 * Sets the selected item in the list using a value.
	 * The first matching value is selected.
	 * @param value the item to select in the list. Can be null for no selection.
	 */
	public void setSelectedValue(V value)
	{
		if (value == null)
			list.clearSelection();
		else
			setSelectedIndex(dataModel.getIndexOfValue(value));
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
	 * Gets the key of an item in the list.
	 * @param index the index to use.
	 * @return the key at a particular index in the list, or null if bad index.
	 */
	public K getKey(int index)
	{
		return dataModel.getKey(index);
	}
	
	/**
	 * Gets the value of an item in the list.
	 * @param index the index to use.
	 * @return the value at a particular index in the list, or null if bad index.
	 */
	public V getValue(int index)
	{
		return dataModel.getValue(index);
	}
	
	/**
	 * Returns the items between two particular indices in the list,
	 * exclusively.
	 */
	public Iterable<ObjectPair<K, V>> getItems(int index0, int index1)
	{
		List<ObjectPair<K, V>> alist = new List<ObjectPair<K, V>>(index1 - index0);
		for (int i = index0; i < index1; i++)
			alist.add(dataModel.getElementAt(i));
		return alist;
	}
	
	/**
	 * Adds an item to the list.
	 * If an item has the same key, it is replaced.
	 * @param key the item key.
	 * @param value the item value.
	 */
	public void addItem(K key, V value)
	{
		dataModel.replace(key, value);
		onAdd(dataModel.getElementAt(dataModel.getIndexOf(key)));
	}
	
	/**
	 * Removes an item from the list.
	 * @param key the item key.
	 * @return the corresponding value if removed, null if not.
	 */
	public V removeItem(K key)
	{
		int index = dataModel.getIndexOf(key);
		if (index < 0)
			return null;
		else
			return removeItem(index);
	}
	
	/**
	 * Removes an item from the list at a specific index.
	 * @param index the index from which to remove the object.
	 * @return the corresponding value if removed, null if not.
	 */
	public V removeItem(int index)
	{
		ObjectPair<K, V> pair = dataModel.getElementAt(index);
		dataModel.removeValueAtIndex(index);
		onRemove(pair);
		return pair.getValue();
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
	 * Returns the first selected key in the list.
	 * @return the object key or null if no object selected.
	 */
	public K getSelectedKey()
	{
		int s = getSelectedIndex();
		if (s < 0) return null;
		return dataModel.getKey(s);
	}
	
	/**
	 * Returns the first selected value in the list.
	 * @return the object value or null if no object selected.
	 */
	public V getSelectedValue()
	{
		int s = getSelectedIndex();
		if (s < 0) return null;
		return dataModel.getValue(s);
	}
	
	/**
	 * Returns the selected object keys in the list.
	 * @return the selected object keys.
	 */
	public Iterable<K> getAllSelectedKeys()
	{
		int[] s = getSelectedIndices();
		List<K> alist = new List<K>(s.length);
		for (int i : s)
			alist.add(getKey(i));
		return alist;
	}
	
	/**
	 * Returns the selected object keys in the list.
	 * @return the selected object keys.
	 */
	public Iterable<V> getAllSelectedValues()
	{
		int[] s = getSelectedIndices();
		List<V> alist = new List<V>(s.length);
		for (int i : s)
			alist.add(getValue(i));
		return alist;
	}
	
	/**
	 * @return all of the item keys in this list.
	 */
	public Iterable<K> getAllKeys()
	{
		List<K> alist = new List<K>(dataModel.getSize());
		for (ObjectPair<K, V> obj : dataModel)
			alist.add(obj.getKey());
		return alist;
	}
	
	/**
	 * @return all of the item values in this list.
	 */
	public Iterable<V> getAllValues()
	{
		List<V> alist = new List<V>(dataModel.getSize());
		for (ObjectPair<K, V> obj : dataModel)
			alist.add(obj.getValue());
		return alist;
	}
	
	/**
	 * @return all of the item values in this list.
	 */
	public Iterable<ObjectPair<K, V>> getAllItems()
	{
		List<ObjectPair<K, V>> alist = new List<ObjectPair<K, V>>(dataModel.getSize());
		for (ObjectPair<K, V> obj : dataModel)
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
	 * what to display as a string. By default this is the key's <code>toString()</code>.
	 * @return the string to use. Should not return null.
	 */
	public String getItemString(K key)
	{
		return key.toString();
	}
	
	/**
	 * @return this list's data model.
	 */
	protected RSortedMapListModel<K, V> getDataModel()
	{
		return dataModel;
	}
	
	@Override
	public void onAdd(ObjectPair<K, V> object)
	{
		// Do nothing.
	}

	@Override
	public void onRemove(ObjectPair<K, V> object)
	{
		// Do nothing.
	}

	@Override
	public void onSelect()
	{
		// Do nothing.
	}

}
