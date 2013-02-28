package com.snapwork.components;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

/**
 * 
 * <p>This class provide the same theme of customLabelField for all Versions of BlackBerry
 *
 */
public class CustomLabelField extends LabelField
{
	private int fontColor;
	public CustomLabelField(String strLabelText,long style,int intColor,Font fntFont) 
	{
		super(strLabelText,style);
		this.setFont(fntFont);
		this.fontColor = intColor;
	}

	protected void onFocus(int direction)
	{
		super.onFocus(direction);
		invalidate();
	}

	protected void onUnfocus() 
	{
		super.onUnfocus();
		invalidate();
	}

	protected void paint(Graphics graphics)
	{
		graphics.setColor(fontColor);
		super.paint(graphics);
	}
	public void setColor(int intColor)
	{
		this.fontColor = intColor;
		invalidate();
	}

}
