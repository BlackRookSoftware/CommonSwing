/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.test;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import com.blackrook.swing.SwingCommon;
import com.blackrook.swing.field.RFileField;

public final class SwingTest
{
	public static void main(String[] args)
	{
		SwingCommon.setExceptionHandler();
		SwingCommon.setSystemLAF();
		SwingCommon.createSimpleFrame("Test", createTestPanel()).setVisible(true);
	}
	
	// Change this!
	private static JPanel createTestPanel()
	{
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(new RFileField("File", 64));
		return p;
	}
	
}
