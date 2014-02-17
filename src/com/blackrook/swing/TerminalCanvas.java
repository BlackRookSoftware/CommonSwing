/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing;

import javax.swing.*;

import com.blackrook.commons.math.RMath;

import java.awt.*;

/**
 * A wonderful text-based canvas. 
 * @author Matthew Tropiano
 */
public class TerminalCanvas extends JTextArea
{
	private static final long serialVersionUID = 2478520066968011556L;

	public static final int
	LAYER_FOREGROUND = 0,
	LAYER_BACKGROUND = 1,
	NUM_COLOR_LAYERS = 2;

	public static final char
	BOX_H = '\u2500',
	BOX_V = '\u2502',
	BOX_UL = '\u250C',
	BOX_UR = '\u2510',
	BOX_DL = '\u2514',
	BOX_DR = '\u2518';
	

	/** The display buffer for the canvas. */
	char[] charDisplay;
	/** The current row to write stuff to. */
	int currRow;
	/** The current column to write stuff to. */
	int currColumn;
	/** Number of rows. */
	int rows;
	/** Number of columns. */
	int cols;
	
	/**
	 * Creates a new Terminal Canvas. 
	 * @param rows	the amount of rows.
	 * @param cols	the amount of columns.
	 */
	public TerminalCanvas(int rows, int cols)
	{
		this.rows = rows;
		this.cols = cols;
		currRow = 0;
		currColumn = 0;
		Font f = new Font("Courier New",Font.PLAIN,12);
		setFont(f);
		setRows(rows);
		setColumns(cols);
		setWrapStyleWord(false);
		setLineWrap(true);
		charDisplay = new char[rows*cols];
		clearScreen();
	}

	/**
	 * Prints a string to the screen from the current cursor position.
	 * @param s		the string.
	 */
	public void print(String s)
	{
		s = s.replace('\n',' ');
		writeChars(s.toCharArray(),currRow,currColumn);
		paintDisplay();
	}

	/**
	 * Increments cursor column position. "cols" can be negative.
	 */
	protected void incCol(int cols)
	{
		currColumn = RMath.clampValue(cols+currColumn,0,this.cols);
	}
	
	/**
	 * Increments cursor row position. "rows" can be negative.
	 */
	protected void incRow(int rows)
	{
		currRow = RMath.clampValue(cols+currRow,0,this.rows);
	}

	/**
	 * Prints a string centered on a row. 
	 * @param s		the string.
	 * @param row	the row to print on.
	 */
	public void printCenteredText(String s, int row)
	{
		s = s.replace('\n',' ');
		int startcol = (cols/2)-(s.length()/2);
		writeChars(s.toCharArray(),row,startcol);
		paintDisplay();
	}

	/**
	 * Prints a string right-aligned to a row. 
	 * @param s		the string.
	 * @param row	the row to print on.
	 */
	public void printRightText(String s, int row)
	{
		s = s.replace('\n',' ');
		int startcol = cols-s.length();
		writeChars(s.toCharArray(),row,startcol);
		paintDisplay();
	}
	
	/**
	 * Prints a string left-aligned to a row. 
	 * @param s		the string.
	 * @param row	the row to print on.
	 */
	public void printLeftText(String s, int row)
	{
		s = s.replace('\n',' ');
		writeChars(s.toCharArray(),row,0);
		paintDisplay();
	}

	/**
	 * Sets the position where the next character will be written.
	 */
	public void setPos(int row, int col)
	{
		currRow = RMath.clampValue(row,0,rows);
		currColumn = RMath.clampValue(col,0,cols);
	}
	
	/**
	 * Clears the canvas.
	 */
	public void clearScreen()
	{
		for (int i = 0; i < charDisplay.length; i++)
			charDisplay[i] = 32;
		paintDisplay();
	}
	
	/**
	 * Prints a solid box to the screen from the current write position.
	 * @param width			the width of the box.
	 * @param height		the height of the box.
	 * @param borderChar	the character to draw the border with.
	 * @param fillChar		the character to fill the box with.
	 */
	public void printBox(int width, int height, char borderChar, char fillChar)
	{
		if (width == 0 || height == 0) return;
		char[][] boxLayers = new char[2][width];
		for (int i = 0; i < width; i++)
			boxLayers[0][i] = borderChar;
		boxLayers[1][0] = borderChar;
		boxLayers[1][width-1] = borderChar;
		for (int i = 1; i < width-1; i++)
			boxLayers[1][i] = fillChar;
		
		writeChars(boxLayers[0],currRow,currColumn);
		for (int i = 1; i < height-1; i++)
			writeChars(boxLayers[1],currRow+i,currColumn);
		writeChars(boxLayers[0],currRow+height-1,currColumn);
		
		paintDisplay();
	}
	
	/**
	 * Prints a solid outline border box to the screen from the current write position.
	 * @param width			the width of the box.
	 * @param height		the height of the box.
	 * @param fillChar		the character to fill the box with.
	 */
	public void printOutlineBox(int width, int height, char fillChar)
	{
		if (width == 0 || height == 0) return;
		char[][] boxLayers = new char[3][width];

		boxLayers[0][0] = BOX_UL;
		boxLayers[0][width-1] = BOX_UR;
		for (int i = 1; i < width-1; i++)
			boxLayers[0][i] = BOX_H;

		boxLayers[2][0] = BOX_DL;
		boxLayers[2][width-1] = BOX_DR;
		for (int i = 1; i < width-1; i++)
			boxLayers[2][i] = BOX_H;

		boxLayers[1][0] = BOX_V;
		boxLayers[1][width-1] = BOX_V;
		for (int i = 1; i < width-1; i++)
			boxLayers[1][i] = fillChar;

		writeChars(boxLayers[0],currRow,currColumn);
		for (int i = 1; i < height-1; i++)
			writeChars(boxLayers[1],currRow+i,currColumn);
		writeChars(boxLayers[2],currRow+height-1,currColumn);

		paintDisplay();
	}
	
	/**
	 * Writes a series of characters to the terminal screen buffer.
	 * @param c		the array of characters.
	 * @param row	the starting row.
	 * @param col	the starting column.
	 */
	public void writeChars(char[] c, int row, int col)
	{
		row = Math.min(row,rows-1);
		col = Math.min(col,cols-1);
		int len = c.length;
		System.arraycopy(c,0,
				charDisplay,getIndex(row,col),
				col+len>=cols?cols-col:len);
	}
	
	/**
	 * Updates the contents of the canvas.
	 */
	protected synchronized void paintDisplay()
	{
		setText(new String(charDisplay));		
	}
	
	/**
	 * Returns the actual char position of a row and column.
	 * @param row	the row number. 0 is the topmost.
	 * @param col	the column number. 0 is the leftmost.
	 * @return		the actual char position of a row and column.
	 */
	protected int getIndex(int row, int col)
	{
		return (row*cols)+col;
	}
	
}
