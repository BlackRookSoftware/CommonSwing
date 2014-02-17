/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.blackrook.commons.math.RMath;

/**
 * Color field class.
 * @author Matthew Tropiano
 */
public class RColorField extends RInputFieldAbstract<Color>
{
	private static final long serialVersionUID = 1631819532729544116L;

	/** The field for the red component. */
	private Field redField;
	/** The field for the green component. */
	private Field greenField;
	/** The field for the blue component. */
	private Field blueField;
	/** The field for the alpha component. */
	private Field alphaField;
	/** The color preview. */
	private RButton colorButton;
	
	/** The color data. */
	private Color color;
	
	/**
	 * Creates a new RColorField.
	 * @param labelName		this field's label name.
	 * @param labelSpacing	the width of the label area.
	 */
	public RColorField(String labelName, int labelSpacing)
	{
		super(labelName, labelSpacing);
		color = new Color(255, 255, 255, 255);
		JPanel inputPanel = getInputComponent();
		inputPanel.setLayout(new BorderLayout());
			JPanel cpanel = new JPanel();
			cpanel.setLayout(new GridLayout(1,4));
			cpanel.add(redField = new Field());
			cpanel.add(greenField = new Field());
			cpanel.add(blueField = new Field());
			cpanel.add(alphaField = new Field());
		inputPanel.add(cpanel, BorderLayout.CENTER);
		inputPanel.add(colorButton = new RButton()
		{
			private static final long serialVersionUID = -3027497934941268824L;

			public void onClick()
			{
				Color c = pickColor();
				if (c != null)
					setValue(c);
			}
		}, BorderLayout.EAST);
		redField.setText("255");
		greenField.setText("255");
		blueField.setText("255");
		alphaField.setText("255");
		performChange();
	}

	@Override
	public Color getValue()
	{
		return color;
	}

	@Override
	public void setValue(Color value)
	{
		redField.setValue(value.getRed());
		greenField.setValue(value.getGreen());
		blueField.setValue(value.getBlue());
		alphaField.setValue(value.getAlpha());
		performChange();
	}

	/**
	 * Re-evaluates the color based on the component values.
	 */
	protected void performChange()
	{
		color = new Color(
			redField.getValue(),
			greenField.getValue(),
			blueField.getValue(),
			255
			);
		colorButton.setBackground(color);
	}
	
	/**
	 * Opens the color picker so that a color can be picked.
	 */
	protected Color pickColor()
	{
		return JColorChooser.showDialog(this, "Pick a color.", color);
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		redField.setEnabled(enabled);
		blueField.setEnabled(enabled);
		greenField.setEnabled(enabled);
		alphaField.setEnabled(enabled);
		colorButton.setEnabled(enabled);
	}
	
	/**
	 * The individual byte fields.
	 */
	protected class Field extends JTextField
	{
		private static final long serialVersionUID = -8699830153361750040L;

		/**
		 * Centralized key handler for all instances of this field.
		 */
		private class KeyHandler extends KeyAdapter
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					checkValue();
			}
		}
		
		/**
		 * Centralized focus handler for all instances of this field.
		 */
		private class FocusHandler implements FocusListener
		{
			@Override
			public void focusGained(FocusEvent e)
			{
				selectAll();
			}

			@Override
			public void focusLost(FocusEvent e)
			{
				checkValue();
			}
		}
		
		public Field()
		{
			super();
			setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			addKeyListener(new KeyHandler());
			addFocusListener(new FocusHandler());
		}
		
		public void setValue(int val)
		{
			setText(String.valueOf(val));
		}
		
		public int getValue()
		{
			try {
				int i = Integer.parseInt(getText());
				return RMath.clampValue(i, 0, 255); 
			} catch (NumberFormatException ex) {
				return 0;
			}
		}
		
		/**
		 * Corrects the value.
		 */
		protected void checkValue()
		{
			setText(String.valueOf(getValue()));
			performChange();
		}
		
	}
	
}
