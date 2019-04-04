/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A skeleton class for encapsulating laid-out input field/label combos
 * that requires an input panel for the main interface. 
 * @author Matthew Tropiano
 */
public abstract class RInputFieldAbstract<V extends Object> extends JPanel implements RInputField<JPanel, V>
{
	private static final long serialVersionUID = -5338417283541766196L;

	/** The label of this field. */
	protected JLabel fieldLabel;
	/** The actual field that contains the user input. */
	protected JPanel field;

	/**
	 * Constructs a new text field with a label name (placed to the left of the
	 * text field) and the field next to the label. If the label name for the 
	 * field is <b>null</b>, then the label will be blank. 
	 * @param labelName			this field's label.
	 */
	public RInputFieldAbstract(String labelName)
	{
		this(labelName, -1);
	}
	
	/**
	 * Constructs a new text field with a label name (placed to the left of the
	 * text field) and the field next to the label. If the label name for the 
	 * field is <b>null</b>, then the label will be blank. 
	 * @param labelName				this field's label.
	 * @param labelSpacing			the width of the label (for consistent spacing). -1 = don't adjust.
	 * @throws IllegalArgumentException if any of the "spacing" parameters are less than 0.
	 */
	public RInputFieldAbstract(String labelName, int labelSpacing)
	{
		if (labelSpacing < -1)
			throw new IllegalArgumentException("Spacing parameters cannot be less than -1.");
		
		fieldLabel = new JLabel(labelName);
		this.field = new JPanel(); 
		
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

	/**
	 * Returns the reference to the encapsulated panel. 
	 */
	@Override
	public JPanel getInputComponent()
	{
		return field;
	}

	/**
	 * Returns this object's value as a string.
	 * Equivalent to <code>getValue().toString()</code>.
	 */
	public String getStringValue()
	{
		return getValue().toString();
	}
	
	@Override
	public void requestFocus()
	{
		field.requestFocus();
	}

}
