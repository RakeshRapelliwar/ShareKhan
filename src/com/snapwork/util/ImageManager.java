package com.snapwork.util;

import com.snapwork.components.BottomMenu;

import net.rim.device.api.system.AccelerometerSensor;
import net.rim.device.api.system.AccelerometerSensor.Channel;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;

public class ImageManager
{
	private static Bitmap arrow_R=null;
	private static Bitmap arrow_S=null;
	private static Bitmap tradenow=null;
	private static Bitmap smalltradebutton=null;
	private static Bitmap[] reportMenu;
	private static Bitmap[] reportMenuSelect;
	
	private static Bitmap[] bottomMenu;
	private static Bitmap[] bottomMenuSelect;
	
	private static Bitmap[] tradeMenu;
	private static Bitmap[] tradeMenuSelect;
	public static Bitmap getSplashImage()
	{
		Bitmap bmpSplash;
		try
		{
			if (AccelerometerSensor.isSupported())
			switch(Display.getOrientation()) 
			{
			   case Display.ORIENTATION_LANDSCAPE | Display.DIRECTION_LANDSCAPE:
				   AppConstants.screenWidth = Display.getHeight();
				   AppConstants.screenHeight = Display.getWidth();
					break;
			   case Display.ORIENTATION_PORTRAIT | Display.DIRECTION_PORTRAIT:
				   AppConstants.screenWidth = Display.getWidth();
				   AppConstants.screenHeight = Display.getHeight();
					break;
			   case Display.ORIENTATION_SQUARE:
				    /*screenWidth = Display.getWidth();
					 screenHeight = Display.getHeight();
					AppConstants.screenWidth = screenWidth;
					AppConstants.screenHeight = screenHeight;*/
				   AppConstants.screenWidth = Display.getHeight();
				   AppConstants.screenHeight = Display.getWidth();
				   break;
			   default:
				   AppConstants.screenWidth = Display.getWidth();
				   AppConstants.screenHeight = Display.getHeight();
				   break;
			}
			
			if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==480)
				{
					bmpSplash = Bitmap.getBitmapResource("splash_480x640.png");
					if(AppConstants.screenHeight==480)
					{
						bmpSplash = resizeBitmap(bmpSplash,AppConstants.screenWidth, AppConstants.screenHeight );
					}
				}
			else if( AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
				bmpSplash = Bitmap.getBitmapResource("splash_480x800.png");
			else if (AppConstants.screenWidth<321 && AppConstants.screenHeight<241)
				bmpSplash = Bitmap.getBitmapResource("splash.png");
			else 
				bmpSplash = resizeBitmap(Bitmap.getBitmapResource("splash.png"),AppConstants.screenWidth, AppConstants.screenHeight );
		}
		catch (Exception ex)
		{
			bmpSplash = null;
		}
		return bmpSplash;
	}
	public static Bitmap getTradeNow()
	{
		if(tradenow !=null)
			return tradenow;
		try
		{
			tradenow = Bitmap.getBitmapResource("trade-now.png");
		}
		catch (Exception ex)
		{
			tradenow = null;
		}
		return tradenow;
	}

	public static Bitmap getSmallTradeButton()
	{
		if(smalltradebutton !=null)
			return smalltradebutton;
		try
		{
			smalltradebutton = Bitmap.getBitmapResource("smallbutton.png");
		}
		catch (Exception ex)
		{
			smalltradebutton = null;
		}
		return smalltradebutton;
	}
	public static EncodedImage getAppIndicationIcon()
	{
		EncodedImage encodedImage = null;
		try
		{
			encodedImage = EncodedImage.getEncodedImageResource("not_icon.png");
		}
		catch(Exception ex)
		{
			encodedImage = null;
		}
		return encodedImage;
	}

	public static Bitmap getTitleBarImage()
	{
		Bitmap objBMP = null;
		try
		{
			/*objBMP = ResizeImageRelativelyBaseBuildSplash(Bitmap.getBitmapResource("header.png"));
			objBMP = resizeBitmap(objBMP, objBMP.getWidth(), objBMP.getHeight()+4);*/
			objBMP = Bitmap.getBitmapResource("header.png");
			objBMP = resizeBitmap(objBMP,CalculateRelativeDimensions(objBMP.getWidth(),AppConstants.screenWidth,AppConstants.baseBuildScreenWidth),CalculateRelativeDimensions(objBMP.getHeight(),AppConstants.screenHeight,AppConstants.baseBuildScreenHeight)+4);
		}
		catch(Exception ex)
		{
		}
		return objBMP;
	}

	public static Bitmap getLeftArrowImage()
	{
		if(AppConstants.screenHeight>=480)
			return Bitmap.getBitmapResource("arrow_left2.png");
		else
		return ResizeImageRelativelyBaseBuildSplash(Bitmap.getBitmapResource("arrow_left.png"));
	}

	public static Bitmap getRightArrowImage()
	{
		if(AppConstants.screenHeight>=480)
			return Bitmap.getBitmapResource("arrow_right2.png");
		else
			return ResizeImageRelativelyBaseBuildSplash(Bitmap.getBitmapResource("arrow_right.png"));
	}

	public static Bitmap[] getReportsBottomMenuImages(boolean isFocusImage)
	{
		if(isFocusImage)
		{
			if(reportMenuSelect != null)
				return reportMenuSelect;
		}
		else
		{
			if(reportMenu != null)
				return reportMenu;
		}
		
		String[] strImagePath = {"home","watchlist","gain-loose","news"};
		LOG.print("AppConstants.screenWidth : => "+AppConstants.screenWidth);
		LOG.print("AppConstants.screenHeight : => "+AppConstants.screenHeight);
		//if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
		if(AppConstants.screenHeight>240)
		{
			String[] strImagePathTemp = {"hhome","hwatchlist","hgain-loose","hnews"};
			strImagePath = strImagePathTemp;
			strImagePathTemp = null;
		}
		//LOG.print("ImageManager BottomIcons Screen width = "+AppConstants.screenWidth+" and height = "+AppConstants.screenHeight);
		Bitmap[] bitmap = new Bitmap[strImagePath.length];
		if(isFocusImage)
		{
			for(int i=0;i<strImagePath.length;i++)
			{
				bitmap[i] = Bitmap.getBitmapResource(strImagePath[i]+"-S.png");
				//456bitmap[i] = ResizeImageRelativelyBaseBuild(Bitmap.getBitmapResource(strImagePath[i]+"-S.png"));
				//LOG.print("BottomScreen : bitmapWidth = "+bitmap[i].getWidth()+" : bitmapHeight = "+bitmap[i].getHeight());
			}
		}
		else
		{
			for(int i=0;i<strImagePath.length;i++)
			{
				bitmap[i] = Bitmap.getBitmapResource(strImagePath[i]+"-R.png");
				//456bitmap[i] = ResizeImageRelativelyBaseBuild(Bitmap.getBitmapResource(strImagePath[i]+"-R.png"));
				//LOG.print("BottomScreen : bitmapWidth = "+bitmap[i].getWidth()+" : bitmapHeight = "+bitmap[i].getHeight());
			}
		}
		if(isFocusImage)
			reportMenuSelect = bitmap;
		else
			reportMenu = bitmap;
		return bitmap;
	}
	
	public static Bitmap[] getBottomMenuImages(boolean isFocusImage)
	{
		if(isFocusImage)
		{
			if(bottomMenuSelect != null)
				return bottomMenuSelect;
		}
		else
		{
			if(bottomMenu != null)
				return bottomMenu;
		}
		String[] strImagePath = {"home","watchlist","gain-loose","news","getquote"};
		String[] strImagePathBig = {"hhome","hwatchlist","hgain-loose","hnews","hgetquote"};
		boolean flag = false;
		//if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
		LOG.print("AppConstants.screenWidth : => "+AppConstants.screenWidth);
		LOG.print("AppConstants.screenHeight : => "+AppConstants.screenHeight);
		if(AppConstants.screenHeight>240)
		{
			/*String[] strImagePathTemp = {"hhome","hwatchlist","hgain-loose","hnews","hgetquote"};
			strImagePath = strImagePathTemp;
			strImagePathTemp = null;*/
			flag = true;
		}
		//LOG.print("ImageManager BottomIcons Screen width = "+AppConstants.screenWidth+" and height = "+AppConstants.screenHeight);
		Bitmap[] bitmap = new Bitmap[strImagePath.length];
		
		LOG.print(" strImagePath[0] : "+strImagePath[0]);
		if(isFocusImage)
		{
			for(int i=0;i<strImagePath.length;i++)
			{
				if(flag)
					bitmap[i] = Bitmap.getBitmapResource(strImagePathBig[i]+"-S.png");
					//456bitmap[i] = ResizeImageRelativelyBaseBuild(Bitmap.getBitmapResource(strImagePathBig[i]+"-S.png"));
				else
					bitmap[i] = Bitmap.getBitmapResource(strImagePath[i]+"-S.png");
					//456bitmap[i] = ResizeImageRelativelyBaseBuild(Bitmap.getBitmapResource(strImagePath[i]+"-S.png"));
				//LOG.print("BottomScreen : bitmapWidth = "+bitmap[i].getWidth()+" : bitmapHeight = "+bitmap[i].getHeight());
			}
		}
		else
		{
			for(int i=0;i<strImagePath.length;i++)
			{
				if(flag)
					bitmap[i] = Bitmap.getBitmapResource(strImagePathBig[i]+"-R.png");
					//456bitmap[i] = ResizeImageRelativelyBaseBuild(Bitmap.getBitmapResource(strImagePathBig[i]+"-R.png"));
			else
				bitmap[i] = Bitmap.getBitmapResource(strImagePath[i]+"-R.png");
				//456bitmap[i] = ResizeImageRelativelyBaseBuild(Bitmap.getBitmapResource(strImagePath[i]+"-R.png"));
				//LOG.print("BottomScreen : bitmapWidth = "+bitmap[i].getWidth()+" : bitmapHeight = "+bitmap[i].getHeight());
			}
		}
		if(isFocusImage)
			bottomMenuSelect = bitmap;
		else
			bottomMenu = bitmap;
		return bitmap;
	}
	
	public static Bitmap[] getBottomTradeMenuImages(boolean isFocusImage)
	{
		if(isFocusImage)
		{
			if(tradeMenuSelect != null)
				return tradeMenuSelect;
		}
		else
		{
			if(tradeMenu != null)
				return tradeMenu;
		}
		String[] strImagePath = {"home","watchlist","reportsb","tradeb"};
		//if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
		if(AppConstants.screenHeight>240 )
		{
			String[] strImagePathTemp = {"hhome","hwatchlist","hreportsb","htradeb"};
			strImagePath = strImagePathTemp;
			strImagePathTemp = null;
		}
		//LOG.print("ImageManager BottomIcons Screen width = "+AppConstants.screenWidth+" and height = "+AppConstants.screenHeight);
		Bitmap[] bitmap = new Bitmap[strImagePath.length];
		if(isFocusImage)
		{
			for(int i=0;i<strImagePath.length;i++)
			{
				bitmap[i] = Bitmap.getBitmapResource(strImagePath[i]+"-S.png");
				//456bitmap[i] = ResizeImageRelativelyBaseBuild(Bitmap.getBitmapResource(strImagePath[i]+"-S.png"));
				//LOG.print("BottomScreen : bitmapWidth = "+bitmap[i].getWidth()+" : bitmapHeight = "+bitmap[i].getHeight());
			}
		}
		else
		{
			for(int i=0;i<strImagePath.length;i++)
			{
				bitmap[i] = Bitmap.getBitmapResource(strImagePath[i]+"-R.png");
				//456bitmap[i] = ResizeImageRelativelyBaseBuild(Bitmap.getBitmapResource(strImagePath[i]+"-R.png"));
				//LOG.print("BottomScreen : bitmapWidth = "+bitmap[i].getWidth()+" : bitmapHeight = "+bitmap[i].getHeight());
			}
		}
		if(isFocusImage)
			tradeMenuSelect = bitmap;
		else
			tradeMenu = bitmap;
		return bitmap;
	}
	private static Bitmap icon;
	public static Bitmap getIcon()
	{
		if(icon==null)
		{
			icon = Bitmap.getBitmapResource("icon.png");
		}
		return icon;
	}

	private static Bitmap remove;
	public static Bitmap getRemove()
	{
		if(remove==null)
		{
			remove = Bitmap.getBitmapResource("remove.png");
		}
		return remove;
	}
	private static Bitmap refreshbtn;
	public static Bitmap getRefreshButton()
	{
		if(refreshbtn==null)
		{
			refreshbtn = Bitmap.getBitmapResource("refresh-R.png");
			if(AppConstants.screenHeight>240)
				{
				if(AppConstants.screenHeight==480)
					refreshbtn = Bitmap.getBitmapResource("refresh-normal.png");
			else {
				refreshbtn = Bitmap.getBitmapResource("refresh-normal.png");
				int hgt = getTitleBarImage().getHeight()-12;
				int wth = (refreshbtn.getWidth()* hgt)/refreshbtn.getHeight();
				LOG.print("refreshButton width="+wth+" height="+hgt);
					refreshbtn = resizeBitmap(refreshbtn, wth, hgt);
				}
				}
		}
		return refreshbtn;
	}
	private static Bitmap refreshbtnloading;
	public static Bitmap getRefreshButtonLoading()
	{
		if(refreshbtnloading==null)
		{
			refreshbtnloading = Bitmap.getBitmapResource("refresh-S.png");
			if(AppConstants.screenHeight>240)
			{
				if(AppConstants.screenHeight==480)
					refreshbtnloading = Bitmap.getBitmapResource("refresh-dotted.png");
			else {
				refreshbtnloading = Bitmap.getBitmapResource("refresh-dotted.png");
				int hgt = getTitleBarImage().getHeight()-12;
				int wth = (refreshbtnloading.getWidth()* hgt)/refreshbtnloading.getHeight();
				refreshbtnloading = resizeBitmap(refreshbtnloading, wth, hgt);
			}}
			}
		return refreshbtnloading;
	}
	public static Bitmap getArrow(boolean isFocusImage)
	{
		if(isFocusImage)
		{
			if(arrow_S==null)
				arrow_S = Bitmap.getBitmapResource("arrow-S.png");
			return arrow_S;
		}
		else
		{	
			if(arrow_R==null)
				arrow_R = Bitmap.getBitmapResource("arrow-R.png");
			return arrow_R;
		}
	}

	public static Bitmap[] getBottomMenuImagesSetting(boolean isFocusImage)
	{
		String[] strImagePath = {"watchlist","commentry","gain-loose","news","getquote","settings"};
		Bitmap[] bitmap = new Bitmap[strImagePath.length];
		if(isFocusImage)
		{
			for(int i=0;i<strImagePath.length;i++)
			{
				bitmap[i] = ResizeImageRelativelyBaseBuild(Bitmap.getBitmapResource(strImagePath[i]+"-S.png"));
				LOG.print("BottomMenuScreen : bitmapWidth = "+bitmap[i]+" : bitmapHeight = "+bitmap[i]);
			}
		}
		else
		{
			for(int i=0;i<strImagePath.length;i++)
			{
				bitmap[i] = ResizeImageRelativelyBaseBuild(Bitmap.getBitmapResource(strImagePath[i]+"-R.png"));
				LOG.print("BottomMenuScreen : bitmapWidth = "+bitmap[i]+" : bitmapHeight = "+bitmap[i]);
			}
		}
		return bitmap;
	}

	private static int[] rescaleArray(int[] ini, int x, int y, int x2, int y2)
	{
		int out[] = new int[x2*y2];
		for (int yy = 0; yy < y2; yy++)
		{
			int dy = yy * y / y2;
			for (int xx = 0; xx < x2; xx++)
			{
				int dx = xx * x / x2;
				out[(x2 * yy) + xx] = ini[(x * dy) + dx];
			}
		}
		return out;
	}


	public static synchronized Bitmap resizeBitmap(Bitmap image, int width, int height)
	{       
		// Note from DCC:
		// an int being 4 bytes is large enough for Alpha/Red/Green/Blue in an 8-bit plane...
		// my brain was fried for a little while here because I am used to larger plane sizes for each
		// of the color channels....
		//
		if(image.getWidth()==width && image.getHeight()==height)
		{
			return image;
		}
		//Need an array (for RGB, with the size of original image)
		//
		int rgb[] = new int[image.getWidth()*image.getHeight()];

		//Get the RGB array of image into "rgb"
		//


		image.getARGB(rgb, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

		//image.
		//Call to our function and obtain RGB2
		//
		int rgb2[] = rescaleArray(rgb, image.getWidth(), image.getHeight(), width, height);

		//Create an image with that RGB array
		//
		Bitmap temp2 = new Bitmap(width, height);

		temp2.setARGB(rgb2, 0, width, 0, 0, width, height);

		return temp2;
	}

	public static Bitmap bestFit(Bitmap image, int maxWidth, int maxHeight)
	{

		// getting image properties
		int w = image.getWidth();
		int h = image.getHeight();

		//  get the ratio
		int ratiow = 100 * maxWidth / w;
		int ratioh = 100 * maxHeight / h;

		// this is to find the best ratio to
		// resize the image without deformations
		int ratio = Math.min(ratiow, ratioh);

		// computing final desired dimensions
		int desiredWidth = w * ratio / 100;
		int desiredHeight = h * ratio / 100;

		//resizing
		return resizeBitmap(image, desiredWidth, desiredHeight);
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int angle)
	{
		if (angle == 0)
		{
			return bitmap;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int[] rowData = new int[width];
		int[] rotatedData = new int[width * height];

		int rotatedIndex = 0;

		for (int i = 0; i < height; i++)
		{
			bitmap.getARGB(rowData, 0, width, 0, i, width, 1);

			for (int j = 0; j < width; j++)
			{
				rotatedIndex = angle == 90 ? (height - i - 1) + j * height
						: (angle == 270 ? i + height * (width - j - 1) : width
								* height - (i * width + j) - 1);

				rotatedData[rotatedIndex] = rowData[j];
			}
		}
		Bitmap rotatedBitmap;
		if (angle == 90 || angle == 270)
		{
			rotatedBitmap = new Bitmap(height, width);
			rotatedBitmap.setARGB(rotatedData, 0, height, 0, 0, height, width);
		}
		else
		{
			rotatedBitmap = new Bitmap(width, height);
			rotatedBitmap.setARGB(rotatedData, 0, width, 0, 0, width, height);
		}
		return rotatedBitmap;
	}

	public static Bitmap ResizeImageRelativelyBaseBuild(Bitmap bmpTarget)
	{
		//return ImageManager.resizeBitmap(bmpTarget,CalculateRelativeDimensions(bmpTarget.getWidth(),AppConstants.screenWidth,AppConstants.baseBuildScreenWidth),CalculateRelativeDimensions(bmpTarget.getHeight(),AppConstants.screenHeight,AppConstants.baseBuildScreenHeight));
		BottomMenu.BottomMenuImageHeight = CalculateRelativeDimensions(bmpTarget.getWidth(),AppConstants.screenWidth,AppConstants.baseBuildScreenWidth)*bmpTarget.getHeight()/bmpTarget.getWidth();
		LOG.print("Base Icon width "+CalculateRelativeDimensions(bmpTarget.getWidth(),AppConstants.screenWidth,AppConstants.baseBuildScreenWidth));
		LOG.print("Base Icon height "+CalculateRelativeDimensions(bmpTarget.getWidth(),AppConstants.screenWidth,AppConstants.baseBuildScreenWidth)*bmpTarget.getHeight()/bmpTarget.getWidth());
		return ImageManager.resizeBitmap(bmpTarget,CalculateRelativeDimensions(bmpTarget.getWidth(),AppConstants.screenWidth,AppConstants.baseBuildScreenWidth),CalculateRelativeDimensions(bmpTarget.getWidth(),AppConstants.screenWidth,AppConstants.baseBuildScreenWidth)*bmpTarget.getHeight()/bmpTarget.getWidth());
	}

	public static Bitmap ResizeImageRelativelyBaseBuildSplash(Bitmap bmpTarget)
	{
		return ImageManager.resizeBitmap(bmpTarget,CalculateRelativeDimensions(bmpTarget.getWidth(),AppConstants.screenWidth,AppConstants.baseBuildScreenWidth),CalculateRelativeDimensions(bmpTarget.getHeight(),AppConstants.screenHeight,AppConstants.baseBuildScreenHeight));
	}

	public static int CalculateRelativeDimensions(int factor,int origDimenstion,int baseDimension)
	{
		if(origDimenstion==baseDimension)
		{
			return factor;
		}
		return (factor*origDimenstion/baseDimension);
	}
	
	public static int CalculateRelativeDimensions(int factor,int origDimenstion)
	{
		if(origDimenstion==320)
		{
			return factor;
		}
		return (factor*origDimenstion/320);
	}

}
