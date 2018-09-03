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
 * A label-plus-field panel for changing 32-bit numeric data.
 * @author Matthew Tropiano
 */
public class RFloatField extends RTextField<Float>
{
	private static final long serialVersionUID = -4428129609588286514L;

	/**
	 * Creates a new RFloatField.
	 * @param label			the label of this field.
	 * @param labelSpacing	the width of the label area.
	 */
	public RFloatField(String label, int labelSpacing)
	{
		super(label, labelSpacing);
		setValue(0f);
	}

	@Override
	public void checkValue()
	{
		try {
			// this will truncate unnecessary digits
			setValue(Float.parseFloat(field.getText())); 
		} catch (NumberFormatException e) {
			setValue(0f);
		}
	}
	
	@Override
	public Float getValue()
	{
		return Float.valueOf(field.getText());
	}

	@Override
	public void setStringValue(String value)
	{
		setValue(Common.parseFloat(value, 0f));
	}

}
