package com.snapwork.components;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.VerticalFieldManager;

/**
 * 
 * <p>This class provides custom LabelField which shows one or more line of text. 
 *
 */

public class CustomMultilineTextField extends VerticalFieldManager 
{
	private int managerWidth;
	private int managerHeight;
	private EditField editField;
	private int fontColor = -1;
	private int backColor = 0x000000;
	public CustomMultilineTextField(int width,int height,Font fnt,int mFontColor)
	{
		super(Manager.NO_VERTICAL_SCROLL);
		this.setFont(fnt);
		fontColor = mFontColor;
		managerWidth = width;
		managerHeight = height;
		VerticalFieldManager vfm =	new VerticalFieldManager(Manager.VERTICAL_SCROLL);
		editField = new EditField(NON_SPELLCHECKABLE)
		{
			public void paint(Graphics g) 
			{
				getManager().invalidate();
				super.paint(g);
			}
		};
		vfm.add(editField);
		add(vfm);
	}
	
	public CustomMultilineTextField(int width,int height,Font fnt,int mFontColor,final int bgColor)
	{
		super(Manager.NO_VERTICAL_SCROLL);
		this.setFont(fnt);
		fontColor = mFontColor;
		managerWidth = width;
		managerHeight = height;
		backColor = bgColor;
		VerticalFieldManager vfm =	new VerticalFieldManager(Manager.VERTICAL_SCROLL)
		{
			protected void paintBackground(Graphics g) {
				g.setColor(backColor);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(fontColor);
			}
		};
		editField = new EditField(NON_SPELLCHECKABLE)
		{
			public void paint(Graphics g) 
			{
				getManager().invalidate();
				super.paint(g);
			}
			
		};
		vfm.add(editField);
		add(vfm);
	}
	
	

	public void paint(Graphics g) 
	{
		g.setColor(fontColor);
		super.paint(g);
	}

	public void sublayout(int width, int height) 
	{
		if (managerWidth == 0) 
		{
			managerWidth = width;
		}
		if (managerHeight == 0) 
		{
			managerHeight = height;
		}
		super.sublayout(managerWidth, managerHeight);
		setExtent(managerWidth, managerHeight);
	}

	public String getText() 
	{
		return editField.getText();
	}

	public void setText(String text) 
	{
		editField.setText(text);
	}
}
