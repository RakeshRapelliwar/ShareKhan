package com.snapwork.view;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.util.CharacterUtilities;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.WatchListMainBean;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.CustomObjectChoiceFieldReg;
import com.snapwork.components.Snippets;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TitleBar3;
import com.snapwork.components.WatchListHomeField;
import com.snapwork.components.WatchListSecondField;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.Json;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class WatchListScreen extends MainScreen implements ThreadedComponents,ActionListener, ReturnString{

	VerticalFieldManager verticalWatchListFieldManager = null;
	private BottomMenu bottomMenu = null;
	private CustomPopUpScreen customPopUpScreen;
	//VerticalFieldManager mainManager ;
	WatchListScreen watchListScreen;
	public static boolean SHOW_DIALOG;
	public static String SHOW_DIALOG_MESSAGE = "";
	Vector watchListCompanies;
	private Thread thread;
	public static boolean watchFlag;
	public String exchangesnp;
	public String codesnp;
	private long timer;
	private int WATCHLISTSCRIPDATA = 888;
	private ReturnString returnString = this;
	
	
	
	
	public WatchListScreen(Vector watchListCompanies) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		watchListScreen = this;
		MyWatchList.isFINISHED = true;
		timer = System.currentTimeMillis();
		
		
		createUI(watchListCompanies,false);
		
	}

	public void createUI(Vector watchListCompanies_,boolean flag) {
		timer = System.currentTimeMillis();
		if(flag)
		{ 
			int httl = 0;
			watchListCompanies = watchListCompanies_;
			ButtonField add = new ButtonField("Add", FOCUSABLE | FIELD_RIGHT | USE_ALL_HEIGHT)
			{
				public net.rim.device.api.ui.Font getFont() {
					if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
						return FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT);
					
					return FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT);
				}
				public int getPreferredWidth() {
					// TODO Auto-generated method stub
					return 60;
				}
				protected boolean navigationClick(int status,int time) {
					if((timer+100)<System.currentTimeMillis())
					{
						timer = System.currentTimeMillis();
						/*if(watchListCompanies.size() >5)
						{
							UiApplication.getUiApplication().invokeLater(new Runnable() {
								public void run() {
									Dialog.alert("Maximum 6 Template allowed.");
								}
							});
							return super.navigationClick(status, time);
						}
						else*/{
							if(watchListCompanies.size()>0){
				/*	verticalWatchListFieldManager.setFieldWithFocus(verticalWatchListFieldManager.getField(0));
					((WatchListHomeField) verticalWatchListFieldManager.getField(0)).invalidate();*/
					}
					LOG.print("********************************************************************** Add watchlist");
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							customPopUpScreen = new CustomPopUpScreen();
							UiApplication.getUiApplication().pushScreen(customPopUpScreen);
						}
					});
					}
				}
					return super.navigationClick(status, time);
				}
				protected boolean touchEvent(TouchEvent message)
				{
					if(message.getEvent() == TouchEvent.CLICK) {
						if((timer+100)<System.currentTimeMillis()){
							timer = System.currentTimeMillis();
							/*if(watchListCompanies.size() == 6)
							{
								UiApplication.getUiApplication().invokeLater(new Runnable() {
									public void run() {
										Dialog.alert("Maximum 6 Template allowed.");
									}
								});
								return super.touchEvent(message);
							}
							else*/{
					if(watchListCompanies.size()>0){
						/*verticalWatchListFieldManager.setFieldWithFocus(verticalWatchListFieldManager.getField(0));
						((WatchListHomeField) verticalWatchListFieldManager.getField(0)).invalidate();*/
						}
					LOG.print("********************************************************************** Add watchlist Touch");
					UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								customPopUpScreen = new CustomPopUpScreen();
								UiApplication.getUiApplication().pushScreen(customPopUpScreen);
							}
						}); }}
				}return super.touchEvent(message);}
			};
			//if(watchListCompanies_.size()>2){
				ButtonField edit = new ButtonField("Edit", FOCUSABLE | FIELD_RIGHT | USE_ALL_HEIGHT)
			{
				public net.rim.device.api.ui.Font getFont() {
					if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
						return FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT);
					
					return FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT);
				}
				public int getPreferredWidth() {
					// TODO Auto-generated method stub
					return 60;
				}
				protected boolean navigationClick(int arg0, int arg1) {
					if(watchListCompanies.size()>0){
						LOG.print("EDIT screen Start");
						Vector vectorCommandData = new Vector();
						vectorCommandData.addElement(Utils.WATCHLIST_LABEL);
						vectorCommandData.addElement(watchListCompanies);
						Action action = new Action(ActionCommand.CMD_WL_SCREEN_EDIT_SCREEN, vectorCommandData);
						ActionInvoker.processCommand(action);
						}
				return super.navigationClick(arg0, arg1);
				}
			};
			//Sets the title
			setTitle(new TitleBar3("Market Watch",add,edit));
			httl = TitleBar3.getItemHeight();
			/*}
			else
			{  	setTitle(new TitleBar("Market Watch"));
			httl = TitleBar.getItemHeight();*/
			//}
			final int titleHt = httl;
			VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR ) {
				public void paintBackground(Graphics graphics)
				{
					graphics.setColor(Color.BLACK);
					graphics.fillRect(0, 0, getWidth(), getHeight()<AppConstants.screenHeight?AppConstants.screenHeight:getHeight());
				}
				protected void sublayout( int maxWidth, int maxHeight )
				{
					super.sublayout(maxWidth,maxHeight);
					setExtent(maxWidth, AppConstants.screenHeight-titleHt);
				}
			};


			verticalWatchListFieldManager = new VerticalFieldManager(FOCUSABLE)
			{
				public void paintBackground(Graphics graphics)
				{
					graphics.setColor(Color.BLACK);
					graphics.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			LOG.print("watchListCompanies Template Size : "+watchListCompanies.size());
			if(watchListCompanies.size()!=0)
			for(int x=0;x<watchListCompanies.size();x++)
			{
				if(watchListCompanies.elementAt(x) instanceof WatchListMainBean)
				{
					final WatchListMainBean bean = (WatchListMainBean)watchListCompanies.elementAt(x);
				//String strn = (String)watchListCompanies.elementAt(x);
				final int y = x;
				verticalWatchListFieldManager.add(new WatchListHomeField(FOCUSABLE, bean.getTemplateName(), null)
				{
					protected boolean navigationClick(int status, int time)
					{
						handler();
						return super.navigationClick(status, time);
					}

					protected boolean touchEvent(TouchEvent message)
					{
						setFocus();
						
						if(message.getEvent() == TouchEvent.CLICK) {
							handler(); 
					}return super.touchEvent(message);
					}
					private void handler()
					{
						
						
						
						if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
						Utils.WATCHLIST_CLOSE = false;
						Utils.WATCHLIST_INDEX = 0;
						MyWatchList.isFINISHED  = false;
						MyWatchList.REFRESH = false;
						/*Vector vectorCommandData = new Vector();
					vectorCommandData.addElement((String)watchListCompanies.elementAt(y));*/
					ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					Utils.WATCHLIST_NAME = bean.getTemplateName();
					Utils.WATCHLIST = true;
					ReturnStringParser rts = new ReturnStringParser(Utils.getWatchListURL(UserInfo.getUserID(),Utils.WATCHLIST_NAME), WATCHLISTSCRIPDATA, returnString, true);
					/*Action action = new Action(ActionCommand.CMD_WL_FILL_SCREEN, vectorCommandData);
					ActionInvoker.processCommand(action);*/
				}
						
						
					}
				});
				/*if(x==(result.size()-1))
                                {
                                        mainManager.add(new WatchListHomeField(FOCUSABLE, (String)result.elementAt(0), null));
                                        mainManager.add(new WatchListHomeField(FOCUSABLE, (String)result.elementAt(1), null));
                                }*/

                                //      add( new WatchListSecondField(FOCUSABLE, (String)result.elementAt(x),null, width_,Utils.getWatchListURL(1,UserInfo.getUserID())));
			}
			}
			HorizontalFieldManager h = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER )
			{
				public void paintBackground(Graphics graphics)
				{
					graphics.setColor(Color.BLACK);
					graphics.fillRect(0, 0, getWidth(), getHeight());
				}
				/*protected void sublayout(int width, int height) {
                                        setPositionChild(getField(0), (AppConstants.screenWidth/2)-(getField(0).getPreferredWidth()/2), 0);
                                        super.sublayout(width, height);
                                }*/
			};
			/*h.add(new ButtonField("Create Market watch",  FOCUSABLE | DrawStyle.HCENTER)  {

				protected boolean navigationClick(int status,int time) {
					verticalWatchListFieldManager.setFieldWithFocus(verticalWatchListFieldManager.getField(1));
					((WatchListHomeField) verticalWatchListFieldManager.getField(1)).invalidate();
					invalidate();
					
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							customPopUpScreen = new CustomPopUpScreen();
							UiApplication.getUiApplication().pushScreen(customPopUpScreen);
						}
					}); return super.navigationClick(status, time);
				}

			});*/
			h.add(new NullField(NON_FOCUSABLE));
			verticalWatchListFieldManager.add(h);
			mainManager.add(verticalWatchListFieldManager);
			add(mainManager);
			// Configure BottomMenu and set commands
			bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_WATCHLIST_SCREEN, AppConstants.bottomMenuCommands);

			//
			ScreenInvoker.pushScreen(watchListScreen, true, true);
			if(SHOW_DIALOG)
				{
				SHOW_DIALOG = false;
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
				Dialog.alert(SHOW_DIALOG_MESSAGE);
					}});
				}


		}
		else
		{
			
//			Dialog.alert("here");
			
			if(Utils.getWatchListedCompanyRecordsDataMain() == null)
			{
				
//				Dialog.alert("here   1");
				
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				sendHTTPRequest(1);
				
				 
				
				
			}
			else
			{
//				Dialog.alert("here   2");
				
				
				UiApplication.getUiApplication().invokeLater(new Runnable() {
            		public void run() {
        				ScreenInvoker.removeRemovableScreen();
        				createUI(Utils.getWatchListedCompanyRecordsDataMain(),true);
            		}
            	});
			}
		}
		
		
//		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
//			
//			public void run() {
//				// TODO Auto-generated method stub
//			    Dialog.alert(""+testURL);	
//			}
//		});
		
		
	}

	public void componentsPrepared(byte componentID, Object component) {}

	public void componentsDataPrepared(byte componentID,final Object data) {
		try {
			switch(componentID) {
			}
		} catch(Exception ex) {
			Debug.debug("componentsDataPrepared,Error : "+ex.toString());
		}
	}

	public void actionPerfomed(byte Command, Object sender) {
		//switch(Command) {
		ActionInvoker.processCommand(new Action(Command,null));
		//}
	}

	public boolean onMenu(int instance) 
	{
		return false;
	}

	public boolean keyChar( char key, int status, int time )
	{
		return false;
	}
	protected boolean onSavePrompt() {
        return true;
    }

	public boolean keyDown( int keyCode, int time )
	{
		int key = Keypad.key( keyCode );
		if(key == Keypad.KEY_MENU) {
			if(watchListCompanies.size()>0){
			verticalWatchListFieldManager.focusChangeNotify(0);
			verticalWatchListFieldManager.setFieldWithFocus(verticalWatchListFieldManager.getField(0));
			((WatchListHomeField) verticalWatchListFieldManager.getField(0)).invalidate();
			}
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
		else if(key == Keypad.KEY_END)
		{
			LOG.print("KEY_END EXIT from app");
			System.exit(0);
		}
		else
            	return super.keyDown(keyCode, time);

            return true;
	}

	public boolean keyUp( int keyCode, int time )
	{
		return super.keyUp(keyCode, time);
	}

	public Vector getComponentData() {
		return null;
	}

	public boolean onClose() {
		Action action = new Action(ActionCommand.CMD_GRID_SCREEN, null);
		ActionInvoker.processCommand(action);
		return super.onClose();
	}
	
	private String testURL;
	
	public void sendHTTPRequest(final int i) {
		thread = new Thread(new Runnable() {
			public void run() {
				try {
					//doPaint();
					 final String strAuthURL = Utils.getWatchListURL(i,UserInfo.getUserID());
					 
					
					 
					 
					 String strResponse = HttpProcess.getHttpsMD5Connection(strAuthURL);
					 
					 
					 testURL=strResponse;
					 
					 
					 final Vector result = parseAndSaveRegistrationInfo(i, strResponse);
					 UiApplication.getUiApplication().invokeLater(new Runnable() {
						 public void run() {
							 if(!Utils.sessionAlive) return;
							 if(result.size()==0)
							 {
								 ScreenInvoker.removeRemovableScreen();
								 deleteAll();
								 watchListCompanies = new Vector();
								 createUI(watchListCompanies,true);
								 Dialog.alert("No records found");
							 }
							 else
							 {
								 if(i==1)
								 {
									 ScreenInvoker.removeRemovableScreen();
									 watchListCompanies = result;
									 createUI(watchListCompanies,true);
}
							 }

						 }
					 });
				} catch (Exception ex) {
					//ScreenInvoker.removeRemovableScreen();
					//Dialog.alert("Error occured during Registration Process");
				}
			}
		});
		thread.start();
	}

	public Vector parseAndSaveRegistrationInfo(int i,String strResponse)
	{
		Vector v = new Vector();
		if(!Utils.sessionAlive) return v;
		LOG.print(strResponse);
		if(i==1){
			Json js = new Json(strResponse);
			String user="";
			for(int x=0;x<js.getdata.size();x++)
			{
				Hashtable ht = (Hashtable) js.getdata.elementAt(x);
				user = (String)ht.get("Type");
				WatchListMainBean bean = new WatchListMainBean();
				bean.setDisplayName((String)ht.get("DisplayName"));
				bean.setTemplateName((String)ht.get("Template"));
				v.addElement(bean.copy());
			}
			Utils.setWatchListedCompanyRecordsDataMain(v);
		}               
		else
		{
			String text = "";
			for(int x=1;x<strResponse.length()-1;x++)
			{
				if(strResponse.charAt(x)==',')
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
		return v;

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
			exchangesnp = "";
			codesnp = code;
			ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
			//doPaint();
			String strAuthURL = Utils.getWatchListURLADD(UserInfo.getUserID(),code,"");
			new ReturnStringParser(strAuthURL, 12, watchListScreen, true);
		}

		public String parseAndSaveRegistrationInfo(String strResponse)
		{
			Json js = new Json(strResponse);
			String user="";
			thread = null;
			Hashtable ht = (Hashtable) js.getdata.elementAt(0);
			user = (String)ht.get("MSG");
			String dispmsg = (String)ht.get("DISPMSG");
			LOG.print("-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0");
			LOG.print("MSG "+user );
			LOG.print("DISPMSG"+dispmsg );
			if(user.equalsIgnoreCase("SUCCESS"))
				return user;
			return dispmsg;

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

	public void setReturnString(final String strResponse, int id) {
		
		
		
//UiApplication.getUiApplication().invokeAndWait(new Runnable() {
//			
//			public void run() {
//				// TODO Auto-generated method stub
//			   Dialog.alert(""+strResponse);	
//			}
//		});
		
		
		
		
	if(id == WATCHLISTSCRIPDATA)
	{
		LOG.print(strResponse);
		if(strResponse.equalsIgnoreCase("0"))
		{
			Utils.setWatchListedCompanyRecords(new Vector());
			MyWatchList myWatchList = new MyWatchList(new Vector());
		}
		else
		{
		Vector v = HomeJsonParser.getVector(strResponse);
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
	}	
	else
	{
		Json js = new Json(strResponse);
		String user="";
		thread = null;
		Hashtable ht = (Hashtable) js.getdata.elementAt(0);
		user = (String)ht.get("MSG");
		String dispmsg = (String)ht.get("DISPMSG");
		LOG.print("-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0-0");
		LOG.print("MSG "+user );
		LOG.print("DISPMSG"+dispmsg );
		
		if(user.equalsIgnoreCase("SUCCESS"))
		{
			dispmsg = user;
		//watchListCompanies.addElement(codesnp+"."+exchangesnp);
		}
		final String result  = dispmsg;
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				ScreenInvoker.removeRemovableScreen();
				if(result.equalsIgnoreCase("SUCCESS"))
				{
					WatchListMainBean bean = new WatchListMainBean();
					bean.setDisplayName(codesnp);
					bean.setTemplateName(codesnp);
					Utils.getWatchListedCompanyRecordsDataMain().addElement(bean);
					SHOW_DIALOG = true;
					SHOW_DIALOG_MESSAGE = "Added to Market Watch!";
					Utils.setWatchListedCompanyRecordsDataMain(null);
					Action action = new Action(ActionCommand.CMD_WATCHLIST_SCREEN, null);
					ActionInvoker.processCommand(action);
//					createUI(null, false);
//					verticalWatchListFieldManager.insert(new WatchListHomeField(FOCUSABLE, codesnp, null)
//					{
//						protected boolean navigationClick(int status, int time)
//						{
//							if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
//							handler();
//						}
//							return super.navigationClick(status, time);
//							
//							
//						}
//						protected boolean touchEvent(TouchEvent message)
//						{
//							setFocus();
//							if(message.getEvent() == TouchEvent.CLICK) {
//								if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
//								handler();
//						}	}return super.touchEvent(message);
//						}
//						private void handler()
//						{
//							if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
//							Utils.WATCHLIST_CLOSE = false;
//							Utils.WATCHLIST_INDEX = 0;
//							MyWatchList.isFINISHED  = false;
//							MyWatchList.REFRESH = false;
//							/*Vector vectorCommandData = new Vector();
//						vectorCommandData.addElement((String)watchListCompanies.elementAt(y));*/
//						ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
//						Utils.WATCHLIST_NAME = codesnp;
//						Utils.WATCHLIST = true;
//						ReturnStringParser rts = new ReturnStringParser(Utils.getWatchListURL(UserInfo.getUserID(),Utils.WATCHLIST_NAME), WATCHLISTSCRIPDATA, returnString, true);
//						/*Action action = new Action(ActionCommand.CMD_WL_FILL_SCREEN, vectorCommandData);
//						ActionInvoker.processCommand(action);*/
//					}
//						}
//					},verticalWatchListFieldManager.getFieldCount()-1);
//					//Vector v = Utils.getWatchListedCompanyRecordsDataMain();
//					//v.addElement(codesnp);
//					//Utils.setWatchListedCompanyRecordsDataMain(v);
//					Dialog.alert("Added to Market Watch!");

				}
				else
					Dialog.alert(result/*"Add to Market Watch Failed!"*/);

				LOG.print("Loading Removed");
timer = System.currentTimeMillis();
			}
		});
		
	}
	}

}
