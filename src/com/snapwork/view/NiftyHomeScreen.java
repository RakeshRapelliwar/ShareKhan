package com.snapwork.view;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
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
import com.snapwork.components.CustomInfoLabelField;
import com.snapwork.components.GL_FutureOptionView;
import com.snapwork.components.HomeScreenBanner;
import com.snapwork.components.LoadingComponent;
import com.snapwork.components.RefreshButton;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TitleBarRefresh;
import com.snapwork.util.AppConstants;
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;

public class NiftyHomeScreen extends MainScreen implements ThreadedComponents,ActionListener,AutoRefreshableScreen {

	public static final byte COMPONENT_NSE_CHART = 0;
	private BottomMenu bottomMenu = null;
	private boolean isChartLoaded = false;
	private boolean isLoaded = false;
	private RefreshButton refreshme;
	private long graphRefreshTime = 0;

	
	CustomInfoLabelField lblValues = null;
	
	private int blockAutoRefresh = 0;
	private Field field;
	private String choose;
	private String id;
	
	private int check=1;
	
	public NiftyHomeScreen(Object identifier) {
		this.id=(String) identifier;
		if(id.equalsIgnoreCase("BSE"))
			choose=CurrentStat.BSE_COMPANYCODE;
		else if (id.equalsIgnoreCase("NSE")) 
			choose=CurrentStat.NSE_COMPANYCODE;
		else if (id.equalsIgnoreCase("NSEFO"))
		{
			choose=CurrentStat.NSEFO_COMPANYCODE;
			check=0;
		}
		else if (id.equalsIgnoreCase("MCX")) 
			choose=CurrentStat.MCX_COMPANYCODE;
		else if (id.equalsIgnoreCase("NCDEX")) 
			choose=CurrentStat.NCDEX_COMPANYCODE;
		
			
		
//		Dialog.alert(id);
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		AutoScreenRefresherThread.onLoad = false;
		createUI();
	}

	protected void paintBackground(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, AppConstants.screenWidth, AppConstants.screenHeight);
	}

	//Fields for the screen
	HorizontalFieldManager horizontalFieldManager = null;
	VerticalFieldManager verticalFieldManager = null;
	HomeScreenBanner nseBanner = null;
	LabelField lastUpdatedDateTime = null;
	public void createUI() {
		refreshme = new RefreshButton()
		{
			protected boolean navigationClick(int arg0, int arg1) {
				AutoScreenRefresherThread.skipme = true;
				isChartLoaded = true;
					refreshFields();
					return super.navigationClick(arg0, arg1);
				}
		};
		//Sets the title
		setTitle(new TitleBarRefresh(id, refreshme));
		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}            
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(AppConstants.screenWidth, AppConstants.screenHeight);
				setExtent(AppConstants.screenWidth, AppConstants.screenHeight-BottomMenu.getItemHeight()-TitleBar.getItemHeight());
			}
		};
		//Create Horizontal Field Manager
		horizontalFieldManager = new HorizontalFieldManager(FIELD_HCENTER | FIELD_HCENTER | NON_FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}
		};

		
		
		if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			lblValues = new CustomInfoLabelField(FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT_FNO));
		else
			lblValues = new CustomInfoLabelField(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
		
		
		//Create Vertical Field Manager
		verticalFieldManager = new VerticalFieldManager(USE_ALL_WIDTH | FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}
		};

		nseBanner = new HomeScreenBanner(FIELD_HCENTER, id,check);
		lastUpdatedDateTime = new LabelField("", NON_FOCUSABLE | FIELD_RIGHT){
			protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}
		};
		lastUpdatedDateTime.setFont(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT));
		verticalFieldManager.add(new NullField(FOCUSABLE));
		verticalFieldManager.add(lastUpdatedDateTime);
		verticalFieldManager.add(nseBanner);
		
		
		lblValues.setValue("Day High/Low", "","","","Volume", "");
		
		
		
		
		
		lblValues.setMargin(5, 0, 5, 0);
		
		
		verticalFieldManager.add(lblValues);
		
		
		ButtonField  fnoButton = new ButtonField (" NIFTY F&O ", LabelField.FOCUSABLE | FIELD_HCENTER | FIELD_VCENTER)
		{
			protected boolean navigationClick(int status, int time) {
				GL_FutureOptionView.niftyIndex = 0;
				AppConstants.source = "17023929";
				AppConstants.COMPANY_NAME = "NIFTY";
				AppConstants.OPTIONS_FLAG = false;
				AppConstants.optionsMonth = "";
				AppConstants.optionsAmount = "";
				AppConstants.optionsCEPE = "";
				actionPerfomed(ActionCommand.CMD_FUTURE_SCREEN, null);
				return super.navigationClick(status, time);
			}
		};
		
		horizontalFieldManager.add(new LoadingComponent(AppConstants.loadingMessage,AppConstants.screenWidth,ChartProperties.getNSEChartProperties().getChartHeight()+ChartProperties.getNSEChartProperties().getChartxAxisHeight()));

		verticalFieldManager.add(horizontalFieldManager);
       if(id.equalsIgnoreCase("NSE"))
		  verticalFieldManager.add(fnoButton);
		
		mainManager.add(verticalFieldManager);
		add(mainManager);

		// Configure BottomMenu and set commands
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_GRID_SCREEN, AppConstants.bottomMenuCommands);
		
	}

	public void componentsPrepared(byte componentID,final Object component) {
		switch(componentID) {
		case COMPONENT_NSE_CHART:
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
                        field = (Field) component;
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
					//isChartLoaded = true;
					graphRefreshTime = System.currentTimeMillis();
				}
			});
		}
	}

	public void componentsDataPrepared(byte componentID, final Object data) {
		switch(componentID) {
		case HomeScreen.BANNERS_DATA:
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Vector vector = (Vector)data;
					for(byte i=0;i<vector.size();i++) {
						HomeJson homeJson = (HomeJson)vector.elementAt(i);
						if(homeJson.getCompanyCode().equals(choose)) {
							lastUpdatedDateTime.setText("Last Updated : "+homeJson.getLastTradedTime());
							nseBanner.setStartFlag(false);
							
							
//							Dialog.alert(""+homeJson.getA());
							
							nseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), homeJson.getPercentageDiff(),homeJson.getA(),homeJson.getD(),homeJson.getS());
							nseBanner.startUpdate();
							
							lblValues.setValue("Day High/Low", homeJson.getHigh()+"-"+homeJson.getLow(),
									"", "",
									"Volume", homeJson.getVolume());
							
							
							/*if(field!=null) {
								horizontalFieldManager.deleteAll();
								horizontalFieldManager.add(field);
							}*/
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
		case ActionCommand.CMD_NSE_GET_GRAPH_DATA:
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
		// TODO Auto-generated method stub
		return null;
	}

	public void refreshFields() {
		if(blockAutoRefresh == 1 ){blockAutoRefresh--; return;}
		else if(blockAutoRefresh == 2) blockAutoRefresh--;
		if(nseBanner.isBlockLoaded()==true) {
			refreshme.setLoading(true);
			Debug.debug("Refreshing NSE banner");
			//UiApplication.getUiApplication().invokeLater(new Runnable() {
			//	public void run() {
					actionPerfomed(ActionCommand.CMD_BANNERS_HOME_SCREEN, null);
			//	}
			//});		
		}
		else
			refreshme.setLoading(false);
		
		LOG.print("isChartLoaded............."+isChartLoaded);
		LOG.print("ChartComponent.isDrawFinished............."+ChartComponent.isDrawFinished);
		
		if(isChartLoaded == true /*&& (graphRefreshTime+10000)<System.currentTimeMillis() && graphRefreshTime!=0*/) {
			//ChartComponent.isDrawFinished = false;
			isChartLoaded = false;
			Debug.debug("Refreshing Charts");
			refreshme.setLoading(true);
			//UiApplication.getUiApplication().invokeLater(new Runnable() {
			//	public void run() {
					actionPerfomed(ActionCommand.CMD_NSE_GET_GRAPH_DATA, null);
					//isChartLoaded = false;
			//	}
			//});		
		}
	}
	public boolean onSavePrompt() {
		return true;
	}

}
