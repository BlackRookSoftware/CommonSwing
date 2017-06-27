package com.blackrook.swing.field;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.blackrook.swing.field.RChangeEventListener;
import com.blackrook.swing.field.RFocusEventListener;
import com.blackrook.swing.field.RInputFieldAbstract;
import com.blackrook.swing.field.RKeyEventListener;

/**
 * Abstract object browse panel for objects with a browseable aspect. 
 * @author Matthew Tropiano
 * @param <T> object.
 * @since 2.7.0
 */
public abstract class RBrowseObjectField<T> extends RInputFieldAbstract<T> implements RChangeEventListener, RKeyEventListener, RFocusEventListener
{
	private static final long serialVersionUID = -5444583054506716619L;
	
	/**
	 * Centralized key handler for all instances of this field.
	 */
	private static class KeyHandler implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			RBrowseObjectField<?> field = (RBrowseObjectField<?>)(e.getComponent().getParent().getParent());
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			{
				field.changeValue();
			}
			field.onKeyPress(e.getKeyCode());
		}
	
		@Override
		public void keyReleased(KeyEvent e)
		{
			((RBrowseObjectField<?>)(e.getComponent().getParent().getParent())).onKeyRelease(e.getKeyCode());
		}
	
		@Override
		public void keyTyped(KeyEvent e)
		{
			((RBrowseObjectField<?>)(e.getComponent().getParent().getParent())).onKeyType();
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
			RBrowseObjectField<?> field = (RBrowseObjectField<?>)(e.getComponent().getParent().getParent());
			field.onFocus();
		}
	
		@Override
		public void focusLost(FocusEvent e)
		{
			RBrowseObjectField<?> field = (RBrowseObjectField<?>)(e.getComponent().getParent().getParent());
			field.changeValue();
			field.onBlur();
		}
	}

	/** The key handler for all instances of this field. */
	protected static final KeyHandler STATIC_KEYHANDLER = new KeyHandler();
	/** The focus handler for all instances of this field. */
	protected static final FocusHandler STATIC_FOCUSHANDLER = new FocusHandler();

	/** Text field. */
	protected JTextField textField;
	/** Label field. */
	private T browsedObject;
	/** Last object. */
	private T lastObject;

	/**
	 * Creates a new object browser panel.
	 * @param title the label title.
	 */
	public RBrowseObjectField(String title)
	{
		this(title, -1, "...");
	}

	/**
	 * Creates a new object browser panel.
	 * @param title the label title.
	 * @param browseButtonName the name of the browse button.
	 */
	public RBrowseObjectField(String title, String browseButtonName)
	{
		this(title, -1, browseButtonName);
	}

	/**
	 * Creates a new object browser panel.
	 * @param title the label title.
	 * @param spacing the spacing width for the label.
	 */
	public RBrowseObjectField(String title, int spacing)
	{
		this(title, spacing, "...");
	}

	/**
	 * Creates a new browser panel.
	 * @param title the label title.
	 * @param spacing the spacing width for the label.
	 * @param browseButtonName the name of the browse button.
	 */
	public RBrowseObjectField(String title, int spacing, String browseButtonName)
	{
		super(title, spacing);
		this.lastObject = null;
		this.browsedObject = null;
		JPanel inputPanel = getInputComponent();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.add(textField = makeTextField(), BorderLayout.CENTER);
		inputPanel.add(makeBrowseButton(browseButtonName), BorderLayout.EAST);
		textField.addFocusListener(STATIC_FOCUSHANDLER);
		textField.addKeyListener(STATIC_KEYHANDLER);
	}
	
	// Make text field.
	private JTextField makeTextField()
	{
		JTextField out = new JTextField();
		out.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return out;
	}
	
	// Make browse button.
	private JButton makeBrowseButton(String name)
	{
		JButton out = new JButton();
		out.setText(name);
		out.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				T obj;
				if ((obj = onBrowse()) != null)
					setValue(obj);
			}
		});
		return out;
	}

	@Override
	public final T getValue()
	{
		return browsedObject;
	}
	
	@Override
	public final void setValue(T value)
	{
		lastObject = browsedObject;
		browsedObject = value;
		textField.setText(getObjectName(value));
		if (lastObject != null && browsedObject != null && !browsedObject.equals(lastObject))
			onChange();
		else if (lastObject != null && browsedObject == null)
			onChange();
		else if (lastObject == null && browsedObject != null)
			onChange();
	}
	
	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		textField.setEnabled(enabled);
	}

	/**
	 * Called when the input field changes to set the internal value. 
	 */
	protected void changeValue()
	{
		setValue(onNameChange(textField.getText()));
	}
	
	/** 
	 * Called when the browse button is clicked.
	 * @return the object selected or null if dialog exited.
	 */
	protected abstract T onBrowse();
	
	/** 
	 * Called when the input field changes.
	 * @param inputName the name to search for to return an object. 
	 * @return the object selected or null if not found.
	 */
	protected abstract T onNameChange(String inputName);

	/** 
	 * Called when the input field needs to change based on the selected object name.
	 * @param object the object.
	 * @return the corresponding name.
	 */
	protected abstract String getObjectName(T object);

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
