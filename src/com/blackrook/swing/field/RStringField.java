/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

/**
 * A label-plus-field panel for changing string data.
 * @author Matthew Tropiano
 */
public class RStringField extends RTextField<String>
{
	private static final long serialVersionUID = 3702932926246453380L;

	/**
	 * Creates a new RStringField.
	 * @param label			the label of this field.
	 * @param labelSpacing	the width of the label area.
	 */
	public RStringField(String label, int labelSpacing)
	{
		super(label, labelSpacing);
		setValue("");
	}

	@Override
	public void checkValue()
	{
		// Do nothing. It's already a string.
	}
	
	@Override
	public String getValue()
	{
		return field.getText();
	}

	@Override
	public void setStringValue(String value)
	{
		setValue(value);
	}
	
}
