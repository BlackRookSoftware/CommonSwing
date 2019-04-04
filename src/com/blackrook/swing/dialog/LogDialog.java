/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import com.blackrook.swing.field.RButton;

/**
 * Dialog type that is used for dumping text into
 * and then displaying layer if something happens.
 * This is created as a modal dialog.
 * @author Matthew Tropiano
 */
public class LogDialog extends JDialog
{
	private static final long serialVersionUID = -3470961719674420427L;

	/** The text area. */
	private JScrollPane scrollPane;
	/** The text area. */
	private JTextArea textArea;
	
	/**
	 * Creates a new LogDialog with a title.
	 * @param title	the title of the dialog.
	 */
	public LogDialog(String title)
	{
		super();
		setTitle(title);
		setModal(true);
		setLayout(new BorderLayout());
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		centerPanel.add(scrollPane = makePane(), BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);
		JPanel bottomPanel = new JPanel(); 
		bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		bottomPanel.add(new RButton("OK")
		{
			private static final long serialVersionUID = 7434180420569264040L;
			@Override
			public void onClick()
			{
				closeDialog();
			}
		});
		add(bottomPanel, BorderLayout.SOUTH);
	}
	
	/**
	 * Closes the dialog.
	 */
	public void closeDialog()
	{
		setVisible(false);
	}
	
	/**
	 * Returns the scroll pane in the dialog.
	 */
	public JScrollPane getScrollPane()
	{
		return scrollPane;
	}

	protected JScrollPane makePane()
	{
		JScrollPane out = new JScrollPane(textArea);
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Courier New",Font.PLAIN,11));
		textArea.setTabSize(4);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		out.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		out.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		out.setBorder(new BevelBorder(BevelBorder.LOWERED));
		out.setWheelScrollingEnabled(true);
		return out;
	}
	
	/** Print a message to the text area with a newline character at the end. */
	public void println(Object o)
	{
		print(o.toString()+"\n");
	}

	/** Print a newline character to the text area. */
	public void println()
	{
		print("\n");
	}

	/** Print a message to the text area. */
	public synchronized void print(Object o)
	{
		textArea.append(o.toString());
	}
}
