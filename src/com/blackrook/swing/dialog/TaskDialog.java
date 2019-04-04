/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * A dialog box that could be used for showing progress for a task.
 * @author Matthew Tropiano
 */
public class TaskDialog extends JDialog
{
	private static final long serialVersionUID = -4907360436702195406L;
	
	/** Heading label. */
	private JLabel header;
	/** Info label. */
	private JLabel info;
	/** Info label. */
	private JProgressBar progressBar;

	/**
	 * Creates a new TaskDialog.
	 * This shows the Cancel button.
	 * @param title the title of this dialog.
	 */
	public TaskDialog(String title)
	{
		this(null, title, true);
	}
	
	/**
	 * Creates a new TaskDialog.
	 * This shows the Cancel button.
	 * @param parent the parent component for the dialog.
	 * @param title the title of this dialog.
	 */
	public TaskDialog(JFrame parent, String title)
	{
		this(parent, title, true);
	}
	
	/**
	 * Creates a new TaskDialog.
	 * @param parent the parent component for the dialog.
	 * @param title the title of this dialog.
	 * @param addCancelPanel if false, this does not add the "Cancel" button.
	 */
	public TaskDialog(JFrame parent, String title, boolean addCancelPanel)
	{
		super(parent);
		setTitle(title);
		setResizable(false);

		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		c.setPreferredSize(new Dimension(160,120));
		c.add(makeHeaderPanel(),BorderLayout.NORTH);
		c.add(makeMiddlePanel(),BorderLayout.CENTER);
		if (addCancelPanel)
			c.add(makeCancelPanel(),BorderLayout.SOUTH);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		pack();
	}
	
	protected JPanel makeHeaderPanel()
	{
		JPanel out = new JPanel();
		out.setLayout(new FlowLayout(FlowLayout.CENTER));
		out.add(header = new JLabel("HEADER"));
		return out;
	}

	protected JPanel makeMiddlePanel()
	{
		JPanel out = new JPanel();
		out.setLayout(new BorderLayout());
		out.add(makeInfoPanel(),BorderLayout.NORTH);
		out.add(progressBar = new JProgressBar(), BorderLayout.CENTER);
		return out;
	}

	protected JPanel makeInfoPanel()
	{
		JPanel out = new JPanel();
		out.setLayout(new FlowLayout(FlowLayout.CENTER));
		out.add(info = new JLabel("INFO"));
		return out;
	}

	@SuppressWarnings("serial")
	protected JPanel makeCancelPanel()
	{
		JPanel out = new JPanel();
		out.setLayout(new FlowLayout(FlowLayout.CENTER));
		out.add(new JButton(new AbstractAction("Cancel")
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				setVisible(false);
			}
		}));
		return out;
	}

	/**
	 * Set the bounds of the progress bar.
	 * @param min	the min value.
	 * @param max	the max value.
	 */
	public void setBarBounds(int min, int max)
	{
		progressBar.setMinimum(min);
		progressBar.setMaximum(max);
	}

	/**
	 * Sets the bar value.
	 */
	public void setBarValue(int val)
	{
		progressBar.setValue(val);
	}
	
	/**
	 * Increments the bar value.
	 */
	public void incBarValue()
	{
		progressBar.setValue(progressBar.getValue()+1);
	}
	
	/**
	 * Sets the bar to indeterminate value.
	 */
	public void setBarIndeterminate(boolean val)
	{
		progressBar.setIndeterminate(val);
	}

	/**
	 * Sets the text in the header label.
	 */
	public void setHeader(String s)
	{
		header.setText(s);
	}
	
	/**
	 * Sets the info label text.
	 */
	public void setInfo(String s)
	{
		info.setText(s);
	}
}
