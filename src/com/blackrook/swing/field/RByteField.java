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
 * A label-plus-field panel for changing byte-sized (8-bit) integer numeric data.
 * @author Matthew Tropiano
 */
public class RByteField extends RTextField<Byte>
{
	private static final long serialVersionUID = -8372137678067753530L;

	/**
	 * Creates a new RByteField.
	 * @param label			the label of this field.
	 * @param labelSpacing	the width of the label area.
	 */
	public RByteField(String label, int labelSpacing)
	{
		super(label, labelSpacing);
		setValue((byte)0);
	}

	@Override
	public void checkValue()
	{
		try {
			// this will truncate unnecessary digits
			setValue(Byte.parseByte(field.getText())); 
		} catch (NumberFormatException e) {
			setValue((byte)0);
		}
	}
	
	@Override
	public Byte getValue()
	{
		return Byte.valueOf(field.getText());
	}
	
	@Override
	public void setStringValue(String value)
	{
		setValue(Common.parseByte(value, (byte)0));
	}
	
}
