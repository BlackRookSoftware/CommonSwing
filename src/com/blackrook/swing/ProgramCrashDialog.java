/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import com.blackrook.commons.util.StringUtils;

/**
 * A dialog window for end-user program crashes that, if the JVM 
 * and platform supports it, presents the opportunity for the user
 * to open their E-mail client and send an E-mail to the developer.
 * @author Matthew Tropiano
 */
public class ProgramCrashDialog extends JDialog
{
	private static final long serialVersionUID = -6446066521771267843L;

	private static final String[] JVM_PROPS = new String[]{
		"java.runtime.name",
		"sun.boot.library.path",
		"java.vm.version",
		"java.vm.vendor",
		"java.vendor.url",
		"java.vm.name",
		"user.country",
		"java.runtime.version",
		"os.name",
		"os.arch",
		"java.library.path"
};
	
	/** E-mail address. */ 
	private String eMailAddress;
	/** E-mail subject. */ 
	private String eMailSubject;
	/** E-mail body prefix. */ 
	private String eMailBodyPrefix;
	
	/** Exception reference. */
	private Throwable throwableRef;
	
	/** Text area for the exception. */
	private JTextArea exceptionArea;
	/** Text area for the body. */
	private JTextArea bodyArea;
	/** Desktop instance. */
	private Desktop desktop;
	
	/**
	 * Creates a new crash dialog.
	 * @param title				Dialog title.
	 * @param icon				An icon to display in the top left of the window.
	 * @param headingLabel		A message detailing what happened.
	 * @param exceptionLabel	The label text for the exception text.
	 * @param bodyLabel			The label text for the body.
	 * @param exception			The exception itself.
	 * @param eMailAddress		The target E-mail address.
	 * @param eMailSubject		The target subject.
	 * @param eMailBodyPrefix	Part of the E-mail body, if any (can be null).
	 * @param mailButtonText	Mail button text.
	 * @param cancelButtonText	The cancel button text.
	 */
	public ProgramCrashDialog(
		String title, Icon icon, 
		String headingLabel, String exceptionLabel, String bodyLabel,
		Throwable exception, 
		String eMailAddress, String eMailSubject, String eMailBodyPrefix,
		String mailButtonText, String cancelButtonText)
	{
		setModal(true);
		setTitle(title);
		
		this.eMailAddress = eMailAddress;
		this.eMailSubject = eMailSubject;
		this.eMailBodyPrefix = eMailBodyPrefix;
		this.throwableRef = exception;
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		JPanel p;
		JLabel lb;
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BorderLayout());
		if (icon != null)
		{
			p = new JPanel();
			lb = new JLabel();
			lb.setIcon(icon);
			lb.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			lb.setVerticalAlignment(SwingConstants.TOP);
			topPanel.add(lb, BorderLayout.WEST);
		}
		p = new JPanel();

		lb = new JLabel(String.valueOf(headingLabel));
		lb.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		lb.setVerticalAlignment(SwingConstants.TOP);

		topPanel.add(lb, BorderLayout.CENTER);
		c.add(topPanel,BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		p = new JPanel();
		p.setLayout(new BorderLayout());

		lb = new JLabel(String.valueOf(exceptionLabel));
		lb.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		p.add(lb, BorderLayout.NORTH);
		exceptionArea = new JTextArea();
		exceptionArea.setEditable(false);
		JScrollPane jsp = new JScrollPane(
				exceptionArea,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		jsp.setBorder(new BevelBorder(BevelBorder.LOWERED));
		jsp.setPreferredSize(new Dimension(600,160));
		exceptionArea.setText(StringUtils.getExceptionString(exception));
		p.add(jsp, BorderLayout.CENTER);
		centerPanel.add(p, BorderLayout.CENTER);

		if (eMailAddress != null && 
				Desktop.isDesktopSupported() && 
				Desktop.getDesktop().isSupported(Desktop.Action.MAIL))
		{
			desktop = Desktop.getDesktop();
			
			p = new JPanel();
			p.setLayout(new BorderLayout());
			lb = new JLabel(String.valueOf(bodyLabel));
			lb.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
			p.add(lb, BorderLayout.NORTH);

			bodyArea = new JTextArea();
			jsp = new JScrollPane(
					bodyArea,
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			jsp.setBorder(new BevelBorder(BevelBorder.LOWERED));
			jsp.setPreferredSize(new Dimension(600,160));
			p.add(jsp, BorderLayout.CENTER);
			bodyArea.setText("");
			centerPanel.add(p, BorderLayout.SOUTH);
			
			p = new JPanel();
			p.setLayout(new FlowLayout(FlowLayout.RIGHT));
			p.add(new JButton(new AbstractAction(mailButtonText)
			{
				private static final long serialVersionUID = 5641944497201042821L;

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					try{
						URI uri = createMailToURI();
						desktop.mail(uri);
						setVisible(false);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}));
			p.add(new JButton(new AbstractAction(cancelButtonText)
			{
				private static final long serialVersionUID = 5641944497201023651L;

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					setVisible(false);
				}
			}));
			
			c.add(p,BorderLayout.SOUTH);
		}

		c.add(centerPanel,BorderLayout.CENTER);
		c.setPreferredSize(new Dimension(640,480));
		setResizable(false);
		pack();
	}

	/**
	 * Creates a MAILTO URI.
	 */
	private URI createMailToURI()
	{
		StringBuilder sb = new StringBuilder("mailto:");
		sb.append(eMailAddress);
		sb.append('?');
		if (eMailSubject != null)
			sb.append("subject="+StringUtils.urlEscape(eMailSubject));
		if (eMailSubject != null)
			sb.append("&");
		sb.append("body="+StringUtils.urlEscape(
				(eMailBodyPrefix != null ? eMailBodyPrefix : "") + 
				"\n\n----------------------------\n\n" +
				bodyArea.getText() + 
				"\n\n----------------------------\n\n" +
				"Stack Trace:\n" +
				StringUtils.getExceptionString(throwableRef) + "\n\n" +
				"JVM Properties:\n" +
				getSystemPropertiesStrings()
				));
		
		try{
			return new URI(sb.toString());
		} catch (URISyntaxException e){
			return null;
		}
	}
	
	private String getSystemPropertiesStrings()
	{
		StringBuilder sb = new StringBuilder();
		
		for (String keyname : JVM_PROPS)
			sb.append(keyname + " = " + System.getProperty(keyname) + "\n");
		
		return sb.toString();
	}
	
}
