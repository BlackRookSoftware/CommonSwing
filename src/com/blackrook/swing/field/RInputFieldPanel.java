/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A skeleton class for encapsulating laid-out input field/label combos.
 * @author Matthew Tropiano
 * @param <T>	a Java Swing component to encapsulate.
 */
public abstract class RInputFieldPanel<T extends JComponent, V extends Object> extends JPanel implements RInputField<T, V>
{
	private static final long serialVersionUID = -5338417283541766196L;

	/** The label of this field. */
	protected JLabel fieldLabel;
	/** The actual field that contains the user input. */
	protected T field;

	/**
	 * Constructs a new text field with a label name (placed to the left of the
	 * text field) and the field next to the label. If the label name for the 
	 * field is <b>null</b>, then the label will be blank. 
	 * @param labelName			this field's label.
	 * @param field				the field to add.
	 */
	public RInputFieldPanel(String labelName, T field)
	{
		this(labelName, field, -1);
	}
	
	/**
	 * Constructs a new text field with a label name (placed to the left of the
	 * text field) and the field next to the label. If the label name for the 
	 * field is <b>null</b>, then the label will be blank. 
	 * @param labelName				this field's label.
	 * @param field					the field to add.
	 * @param labelSpacing			the width of the label (for consistent spacing). -1 = don't adjust.
	 * @throws IllegalArgumentException if any of the "spacing" parameters are less than 0.
	 */
	public RInputFieldPanel(String labelName, T field, int labelSpacing)
	{
		if (labelSpacing < -1)
			throw new IllegalArgumentException("Spacing parameters cannot be less than -1.");
		
		fieldLabel = new JLabel(labelName);
		this.field = field; 
		
		if (labelSpacing > -1)
			fieldLabel.setPreferredSize(
					new Dimension(labelSpacing,fieldLabel.getPreferredSize().height));

		setLayout(new BorderLayout());
		add(fieldLabel,BorderLayout.WEST);
		add(field,BorderLayout.CENTER);
	}

	@Override
	public JLabel getLabel()
	{
		return fieldLabel;
	}

	@Override
	public T getInputComponent()
	{
		return field;
	}

	/**
	 * Returns this object's value as a string.
	 * Equivalent to <code>getValue().toString()</code>.
	 */
	public String getStringValue()
	{
		return getValue() != null ? String.valueOf(getValue()) : "";
	}
	
	@Override
	public void requestFocus()
	{
		field.requestFocus();
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		field.setEnabled(enabled);
	}

}
