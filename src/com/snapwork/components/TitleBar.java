package com.snapwork.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;

import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;

/**
 * 
 * <p>This class creates Title bar layout like adding background image and text at title.
 * <p>All View Screens used this class for showing title bar.
 *
 */
public class TitleBar extends Manager
{
	private static Bitmap bmpTitleBarBg = null;
	private static int width = 0,height = 0;
	public static byte padding = 5;
	
	public TitleBar(String strTitleText) {
		super(NON_FOCUSABLE);

		if(bmpTitleBarBg==null) {
			bmpTitleBarBg = ImageManager.getTitleBarImage();
			
			width = bmpTitleBarBg.getWidth();
			height = bmpTitleBarBg.getHeight();
		}
		if(strTitleText.indexOf("&amp;")>-1)
			strTitleText = strTitleText.substring(0,strTitleText.indexOf("&amp;")+1)+strTitleText.substring(strTitleText.indexOf("&amp;")+5,strTitleText.length());
		
		if(strTitleText.length()>32)
			strTitleText = strTitleText.substring(0, 30)+"...";
		CustomLabelField lblTitleText = new CustomLabelField(strTitleText, 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		add(lblTitleText);
		//add(AdsManager.getAdsManager(Manager.FIELD_RIGHT | Manager.NO_HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR));
	}

	public static int getItemHeight() {
		return height;
	}
	
	public int getPreferredHeight() {
		return height;
	}

	public int getPreferredWidth() {
		return width;
	}
	
	public static int getStringWidth(String text) {
		return FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance(text);
	}
	
    protected void paintBackground(Graphics graphics) {
		if(bmpTitleBarBg!=null) {
			graphics.drawBitmap(0, 0, bmpTitleBarBg.getWidth(), bmpTitleBarBg.getHeight(), bmpTitleBarBg, 0, 0);
		}
	}
	
	protected void sublayout(int width, int height) {
		layoutChild(this.getField(0), width, height);
		
		setPositionChild(this.getField(0), padding, getPreferredHeight()/2 - this.getField(0).getFont().getHeight()/2);
		
		setExtent(Math.min(width, getPreferredWidth()),Math.min(height, getPreferredHeight()));
	}

}
