/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.blackrook.commons.Reflect;

/**
 * Time field for starting/stopping a timer and retrieving
 * the amount of time that passed. 
 * @author Matthew Tropiano
 */
public class RTimerField extends RInputFieldAbstract<Double>
{
	private static final long serialVersionUID = -3054069763098215915L;

	/** The time type. */
	public static enum TimeType
	{
		/** Time value is milliseconds, shown in display as hrs, min, secs. */
		FORMATTED,
		/** Time value is milliseconds, shown in display as milliseconds. */
		MILLISECONDS,
		/** Time value is seconds, shown in display as seconds. */
		SECONDS,
		/** Time value is minutes, shown in display as minutes. */
		MINUTES,
		/** Time value is hours, shown in display as hours. */
		HOURS;
	}
	
	/** Play icon. */
	protected static ImageIcon PLAY_ICON = null;
	/** Stop icon. */
	protected static ImageIcon STOP_ICON = null;
	/** Pause icon. */
	protected static ImageIcon PAUSE_ICON = null;
	
	static
	{
		try {
			PLAY_ICON = new ImageIcon(ImageIO.read(ClassLoader.getSystemResource(
					Reflect.getPackagePathForClass(RTimerField.class)+"/data/play.png")));
			STOP_ICON = new ImageIcon(ImageIO.read(ClassLoader.getSystemResource(
					Reflect.getPackagePathForClass(RTimerField.class)+"/data/stop.png")));
			PAUSE_ICON = new ImageIcon(ImageIO.read(ClassLoader.getSystemResource(
					Reflect.getPackagePathForClass(RTimerField.class)+"/data/pause.png")));
		} catch (Exception e) {
			throw new RuntimeException("Could not initialize time field icons. Internal Error. "+e.getLocalizedMessage());
		}
	}
	
	/** The field for the time component. */
	private JTextField counterField;
	/** The play button. */
	private RButton playButton;
	/** The stop button. */
	private RButton stopButton;
	/** The pause button. */
	private RButton pauseButton;

	/** The output type. */
	private TimeType timingType;
	/** Counter field. */
	private long milliseconds;
	
	/** Counter. */
	private long startMillis;
	/** Was timer paused? */
	private boolean paused;
	
	/**
	 * Creates a new RTimerField.
	 * @param labelName		this field's label name.
	 * @param labelSpacing	the width of the label area.
	 * @param timingType	the type of time increment to use.
	 */
	@SuppressWarnings("serial")
	public RTimerField(String labelName, int labelSpacing, TimeType timingType)
	{
		super(labelName, labelSpacing);
		this.timingType = timingType;
		
		JPanel inputPanel = getInputComponent();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(counterField = makeCounterField(), BorderLayout.CENTER);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,0));
		panel.add(playButton = new RButton(PLAY_ICON) { public void onClick() { play();} });
		panel.add(pauseButton = new RButton(PAUSE_ICON) { public void onClick() { pause();} });
		panel.add(stopButton = new RButton(STOP_ICON) { public void onClick() { stop();} });
		inputPanel.add(panel, BorderLayout.EAST);
		playButton.setEnabled(true);
		pauseButton.setEnabled(false);
		stopButton.setEnabled(false);
		paused = false;
		milliseconds = 0L;
	}
	
	/**
	 * Makes the counter field.
	 */
	protected JTextField makeCounterField()
	{
		JTextField out = new JTextField();
		out.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		out.setEditable(false);
		return out;
	}

	/** Called when play is clicked. */
	protected void play()
	{
		playButton.setEnabled(false);
		pauseButton.setEnabled(true);
		stopButton.setEnabled(true);
		if (!paused)
			milliseconds = 0L;
		paused = false;
		counterField.setText("Started...");
		startMillis = System.currentTimeMillis();
	}

	/** Called when stop is clicked. */
	protected void stop()
	{
		playButton.setEnabled(true);
		pauseButton.setEnabled(false);
		stopButton.setEnabled(false);
		paused = false;
		milliseconds += System.currentTimeMillis() - startMillis;
		counterField.setText(renderText());
	}
	
	/** Called when pause is clicked. */
	protected void pause()
	{
		playButton.setEnabled(true);
		pauseButton.setEnabled(false);
		stopButton.setEnabled(false);
		paused = true;
		milliseconds += System.currentTimeMillis() - startMillis;
		counterField.setText(renderText());
	}
	
	/**
	 * Returns the text value of the current millisecond value.
	 * This varies depending on the timing type.
	 */
	protected String renderText()
	{
		switch (timingType)
		{
			case FORMATTED:
			{
				if ((milliseconds/3600000) > 0)
					return String.format("%dh %dm %.03fs", (milliseconds/3600000), (milliseconds/60000), (milliseconds%60000f/1000));
				else if ((milliseconds/60000) > 0)
					return String.format("%dm %.03fs", (milliseconds/60000), (milliseconds%60000f/1000));
				else
					return String.format("%.03fs", (milliseconds%60000f/1000));
			}
			default:
			case MILLISECONDS:
				return milliseconds+"";
			case SECONDS: 
				return String.format("%.03f", (milliseconds/1000f));
			case MINUTES: 
				return String.format("%.03f", (milliseconds/60000f));
			case HOURS: 
				return String.format("%.03f", (milliseconds/3600000f));
		}
	}
	
	@Override
	public Double getValue()
	{
		switch (timingType)
		{
			default:
			case MILLISECONDS:
				return Double.valueOf(milliseconds);
			case SECONDS: 
				return Double.valueOf(milliseconds/1000f);
			case MINUTES: 
				return Double.valueOf(milliseconds/60000f);
			case HOURS: 
				return Double.valueOf(milliseconds/3600000f);
		}
	}

	@Override
	public void setValue(Double value)
	{
		milliseconds = value.longValue();
		switch (timingType)
		{
			default:
			case MILLISECONDS:
				break;
			case SECONDS: 
				milliseconds *= 1000;
				break;
			case MINUTES: 
				milliseconds *= 60000;
				break;
			case HOURS: 
				milliseconds *= 3600000;
				break;
		}
		counterField.setText(renderText());
	}
	
	/**
	 * Gets the timing type for this field.
	 */
	public TimeType getTimingType()
	{
		return timingType;
	}

	/**
	 * Sets the timing type for this field.
	 */
	public void setTimingType(TimeType timingType)
	{
		this.timingType = timingType;
	}

}
