package com.snapwork.view;

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

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.WatchListMainBean;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomLinkButton;
import com.snapwork.components.CustomLinkButtonUnderLine;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TitleBar2;
import com.snapwork.components.WatchListField;
import com.snapwork.components.WatchListFieldRemove;
import com.snapwork.components.WatchListHomeField;
import com.snapwork.components.WatchListHomeRemoveField;
import com.snapwork.parsers.Json;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class MyWatchMainListEdit extends MainScreen implements ActionListener {

	VerticalFieldManager verticalWatchListFieldManager = null;
	private BottomMenu bottomMenu = null;
	//private CustomLinkButtonUnderLine refreshButton = null; 
	private Vector watchListCompanies; 
	private PopUpView customPopUpScreen;
	private Thread thread;
	private long timer;
	private boolean reload;
	public MyWatchMainListEdit(Vector watchListCompanies) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		/*Vector vector = new Vector();
    	for(int i=0; i< watchListCompanies.size(); i++)
    	{
    		String b = (String)watchListCompanies.elementAt(i);
    		if(vector.size() == 0)
    		{
    			vector.addElement(b);
    		}
    		else
    		{
    			boolean flag = false;
    			for(int j=0; j<vector.size();j++)
    			{
    				String a = (String) vector.elementAt(j);
    				if(a.equalsIgnoreCase(b))
    				{
    					flag = true;
    				}
    			}
    			if(flag)
    			{
    				vector.addElement(b);
    			}
    		}
    		if(i == watchListCompanies.size()-1)
    			createUI(vector);
    	}*/
		createUI(watchListCompanies);
	}

	public void createUI(Vector watchListCompanies_) {

		//Sets the title
		setTitle(new TitleBar("Edit Watchlist"));
		timer = System.currentTimeMillis();
		watchListCompanies = watchListCompanies_;
		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight()<AppConstants.screenHeight?AppConstants.screenHeight:getHeight());
			}
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(maxWidth,maxHeight);
				setExtent(maxWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
				//C super.sublayout(maxWidth,maxHeight<AppConstants.screenHeight?AppConstants.screenHeight:maxHeight);
				//C setExtent(maxWidth,maxHeight<AppConstants.screenHeight?AppConstants.screenHeight:maxHeight);
				//setExtent(maxWidth, AppConstants.screenHeight-TitleBar2.getItemHeight());
				//setExtent(maxWidth, AppConstants.screenHeight-TitleBar2.getItemHeight());
			}
		};
		//LOG.print("Utils.getWatchListedCompanyRecords().size() "+Utils.getWatchListedCompanyRecords().size());
		verticalWatchListFieldManager = new VerticalFieldManager(FOCUSABLE)
		{
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		LOG.print("watchListCompanies.size() : "+watchListCompanies.size());
		//watchListCompanies = Utils.getWatchListedCompanyRecordsDataMain();
		//Vector inbuildWatchList = Utils.getWatchListedCompanyRecords();
		// final String exch = Utils.WATCHLIST_NAME.indexOf('.')<0?(Utils.WATCHLIST_NAME.equalsIgnoreCase("NIFTY")?"NSE":"BSE"):Utils.WATCHLIST_NAME.substring(Utils.WATCHLIST_NAME.length()-3, Utils.WATCHLIST_NAME.length());
		for(int i=0;i<watchListCompanies.size();i++) {
			if(watchListCompanies.elementAt(i) instanceof WatchListMainBean)
			{
				final WatchListMainBean bean = (WatchListMainBean)watchListCompanies.elementAt(i);
			final int y = i;
			//String text = bean.getTemplateName();
			//LOG.print(text);
			// HomeJson homeJSon = (HomeJson)watchListCompanies.elementAt(i);
			//if(homeJSon.getChange().indexOf("-")==-1)
			boolean fl = true;
			if(bean.getTemplateName().equalsIgnoreCase("NIFTY") || bean.getTemplateName().equalsIgnoreCase("SENSEX"))
			{fl = false;}
			else{
				final boolean flag = fl; 
				verticalWatchListFieldManager.add( new WatchListHomeRemoveField(FOCUSABLE, bean, null,flag)
				{
					

					WatchListHomeRemoveField wl = this;

					protected boolean navigationClick(int status, int time)
					{
						if((timer+100)<System.currentTimeMillis()){
							timer = System.currentTimeMillis();
						if(flag)
							UiApplication.getUiApplication().invokeLater(new Runnable() {
								public void run() {
									customPopUpScreen = new PopUpView(wl);
									UiApplication.getUiApplication().pushScreen(customPopUpScreen);
								}});
						}
						return super.navigationClick(status, time);
					}

					protected boolean touchEvent(TouchEvent message)
					{ setFocus();
						if(message.getEvent() == TouchEvent.CLICK) {
							if((timer+100)<System.currentTimeMillis()){timer = System.currentTimeMillis();
							if(flag) 
							UiApplication.getUiApplication().invokeLater(new Runnable() {
								public void run() {
									customPopUpScreen = new PopUpView(wl);
									UiApplication.getUiApplication().pushScreen(customPopUpScreen);
								}});
					}}return super.touchEvent(message);
					}
				});
			}
		}
		}
		
		mainManager.add(verticalWatchListFieldManager);
		add(mainManager);
		// Configure BottomMenu and set commands
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_WATCHLIST_SCREEN, AppConstants.bottomMenuCommands);
	}

	public void actionPerfomed(byte Command, Object sender) {
		switch(Command) {
		case ActionCommand.CMD_WATCHLIST_SCREEN:
		MyWatchList.isFINISHED = false;
		MyWatchList.REFRESH = false;
		if(reload)
			Utils.setWatchListedCompanyRecordsDataMain(null);
		Action action = new Action(ActionCommand.CMD_WATCHLIST_SCREEN, null);
		ActionInvoker.processCommand(action);
		break;
		default: ActionInvoker.processCommand(new Action(Command,null));
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
		if(key== Keypad.KEY_MENU)
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
			MyWatchList.isFINISHED = false;
			MyWatchList.REFRESH = false;
			if(reload)
				Utils.setWatchListedCompanyRecordsDataMain(null);
			Action action = new Action(ActionCommand.CMD_WATCHLIST_SCREEN, null);
			ActionInvoker.processCommand(action);
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

	/*public boolean onClose() {
	Action action = new Action(ActionCommand.CMD_WL_SCREEN, null);
	ActionInvoker.processCommand(action);
	return super.onClose();
}*/
public class PopUpView extends PopupScreen {

	private String strTitle = "";
	private HorizontalFieldManager horizontalFieldManager;
	public PopUpView(WatchListHomeRemoveField field) {
		super(new VerticalFieldManager(),Field.FOCUSABLE);

		strTitle = "Do you want to delete from Market Watch?";

		createUI(strTitle, field);
	}

	public void createUI(final String notificationType, final WatchListHomeRemoveField field) {
		//set the title
		LabelField lblTitle = new LabelField(strTitle);

		horizontalFieldManager = new HorizontalFieldManager(FIELD_HCENTER);


		ButtonField btnYes = new ButtonField("Yes",FOCUSABLE | FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {                                                   
				synchronized( UiApplication.getEventLock() ){

					if(isDisplayed()) 
					{
						startRegistrationProcess(field, field.getName());
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
	public void startRegistrationProcess(final WatchListHomeRemoveField wlField,final String wlname) {
		ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);

		thread = new Thread(new Runnable() {
			public void run() {
				try {
					//doPaint();
					//Show wait registering moments
					String strAuthURL = Utils.getWatchListURLRemove(UserInfo.getUserID(),wlField.getTemplate());
					LOG.print("HTTP call started "+strAuthURL);
					String strResponse = HttpProcess.getHttpsMD5Connection(strAuthURL);

					final boolean result = parseAndSaveRegistrationInfo(strResponse,wlField);
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							if(!Utils.sessionAlive) return;
							//if (result)
							LOG.print("HTTP call finished");
							if (result)
							{
								
								ScreenInvoker.removeRemovableScreen();
								verticalWatchListFieldManager.delete(wlField);
								for(int i=0;i<Utils.getWatchListedCompanyRecordsDataMain().size();i++)
								{
									WatchListMainBean bean = (WatchListMainBean)Utils.getWatchListedCompanyRecordsDataMain().elementAt(i);
									String stri = bean.getTemplateName();
									if(stri.equals(wlname))
									{
										Utils.getWatchListedCompanyRecordsDataMain().removeElementAt(i);
										break;
									}
								}
								//Utils.getWatchListedCompanyRecordsDataMain().removeElement(wlField.getName());
								//Utils.getWatchListedCompanyRecordsDataMain().removeElement(wlField.getTemplate());
								//ActionInvoker.processCommand(new Action(ActionCommand.CMD_GRID_SCREEN,null));
								reload = true;
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


	public boolean parseAndSaveRegistrationInfo(String strResponse,final WatchListHomeRemoveField wlField)
	{
		LOG.print("HTTP call inProcess");
		if(!Utils.sessionAlive) return false;
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
