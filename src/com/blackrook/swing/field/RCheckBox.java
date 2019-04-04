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

import javax.swing.JCheckBox;

/**
 * A button that has an entry point for clicks, which should be overridden.
 * @author Matthew Tropiano
 * @since 2.5.2 is a RInputFieldAbstract.
 */
public class RCheckBox extends RInputFieldPanel<JCheckBox, Boolean> 
	implements RFocusEventListener, RChangeEventListener
{
	private static final long serialVersionUID = -1285767467128672215L;

	/**
	 * The action for this and all boxes.
	 */
	public static class ChangeHandler implements ItemListener
	{
		@Override
		public void itemStateChanged(ItemEvent e)
		{
			RCheckBox box = (RCheckBox)(((JCheckBox)e.getSource()).getParent());
			box.onChange();
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
			RCheckBox field = (RCheckBox)(e.getComponent().getParent());
			field.onFocus();
		}

		@Override
		public void focusLost(FocusEvent e)
		{
			RCheckBox field = (RCheckBox)(e.getComponent().getParent());
			field.onBlur();
		}
	}
	
	/** The handler added to all CheckBoxes. */
	private static final ChangeHandler CHANGE_HANDLER = new ChangeHandler();
	/** The handler added to all CheckBoxes. */
	private static final FocusHandler FOCUS_HANDLER = new FocusHandler();
	
	/**
	 * Creates a new check box with label.
	 * @param labelName the label of this field.
	 */
	public RCheckBox(String labelName)
	{
		this(labelName, -1);
	}
	
	/**
	 * Creates a new check box with label.
	 * @param labelName the label of this field.
	 * @param labelSpacing the width of the label area.
	 * @throws IllegalArgumentException if any of the "spacing" parameters are less than -1.
	 */
	public RCheckBox(String labelName, int labelSpacing)
	{
		super(labelName, new JCheckBox(), labelSpacing);
		field.addItemListener(CHANGE_HANDLER);
		field.addFocusListener(FOCUS_HANDLER);
	}
	
	@Override
	public Boolean getValue()
	{
		return field.isSelected();
	}

	@Override
	public void setValue(Boolean value)
	{
		field.setSelected(value);
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
