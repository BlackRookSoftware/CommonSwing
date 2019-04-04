/*******************************************************************************
 * Copyright (c) 2009-2019 Black Rook Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 ******************************************************************************/
package com.blackrook.swing.field;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.MouseInputListener;

import com.blackrook.commons.math.RMath;
import com.blackrook.commons.math.wave.CustomWaveForm;
import com.blackrook.commons.math.wave.WaveForm;
import com.blackrook.commons.math.wave.WaveFormType;
import com.blackrook.commons.math.wave.WaveUtils;
import com.blackrook.commons.math.wave.CustomWaveForm.InterpolationType;

/**
 * Field for creating/altering a waveform type.
 * @author Matthew Tropiano
 */
public class RWaveTypeField extends RInputFieldAbstract<WaveFormType>
{
	private static final long serialVersionUID = -2964172182676047173L;

	/** Fields that shows the waveform name. */
	protected JTextField waveNameField;
	/** Button to bring up the editor. */
	protected RButton editorButton;
	/** Editor dialog. */
	protected EditDialog editDialog;
	/** The waveform type changed by this field. */
	protected WaveFormType waveFormType;
	
	/**
	 * Constructs a new wave type field with a label name (placed to the left of the
	 * text field) and the field next to the label. If the label name for the 
	 * field is <b>null</b>, then the label will be blank. 
	 * @param labelName this field's label.
	 */
	public RWaveTypeField(String labelName)
	{
		this(labelName, -1);
	}
	
	/**
	 * Constructs a new wave type field with a label name (placed to the left of the
	 * text field) and the field next to the label. If the label name for the 
	 * field is <b>null</b>, then the label will be blank. 
	 * @param labelName	this field's label.
	 * @param labelSpacing the width of the label (for consistent spacing). -1 = don't adjust.
	 * @throws IllegalArgumentException if any of the "spacing" parameters are less than 0.
	 */
	public RWaveTypeField(String labelName, int labelSpacing)
	{
		this(labelName, labelSpacing, WaveForm.NONE);
	}
	
	/**
	 * Constructs a new wave type field with a label name (placed to the left of the
	 * text field) and the field next to the label. If the label name for the 
	 * field is <b>null</b>, then the label will be blank. 
	 * @param labelName	this field's label.
	 * @param labelSpacing the width of the label (for consistent spacing). -1 = don't adjust.
	 * @param waveType the starting wave type.
	 * @throws IllegalArgumentException if any of the "spacing" parameters are less than 0.
	 */
	public RWaveTypeField(String labelName, int labelSpacing, WaveFormType waveType)
	{
		super(labelName, labelSpacing);
		editDialog = new EditDialog();
		JPanel inputPanel = getInputComponent();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(waveNameField = new JTextField(), BorderLayout.CENTER);
		waveNameField.setEnabled(false);
		waveNameField.setBorder(new BevelBorder(BevelBorder.LOWERED));
		inputPanel.add(editorButton = new RButton("...")
		{
			private static final long serialVersionUID = -7402151049201851380L;
			
			@Override
			public void onClick()
			{
				editWave();
			}
			
	}, BorderLayout.EAST);
		setValue(waveType);
	}
	
	/**
	 * Re-evaluates the wave type.
	 */
	protected void performChange()
	{
		if (waveFormType instanceof WaveForm)
			waveNameField.setText(((WaveForm)waveFormType).name());
		else if (waveFormType instanceof CustomWaveForm)
			waveNameField.setText("CUSTOM");
	}
	
	protected void editWave()
	{
		editDialog.setVisible(true);
		performChange();
	}
	
	@Override
	public WaveFormType getValue()
	{
		return waveFormType;
	}

	@Override
	public void setValue(WaveFormType value)
	{
		waveFormType = value;
		performChange();
		editDialog.customCheck();
		editDialog.updateCustomFields();
	}

	/**
	 * Editing dialog for the waveform field.
	 * @author Matthew Tropiano
	 */
	protected class EditDialog extends JDialog
	{
		private static final long serialVersionUID = 7927151035545010617L;
		
		/** Canvas for visualizing, adjusting samples. */
		protected WaveCanvas canvas;
		/** Combo box for wave pull down. */
		protected RComboField<WaveFormType> waveList;
		/** Wave amplitude. */
		protected RDoubleField amplitude;
		/** Sample count. */
		protected RShortField sampleCount;
		/** Interpolation Type. */
		protected RComboField<InterpolationType> interpList;

		public EditDialog()
		{
			this.setTitle("Edit Wave");
			this.setModal(true);
			this.setResizable(false);

			Container c = getContentPane();
			c.setLayout(null);
			c.setPreferredSize(new Dimension(600,300));
			canvas = new WaveCanvas();

			JPanel centerPanel = new JPanel();
			centerPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(canvas, BorderLayout.CENTER);
			centerPanel.setBounds(0, 0, 300, 300);
			
			JPanel rightPanel = new JPanel();
			rightPanel.setLayout(new BorderLayout());
			rightPanel.add(waveList = makeWaveField(), BorderLayout.NORTH);
			rightPanel.setBounds(300, 0, 300, 128);

			JPanel rightBottomPanel = new JPanel();
			rightBottomPanel.setLayout(new GridLayout(3,1));
			rightBottomPanel.setBorder(BorderFactory.createTitledBorder("Custom"));
			rightBottomPanel.add(amplitude = makeAmplitudeField());
			rightBottomPanel.add(sampleCount = makeSampleField());
			rightBottomPanel.add(interpList = makeInterpolationField());
			rightPanel.add(rightBottomPanel, BorderLayout.CENTER);

			c.add(centerPanel, BorderLayout.CENTER);
			c.add(rightPanel, BorderLayout.WEST);
			customCheck();
			pack();
		}
		
		private RComboField<WaveFormType> makeWaveField()
		{
			WaveFormType[] types = new WaveFormType[WaveForm.values().length + 1];
			System.arraycopy(WaveForm.values(), 0, types, 0, WaveForm.values().length);
			types[types.length - 1] = new CustomWaveForm(1.0, 16, InterpolationType.NONE);
			RComboField<WaveFormType> out = new RComboField<WaveFormType>("Type", 64, types)
			{
				private static final long serialVersionUID = -7967166742672884384L;
				@Override
				public void onChange()
				{
					waveFormType = getValue();
					canvas.repaint();
					customCheck();
					updateCustomFields();
				}
			};
			return out;
		}
		
		private RDoubleField makeAmplitudeField()
		{
			RDoubleField out = new RDoubleField("Amplitude", 96)
			{
				private static final long serialVersionUID = -8466421599361532886L;
				@Override
				public void onChange()
				{
					if (waveFormType instanceof CustomWaveForm)
					{
						((CustomWaveForm)waveFormType).scaleAmplitudeInline(getValue());
						canvas.repaint();
						updateCustomFields();
					}
				}
			};
			out.setValue(1.0);
			return out;
		}
		
		private RShortField makeSampleField()
		{
			RShortField out = new RShortField("Samples", 96)
			{
				private static final long serialVersionUID = -3077559972873821288L;

				@Override
				public void onChange()
				{
					if (waveFormType instanceof CustomWaveForm)
					{
						((CustomWaveForm)waveFormType).resampleInline(getValue());
						canvas.repaint();
						updateCustomFields();
					}
				}
			};
			out.setValue((short)1);
			return out;
		}
		
		private RComboField<InterpolationType> makeInterpolationField()
		{
			InterpolationType[] types = new InterpolationType[InterpolationType.values().length];
			System.arraycopy(InterpolationType.values(), 0, types, 0, InterpolationType.values().length);
			RComboField<InterpolationType> out = new RComboField<InterpolationType>("Interpolation", 96, types)
			{
				private static final long serialVersionUID = -7967166742672884384L;
				@Override
				public void onChange()
				{
					if (waveFormType instanceof CustomWaveForm)
					{
						((CustomWaveForm)waveFormType).setInterpolationType(getValue());
						canvas.repaint();
						updateCustomFields();
					}
				}
			};
			return out;
		}
		
		protected void customCheck()
		{
			boolean cust = (waveFormType instanceof CustomWaveForm);
			amplitude.setEnabled(cust);
			sampleCount.setEnabled(cust);
			interpList.setEnabled(cust);
		}
		
		protected void updateCustomFields()
		{
			if (waveFormType instanceof CustomWaveForm)
			{
				CustomWaveForm cwf = (CustomWaveForm)waveFormType;
				amplitude.setValue(cwf.getAmplitude());
				sampleCount.setValue((short)cwf.getSampleCount());
				interpList.setValue(cwf.getInterpolationType());
			}
			else
			{
				amplitude.setValue(1.0);
				sampleCount.setValue((short)1);
				interpList.setValue(InterpolationType.NONE);
			}
		}

		protected class WaveCanvas extends Canvas implements MouseMotionListener, MouseInputListener
		{
			private static final long serialVersionUID = -7196816766008565910L;
			
			private BufferedImage buffer;
			
			WaveCanvas()
			{
				this.setBackground(Color.WHITE);
				this.addMouseMotionListener(this);
				this.addMouseListener(this);
			}
			
			@Override
			public void update(Graphics g)
			{
				if (buffer == null || buffer.getWidth() != getWidth() || buffer.getHeight() != getHeight())
					buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

				Graphics2D g2d = buffer.createGraphics();
				g2d.setColor(Color.WHITE);
				g2d.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
				WaveUtils.drawWave(waveFormType, g2d, getBounds(), Color.GREEN, Color.RED);
				g2d.dispose();
				((Graphics2D)g).drawImage(buffer, null, 0, 0);
			}

			protected void changeSample(MouseEvent e)
			{
				if (!(waveFormType instanceof CustomWaveForm))
					return;

				CustomWaveForm cwf = (CustomWaveForm)waveFormType;
				Rectangle r = getBounds();
				double amp = cwf.getAmplitude();
				double sampleInc = amp / (r.height / 2);
				int sampleIndex = RMath.clampValue(e.getX() / (r.width / cwf.getSampleCount()), 0, cwf.getSampleCount()-1);
				cwf.setSampleValue(sampleIndex, RMath.clampValue(sampleInc * (r.height - e.getY()) - amp, -amp, amp));
				repaint();
			}
			
			@Override
			public void mouseDragged(MouseEvent e)
			{
				changeSample(e);
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				changeSample(e);
			}

			@Override
			public void mouseMoved(MouseEvent e)
			{
			}

			@Override
			public void mouseClicked(MouseEvent e)
			{
			}

			@Override
			public void mouseEntered(MouseEvent e)
			{
			}

			@Override
			public void mouseExited(MouseEvent e)
			{
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
			}
			
		}
		
	}
}
