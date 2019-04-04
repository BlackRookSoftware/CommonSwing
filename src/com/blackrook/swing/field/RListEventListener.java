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
 * methods on value additions or removals.
 * @since 1.4.0
 * @author Matthew Tropiano
 */
public interface RListEventListener<T extends Object>
{

	/** 
	 * Called when a value is added to the list.
	 * @param object the added object.
	 */
	public void onAdd(T object);

	/** 
	 * Called when a value is removed from the list.
	 * @param object the removed object.
	 */
	public void onRemove(T object);

	/**
	 * Called when a value is (or values are) selected from the list,
	 * or if a selection changes.
	 */
	public void onSelect();
	
}
