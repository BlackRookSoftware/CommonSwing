/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * Input field interface used for the Black Rook Swing input components.
 * @author Matthew Tropiano
 */
public interface RInputField<T extends JComponent, V extends Object>
{
	/**
	 * Returns the reference to the encapsulated input field label. 
	 */
	public JLabel getLabel();

	/**
	 * Returns the reference to the encapsulated input field component. 
	 */
	public T getInputComponent();

	/**
	 * Returns the field's value. 
	 */
	public V getValue();
	
	/**
	 * Sets the field's value. 
	 */
	public void setValue(V value);
	
}
