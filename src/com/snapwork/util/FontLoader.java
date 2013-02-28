package com.snapwork.util;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;

public class FontLoader
{
	public static FontLoader fontLoader = null;
	public static int currentFontIndex = 1;
	private static FontFamily fntFamily = null;
	private static float scale = 1;

	private static void loadFonts()
	{
		for(int i=0;i<currentFontIndex;i++)
		{
			if(i==(currentFontIndex-1))
			{
				fntFamily = FontFamily.getFontFamilies()[i];
				//System.out.println("Font Bold height"+getFont(AppConstants.BIG_BOLD_FONT).getHeight());
			}
		}
	} 

	public static Font getFont(byte fontType)
	{
		if(fntFamily==null)
			loadFonts();
		Font font;
		if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			scale = (float) 1.5;
		switch(fontType)
		{
		case AppConstants.CHART_SMALL_FONT:
			font = fntFamily.getFont(Font.PLAIN, ImageManager.CalculateRelativeDimensions((int)(06*scale), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.TOC_FONT:
			font = fntFamily.getFont(Font.PLAIN, ImageManager.CalculateRelativeDimensions((int)(14), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.HYPER_LINK_FONT:
			font = fntFamily.getFont(Font.UNDERLINED, ImageManager.CalculateRelativeDimensions((int)(11*scale), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.EXTRA_SMALL_BOLD_FONT:
			font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(8), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			//font = fntFamily.getFont(Font.PLAIN, ImageManager.CalculateRelativeDimensions((int)(8), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.SMALL_PLAIN_FONT:
			font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(11), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));				
			break;
		case AppConstants.SMALL_BOLD_FONT:
			font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(11), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.MEDIUM_BOLD_FONT:
			font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(12*scale), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.MEDIUM_PLAIN_FONT:
			font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(12*scale), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.MEDIUM_SPECIAL_FONT:
			font = fntFamily.getFont(Font.PLAIN, ImageManager.CalculateRelativeDimensions((int)(14*scale), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.BIG_PLAIN_FONT22:
			font = fntFamily.getFont(Font.PLAIN, ImageManager.CalculateRelativeDimensions((int)(22), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.LABEL_FONT:
			font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(16), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.BIG_BOLD_FONT:
			font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(18), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.BIG_PLAIN_FONT:
			font = fntFamily.getFont(Font.PLAIN, ImageManager.CalculateRelativeDimensions((int)(18), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.EXTRA_BOLD_FONT:
			font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(25), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.MEDIUM_SPECIAL_BANNER_FONT:
			font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(13*scale), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.MEDIUM_SPECIAL_BANNER_FONT_FNO:
			font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(13), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		case AppConstants.REPORTS_FONT:
			if(AppConstants.screenHeight<480)
				font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(11), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			else
				font = fntFamily.getFont(Font.BOLD, 14);
			break;
		case AppConstants.REPORTSMARGIN_FONT:
			LOG.print("------------------------- REPORTSMARGIN_FONT");
			if(AppConstants.screenHeight<480)
				font = fntFamily.getFont(Font.PLAIN, 12);
			else
				font = fntFamily.getFont(Font.PLAIN, 16);
			break;
		case AppConstants.REPORTSMARGINBOLD_FONT:
			if(AppConstants.screenHeight<480)
				font = fntFamily.getFont(Font.BOLD, 12);
			else
				font = fntFamily.getFont(Font.BOLD, 16);
			LOG.print("------------------------- REPORTSMARGINBOLD_FONT");
			break;
		default:
			font = fntFamily.getFont(Font.BOLD, ImageManager.CalculateRelativeDimensions((int)(11*scale), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
			break;
		}
		return font;
	}
}