/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.border.BevelBorder;

/**
 * Editable combo box field class.
 * @author Matthew Tropiano
 */
public class REditableComboField extends RComboField<String>
	implements RFocusEventListener, RChangeEventListener
{
	private static final long serialVersionUID = -866477559249504940L;

	private static class ActionHandler implements ActionListener
	{
		@Override
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e)
		{
			REditableComboField field = (REditableComboField)(((JComboBox<String>)e.getSource()).getParent());
			if (!field.containsItem(field.getValue()))
				field.addItem(field.getValue());
		}
	}

	/** The key handler for all instances of this field. */
	protected static final ActionHandler STATIC_ACTIONHANDLER = new ActionHandler();
	
	/**
	 * Creates a new REditableComboField.
	 * @param label			the label of this field.
	 * @param labelSpacing	the width of the label area.
	 */
	public REditableComboField(String label, int labelSpacing)
	{
		this(label, labelSpacing, null);
	}
	
	/**
	 * Creates a new REditableComboField.
	 * @param label			the label of this field.
	 * @param labelSpacing	the width of the label area.
	 * @param objects		the list of objects that are on this combo box.
	 */
	public REditableComboField(String label, int labelSpacing, String[] objects)
	{
		super(label, labelSpacing, objects);
		field.addActionListener(STATIC_ACTIONHANDLER);
		field.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		field.setEditable(true);
	}
	
	/**
	 * Adds an item to this combobox.
	 */
	public void addItem(String item)
	{
		field.addItem(item);
	}

	/**
	 * Returns true if this contains a particular item.
	 */
	public boolean containsItem(String item)
	{
		for (int i = 0; i < field.getItemCount(); i++)
			if (field.getItemAt(i).equals(item))
				return true;
		return false;
	}

	/**
	 * Returns all items in this combobox.
	 */
	public String[] getAllItems()
	{
		String[] out = new String[field.getItemCount()];
		for (int i = 0; i < field.getItemCount(); i++)
			out[i] = (String)field.getItemAt(i);
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

}
