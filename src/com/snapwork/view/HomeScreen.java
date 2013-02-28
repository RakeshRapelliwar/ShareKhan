package com.snapwork.view;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.areacharts.ChartComponent;
import com.snapwork.areacharts.ChartProperties;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomLinkButtonUnderLine;
import com.snapwork.components.HomeScreenBanner;
import com.snapwork.components.LoadingComponent;
import com.snapwork.components.RefreshButton;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBarRefresh;
import com.snapwork.util.AppConstants;
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.Utils;

public class HomeScreen extends MainScreen implements ThreadedComponents,ActionListener,AutoRefreshableScreen {


	public static final byte COMPONENT_BSE_CHART = 0;
	public static final byte BANNERS_DATA = 1;

	public static final String BSE_COMPANYCODE = "17023928";
	public static final String NSE_COMPANYCODE = "17023929";
	public static final String MCX_COMPANYCODE = "MCXFO";
	public static final String NCDEX_COMPANYCODE = "NCDEXFO";
	private BottomMenu bottomMenu = null;
	private boolean isLoaded = false;
	private boolean refreshPressed = false;
	private Field field;
	private Hashtable hasht;
	
	public HomeScreen() {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		AutoScreenRefresherThread.onLoad = false;
		hasht = new Hashtable();
		createUI();
	}

	//Fields for the screen
	HorizontalFieldManager horizontalFieldManager = null;
	VerticalFieldManager verticalFieldManager = null;
	public HomeScreenBanner bseBanner = null;
	public HomeScreenBanner nseBanner = null;
	public HomeScreenBanner mcxBanner = null;
	public HomeScreenBanner ncdexBanner = null;
//	LabelField lastUpdatedDateTime = null;
	private RefreshButton refreshme;
	CustomLinkButtonUnderLine refreshButton = null;
	private int blockAutoRefresh = 0;
	public void createUI() {
		refreshme = new RefreshButton()
		{
			protected boolean navigationClick(int arg0, int arg1) {
				AutoScreenRefresherThread.skipme = true;
					refreshFields();
					return super.navigationClick(arg0, arg1);
				}
		};
		//Sets the title
		setTitle(new TitleBarRefresh("Markets Today", refreshme));

		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}            
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(AppConstants.screenWidth, AppConstants.screenHeight);
				setExtent(AppConstants.screenWidth, AppConstants.screenHeight-BottomMenu.getItemHeight()-TitleBarRefresh.getItemHeight());
			}
		};

		//Create Horizontal Field Manager
		horizontalFieldManager = new HorizontalFieldManager(FIELD_HCENTER | FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, AppConstants.screenWidth, AppConstants.screenHeight);
			}
		};

		//Create Vertical Field Manager
		verticalFieldManager = new VerticalFieldManager(USE_ALL_WIDTH | FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, AppConstants.screenWidth, AppConstants.screenHeight);
			}
		};

		bseBanner = new HomeScreenBanner(FIELD_HCENTER, "BSE",1);
		mcxBanner = new HomeScreenBanner(FIELD_HCENTER, "MCX",1);
		ncdexBanner = new HomeScreenBanner(FIELD_HCENTER, "NCDEX",1);
		nseBanner = new HomeScreenBanner(FOCUSABLE | FIELD_HCENTER, "NSE",1) {
			protected boolean navigationClick(int status, int time) {
				Action action = new Action(ActionCommand.CMD_NIFTY_SCREEN, null);
				ActionInvoker.processCommand(action);
				return super.navigationClick(status, time);
			}
		};

	/*	lastUpdatedDateTime = new LabelField("", NON_FOCUSABLE | FIELD_RIGHT){
			protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}
		};*/
		Manager topManager = new Manager(USE_ALL_WIDTH | USE_ALL_HEIGHT | FIELD_HCENTER | FIELD_VCENTER) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}

			protected void sublayout(int maxWidth, int maxHeight) {
				maxWidth = AppConstants.screenWidth - AppConstants.screenWidth
				/ 15;
				maxHeight = FontLoader.getFont(AppConstants.SMALL_BOLD_FONT)
				.getHeight() + AppConstants.padding;
				layoutChild(getField(0), maxWidth, maxHeight);
				layoutChild(getField(1), maxWidth, maxHeight);

				setPositionChild(getField(0), 0, maxHeight / 2
						- getField(0).getFont().getHeight() / 2);
				setPositionChild(
						getField(1),
						maxWidth
						- getField(1).getFont().getAdvance(
								((LabelField) getField(1)).getText()),
								maxHeight / 2 - getField(1).getFont().getHeight() / 2);

				setExtent(maxWidth, maxHeight);
			}

		};
		//lastUpdatedDateTime.setFont(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT));

		//verticalFieldManager.add(lastUpdatedDateTime);
		String strButtonText = ("Refresh" );

		refreshButton = new CustomLinkButtonUnderLine(strButtonText, FOCUSABLE | FIELD_LEFT | FIELD_VCENTER, 0xeeeeee, FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT)) {

			protected boolean navigationClick(int status, int time) {
				/*Action action = new Action(ActionCommand.CMD_HOME_SCREEN, null);
	                                ActionInvoker.processCommand(action);*/
				//bseBanner.reset();
				//nseBanner.reset();
				refreshPressed = true;
				refreshFields();
				return super.navigationClick(status, time);
			}

		};

		topManager.add(/*refreshButton*/new NullField(NON_FOCUSABLE));
		//topManager.add(lastUpdatedDateTime);
		topManager.add(Utils.BlankField(10, AppConstants.screenWidth, 0x000000));

		verticalFieldManager.add(topManager);

		verticalFieldManager.add(bseBanner);
		verticalFieldManager.add(nseBanner);
		verticalFieldManager.add(mcxBanner);
		verticalFieldManager.add(ncdexBanner);
		horizontalFieldManager.add(new LoadingComponent(AppConstants.loadingMessage,AppConstants.screenWidth,ChartProperties.getBSEChartProperties().getChartHeight()+ChartProperties.getBSEChartProperties().getChartxAxisHeight()*2));

		verticalFieldManager.add(horizontalFieldManager);

		mainManager.add(verticalFieldManager);
		add(mainManager);
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_HOME_SCREEN, AppConstants.bottomMenuCommands);

	}        
	public void componentsPrepared(byte componentID,final Object component) {
		
		switch(componentID) {
		case COMPONENT_BSE_CHART:
			/*if(field != null) 
			{
				field = (Field)component;
				isChartLoaded = true;
				graphRefreshTime = System.currentTimeMillis();
				return;
				}*/
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					if(component==null) {
						horizontalFieldManager.deleteAll();
					} else {
						horizontalFieldManager.deleteAll();
						horizontalFieldManager.add((Field)component);
						field = (Field)component;
					}
					//isChartLoaded = true;
					if(!isLoaded)
					{
						UiApplication.getUiApplication().invokeAndWait(new Runnable() {
							public void run() {
								ScreenInvoker.removeRemovableScreen();  
							}
						});
						isLoaded = true;
					}

				}

			});
			break;
		}
	}

	public void componentsDataPrepared(byte componentID, final Object data) {
		switch(componentID) {
		case BANNERS_DATA:
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					LOG.print("HOME SCREEN componentsDataPrepared ");
					Vector vector = (Vector)data;
					LOG.print("BSE Banner Vector Size "+vector.size());
					
					for(byte i=0;i<vector.size();i++) {
						HomeJson homeJson = (HomeJson)vector.elementAt(i);
						if(homeJson.getCompanyCode().equals(BSE_COMPANYCODE)) {
							Utils.bseJsonStore = homeJson;
							bseBanner.setStartFlag(false);
							bseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), homeJson.getPercentageDiff(),homeJson.getA(),homeJson.getD(),homeJson.getS());
							bseBanner.startUpdate();
						} else if(homeJson.getCompanyCode().equals(NSE_COMPANYCODE)) {
							nseBanner.setStartFlag(false);
							nseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), homeJson.getPercentageDiff(),homeJson.getA(),homeJson.getD(),homeJson.getS());
							nseBanner.startUpdate();
						}
						else if(homeJson.getCompanyCode().equals(MCX_COMPANYCODE)) {
							mcxBanner.setStartFlag(false);
							mcxBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), homeJson.getPercentageDiff(),homeJson.getA(),homeJson.getD(),homeJson.getS());
							mcxBanner.startUpdate();
						}
						else if(homeJson.getCompanyCode().equals(NCDEX_COMPANYCODE)) {
							ncdexBanner.setStartFlag(false);
							bseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), homeJson.getPercentageDiff(),homeJson.getA(),homeJson.getD(),homeJson.getS());
							bseBanner.startUpdate();
						}
					}
					if(!isLoaded)
					{
						UiApplication.getUiApplication().invokeAndWait(new Runnable() {
							public void run() {
								ScreenInvoker.removeRemovableScreen();  
							}
						});
						isLoaded = true;
					}
					refreshme.setLoading(false);
				}
			});
			break;
		}
	}

	public void actionPerfomed(byte Command, Object sender) {
		switch(Command) {
		case ActionCommand.CMD_BANNERS_HOME_SCREEN:
		//case ActionCommand.CMD_BSE_GET_GRAPH_DATA:
			ActionInvoker.processCommand(new Action(Command,(ThreadedComponents)this));
			break;
		default:
			ActionInvoker.processCommand(new Action(Command,sender));
			break;
		}
	}

	public boolean onMenu(int instance) 
	{
		return false;
	}
	
	protected  void makeMenu(Menu menu, int instance)
    {
        ContextMenu contextMenu = ContextMenu.getInstance();
        contextMenu.setTarget(this);
        contextMenu.clear();
        this.makeContextMenu(contextMenu);
        menu.deleteAll();
        menu.add(contextMenu);
    }
    /*public void makeContextMenu(ContextMenu contextMenu)
    {
            contextMenu.addItem(YourMenuItem);   

    }*/

	public boolean keyChar( char key, int status, int time )
	{
		return false;
	}

	public boolean keyDown( int keyCode, int time )
	{

		int key = Keypad.key( keyCode );
		if(key == Keypad.KEY_MENU) {
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

	public void refreshFields() {
		if(blockAutoRefresh == 1 ){blockAutoRefresh--; return;}
		else if(blockAutoRefresh == 2) blockAutoRefresh--;
		if(bseBanner.isBlockLoaded() == true && nseBanner.isBlockLoaded()==true) {
			refreshme.setLoading(true);
			refreshPressed = false;
			actionPerfomed(ActionCommand.CMD_BANNERS_HOME_SCREEN, null);
		}
		else
		{
			refreshPressed = false;
			refreshme.setLoading(false);
		}
	}
}