package com.snapwork.components;

import com.snapwork.util.AppConstants;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;

/**
 * 
 * <p>This class used in Futures and Options Screen in Options sections for Call and Put.
 *
 */

public class RadioButtonLayout extends Manager
{
	private int middleDiff = 60;
	public RadioButtonLayout(int middleDiff)
	{
		super(Manager.VERTICAL_SCROLL | Manager.RIGHTMOST);
		this.middleDiff = middleDiff;
	}
	
	public RadioButtonLayout()
	{
		super(Manager.VERTICAL_SCROLL | Manager.RIGHTMOST);
	}

	protected void sublayout(int width, int height)
	{
		Field field;
		//get total number of fields within this manager
		int numberOfFields = getFieldCount();
		int x = 100;
		if(AppConstants.screenWidth==360 && AppConstants.screenHeight==480)
			x = 80;
		else if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			x = 150;
		int y = 5;
		int i=0;
		if(numberOfFields>2)
		{
			setPositionChild(getField(i),30,5);
			/*if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
				layoutChild(getField(i), width, height*3);
			else*/
				layoutChild(getField(i), width, height+7);
			x=x+middleDiff;
			/*if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
				 x=x+70;*/
			i++;
		}
		for (;i < numberOfFields;i++)
		{
			field = getField(i); //get the field
			setPositionChild(field,x,y); //set the position for the field
			if(AppConstants.screenHeight >=480 || AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			{
				layoutChild(getField(i), 80, height*2);
				x += 120;
			}
			else
			{	
			layoutChild(field, width, height+7); //lay out the field
			x += middleDiff;
			}
			/*if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
				x += 60;   */
		}
		height=20;
		/*if(AppConstants.screenWidth==360 && AppConstants.screenHeight==480)
			height = 50;
		else if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
			height = 80;*/
		if(AppConstants.screenHeight>=480)
			height = 45;
		setExtent(width, height+7);
	}

}
