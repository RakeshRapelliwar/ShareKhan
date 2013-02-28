package com.snapwork.view;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.util.Arrays;
import net.rim.device.api.util.Comparator;
import net.rim.device.api.util.StringComparator;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.WatchListJson;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomLinkButton;
import com.snapwork.components.CustomLinkButtonUnderLine;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.components.WatchListField;
import com.snapwork.components.WatchListFieldRemove;
import com.snapwork.parsers.Json;
import com.snapwork.util.AppConstants;
import com.snapwork.util.DBPackager;
import com.snapwork.util.DBmanager;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.URLEncode;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class MyWatchListEdit extends MainScreen implements ActionListener {

    VerticalFieldManager verticalWatchListFieldManager = null;
    private BottomMenu bottomMenu = null;
    //private CustomLinkButtonUnderLine refreshButton = null; 
    private PopUpView customPopUpScreen;
    public static Vector storeEditData;
    public Vector watchListCompanies;
    private Thread thread;
    private long timer;
        public MyWatchListEdit(Vector watchListCompanies) {
        	getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        	
        	    createUI(watchListCompanies);
              //  createUI(storeEditData);
        }

        public void createUI(Vector watchListCompanies) {
        	timer = System.currentTimeMillis();
        	this.watchListCompanies = watchListCompanies;
	        //Sets the title
	        setTitle(new TitleBar("Edit Watchlist"));
	        
	        VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
	            public void paintBackground(Graphics graphics)
	            {
	               graphics.setColor(Color.BLACK);
	               graphics.fillRect(0, 0, getWidth(), getHeight());
	            }
	            protected void sublayout( int maxWidth, int maxHeight )
	            {
	            	super.sublayout(maxWidth,maxHeight);
	            	if(bottomMenu.isAttached) {
		            	if(getField(0).getPreferredHeight()<AppConstants.screenHeight-BottomMenu.BottomMenuImageHeight-TitleBar.getItemHeight()) {
		            		setExtent(maxWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
		            	}
	            	} else {
		            	if(getField(0).getPreferredHeight()<AppConstants.screenHeight-TitleBar.getItemHeight()) {
		            		setExtent(maxWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
		            	}            		
	            	}
	            }
	        };
	        //LOG.print("Utils.getWatchListedCompanyRecordsData().size() "+Utils.getWatchListedCompanyRecordsData()/*Data()*/);//getWatchListedCompanyRecords().size());
	        verticalWatchListFieldManager = new VerticalFieldManager(FOCUSABLE);
	                       // Vector inbuildWatchList = Utils.getWatchListedCompanyRecordsData()/*Data()*/;//getWatchListedCompanyRecords();
	                      //  final String exch = Utils.WATCHLIST_NAME.indexOf('.')<0?(Utils.WATCHLIST_NAME.equalsIgnoreCase("NIFTY")?"NSE":"BSE"):Utils.WATCHLIST_NAME.substring(Utils.WATCHLIST_NAME.length()-3, Utils.WATCHLIST_NAME.length());
	                        //watchListCompanies = Utils.getWatchListedCompanyRecordsData()/*Data()*/;
	                        for(int i=0;i<watchListCompanies.size();i++) {
	                                HomeJson homeJSon = (HomeJson)watchListCompanies.elementAt(i);
	                                String code = homeJSon.getCompanyCode();
	                               // String symbol = homeJSon.getSymbol();
	                                LOG.print(homeJSon.getCompanyCode()+" : WatchList Name Delete : "+homeJSon.getDisplayName1());
	                                String exch = "NSEFO";
	                                if(homeJSon.getDisplayName2().equalsIgnoreCase("BSE"))
	                                	exch = "BSE";
	                                else if(homeJSon.getDisplayName2().equalsIgnoreCase("NSE"))
	                                	exch = "NSE";
	                                        		verticalWatchListFieldManager.add(new WatchListFieldRemove(FOCUSABLE, homeJSon.getDisplayName1()+" "+homeJSon.getDisplayName2(), code, Utils.WATCHLIST_NAME, exch, this){
	                                        			WatchListFieldRemove wl = this;
	                                        			protected boolean navigationClick(
	                                        				int status,
	                                        				int time) {
	                                        				if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
	                                        			UiApplication.getUiApplication().invokeLater(new Runnable() {
	                                    	                public void run() {
	                                    	                 customPopUpScreen = new PopUpView(wl);
	                                    	                 UiApplication.getUiApplication().pushScreen(customPopUpScreen);
	                                    	                }
	                                    	            });
	                                        				}
	                                        			return super.navigationClick(status, time);
	                                        		}
	                                        			protected boolean touchEvent(TouchEvent message)
	                                        			{setFocus();
	                                        			
	                                        				if(message.getEvent() == TouchEvent.CLICK) {
	                                        					if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
		                                        			UiApplication.getUiApplication().invokeLater(new Runnable() {
		                                    	                public void run() {
		                                    	                 customPopUpScreen = new PopUpView(wl);
		                                    	                 UiApplication.getUiApplication().pushScreen(customPopUpScreen);
		                                    	                }
		                                    	            });
	                                        				}}return super.touchEvent(message);
		                                        		}});
	                        }
	                        mainManager.add(verticalWatchListFieldManager);
	                        
	        
	        add(mainManager);
	        // Configure BottomMenu and set commands
	        bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_WL_SCREEN, AppConstants.bottomMenuCommands);
        }
        
        public void actionPerfomed(byte Command, Object sender) {
	        switch(Command) {
		      
	        }
        }
        public boolean onMenu(int instance) 
        {
                return false;
        }
        
        public boolean keyChar( char key, int status, int time )
        {
            return false;
        }
        
        public boolean keyDown( int keyCode, int time )
        {
            int key = Keypad.key( keyCode );
            if(key== Keypad.KEY_ENTER)
            {
            	UiApplication.getUiApplication().invokeLater(new Runnable() {
	                public void run() {
	                	try {
							if(bottomMenu != null)
								bottomMenu.autoAttachDetachFromScreen();
						} catch (Exception e) {
						}
	                }
	            });
            }
            else if(key== Keypad.KEY_ESCAPE)
            {
            MyWatchList mw = new MyWatchList(watchListCompanies);
	         Utils.setWatchListedCompanyRecordsData(watchListCompanies);
            }
            else if(key == Keypad.KEY_END)
    		{
    			LOG.print("KEY_END EXIT from app");
    			System.exit(0);
    		}	
            return true;
        }
        
        public boolean keyUp( int keyCode, int time )
        {
            return super.keyUp(keyCode, time);
        }

/*public boolean onClose() {
	Action action = new Action(ActionCommand.CMD_WL_SCREEN, null);
	ActionInvoker.processCommand(action);
	return super.onClose();
}*/
public class PopUpView extends PopupScreen {

    private String strTitle = "";
    private HorizontalFieldManager horizontalFieldManager;
     public PopUpView(WatchListFieldRemove field) {
            super(new VerticalFieldManager(),Field.FOCUSABLE);
            
           strTitle = "Do you want to delete from Market Watch?";
            
            createUI(strTitle, field);
    }
  
    public void createUI(final String notificationType, final WatchListFieldRemove field) {
                    //set the title
                   LabelField lblTitle = new LabelField(strTitle);
                   
                   horizontalFieldManager = new HorizontalFieldManager(FIELD_HCENTER);

                   
                   ButtonField btnYes = new ButtonField("Yes",FOCUSABLE | FIELD_HCENTER) {
                protected boolean navigationClick(int status, int time) {                                                   
                                                   synchronized( UiApplication.getEventLock() ){
                                                        
                                                        if(isDisplayed()) 
                                                     	   {
                                                        	startRegistrationProcess(field, field.getWl_name(), field.getExch(), field.getCcode());
                                                        	close();

                                                     	   }
                                  	   }
                    return true;
                }

				
                   };
                   
                   ButtonField btnNo = new ButtonField("No",FOCUSABLE | FIELD_HCENTER) {
                       protected boolean navigationClick(int status, int time) {
                    	   synchronized( UiApplication.getEventLock() ){
                              
                                          if(isDisplayed()) 
                                       	   {
                                       	   	close();

                                       	   }
                    	   }
                               return true;
                       }
                   };
                   
                   add(lblTitle);
                   this.horizontalFieldManager.add(btnYes);
                   this.horizontalFieldManager.add(btnNo);
                   add(this.horizontalFieldManager);
    		}
    public void startRegistrationProcess(final WatchListFieldRemove wlField,final String wlname,final String exch,final String ccode) {
    	ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
		
		thread = new Thread(new Runnable() {
			public void run() {
				try {
					//doPaint();
//Show wait registering moments
					String strAuthURL = Utils.getWatchListURLRemoveScrips(UserInfo.getUserID(),wlname, exch, ccode);
					LOG.print("HTTP call started "+strAuthURL);
					String strResponse = HttpProcess.getHttpsMD5Connection(strAuthURL);
					
					final boolean result = parseAndSaveRegistrationInfo(strResponse,wlField);
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							//if (result)
							LOG.print("HTTP call finished");
							if(!Utils.sessionAlive) return;
							if (result)
							{
								
									ScreenInvoker.removeRemovableScreen();
									verticalWatchListFieldManager.delete(wlField);
									for(int i=0; i<watchListCompanies.size();i++)
									{
										HomeJson homeJSon = (HomeJson)watchListCompanies.elementAt(i);
		                                if(homeJSon.getCompanyCode().equalsIgnoreCase(wlField.getCcode()))
		                                {
		                                	watchListCompanies.removeElementAt(i);
		                                	i--;
		                                }
										
									}
									//ActionInvoker.processCommand(new Action(ActionCommand.CMD_GRID_SCREEN,null));
									Dialog.alert("Successfully deleted!");
								
							} else {
								ScreenInvoker.removeRemovableScreen();
								//ActionInvoker.processCommand(new Action(ActionCommand.CMD_USER_REGISTRATION,null));
								Dialog.alert("Unable to delete!");
							}                                                       
						}
					});
				} catch (Exception ex) {
					}
			}
		});
		thread.start();
	}


	public boolean parseAndSaveRegistrationInfo(String strResponse,final WatchListFieldRemove wlField)
	{
		if(!Utils.sessionAlive)
			return false;
		LOG.print("HTTP call inProcess");
		LOG.print(strResponse);
		Json js = new Json(strResponse);
		Hashtable ht = (Hashtable) js.getdata.elementAt(0);
			final String result =  (String)ht.get("MSG");
			LOG.print(result);
					LOG.print("HTTP call finished");
					if (result.equalsIgnoreCase("SUCCESS"))
					{
						return true;
					}
					return false;
	}
}
public void delete(int id)
{
	verticalWatchListFieldManager.deleteRange(id,id);
	}
}
