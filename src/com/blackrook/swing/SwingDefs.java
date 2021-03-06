/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URI;

import javax.swing.JOptionPane;

/**
 * Filled with common throwaway functions and calls for convenience.
 * @author Matthew Tropiano
 * @deprecated Since 2.1.0 in favor of better naming convention.
 */
@Deprecated
public final class SwingDefs
{
	private SwingDefs() {}
	
	/** Desktop instance. */
	private static Desktop desktopInstance;

	static
	{
		if (Desktop.isDesktopSupported());
			desktopInstance = Desktop.getDesktop();
	}

	/**
	 * Show an alert window.
	 * @param message	The message to show.
	 */
	public static void error(String message)
	{
		error(message,null);
	}

	/**
	 * Show an warning window.
	 * @param message	The message to show.
	 */
	public static void warning(String message)
	{
		warning(message,null);
	}

	/**
	 * Show an info window.
	 * @param message	The message to show.
	 */
	public static void info(String message)
	{
		info(message,null);
	}

	/**
	 * Displays a confirmation window asking a user a yes or no question.
	 * @param message	the message to show.
	 * @return			true if "yes" was clicked. false otherwise.
	 */
	public static boolean yesTo(String message)
	{
		return yesTo(message, null);
	}

	/**
	 * Displays a confirmation window asking a user a yes or no question.
	 * This is a convenience method for code readability, and is completely
	 * equivalent to !yesTo(message).
	 * @param message	the message to show.
	 * @return			true if "no" was clicked. false otherwise.
	 */
	public static boolean noTo(String message)
	{
		return !yesTo(message);
	}

	/**
	 * Show an alert window.
	 * @param message	The message to show.
	 * @param parent	Parent component of this dialog.
	 */
	public static void error(String message, Component parent)
	{
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(
			    parent,message,"Alert",
			    JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Show a warning window.
	 * @param message	The message to show.
	 * @param parent	Parent component of this dialog.
	 */
	public static void warning(String message, Component parent)
	{
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(
			    parent,message,"Warning",
			    JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Show an info window.
	 * @param message	The message to show.
	 * @param parent	Parent component of this dialog.
	 */
	public static void info(String message, Component parent)
	{
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(
			    parent,message,"Info",
			    JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Displays a confirmation window asking a user a yes or no question.
	 * @param message	the message to show.
	 * @param parent	Parent component of this dialog.
	 * @return			true if "yes" was clicked. false otherwise.
	 */
	public static boolean yesTo(String message, Component parent)
	{
		int c = JOptionPane.showConfirmDialog(
			    parent, 
			    message,
			    "Confirm",
			    JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE);
		boolean out = c == JOptionPane.YES_OPTION;
		return out;
	}

	/**
	 * Displays a confirmation window asking a user a yes or no question.
	 * This is a convenience method for code readability, and is completely
	 * equivalent to !yesTo(message,parent).
	 * @param message	the message to show.
	 * @param parent	Parent component of this dialog.
	 * @return			true if "no" was clicked. false otherwise.
	 */
	public static boolean noTo(String message, Component parent)
	{
		return !yesTo(message,parent);
	}

	/**
	 * Attempts to open a file using the default associated opening program.
	 * Returns false if unsuccessful, true otherwise.
	 * @throws IOException if the file could not be opened for some reason.
	 */
	public static boolean open(File file) throws IOException
	{
		if (desktopInstance != null && desktopInstance.isSupported(Desktop.Action.OPEN))
		{
			desktopInstance.open(file);
			return true;
		}
		return false;
	}

	/**
	 * Attempts to open a location (usually a web browser) for a URI.
	 * Returns false if unsuccessful, true otherwise.
	 * @throws IOException if the URI could not be opened for some reason.
	 */
	public static boolean browse(URI uri) throws IOException
	{
		if (desktopInstance != null && desktopInstance.isSupported(Desktop.Action.BROWSE))
		{
			desktopInstance.browse(uri);
			return true;
		}
		return false;
	}

	/**
	 * Attempts to open a mail client for a "mailto" URI.
	 * Returns false if unsuccessful, true otherwise.
	 * @throws IOException if the URI could not be opened for some reason.
	 */
	public static boolean mail(URI uri) throws IOException
	{
		if (desktopInstance != null && desktopInstance.isSupported(Desktop.Action.MAIL))
		{
			desktopInstance.mail(uri);
			return true;
		}
		return false;
	}

}
