package com.snapwork.view;

import java.util.Hashtable;
import java.util.Vector;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.TitleBar;
import com.snapwork.components.WatchListSecondField;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.Json;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;
import com.snapwork.view.WatchListScreen.CustomPopUpScreen;

import net.rim.device.api.crypto.VerificationException;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.util.CharacterUtilities;

public class WatchListPopUp extends PopupScreen implements ReturnString{

	private ReturnStringParser returnStringParser = null;
	//private LabelField loading;
	private String ccode;
	private String wlName;
	private WatchListPopUp watchListPopUp;
	private long timer;
	private int WATCHLISTSCRIPDATA = 898;
	private String exchangeFO; 
	private ReturnString watchListScreen = this;
	private boolean addDir;
     public WatchListPopUp(String strccode, boolean addDir) {
            super(new VerticalFieldManager(WatchListPopUp.VERTICAL_SCROLL | WatchListPopUp.VERTICAL_SCROLLBAR ),Field.FOCUSABLE);
            ccode = strccode;
            this.addDir = addDir;
            if(UiApplication.getUiApplication().getActiveScreen() instanceof CompanyFNODetailsSnippetsScreen)
            	exchangeFO = "NSEFO";
            else
            {
            	if(!AppConstants.NSE)
            		exchangeFO = "BSE";
            	else
            		exchangeFO = "NSE";
            }
            watchListPopUp = this;
            timer = System.currentTimeMillis();
            createUI(strccode);
    }
     
  
    public void createUI(final String strccode) {
                    //set the title
                  
                   //loading = new LabelField("Loading...");
                  // add(loading);
                   if(!addDir){
                	   LOG.print(Utils.getWatchListURL(1,UserInfo.getUserID()));
                   returnStringParser = new ReturnStringParser(Utils.getWatchListURL(1,UserInfo.getUserID()),1,this,true);
                   }
                   else
                   {
                	   ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
						String addWatchListScripURL = Utils.getWatchListURLAddScrips(UserInfo.getUserID(),Utils.WATCHLIST_NAME,exchangeFO,ccode);
						LOG.print("addWatchListScripURL : "+addWatchListScripURL);
						returnStringParser = new ReturnStringParser(addWatchListScripURL,2,watchListPopUp,true);
                   }
            }
    
    protected boolean keyDown( int keyCode, int time ) {
		int key = Keypad.key( keyCode );
		if(key == Keypad.KEY_END)
		{
			LOG.print("KEY_END EXIT from app");
			//if(returnStringParser != null)
			//	returnStringParser.httpKill();
			System.exit(0);
		}
		else if(key== Keypad.KEY_ESCAPE)
		{
			synchronized( UiApplication.getEventLock() ){
                
                if(isDisplayed()) 
             	   {
             	   	close();

             	   }
 }
			/*Action action = new Action(ActionCommand.CMD_WATCHLIST_SCREEN, null);
			ActionInvoker.processCommand(action);*/
		}
		return super.keyDown(keyCode, time);
	}
    public boolean onClose() {
    	synchronized( UiApplication.getEventLock() ){
            
            if(isDisplayed()) 
         	   {
         	   	close();

         	   }
}
    	if(returnStringParser != null)
			returnStringParser.httpKill();
    	/*Action action = new Action(ActionCommand.CMD_WATCHLIST_SCREEN, null);
		ActionInvoker.processCommand(action);*/
    	return true;
    }

	public void setReturnString(String string, int id) {
		// add watchlist BSE or NSE list
		LOG.print("Response Id: "+id);
		LOG.print("Response : "+string);
		if(id == 1)
		{
			Json js = new Json(string);
			String user="";
			String template="";


			
			int counter = 0;
			for(int x=0;x<js.getdata.size();x++)
			{
				Hashtable ht = (Hashtable) js.getdata.elementAt(x);
				user = (String)ht.get("Type");
			//	if(!exchangeFO.equals("NSEFO"))
			//		exchangeFO = (String)ht.get("Exchange");
				template = (String)ht.get("Template");
				if(user.equalsIgnoreCase("USER") )
				{
					if(counter==0)
					{
						 LabelField lblTitle = new LabelField("Add to:");
		                 lblTitle.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		                 add(lblTitle);
					}
					counter++;
					if(!template.equalsIgnoreCase("NIFTY") && !template.equalsIgnoreCase("SENSEX"))
						//v.addElement(template);
					{
						final String text = template; 
						if(template.length()>11)
						{
							template = template.substring(0, 11)+"...";
						}
						
						add(new WatchListSecondField(FOCUSABLE, template, (AppConstants.screenWidth/3)*2)
						{
							protected boolean navigationClick(int status, int time)
							{
								if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
							//need to implement	sendHTTPRequest(text,ccode);
								synchronized( UiApplication.getEventLock() ){
					                
					                if(isDisplayed()) 
					             	   {
					             	   	close();

					             	   }
								}
								ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
								wlName = text;
								Utils.WATCHLIST_NAME = wlName;
								String addWatchListScripURL = Utils.getWatchListURLAddScrips(UserInfo.getUserID(),text,exchangeFO,ccode);
								LOG.print("addWatchListScripURL : "+addWatchListScripURL);
								returnStringParser = new ReturnStringParser(addWatchListScripURL,2,watchListPopUp,true);
							}return super.navigationClick(status, time);
							}

							protected boolean touchEvent(TouchEvent message)
							{
								//need to implement	sendHTTPRequest(text,ccode);
								if(message.getEvent() == TouchEvent.CLICK) {
									if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
									synchronized( UiApplication.getEventLock() ){
					                
					                if(isDisplayed()) 
					             	   {
					             	   	close();

					             	   }
								}
								ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
								wlName = text;
								Utils.WATCHLIST_NAME = wlName;
								//final String exch = text.indexOf('.')<0?(text.equalsIgnoreCase("NIFTY")?"NSE":"BSE"):text.substring(text.length()-3, text.length());
								returnStringParser = new ReturnStringParser(Utils.getWatchListURLAddScrips(UserInfo.getUserID(),text,exchangeFO,ccode),2,watchListPopUp,true);
							}}return super.touchEvent(message);
							}
						});
					}
				}
			}	
			// ScreenInvoker.removeRemovableScreen();
			/*if(counter == 0)
			{
				final String e = exchange;
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						MyWatchList.isFINISHED = false;
						MyWatchList.REFRESH = false;
						WatchListScreen.SHOW_DIALOG = true;
						WatchListScreen.SHOW_DIALOG_MESSAGE = "No Watchlist found for "+e+" exchange,"+
						"\nPlease create watchlist first!";
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_WATCHLIST_SCREEN,null));
				
					}});
			}
			else
			{*/
			if(counter==0)
			{
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						CustomPopUpScreen customPopUpScreen = new CustomPopUpScreen();
						UiApplication.getUiApplication().pushScreen(customPopUpScreen);
					}
				});
			}
			else
			{
				
           
			 ScreenInvoker.pushScreen(this, true, true);
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					ScreenInvoker.removeRemovableScreen();
				}
			});
			}
		}
		
		else if(id == 2)
		{
			LOG.print(string);
			Json js = new Json(string);
			Hashtable ht = (Hashtable) js.getdata.elementAt(0);
			final String dispmsg = (String)ht.get("MSG");

			if(dispmsg.equalsIgnoreCase("SUCCESS"))
			{
				/*Vector v = new Vector();
				v.addElement(Utils.WATCHLIST_NAME);//ccode
				Utils.WATCHLIST = true;
				MyWatchList.isFINISHED = false;
				MyWatchList.REFRESH = false;
				Vector wlScreen = new Vector();
				wlScreen.addElement(wlName);
				Action action = new Action(ActionCommand.CMD_WL_FILL_SCREEN, wlScreen);
				ActionInvoker.processCommand(action);*/
				Utils.WATCHLIST_CLOSE = false;
				Utils.WATCHLIST_INDEX = 0;
				MyWatchList.isFINISHED  = false;
				MyWatchList.REFRESH = false;
				/*Vector vectorCommandData = new Vector();
			vectorCommandData.addElement((String)watchListCompanies.elementAt(y));*/
			//ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
			//Utils.WATCHLIST_NAME = (String)watchListCompanies.elementAt(y);
			Utils.WATCHLIST = true;
			Utils.WATCHLIST_MODE = false;
			Utils.setWatchListedCompanyRecordsDataMain(null);
			ReturnStringParser rts = new ReturnStringParser(Utils.getWatchListURL(UserInfo.getUserID(),Utils.WATCHLIST_NAME), WATCHLISTSCRIPDATA, this, true);
			}
			else
			{
				try
				{
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							ScreenInvoker.removeRemovableScreen();
						}
					});
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							if(dispmsg.equalsIgnoreCase("Duplicate"))
								Dialog.alert("Scrip already present");
							else
								{
									Dialog.alert(dispmsg);
								}
							LOG.print("-0-0-0-0-00-0-00-0-0-0-0-00-0-00-0");
							LOG.print("You are here");
						}
					});
					
				}
				catch(Exception e)
				{
					
				}
			}
		}
		else if(id == WATCHLISTSCRIPDATA)
		{
			LOG.print(string);
			Vector v = HomeJsonParser.getVector(string);
			if(v.size()==0)
			{
				Utils.setWatchListedCompanyRecords(new Vector());
			}else{
				Vector vx = new Vector();
			for(int i=0; i<v.size(); i++)
			{
				vx.addElement(((HomeJson) v.elementAt(i)).getDisplayName1());
			}
			Utils.setWatchListedCompanyRecords(vx);
			}
			MyWatchList myWatchList = new MyWatchList(v);
		}	
		
		else if(id == 12)
		{
			Json js = new Json(string);
			String user="";
			Hashtable ht = (Hashtable) js.getdata.elementAt(0);
			user = (String)ht.get("MSG");
			String dispmsg = (String)ht.get("DISPMSG");
			LOG.print("-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0");
			LOG.print("MSG "+user );
			LOG.print("DISPMSG"+dispmsg );
			if(user.equalsIgnoreCase("SUCCESS"))
			{
			synchronized( UiApplication.getEventLock() ){
                
                if(isDisplayed()) 
             	   {
             	   	close();

             	   }
			}
			ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
			
			//final String exch = text.indexOf('.')<0?(text.equalsIgnoreCase("NIFTY")?"NSE":"BSE"):text.substring(text.length()-3, text.length());
			returnStringParser = new ReturnStringParser(Utils.getWatchListURLAddScrips(UserInfo.getUserID(),Utils.WATCHLIST_NAME,exchangeFO,ccode),2,watchListPopUp,true);
			}
		}
		
	}

	
	
	public class CustomPopUpScreen extends PopupScreen {
		CustomBasicEditField editfield;
		WatchListSecondField nse;
		WatchListSecondField bse;
		//CustomObjectChoiceFieldReg dropdown;
		int popupWidth;
		String[] choice = {"NSE","BSE"};

		public int getPreferredWidth() {
			return AppConstants.screenWidth-90;
			//return AppConstants.screenWidth-20;
		}
		
		public CustomPopUpScreen() {
			super(new VerticalFieldManager(CustomPopUpScreen.VERTICAL_SCROLL | CustomPopUpScreen.VERTICAL_SCROLLBAR ));
			
			popupWidth = getPreferredWidth();
			LOG.print("1");
			add(new TitleBar("Create New Watchlist"));//1
			LOG.print("2");
			add(new LabelField());//2
			LOG.print("3");
			//LabelField lbl = new LabelField("Enter WatchList/Type Name:",NON_FOCUSABLE);
			LOG.print("4");
			//lbl.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			LOG.print("5");
			add(new LabelField("Enter WatchList/Type Name:",NON_FOCUSABLE));
			LOG.print("6");
			add(new LabelField("",NON_FOCUSABLE){protected void layout(int arg0, int arg1) {
				if(AppConstants.screenHeight>360)
					super.layout(arg0, arg1);
				else
					setExtent(arg0, 2);
			}});//3
			LOG.print("7");
			final int x = (getPreferredWidth()>getWidth()?getPreferredWidth():getWidth())+10;
			//final int x = AppConstants.screenWidth/3;
			editfield = new CustomBasicEditField(Color.BLACK  | BasicEditField.NO_COMPLEX_INPUT)
			{
			protected void paint(Graphics graphics)
			{
				setText(getText().toUpperCase());
				super.paint(graphics);
			}
				public int getPreferredHeight()
				{
					return getFont().getHeight()*2;
				};
				public int getPreferredWidth() {
					return x;
				}
				protected void layout(int arg0, int arg1)
				{
					if(AppConstants.screenHeight>360)
						super.layout(arg0, arg1);
					else
						super.layout(getPreferredWidth(), arg1+(arg1/3));
				};
				protected boolean keyChar(char ch, int status, int time) {
			        if (CharacterUtilities.isLetter(ch) || CharacterUtilities.isDigit(ch) || (ch == Characters.BACKSPACE) || (ch == Characters.DELETE) || (ch == Characters.ESCAPE)) {
			        	return super.keyChar(ch, status, time);
			        }
			        else
			        {
			        	UiApplication.getUiApplication().invokeLater(new Runnable() {
			    			public void run() {
			        	Dialog.alert("Only alphanumeric characters allowed.");
			    			}});
			        	return true;
			        }
			        //return true;
			    }
			}; 
			//add(editfield);
			//add(new LabelField("",NON_FOCUSABLE));
			LOG.print("8");
			/*dropdown = new CustomObjectChoiceFieldReg("", choice,0,ObjectChoiceField.FORCE_SINGLE_LINE |  ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)/*Field.USE_ALL_WIDTH | FIELD_LEFT )
			{
				public int getPreferredWidth() {
					return x;
				}
				
				protected void layout(int width, int height) {
					if(AppConstants.screenHeight>360)
					{	if(AppConstants.OS == 5)
							setExtent(popupWidth, (getFont().getHeight()*2)+(getFont().getHeight()/3));
						else
							{
							LOG.print("popupWidth : "+popupWidth);
							LOG.print("(getFont().getHeight()*2)+(getFont().getHeight()/3) : "+((getFont().getHeight()*2)+(getFont().getHeight()/3)));
							LOG.print("width : "+width);
							LOG.print("height : "+height);
							setExtent(popupWidth, (getFont().getHeight()*2)+(getFont().getHeight()/3));
							super.layout(popupWidth, height);
								}
					}
					else
						setExtent(x, getFont().getHeight()*2);  // force the field to use all available space
				}
			};
			dropdown.setMinimalWidth(x);
			add(dropdown);//4 OK*/
			LOG.print("9");
			add(new LabelField("",NON_FOCUSABLE){protected void layout(int arg0, int arg1) {
				if(AppConstants.screenHeight>360)
					super.layout(arg0, arg1);
				else
					setExtent(arg0, 10);
			}});//5
			LOG.print("10");
			add(editfield);//6 OK
			LOG.print("11");
			add(new LabelField("",NON_FOCUSABLE){protected void layout(int arg0, int arg1) {
				if(AppConstants.screenHeight>360)
					super.layout(arg0, arg1);
				else
					setExtent(arg0, 10);
			}});//7
			LOG.print("12");
			HorizontalFieldManager h = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER)
			{
				
				};
			h.add(new ButtonField("Go",  FOCUSABLE | DrawStyle.HCENTER)  {
				
				
				protected boolean navigationClick(int status,int time) {
					//ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
						synchronized( UiApplication.getEventLock() ){
                        
                        if(isDisplayed()) 
                     	   {
                        	sendHTTPRequest(editfield.getText().toUpperCase());
                        	close();
                     	   }
  	   }}
					return super.navigationClick(status, time);
				}
				
				protected boolean touchEvent(TouchEvent message) 
				{
					if(message.getEvent() == TouchEvent.CLICK) {
						if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
					//ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
						
					synchronized( UiApplication.getEventLock() ){
                        
                        if(isDisplayed()) 
                     	   {
                        	sendHTTPRequest(editfield.getText().toUpperCase());
                        	close();
                     	   }
  	   }
					}}
					return super.touchEvent(message);
				}
			});
			add(h);//8 OK
			LOG.print("13");
			
		}
		/*protected void sublayout( int maxWidth, int maxHeight )
        {
			if(AppConstants.OS > 5)
			{
				int hh = FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight();
			 //layoutChild(getField(4), popupWidth-50, (hh*2)+(hh/3));
				layoutChild(getField(4), popupWidth-50, (hh*2)+(hh/3));
				layoutChild(getField(6), popupWidth-50, (hh*2)+(hh/3));
				layoutChild(getField(8), popupWidth-50, (hh*2)+(hh/3));
				setPositionChild(getField(4),10,20);
				setPositionChild(getField(6),10,40);
				setPositionChild(getField(8),10,60);
			// layoutChild(getField(4), getField(4).getPreferredWidth(), getField(4).getHeight());
			 //layoutChild(getField(4), getField(4).getPreferredWidth(), getField(4).getHeight());
			// setPositionChild(getField(0), (AppConstants.screenWidth/2)-(getField(0).getPreferredWidth()/2), 0);
			 setExtent(maxWidth,hh*4);
			}
        }*/
		
		public void sendHTTPRequest(final String code) {
			if(code.length()==0) return;
			if(code.indexOf(' ')>-1) return;
			ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
			//doPaint();
			Utils.WATCHLIST_NAME = code;
			String straddURL = Utils.getWatchListURLADD(UserInfo.getUserID(),code,"");
			new ReturnStringParser(straddURL, 12, watchListScreen, true);
		}

		
		protected boolean keyDown( int keyCode, int time ) {
			int key = Keypad.key( keyCode );
			if(key== Keypad.KEY_ENTER)
			{

			}
			else if(key==Keypad.KEY_ESCAPE)
			{
				synchronized( UiApplication.getEventLock() ){

					if(isDisplayed()) 
					{
						close();

					}
				}
			}

			else if(key == Keypad.KEY_END)
			{
				LOG.print("KEY_END EXIT from app");
				System.exit(0);
			}
			return super.keyDown(keyCode, time);
		}
	}
}
