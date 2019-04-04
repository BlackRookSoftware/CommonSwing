/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import com.blackrook.commons.list.List;

/**
 * Combo box field class.
 * @author Matthew Tropiano
 */
public class RComboField<T extends Object> extends RInputFieldPanel<JComboBox<T>, T>
	implements RFocusEventListener, RChangeEventListener
{
	private static final long serialVersionUID = -757291379482826048L;

	/**
	 * Centralized change handler for all instances of this field.
	 */
	private static class ChangeHandler implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent e)
		{
			RComboField<?> field = (RComboField<?>)(((JComboBox<?>)e.getSource()).getParent());
			if (e.getStateChange() == ItemEvent.SELECTED)
				field.onChange();
		}
	}
	
	/**
	 * Centralized focus handler for all instances of this field.
	 */
	private static class FocusHandler implements FocusListener
	{
		@Override
		public void focusGained(FocusEvent e)
		{
			RComboField<?> field = (RComboField<?>)(e.getComponent().getParent());
			field.onFocus();
		}

		@Override
		public void focusLost(FocusEvent e)
		{
			RComboField<?> field = (RComboField<?>)(e.getComponent().getParent());
			field.onBlur();
		}
	}
	
	/** The key handler for all instances of this field. */
	protected static final ChangeHandler STATIC_CHANGEHANDLER = new ChangeHandler();
	/** The focus handler for all instances of this field. */
	protected static final FocusHandler STATIC_FOCUSHANDLER = new FocusHandler();
	
	/**
	 * Creates a new RComboField.
	 * @param label			the label of this field.
	 * @param labelSpacing	the width of the label area.
	 * @param objects		the list of objects that are on this combo box.
	 */
	@SafeVarargs
	public RComboField(String label, int labelSpacing, T ... objects)
	{
		super(label, new JComboBox<T>(), labelSpacing);
		if (objects != null) for (T obj : objects)
			field.addItem(obj);
		field.addItemListener(STATIC_CHANGEHANDLER);
		field.addFocusListener(STATIC_FOCUSHANDLER);
	}

	/**
	 * Creates a new RComboField.
	 * @param label			the label of this field.
	 * @param labelSpacing	the width of the label area.
	 * @param comboBox		the combo box to encapsulate.
	 */
	public RComboField(String label, int labelSpacing, JComboBox<T> comboBox)
	{
		super(label, comboBox, labelSpacing);
		field.addItemListener(STATIC_CHANGEHANDLER);
		field.addFocusListener(STATIC_FOCUSHANDLER);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T getValue()
	{
		return (T)field.getSelectedItem();
	}

	@Override
	public void setValue(T value)
	{
		field.setSelectedItem(value);
	}

	/**
	 * Returns the selected index for this field.
	 * @since 2.1.0
	 */
	public int getSelectedIndex()
	{
		return field.getSelectedIndex();
	}

	/**
	 * Sets the selected index for this field.
	 * @throws IllegalArgumentException if index < -1 or index is greater than or equal to number of elements.
	 * @since 2.1.0
	 */
	public void setSelectedIndex(int index)
	{
		field.setSelectedIndex(index);
	}

	/**
	 * Adds an item to this combobox.
	 * @since 2.5.1
	 */
	public void addItem(T item)
	{
		field.addItem(item);
	}

	/**
	 * Returns true if this contains a particular item.
	 * @since 2.5.1
	 */
	public boolean containsItem(T item)
	{
		for (int i = 0; i < field.getItemCount(); i++)
			if (field.getItemAt(i).equals(item))
				return true;
		return false;
	}

	/**
	 * Returns all items in this combobox.
	 * @since 2.5.1
	 */
	@SuppressWarnings("unchecked")
	public T[] getAllItems()
	{
		List<T> outList = new List<T>(field.getItemCount());
		for (int i = 0; i < field.getItemCount(); i++)
			outList.add((T)field.getItemAt(i));
		
		T[] out = (T[])new Object[field.getItemCount()];
		outList.toArray(out);
		return out;
	}

	/**
	 * Removes all items from this combobox.
	 * @since 2.5.1
	 */
	public void clearAllItems()
	{
		field.removeAllItems();
	}

	@Override
	public void onChange()
	{
		// Do nothing.
	}
	
	@Override
	public void onFocus()
	{
		// Do nothing.
	}
	
	@Override
	public void onBlur()
	{
		// Do nothing.
	}
	
}
