package com.snapwork.util;

import net.rim.device.api.system.AccelerometerSensor;
import net.rim.device.api.system.Display;
import com.snapwork.components.Snippets;
import com.snapwork.components.TitleBar;

public class AppConfigurer
{
	public static void configure()
	{
		int screenWidth ;
		int screenHeight ;
		if (AccelerometerSensor.isSupported())
		switch(Display.getOrientation()) 
		{
		   case Display.ORIENTATION_LANDSCAPE | Display.DIRECTION_LANDSCAPE:
			    screenWidth = Display.getHeight();
				 screenHeight = Display.getWidth();
				AppConstants.screenWidth = screenHeight;
				AppConstants.screenHeight = screenWidth;
				break;
		   case Display.ORIENTATION_PORTRAIT | Display.DIRECTION_PORTRAIT:
			   screenWidth = Display.getWidth();
				 screenHeight = Display.getHeight();
				AppConstants.screenWidth = screenWidth;
				AppConstants.screenHeight = screenHeight;
				break;
		   case Display.ORIENTATION_SQUARE:
			    /*screenWidth = Display.getWidth();
				 screenHeight = Display.getHeight();
				AppConstants.screenWidth = screenWidth;
				AppConstants.screenHeight = screenHeight;*/
			   screenWidth = Display.getHeight();
				 screenHeight = Display.getWidth();
				AppConstants.screenWidth = screenHeight;
				AppConstants.screenHeight = screenWidth;
				break;
		   default:
			    screenWidth = Display.getWidth();
				 screenHeight = Display.getHeight();
				AppConstants.screenWidth = screenWidth;
				AppConstants.screenHeight = screenHeight; break;
		}
		else
		{
			 screenWidth = Display.getWidth();
		 screenHeight = Display.getHeight();
		AppConstants.screenWidth = screenWidth;
		AppConstants.screenHeight = screenHeight;
		}
		if(screenWidth==AppConstants.baseBuildScreenWidth && screenHeight==AppConstants.baseBuildScreenHeight)
		{
			return;
		}
		Snippets.BlockHeight = ImageManager.CalculateRelativeDimensions(Snippets.BlockHeight, screenHeight, AppConstants.baseBuildScreenHeight);
		Snippets.padding = (byte)ImageManager.CalculateRelativeDimensions(Snippets.padding, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
		Snippets.BlockImageWidth = screenWidth - Snippets.padding*2;
		Snippets.BlockImageHeight = ImageManager.CalculateRelativeDimensions(Snippets.BlockImageHeight, screenHeight, AppConstants.baseBuildScreenHeight); 
		Snippets.BitmapWidth = ImageManager.CalculateRelativeDimensions(Snippets.BitmapWidth, screenWidth, AppConstants.baseBuildScreenWidth);
		Snippets.BitmapHeight = ImageManager.CalculateRelativeDimensions(Snippets.BitmapHeight, screenHeight, AppConstants.baseBuildScreenHeight);
		AppConstants.padding = (byte)ImageManager.CalculateRelativeDimensions(AppConstants.padding, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
		TitleBar.padding = (byte)ImageManager.CalculateRelativeDimensions(TitleBar.padding, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
	}
}