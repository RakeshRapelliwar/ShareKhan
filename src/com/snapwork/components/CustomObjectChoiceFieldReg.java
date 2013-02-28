package com.snapwork.components;

import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.ObjectChoiceField;

/**
 * 
 * <p>This class provides custom Choice Field like Drop Down List.
 *
 */

public class CustomObjectChoiceFieldReg extends ObjectChoiceField 
{
	//private final static int itemBackColor = 4343106;
	

	public CustomObjectChoiceFieldReg(String string, String[] choices, int i, long style) 
	{
		super(string, choices, i, style);
	}
	
	

/*	protected void paintBackground(Graphics graphics) 
	{
		graphics.setColor(0xeeeeee);
		graphics.fillRect(0,0,getWidth(),getHeight());
		graphics.setColor(itemBackColor);
		graphics.fillRoundRect(2, 2, getPreferredWidth()-4, getPreferredHeight()-4, 10, 10);
		graphics.drawBitmap(getPreferredWidth()-25, (getPreferredHeight()/2)-(ImageManager.getArrow(true).getHeight()/2), ImageManager.getArrow(true).getWidth(), ImageManager.getArrow(true).getHeight(), ImageManager.getArrow(true), 0, 0);
		graphics.setColor(Color.BLACK);
	}

	
	protected void paint(Graphics graphics) 
	{
		graphics.setColor(0x000000);
		graphics.fillRect(0, 0, getWidth(), getHeight());
		graphics.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
		graphics.setColor(0xeeeeee);
		graphics.drawText("User Type", 10, (getPreferredHeight()/2)-(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight()/2));
		super.paint(graphics);
	}*/
	public int getPreferredWidth() {
		return (AppConstants.screenWidth/2)-20;
	}
	public int getPreferredHeight() {
		return  FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight();
	}
	 protected void layout(int width, int height) {
		  super.layout(getPreferredWidth(), getPreferredHeight());
	      //setExtent(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance("TradingPassw") + Snippets.padding*2, height);  // force the field to use all available space
	   }
}
