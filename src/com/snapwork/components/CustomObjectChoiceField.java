package com.snapwork.components;

import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.view.NiftyHomeScreen;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ObjectChoiceField;

/**
 * 
 * <p>This class provides custom Choice Field like Drop Down List.
 *
 */

public class CustomObjectChoiceField extends ObjectChoiceField 
{
	//private final static int itemBackColor = 4343106;
	private boolean id;

	public CustomObjectChoiceField(String string, String[] choices, int i, long style) 
	{
		super(string, choices, i, style);
	}

public boolean isId() {
		return id;
	}

	public void setId(boolean id) {
		this.id = id;
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
		graphics.setFont(this.getFont());
		graphics.setColor(Color.BLACK);
		super.paint(graphics);
	}*/
	public int getPreferredWidth() {
		return AppConstants.screenWidth;
	}
	 protected void layout(int width, int height) {
		 if(isId() && AppConstants.OS>5)
			 super.layout(getPreferredWidth(), FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight());
		 else
			 setExtent(AppConstants.screenWidth, getPreferredHeight());  // force the field to use all available space
	   }
}
