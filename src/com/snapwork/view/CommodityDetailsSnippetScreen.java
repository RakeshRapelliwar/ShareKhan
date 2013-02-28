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
import com.snapwork.areacharts.ChartEngine;
import com.snapwork.areacharts.ChartProperties;
import com.snapwork.beans.Expiry;
import com.snapwork.beans.FoScrips;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.WatchListMainBean;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomFNOLabelField;
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
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.ChartItemParser;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.Json;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.Debug;
import com.snapwork.util.FNOTradeConfiguration;
import com.snapwork.util.FontLoader;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.URLEncode;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;
import com.snapwork.view.CompanyFNODetailsSnippetsScreen.CustomPopUpScreen;
import com.snapwork.view.trade.FNOTradeScreen;
import com.snapwork.view.trade.TradeNowMainScreen;
import com.snapwork.view.trade.TradeNowMainScreenCM;
public class CommodityDetailsSnippetScreen extends MainScreen implements
ActionListener, AutoRefreshableScreen, ReturnString, ReturnDataWithId {
	public static final byte COMPONENT_COMPANY_CHART = 0;
	public static final byte BANNERS_DATA = 1;
	Thread controllerThread = null;
	Thread updaterThread = null;
	BottomMenu bottomMenu = null;
	private String strTitle = null;
	private String strCompanyCode = null;
	private boolean isChartLoaded = false;
	MenuItem _addRemoveItem = null;
	MenuItem _addEquityItem = null;
	MenuItem _tradeNowItem = null;
	MenuItem _homeItem = null;
	MenuItem _searchItem = null;
	MenuItem _marketDepth = null;
	private static String storeCCode;
	private CommodityDetailsSnippetScreen object = this;
	private Field field;
	private Thread thread;
	// addMenuItem(SeparatorField.LINE_HORIZONTAL);

	private String src = null;
	private int index;
	private byte actionCommand;
	// Fields for the screen
	HorizontalFieldManager horizontalFieldManager = null;
	VerticalFieldManager verticalFieldManager = null;
	public HomeScreenBanner bseBanner = null;
	public HomeJson jsonBanner = null;
	public static Vector marketDepthVector;
	private boolean menuBlock;

	LabelField lastUpdatedDateTime = null;
	CustomInfoLabelField lblValues = null;
	boolean isCompanyInTheWatchList = false;
	private String symbol = "";
	private ActionListener actionListener = this;
	private RefreshButton refreshme;
	private boolean refreshClick;
	private boolean blockAutoRefresh;
	private CommodityDetailsSnippetScreen cmp = this;
	private long timer;
	private HomeJson homeJson = null;
	private boolean addDirect;
	private boolean future;
	private boolean REFRESH = true;
	private boolean onceRefresh = true;
private String commodityChartUrl=null;

private String exchange=null;

	public CommodityDetailsSnippetScreen(HomeJson homeJson, int dateTextID,
			boolean addDirect, String commodityChartUrl, String exchange, String symbol2) {
		getMainManager().setBackground(
				BackgroundFactory.createSolidBackground(Color.BLACK));
		this.addDirect = addDirect;
		this.commodityChartUrl=commodityChartUrl;
		this.exchange=exchange;
		this.symbol=symbol2;
		/*
		 * if(dateTextID!=-1){ String title = homeJson.getCompanyCode();
		 * LOG.print(homeJson.getCompanyCode()); String datetext =
		 * title.substring(title.indexOf("_")+1,title.indexOf("_")+11); for(int
		 * i=0;i<Expiry.size();i++) {
		 * if(datetext.equalsIgnoreCase(Expiry.getValue(i))) { dateTextID=i; } }
		 * title =
		 * /*title.substring(0,title.indexOf("_"))*homeJson.getDisplayName1
		 * ()+" "
		 * +Expiry.getValue(dateTextID)+title.substring(title.indexOf("-")+
		 * 3,title.length()); title = title.replace('_', ' '); this.strTitle =
		 * title; } else this.strTitle = homeJson.getDisplayName1() +
		 * Expiry.getValue(i);
		 */
		String datetext = "";
		String title = homeJson.getCompanyCode();
		if (title.indexOf("CE") > -1 || title.indexOf("PE") > -1)
			future = false;
		else
			future = true;
		if (title.indexOf("_") + 11 > title.length())
			datetext = title.substring(title.indexOf("_") + 1, title.length());
		else
			datetext = title.substring(title.indexOf("_") + 1,
					title.indexOf("_") + 11);
		for (int i = 0; i < Expiry.size(); i++) {
			if (datetext.equalsIgnoreCase(Expiry.getValue(i))) {
				dateTextID = i;
			}
		}
		// if(title.indexOf("_")+11>title.length())
		// title =
		// title.substring(0,title.indexOf("_")+1)+Expiry.getTextWithYear(dateTextID);
		// else
		// title =
		// title.substring(0,title.indexOf("_")+1)+Expiry.getTextWithYear(dateTextID)+title.substring(title.indexOf("_")+11,title.length());
		if (homeJson.getDisplayName2().equalsIgnoreCase("BSE")
				|| homeJson.getDisplayName2().equalsIgnoreCase("NSE")) {
//			Dialog.alert("here");
			title = homeJson.getDisplayName1();
		} else{
			
//			Dialog.alert("here   1");
			title = homeJson.getDisplayName1() + " "
					+ homeJson.getDisplayName2();
		}
		this.strTitle = title.replace('_', ' ');
		// this.strTitle = homeJson.getCompanyCode().replace('_', ' ');
		timer = System.currentTimeMillis();
		this.homeJson = homeJson;
		blockAutoRefresh = true;
		createUI(dateTextID);
	}

	public void createUI(final int dateTextID) {
		refreshme = new RefreshButton() {
			protected boolean navigationClick(int arg0, int arg1) {
				menuBlock = true;
				AutoScreenRefresherThread.skipme = true;
				refreshClick = true;
				refreshFields();
				return super.navigationClick(arg0, arg1);
			}
		};
		setTitle(new TitleBarRefresh(strTitle, refreshme));
		VerticalFieldManager mainManager = new VerticalFieldManager(
				Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}

			protected void sublayout(int maxWidth, int maxHeight) {
				super.sublayout(AppConstants.screenWidth,
						AppConstants.screenHeight);
				setExtent(AppConstants.screenWidth, AppConstants.screenHeight
						- BottomMenu.getItemHeight() - TitleBar.getItemHeight());
			}
		};

		horizontalFieldManager = new HorizontalFieldManager(FIELD_HCENTER
				| FIELD_HCENTER | FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, AppConstants.screenWidth,
						AppConstants.screenHeight);
			}
		};

		Manager topManager = new Manager(USE_ALL_WIDTH | USE_ALL_HEIGHT
				| FIELD_HCENTER | FIELD_VCENTER) {
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
		// Create Vertical Field Manager
		verticalFieldManager = new VerticalFieldManager(USE_ALL_WIDTH
				| FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, AppConstants.screenWidth,
						AppConstants.screenHeight);
			}
		};

		if (AppConstants.screenWidth == 480 && AppConstants.screenHeight == 640
				|| AppConstants.screenHeight == 800
				&& AppConstants.screenWidth == 480)
			lblValues = new CustomInfoLabelField(
					FontLoader
							.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT_FNO));
		else
			lblValues = new CustomInfoLabelField(
					FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));

		// bseBanner = new
		// CustomFNOLabelField(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),Expiry.getText(dateTextID),homeJson.getCompanyCode(),true,homeJson,Expiry.getValue(dateTextID),
		// actionListener);
		bseBanner = new HomeScreenBanner(FIELD_HCENTER, exchange, 0);
		// bseBanner.setData(homeJson.getLastTradedPrice(),
		// homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") ==
		// -1 ? "+"+homeJson.getPercentageDiff() :
		// homeJson.getPercentageDiff()),homeJson.getOpenInterest(),
		// homeJson.getVolume(), homeJson);
		// bseBanner.setShowStockScreen(true);
		bseBanner.setStartFlag(false);
		bseBanner.setData(
				homeJson.getLastTradedPrice(),
				homeJson.getChange(),
				(homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"
						+ homeJson.getPercentageDiff() : homeJson
						.getPercentageDiff()), homeJson.getA(),
				homeJson.getD(), homeJson.getS());
		bseBanner.startUpdate();
		lastUpdatedDateTime = new LabelField("", NON_FOCUSABLE | FIELD_RIGHT
				| FIELD_VCENTER) {
			protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}
		};
		lastUpdatedDateTime.setFont(FontLoader
				.getFont(AppConstants.SMALL_PLAIN_FONT));
		String strButtonText = "+ Add to Watchlist";
		String strButtonTex = ("Refresh");

		topManager.add(/* refreshButton */new NullField(NON_FOCUSABLE));
		topManager.add(lastUpdatedDateTime);
		verticalFieldManager.add(topManager);
		verticalFieldManager.add(bseBanner);
		verticalFieldManager.add(lblValues);

		horizontalFieldManager.add(new LoadingComponent(
				AppConstants.loadingMessage, AppConstants.screenWidth,
				ChartProperties.getDefaultChartProperties().getChartHeight()
						+ ChartProperties.getDefaultChartProperties()
								.getChartxAxisHeight() * 2));

		verticalFieldManager.add(horizontalFieldManager);
		mainManager.add(verticalFieldManager);

		_addRemoveItem = new MenuItem(strButtonText, 100, 100) // Add to /
																// Remove from
																// Watchlist
		{
			public void run() {
				if (!Utils.sessionAlive) {
					ActionInvoker.processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				} else // if(!restrict)
				{
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {
								public void run() {
									Utils.WATCHLIST = true;
									MyWatchList.isFINISHED = false;
									MyWatchList.REFRESH = false;
									ScreenInvoker
											.showWaitScreen(AppConstants.loadingMessage);
									new WatchListPopUp(homeJson
											.getCompanyCode(),
											Utils.WATCHLIST_MODE);
								}
							});
				}
			}
		};
		String strEquityText = "Equity";
		_addEquityItem = new MenuItem(strEquityText, 100, 100) // Add to /
																// Remove from
																// Watchlist
		{
			public void run() {
				if (!Utils.sessionAlive) {
					ActionInvoker.processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				} else // if(!restrict)
				{
					/*
					 * UiApplication.getUiApplication().invokeAndWait(new
					 * Runnable() { public void run() { Utils.WATCHLIST = true;
					 * MyWatchList.isFINISHED = false; MyWatchList.REFRESH =
					 * false;
					 * ScreenInvoker.showWaitScreen(AppConstants.loadingMessage
					 * ); new WatchListPopUp(homeJson.getCompanyCode(),
					 * Utils.WATCHLIST_MODE); } });
					 */
					Vector vectorCommandData = new Vector();
					vectorCommandData.addElement(homeJson.getSymbol());
					vectorCommandData.addElement(homeJson.getReligareCode());
					vectorCommandData.addElement("BSE");
					// CompanyDetailsSnippetsScreen.addDirect = flg;
					actionPerfomed(ActionCommand.CMD_COMPANY_DETAILS_SCREEN,
							vectorCommandData);
				}
			}
		};
		_marketDepth = new MenuItem("Market Depth", 100, 100) {
			public void run() {
				LOG.print(" - 0 = - 0 = 0 -");
				{
					if (homeJson != null) {
						marketDepthVector = new Vector();
						marketDepthVector.addElement(Utils
								.getCompanyLatestTradingDetailsURL(homeJson
										.getCompanyCode()));
						marketDepthVector.addElement(homeJson.getBuyQty());
						marketDepthVector.addElement(homeJson.getBuyPrice());
						marketDepthVector.addElement(homeJson.getSellQty());
						marketDepthVector.addElement(homeJson.getSellPrice());
						LOG.print(" ---------->>>> ///////////////// marketDepthVector.size() "
								+ marketDepthVector.size());
						if (marketDepthVector.size() == 5) {
							actionPerfomed(
									ActionCommand.CMD_MARKET_DEPTH_SCREEN,
									marketDepthVector);
							// Action action = new
							// Action(ActionCommand.CMD_MARKET_DEPTH_SCREEN,
							// marketDepthVector);
							// ActionInvoker.processCommand(action);
						} else if (marketDepthVector.size() == 4) {
							actionPerfomed(
									ActionCommand.CMD_MARKET_DEPTH_SCREEN,
									MarketDepthScreen.marketDepthdata_);
						}
					}
				}
			}
		};
		AppConstants.WEBVIEW_URL_TEXT = "Trade";
		_tradeNowItem = new MenuItem("Trade", 100, 100) {
			public void run() {
				if (!Utils.sessionAlive) {
					ActionInvoker.processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				} else {
					ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					/*
					 * if(!AppConstants.NSE) { jsonBanner.setExchange(true); new
					 * TradeNowMainScreenCM
					 * ("tradenow",AppConstants.WEBVIEW_URL,jsonBanner); } else
					 * { jsonBanner.setExchange(false); new
					 * TradeNowMainScreenCM(
					 * "tradenow",AppConstants.WEBVIEW_URL,jsonBanner); }
					 */
					Vector v = new Vector();
					v.addElement(Utils.getFNOExpiryAndStrikeDataURL(homeJson
							.getCompanyCode()));
					v.addElement("");
					FNOTradeConfiguration fnoTradeConfig = new FNOTradeConfiguration();
					fnoTradeConfig
							.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX);
					fnoTradeConfig
							.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX);
					fnoTradeConfig.setCompanyCode(homeJson.getCompanyCode());
					String expiry = null;
					if (homeJson.getCompanyCode().indexOf("_") > -1) {
						expiry = homeJson.getCompanyCode();
						expiry = expiry.substring(expiry.indexOf("_") + 1,
								expiry.length());
						expiry = expiry.substring(0,
								expiry.length() > 12 ? expiry.indexOf("_")
										: expiry.length());
					}
					fnoTradeConfig.setFnoExpiryDate(expiry);
					if (homeJson.getCompanyCode().indexOf("_CE") > -1
							|| homeJson.getCompanyCode().indexOf("_PE") > -1) {
						fnoTradeConfig
								.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX);
						String string = "";
						if (homeJson.getCompanyCode().indexOf("_CE") > -1) {
							LOG.print("fnoTradeConfig.setOptioncepeType(FNOTradeConfiguration.FNO_OPTION_TYPE_CE)"
									+ homeJson.getCompanyCode());
							fnoTradeConfig
									.setOptioncepeType(FNOTradeConfiguration.FNO_OPTION_TYPE_CE);
							string = homeJson.getCompanyCode().substring(0,
									homeJson.getCompanyCode().indexOf("_CE"));

						} else {
							LOG.print("fnoTradeConfig.setOptioncepeType(FNOTradeConfiguration.FNO_OPTION_TYPE_PE)"
									+ homeJson.getCompanyCode());
							fnoTradeConfig
									.setOptioncepeType(FNOTradeConfiguration.FNO_OPTION_TYPE_PE);
							string = homeJson.getCompanyCode().substring(0,
									homeJson.getCompanyCode().indexOf("_PE"));
						}
						String prs = "-1";
						if (string.indexOf("_") > -1) {
							int len = string.indexOf("_") + 1;
							string = string.substring(len, string.length());
							if (string.indexOf("_") > -1) {
								len = string.indexOf("_") + 1;
								string = string.substring(len, string.length());
								prs = string;
								LOG.print("fnoTradeConfig.setFnoStrikePriceValue : "
										+ string);
							}
						}
						fnoTradeConfig.setFnoStrikePriceValue(prs);
					}
					/*
					 * else if(homeJson.getCompanyCode().indexOf("_PE")>-1) {
					 * LOG.print(
					 * "fnoTradeConfig.setOptioncepeType(FNOTradeConfiguration.FNO_OPTION_TYPE_PE)"
					 * );
					 * fnoTradeConfig.setOptioncepeType(FNOTradeConfiguration.
					 * FNO_OPTION_TYPE_PE); String string =
					 * homeJson.getCompanyCode().substring(0,
					 * homeJson.getCompanyCode().indexOf("_PE"));
					 * if(string.indexOf("_")>-1) { int len =
					 * string.indexOf("_")+1; string = string.substring(len,
					 * string.length()); if(string.indexOf("_")>-1) { len =
					 * string.indexOf("_")+1; string = string.substring(len,
					 * string.length());
					 * fnoTradeConfig.setFnoStrikePriceValue(string); } else
					 * fnoTradeConfig.setFnoStrikePriceValue("-1"); } else {
					 * fnoTradeConfig.setFnoStrikePriceValue("-1"); } }
					 */
					else
						fnoTradeConfig.setFnoStrikePriceValue("-1");
					v.addElement(fnoTradeConfig.copy());
					v.addElement(homeJson.copy());
					v.addElement(expiry);
					FNOTradeScreen.storeForModifyFlag = false;
					FNOTradeScreen.storeForModify = v;
					FNOTradeScreen.storeForModifyHash = null;
					ActionInvoker.processCommand(new Action(
							ActionCommand.CMD_FNO_TRADE, v));
				}
			}
		};

		_homeItem = new MenuItem("Home", 100, 100) {
			public void run() {
				actionPerfomed(ActionCommand.CMD_GRID_SCREEN, null);
			}
		};
		_searchItem = new MenuItem("Search Scrip", 100, 100) {
			public void run() {
				actionPerfomed(ActionCommand.CMD_SEARCH_SCREEN, null);
			}
		};

		add(mainManager);
		// refreshme.setLoading(true);
		lastUpdatedDateTime.setText("Last Updated : "
				+ homeJson.getLastTradedTime());
		LOG.print("LastTradedTime");
		lastUpdatedDateTime.getManager().invalidate();
		lblValues.setValue("Day High/Low",
				homeJson.getHigh() + "-" + homeJson.getLow(), "Open Interest",
				homeJson.getOpenInterest(), "Volume", homeJson.getVolume());
		// Configure BottomMenu and set commands
		bottomMenu = BottomMenu.getBottomMenuInstance(this,
				ImageManager.getBottomMenuImages(true),
				ImageManager.getBottomMenuImages(false),
				ActionCommand.CMD_NONE, AppConstants.bottomMenuCommands);
		loadGraph();
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				try {
					ScreenInvoker.pushScreen(object, true, true);
				} catch (Exception e) {
				}
			}
		});
		if (onceRefresh) {
			onceRefresh = false;
			refreshFields();
		}
	}

	public String getTitle() {
		return this.strTitle;
	}

	public boolean onClose() {
		AppConstants.NSE = false;
		return super.onClose();
	}

	public boolean onMenu(int instance) {
		LOG.print("onMenumenuBlock " + menuBlock);
		if (menuBlock) {
			menuBlock = false;
			return false;
		}
		return super.onMenu(instance);
	}

	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
		LOG.print("onMenumenuBlock " + menuBlock);
		if (menuBlock) {
			menuBlock = false;
			return;
		}
		// if(!refreshme.getLoading()){
		// if(symbol.length()>0)
		menu.add(_tradeNowItem);
		menu.add(_addRemoveItem);
//		if (!((homeJson.getReligareCode().equalsIgnoreCase("0"))))
//			menu.add(_addEquityItem);
		menu.add(_marketDepth);
		menu.add(MenuItem.separator(100));
		// addMenuItem(SeparatorField.LINE_HORIZONTAL);
		menu.add(_homeItem);
		menu.add(_searchItem);
		// }
	}

	public void componentsPrepared(byte componentID, final Object component) {
		// if(!isRefresh) return;
		// if(!ChartComponent.isDrawFinished) return;
		switch (componentID) {
		case COMPONENT_COMPANY_CHART:
			/*
			 * if(field != null) { field = (Field)component; isChartLoaded =
			 * true; return; }
			 */
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {

					horizontalFieldManager.deleteAll();
					if (component != null) {
						horizontalFieldManager.add((Field) component);
						field = (Field) component;
					}
					isChartLoaded = true;
				}
			});
			break;
		}
	}

	public void actionPerfomed(byte Command, Object sender) {
		switch (Command) {
		default:
			ActionInvoker.processCommand(new Action(Command, sender));
			break;
		}
	}

	public void refreshFields() {
		if (homeJson != null) {
			if (homeJson.getMarketStatus().equalsIgnoreCase("CLOSED")) {
				return;
			}
		}
		if (REFRESH) {
			if (homeJson != null && blockAutoRefresh) {
				refreshme.setLoading(true);
				blockAutoRefresh = false;
				
				
				
				
				
				ReturnStringParser rparse = new ReturnStringParser(
						Utils.replaceString(AppConstants.commoditySpecificSearchURL, "##KEYWORD##", symbol), 777, this);
			}
			if (isChartLoaded && refreshClick) {
				isChartLoaded = false;
				refreshClick = false;
				loadGraph();
			} else
				refreshClick = false;
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
			super(new VerticalFieldManager(CustomPopUpScreen.VERTICAL_SCROLL
					| CustomPopUpScreen.VERTICAL_SCROLLBAR));
			ccode = ccode_;
			LOG.print("Company Details Snippet " + ccode_);
			exchange = "NSEFO";
			sendHTTPRequest(1);
		}

		public void sendHTTPRequest(final int i) {
			thread = new Thread(new Runnable() {
				public void run() {
					try {
						ScreenInvoker
								.showWaitScreen(AppConstants.loadingMessage);
						// doPaint();
						String strAuthURL = Utils.getWatchListURL(i,
								UserInfo.getUserID());
						LOG.print("Company Add to WatchList URL : "
								+ strAuthURL);
						String strResponse = HttpProcess
								.getHttpsMD5Connection(strAuthURL);
						LOG.print("Company Add to WatchList Response : "
								+ strResponse);
						final Vector result = parseAndSaveRegistrationInfo(i,
								strResponse);
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {
									public void run() {
										if (!Utils.sessionAlive)
											return;
										if (result.size() == 0) {
											ScreenInvoker
													.removeRemovableScreen();
											deleteAll();
											Dialog.alert("No records found");
										} else {
											if (i == 1) {
												vector = result;
												ScreenInvoker
														.removeRemovableScreen();
												if (vector.size() == 2) {
													Action action = new Action(
															ActionCommand.CMD_WATCHLIST_SCREEN,
															null);
													ActionInvoker
															.processCommand(action);
												} else {
													for (int i = 0; i < vector
															.size(); i++) {
														if (vector.elementAt(i) instanceof WatchListMainBean) {
															final WatchListMainBean bean = (WatchListMainBean) vector
																	.elementAt(i);

															add(new WatchListSecondField(
																	FOCUSABLE,
																	bean.getDisplayName(),
																	(AppConstants.screenWidth / 3) * 2) {
																protected boolean navigationClick(
																		int status,
																		int time) {
																	if ((timer + 100) < System
																			.currentTimeMillis()) {
																		timer = System
																				.currentTimeMillis();
																		sendHTTPRequest(
																				bean.copy(),
																				ccode);
																	}
																	return super
																			.navigationClick(
																					status,
																					time);
																}

																protected boolean touchEvent(
																		TouchEvent message) {
																	if (message
																			.getEvent() == TouchEvent.CLICK) {
																		if ((timer + 100) < System
																				.currentTimeMillis()) {
																			timer = System
																					.currentTimeMillis();
																			sendHTTPRequest(
																					bean.copy(),
																					ccode);
																		}
																	}
																	return super
																			.touchEvent(message);
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

		public Vector parseAndSaveRegistrationInfo(int i, String strResponse) {
			Vector v = new Vector();
			if (!Utils.sessionAlive)
				return v;
			if (i == 1) {
				Json js = new Json(strResponse);
				String user = "";
				for (int x = 0; x < js.getdata.size(); x++) {
					Hashtable ht = (Hashtable) js.getdata.elementAt(x);
					user = (String) ht.get("Type");
					if (user.equalsIgnoreCase("USER")) {
						WatchListMainBean bean = new WatchListMainBean();
						bean.setDisplayName((String) ht.get("DisplayName"));
						bean.setTemplateName((String) ht.get("Template"));
						bean.setType((String) ht.get("Type"));
						bean.setExchange((String) ht.get("Exchange"));
						v.addElement(bean.copy());
					}
				}
			} else {
				String text = "";
				for (int x = 1; x < strResponse.length() - 1; x++) {
					if (strResponse.charAt(x) == ',') {
						if (text.length() != 0)
							v.addElement(text);
						text = "";
					} else {
						text = text + strResponse.charAt(x);
					}
				}
			}
			return v;

		}

		public void sendHTTPRequest(final WatchListMainBean bean,
				final String code) {
			if (code.length() == 0)
				return;
			if (code.indexOf(' ') > -1)
				return;
			thread = new Thread(new Runnable() {
				public void run() {
					try {
						ScreenInvoker.removeRemovableScreen();
						ScreenInvoker
								.showWaitScreen(AppConstants.loadingMessage);
						// doPaint();
						// final String exch =
						// wlName.indexOf('.')<0?(wlName.equalsIgnoreCase("NIFTY")?"NSE":"BSE"):wlName.substring(wlName.length()-3,
						// wlName.length());
						String strAuthURL = Utils.getWatchListURLAddScrips(
								UserInfo.getUserID(), bean.getDisplayName(),
								"NSEFO", code);
						LOG.print(strAuthURL);
						String strResponse = HttpProcess
								.getHttpsMD5Connection(strAuthURL);
						final int result = parseAndSaveRegistrationInfo(strResponse);
						UiApplication.getUiApplication().invokeLater(
								new Runnable() {
									public void run() {
										if (result != 3)
											ScreenInvoker
													.removeRemovableScreen();
										if (result == 1) {
											Vector v = new Vector();
											v.addElement(code);
											Utils.WATCHLIST = true;
											Vector wlScreen = new Vector();
											wlScreen.addElement(bean
													.getTemplateName());
											Action action = new Action(
													ActionCommand.CMD_WL_FILL_SCREEN,
													wlScreen);
											ActionInvoker
													.processCommand(action);

										} else if (result == 2)
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

		public int parseAndSaveRegistrationInfo(String strResponse) {
			if (strResponse == null)
				return 3;
			Json js = new Json(strResponse);
			String user = "";
			Hashtable ht = (Hashtable) js.getdata.elementAt(0);
			user = (String) ht.get("MSG");
			if (user.equalsIgnoreCase("SUCCESS"))
				return 1;
			return 2;

		}

		protected boolean keyDown(int keyCode, int time) {
			int key = Keypad.key(keyCode);
			if (key == Keypad.KEY_ENTER) {

			} else if (key == Keypad.KEY_ESCAPE) {
				synchronized (UiApplication.getEventLock()) {

					if (isDisplayed()) {
						close();
					}
				}
			} else if (key == Keypad.KEY_END) {
				LOG.print("KEY_END EXIT from app");
				System.exit(0);
			}
			return super.keyDown(keyCode, time);
		}

	}

	protected boolean keyDown(int keyCode, int time) {
		int key = Keypad.key(keyCode);
		if (key == Keypad.KEY_END) {
			LOG.print("KEY_END EXIT from app");
			System.exit(0);
		}
		return super.keyDown(keyCode, time);
	}

	public void setReturnString(final String string, int id) {
		if (id == 777) {
			UiApplication.getUiApplication().invokeAndWait(new Runnable() {
				public void run() {
					Vector v = HomeJsonParser.getVector(string);
					if (v.size() > 0) {
						homeJson = (HomeJson) v.elementAt(0);

						// bseBanner.setData(homeJson.getLastTradedPrice(),
						// homeJson.getChange(),
						// (homeJson.getPercentageDiff().indexOf("-") == -1 ?
						// "+"+homeJson.getPercentageDiff() :
						// homeJson.getPercentageDiff()),homeJson.getOpenInterest(),
						// homeJson.getVolume(), homeJson);
						bseBanner.setStartFlag(false);
						bseBanner.setData(
								homeJson.getLastTradedPrice(),
								homeJson.getChange(),
								(homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"
										+ homeJson.getPercentageDiff()
										: homeJson.getPercentageDiff()),
								homeJson.getA(), homeJson.getD(), homeJson
										.getS());
						bseBanner.startUpdate();
						if (homeJson.getMarketStatus().equalsIgnoreCase(
								"CLOSED"))
							REFRESH = false;
						lastUpdatedDateTime.setText("Last Updated : "
								+ homeJson.getLastTradedTime());
						if (!homeJson.getLastTradedPrice().equalsIgnoreCase(
								"null")) {
							lblValues.setValue(
									"Day High/Low",
									homeJson.getHigh() + "-"
											+ homeJson.getLow(),
									"Open Interest",
									homeJson.getOpenInterest(), "Volume",
									homeJson.getVolume());
						} else {
							lblValues.setValue("Day High/Low", "-",
									"Open Interest", "", "Volume", "");
						}
					}
					refreshme.setLoading(false);
					blockAutoRefresh = true;
				}
			});

		}
	}

	public void loadGraph() {
	
		ChartItemParser chartCompanyDataParser = new ChartItemParser(
				commodityChartUrl, this, 878);
		chartCompanyDataParser.getScreenData();
	}

	public void setData(Vector vector, int id) {
		if (id == 878) {
			Vector dataVector = vector;
			LOG.print(" Chart Data Size " + dataVector.size());
			if (dataVector.size() == 0) {
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						horizontalFieldManager.deleteAll();
						horizontalFieldManager.add(new LoadingComponent(
								"Graph data not found!",
								AppConstants.screenWidth, ChartProperties
										.getDefaultChartProperties()
										.getChartHeight()
										+ ChartProperties
												.getDefaultChartProperties()
												.getChartxAxisHeight() * 2,
								false));
					}
				});
			} else {
				ChartEngine chartCDComponent = ChartEngine.getInstance(
						ChartProperties.getDefaultChartProperties(),
						dataVector, false, ChartEngine.CHART_COMPANY_DETAIL);
				componentsPrepared(
						COMPONENT_COMPANY_CHART,
						chartCDComponent.createChart(
								vector,
								Double.parseDouble(vector.elementAt(
										vector.size() - 3).toString()),
								Double.parseDouble(vector.elementAt(
										vector.size() - 2).toString()),
								Double.parseDouble(vector.elementAt(
										vector.size() - 1).toString())));
			}
			refreshme.setLoading(false);
		}
	}
}

