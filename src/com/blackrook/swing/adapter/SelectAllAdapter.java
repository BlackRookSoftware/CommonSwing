/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.adapter;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.text.JTextComponent;

/**
 * A special focus adapter that selects all text on focus {@link JTextComponent}s.
 * If this is attached to a non-JTextComponent object, this does nothing.
 * @author Matthew Tropiano
 * @since 2.1.0
 */
public class SelectAllAdapter extends FocusAdapter
{
	@Override
	public void focusGained(FocusEvent e)
	{
		Component c = e.getComponent();
		if (c instanceof JTextComponent)
			((JTextComponent)c).selectAll();
	}
}
