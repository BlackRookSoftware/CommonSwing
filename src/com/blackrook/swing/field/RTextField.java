/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

/**
 * Text field class.
 * @author Matthew Tropiano
 */
public abstract class RTextField<V extends Object> extends RInputFieldPanel<JTextField, V> 
	implements RKeyEventListener, RFocusEventListener, RChangeEventListener
{
	private static final long serialVersionUID = 4861993780575951587L;
	
	/**
	 * Centralized key handler for all instances of this field.
	 */
	private static class KeyHandler implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			RTextField<?> field = (RTextField<?>)(e.getComponent().getParent());
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				field.checkValue();
				field.checkChange();
			}
			else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				field.resetValue();
			field.onKeyPress(e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			((RTextField<?>)(e.getComponent().getParent())).onKeyRelease(e.getKeyCode());
		}

		@Override
		public void keyTyped(KeyEvent e)
		{
			((RTextField<?>)(e.getComponent().getParent())).onKeyType();
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
			RTextField<?> field = (RTextField<?>)(e.getComponent().getParent());
			field.saveLastValue();
			field.onFocus();
		}

		@Override
		public void focusLost(FocusEvent e)
		{
			RTextField<?> field = (RTextField<?>)(e.getComponent().getParent());
			field.onBlur();
			field.checkValue();
			field.checkChange();
		}
	}
	
	/** The key handler for all instances of this field. */
	protected static final KeyHandler STATIC_KEYHANDLER = new KeyHandler();
	/** The focus handler for all instances of this field. */
	protected static final FocusHandler STATIC_FOCUSHANDLER = new FocusHandler();
	
	/** The last value used by this object. */
	private V lastValue;
	
	/**
	 * Creates a new RTextField.
	 * @param label				the label of this field.
	 * @param labelSpacing		the width of the label area.
	 */
	public RTextField(String label, int labelSpacing)
	{
		this(label, labelSpacing, new JTextField());
	}
	
	/**
	 * Creates a new RTextField.
	 * @param label			the label of this field.
	 * @param labelSpacing	the width of the label area.
	 * @param textField		the text field to encapsulate.
	 */
	public RTextField(String label, int labelSpacing, JTextField textField)
	{
		super(label, textField, labelSpacing);
		field.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		field.addKeyListener(STATIC_KEYHANDLER);
		field.addFocusListener(STATIC_FOCUSHANDLER);
	}
	
	@Override
	public abstract V getValue();

	@Override
	public void setValue(V value)
	{
		field.setText(String.valueOf(value));
	}

	/**
	 * Sets the value on this field using a string.
	 * The value should be parsed by the implementing field,
	 * and call {@link #setValue(Object)} to set it.
	 * @since 2.5.4
	 */
	public abstract void setStringValue(String value); 
	
	/** Sets the last value to the current value. */
	protected void saveLastValue()
	{
		lastValue = getValue();
	}
	
	/** Sets the current value to the last value. */
	protected void resetValue()
	{
		getInputComponent().setText(String.valueOf(lastValue));
	}
	
	/**
	 * Checks and corrects this value before it actually registers as "changed".
	 */
	protected void checkValue()
	{
	}

	/**
	 * Checks to see if this field's data has changed.
	 */
	protected void checkChange()
	{
		if (!getValue().equals(lastValue))
			onChange();
		saveLastValue();
	}

	@Override
	public void onChange()
	{
		// Do nothing.
	}

	@Override
	public void onKeyPress(int keyCode)
	{
		// Do nothing.
	}
	
	@Override
	public void onKeyRelease(int keyCode)
	{
		// Do nothing.
	}
	
	@Override
	public void onKeyType()
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
