/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * A button that has an entry point for clicks, which should be overridden.
 * @author Matthew Tropiano
 */
public class RButton extends JButton
{
	private static final long serialVersionUID = -1285767467128672215L;

	/**
	 * The action for the button.
	 */
	public static class ClickAction extends AbstractAction
	{
		private static final long serialVersionUID = 2442971411787555049L;

		@Override
		public void actionPerformed(ActionEvent e)
		{
			RButton button = (RButton)e.getSource();
			button.onClick();
		}
	}

	/** The handler added to all RButtons. */
	private static ClickAction CLICK_HANDLER = new ClickAction();
	
	/**
	 * Creates a new blank RButton.
	 */
	public RButton()
	{
		this(null, null);
	}
	
	/**
	 * Creates a new blank RButton.
	 */
	public RButton(String name)
	{
		this(name, null);
	}
	
	/**
	 * Creates a new blank RButton.
	 */
	public RButton(Icon icon)
	{
		this(null, icon);
	}
	
	/**
	 * Creates a new blank RButton.
	 * @param name	the name of the button.
	 * @param icon	the icon on the button.
	 */
	public RButton(String name, Icon icon)
	{
		super(name, icon);
		addActionListener(CLICK_HANDLER);
	}
	
	/**
	 * Called when the button is clicked.
	 */
	public void onClick()
	{
		// Do nothing.
	}
	
}
