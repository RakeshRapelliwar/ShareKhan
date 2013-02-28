package com.snapwork.components;

import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

/**
 * 
 * <p>This class provide the same theme of customLabelField for all Versions of BlackBerry
 *
 */
public class NoRecordFoundField extends Field
{
	private byte borderWidth = 3;
	private String strLabelText;
	public NoRecordFoundField(String strLabelText,long style) 
	{
		super(style);
		this.strLabelText = strLabelText;
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
		graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
		graphics.setColor(isFocus() == true ? Color.ORANGE : Color.WHITE);
		graphics.fillRoundRect(borderWidth/2,borderWidth/2, getPreferredWidth()-borderWidth, getPreferredHeight()-borderWidth, 10, 10);
		graphics.setColor(0x505050);				
		graphics.fillRoundRect(borderWidth, borderWidth, getPreferredWidth()-borderWidth*2-1, getPreferredHeight()-borderWidth*2-1, 10, 10);
		graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT));
		graphics.setColor(0xffffff);	
		graphics.drawText(this.strLabelText, getPreferredWidth()/2 - graphics.getFont().getAdvance(this.strLabelText)/2, getPreferredHeight()/2 - graphics.getFont().getHeight()/2);
	}
	public int getPreferredWidth() {
		return AppConstants.screenWidth;
	}
	
	public int getPreferredHeight() {
		return FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT).getHeight()*3;
	}
	
	protected void layout(int width, int height) 
	{
		setExtent(getPreferredWidth(), getPreferredHeight());
	}
	
}
