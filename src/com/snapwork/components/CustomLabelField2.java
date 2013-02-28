package com.snapwork.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

/**
 * 
 * <p>This class provide the same theme of customLabelField for all Versions of BlackBerry
 *
 */
public class CustomLabelField2 extends LabelField
{
	private int fontColor;
	public CustomLabelField2(String strLabelText,long style,int intColor,Font fntFont) 
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
		graphics.setColor(Color.BLACK);
    	graphics.fillRect(5,0,getWidth()-10,getHeight());
    	graphics.setColor(0xeeeeee);
		//graphics.setColor(fontColor);
		super.paint(graphics);
	}
	protected void layout(int width, int height)
	{
		setPosition(5, 0);
		setExtent(getPreferredWidth()-10, getPreferredHeight());
	}
}
