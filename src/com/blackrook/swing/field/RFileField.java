/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;

import com.blackrook.commons.Common;
import com.blackrook.commons.list.List;

/**
 * A panel that has a file browsing button.
 * @author Matthew Tropiano
 */
public class RFileField extends RInputFieldAbstract<File[]> implements RChangeEventListener, RKeyEventListener, RFocusEventListener
{
	private static final long serialVersionUID = 8144734358210178124L;

	/**
	 * Centralized key handler for all instances of this field.
	 */
	private static class KeyHandler implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			RFileField field = (RFileField)(e.getComponent().getParent().getParent());
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				field.checkValue();
				field.checkChange();
			}
			else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
				field.resetValue();
			field.onKeyPress(e.getKeyCode());
		}
	
		@Override
		public void keyReleased(KeyEvent e)
		{
			((RFileField)(e.getComponent().getParent().getParent())).onKeyRelease(e.getKeyCode());
		}
	
		@Override
		public void keyTyped(KeyEvent e)
		{
			((RFileField)(e.getComponent().getParent().getParent())).onKeyType();
		}
	}

	/**
	 * Centralized focus handler for all instances of this field.
	 */
	private static class FocusHandler implements FocusListener
	{
		@Override
		public void focusGained(FocusEvent e)
		{
			RFileField field = (RFileField)(e.getComponent().getParent().getParent());
			field.saveLastValue();
			field.onFocus();
		}
	
		@Override
		public void focusLost(FocusEvent e)
		{
			RFileField field = (RFileField)(e.getComponent().getParent().getParent());
			field.checkValue();
			field.checkChange();
			field.onBlur();
		}
	}

	public static final int
	FILES = JFileChooser.FILES_ONLY,
	DIRECTORIES = JFileChooser.DIRECTORIES_ONLY,
	FILES_AND_DIRECTORIES = JFileChooser.FILES_AND_DIRECTORIES;
	
	/** The path text field. */
	private JTextField pathField;
	/** The browse button. */
	private JButton browseButton;
	/** The browse button. */
	private int selectionMode;
	/** The browse button. */
	private boolean multiSelect;
	/** The file filters for this panel. */
	private FileFilter[] fileFilters;
	
	/** Use relative path? */
	private String relativePath;
	/** The last value used by this object. */
	private String lastValue;

	/** The last opened file from any field. */
	private static File lastFile;
	
	/** The key handler for all instances of this field. */
	protected static final KeyHandler STATIC_KEYHANDLER = new KeyHandler();
	/** The focus handler for all instances of this field. */
	protected static final FocusHandler STATIC_FOCUSHANDLER = new FocusHandler();
	

	/**
	 * Creates a new RFileField.
	 * @param labelName			the label of this field.
	 * @param labelSpacing		the width of the label area.
	 * @param filters			what filters are on this chooser?
	 */
	public RFileField(String labelName, int labelSpacing, FileFilter ... filters)
	{
		this(labelName,labelSpacing,"...",FILES_AND_DIRECTORIES,false,filters);
	}
	
	/**
	 * Creates a new RFileField.
	 * @param labelName			the label of this field.
	 * @param labelSpacing		the width of the label area.
	 * @param selectionMode		the chooser selection mode.
	 * @param filters			what filters are on this chooser?
	 */
	public RFileField(String labelName, int labelSpacing, int selectionMode, FileFilter ... filters)
	{
		this(labelName,labelSpacing,"...",selectionMode,false,filters);
	}
	
	/**
	 * Creates a new RFileField.
	 * @param labelName			the label of this field.
	 * @param labelSpacing		the width of the label area.
	 * @param selectionMode		the chooser selection mode.
	 * @param multiSelect		select multiple files? 
	 * @param filters			what filters are on this chooser?
	 */
	public RFileField(String labelName, int labelSpacing, int selectionMode, boolean multiSelect, FileFilter ... filters)
	{
		this(labelName,labelSpacing,"...",selectionMode,multiSelect,filters);
	}
	
	/**
	 * Creates a new RFileField.
	 * @param labelName			the label of this field.
	 * @param labelSpacing		the width of the label area.
	 * @param browseButtonName	the name of the browser button.
	 * @param selectionMode		the chooser selection mode.
	 * @param multiSelect		select multiple files? 
	 * @param filters			what filters are on this chooser?
	 */
	public RFileField(String labelName, int labelSpacing, String browseButtonName, 
			int selectionMode, boolean multiSelect, FileFilter ... filters)
	{
		super(labelName, labelSpacing);
		JPanel inputPanel = getInputComponent();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(pathField = makeTextField(), BorderLayout.CENTER);
		inputPanel.add(browseButton = makeBrowseButton(browseButtonName), BorderLayout.EAST);
		pathField.addFocusListener(STATIC_FOCUSHANDLER);
		pathField.addKeyListener(STATIC_KEYHANDLER);
		this.selectionMode = selectionMode;
		this.multiSelect = multiSelect;
		this.fileFilters = filters;
		this.relativePath = null;
	}
	
	/**
	 * Returns the last file opened by any of the file fields.
	 * Used for originating directory if no file selected.
	 * @since 2.2.0
	 */
	public static File getLastFile()
	{
		return lastFile;
	}
	
	/**
	 * Sets the last file opened by any of the file fields.
	 * Used for originating directory if no file selected.
	 * @since 2.2.0
	 */
	public static void setLastFile(File file)
	{
		lastFile = file;
	}
	
	/**
	 * The file chooser, broken out for other applications to use easily.
	 * This affects "last file selected" for all RFileFields and users of the function, though.
	 * @param selectionMode the RFileField selection mode.
	 * @param multiSelect if true, can select multiple files.
	 * @param lastSelected the last selected file to open the dialog to.
	 * @param fileFilters the file filters to apply to the chooser. 
	 * @return the list of files selected, or null if the chooser was cancelled.
	 * @since 2.5.2
	 */
	public static File[] openFileChooser(int selectionMode, boolean multiSelect, File lastSelected, FileFilter ... fileFilters)
	{
		JFileChooser fc = new JFileChooser();
		fc.setDialogType(JFileChooser.OPEN_DIALOG);
		fc.setFileSelectionMode(selectionMode);
		fc.setMultiSelectionEnabled(multiSelect);
		if (lastSelected != null)
			fc.setSelectedFile(lastSelected.getAbsoluteFile());

		for (FileFilter ff : fileFilters)
			fc.addChoosableFileFilter(ff);
		
		if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			if (fc.getSelectedFiles().length != 0)
			{
				File[] sel = fc.getSelectedFiles();
				setLastFile(sel[0]);
				return sel;
			}
			else if (fc.getSelectedFile() != null)
			{
				File f = fc.getSelectedFile();
				setLastFile(f);
				return new File[]{f};
			}
			else 
				return null;
		}
		else
			return null;
	}
	
	protected JTextField makeTextField()
	{
		JTextField out = new JTextField();
		out.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return out;
	}
	
	protected JButton makeBrowseButton(String name)
	{
		JButton out = new JButton();
		out.setText(name);
		out.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				File last = null;
				File[] fa = getValue();
				if (fa != null && fa.length > 0)
				{
					File f = fa[0];
					if (f != null)
						last = f.getAbsoluteFile();
				}
				else
					last = lastFile;

				File[] selected = openFileChooser(selectionMode, multiSelect, last, fileFilters);
				if (selected != null && selected.length > 0)
					setValue(selected);
				checkValue();
				checkChange();
			}
	});
		return out;
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		pathField.setEnabled(enabled);
		browseButton.setEnabled(enabled);
	}
	
	/**
	 * Sets the field's value.
	 */
	public void setPath(String path)
	{
		pathField.setText(path);
	}
	
	@Override
	public void setValue(File[] value)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < value.length; i++)
		{
			sb.append(makeRelativePath(value[i]));
			if (i < value.length-1)
				sb.append(File.pathSeparator);
			setLastFile(value[i]);
		}
		setPath(sb.toString());
	}

	/**
	 * Makes a path into a relative path if relative path is set.
	 */
	protected String makeRelativePath(File in)
	{
		boolean rel = (relativePath != null && relativePath.trim().length() > 0);
		String out = in.getPath();
		if (rel)
		{
			try {
				out = Common.getRelativePath(relativePath, in.getAbsolutePath());
			} catch (IOException e) {}
		}
		return out;
	}
	
	/**
	 * Sets the relative path to use for all files set via setValue() or 
	 * the browse button. If null, this uses absolute paths.
	 * @since 2.2.0
	 */
	public void setRelativePath(String relativePath)
	{
		this.relativePath = relativePath;
	}

	/**
	 * Gets the field's value.
	 */
	public String getPath()
	{
		return pathField.getText();
	}
	
	/**
	 * Gets the path field itself.
	 */
	public JTextField getPathField()
	{
		return pathField;
	}

	@Override
	public String getStringValue()
	{
		return getPath();
	}
	
	@Override
	public File[] getValue()
	{
		String[] paths = getPath().split(File.pathSeparator);
		List<File> list = new List<File>(paths.length);
		for (String p : paths)
			if (p.trim().length() > 0)
				list.add(new File(p.trim()));
		
		File[] out = new File[list.size()];
		list.toArray(out);
		return out;
	}

	/**
	 * Gets the relative path to use for all files set via setValue() or 
	 * the browse button. If null, this uses absolute paths.
	 * @since 2.2.0
	 */
	public String getRelativePath()
	{
		return relativePath;
	}

	/** Sets the last value to the current value. */
	protected void saveLastValue()
	{
		lastValue = getPath();
	}

	/** Sets the current value to the last value. */
	protected void resetValue()
	{
		pathField.setText(lastValue);
	}

	/**
	 * Checks and corrects this value before it actually registers as "changed".
	 */
	protected void checkValue()
	{
	}

	/**
	 * Checks to see if this field's data has changed.
	 */
	protected void checkChange()
	{
		if (!getPath().equals(lastValue))
			onChange();
		saveLastValue();
	}

	@Override
	public void onChange()
	{
		// Do nothing.
	}

	@Override
	public void onKeyPress(int keyCode)
	{
		// Do nothing.
	}

	@Override
	public void onKeyRelease(int keyCode)
	{
		// Do nothing.
	}

	@Override
	public void onKeyType()
	{
		// Do nothing.
	}

	@Override
	public void onFocus()
	{
		// Do nothing.
	}

	@Override
	public void onBlur()
	{
		// Do nothing.
	}


}
