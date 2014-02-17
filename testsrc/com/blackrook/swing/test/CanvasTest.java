/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

import javax.swing.JFrame;

import com.blackrook.commons.Ticker;
import com.blackrook.swing.SwingCommon;
import com.blackrook.swing.canvas.BufferedCanvas;

public class CanvasTest
{
	public static void main(String[] args)
	{
		final Color[] COLORS = {Color.WHITE, Color.BLUE, Color.RED, Color.GREEN, Color.BLACK};
		final BufferedCanvas canvas = new BufferedCanvas();
		JFrame f = SwingCommon.createSimpleFrame("Test", canvas);
		f.setSize(new Dimension(512, 384));
		f.setVisible(true);
		new Ticker(5)
		{
			@Override
			public void doTick(long tick)
			{
				Color c = COLORS[(int)(tick % COLORS.length)];
				Graphics2D g = canvas.startFrame();
				g.setColor(c);
				g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
				g.dispose();
				canvas.commit();
			}
		}.start();
	}
}
