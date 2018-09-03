/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import com.blackrook.commons.Common;

/**
 * A label-plus-field panel for changing short (16-bit) integer numeric data.
 * @author Matthew Tropiano
 */
public class RShortField extends RTextField<Short>
{
	private static final long serialVersionUID = -1674234775293586109L;

	/**
	 * Creates a new RShortField.
	 * @param label			the label of this field.
	 * @param labelSpacing	the width of the label area.
	 */
	public RShortField(String label, int labelSpacing)
	{
		super(label, labelSpacing);
		setValue((short)0);
	}

	@Override
	public void checkValue()
	{
		try {
			Short.parseShort(field.getText()); 
		} catch (NumberFormatException e) {
			setValue((short)0);
		}
	}
	
	@Override
	public Short getValue()
	{
		return Short.valueOf(field.getText());
	}
	
	@Override
	public void setStringValue(String value)
	{
		setValue(Common.parseShort(value, (short)0));
	}
	
}
