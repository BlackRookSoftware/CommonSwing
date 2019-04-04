/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

/**
 * All implementors of this class call the contained
 * methods on key events.
 * @since 1.3.0
 * @author Matthew Tropiano
 */
public interface RKeyEventListener
{

	/** 
	 * Called when a key is pressed on this component.
	 * @param keyCode	the KeyEvent keycode that was pressed.
	 */
	public void onKeyPress(int keyCode);

	/** 
	 * Called when a key is released on this component.
	 * @param keyCode	the KeyEvent keycode that was released.
	 */
	public void onKeyRelease(int keyCode);

	/** 
	 * Called when a key is typed on this component.
	 */
	public void onKeyType();

}
