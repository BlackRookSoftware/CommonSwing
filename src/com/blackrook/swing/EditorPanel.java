/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing;

import javax.swing.JPanel;

/**
 * This is an abstract panel type that assists in
 * providing skeleton functions for "viewer" or "editor" functions.
 * <p>
 * It also provides a set of "listener" abstract method for when the object
 * to display has been changed, or to inquire if the object's contents have changed
 * inside the editor.
 * @author Matthew Tropiano
 */
public abstract class EditorPanel<T extends Object> extends JPanel
{
	private static final long serialVersionUID = -8112290206641370480L;
	
	/** The object being "viewed" by the panel. */
	private T object;
	/** Flag for if the object had been changed by the user of the panel. */
	private boolean hasChanged;

	/**
	 * Common constructor.
	 */
	protected EditorPanel()
	{
		object = null;
		hasChanged = false; 
	}
	
	/**
	 * Sets the reference to object viewed by this panel. 
	 * @param obj	the object reference to set.	
	 */
	public void setObject(T obj)
	{
		if (!hasChanged || (hasChanged && allowObjectChange()))
		{
			object = obj;
			onObjectChange();
		}
	}
	
	/**
	 * Gets the reference to object viewed by this panel. 
	 */
	public T getObject()
	{
		return object;
	}
	
	/**
	 * Called by setObject() if the "hasChanged" flag has been set.
	 * @return true to allow the object to be changed, false if not.
	 */
	protected abstract boolean allowObjectChange();
	
	/**
	 * Called by setObject() after it has changed the object of interest to
	 * this EditorPanel. This should perform the necessary tasks to refresh
	 * the view.
	 */
	protected abstract void onObjectChange();
	
	/**
	 * Sets if the object of interest's contents have changed.
	 */
	protected void setChanged(boolean changed)
	{
		hasChanged = changed;
	}
	
}
