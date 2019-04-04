/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.canvas;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A special canvas that uses a graphics context to write to it.
 * The user calls {@link #startFrame()} to start the next frame.
 * @author Matthew Tropiano
 * @since 2.5.0
 */
public class BufferedCanvas extends Canvas
{
	private static final long serialVersionUID = 7006646282633167280L;

	/**
	 * The image buffer to write to for the next frame.
	 * Can be null.
	 */
	private BufferedImage currentBuffer;

	/**
	 * The image buffer to write.
	 */
	private BufferedImage paintBuffer;
	
	/**
	 * Creates a new buffered canvas.
	 */
	public BufferedCanvas()
	{
		currentBuffer = null;
	}
	
	/**
	 * Generates a new image buffer for writing and returns a {@link Graphics2D} context
	 * for updating the contents. Any uncommitted data is discarded.
	 * @return the {@link Graphics2D} context to manipulate.
	 */
	public Graphics2D startFrame()
	{
		currentBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		return (Graphics2D)currentBuffer.getGraphics();
	}
	
	/**
	 * Returns a {@link Graphics2D} context for updating the contents of
	 * an already started buffer with {@link #startFrame()}. If {@link #startFrame()}
	 * was not called yet, this will return null.
	 * @return the {@link Graphics2D} context to manipulate, or null if 
	 */
	public Graphics2D continueFrame()
	{
		return currentBuffer != null ? (Graphics2D)currentBuffer.getGraphics() : null;
	}
	
	/**
	 * Commits the new frame to the canvas.
	 */
	public void commit()
	{
		paintBuffer = currentBuffer;
		currentBuffer = null;
		repaint();
	}
	
	@Override
	public void update(Graphics g)
	{
		if (paintBuffer != null)
		{
			((Graphics2D)g).drawImage(paintBuffer, null, 0, 0);
			paintBuffer = null;
		}
	}
	
}
