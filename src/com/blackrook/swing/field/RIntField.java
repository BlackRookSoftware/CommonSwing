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
 * A label-plus-field panel for changing integer numeric data.
 * @author Matthew Tropiano
 */
public class RIntField extends RTextField<Integer>
{
	private static final long serialVersionUID = 6944530059158019035L;

	/**
	 * Creates a new RIntField.
	 * @param label			the label of this field.
	 * @param labelSpacing	the width of the label area.
	 */
	public RIntField(String label, int labelSpacing)
	{
		super(label, labelSpacing);
		setValue(0);
	}

	@Override
	public void checkValue()
	{
		try {
			// this will truncate unnecessary digits
			setValue(Integer.parseInt(field.getText())); 
		} catch (NumberFormatException e) {
			setValue(0);
		}
	}
	
	@Override
	public Integer getValue()
	{
		return new Integer(field.getText());
	}
	
	@Override
	public void setStringValue(String value)
	{
		setValue(Common.parseInt(value, 0));
	}
	
}
