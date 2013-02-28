package com.snapwork.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;
/**
 * 
 * <p>This class extends BitmapField which provide two different focus and unfocus images.
 *
 */
public class CustomBitmapButtonField extends BitmapField {
	
	private Bitmap bmpOn,bmpOff;
	public CustomBitmapButtonField(Bitmap bmpOn,Bitmap bmpOff) 
	{
		super(bmpOff,FOCUSABLE);		
		this.bmpOn = bmpOn;
		this.bmpOff = bmpOff;
	}
	


	public int getPreferredWidth() 
	{
		return super.getPreferredWidth();
	}

	protected void onFocus(int direction) 
	{
		this.setBitmap(bmpOn);
		super.onFocus(direction);
		invalidate();
	}
	
	protected void onUnfocus() 
	{
		this.setBitmap(bmpOff);
		super.onUnfocus();
		invalidate();
	}

	protected void drawFocus(Graphics graphics, boolean on)
	{
	}	
	protected void layout(int width, int height) {
		setExtent(this.getBitmapWidth(), this.getBitmapHeight());
	}
	
	
}
