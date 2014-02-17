/*******************************************************************************
 * Copyright (c) 2009-2014 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingWorker;

import com.blackrook.swing.dialog.TaskDialog;

/**
 * A task that is passively executed in the swing environment via the
 * SwingWorker class. executing this task shows the TaskDialog for this
 * process until it completes.
 * @author Matthew Tropiano
 */
public abstract class SwingTask extends SwingWorker<Void,Void>
{
	/** The name of the task. */
	private String name;
	/** Internal class dialog. */
	private TaskDialog taskDialog;
	
	/**
	 * Creates a new SwingTask.
	 * Sets "true" for cancellable.
	 * @param name the name of the task.
	 */
	public SwingTask(String name)
	{
		this(name,true);
	}
	
	/**
	 * Creates a new SwingTask.
	 * @param name the name of the task.
	 * @param cancellable true if the task can be cancelled, false if not.
	 */
	public SwingTask(String name, boolean cancellable)
	{
		this(name,cancellable,null);
	}
	
	/**
	 * Creates a new SwingTask.
	 * Sets "true" for cancellable.
	 * @param name the name of the task.
	 * @param parent the parent component of this task (and the dialog).
	 */
	public SwingTask(String name, JFrame parent)
	{
		this(name,true,parent);
	}
	
	/**
	 * Creates a new SwingTask.
	 * @param name the name of the task.
	 * @param cancellable true if the task can be cancelled, false if not.
	 * @param parent the parent component of this task (and the dialog).
	 */
	public SwingTask(String name, boolean cancellable, JFrame parent)
	{
		this.name = name;
		taskDialog = new TaskDialog(parent,name,cancellable);
		taskDialog.setLocationByPlatform(true);
		taskDialog.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosed(WindowEvent arg0)
			{
				cancel(true);
			}
		});
	}
	
	/** Returns the name for this task. */
	public String getName()
	{
		return name;
	}
	
	@Override
	public Void doInBackground()
	{
		taskDialog.setVisible(true);
		setBarIndeterminate(true);
		try {
			doTask();
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	@Override
	public void done()
	{
		taskDialog.setVisible(false);
		taskDialog.dispose();
	}

	/**
	 * Performs the task.
	 * Does nothing unless overridden.
	 */
	public abstract void doTask();
	
	/**
	 * Set the bounds of the progress bar.
	 * @param min	the min value.
	 * @param max	the max value.
	 */
	public void setBarBounds(int min, int max)
	{
		taskDialog.setBarBounds(min,max);
	}

	/**
	 * Sets the bar value.
	 */
	public void setBarValue(int val)
	{
		taskDialog.setBarValue(val);
	}
	
	/**
	 * Increments the bar value.
	 */
	public void incBarValue()
	{
		taskDialog.incBarValue();
	}
	
	/**
	 * Sets the bar in indeterminate.
	 */
	public void setBarIndeterminate(boolean val)
	{
		taskDialog.setBarIndeterminate(val);
	}

	/**
	 * Sets the text in the header label.
	 */
	public void setHeader(String s)
	{
		taskDialog.setHeader(s);
	}
	
	/**
	 * Sets the info label text.
	 */
	public void setInfo(String s)
	{
		taskDialog.setInfo(s);
	}
	
}
