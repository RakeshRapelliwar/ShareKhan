package com.snapwork.view;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.areacharts.ChartComponent;
import com.snapwork.areacharts.ChartProperties;
import com.snapwork.beans.FoScrips;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.WatchListMainBean;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomInfoLabelField;
import com.snapwork.components.CustomLinkButtonUnderLine;
import com.snapwork.components.HomeScreenBanner;
import com.snapwork.components.LoadingComponent;
import com.snapwork.components.RefreshButton;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TitleBarRefresh;
import com.snapwork.components.WatchListSecondField;
import com.snapwork.parsers.Json;
import com.snapwork.util.AppConstants;
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.URLEncode;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;
import com.snapwork.view.trade.TradeNowMainScreen;
import com.snapwork.view.trade.TradeNowMainScreenCM;

public class CompanyDetailsSnippetsScreen extends MainScreen implements ThreadedComponents,ActionListener,AutoRefreshableScreen {

	public static final byte COMPONENT_COMPANY_CHART = 0;
	public static final byte BANNERS_DATA = 1;
	Thread controllerThread = null;
	Thread updaterThread = null;
	BottomMenu bottomMenu = null;
	private String strTitle = null;
	private String strCompanyCode = null;
	private boolean isChartLoaded = false;
	MenuItem _addRemoveItem = null;
	MenuItem _FNOItem = null;
	MenuItem _tradeNowItem = null;
	MenuItem _marketDepth = null;
	MenuItem _homeItem = null;
	MenuItem _searchItem = null;
	private static String storeCCode;
	private boolean isRefresh;
	private CustomLinkButtonUnderLine refreshButton = null;    
	private boolean restrict = false;
	private long graphRefreshTime = 0;
	private Field field;
	private Thread thread;
	private HomeJson homeJsonC = null;
	//addMenuItem(SeparatorField.LINE_HORIZONTAL);

	private String src=null;
	private int index;
	private byte actionCommand;
	//Fields for the screen
	HorizontalFieldManager horizontalFieldManager = null;
	VerticalFieldManager verticalFieldManager = null;
	public HomeScreenBanner bseBanner = null;
	public HomeScreenBanner nseBanner = null;
	public HomeJson jsonBanner = null;
	public static Vector marketDepthVector;
	private boolean menuBlock;

	LabelField lastUpdatedDateTime = null;
	CustomInfoLabelField lblValues = null;
	boolean isCompanyInTheWatchList = false;
	private String symbol = "";
	private boolean refreshPressed = true;
	private RefreshButton refreshme;
	private int blockAutoRefresh = 0;
	//blic static boolean addDirect;
	private boolean addDir;
	private CompanyDetailsSnippetsScreen cmp = this;
	private long timer;
	private boolean REFRESH = true;

	public CompanyDetailsSnippetsScreen(int index, String strTitle,final String source,final String companyCode) {
		
		
		
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		this.strTitle = strTitle;
		marketDepthVector = null;
		
		
		
		
		//if(addDirect)
		//{
			addDir = Utils.WATCHLIST_MODE;
		//	addDirect = false;
		//}
		isRefresh = true;
		restrict = false;
		timer = System.currentTimeMillis();
		this.strCompanyCode = companyCode;
		if(index==0)
			AppConstants.source = companyCode;
		else
			{
				symbol = this.src = source;
				//symbol = Utils.NSE_SYMBOL;
			}
		
		this.src = source;
		this.index=index;
		LOG.print("strTitle "+strTitle);
		LOG.print("companyCode "+companyCode);
		LOG.print("AppConstants.source "+AppConstants.source);
		LOG.print("symbol "+symbol);
		if(index==0)
		{
			AppConstants.NSE = false;
			this.actionCommand=ActionCommand.CMD_COMPANY_DETAILS_SCREEN;
		}
		else
		{
			AppConstants.NSE = true;
			this.actionCommand=ActionCommand.CMD_COMPANY_DETAILS_SCREEN_NSE;
		}
		createUI(strTitle,source,companyCode);
	}

	public void createUI(final String strTitle,final String source,final String companyCode) {
		//System.out.println("Company Details Snippets");
		AppConstants.COMPANY_NAME = strTitle;
		refreshme = new RefreshButton()
		{
			protected boolean navigationClick(int arg0, int arg1) {
				menuBlock = true;
				blockAutoRefresh = 2;
				isChartLoaded = true;
				AutoScreenRefresherThread.skipme = true;
				if(AppConstants.NSE && !restrict || restrict && !AppConstants.NSE || !restrict && !AppConstants.NSE)
				{
					//setLoading(true);
					refreshPressed = true;
					refreshFields();
					//refreshPage();
				}
					return super.navigationClick(arg0, arg1);
				}
		};
		setTitle(new TitleBarRefresh(strTitle, refreshme));
		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}            
			protected void sublayout( int maxWidth, int maxHeight)
			{
				super.sublayout(AppConstants.screenWidth, AppConstants.screenHeight);
				setExtent(AppConstants.screenWidth, AppConstants.screenHeight-BottomMenu.getItemHeight()-TitleBar.getItemHeight());
			}
		};

		horizontalFieldManager = new HorizontalFieldManager(FIELD_HCENTER | FIELD_HCENTER | FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, AppConstants.screenWidth, AppConstants.screenHeight);
			}
		};

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
		//Create Vertical Field Manager
		verticalFieldManager = new VerticalFieldManager(USE_ALL_WIDTH | FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, AppConstants.screenWidth, AppConstants.screenHeight);
			}
		};

		if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			lblValues = new CustomInfoLabelField(FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT_FNO));
		else
			lblValues = new CustomInfoLabelField(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));

		if(!AppConstants.NSE)
			bseBanner = new HomeScreenBanner(FIELD_HCENTER, "BSE",0);
		else
		{
			nseBanner = new HomeScreenBanner(FIELD_HCENTER, "NSE",0);

			LOG.print("createUI strCompanyCode "+strCompanyCode);
		}
		lastUpdatedDateTime = new LabelField("", NON_FOCUSABLE | FIELD_RIGHT | FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}
		};
		lastUpdatedDateTime.setFont(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT));
		String strButtonText = "+ Add to Watchlist";
		String strButtonTex = ("Refresh" );

		refreshButton = new CustomLinkButtonUnderLine(strButtonTex, FOCUSABLE | FIELD_LEFT | FIELD_VCENTER, 0xeeeeee, lastUpdatedDateTime.getFont()) {

			protected boolean navigationClick(int status, int time) {
				if(AppConstants.NSE && !restrict || restrict && !AppConstants.NSE || !restrict && !AppConstants.NSE)
				{
					refreshPressed = true;
					refreshPage();
				}
				return super.navigationClick(status, time);
			}

		};
		topManager.add(/*refreshButton*/ new NullField(NON_FOCUSABLE));
		topManager.add(lastUpdatedDateTime);
		verticalFieldManager.add(topManager);
		if(!AppConstants.NSE)
			verticalFieldManager.add(bseBanner);
		else
			verticalFieldManager.add(nseBanner);
		verticalFieldManager.add(lblValues);


		horizontalFieldManager.add(new LoadingComponent(AppConstants.loadingMessage,AppConstants.screenWidth,ChartProperties.getDefaultChartProperties().getChartHeight()+ChartProperties.getDefaultChartProperties().getChartxAxisHeight()*2));

		verticalFieldManager.add(horizontalFieldManager);
		mainManager.add(new TopTabber(this,this.index,(ThreadedComponents)this));
		mainManager.add(verticalFieldManager);

		_addRemoveItem = new MenuItem(strButtonText, 100, 100) //Add to / Remove from Watchlist
		{
			public void run() 
			{
				if(!Utils.sessionAlive)
				{
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
				}
				else if(!restrict)
				{
					UiApplication.getUiApplication().invokeAndWait(new Runnable() {
						public void run() {							
							Utils.WATCHLIST = true;
							MyWatchList.isFINISHED = false;
							MyWatchList.REFRESH = false;
							ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
		                    new WatchListPopUp(AppConstants.NSE?homeJsonC.getSymbol():homeJsonC.getReligareCode(), Utils.WATCHLIST_MODE);
						}
						});
				}
			}
		};
		_FNOItem = new MenuItem("F&O", 100, 100) 
		{
			public void run() 
			{
				if(!symbol.equalsIgnoreCase("null") && !restrict)
				{
					AppConstants.optionsMonth = "";
					AppConstants.optionsAmount = "";
					AppConstants.optionsCEPE = "";
					if(!Utils.sessionAlive)
					{
						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
					}
					else
					{
						AppConstants.OPTIONS_FLAG = false;
						AppConstants.source = symbol; //storeCCode;
						actionPerfomed(ActionCommand.CMD_FUTURE_SCREEN, null);
						//Action action = new Action(ActionCommand.CMD_FUTURE_SCREEN, null);
						//ActionInvoker.processCommand(action);
					}
				}
			}
		};
		AppConstants.WEBVIEW_URL_TEXT = "Trade";
		_tradeNowItem = new MenuItem("Trade", 100, 100) 
		{
			public void run() 
			{
				//if(!restrict)
				{
					if(marketDepthVector!=null && !restrict)
					{
						if(!Utils.sessionAlive)
						{
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
						}
						else
						{
							ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
							TradeNowMainScreenCM.storage = null;
						if(!AppConstants.NSE)
							{
							jsonBanner.setExchange(true);
							new TradeNowMainScreenCM("tradenow",AppConstants.WEBVIEW_URL,jsonBanner);
							}
						else
							{
							jsonBanner.setExchange(false);
							new TradeNowMainScreenCM("tradenow",AppConstants.WEBVIEW_URL,jsonBanner);
							}
						}
					}
				}


			}
		};
		_marketDepth = new MenuItem("Market Depth", 100, 100)
		{
			public void run() 
			{
				LOG.print(" - 0 = - 0 = 0 -");
				LOG.print("restrict "+restrict);
				//if(!restrict)
				{
					if(marketDepthVector!=null && !restrict)
					{
						LOG.print(" ---------->>>> ///////////////// marketDepthVector.size() "+marketDepthVector.size());
						if(marketDepthVector.size()==5)
						{
							actionPerfomed(ActionCommand.CMD_MARKET_DEPTH_SCREEN, marketDepthVector);
							//Action action = new Action(ActionCommand.CMD_MARKET_DEPTH_SCREEN, marketDepthVector);
							//ActionInvoker.processCommand(action);
						}
						else if(marketDepthVector.size()==4)
						{
							actionPerfomed(ActionCommand.CMD_MARKET_DEPTH_SCREEN, MarketDepthScreen.marketDepthdata_);
						}
					}
				}
			}
		};
		_homeItem = new MenuItem("Home", 100, 100)
		{
			public void run() 
			{
				actionPerfomed(ActionCommand.CMD_GRID_SCREEN, null);
			}
		};
		_searchItem = new MenuItem("Search Scrip", 100, 100)
		{
			public void run() 
			{
				actionPerfomed(ActionCommand.CMD_SEARCH_SCREEN, null);
			}
		};


		add(mainManager);
		refreshme.setLoading(true);
		// Configure BottomMenu and set commands
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomMenuCommands);

	}
	public String getTitle()
	{
		return this.strTitle;
	}
	public boolean onClose() {
		AppConstants.NSE = false;
		return super.onClose();
	}
	public void refreshPage()
	{
		refreshme.setLoading(true);
		Vector vectorCommandData = new Vector();
		vectorCommandData.addElement(this.strTitle);
		vectorCommandData.addElement(this.strCompanyCode);
		vectorCommandData.addElement(this.src);
		ActionInvoker.processCommand(new Action(this.actionCommand,vectorCommandData));
		//	actionListner.actionPerfomed(ActionCommand.CMD_COMPANY_DETAILS_SCREEN, vectorCommandData);
	}
	public void refreshPage(int b)
	{

		//refreshFields();
		if(UiApplication.getUiApplication().getActiveScreen() instanceof PopupScreen)
		{
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
		}
		//UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
		Vector vectorCommandData = new Vector();
		vectorCommandData.addElement(this.strTitle);
		LOG.print("this.strTitle"+this.strTitle);
		if(AppConstants.NSE)
		{
			vectorCommandData.addElement(symbol);
			LOG.print("symbol"+symbol);
		}
		else
		{
			vectorCommandData.addElement(AppConstants.source);
			LOG.print("AppConstants.source"+AppConstants.source);
		}

		vectorCommandData.addElement(this.src);
		LOG.print("src "+this.src);
		if(b==0)
			ActionInvoker.processCommand(new Action(ActionCommand.CMD_COMPANY_DETAILS_SCREEN,vectorCommandData));
		else 
			ActionInvoker.processCommand(new Action(ActionCommand.CMD_COMPANY_DETAILS_SCREEN_NSE,vectorCommandData));
		//	actionListner.actionPerfomed(ActionCommand.CMD_COMPANY_DETAILS_SCREEN, vectorCommandData);
	}
	public boolean onMenu(int instance) 
	{
		LOG.print("onMenumenuBlock "+menuBlock);
		if(menuBlock){menuBlock = false; return false;}
		return super.onMenu(instance);
	}
	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		LOG.print("onMenumenuBlock "+menuBlock);
		if(menuBlock){menuBlock = false; return;}
		//if(!refreshme.getLoading()){
		//if(symbol.length()>0)
		menu.add(_tradeNowItem);
		menu.add(_addRemoveItem);
		if(FoScrips.getValueForCompany(AppConstants.source)!=null) {
			menu.add(_FNOItem);
		}

		menu.add(_marketDepth);
		menu.add(MenuItem.separator(100));
		//addMenuItem(SeparatorField.LINE_HORIZONTAL);
		menu.add(_homeItem);
		menu.add(_searchItem);
		//}
	}
	public void componentsPrepared(byte componentID,final Object component) {
		//if(!isRefresh) return;
		//if(!ChartComponent.isDrawFinished) return;
		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
			   Dialog.alert("here");	
			}
		});
		
		switch(componentID) {
		case COMPONENT_COMPANY_CHART:
			/*if(field != null) 
			{
				field = (Field)component;
				isChartLoaded = true;
				return;
				}*/
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					
					
					horizontalFieldManager.deleteAll();
					if(component!=null) {
						horizontalFieldManager.add((Field)component);
						field = (Field)component;
						//isChartLoaded = true;
					}
				refreshme.setLoading(false);
				graphRefreshTime = System.currentTimeMillis();
				//if(!(UiApplication.getUiApplication().getActiveScreen() instanceof CompanyDetailsSnippetsScreen))
				//	ScreenInvoker.pushScreen(cmp, true, true);
				}
			});
			break;
		}
	}

	public void componentsDataPrepared(byte componentID, final Object data) {
		if(!isRefresh) return;
		switch(componentID) {
		case BANNERS_DATA:
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					
					try {
						LOG.print("Start");
						Vector vector = (Vector)data;
						HomeJson homeJson = (HomeJson)vector.elementAt(0);
						System.out.println("homeJson.getReligareCode() : "+homeJson.getReligareCode());
						homeJsonC = homeJson;
						symbol = homeJson.getSymbol();
						//if(symbol == null)
							//symbol = Utils.NSE_SYMBOL;
						boolean flagQuote = false;
						if(homeJson.getLastTradedPrice() == null)
							flagQuote = true;
						else if(homeJson.getLastTradedPrice().equalsIgnoreCase("null"))
							flagQuote = true;
						LOG.print("homeJson.getLastTradedPrice() : >>>>> "+homeJson.getLastTradedPrice() +"\nflagQuote "+flagQuote);
						if(flagQuote)//(homeJson.getLastTradedTime().equalsIgnoreCase("null"))
						{
							restrict = true;
							//nseBanner.setNullData();// = new HomeScreenBanner(FIELD_HCENTER, "NSE",false);
							LOG.print("NSEBannser");
							if(AppConstants.NSE)
							{
								nseBanner.setStartFlag(false);
								nseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()),homeJson.getA(),homeJson.getD(),homeJson.getS());
								nseBanner.startUpdate();
							}
							else
							{
								bseBanner.setStartFlag(false);
								bseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()),homeJson.getA(),homeJson.getD(),homeJson.getS());
								bseBanner.startUpdate();
							}
							LOG.print("|=| |+| |=| |+|");
							lblValues.setValue("Day High/Low", "",
									"52 week High/Low", "",
									"Volume", "");
							marketDepthVector = null;
							REFRESH = false;
						}
						else
						{
							restrict = false;
							if(homeJson.getMarketStatus().equalsIgnoreCase("CLOSED"))
								REFRESH = false;
							lastUpdatedDateTime.setText("Last Updated : "+homeJson.getLastTradedTime());
							LOG.print("LastTradedTime");
							lastUpdatedDateTime.getManager().invalidate();
							lblValues.setValue("Day High/Low", homeJson.getHigh()+"-"+homeJson.getLow(),
									"52 week High/Low", homeJson.getFiftyTwoWeekHigh()+"-"+homeJson.getFiftyTwoWeekLow(),
									"Volume", homeJson.getVolume());
							LOG.print("lblValues");
							if(!AppConstants.NSE)
							{
								jsonBanner = homeJson;
								storeCCode = homeJson.getCompanyCode();
								LOG.print("BSEBanner");
								bseBanner.setStartFlag(false);
								bseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()),homeJson.getA(),homeJson.getD(),homeJson.getS());
								bseBanner.startUpdate();
							}
							else
							{
								jsonBanner = homeJson;
								LOG.print("NSEBannser");
								nseBanner.setStartFlag(false);
								nseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()),homeJson.getA(),homeJson.getD(),homeJson.getS());
								nseBanner.startUpdate();
							}
						//}
						//		Market depth Store Start 1 Vector = 6 String[] bOrd, bQty, bPrc, sOrd, sQty, sPrc,
							marketDepthVector = new Vector();
							marketDepthVector.addElement(Utils.getCompanyLatestTradingDetailsURL(strCompanyCode));
							marketDepthVector.addElement(homeJson.getBuyQty());
							marketDepthVector.addElement(homeJson.getBuyPrice());
							marketDepthVector.addElement(homeJson.getSellQty());
							marketDepthVector.addElement(homeJson.getSellPrice());
					/*	if(field!=null) {
							horizontalFieldManager.deleteAll();
							horizontalFieldManager.add(field);
						}*/
						//	Market depth Store End
						//AppConstants.WEBVIEW_URL="http://50.17.18.243/SK/orderEQ.php?ccode="+homeJson.getCompanyCode()+"&perchange="+homeJson.getChangePercent()+"&change="+homeJson.getChange()+"&custId="+UserInfo.getUserID()+"&ltp="+homeJson.getLastTradedPrice()+"&userAgent=bb&dpId="+UserInfo.getDpid();
						AppConstants.WEBVIEW_URL=Utils.getTradeNowURL(homeJson.getCompanyCode(), UserInfo.getUserID(), homeJson.getDisplayName2());
						LOG.print("Trading URL :"+AppConstants.WEBVIEW_URL);
					}
						//System.out.println("AppConstants.WEBVIEW_URL : "+AppConstants.WEBVIEW_URL);
					} catch(Exception ex) {
						//ScreenInvoker.showDialog("No Data To Display");
						LOG.print("Error - - - - ");
						//UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
						Debug.debug("Error is here : "+ex.toString());
					}
					refreshme.setLoading(false);
			
				
				//	if(!(UiApplication.getUiApplication().getActiveScreen() instanceof CompanyDetailsSnippetsScreen))
				//		ScreenInvoker.pushScreen(cmp, true, true);
			}
			});
			break;
		}
	}

	public void actionPerfomed(byte Command, Object sender) {
		switch(Command) {
		case ActionCommand.CMD_COMPANY_STATICS_BANNERS:
			
			
			
			Vector commandData = new Vector();
			commandData.addElement(strCompanyCode);
			commandData.addElement((ThreadedComponents)this);
			ActionInvoker.processCommand(new Action(ActionCommand.CMD_COMPANY_STATICS_BANNERS,commandData));
			break;
		case ActionCommand.CMD_COMPANY_INTRA_CHART:
			

			
			Vector commandDatax = new Vector();
			commandDatax.addElement(strCompanyCode);
			commandDatax.addElement((ThreadedComponents)this);
			//ActionInvoker.processCommand(new Action(ActionCommand.CMD_COMPANY_STATICS_BANNERS,commandData));
			
			ActionInvoker.processCommand(new Action( ActionCommand.CMD_COMPANY_INTRA_CHART,commandDatax));
			
			break;
		default:
			ActionInvoker.processCommand(new Action(Command,sender));
			break;
		}
	}

	public Vector getComponentData() {
		Vector vector = new Vector();
		vector.addElement(strTitle);
		vector.addElement(strCompanyCode);
		return vector;
	}
	public void refreshFields() {
		if(restrict) return;
		if(homeJsonC!=null)
		{
if(homeJsonC.getMarketStatus().equalsIgnoreCase("CLOSED"))
{
	return;
}
}
		if(REFRESH)
		{
		if(blockAutoRefresh == 1 ){blockAutoRefresh--; return;}
		else if(blockAutoRefresh == 2) blockAutoRefresh--;
		if(isRefresh ){
			refreshme.setLoading(true);
			boolean flag ;
			if(!AppConstants.NSE)
				flag=bseBanner.isBlockLoaded();
			else
				flag=nseBanner.isBlockLoaded();
			if(flag) {
				Debug.debug("Refreshing Banners And Charts");
				//UiApplication.getUiApplication().invokeLater(new Runnable() {
				//	public void run() {
						if(refreshPressed)
						{
							if(!AppConstants.NSE)
								;//bseBanner.reset();
							else if(symbol.equalsIgnoreCase("null"))
							{
								LOG.print("nseBanner symbol");
								nseBanner.setNullData();
								return;
							}
							else
							{
								LOG.print("nseBanner");
								//nseBanner.reset();
							}
							refreshPressed = false;
						}
						if(!symbol.equalsIgnoreCase("null"))
						{
							actionPerfomed(ActionCommand.CMD_COMPANY_STATICS_BANNERS, null);
						}
						//actionPerfomed(ActionCommand.CMD_COMPANY_INTRA_CHART, null);
				//	}
				//});
			}
			else
			{
				refreshPressed = false;
				refreshme.setLoading(false);
			}
			LOG.print("ChartComponent.isDrawFinished............."+ChartComponent.isDrawFinished);
			if(isChartLoaded /*&& (graphRefreshTime+10000)<System.currentTimeMillis() && graphRefreshTime!=0*/) {
				//ChartComponent.isDrawFinished = false;
				isChartLoaded = false;
				
				LOG.print("Refreshing Banners And Chartsdddddddddd");
				//UiApplication.getUiApplication().invokeLater(new Runnable() {
				//	public void run() {
						actionPerfomed(ActionCommand.CMD_COMPANY_INTRA_CHART, null);
				//	}
				//});
			}
		}
	}
	}
	private class TopTabber extends Manager {

		LabelField lblBSETab = null;
		LabelField lblNSETab = null;
		private byte padding = 4;
		int selectedIndex = 0;

		public TopTabber(final ActionListener actionListner,int selectedIndex,final ThreadedComponents threadedComponents) {
			super(FOCUSABLE);

			this.selectedIndex = selectedIndex;

			lblBSETab = new LabelField("BSE", FOCUSABLE) {

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getFont().getAdvance(getText())+2, getFont().getHeight());
				}

				public int getPreferredHeight() {
					return getFont().getHeight()+2;
				}

				public int getPreferredWidth() {
					return getFont().getAdvance(getText())+2;
				}

				protected void paint(Graphics graphics) {
					if(isFocus()) {
						graphics.setColor(Color.ORANGE);
						//graphics.drawRect(0, 0, getWidth(), getHeight());
						graphics.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
					}
					graphics.setColor(0xeeeeee);
					graphics.drawText(getText(), 0, 0);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
					//Dont do anything and let the system
				}

				protected void onFocus(int direction) {
					super.onFocus(direction);
					invalidate();
				}

				protected void onUnfocus() {
					super.onUnfocus();
					invalidate();
				}

				protected boolean navigationClick(int status, int time) {
					menuBlock = true;
					AppConstants.NSE = false;
					refreshPage(0);
					return super.navigationClick(status, time);
				}
				protected boolean touchEvent (TouchEvent message)
				{
					menuBlock = true;
					AppConstants.NSE = false;
					refreshPage(0);
				    return super.touchEvent(message);
				}
								
			};
			if(AppConstants.screenHeight>=480)
				lblBSETab.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			else
				lblBSETab.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));

			lblNSETab = new LabelField("NSE", FOCUSABLE) {

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getFont().getAdvance(getText())+2, getFont().getHeight());
				}

				protected void paint(Graphics graphics) {
					if(isFocus()) {
						graphics.setColor(Color.ORANGE);
						//graphics.drawRect(0, 0, getWidth(), getHeight());
						graphics.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
					}
					graphics.setColor(0xeeeeee);
					graphics.drawText(getText(), 0, 0);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
					//Dont do anything and let the system
				}

				protected void onFocus(int direction) {
					super.onFocus(direction);
					invalidate();
				}

				protected void onUnfocus() {
					super.onUnfocus();
					invalidate();
				}

				protected boolean navigationClick(int status, int time) {
					menuBlock = true;
					AppConstants.NSE = true;
					refreshPage(1);
					return super.navigationClick(status, time);
				}
				protected boolean touchEvent (TouchEvent message)
				{
					menuBlock = true;
					AppConstants.NSE = true;
					refreshPage(1);
				    return super.touchEvent(message);
				}
			};
			if(AppConstants.screenHeight>=480)
				lblNSETab.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			else
				lblNSETab.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));

			add(lblBSETab);
			add(lblNSETab);

		}

		protected void sublayout(int width, int height) {
			layoutChild(getField(0), width, height);
			layoutChild(getField(1), width, height);

			setPositionChild(getField(0), AppConstants.screenWidth/4-getField(0).getPreferredWidth()/2, padding/2);
			setPositionChild(getField(1), AppConstants.screenWidth/2 + AppConstants.screenWidth/4-getField(1).getPreferredWidth()/2, padding/2);

			setExtent(AppConstants.screenWidth, getField(0).getPreferredHeight()+padding*2);
		}

		protected void drawFocus(Graphics graphics, boolean on) {

		}

		protected void paintBackground(Graphics graphics) {

			graphics.setColor(Color.GRAY);
			graphics.fillRect(0, 0, getWidth(), getHeight());

			graphics.setColor(Color.BLACK);
			if(selectedIndex==0)
				graphics.fillRect(AppConstants.screenWidth/2, 0, AppConstants.screenWidth/2, getField(0).getPreferredHeight()+padding);
			else
				graphics.fillRect(0, 0, AppConstants.screenWidth/2, getField(0).getPreferredHeight()+padding);
		}

	}
	public class CustomPopUpScreen extends PopupScreen {
		Vector vector;
		String ccode;
		String exchange;
		protected void sublayout(int arg0, int arg1) {
			super.sublayout(arg0, arg1);
		}
		public CustomPopUpScreen(String ccode_) {
			super(new VerticalFieldManager(CustomPopUpScreen.VERTICAL_SCROLL | CustomPopUpScreen.VERTICAL_SCROLLBAR ));
			ccode = ccode_;
			LOG.print("Company Details Snippet "+ccode_);
			if(AppConstants.NSE)
				exchange = "NSE";
			else
				exchange = "BSE";
			sendHTTPRequest(1);
		}

		public void sendHTTPRequest(final int i) {
			thread = new Thread(new Runnable() {
				public void run() {
					try {
						ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
						//doPaint();
						String strAuthURL = Utils.getWatchListURL(i,UserInfo.getUserID());
						LOG.print("Company Add to WatchList URL : "+strAuthURL);
						String strResponse = HttpProcess.getHttpsMD5Connection(strAuthURL);
						LOG.print("Company Add to WatchList Response : "+strResponse);
						final Vector result = parseAndSaveRegistrationInfo(i, strResponse);
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								if(!Utils.sessionAlive)
									return;
								if(result.size()==0)
								{
									ScreenInvoker.removeRemovableScreen();
									deleteAll();
									Dialog.alert("No records found");
								}
								else
								{
									if(i==1)
									{
										vector = result;
										ScreenInvoker.removeRemovableScreen();
										if(vector.size()==2)
										{
											isRefresh = false;
											Action action = new Action(ActionCommand.CMD_WATCHLIST_SCREEN, null);
											ActionInvoker.processCommand(action);
										}
										else{
											for(int i=0;i<vector.size();i++)
											{
												if(vector.elementAt(i) instanceof WatchListMainBean)
												{
													final WatchListMainBean bean = (WatchListMainBean)vector.elementAt(i);
												//final String text = URLEncode.replace((String)vector.elementAt(i)); 
												//final String exch = text.indexOf('.')<0?"NONE":text.substring(text.length()-3, text.length());
												//LOG.print("Added to Exchange : "+exch);
												//if(!exch.equalsIgnoreCase("NONE"))
													add(new WatchListSecondField(FOCUSABLE, bean.getDisplayName(), (AppConstants.screenWidth/3)*2)
													{
														protected boolean navigationClick(int status, int time)
														{
															if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
															sendHTTPRequest(bean.copy(),ccode);}
															return super.navigationClick(status, time);
														}

														protected boolean touchEvent(TouchEvent message)
														{
															if(message.getEvent() == TouchEvent.CLICK) {
																if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
																sendHTTPRequest(bean.copy(),ccode);
															}}
															return super.touchEvent(message);
														}
													});
											}
											}
										}
									}
								}

							}
						});
					} catch (Exception ex) {

					}
				}
			});
			thread.start();
		}

		public Vector parseAndSaveRegistrationInfo(int i,String strResponse)
		{
			Vector v = new Vector();
			if(!Utils.sessionAlive) return v;
			if(i==1){
				Json js = new Json(strResponse);
				String user="";
				for(int x=0;x<js.getdata.size();x++)
				{
					Hashtable ht = (Hashtable) js.getdata.elementAt(x);
					user = (String)ht.get("Type");
					if(user.equalsIgnoreCase("USER"))
					{
						WatchListMainBean bean = new WatchListMainBean();
						bean.setDisplayName((String)ht.get("DisplayName"));
						bean.setTemplateName((String)ht.get("Template"));
						bean.setType((String)ht.get("Type"));
						bean.setExchange((String)ht.get("Exchange"));
						v.addElement(bean.copy());
					}
				}
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

		public void sendHTTPRequest(final WatchListMainBean bean, final String code) {
			if(code.length()==0) return;
			if(code.indexOf(' ')>-1) return;
			thread = new Thread(new Runnable() {
				public void run() {
					try {
						ScreenInvoker.removeRemovableScreen();
						ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
						//doPaint();
						//final String exch = wlName.indexOf('.')<0?(wlName.equalsIgnoreCase("NIFTY")?"NSE":"BSE"):wlName.substring(wlName.length()-3, wlName.length());
						String strAuthURL = Utils.getWatchListURLAddScrips(UserInfo.getUserID(),bean.getTemplateName(),AppConstants.NSE?"NSE":"BSE",code);
						LOG.print(strAuthURL);
						String strResponse = HttpProcess.getHttpsMD5Connection(strAuthURL);
						final int result = parseAndSaveRegistrationInfo(strResponse);
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								if(result != 3)
								ScreenInvoker.removeRemovableScreen();
								if(result == 1)
								{
									Vector v = new Vector();
									v.addElement(code);
									Utils.WATCHLIST = true;
									Vector wlScreen = new Vector();
									wlScreen.addElement(bean.getTemplateName());
									Action action = new Action(ActionCommand.CMD_WL_FILL_SCREEN, wlScreen);
									ActionInvoker.processCommand(action);

								}
								else if(result == 2)
									Dialog.alert("Add to WatchList Failed!");

								LOG.print("Loading Removed");

							}
						});

					} catch (Exception ex) {
					}
				}
			});
			thread.start();
		}

		public int parseAndSaveRegistrationInfo(String strResponse)
		{
			if(strResponse == null)
				return 3;
			Json js = new Json(strResponse);
			String user="";
			Hashtable ht = (Hashtable) js.getdata.elementAt(0);
			user = (String)ht.get("MSG");
			if(user.equalsIgnoreCase("SUCCESS"))
				return 1;
			return 2;

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
						isRefresh = true;
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
	protected boolean keyDown( int keyCode, int time ) {
		int key = Keypad.key( keyCode );
		if(key == Keypad.KEY_END)
		{
			LOG.print("KEY_END EXIT from app");
			System.exit(0);
		}
		return super.keyDown(keyCode, time);
	}
	public HomeScreenBanner getBSEbanner()
	{
		return bseBanner;
	}
	public HomeScreenBanner getNSEbanner()
	{
		return nseBanner;
	}
}

