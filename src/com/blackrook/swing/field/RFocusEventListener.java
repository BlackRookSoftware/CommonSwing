/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

/**
 * All implementors of this class call the contained
 * methods on focus changes.
 * @author Matthew Tropiano
 */
public interface RFocusEventListener
{

	/** 
	 * Called when a this component gains focus.
	 */
	public void onFocus();
	
	/** 
	 * Called when a this component loses focus.
	 */
	public void onBlur();

}
