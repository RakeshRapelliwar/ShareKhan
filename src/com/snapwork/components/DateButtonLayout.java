package com.snapwork.components;

import com.snapwork.util.AppConstants;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;

/**
 * 
 * <p>This Manager class displays DateLabel with left and right arrow field.
 * <p>This Manager used in Futures and Options Screen in Options sections.
 *
 */
public class DateButtonLayout extends Manager{

	private int ht;
	public DateButtonLayout()
	{
		super(Manager.VERTICAL_SCROLL | Manager.RIGHTMOST);
	}

	protected void sublayout(int width, int height) 
	{
		Field field;
		int wth = getField(1).getPreferredWidth();
		int w1 = ((AppConstants.screenWidth/2)-(wth/2))-getField(0).getPreferredWidth();
		int w2 = ((AppConstants.screenWidth/2)-(wth/2));
		int w3 = ((AppConstants.screenWidth/2)-(wth/2))+getField(1).getPreferredWidth();
		int numberOfFields = getFieldCount();
		int x = 100;
		int y = 0;
		int i=0;
		for (;i < numberOfFields;i++) {
			field = getField(i); //get the field
			if(i==0)
			{
				x=w1;

			}
			else if(i==1)
			{
				x=w2;

			}
			else if(i==2)
			{
				if(AppConstants.screenHeight>=480)
					w3=w3+10;
				x=w3;
			}
			setPositionChild(field,x,y); //set the position for the field
			layoutChild(field, width, getHt()); //lay out the field
		}
		setExtent(width, getHt());
	}

	public void setHt(int i) {
		this.ht = i;

	}
	public int getHt()
	{
		return this.ht;
	}

}
