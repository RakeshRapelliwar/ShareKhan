package com.snapwork.view;

import java.util.Vector;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.components.WaitScreen;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.parsers.WatchListJsonParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.Utils;

public class WatchListFill implements ReturnString
{
	private boolean flag = false;
	private ReturnStringParser returnStringParser;
	
	public WatchListFill(String url, boolean flag)
	{
		this.flag = flag;
		if(Utils.WATCHLIST_NAME.substring(Utils.WATCHLIST_NAME.length()-3, Utils.WATCHLIST_NAME.length()).equalsIgnoreCase("NSE") || Utils.WATCHLIST_NAME.equalsIgnoreCase("NIFTY"))
		{if(url.indexOf("flag=")<0)
		{
			url = url + "&flag=1";
		}
		}
		WaitScreen.HTTPCALL = true;
		WaitScreen.HTTPCALL_ID = 17;
		returnStringParser = new ReturnStringParser(url, 1, this, true);
	}

	public void setReturnString(String strResponse, int id) {
		LOG.print("WatchListFill ---=====:::::>>>>> false"+MyWatchList.isFINISHED);
		if(!MyWatchList.isFINISHED)
		{if(!WaitScreen.HTTPCALL)
		{
			Utils.WATCHLIST_CLOSE = true;
        	LOG.print("MyWatchList Utils.WATCHLIST_CLOSE"+Utils.WATCHLIST_CLOSE);
        	Action action = new Action(ActionCommand.CMD_WATCHLIST_SCREEN, null);
        	ActionInvoker.processCommand(action);
			return;
		}
		else if(WaitScreen.HTTPCALL_ID == 0)
		{
			Utils.WATCHLIST_CLOSE = true;
        	LOG.print("MyWatchList Utils.WATCHLIST_CLOSE"+Utils.WATCHLIST_CLOSE);
        	Action action = new Action(ActionCommand.CMD_WATCHLIST_SCREEN, null);
        	ActionInvoker.processCommand(action);
			return;
		}
		LOG.print(flag+"Utils.WATCHLIST_CLOSE : "+Utils.WATCHLIST_CLOSE);
		//if(!(UiApplication.getUiApplication().getActiveScreen() instanceof MyWatchList) || (UiApplication.getUiApplication().getActiveScreen() instanceof WatchListScreen) )
		/*if(Utils.WATCHLIST_CLOSE)
		{
			return;
		}*/
		if(strResponse.length()==0)
		{
			if(UiApplication.getUiApplication().getActiveScreen() instanceof WaitScreen)
				  UiApplication.getUiApplication().invokeAndWait(new Runnable() {
                      public void run() {
                              ScreenInvoker.removeRemovableScreen();
                              Dialog.alert("No records found");
                      }});
		}
		else if(strResponse.equals("0"))
		{
			UiApplication.getUiApplication().invokeAndWait(new Runnable() {
				public void run() {
					ScreenInvoker.removeRemovableScreen();
				}});
			
			Vector v = new Vector();
			Utils.setWatchListedCompanyRecords(v);
			LOG.print("Current Watchlist records"+v.size());
			Action action;
			if(flag)
				action = new Action(ActionCommand.CMD_WL_SCREEN, null);
			else
				action = new Action(ActionCommand.CMD_WL_EDIT_SCREEN, null);
				ActionInvoker.processCommand(action);
		}
		else
		{
			Vector v = new Vector();
			if(Utils.WATCHLIST_NAME.substring(Utils.WATCHLIST_NAME.length()-3, Utils.WATCHLIST_NAME.length()).equalsIgnoreCase("NSE")  || Utils.WATCHLIST_NAME.equalsIgnoreCase("NIFTY"))
			{
				WatchListJsonParser wth = new WatchListJsonParser(true);
				v = wth.getResponse(strResponse);
			}
			else{
				String text = "";
				for(int x=1;x<strResponse.length();x++)
				{
					if(strResponse.charAt(x)==']')
					{
						if(text.length() != 0)
							v.addElement(text);
						text = "";
					}
					else if(strResponse.charAt(x)==',')
					{
						if(text.length() != 0)
							v.addElement(text);
						text = "";
					}
					else
					{
						text = text + strResponse.charAt(x);
					}
				}
				
		}
				Utils.setWatchListedCompanyRecords(v);
				MyWatchList.isFINISHED = false;
				MyWatchList.REFRESH = false;
				LOG.print("Else Current Watchlist records"+v.size());
				Action action;
				if(flag)
					action = new Action(ActionCommand.CMD_WL_SCREEN, null);
				else
					action = new Action(ActionCommand.CMD_WL_EDIT_SCREEN, null);
					
				//Action action = new Action(ActionCommand.CMD_WL_SCREEN, null);
				ActionInvoker.processCommand(action);
			}
	}
	}
}
