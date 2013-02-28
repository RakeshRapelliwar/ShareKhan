package com.snapwork.components;

import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.LOG;
import com.snapwork.util.Utils;
import com.snapwork.view.trade.TradeNowMainScreen;
import com.snapwork.view.trade.TradeNowMainScreenCM;

/**
 * 
 * <p>This class shows loading message like Loading Intraday Chart 
 *
 */
public class WaitScreen extends PopupScreen implements RemovableScreen
{
	public static boolean HTTPCALL = false;
	public static int HTTPCALL_ID = 0;
	public WaitScreen(String message)
	{
		super(new VerticalFieldManager()
		{
			protected void sublayout(int width,int height)
			{
				layoutChild(this.getField(0), width, height);
				setPositionChild(this.getField(0), Snippets.padding, Snippets.padding);
				setExtent((AppConstants.screenWidth * 80)/100, this.getField(0).getFont().getHeight()+Snippets.padding*2);
			}
		},NON_FOCUSABLE | USE_ALL_WIDTH | USE_ALL_HEIGHT);
		
		LabelField labelField = new LabelField(message,FIELD_VCENTER | USE_ALL_WIDTH | USE_ALL_HEIGHT);
		labelField.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
		add(labelField);
	}
	
	public WaitScreen(String message,byte font)
	{
		super(new VerticalFieldManager()
		{
			protected void sublayout(int width,int height)
			{
				layoutChild(this.getField(0), width, height);
				setPositionChild(this.getField(0), Snippets.padding, Snippets.padding);
				setExtent((AppConstants.screenWidth * 80)/100, this.getField(0).getFont().getHeight()+Snippets.padding*2);
			}
		},NON_FOCUSABLE | USE_ALL_WIDTH | USE_ALL_HEIGHT);
		
		LabelField labelField = new LabelField(message,FIELD_VCENTER | USE_ALL_WIDTH | USE_ALL_HEIGHT);
		labelField.setFont(FontLoader.getFont(font));
		add(labelField);
	}
	
	public boolean onClose()
	{
//		HTTPCALL_ID = 0;
//		TradeNowMainScreen.tradeStart = false;
//		TradeNowMainScreenCM.tradeStart = false;
//		if(Utils.LOGIN_STATUS)
//		{
//			if(HTTPCALL)
//			{
//				synchronized( UiApplication.getEventLock() ){
//		            
//		            if(isDisplayed()) 
//		         	   {
//		            	HTTPCALL = false;
//						close();
//
//		         	   }
//		}
//				/*if(TradeNowMainScreen.CLOSE){
//synchronized( UiApplication.getEventLock() ){
//		            
//		            if(isDisplayed()) 
//		         	   {
//		            	if(UiApplication.getUiApplication().getActiveScreen() instanceof TradeNowMainScreen)
//		    			{
//		            		TradeNowMainScreen.CLOSE = false;
//		            		UiApplication.getUiApplication().getActiveScreen().close();
//		    			}
//
//		         	   }
//		}
//}*/
//			}
//			else
//			{
//				ActionInvoker.processCommand(new Action(ActionCommand.CMD_GRID_SCREEN,null));
//			}
//		}
//		else
//		{
//			System.exit(0);
//		}
		
		UiApplication.getUiApplication().popScreen(this);
		
		return false;
	}
	
	protected boolean keyDown( int keyCode, int time ) {
		int key = Keypad.key( keyCode );
		if(key == Keypad.KEY_END)
		{
			LOG.print("KEY_END EXIT from app");
			System.exit(0);
		}
		return super.keyDown(keyCode, time);
	}
	
}
