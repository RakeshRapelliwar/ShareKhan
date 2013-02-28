package com.snapwork.components;

import com.snapwork.util.AppConstants;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BasicEditField;

/**
 * 
 * <p>This class created customEditField which background white and textcolor black for all versions of BlackBerry OS.
 *
 */

public class CustomBasicEditField2 extends BasicEditField 
{
	public CustomBasicEditField2(long style) 
	{
		super(style);
	}

	protected void paintBackground(Graphics graphics) 
	{
		graphics.setColor(0xeeeeee);
		graphics.fillRect(0,0,getWidth(),getHeight());
		graphics.setColor(Color.BLACK);
	}

	protected void paint(Graphics graphics) 
	{
		graphics.setFont(this.getFont());
		graphics.setColor(Color.BLACK);
		super.paint(graphics);
	}
	/*protected void layout(int width, int height)
	{
		//layout(width, height);
		//setPosition(5, 0);
		super.layout(AppConstants.screenWidth-10, height);
		//setExtent(getPreferredWidth()-10, getPreferredHeight());
	}*/
}
