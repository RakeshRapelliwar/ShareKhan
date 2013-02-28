package com.snapwork.view;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
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
import com.snapwork.areacharts.ChartProperties;
import com.snapwork.beans.FoScrips;
import com.snapwork.beans.HomeJson;
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
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.parsers.HomeJsonParser;
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
import com.snapwork.view.trade.TradeNowMainScreen;

public class NIFTYCompanyDetailsSnippetsScreen extends MainScreen implements ThreadedComponents,ActionListener,ReturnDataWithId {

	public static final byte BANNERS_DATA = 1;
	Thread controllerThread = null;
	Thread updaterThread = null;
	BottomMenu bottomMenu = null;
	private String strTitle = null;
	private String strCompanyCode = null;
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
	//addMenuItem(SeparatorField.LINE_HORIZONTAL);

	private String src=null;
	private int index;
	private byte actionCommand;
	//Fields for the screen
	HorizontalFieldManager horizontalFieldManager = null;
	VerticalFieldManager verticalFieldManager = null;
	public HomeScreenBanner nseBanner = null;
	public HomeJson jsonBanner = null;
	private Vector marketDepthVector;

	LabelField lastUpdatedDateTime = null;
	CustomInfoLabelField lblValues = null;
	boolean isCompanyInTheWatchList = false;
	private String symbol = "";
	private boolean refreshPressed = true;
	private RefreshButton refreshme;
	private ReturnDataWithId rparse = this;
	private int REFRESH_ID = 78;
	private Field field;
	public NIFTYCompanyDetailsSnippetsScreen(int index, String strTitle,final String source,final String companyCode) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		this.strTitle = strTitle;
		isRefresh = true;
		this.strCompanyCode = companyCode;
		if(index==0)
			AppConstants.source = companyCode;
		else
			symbol = this.src = source;
		this.src = source;
		this.index=index;
		createUI(strTitle,source,companyCode);
	}

	public void createUI(final String strTitle,final String source,final String companyCode) {
		//System.out.println("NIFTY Company Details Snippets");
		//AppConstants.COMPANY_NAME = strTitle;
		//setTitle(new TitleBar(strTitle));
		refreshme = new RefreshButton()
		{
			protected boolean navigationClick(int arg0, int arg1) {
				if(!getLoading())
				{
					setLoading(true);
					new HomeJsonParser(Utils.getCompanyLatestTradingDetailsURL(strCompanyCode), rparse, REFRESH_ID);
				}
				return super.navigationClick(arg0, arg1);
				}
		};
		//Sets the title
		setTitle(new TitleBarRefresh(strTitle, refreshme));
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
				maxHeight = FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT)
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

		nseBanner = new HomeScreenBanner(FIELD_HCENTER, "NSEFO",1);
		lastUpdatedDateTime = new LabelField("", NON_FOCUSABLE | FIELD_RIGHT | FIELD_VCENTER){
			protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}
		};
		lastUpdatedDateTime.setFont(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT));
		String strButtonText = "+ Add to Watchlist";
		String strButtonTex = ("Refresh" );

		/*refreshButton = new CustomLinkButtonUnderLine(strButtonTex, FOCUSABLE | FIELD_LEFT | FIELD_VCENTER, 0xeeeeee, lastUpdatedDateTime.getFont()) {

			protected boolean navigationClick(int status, int time) {
				if(AppConstants.NSE && !restrict || restrict && !AppConstants.NSE || !restrict && !AppConstants.NSE)
				{
					refreshPressed = true;
					refreshPage();
				}
				return super.navigationClick(status, time);
			}

		};*/
		topManager.add(new NullField());
		topManager.add(lastUpdatedDateTime);
		verticalFieldManager.add(topManager);
		verticalFieldManager.add(nseBanner);
		verticalFieldManager.add(lblValues);


		horizontalFieldManager.add(new LoadingComponent(AppConstants.loadingMessage,AppConstants.screenWidth,ChartProperties.getDefaultChartProperties().getChartHeight()+ChartProperties.getDefaultChartProperties().getChartxAxisHeight()*2));
//Scrollable VerticalfieldManager
		verticalFieldManager.add(Utils.separator(5, 0x000000));
		verticalFieldManager.add(horizontalFieldManager);
		//mainManager.add(new TopTabber(this,this.index,(ThreadedComponents)this));
		mainManager.add(verticalFieldManager);

		_addRemoveItem = new MenuItem(strButtonText, 100, 100) //Add to / Remove from Watchlist
		{
			public void run() 
			{
				if(!Utils.sessionAlive)
				{
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
				}
				else
				{
					Utils.WATCHLIST_QUOTE = companyCode;
					Utils.WATCHLIST = true;
					Utils.setWatchListedCompanyRecords(null);
					Action action = new Action(ActionCommand.CMD_WL_SCREEN, null);
					ActionInvoker.processCommand(action);
				}
			}
		};
		_FNOItem = new MenuItem("F&O", 100, 100) 
		{
			public void run() 
			{
				if(!symbol.equalsIgnoreCase("null"))
				{
					if(!Utils.sessionAlive)
					{
						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
					}
					else
					{
						AppConstants.OPTIONS_FLAG = false;
						AppConstants.source = symbol; //storeCCode;
						actionPerfomed(ActionCommand.CMD_FUTURE_SCREEN, null);
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
					if(marketDepthVector!=null)
					{
						if(!Utils.sessionAlive)
						{
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
						}
						else
						{ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
						if(!AppConstants.NSE)
							new TradeNowMainScreen("tradenow",AppConstants.WEBVIEW_URL,jsonBanner);
						else
							new TradeNowMainScreen("tradenow",AppConstants.WEBVIEW_URL,jsonBanner);
						}
					}
				}


			}
		};
		_marketDepth = new MenuItem("Market Depth", 100, 100)
		{
			public void run() 
			{
				//if(!restrict)
				{
					if(marketDepthVector!=null)
					{
						if(marketDepthVector.size()==4)
						{
							actionPerfomed(ActionCommand.CMD_MARKET_DEPTH_SCREEN, marketDepthVector);
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
		_searchItem = new MenuItem("Search", 100, 100)
		{
			public void run() 
			{
				actionPerfomed(ActionCommand.CMD_SEARCH_SCREEN, null);
			}
		};


		add(mainManager);
		// Configure BottomMenu and set commands
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomMenuCommands);

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
            	return false;

            return false;
	}

	public boolean keyUp( int keyCode, int time )
	{
		return super.keyUp(keyCode, time);
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
		ActionInvoker.processCommand(new Action(ActionCommand.CMD_NIFTY_COMPANY_DETAILS_SCREEN,vectorCommandData));
		//	actionListner.actionPerfomed(ActionCommand.CMD_COMPANY_DETAILS_SCREEN, vectorCommandData);
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
	public void componentsPrepared(byte componentID,final Object component) {
		
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
						symbol = homeJson.getSymbol();
						if(AppConstants.NSE && homeJson.getLastTradedPrice().equalsIgnoreCase("null"))//(homeJson.getLastTradedTime().equalsIgnoreCase("null"))
						{
							restrict = true;
							//nseBanner.setNullData();// = new HomeScreenBanner(FIELD_HCENTER, "NSE",false);
							LOG.print("NSEBannser");
							nseBanner.setStartFlag(false);
							nseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()),homeJson.getA(),homeJson.getD(),homeJson.getS());
							nseBanner.startUpdate();
							/*lblValues.setValue("Day High/Low", "",
									"52 week High/Low", "",
									"Volume", "");*/
							lblValues.setValue("Day High/Low", "",
									"Open Interest", "",
									"Volume", "");
						}
						else
						{

							lastUpdatedDateTime.setText("Last Updated : "+homeJson.getLastTradedTime());
							LOG.print("LastTradedTime");
							lastUpdatedDateTime.getManager().invalidate();
							/*lblValues.setValue("Day High/Low", homeJson.getHigh()+"-"+homeJson.getLow(),
									"52 week High/Low", homeJson.getFiftyTwoWeekHigh()+"-"+homeJson.getFiftyTwoWeekLow(),
									"Volume", homeJson.getVolume());*/
							
							lblValues.setValue("Day High/Low", homeJson.getHigh()+"-"+homeJson.getLow(),
									"Open Interest", homeJson.getOpenInterest(),
									"Volume", homeJson.getVolume());
							LOG.print("lblValues");
							restrict = false;
								jsonBanner = homeJson;
								LOG.print("NSEBannser");
								nseBanner.setStartFlag(false);
								nseBanner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()),homeJson.getA(),homeJson.getD(),homeJson.getS());
								nseBanner.startUpdate();
							
						}
						//		Market depth Store Start 1 Vector = 6 String[] bOrd, bQty, bPrc, sOrd, sQty, sPrc,
						marketDepthVector = new Vector();
						marketDepthVector.addElement(homeJson.getBuyQty());
						marketDepthVector.addElement(homeJson.getBuyPrice());
						marketDepthVector.addElement(homeJson.getSellQty());
						marketDepthVector.addElement(homeJson.getSellPrice());
						//UiApplication.getUiApplication().invokeLater(new Runnable() {
							//public void run() {
								horizontalFieldManager.deleteAll();
								horizontalFieldManager.add(getMarketDepthManager());
							//}
						//});
						//	Market depth Store End
						//AppConstants.WEBVIEW_URL="http://50.17.18.243/SK/orderEQ.php?ccode="+homeJson.getCompanyCode()+"&perchange="+homeJson.getChangePercent()+"&change="+homeJson.getChange()+"&custId="+UserInfo.getUserID()+"&ltp="+homeJson.getLastTradedPrice()+"&userAgent=bb&dpId="+UserInfo.getDpid();
						//AppConstants.WEBVIEW_URL=Utils.getTradeNowURL(storeCCode, homeJson.getChangePercent(), homeJson.getChange(), UserInfo.getUserID(), homeJson.getLastTradedPrice(), UserInfo.getDpid());
						//LOG.print(AppConstants.WEBVIEW_URL);

						//System.out.println("AppConstants.WEBVIEW_URL : "+AppConstants.WEBVIEW_URL);
					} catch(Exception ex) {
						//ScreenInvoker.showDialog("No Data To Display");
						LOG.print("Error - - - - ");
						UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
						Debug.debug("Error is here : "+ex.toString());
					}
				}
			});
			refreshme.setLoading(false);
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
		if(isRefresh){
			if(nseBanner.isBlockLoaded()==true) {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						if(refreshPressed)
						{
							if(symbol.equalsIgnoreCase("null"))
							{
								LOG.print("nseBanner symbol");
								nseBanner.setNullData();
								return;
							}
							else
							{
								LOG.print("nseBanner");
								nseBanner.reset();
							}
							refreshPressed = false;
						}
						if(!symbol.equalsIgnoreCase("null"))
						{
							actionPerfomed(ActionCommand.CMD_COMPANY_STATICS_BANNERS, null);
						}
						//actionPerfomed(ActionCommand.CMD_COMPANY_INTRA_CHART, null);
					}
				});
			}
			else
			{
				refreshPressed = false;
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
					AppConstants.NSE = false;
					refreshPage();
					return super.navigationClick(status, time);
				}
			};
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
					AppConstants.NSE = true;
					refreshPage();
					return super.navigationClick(status, time);
				}
			};
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
	
	
	public HomeScreenBanner getNSEbanner()
	{
		return nseBanner;
	}
	
	public VerticalFieldManager getMarketDepthManager()
	{
		VerticalFieldManager vFManager =  new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			/*public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.CADETBLUE);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}            */
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(maxWidth,AppConstants.screenHeight<241?70:(AppConstants.screenHeight - (TitleBar.getItemHeight() + nseBanner.getPreferredHeight() + lblValues.getPreferredHeight() + 40)));
				setExtent(maxWidth, AppConstants.screenHeight<241?70:(AppConstants.screenHeight - (TitleBar.getItemHeight() + nseBanner.getPreferredHeight() + lblValues.getPreferredHeight() + 40)));
				//(AppConstants.screenHeight - (TitleBar.getItemHeight() + nseBanner.getPreferredHeight() + lblValues.getPreferredHeight() + 30))
			}
		};
		/*vFManager.add(new LabelField("LabelFirst", FOCUSABLE));
		vFManager.add(new LabelField("LabelSecond", FOCUSABLE));
		vFManager.add(new LabelField("LabelThird", FOCUSABLE));
		vFManager.add(new LabelField("LabelLast", FOCUSABLE));
		vFManager.add(new LabelField("LabelFirstee", FOCUSABLE));
		vFManager.add(new LabelField("LabelSecond22", FOCUSABLE));
		vFManager.add(new LabelField("LabelThird33", FOCUSABLE));
		vFManager.add(new LabelField("LabelLast33", FOCUSABLE));
		vFManager.add(new LabelField("LabelFirst22", FOCUSABLE));
		vFManager.add(new LabelField("LabelSeco33nd", FOCUSABLE));
		vFManager.add(new LabelField("LabelTh22ird", FOCUSABLE));
		vFManager.add(new LabelField("LabelLa33st", FOCUSABLE));*/
		String str1[],str2[],str3[],str4[];
		str1 = (String[])marketDepthVector.elementAt(0);
		str2 = (String[])marketDepthVector.elementAt(1);
		str3 = (String[])marketDepthVector.elementAt(2);
		str4 = (String[])marketDepthVector.elementAt(3);
		int len = 0;
		String str="";
		for(int i=0;i<str1.length;i++)
		{
			if(len < str1[i].length())
			{
				len = str1[i].length();
				str = str1[i];
			}
			if(len < str4[i].length())
			{
				len = str4[i].length();
				str = str4[i];
			}
		}
		int maxWth  = FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getAdvance(str)+6;
		if(maxWth < FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance("BestBuy")+6)
			maxWth = FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance("BestBuy")+6;
		//verticalFieldManager.add(getLabelField(NON_FOCUSABLE, "Order","Qty","Price",FontLoader.getFont(AppConstants.BIG_BOLD_FONT),0x769698,0xffffff,maxWth,true)); //0x769698 light //0x638284 dark
		vFManager.add(getHeaderLabelField(FOCUSABLE, "BestBuy Qty", "BestBuy Price", maxWth, FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT)));
		
		boolean fl = false;
		for(int x=0;x<5;x++)
		{
			fl =!fl;
			vFManager.add(getLabelField(FOCUSABLE, str1[x],str2[x],FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),fl?0x4a4942:0x73756b,0xffffff,-1,false));
		}
		vFManager.add(getHeaderLabelField(FOCUSABLE, "BestSell Qty", "BestSell Price", maxWth, FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT)));
		fl = false;
		for(int x=0;x<5;x++)
		{
			fl = !fl;
			vFManager.add(getLabelField(FOCUSABLE, str3[x],str4[x],FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),fl?0x4a4942:0x73756b,0xffffff,-1,false));
		}
		return vFManager;
		
	}
	public LabelField getLabelField(long style, final String strTitle, final String text2,final Font fntFont,final int bgColor,final int fgColor,final int maxWth, final boolean flag)
	{
		return new LabelField(strTitle, style) 
		{
			protected void layout(int width, int height) 
			{
				super.layout(width, height);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}

			public int getPreferredHeight() 
			{
				return (fntFont.getHeight()+10);
			}

			public int getPreferredWidth() 
			{
				return AppConstants.screenWidth;
			}
protected void paintBackground(Graphics arg0) {
				
			}
			protected void paint(Graphics graphics) 
			{
				graphics.setColor(bgColor);
				graphics.fillRect(10, 2, getWidth()-20, getHeight()-4);
				graphics.setColor(fgColor);
				graphics.setFont(fntFont);
				String t1 = strTitle;
				if(strTitle.indexOf('.')>-1)
				{
					if(strTitle.indexOf('.')==strTitle.length()-2)
					{
						t1 = strTitle + "0";
					}
				}
				if(!t1.equalsIgnoreCase("null")) {
					graphics.drawText(t1, maxWth==-1?20:(maxWth - fntFont.getAdvance(t1)), 5);
				}
				String t3 = text2;
				if(!flag)
				if(text2.indexOf('.')>-1)
				{
					if(text2.indexOf('.')==text2.length()-2)
					{
						t3 = text2 + "0";
					}
				}
				else 
				{
					if(!t3.equalsIgnoreCase("null"))
						t3 = t3 + ".00";
				}
				if(!t3.equalsIgnoreCase("null"))
				graphics.drawText(t3, AppConstants.screenWidth - fntFont.getAdvance(t3) - 21 , 5);
			}

			protected void drawFocus(Graphics graphics, boolean on) 
			{

			}
		};
	}
	
	
	
	public LabelField getHeaderLabelField(long style, final String strTitle, final String strTitle2, final int maxw, final Font fntFont)
	{
		return new LabelField(strTitle, style) 
		{
			protected void layout(int width, int height) 
			{
				super.layout(getPreferredWidth(), height);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
			public void setFont(Font arg0) {
				super.setFont(fntFont);
			}
			public int getPreferredHeight() 
			{
				return (fntFont.getHeight()+6);
			}

			public int getPreferredWidth() 
			{
				return AppConstants.screenWidth;
			}
			protected void paintBackground(Graphics arg0) {
				
			}
			protected void paint(Graphics graphics) 
			{
				//if(!isFocus())
				//{
					//graphics.setColor(0x1059c6); //0x5f7b90
					graphics.setColor(0x639084);
					graphics.fillRect(10, 0, getPreferredWidth()-20, getPreferredHeight());
				//}
				graphics.setColor(0xeeeeee);
				graphics.setFont(fntFont);
				graphics.drawText(strTitle, 20, 3);
				graphics.drawText(strTitle2, getPreferredWidth() - graphics.getFont().getAdvance(strTitle2) - 21, 3);
			}
			protected void drawFocus(Graphics graphics, boolean on) 
			{

			}

		};
	}

	public void setData(Vector vector, int id) {
		if( id == REFRESH_ID)
		{
			HomeJson homeJson = (HomeJson)vector.elementAt(0);
			Vector vec = new Vector();
			vec.addElement(homeJson);
			componentsDataPrepared(BANNERS_DATA, vec);
		}
	}
}

