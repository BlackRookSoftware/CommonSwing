/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import com.blackrook.commons.util.ValueUtils;

/**
 * A label-plus-field panel for changing numeric data.
 * @author Matthew Tropiano
 */
public class RDoubleField extends RTextField<Double>
{
	private static final long serialVersionUID = 5809346425301844586L;

	/**
	 * Creates a new RDoubleField.
	 * @param label			the label of this field.
	 * @param labelSpacing	the width of the label area.
	 */
	public RDoubleField(String label, int labelSpacing)
	{
		super(label, labelSpacing);
		setValue(0d);
	}

	@Override
	public void checkValue()
	{
		try {
			// this will truncate unnecessary digits
			setValue(Double.parseDouble(field.getText())); 
		} catch (NumberFormatException e) {
			setValue(0d);
		}
	}
	
	@Override
	public Double getValue()
	{
		return Double.valueOf(field.getText());
	}

	@Override
	public void setStringValue(String value)
	{
		setValue(ValueUtils.parseDouble(value, 0.0));
	}

}
