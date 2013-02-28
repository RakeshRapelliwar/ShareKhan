package com.snapwork.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BasicEditField;

/**
 * 
 * <p>This class created customEditField which background white and textcolor black for all versions of BlackBerry OS.
 *
 */

public class CustomBasicEditFieldSearchEF extends BasicEditField 
{
	private int color = 0xeeeeee;
	public CustomBasicEditFieldSearchEF(long style) 
	{
		super(style);
	}

	protected void paintBackground(Graphics graphics) 
	{
		graphics.setColor(color);
		graphics.fillRect(0,0,getPreferredWidth(),getPreferredHeight());
		graphics.setColor(Color.BLACK);
	}

	protected void paint(Graphics graphics) 
	{
		graphics.setFont(this.getFont());
		graphics.setColor(Color.BLACK);
		super.paint(graphics);
	}
	
	public void setColor(int col)
	{
		color = col;
		invalidate();
	}

}
