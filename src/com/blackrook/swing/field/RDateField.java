/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.GregorianCalendar;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.blackrook.commons.math.RMath;

/**
 * Date field class.
 * @author Matthew Tropiano
 */
public class RDateField extends RInputFieldAbstract<GregorianCalendar>
{
	private static final long serialVersionUID = 1631819532729544116L;

	/** The field for the date. */
	private Field dateField;
	
	/** Date dialog. */
	private DatePicker datePicker;
	/***/

	/** The date data. */
	private GregorianCalendar calendar;
	
	/**
	 * Creates a new RDateField.
	 * @param labelName		this field's label name.
	 * @param labelSpacing	the width of the label area.
	 */
	public RDateField(String labelName, int labelSpacing)
	{
		super(labelName, labelSpacing);
		calendar = new GregorianCalendar();
		datePicker = new DatePicker();
		JPanel inputPanel = getInputComponent();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(dateField = new Field(), BorderLayout.CENTER);
		
		JButton button = new JButton();
		button.setText("...");
		button.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				pickDate();
			}
		});
		inputPanel.add(button, BorderLayout.EAST);
		setValue(calendar);
	}

	@Override
	public GregorianCalendar getValue()
	{
		return calendar;
	}

	@Override
	public void setValue(GregorianCalendar value)
	{
		dateField.setValue(value);
	}

	/**
	 * Opens the date picker so that a date can be picked.
	 */
	protected void pickDate()
	{
		datePicker.setDate(calendar);
		datePicker.setVisible(true);
		setValue(datePicker.getDate());
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		dateField.setEnabled(enabled);
	}
	
	/**
	 * The field that contains the date.
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
					setValue(getCalendarFromString(getText()));
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
				setValue(getCalendarFromString(getText()));
			}
		}
		
		public Field()
		{
			super();
			setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			addKeyListener(new KeyHandler());
			addFocusListener(new FocusHandler());
		}
		
		public void setValue(GregorianCalendar val)
		{
			setText(getDateString(val));
		}
		
		public GregorianCalendar getValue()
		{
			return getCalendarFromString(getText());
		}
		
		private String getDateString(GregorianCalendar val)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(String.valueOf(val.get(GregorianCalendar.MONTH)+1));
			sb.append('/');
			sb.append(String.valueOf(val.get(GregorianCalendar.DAY_OF_MONTH)));
			sb.append('/');
			sb.append(String.valueOf(val.get(GregorianCalendar.YEAR)));
			return sb.toString();
		}

		private GregorianCalendar getCalendarFromString(String str)
		{
			try {
				int day = 1;
				int month = 1;
				int year = 1;
				String[] sa = str.split("\\/");
				if (sa.length >= 3)
				{
					month = Integer.parseInt(sa[0]);
					day = Integer.parseInt(sa[1]);
					year = Integer.parseInt(sa[2]);
				}
				else if (sa.length == 2)
				{
					month = Integer.parseInt(sa[0]);
					year = Integer.parseInt(sa[1]);
				}
				else if (sa.length == 1)
				{
					year = Integer.parseInt(sa[0]);
				}
				GregorianCalendar out = new GregorianCalendar();
				out.set(GregorianCalendar.YEAR, year);
				out.set(GregorianCalendar.MONTH, RMath.clampValue(month, 1, 12) - 1);
				out.set(GregorianCalendar.DAY_OF_MONTH, RMath.clampValue(day, 1, 
					out.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)));
				return out; 
			} catch (NumberFormatException ex) {
				return new GregorianCalendar();
			}
		}
		
	}

	/**
	 * Common dialog for choosing a date.
	 */
	protected static class DatePicker extends JDialog
	{
		private static final long serialVersionUID = 5363730190643034560L;

		/** Month selection box. */
		protected JComboBox<String> monthBox;
		/** Year selection box. */
		protected JSpinner yearBox;
		/** The buttons with all of the dates on them. */
		protected JButton[] dateButtons;

		/** Internal calendar. */
		protected GregorianCalendar calendar;
		
		/**
		 * Creates a new dialog set to a particular date.
		 */
		DatePicker()
		{
			setTitle("Pick Date");
			setModal(true);
			
			calendar = new GregorianCalendar();
			Container c = getContentPane();
			c.setLayout(new BorderLayout());
			JPanel topPanel = new JPanel();
			topPanel.setLayout(new BorderLayout());
			topPanel.add(monthBox = makeMonthBox(), BorderLayout.CENTER);
			topPanel.add(yearBox = makeYearBox(), BorderLayout.EAST);
			int size = 50;
			topPanel.setPreferredSize(new Dimension(size*7,24));
			add(topPanel, BorderLayout.NORTH);
			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new GridLayout(6,7));
			centerPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			centerPanel.setPreferredSize(new Dimension(size*7,size*5));
			dateButtons = makeButtons();
			for (int i = 0; i < dateButtons.length; i++)
				centerPanel.add(dateButtons[i]);
			add(centerPanel, BorderLayout.CENTER);
			
			setMonthField();
			setYearField();
			setDateButtons();
			setResizable(false);
			pack();
		}
		
		/** Sets this dialog's date. */
		public void setDate(GregorianCalendar cal)
		{
			calendar = cal;
			setMonthField();
			setYearField();
			setDateButtons();
		}

		/** Gets this dialog's date. */
		public GregorianCalendar getDate()
		{
			return calendar;
		}

		/** Creates the month drop-down. */
		protected JComboBox<String> makeMonthBox()
		{
			JComboBox<String> out = new JComboBox<String>();
			out.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			out.addItemListener(new ItemListener()
			{
				@Override
				@SuppressWarnings("unchecked")
				public void itemStateChanged(ItemEvent e)
				{
					if (e.getStateChange() == ItemEvent.SELECTED)
					{
						JComboBox<String> cb = (JComboBox<String>)e.getSource();
						calendar.set(GregorianCalendar.MONTH, cb.getSelectedIndex());
						setDateButtons();
					}
				}
			});
			for (int i = 0; i < 12; i++)
			{
				GregorianCalendar c = new GregorianCalendar();
				c.set(1, i, 1);
				out.addItem(c.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.LONG, getLocale()));
			}
			return out;
		}
		
		/** Creates the month drop-down. */
		protected JSpinner makeYearBox()
		{
			JSpinner out = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
			out.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			out.addChangeListener(new ChangeListener()
			{
				@Override
				public void stateChanged(ChangeEvent e)
				{
					JSpinner s = (JSpinner)e.getSource();
					calendar.set(GregorianCalendar.YEAR, (Integer)s.getValue());
					setDateButtons();
				}
			});
			return out;
		}
		
		/** Creates the month drop-down. */
		protected JButton[] makeButtons()
		{
			ButtonAction ba = new ButtonAction();
			JButton[] out = new JButton[42];
			for (int i = 0; i < out.length; i++)
			{
				out[i] = new JButton();
				out[i].setEnabled(false);
				out[i].addActionListener(ba);
			}
			return out;
		}

		/** Adjusts the month field according to the current Gregorian Calendar settings. */
		protected void setMonthField()
		{
			monthBox.setSelectedIndex(calendar.get(GregorianCalendar.MONTH));
		}

		/** Adjusts the year field according to the current Gregorian Calendar settings. */
		protected void setYearField()
		{
			yearBox.setValue(calendar.get(GregorianCalendar.YEAR));
		}

		/** Adjusts the date buttons according to the current Gregorian Calendar settings. */
		protected void setDateButtons()
		{
			if (yearBox == null || monthBox == null)
				return;
			int maxDate = calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
			int minDate = calendar.getActualMinimum(GregorianCalendar.DAY_OF_MONTH);
			int startDay = calendar.get(GregorianCalendar.DAY_OF_WEEK) - 1;
			
			int cdate = minDate;
			for (int d = 0; d < dateButtons.length; d++)
			{
				if (d < startDay || cdate > maxDate)
				{
					dateButtons[d].setActionCommand(" ");
					dateButtons[d].setText(" ");
					dateButtons[d].setEnabled(false);
				}
				else
				{
					dateButtons[d].setActionCommand(""+cdate);
					dateButtons[d].setText(""+cdate);
					dateButtons[d].setEnabled(true);
					cdate++;
				}
			}
		}

		public class ButtonAction extends AbstractAction
		{
			private static final long serialVersionUID = 4577849142986094845L;

			@Override
			public void actionPerformed(ActionEvent e)
			{
				calendar.set(GregorianCalendar.DATE, Integer.parseInt(e.getActionCommand()));
				setVisible(false);
			}
		}
		
	}
	
}
