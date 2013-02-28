package com.snapwork.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import com.snapwork.actions.ActionListener;
import com.snapwork.util.AppConstants;
import com.snapwork.util.ImageManager;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.Utils;
import com.snapwork.view.CurrentStat;
import com.snapwork.view.MyWatchList;

/**
 * 
 * <p>This class shows bottom menu of six images home, watchlist, commentary, news, top loosers gainers and company search.
 * <p>This class menus activated by pressing blackberry default menu button.
 *
 */
public class BottomMenu 
{
	private HorizontalFieldManager hfmBottomMenuBar;
	private VerticalFieldManager vfmBottomMenu;
	private MainScreen objParentScreen;
	private static int itemHeight;
	private static boolean isHeightUnset = true;
	public  boolean isAttached;
	private final static int BannerItemHeight = 0;//28;
	public static int BottomMenuImageHeight = ImageManager.CalculateRelativeDimensions(42, AppConstants.screenHeight, AppConstants.baseBuildScreenHeight);

	public BottomMenu(long lngStyle) {
		vfmBottomMenu = new VerticalFieldManager(Manager.USE_ALL_WIDTH) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		hfmBottomMenuBar = new HorizontalFieldManager(lngStyle);
	}

	public void autoAttachDetachFromScreen() 
	{
		if(isAttached) 
		{
			detachFromScreen();  
		} 
		else 
		{
			attachToScreen();
		}
	}

	public void attachToScreen() 
	{
		isAttached = true;
		vfmBottomMenu.add(hfmBottomMenuBar);
	}

	public void detachFromScreen() 
	{
		isAttached = false;
		vfmBottomMenu.delete(vfmBottomMenu.getField(vfmBottomMenu.getFieldCount()-1));
	}

	private static void setItemHeight(int height) 
	{
		if(isHeightUnset) 
		{
			itemHeight = height;
			isHeightUnset = false;
		}
	}

	public static int getItemHeight() 
	{
		return itemHeight;
	}

	public static BottomMenu getBottomMenuInstance(MainScreen objParentScreen,Bitmap[] bmpFocusIcons,Bitmap[] bmpUnFocusIcons,byte selectedCommand,byte[] intCommands) 
	{
		BottomMenu objBM = null;
		try {
			if(objParentScreen!=null) 
			{
				
				
				
				objBM = new BottomMenu(Manager.FIELD_HCENTER | Manager.FIELD_VCENTER);
				BottomMenu.setItemHeight(BannerItemHeight);
				objBM.createMenuBar(objParentScreen, bmpFocusIcons, bmpUnFocusIcons, selectedCommand,intCommands);
				objParentScreen.setStatus(objBM.vfmBottomMenu);
			}
		}
		catch(Exception ex) 
		{
			objBM = null;
		}
		return objBM;
	}

	public void createMenuBar(MainScreen objParentScreen,Bitmap[] bmpFocusIcons,Bitmap[] bmpUnFocusIcons,byte selectedCommand,byte[] intCommands) 
	{
		this.objParentScreen = objParentScreen;
		for(int i = 0; i < bmpFocusIcons.length ; i++) 
		{
			
			BitmapField objField;
			if(intCommands[i]==selectedCommand && AppConstants.keepBottomItemSelected)
				objField = createBitmapField(bmpFocusIcons[i],bmpFocusIcons[i],BitmapField.FOCUSABLE | BitmapField.FIELD_HCENTER | BitmapField.FIELD_VCENTER,intCommands[i]);
			else 
				objField = createBitmapField(bmpFocusIcons[i],bmpUnFocusIcons[i],BitmapField.FOCUSABLE | BitmapField.FIELD_HCENTER | BitmapField.FIELD_VCENTER,intCommands[i]);
			if(objField!=null)
				hfmBottomMenuBar.add(objField);
		}
	}

	public BitmapField createBitmapField(final Bitmap bmpFocusIcon,final Bitmap bmpUnFocusIcon,long lngStyle,final byte command) 
	{
		BitmapField objField = new BitmapField(bmpUnFocusIcon, lngStyle) 
		{
			private boolean isFocused = false;
			protected void onFocus(int direction) 
			{
				isFocused = true;
				invalidate();
			}
			protected void onUnfocus() {
				isFocused = false;
				invalidate();
			}
			protected void paint(Graphics graphics) 
			{
				if(isFocused) {
					graphics.drawBitmap((AppConstants.screenWidth/AppConstants.bottomMenuCommands.length-getBitmapWidth())/2, 0, getBitmapWidth(), getBitmapHeight(), bmpFocusIcon, 0, 0);
				} else {
					graphics.drawBitmap((AppConstants.screenWidth/AppConstants.bottomMenuCommands.length-getBitmapWidth())/2, 0, getBitmapWidth(), getBitmapHeight(), bmpUnFocusIcon, 0, 0);                                        
				}
			}
			protected void drawFocus(Graphics graphics, boolean on) 
			{
//Top Gainers
			}
			protected void layout(int width, int height) 
			{
				setExtent(AppConstants.screenWidth / AppConstants.bottomMenuCommands.length,getPreferredHeight());
			}
			protected boolean navigationClick(int status, int time) 
			{
				AppConstants.NSE = false;
				Utils.FNO_SEARCH = false;
				MyWatchList.isFINISHED = true;
				MyWatchList.REFRESH = false;
				if(objParentScreen instanceof ActionListener) 
				{
					((ActionListener)objParentScreen).actionPerfomed(command,null);
				}
				
				return true;
			}
		};
		return objField;
	}
}
