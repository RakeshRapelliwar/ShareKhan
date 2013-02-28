package com.snapwork.actions;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.Vector;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.PopupScreen;

import com.snapwork.areacharts.ChartEngine;
import com.snapwork.areacharts.ChartProperties;
import com.snapwork.beans.Expiry;
import com.snapwork.beans.FNOOrderConfirmationBean;
import com.snapwork.beans.FNOSearchBean;
import com.snapwork.beans.FNOTradeBean;
import com.snapwork.beans.FNOTradeOrderConfirmBean;
import com.snapwork.beans.FoScrips;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.WatchListJson;
import com.snapwork.components.GLTabView;
import com.snapwork.components.GL_FutureOptionView;
import com.snapwork.components.LoadingComponent;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.WaitScreen;
import com.snapwork.parsers.ChartItemParser;
import com.snapwork.parsers.CommodityParser;
import com.snapwork.parsers.FNOOrderParser;
import com.snapwork.parsers.FNOTradeModifyDetailsParser;
import com.snapwork.parsers.FNOTradeOrderConfirmParser;
import com.snapwork.parsers.FNOTradeOrderDetailsParser;
import com.snapwork.parsers.FNOTradeOrderTurnOverParser;
import com.snapwork.parsers.FNOTradeParser;
import com.snapwork.parsers.GlobalParser;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.NewsParser;
import com.snapwork.parsers.SearchParser;
import com.snapwork.parsers.TopGainLoseItemParser;
import com.snapwork.parsers.TradeNowMainParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.Debug;
import com.snapwork.util.FNOTradeConfiguration;
import com.snapwork.util.HttpConnectionFactory;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.URLEncode;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;
import com.snapwork.view.AppDialogMemPass;
import com.snapwork.view.AppExitSessionExpired;
import com.snapwork.view.AppExitView;
import com.snapwork.view.CommodityDetailsSnippetScreen;
import com.snapwork.view.CompanyDetailsSnippetsScreen;
import com.snapwork.view.CompanyFNODetailsSnippetsScreen;
import com.snapwork.view.CompanyPeriodicCharts;
import com.snapwork.view.CurrentStat;
import com.snapwork.view.FullScreenChart;
import com.snapwork.view.FutureOptionScreen;
import com.snapwork.view.GridScreen;
import com.snapwork.view.HomeScreen;
import com.snapwork.view.MarketDepthScreen;
import com.snapwork.view.MemberShipPasswordChangeScreen;
import com.snapwork.view.MemberShipQAChangeScreen;
import com.snapwork.view.MyWatchList;
import com.snapwork.view.MyWatchListEdit;
import com.snapwork.view.MyWatchMainListEdit;
import com.snapwork.view.NIFTYCompanyDetailsSnippetsScreen;
import com.snapwork.view.NewsScreen;
import com.snapwork.view.NewsSnippetsScreen;
import com.snapwork.view.NiftyHomeScreen;
import com.snapwork.view.ProfileChangeScreen;
import com.snapwork.view.ReportsMainScreen;
import com.snapwork.view.SearchCommodity;
import com.snapwork.view.SearchEFScreen;
import com.snapwork.view.SearchStocks;
import com.snapwork.view.SplashPreScreen;
import com.snapwork.view.SplashScreen;
import com.snapwork.view.TOCScreen;
import com.snapwork.view.TopGLScreen;
import com.snapwork.view.TradeViewScreen;
import com.snapwork.view.TradingPasswordChangeScreen;
import com.snapwork.view.TradingQAChangeScreen;
import com.snapwork.view.UserRegistrationScreen;
import com.snapwork.view.VersionUpdatesScreen;
import com.snapwork.view.WatchListFill;
import com.snapwork.view.WatchListPopUp;
import com.snapwork.view.WatchListScreen;
import com.snapwork.view.trade.FNOTradeOrderConfirmationScreen;
import com.snapwork.view.trade.FNOTradeOrderModifyScreen;
import com.snapwork.view.trade.FNOTradeOrderNumberViewScreen;
import com.snapwork.view.trade.FNOTradeOrderTurnOverScreen;
import com.snapwork.view.trade.FNOTradeScreen;
import com.snapwork.view.trade.FNOTradeTurnOverConfirmationScreen;
import com.snapwork.view.trade.ReportsOrderView;
import com.snapwork.view.trade.ReportsOrderViewScreen;
import com.snapwork.view.trade.SlideViewScreen;
import com.snapwork.view.trade.TradeNowMainScreen;
import com.snapwork.view.trade.TradeNowModifyScreen;
import com.snapwork.view.trade.TradeNowOrderResultScreen;

public class ActionInvoker implements Screen {
	private byte dataForCommand = 0;
	private Thread animationThread;
	public static byte extra = 0;
	static String commodityChartUrl = null, exchange = null;
	private static String symbol;

	public ActionInvoker(byte dataForCommand) {
		this.dataForCommand = dataForCommand;
	}

	public static void processCommand(final Action action) {
		try {
			/*
			 * int directions =
			 * net.rim.device.api.system.Display.DIRECTION_PORTRAIT |
			 * net.rim.device.api.system.Display.ORIENTATION_PORTRAIT;
			 * 
			 * net.rim.device.api.ui.Ui.getUiEngineInstance().
			 * setAcceptableDirections(directions);
			 */
			AutoScreenRefresherThread.onLoad = false;
			System.gc();
			switch (action.getCommand()) {
			case ActionCommand.CMD_DEBUG:
				// Debug.debug("Double zero : "+Double.parseDouble("0"));
				int day,
				hour;
				Calendar calSystemTimeIST = null;
				if (TimeZone.getDefault().getID()
						.equals(TimeZone.getTimeZone("Asia/Calcutta").getID())) {
					calSystemTimeIST = Calendar.getInstance();
				} else {
					calSystemTimeIST = Calendar.getInstance(TimeZone
							.getTimeZone("Asia/Calcutta"));
				}
				day = calSystemTimeIST.get(Calendar.DAY_OF_WEEK);
				hour = calSystemTimeIST.get(Calendar.HOUR_OF_DAY);

				Debug.debug("day : " + day);
				Debug.debug("hour : " + hour);
				System.exit(1);
				break;
			case ActionCommand.CMD_SPLASH:
				if (action.getCommandData() != null) {
					ScreenInvoker.pushScreen(new SplashScreen(action
							.getCommandData().toString()), true, true);
				} else {
					ScreenInvoker
							.pushScreen(new SplashScreen(null), true, true);
				}
				break;
			case ActionCommand.CMD_SPLASH_PRE:
				if (action.getCommandData() != null) {
					ScreenInvoker.pushScreen(new SplashPreScreen(action
							.getCommandData().toString()), true, true);
				} else {
					ScreenInvoker.pushScreen(new SplashPreScreen(null), true,
							true);
				}
				break;
			case ActionCommand.CMD_UPDATE_VERSION:
				if (action.getCommandData() != null) {
					Vector vector = (Vector) action.getCommandData();
					VersionUpdatesScreen versionUpdatesScreen = new VersionUpdatesScreen(
							vector.elementAt(0).toString(), vector.elementAt(1)
									.toString(), vector.elementAt(2).toString());
					ScreenInvoker.pushModalPopupScreen(versionUpdatesScreen);
				}
				break;
			case ActionCommand.CMD_USER_REGISTRATION:
				ScreenInvoker.pushScreen(new UserRegistrationScreen(), true,
						true);
				break;
			case ActionCommand.CMD_PROFILE_SCREEN:
				if (Utils.sessionAlive) {
					// Utils.firstWebViewLoad = true;
					// AppConstants.WEBVIEW_URL = AppConstants.domainUrl
					// +"profile.php?custId="+UserInfo.getUserID()+"&userAgent=bb";
					// Utils.webViewInstance = new
					// WebViewScreen("Profile",AppConstants.WEBVIEW_URL);
					ProfileChangeScreen myProfileChangeScreen = new ProfileChangeScreen();
					ScreenInvoker.pushScreen(myProfileChangeScreen, true, true);
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;
			case ActionCommand.CMD_PROFILE_CHANGE_SCREEN:

				// AppConstants.WEBVIEW_URL = AppConstants.domainUrl
				// +"profile.php?custId="+UserInfo.getUserID();
				// WebViewScreen webViewp = new
				// WebViewScreen("Profile",AppConstants.domainUrl
				// +"profile.php?custId="+UserInfo.getUserID());
				// ScreenInvoker.pushScreen(webViewp, true, true);
				String profileDataChangeString = (String) ((Vector) action
						.getCommandData()).elementAt(0);
				if (profileDataChangeString
						.equalsIgnoreCase("MEMBERSHIP PASSWORD")) {
					LOG.print("MEMBERSHIP PASSWORD CHANGE SCREEN");
					MemberShipPasswordChangeScreen memberShipPasswordChangeScreen = new MemberShipPasswordChangeScreen();
					ScreenInvoker.pushScreen(memberShipPasswordChangeScreen,
							true, true);
				} else if (profileDataChangeString
						.equalsIgnoreCase("TRADING PASSWORD")) {
					LOG.print("TRADING PASSWORD CHANGE SCREEN");
					TradingPasswordChangeScreen tradingPasswordChangeScreen = new TradingPasswordChangeScreen();
					ScreenInvoker.pushScreen(tradingPasswordChangeScreen, true,
							true);
				} else if (profileDataChangeString
						.equalsIgnoreCase("MEMBERSHIP HINT Q&A")) {
					LOG.print("MEMBERSHIP HINT Q&A CHANGE SCREEN");
					MemberShipQAChangeScreen memberShipQAChangeScreen = new MemberShipQAChangeScreen();
					ScreenInvoker.pushScreen(memberShipQAChangeScreen, true,
							true);
				} else if (profileDataChangeString
						.equalsIgnoreCase("TRADING HINT Q&A")) {
					LOG.print("TRADING HINT Q&A CHANGE SCREEN");
					TradingQAChangeScreen tradingQAChangeScreen = new TradingQAChangeScreen();
					ScreenInvoker.pushScreen(tradingQAChangeScreen, true, true);

				}
				break;

			case ActionCommand.CMD_BANNERS_HOME_SCREEN:
				// get BSE/NSE Banners Data
				ActionInvoker actionBannersData = new ActionInvoker(
						ActionCommand.CMD_BANNERS_HOME_SCREEN);

				HomeJsonParser homeJsonParser = new HomeJsonParser(
						AppConstants.bannersDataProvideUrl + "&"
								+ System.currentTimeMillis(),
						actionBannersData,
						((ThreadedComponents) action.getCommandData()));

				homeJsonParser.getScreenData();
				break;

			case ActionCommand.CMD_COMMODITY_DATA:
				// get BSE/NSE Banners Data
				ActionInvoker actionBannersData1 = new ActionInvoker(
						ActionCommand.CMD_COMMODITY_DATA);

				Vector vect = (Vector) action.getCommandData();

				commodityChartUrl = (String) vect.elementAt(2);
				exchange = (String) vect.elementAt(3);
				symbol = (String) vect.elementAt(4);

				HomeJsonParser homeJsonParser1 = new HomeJsonParser(
						(String) vect.elementAt(1), actionBannersData1, null);

				homeJsonParser1.getScreenData();
				break;

			case ActionCommand.CMD_COMMODITY_GRAPH_DATA:
				// ActionInvoker actionBseChartInvoker1 = new ActionInvoker(
				// ActionCommand.CMD_COMMODITY_GRAPH_DATA);
				// ChartItemParser chartBseItemParser1 = new ChartItemParser(
				// Utils.getCommodityChartData(companyCode, symbol),
				// actionBseChartInvoker1,
				// ((ThreadedComponents) action.getCommandData()),
				// AppConstants.INTRADAY);
				// chartBseItemParser1.getScreenData();
				break;

			case ActionCommand.CMD_BSE_GET_GRAPH_DATA:
				ActionInvoker actionBseChartInvoker = new ActionInvoker(
						ActionCommand.CMD_BSE_GET_GRAPH_DATA);
				ChartItemParser chartBseItemParser = new ChartItemParser(
						AppConstants.bseChartDataProviderUrl,
						actionBseChartInvoker,
						((ThreadedComponents) action.getCommandData()),
						AppConstants.INTRADAY);
				chartBseItemParser.getScreenData();
				break;
			case ActionCommand.CMD_NSE_GET_GRAPH_DATA:
				ActionInvoker actionNseChartInvoker = new ActionInvoker(
						ActionCommand.CMD_NSE_GET_GRAPH_DATA);
				ChartItemParser chartNseItemParser = new ChartItemParser(
						AppConstants.nseChartDataProviderUrl,
						actionNseChartInvoker,
						((ThreadedComponents) action.getCommandData()),
						AppConstants.INTRADAY);
				chartNseItemParser.getScreenData();
				break;

			case ActionCommand.CMD_NSEFO_GET_GRAPH_DATA:
				ActionInvoker actionNSEFOChartInvoker = new ActionInvoker(
						ActionCommand.CMD_BSE_GET_GRAPH_DATA);
				ChartItemParser chartNSEFOItemParser = new ChartItemParser(
						AppConstants.bseChartDataProviderUrl,
						actionNSEFOChartInvoker,
						((ThreadedComponents) action.getCommandData()),
						AppConstants.INTRADAY);
				chartNSEFOItemParser.getScreenData();
				break;
			case ActionCommand.CMD_MCX_GET_GRAPH_DATA:
				ActionInvoker actionMCXChartInvoker = new ActionInvoker(
						ActionCommand.CMD_NSE_GET_GRAPH_DATA);
				ChartItemParser chartMCXItemParser = new ChartItemParser(
						AppConstants.mcxChartDataProviderUrl,
						actionMCXChartInvoker,
						((ThreadedComponents) action.getCommandData()),
						AppConstants.INTRADAY);
				chartMCXItemParser.getScreenData();
				break;

			case ActionCommand.CMD_NCDEX_GET_GRAPH_DATA:
				ActionInvoker actionNCDEXChartInvoker = new ActionInvoker(
						ActionCommand.CMD_NSE_GET_GRAPH_DATA);
				ChartItemParser chartNCDEXItemParser = new ChartItemParser(
						AppConstants.ncdexChartDataProviderUrl,
						actionNCDEXChartInvoker,
						((ThreadedComponents) action.getCommandData()),
						AppConstants.INTRADAY);
				chartNCDEXItemParser.getScreenData();
				break;

			case ActionCommand.CMD_GRID_SCREEN:
			case ActionCommand.CMD_SETTINGS:
				GridScreen gridScreen = new GridScreen();
				ScreenInvoker.pushScreen(gridScreen, true, true);
				break;
			case ActionCommand.CMD_HOME_SCREEN:

				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);

				HomeScreen homeScreen = new HomeScreen();

				ScreenInvoker.pushScreen(homeScreen, true, true);
				// get BSE/NSE Banners Data

				processCommand(new Action(
						ActionCommand.CMD_BANNERS_HOME_SCREEN,
						(ThreadedComponents) homeScreen));
				// get BSE Chart Data
				processCommand(new Action(ActionCommand.CMD_BSE_GET_GRAPH_DATA,
						(ThreadedComponents) homeScreen));
				break;

			case ActionCommand.CMD_CURRENTSTAT:
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				CurrentStat current = new CurrentStat();
				ScreenInvoker.pushScreen(current, true, true);
				// get BSE/NSE Banners Data

				processCommand(new Action(
						ActionCommand.CMD_BANNERS_HOME_SCREEN,
						(ThreadedComponents) current));
				// //get BSE Chart Data
				// processCommand(new
				// Action(ActionCommand.CMD_BSE_GET_GRAPH_DATA,(ThreadedComponents)homeScreen));
				break;

			case ActionCommand.CMD_NIFTY_SCREEN:
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				NiftyHomeScreen niftyScreen = new NiftyHomeScreen(
						action.getCommandData());
				// get BSE/NSE Banners Data

				processCommand(new Action(
						ActionCommand.CMD_BANNERS_HOME_SCREEN,
						(ThreadedComponents) niftyScreen));
				// get NSE Chart Data
				processCommand(new Action(extra,
						(ThreadedComponents) niftyScreen));
				ScreenInvoker.pushScreen(niftyScreen, true, true);
				break;
			case ActionCommand.CMD_FULL_SCREEN_CHART:
				Vector vector = (Vector) action.getCommandData();
				FullScreenChart fullScreenChart = new FullScreenChart(vector
						.elementAt(0).toString());
				ChartEngine chartEngine = ChartEngine.getInstance(
						ChartProperties.getFullScreenChartProperties(), null,
						false, ChartEngine.CHART_BSE_NSE);
				Vector chartDataVector = (Vector) vector.elementAt(1);
				ScreenInvoker.pushScreen(fullScreenChart, true, true);
				fullScreenChart
						.componentsPrepared((byte) 0,
								chartEngine.createChart(chartDataVector, Double
										.parseDouble(chartDataVector.elementAt(
												chartDataVector.size() - 3)
												.toString()), Double
										.parseDouble(chartDataVector.elementAt(
												chartDataVector.size() - 2)
												.toString()), Double
										.parseDouble(chartDataVector.elementAt(
												chartDataVector.size() - 1)
												.toString())));
				break;
			case ActionCommand.CMD_COMPANY_FULL_INTRA_CHART:
				Vector vectorCompanyFullScreenIChart = (Vector) action
						.getCommandData();
				if (vectorCompanyFullScreenIChart.size() == 2) {
					ActionInvoker actionICFSC = new ActionInvoker(
							action.getCommand());
					CompanyPeriodicCharts companyPeriodicIChart = new CompanyPeriodicCharts(
							vectorCompanyFullScreenIChart.elementAt(0)
									.toString(), vectorCompanyFullScreenIChart
									.elementAt(1).toString(), (byte) 0);
					ChartItemParser chartCFICP = new ChartItemParser(
							Utils.getCompanyChartData(vectorCompanyFullScreenIChart
									.elementAt(1).toString()), actionICFSC,
							companyPeriodicIChart, AppConstants.INTRADAY);
					chartCFICP.getScreenData();
					ScreenInvoker.pushScreen(companyPeriodicIChart, true, true);
				} else {
					ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					ActionInvoker actionICFSC = new ActionInvoker(
							action.getCommand());
					ChartItemParser chartCFICP = new ChartItemParser(
							Utils.getCompanyChartData(vectorCompanyFullScreenIChart
									.elementAt(1).toString()), actionICFSC,
							(ThreadedComponents) vectorCompanyFullScreenIChart
									.elementAt(2), AppConstants.INTRADAY);
					chartCFICP.getScreenData();
				}
				break;
			case ActionCommand.CMD_COMPANY_FULL_WEEK_CHART:
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				Vector vectorCompanyFullScreenWChart = (Vector) action
						.getCommandData();
				ActionInvoker actionWCFSC = new ActionInvoker(
						action.getCommand());
				ChartItemParser chartCFWCP = new ChartItemParser(
						Utils.getCompanyWeekChartData(vectorCompanyFullScreenWChart
								.elementAt(1).toString()), actionWCFSC,
						(ThreadedComponents) vectorCompanyFullScreenWChart
								.elementAt(2), AppConstants.WEEK);
				chartCFWCP.getScreenData();
				break;
			case ActionCommand.CMD_COMPANY_FULL_MONTH_CHART:
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				Vector vectorCompanyFullScreenMChart = (Vector) action
						.getCommandData();
				ActionInvoker actionMCFSC = new ActionInvoker(
						action.getCommand());
				ChartItemParser chartCFMCP = new ChartItemParser(
						Utils.getCompanyMonthChartData(vectorCompanyFullScreenMChart
								.elementAt(1).toString()), actionMCFSC,
						(ThreadedComponents) vectorCompanyFullScreenMChart
								.elementAt(2), AppConstants.MONTH);
				chartCFMCP.getScreenData();
				break;
			case ActionCommand.CMD_COMPANY_FULL_6MONTH_CHART:
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				Vector vectorCompanyFullScreen6MChart = (Vector) action
						.getCommandData();
				ActionInvoker action6MCFSC = new ActionInvoker(
						action.getCommand());
				ChartItemParser chartCF6MCP = new ChartItemParser(
						Utils.getCompany6MonthChartData(vectorCompanyFullScreen6MChart
								.elementAt(1).toString()), action6MCFSC,
						(ThreadedComponents) vectorCompanyFullScreen6MChart
								.elementAt(2), AppConstants.MONTH6);
				chartCF6MCP.getScreenData();
				break;
			case ActionCommand.CMD_COMPANY_FULL_12MONTH_CHART:
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				Vector vectorCompanyFullScreen12MChart = (Vector) action
						.getCommandData();
				ActionInvoker action12MCFSC = new ActionInvoker(
						action.getCommand());
				ChartItemParser chartCF12MCP = new ChartItemParser(
						Utils.getCompany12MonthChartData(vectorCompanyFullScreen12MChart
								.elementAt(1).toString()), action12MCFSC,
						(ThreadedComponents) vectorCompanyFullScreen12MChart
								.elementAt(2), AppConstants.YEAR);
				chartCF12MCP.getScreenData();
				break;
			case ActionCommand.CMD_TRADE_VIEW_SCREEN:
				if (Utils.sessionAlive) {
					TradeViewScreen tradeView = new TradeViewScreen();
					ScreenInvoker.pushScreen(tradeView, true, true);
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}

				// ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				// ActionInvoker actionNewsInvoker = new
				// ActionInvoker(action.getCommand());
				// NewsParser newsParser;

				// /if(action.getCommandData()==null)
				// newsParser = new NewsParser(Utils.getNewsURL("1"),
				// actionNewsInvoker,"1");
				// else
				// newsParser = new
				// NewsParser(Utils.getNewsURL(action.getCommandData().toString()),
				// actionNewsInvoker,action.getCommandData().toString());

				// newsParser.getScreenData();
				break;
			/*
			 * case ActionCommand.CMD_WEBVIEW_SCREEN : if(Utils.sessionAlive) {
			 * Utils.firstWebViewLoad = false;
			 * ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
			 * //ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
			 * WebViewScreen webView = new
			 * WebViewScreen(AppConstants.WEBVIEW_URL_TEXT
			 * ,AppConstants.WEBVIEW_URL); ScreenInvoker.pushScreen(webView,
			 * true, true); } else { processCommand(new
			 * Action(ActionCommand.CMD_SESSION_EXPIRED,null)); } break; case
			 * ActionCommand.CMD_ORDERBOOK_SCREEN : if(Utils.sessionAlive) {
			 * Utils.firstWebViewLoad = true; Utils.webViewInstance = new
			 * WebViewScreen
			 * ("Order Book",Utils.getOrderBookURL(UserInfo.getUserID())); }
			 * else { processCommand(new
			 * Action(ActionCommand.CMD_SESSION_EXPIRED,null)); } break; case
			 * ActionCommand.CMD_HOLDINGS_SCREEN : if(Utils.sessionAlive) {
			 * Utils.firstWebViewLoad = true; Utils.webViewInstance = new
			 * WebViewScreen
			 * ("Holdings","http://www.google.com"Utils.getHoldingsURL
			 * (UserInfo.getUserID())); } else { processCommand(new
			 * Action(ActionCommand.CMD_SESSION_EXPIRED,null)); } break;
			 */
			case ActionCommand.CMD_REPORTSB_SCREEN:
				if (Utils.sessionAlive) {
					// ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					Vector vec = new Vector();
					String string = "Reports";
					ReportsMainScreen reportsMainScreen = new ReportsMainScreen(
							string);
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;
			case ActionCommand.CMD_REPORTS_SCREEN:
				if (Utils.sessionAlive) {
					// ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					Vector vec = (Vector) (action.getCommandData());
					String string = (String) vec.elementAt(0);
					ReportsMainScreen reportsMainScreen = new ReportsMainScreen(
							string);
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;
			case ActionCommand.CMD_REPORTS_ORDER_VIEW:
				if (Utils.sessionAlive) {
					ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					Vector vec = (Vector) (action.getCommandData());
					// String screenString = (String)vec.elementAt(0);
					// String url = (String)vec.elementAt(1);
					ReportsOrderViewScreen reportsOrderViewScreen = new ReportsOrderViewScreen(
							vec);
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;
			case ActionCommand.CMD_SLIDE_VIEW:
				if (Utils.sessionAlive) {
					if (!ReportsOrderView.Load) {
						ScreenInvoker
								.showWaitScreen(AppConstants.loadingMessage);
					}
					ReportsOrderView.Load = false;
					Vector slidevector = (Vector) (action.getCommandData());
					String string = (String) slidevector.elementAt(0);
					Vector slideVec = (Vector) slidevector.elementAt(1);
					SlideViewScreen slideViewScreen = new SlideViewScreen(
							string, slideVec);
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;
			/*
			 * case ActionCommand.CMD_ACCOUNTS_SCREEN : if(Utils.sessionAlive) {
			 * Utils.firstWebViewLoad = true; Utils.webViewInstance = new
			 * WebViewScreen
			 * ("Account",Utils.getAccountsURL(UserInfo.getUserID())); } else {
			 * processCommand(new
			 * Action(ActionCommand.CMD_SESSION_EXPIRED,null)); }
			 * 
			 * break;
			 */
			case ActionCommand.CMD_NEWS_PAGER:
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				ActionInvoker actionagerInvoker = new ActionInvoker(
						action.getCommand());

				Vector agerCommandData = (Vector) (action.getCommandData());
				String ageNo = agerCommandData.elementAt(0).toString();

				NewsParser NewsPagerParser = new NewsParser(
						Utils.getNewsURL(ageNo), actionagerInvoker,
						(ThreadedComponents) agerCommandData.elementAt(1));
				NewsPagerParser.getScreenData();
				break;
			case ActionCommand.CMD_NEWS_SCREEN:
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				ActionInvoker actionNewsInvoker = new ActionInvoker(
						action.getCommand());
				NewsParser newsParser;
				if (action.getCommandData() == null)
					newsParser = new NewsParser(Utils.getNewsURL("1"),
							actionNewsInvoker, "1");
				else
					newsParser = new NewsParser(Utils.getNewsURL(action
							.getCommandData().toString()), actionNewsInvoker,
							action.getCommandData().toString());

				newsParser.getScreenData();
				break;
			case ActionCommand.CMD_NEWS_DETAIL:
				byte selectedNewsIndex = Byte.parseByte(((Vector) action
						.getCommandData()).elementAt(0).toString());
				Vector newsDataVector = (Vector) (((Vector) action
						.getCommandData()).elementAt(1));
				short pageNo = Short.parseShort(((Vector) action
						.getCommandData()).elementAt(2).toString());
				final NewsSnippetsScreen newsSnippetsScreen = new NewsSnippetsScreen(
						newsDataVector, selectedNewsIndex, pageNo);
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						ScreenInvoker
								.pushScreen(newsSnippetsScreen, true, true);
					}
				});
				break;
			case ActionCommand.CMD_BSE_GL_SCREEN:
			case ActionCommand.CMD_NSE_GL_SCREEN:
			case ActionCommand.CMD_GLOBAL_GL_SCREEN:
				LOG.print("Gainer-Loser");
				if (action.getCommand() == ActionCommand.CMD_BSE_GL_SCREEN)
					GLTabView.selectedIndex = 0;
				else if (action.getCommand() == ActionCommand.CMD_NSE_GL_SCREEN)
					GLTabView.selectedIndex = 1;
				else if (action.getCommand() == ActionCommand.CMD_GLOBAL_GL_SCREEN)
					GLTabView.selectedIndex = 2;
				ActionInvoker actionGLInvoker = new ActionInvoker(
						action.getCommand());
				TopGainLoseItemParser topGainerLoserItemParser = null;
				GlobalParser globalGainerLoserItemParser = null;
				if (action.getCommand() == ActionCommand.CMD_BSE_GL_SCREEN) {
					ScreenInvoker.showWaitScreen("Loading...");
					topGainerLoserItemParser = new TopGainLoseItemParser(
							AppConstants.bseTopGainersLoosersUrl,
							actionGLInvoker,
							(ThreadedComponents) action.getCommandData());
					topGainerLoserItemParser.getScreenData();
				} else if (action.getCommand() == ActionCommand.CMD_NSE_GL_SCREEN) {
					ScreenInvoker.showWaitScreen("Loading...");
					topGainerLoserItemParser = new TopGainLoseItemParser(
							AppConstants.nseTopGainersLoosersUrl,
							actionGLInvoker,
							(ThreadedComponents) action.getCommandData());
					topGainerLoserItemParser.getScreenData();
				} else {
					ScreenInvoker.showWaitScreen("Loading...");
					globalGainerLoserItemParser = new GlobalParser(
							AppConstants.globalTopGainersLoosersUrl,
							actionGLInvoker,
							(ThreadedComponents) action.getCommandData());
					globalGainerLoserItemParser.getScreenData();
				}
				break;
			case ActionCommand.CMD_FNO_TRADE:
				if (Utils.sessionAlive) {
					try {
						ScreenInvoker
								.showWaitScreen(AppConstants.loadingMessage);
						Vector fnoDataVector = (Vector) action.getCommandData();
						if (fnoDataVector != null) {
							if (fnoDataVector.size() > 0) {
								String url = fnoDataVector.elementAt(0)
										.toString();
								LOG.print("FNO URL " + url);
								String indexName = ((HomeJson) fnoDataVector
										.elementAt(3)).getDisplayName1();
								FNOTradeConfiguration fnoTradeConfig = (FNOTradeConfiguration) fnoDataVector
										.elementAt(2);

								HomeJson bannerData = null;
								if (fnoDataVector.size() > 3) {
									bannerData = (HomeJson) fnoDataVector
											.elementAt(3);
								}
								String date = (String) fnoDataVector
										.elementAt(4);

								ActionInvoker fnoTradeActionInvoker = new ActionInvoker(
										action.getCommand());

								FNOTradeParser fnoTradeParser = new FNOTradeParser(
										url, indexName, fnoTradeActionInvoker,
										fnoTradeConfig, bannerData, date);
								fnoTradeParser.getScreenData();

							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;
			case ActionCommand.CMD_FNO_TRADE_ORDER_CONFIRMATION:
			case ActionCommand.CMD_FNO_TRADE_FRESH_ORDER_CONFIRMATION:
			case ActionCommand.CMD_FNO_TRADE_TURNOVER_CONFIRMATION:
				if (Utils.sessionAlive) {
					try {
						ScreenInvoker
								.showWaitScreen(AppConstants.loadingMessage);
						Vector fnoTradeOrderDataVector = (Vector) action
								.getCommandData();
						if (fnoTradeOrderDataVector != null) {
							if (fnoTradeOrderDataVector.size() > 0) {

								FNOOrderConfirmationBean confirmationBean = (FNOOrderConfirmationBean) fnoTradeOrderDataVector
										.elementAt(0);

								String url = "";
								if (action.getCommand() == ActionCommand.CMD_FNO_TRADE_TURNOVER_CONFIRMATION)
									url = Utils
											.getFNOPostSQ3TradeURL(confirmationBean);
								else if (action.getCommand() == ActionCommand.CMD_FNO_TRADE_FRESH_ORDER_CONFIRMATION) {
									if (fnoTradeOrderDataVector.size() == 2)
										url = (String) fnoTradeOrderDataVector
												.elementAt(1);
								} else
									url = Utils
											.getFNOPostModifyURL(confirmationBean);
								ActionInvoker fnoTradeOrderActionInvoker = new ActionInvoker(
										action.getCommand());
								LOG.print(url);
								FNOOrderParser fnoTradeOrderParser = new FNOOrderParser(
										url, confirmationBean,
										fnoTradeOrderActionInvoker);
								fnoTradeOrderParser.getScreenData();

							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;
			case ActionCommand.CMD_FNO_TRADE_ORDER_CONFIRM_POST:
				if (Utils.sessionAlive) {
					try {
						ScreenInvoker
								.showWaitScreen(AppConstants.loadingMessage);
						Vector fnoTradeOrderConfirmDataVector = (Vector) action
								.getCommandData();
						if (fnoTradeOrderConfirmDataVector != null) {
							if (fnoTradeOrderConfirmDataVector.size() > 0) {

								FNOOrderConfirmationBean confirmationBean = (FNOOrderConfirmationBean) fnoTradeOrderConfirmDataVector
										.elementAt(0);
								String url = "";
								if (fnoTradeOrderConfirmDataVector.size() > 1) {
									int val = ((Integer) fnoTradeOrderConfirmDataVector
											.elementAt(1)).intValue();
									if (val == 0)
										url = Utils
												.getFNOPostURL(confirmationBean);
									else if (val == 1)
										url = Utils
												.getFNOPostModifyURL(confirmationBean);
									else if (val == 2)// SQOff Trade
									{
										url = Utils
												.getFNOPostSQ2TradeURL(confirmationBean);
									} else if (val == 3)// SQOff Confirm
									{
										/*
										 * if(confirmationBean.getAction().
										 * equalsIgnoreCase("Buy"))
										 * confirmationBean.setAction("B"); else
										 * if(confirmationBean.getAction().
										 * equalsIgnoreCase("Sell"))
										 * confirmationBean.setAction("S");
										 */
										url = Utils
												.getFNOPostSQ5TradeURL(confirmationBean);
										LOG.print("FNO TurnOver Confirm URL");
										LOG.print(url);
									} else if (val == 4)// SQOff Modify
									{
										url = Utils
												.getFNOPostSQ4TradeURL(confirmationBean);
									}
								} else
									url = Utils.getFNOPostURL(confirmationBean);
								LOG.print("Post URL : " + url);
								ActionInvoker fnoTradeOrderConfirmActionInvoker = new ActionInvoker(
										action.getCommand());

								FNOTradeOrderConfirmParser fnoTradeConfirmParser = new FNOTradeOrderConfirmParser(
										url, fnoTradeOrderConfirmActionInvoker);
								fnoTradeConfirmParser.getScreenData();
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;
			case ActionCommand.CMD_FNO_TRADE_ORDER_DETAILS:
				if (Utils.sessionAlive) {
					try {
						ScreenInvoker
								.showWaitScreen(AppConstants.loadingMessage);
						Vector fnoTradeOrderDetailsDataVector = (Vector) action
								.getCommandData();
						if (fnoTradeOrderDetailsDataVector != null) {
							if (fnoTradeOrderDetailsDataVector.size() > 0) {

								String orderID = fnoTradeOrderDetailsDataVector
										.elementAt(0).toString();
								String scripCode = fnoTradeOrderDetailsDataVector
										.elementAt(1).toString();
								String exchange = fnoTradeOrderDetailsDataVector
										.elementAt(2).toString();
								String rmsCode = fnoTradeOrderDetailsDataVector
										.elementAt(3).toString();
								String serverPage = fnoTradeOrderDetailsDataVector
										.elementAt(4).toString();
								HomeJson bannerData = (HomeJson) fnoTradeOrderDetailsDataVector
										.elementAt(5);

								String url = Utils.getFNOTradeOrderDetailsURL(
										orderID, UserInfo.getUserID(),
										exchange, rmsCode, serverPage);
								LOG.print("Post URL : " + url);
								ActionInvoker fnoTradeOrderDetailsActionInvoker = new ActionInvoker(
										action.getCommand());

								FNOTradeOrderDetailsParser fnoTradeOrderDetailsParser = new FNOTradeOrderDetailsParser(
										url, fnoTradeOrderDetailsActionInvoker,
										scripCode, bannerData);
								fnoTradeOrderDetailsParser.getScreenData();
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;
			case ActionCommand.CMD_FNO_TRADE_ORDER_MODIFY:
				if (Utils.sessionAlive) {
					try {
						ScreenInvoker
								.showWaitScreen(AppConstants.loadingMessage);
						Vector fnoTradeOrderModifyDataVector = (Vector) action
								.getCommandData();
						if (fnoTradeOrderModifyDataVector != null) {
							if (fnoTradeOrderModifyDataVector.size() > 0) {

								String url = fnoTradeOrderModifyDataVector
										.elementAt(0).toString();
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;
			case ActionCommand.CMD_FNO_TRADE_ORDER_TURNOVER:
				if (Utils.sessionAlive) {
					try {
						ScreenInvoker
								.showWaitScreen(AppConstants.loadingMessage);
						Vector fnoTradeOrderTurnOver = (Vector) action
								.getCommandData();
						if (fnoTradeOrderTurnOver != null) {
							if (fnoTradeOrderTurnOver.size() > 0) {

								String symbol = fnoTradeOrderTurnOver
										.elementAt(0).toString();
								String exchange = fnoTradeOrderTurnOver
										.elementAt(1).toString();
								String turnOverAction = fnoTradeOrderTurnOver
										.elementAt(2).toString();
								String qty = fnoTradeOrderTurnOver.elementAt(3)
										.toString();
								String page = fnoTradeOrderTurnOver
										.elementAt(4).toString();

								HomeJson bannerData = null;
								if (fnoTradeOrderTurnOver.size() > 5) {
									bannerData = (HomeJson) fnoTradeOrderTurnOver
											.elementAt(5);
								}

								String url = Utils
										.getFnoTradeOrderTurnOverDetailsURL(
												symbol, exchange,
												turnOverAction, qty, page);

								LOG.print("Turn Over url : " + url);

								ActionInvoker fnoTradeOrderTurnOverActionInvoker = new ActionInvoker(
										action.getCommand());

								FNOTradeOrderTurnOverParser fnoTradeOrderParser = new FNOTradeOrderTurnOverParser(
										url,
										fnoTradeOrderTurnOverActionInvoker,
										bannerData);
								fnoTradeOrderParser.getScreenData();

							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;
			case ActionCommand.CMD_FUTURE_SCREEN:
			case ActionCommand.CMD_OPTION_SCREEN:
				ActionInvoker actionFOInvoker = new ActionInvoker(
						action.getCommand());
				String random = Long.toString(System.currentTimeMillis());
				HomeJsonParser fnoJSonParser = null;

				if (action.getCommand() == ActionCommand.CMD_FUTURE_SCREEN) {
					// AppConstants.OPTIONS_FLAG_First = true;
					LOG.print("Future Response");
					int month = 0;
					// LOG.print("Future URL ------ ------ ---- ------- "+Utils.getFuturesURL(AppConstants.source,
					// Expiry.getValue(month),
					// Expiry.getValue(month+1),Expiry.getValue(month+2))+"&"+random);
					ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);

					// String url=
					// "http://money.rediff.com/money1/current_status.php?companylist=";
					String url = AppConstants.domainUrl
							+ "getQuote.php?companylist=";
					String code = AppConstants.source;
					code = FoScrips.getValue(AppConstants.source);
					LOG.print(AppConstants.source
							+ "<- AppConstants.source : code ->" + code);
					String one = Expiry.getValue(month);
					String two = Expiry.getValue(++month);
					String three = Expiry.getValue(++month);
					if (AppConstants.source.equals("17023929")) {
						// ////while loop for nifty
						String add = "";
						if (SearchEFScreen.niftyIndices == null) {
							for (int i = 0; i < 9; i++) {
								if (i == 0) {
									code = "NIFTY";
								} else if (i == 1) {
									code = "MINIFTY";
								} else if (i == 2) {
									code = "BANKNIFTY";
								} else if (i == 3) {
									code = "CNXIT";
								} else if (i == 4) {
									code = "NFTYMCAP50";
								} else if (i == 5)// "DJIA","CNXINFRA","CNXPSE","S&P500"
								{
									code = "DJIA";
								} else if (i == 6) {
									code = "CNXINFRA";
								} else if (i == 7) {
									code = "CNXPSE";
								} else if (i == 8) {
									code = "S-P500";
								}
								String tempx = "";
								if (one != null) {
									tempx = code + "_" + one;
								}
								if (two != null) {
									tempx = tempx + "|" + code + "_" + two;
								}
								if (three != null) {
									tempx = tempx + "|" + code + "_" + three;
								}
								add = add + tempx + "|";
							}
						} else {
							for (int i = 0; i < SearchEFScreen.niftyIndices.length; i++) {
								code = SearchEFScreen.niftyIndices[i].replace(
										'&', '-');
								String tempx = "";
								if (one != null) {
									tempx = code + "_" + one;
								}
								if (two != null) {
									tempx = tempx + "|" + code + "_" + two;
								}
								if (three != null) {
									tempx = tempx + "|" + code + "_" + three;
								}
								add = add + tempx + "|";
							}
						}
						url = url + add.substring(0, add.length() - 1);
					} else {
						code = URLEncode.replace(code);
						String temp = "";
						if (one != null) {
							temp = code + "_" + one;
						}
						if (two != null) {
							temp = temp + "|" + code + "_" + two;
						}
						if (three != null) {
							temp = temp + "|" + code + "_" + three;
						}
						// ss
						url = url + temp;
					}
					LOG.print(url);
					fnoJSonParser = new HomeJsonParser(url + "&" + random,
							actionFOInvoker,
							(ThreadedComponents) action.getCommandData());
					fnoJSonParser.getScreenData();
				} else {
					LOG.print("Options Response");

					ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					String textUrl = AppConstants.source;
					textUrl = FoScrips.getValue(AppConstants.source);
					if (textUrl.indexOf("&") > -1)
						textUrl = URLEncode.getString(textUrl);
					String tx = AppConstants.source;
					if (tx.equals("17023929")) {
						textUrl = GL_FutureOptionView.NIFTY_OPTIONS;
					} else {
						GL_FutureOptionView.NIFTY_OPTIONS = null;
					}
					LOG.print(textUrl);
					// LOG.print(Utils.getOptionsURL(textUrl,
					// AppConstants.optionsMonth, AppConstants.optionsAmount,
					// AppConstants.optionsCEPE)+"&"+random);
					fnoJSonParser = new HomeJsonParser(Utils.getOptionsURL(
							textUrl, AppConstants.optionsMonth,
							AppConstants.optionsAmount,
							AppConstants.optionsCEPE)
							+ "&" + random, actionFOInvoker,
							(ThreadedComponents) action.getCommandData());
					fnoJSonParser.getScreenData();
				}

				break;

			case ActionCommand.CMD_MARKET_DEPTH_SCREEN:
				// ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				MarketDepthScreen marketDepth = new MarketDepthScreen(
						(Vector) action.getCommandData());
				ScreenInvoker.pushScreen(marketDepth, true, true);

				break;

			case ActionCommand.CMD_TRADE_MAIN:
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				Vector second1 = (Vector) action.getCommandData();
				String screenStringMain = (String) second1.elementAt(1);
				TradeNowMainScreen tradeNowMainScreen = new TradeNowMainScreen(
						screenStringMain, AppConstants.WEBVIEW_URL,
						(HomeJson) second1.elementAt(0));
				// ScreenInvoker.pushScreen(marketDepth, true, true);
				break;
			case ActionCommand.CMD_TRADE_MODIFY:

				Vector second2 = (Vector) action.getCommandData();
				String screenString = (String) second2.elementAt(2);
				// final TradeNowMainScreen object = (TradeNowMainScreen)
				// second2.elementAt(3);
				// UiApplication.getUiApplication().invokeLater(new Runnable() {
				// public void run() {
				// ScreenInvoker.pushScreen(object, false, true);
				// }
				// });
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				TradeNowModifyScreen tradeNowModifyScreen = new TradeNowModifyScreen(
						screenString, (Vector) second2.elementAt(0),
						(HomeJson) second2.elementAt(1));
				// ScreenInvoker.pushScreen(marketDepth, true, true);
				break;
			case ActionCommand.CMD_TRADE_RESULT:
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				Vector second3 = (Vector) action.getCommandData();
				String screenStringResult = (String) second3.elementAt(2);
				TradeNowOrderResultScreen tradeNowOrderResultScreen = new TradeNowOrderResultScreen(
						screenStringResult, (Vector) second3.elementAt(0),
						(HomeJson) second3.elementAt(1));
				// ScreenInvoker.pushScreen(marketDepth, true, true);
				break;
			case ActionCommand.CMD_COMPANY_STATICS_BANNERS:
				// get BSE/NSE Banners Data
				ActionInvoker actionCompanyStaticsBannersData = new ActionInvoker(
						action.getCommand());
				Vector companyStatisParamaters = (Vector) action
						.getCommandData();
				LOG.print(Utils
						.getCompanyLatestTradingDetailsURL(companyStatisParamaters
								.elementAt(0).toString()));
				HomeJsonParser companyStaticsJsonParser = new HomeJsonParser(
						Utils.getCompanyLatestTradingDetailsURL(companyStatisParamaters
								.elementAt(0).toString()),
						actionCompanyStaticsBannersData,
						((ThreadedComponents) companyStatisParamaters
								.elementAt(1)));
				companyStaticsJsonParser.getScreenData();
				break;
			case ActionCommand.CMD_COMPANY_INTRA_CHART:
				Vector companyIntraChartParams = (Vector) action
						.getCommandData();
				ActionInvoker actionCDChartInvoker = new ActionInvoker(
						ActionCommand.CMD_COMPANY_INTRA_CHART);
				ChartItemParser chartCompanyDataParser = new ChartItemParser(
						Utils.getCompanyChartData(companyIntraChartParams
								.elementAt(0).toString()),
						actionCDChartInvoker,
						(ThreadedComponents) companyIntraChartParams
								.elementAt(1), AppConstants.INTRADAY);
				chartCompanyDataParser.getScreenData();
				break;
			case ActionCommand.CMD_COMPANY_DETAILS_SCREEN:
			case ActionCommand.CMD_COMPANY_DETAILS_SCREEN_NSE:
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {

						if (UiApplication.getUiApplication().getActiveScreen() instanceof PopupScreen) {

							UiApplication.getUiApplication().popScreen(
									UiApplication.getUiApplication()
											.getActiveScreen());
						}
						if (UiApplication.getUiApplication().getActiveScreen() instanceof CompanyDetailsSnippetsScreen) {

							UiApplication.getUiApplication().popScreen(
									UiApplication.getUiApplication()
											.getActiveScreen());
						}
						Vector companyDetailsScreen = (Vector) action
								.getCommandData();
						CompanyDetailsSnippetsScreen companyDetailsSnippetsScreen;

						boolean flag = true;
						if (action.getCommand() == ActionCommand.CMD_COMPANY_DETAILS_SCREEN) {
							
							
							
							companyDetailsSnippetsScreen = new CompanyDetailsSnippetsScreen(
									0, companyDetailsScreen.elementAt(0)
											.toString(), companyDetailsScreen
											.elementAt(2).toString(),
									companyDetailsScreen.elementAt(1)
											.toString());
							LOG.print("companyDetailsScreen.elementAt(0).toString() : "
									+ companyDetailsScreen.elementAt(0)
											.toString());
							LOG.print("companyDetailsScreen.elementAt(1).toString() : "
									+ companyDetailsScreen.elementAt(1)
											.toString());
//							Dialog.alert("companyDetailsScreen.elementAt(2).toString() : "
//									+ companyDetailsScreen.elementAt(2)
//											.toString());

						} else {
							String str = companyDetailsScreen.elementAt(1)
									.toString();
							LOG.print(":>>str " + str);
							if (str == null)
								str = Utils.NSE_SYMBOL;
							else if (str.equalsIgnoreCase("null"))
								str = Utils.NSE_SYMBOL;
							str = str.trim();
							/*
							 * else Utils.NSE_SYMBOL = str;
							 */
							/*
							 * if(str.length()==0) str =
							 * companyDetailsScreen.elementAt(0).toString();
							 */

							companyDetailsScreen.setElementAt(str, 1);
//							Dialog.alert("here  5   "
//									+ companyDetailsScreen.elementAt(0)
//											.toString());
//							Dialog.alert("here  5   "
//									+ companyDetailsScreen.elementAt(2)
//											);
//							Dialog.alert("here  5   " + str);
							companyDetailsSnippetsScreen = new CompanyDetailsSnippetsScreen(
									1, companyDetailsScreen.elementAt(0)
											.toString(), companyDetailsScreen
											.elementAt(2).toString(), str);

							// LOG.print("companyDetailsScreen.elementAt(0).toString() : "
							// + companyDetailsScreen.elementAt(0)
							// .toString());
							// LOG.print("companyDetailsScreen.elementAt(1).toString() : "
							// + str);
							//
							// LOG.print("companyDetailsScreen.elementAt(2).toString() : "
							// + companyDetailsScreen.elementAt(2)
							// .toString());
						}
						if (flag) {

							Vector companyStaticsCommand = new Vector();
							companyStaticsCommand
									.addElement(companyDetailsScreen.elementAt(
											1).toString());
							LOG.print("last data"
									+ companyStaticsCommand.elementAt(0)
											.toString());
							String text = companyStaticsCommand.elementAt(0)
									.toString();
							if (text.indexOf("&") > -1) {
								// text = text.substring(0, text.indexOf("&")) +
								// "%26"
								// +text.substring(text.indexOf("&")+1,text.length());
								text = URLEncode.getString(text);
								companyStaticsCommand.removeElementAt(0);
								companyStaticsCommand.insertElementAt(text, 0);
							}
							/*
							 * if(UiApplication.getUiApplication().getActiveScreen
							 * () instanceof CompanyDetailsSnippetsScreen &&
							 * !Utils.STOCK_PAGE_START)
							 * UiApplication.getUiApplication
							 * ().popScreen(UiApplication
							 * .getUiApplication().getActiveScreen()); else
							 * Utils.STOCK_PAGE_START = false;
							 */

							companyStaticsCommand
									.addElement((ThreadedComponents) companyDetailsSnippetsScreen);
							
							
							
							
							processCommand(new Action(
									ActionCommand.CMD_COMPANY_STATICS_BANNERS,
									companyStaticsCommand));
							// Get Company Intra Charts
							
							
							
							processCommand(new Action(
									ActionCommand.CMD_COMPANY_INTRA_CHART,
									companyStaticsCommand));
							
							
							
							
							// final CompanyDetailsSnippetsScreen cmp =
							// companyDetailsSnippetsScreen;
							// UiApplication.getUiApplication().invokeLater(new
							// Runnable() {
							// public void run() {
							// if(UiApplication.getUiApplication().getActiveScreen()
							// instanceof CompanyDetailsSnippetsScreen)
							// UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());

							ScreenInvoker.pushScreen(
									companyDetailsSnippetsScreen, true, true);
							// UiApplication.getUiApplication().pushScreen(companyDetailsSnippetsScreen);
						}
					}
				});
				break;

			case ActionCommand.CMD_NIFTY_COMPANY_DETAILS_SCREEN:
				Vector niftycompanyDetailsScreen = (Vector) action
						.getCommandData();
				NIFTYCompanyDetailsSnippetsScreen niftycompanyDetailsSnippetsScreen;
				boolean flagnifty = true;
				if (action.getCommand() == ActionCommand.CMD_NIFTY_COMPANY_DETAILS_SCREEN) {
					niftycompanyDetailsSnippetsScreen = new NIFTYCompanyDetailsSnippetsScreen(
							1, niftycompanyDetailsScreen.elementAt(0)
									.toString(), niftycompanyDetailsScreen
									.elementAt(2).toString(),
							niftycompanyDetailsScreen.elementAt(1).toString());
					LOG.print("companyDetailsScreen.elementAt(0).toString() : "
							+ niftycompanyDetailsScreen.elementAt(0).toString());
					LOG.print("companyDetailsScreen.elementAt(1).toString() : "
							+ niftycompanyDetailsScreen.elementAt(1).toString());
					LOG.print("companyDetailsScreen.elementAt(2).toString() : "
							+ niftycompanyDetailsScreen.elementAt(2).toString());

					/*
					 * niftycompanyDetailsSnippetsScreen = new
					 * NIFTYCompanyDetailsSnippetsScreen
					 * (0,niftycompanyDetailsScreen
					 * .elementAt(0).toString(),niftycompanyDetailsScreen
					 * .elementAt
					 * (2).toString(),niftycompanyDetailsScreen.elementAt
					 * (1).toString());
					 * LOG.print("companyDetailsScreen.elementAt(0).toString() : "
					 * +niftycompanyDetailsScreen.elementAt(0).toString());
					 * LOG.print
					 * ("companyDetailsScreen.elementAt(1).toString() : "
					 * +niftycompanyDetailsScreen.elementAt(1).toString());
					 * LOG.print
					 * ("companyDetailsScreen.elementAt(2).toString() : "
					 * +niftycompanyDetailsScreen.elementAt(2).toString());
					 */

				} else {
					niftycompanyDetailsSnippetsScreen = new NIFTYCompanyDetailsSnippetsScreen(
							1, niftycompanyDetailsScreen.elementAt(0)
									.toString(), niftycompanyDetailsScreen
									.elementAt(2).toString(),
							niftycompanyDetailsScreen.elementAt(1).toString());
					LOG.print("companyDetailsScreen.elementAt(0).toString() : "
							+ niftycompanyDetailsScreen.elementAt(0).toString());
					LOG.print("companyDetailsScreen.elementAt(1).toString() : "
							+ niftycompanyDetailsScreen.elementAt(1).toString());
					LOG.print("companyDetailsScreen.elementAt(2).toString() : "
							+ niftycompanyDetailsScreen.elementAt(2).toString());
				}
				if (flagnifty) {
					Vector companyStaticsCommand = new Vector();
					companyStaticsCommand.addElement(niftycompanyDetailsScreen
							.elementAt(1).toString());
					LOG.print("last data"
							+ companyStaticsCommand.elementAt(0).toString());
					String text = companyStaticsCommand.elementAt(0).toString();
					if (text.indexOf("&") > -1) {
						// text = text.substring(0, text.indexOf("&")) + "%26"
						// +text.substring(text.indexOf("&")+1,text.length());
						text = URLEncode.getString(text);
						companyStaticsCommand.removeElementAt(0);
						companyStaticsCommand.insertElementAt(text, 0);
					}

					companyStaticsCommand
							.addElement((ThreadedComponents) niftycompanyDetailsSnippetsScreen);
					processCommand(new Action(
							ActionCommand.CMD_COMPANY_STATICS_BANNERS,
							companyStaticsCommand));

					ScreenInvoker.pushScreen(niftycompanyDetailsSnippetsScreen,
							true, true);
				}
				break;
			case ActionCommand.CMD_WATCHLIST_SCREEN:
				LOG.print("CMD_WATCHLIST_SCREEN PUSH");
				if (Utils.sessionAlive) {
					ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					WatchListScreen watchListScreen = new WatchListScreen(null);
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));

				}
				break;
			case ActionCommand.CMD_WL_FILL_SCREEN:
				LOG.print("CMD_WL_FILL_SCREEN ---------->>>>>>>");
				/*
				 * if(UiApplication.getUiApplication().getActiveScreen()
				 * instanceof WaitScreen) ScreenInvoker.removeRemovableScreen();
				 */
				if (Utils.sessionAlive) {
					Vector wlScreen = (Vector) action.getCommandData();
					String wl_url = (String) wlScreen.elementAt(0);
					Utils.WATCHLIST_LABEL = wl_url;
					LOG.print("Utils.WATCHLIST " + Utils.WATCHLIST);
					LOG.print("Utils.WATCHLIST_LABEL " + Utils.WATCHLIST_LABEL);
					if (!Utils.WATCHLIST) {
						Vector watchListEditCompanies = Utils
								.getWatchListedCompanyRecords();
						MyWatchListEdit myWatchListEditScreen = new MyWatchListEdit(
								watchListEditCompanies);
						ScreenInvoker.pushScreen(myWatchListEditScreen, true,
								true);
					} else {
						// ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
						// Test sendHTTPRequestWL_FILL(wl_url,Utils.WATCHLIST);
						LOG.print("Utils.getWatchListURL(UserInfo.getUserID(),wl_url) : "
								+ Utils.getWatchListURL(UserInfo.getUserID(),
										wl_url));
						new WatchListFill(Utils.getWatchListURL(
								UserInfo.getUserID(), wl_url), Utils.WATCHLIST);

					}
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));

				}

				break;
			case ActionCommand.CMD_WL_REFRESH:
			case ActionCommand.CMD_WL_SCREEN:

				if (Utils.sessionAlive) {
					LOG.print("Watchlist company count tes --------------------");
					Vector watchListCompanies = Utils
							.getWatchListedCompanyRecords();
					// String watchListFromQuote =
					// (String)action.getCommandData();
					// LOG.print("Watchlist company count tes -------------------- "+watchListCompanies.size());
					// LOG.print("Watchlist NULL -------------------- "+(Vector)action.getCommandData());

					if (watchListCompanies == null) {
						ScreenInvoker
								.showWaitScreen(AppConstants.loadingMessage);
						new WatchListPopUp(Utils.WATCHLIST_QUOTE, true);
						// LOG.print("Watchlist NULL inside -------------------- ");
						// MyWatchList myWatchListScreenQuote = new
						// MyWatchList(watchListCompanies,true);
						// ScreenInvoker.pushScreen(myWatchListScreenQuote,
						// true, true);

						// MyWatchList myWatchListScreen = new
						// MyWatchList(watchListCompanies);
						// ScreenInvoker.pushScreen(myWatchListScreen, true,
						// true);
					} else if (watchListCompanies.size() == 0) {
						MyWatchList myWatchListScreen = new MyWatchList(
								watchListCompanies);
						if (!Utils.WATCHLIST_CLOSE)
							ScreenInvoker.pushScreen(myWatchListScreen, true,
									true);
					} else {

						if (action.getCommand() == ActionCommand.CMD_WL_REFRESH) {

							// ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
							if (Utils.WATCHLIST_NAME.substring(
									Utils.WATCHLIST_NAME.length() - 3,
									Utils.WATCHLIST_NAME.length())
									.equalsIgnoreCase("NSE")
									|| Utils.WATCHLIST_NAME
											.equalsIgnoreCase("NIFTY")) {
								Vector v = new Vector();
								for (int i = 0; i < watchListCompanies.size(); i++) {
									WatchListJson wl = (WatchListJson) watchListCompanies
											.elementAt(i);
									v.addElement(wl.getSymbol());
								}
								watchListCompanies = v;
							}
							StringBuffer companyIDs = null;
							StringBuffer streamIDs = null;
							Vector streamVector = new Vector();
							LOG.print("Watchlist company count "
									+ watchListCompanies.size());
							Utils.setWatchListedCompanyRecordsData(watchListCompanies);

							for (int i = 0; i < watchListCompanies.size(); i++) {
								if (companyIDs == null) {
									companyIDs = new StringBuffer();
									companyIDs.append(watchListCompanies
											.elementAt(i).toString());
									streamIDs = new StringBuffer();
									streamIDs.append("item_"
											+ watchListCompanies.elementAt(i)
													.toString());
									streamVector.addElement(watchListCompanies
											.elementAt(i).toString());
								} else {
									companyIDs.append("|"
											+ watchListCompanies.elementAt(i)
													.toString());
									streamIDs.append(" item_"
											+ watchListCompanies.elementAt(i)
													.toString());
									streamVector.addElement(watchListCompanies
											.elementAt(i).toString());
								}
							}
							MyWatchList.streamURL = streamIDs.toString();
							MyWatchList.streamURLVector = streamVector;
							ActionInvoker watchListInvoker = new ActionInvoker(
									action.getCommand());
							/*
							 * if(Utils.WATCHLIST_NAME.substring(Utils.
							 * WATCHLIST_NAME.length()-3,
							 * Utils.WATCHLIST_NAME.length
							 * ()).equalsIgnoreCase("NSE")) {
							 * WatchListJsonParser wJSonParser = new
							 * WatchListJsonParser
							 * (Utils.getCompanyLatestTradingDetailsURL
							 * (companyIDs.toString())+"&flag=1",
							 * watchListInvoker, null);
							 * wJSonParser.getScreenData(); } else {
							 */
							HomeJsonParser watchListJSonParser = new HomeJsonParser(
									Utils.getCompanyLatestTradingDetailsURL(companyIDs
											.toString()), watchListInvoker,
									(ThreadedComponents) action
											.getCommandData());
							watchListJSonParser.getScreenData();
							/* } */
						} else {
							LOG.print(Utils.WATCHLIST_CLOSE
									+ "2Watchlist company count "
									+ watchListCompanies.size());
							LOG.print("WatchList Name : "
									+ Utils.WATCHLIST_NAME);

							/*
							 * if(*!(UiApplication.getUiApplication().
							 * getActiveScreen() instanceof MyWatchList) &&
							 * *!Utils.WATCHLIST) { return; }
							 */
							// ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
							StringBuffer companyIDs = null;
							StringBuffer streamIDs = null;
							Vector streamVector = new Vector();
							if (Utils.WATCHLIST_NAME.substring(
									Utils.WATCHLIST_NAME.length() - 3,
									Utils.WATCHLIST_NAME.length())
									.equalsIgnoreCase("NSE")
									|| Utils.WATCHLIST_NAME
											.equalsIgnoreCase("NIFTY")) {
								Vector v = new Vector();
								for (int i = 0; i < watchListCompanies.size(); i++) {
									WatchListJson wl = (WatchListJson) watchListCompanies
											.elementAt(i);
									v.addElement(wl.getSymbol());
								}
								watchListCompanies = v;
							}
							for (int i = 0; i < watchListCompanies.size(); i++) {
								if (companyIDs == null) {
									streamIDs = new StringBuffer();
									companyIDs = new StringBuffer();
									String textUrl = watchListCompanies
											.elementAt(i).toString();
									if (textUrl.indexOf("&") > -1) {
										// textUrl = textUrl.substring(0,
										// textUrl.indexOf("&")) + "%26"
										// +textUrl.substring(textUrl.indexOf("&")+1,textUrl.length());
										textUrl = URLEncode.replace(textUrl);
										watchListCompanies.removeElementAt(i);
										watchListCompanies.insertElementAt(
												textUrl, i);
									}
									companyIDs.append(watchListCompanies
											.elementAt(i).toString());
									streamIDs
											.append("item_"
													+ URLEncode
															.getString(watchListCompanies
																	.elementAt(
																			i)
																	.toString()));
									String sr = URLEncode
											.getString(watchListCompanies
													.elementAt(i).toString());
									streamVector.addElement(sr);
								} else {
									String textUrl = watchListCompanies
											.elementAt(i).toString();
									if (textUrl.indexOf("&") > -1) {
										// textUrl = textUrl.substring(0,
										// textUrl.indexOf("&")) + "%26"
										// +textUrl.substring(textUrl.indexOf("&")+1,textUrl.length());
										textUrl = URLEncode.replace(textUrl);
										watchListCompanies.removeElementAt(i);
										watchListCompanies.insertElementAt(
												textUrl, i);
									}
									companyIDs.append("|");
									companyIDs.append(watchListCompanies
											.elementAt(i).toString());
									streamIDs
											.append(" item_"
													+ URLEncode
															.getString(watchListCompanies
																	.elementAt(
																			i)
																	.toString()));
									String sr = URLEncode
											.getString(watchListCompanies
													.elementAt(i).toString());
									streamVector.addElement(sr);
								}
							}

							ActionInvoker watchListInvoker = new ActionInvoker(
									action.getCommand());
							LOG.print("----------------------"
									+ Utils.getCompanyLatestTradingDetailsURL(companyIDs
											.toString()));
							// MyWatchList.companylistUrl =
							// Utils.getCompanyLatestTradingDetailsURL(companyIDs.toString());
							MyWatchList.streamURL = streamIDs.toString();
							MyWatchList.streamURLVector = streamVector;
							LOG.print("AppConstants.NSE " + AppConstants.NSE);
							LOG.print(companyIDs.toString()
									+ "Utils.WATCHLIST_NAME "
									+ Utils.WATCHLIST_NAME);
							/*
							 * if(Utils.WATCHLIST_NAME.substring(Utils.
							 * WATCHLIST_NAME.length()-3,
							 * Utils.WATCHLIST_NAME.length
							 * ()).equalsIgnoreCase("NSE")) {
							 * WatchListJsonParser wJSonParser = new
							 * WatchListJsonParser
							 * (Utils.getCompanyLatestTradingDetailsURL
							 * (companyIDs.toString())+"&flag=1",
							 * watchListInvoker, null);
							 * wJSonParser.getScreenData(); } else
							 */
							{
								HomeJsonParser watchListJSonParser = new HomeJsonParser(
										Utils.getCompanyLatestTradingDetailsURL(companyIDs
												.toString()), watchListInvoker,
										null);
								watchListJSonParser.getScreenData();
							}
						}
					}
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}

				break;
			case ActionCommand.CMD_WL_SCREEN_EDIT_SCREEN:
				LOG.print("ActionCommand.CMD_WL_SCREEN_EDIT_SCREEN Started");
				if (Utils.sessionAlive) {
					Vector wlEdit = (Vector) action.getCommandData();
					wlEdit = (Vector) wlEdit.elementAt(1);
					MyWatchMainListEdit myWatchListMainEditScreen = new MyWatchMainListEdit(
							wlEdit);
					ScreenInvoker.pushScreen(myWatchListMainEditScreen, true,
							true);
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				break;

			case ActionCommand.CMD_WL_EDIT_SCREEN:
				LOG.print("ActionCommand.CMD_WL_EDIT_SCREEN Started");
				if (Utils.sessionAlive) {
					MyWatchListEdit myWatchListEditScreen = new MyWatchListEdit(
							Utils.getWatchListedCompanyRecordsData());
					ScreenInvoker.pushScreen(myWatchListEditScreen, true, true);
				} else {
					processCommand(new Action(
							ActionCommand.CMD_SESSION_EXPIRED, null));
				}
				// MyWatchListEdit myWatchListEditScreen = new
				// MyWatchListEdit(Utils.getWatchListedCompanyRecordsData());
				// ScreenInvoker.pushScreen(myWatchListEditScreen, true, true);
				break;
			case ActionCommand.CMD_SEARCH_SCREEN:
				if (UiApplication.getUiApplication().getActiveScreen() instanceof SearchStocks)
					;
				else if (!(UiApplication.getUiApplication().getActiveScreen() instanceof MyWatchList))
					Utils.WATCHLIST_MODE = false;
				LOG.print("Utils.WATCHLIST_MODE : " + Utils.WATCHLIST_MODE);
				SearchEFScreen searchScreen = new SearchEFScreen();
				// SearchScreen searchScreen = new SearchScreen();
				ScreenInvoker.pushScreen(searchScreen, true, true);
				break;
			case ActionCommand.CMD_SEARCH_COMPANY:
				LOG.print("Command Search ");
				LOG.print("Command Search FNO ");
				LOG.print("Command Search Equity ");
				Vector searchParameters = (Vector) action.getCommandData();

				// for(int i=0;i<searchParameters.size();i++)
				// Dialog.alert(""+(String)searchParameters.elementAt(i));

				if (searchParameters.size() == 0) {
					SearchStocks stocks = new SearchStocks(searchParameters);
					ScreenInvoker.pushScreen(stocks, true, true);
				} else {
					LOG.print("SearchType : "
							+ searchParameters.elementAt(0).toString());
					LOG.print("SearchStock : "
							+ searchParameters.elementAt(1).toString());
					LOG.print("SearchDateText : "
							+ searchParameters.elementAt(2).toString());
					ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					ActionInvoker actionSearchInvoker = new ActionInvoker(
							action.getCommand());
					// LOG.print(Utils.getCompanySearchURL(searchParameters.elementAt(1).toString()));
					SearchParser searchParser = new SearchParser(
							searchParameters.elementAt(0).toString(),
							searchParameters.elementAt(2).toString(),
							searchParameters.elementAt(3).toString(),
							Utils.getCompanySearchURL(searchParameters
									.elementAt(1).toString()),
							(FNOSearchBean) searchParameters.elementAt(4),
							actionSearchInvoker, null);
					searchParser.getScreenData();
				}
				break;

			case ActionCommand.CMD_SEARCH_COMMODITY:
				Vector commoditysearchParameters = (Vector) action
						.getCommandData();
				ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				ActionInvoker actionSearchInvoker = new ActionInvoker(
						action.getCommand());
				// LOG.print(Utils.getCompanySearchURL(searchParameters.elementAt(1).toString()));
				// Dialog.alert(Utils.urlEncode(Utils.findAndReplace(AppConstants.commoditySearchURL,
				// "##KEYWORD##",
				// commoditysearchParameters.elementAt(1).toString())));
				CommodityParser searchParser = new CommodityParser(
						commoditysearchParameters.elementAt(0).toString(),
						commoditysearchParameters.elementAt(2).toString(),
						Utils.urlEncode(Utils.findAndReplace(
								AppConstants.commoditySearchURL, "##KEYWORD##",
								commoditysearchParameters.elementAt(1)
										.toString())), actionSearchInvoker);
				searchParser.getScreenData();

				break;

			case ActionCommand.CMD_CHECK_UPDATES:
				/*
				 * new Thread(new Runnable() { public void run() { //Invoke
				 * Parser NotificationParser notificationParser = new
				 * NotificationParser(AppConstants.notificationUrl,
				 * (Screen)action.getCommandData());
				 * notificationParser.getNotificationData(); } }).start();
				 */
				break;
			case ActionCommand.CMD_EXIT:
				AppExitView appExitView = new AppExitView(
						"Do you want to exit?");
				ScreenInvoker.pushScreen(appExitView, true, true);
				break;
			case ActionCommand.CMD_APPMEMPASS:
				String strMsg = (String) ((Vector) action.getCommandData())
						.elementAt(0);
				AppDialogMemPass appDialogView = new AppDialogMemPass(strMsg);
				ScreenInvoker.pushScreenWithOutRemovingRemovables(
						appDialogView, true, true);
				break;
			case ActionCommand.CMD_TOC:
				TOCScreen toc = new TOCScreen();
				ScreenInvoker.pushScreen(toc, true, true);
				break;
			case ActionCommand.CMD_SESSION_EXPIRED:
				AppExitSessionExpired appExitSessionView = new AppExitSessionExpired(
						"");
				ScreenInvoker.pushScreen(appExitSessionView, true, true);
				break;
			}
			AutoScreenRefresherThread.startThread();
		} catch (Exception ex) {
			Debug.debug("Command : " + action.getCommand());
			Debug.debug("ActionInvoker.processCommand : " + ex.toString());
		}
	}

	public static void sendHTTPRequestWL_FILL(final String url,
			final boolean flag) {
		Thread animationThread = new Thread(new Runnable() {
			public void run() {
				try {
					String strAuthURL = Utils.getWatchListURL(
							UserInfo.getUserID(), url);
					LOG.print("HTTP URL " + strAuthURL);
					final String strResponse = HttpProcess
							.getHttpConnection(strAuthURL);

					LOG.print("HTTP RESPONSE " + strResponse);
					// final Vector result = parseAndSaveRegistrationInfo(i,
					// strResponse);
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {
								public void run() {
									if (!Utils.sessionAlive) {
										ActionInvoker
												.processCommand(new Action(
														ActionCommand.CMD_SESSION_EXPIRED,
														null));
									} else if (strResponse.length() == 0
											&& Utils.sessionAlive) {
										ScreenInvoker.removeRemovableScreen();
										Dialog.alert("No records found");
									} else if (strResponse.equals("0")) {
										Vector v = new Vector();
										Utils.setWatchListedCompanyRecords(v);
										LOG.print("Current Watchlist records"
												+ v.size());
										Action action;
										if (flag)
											action = new Action(
													ActionCommand.CMD_WL_SCREEN,
													null);
										else
											action = new Action(
													ActionCommand.CMD_WL_EDIT_SCREEN,
													null);
										ActionInvoker.processCommand(action);
									} else {
										Vector v = new Vector();

										String text = "";
										for (int x = 1; x < strResponse
												.length(); x++) {
											if (strResponse.charAt(x) == ']') {
												if (text.length() != 0) {
													v.addElement(text);
												}
												text = "";
											} else if (strResponse.charAt(x) == ',') {
												if (text.length() != 0) {
													v.addElement(text);
												}
												text = "";
											} else {
												text = text
														+ strResponse.charAt(x);
											}
										}
										Utils.setWatchListedCompanyRecords(v);
										LOG.print("Else Current Watchlist records"
												+ v.size());
										Action action;
										if (flag)
											action = new Action(
													ActionCommand.CMD_WL_SCREEN,
													null);
										else
											action = new Action(
													ActionCommand.CMD_WL_EDIT_SCREEN,
													null);

										// Action action = new
										// Action(ActionCommand.CMD_WL_SCREEN,
										// null);
										ActionInvoker.processCommand(action);
									}

								}
							});
				} catch (Exception ex) {
					// ScreenInvoker.removeRemovableScreen();
					// Dialog.alert("Error occured during Registration Process");
				}
			}
		});
		animationThread.start();
	}

	public void setData(final Vector vector,
			ThreadedComponents threadedComponents) {
		try {
			if (UiApplication.getUiApplication().getActiveScreen() instanceof GridScreen) {
				return;
			}
			switch (dataForCommand) {
			case ActionCommand.CMD_NEWS_SCREEN:
				int ageNo = Integer.parseInt(vector.elementAt(0).toString());
				NewsScreen newsScreen = new NewsScreen("Market News",
						(Vector) vector.elementAt(1), ageNo);
				ScreenInvoker.pushScreen(newsScreen, true, true);
				break;
			case ActionCommand.CMD_TRADE_VIEW_SCREEN:
				int trageNo = Integer.parseInt(vector.elementAt(0).toString());
				NewsScreen trScreen = new NewsScreen("News",
						(Vector) vector.elementAt(1), trageNo);
				sendHTTPRequestNEWS(AppConstants.bannersDataProvideUrl,
						trScreen);
				// ScreenInvoker.pushScreen(newsScreen, true, true);
				break;

			case ActionCommand.CMD_NEWS_PAGER:
				UiApplication.getUiApplication().invokeAndWait(new Runnable() {
					public void run() {
						ScreenInvoker.removeRemovableScreen();
					}
				});
				threadedComponents.componentsDataPrepared(
						NewsScreen.NEWS_SECTION, vector);
				break;
			case ActionCommand.CMD_COMPANY_INTRA_CHART:
				LOG.print("Intra Chart Response");
				Vector dataVector = threadedComponents.getComponentData();
				if (dataVector.size() == 0) {
					threadedComponents
							.componentsPrepared(
									CompanyDetailsSnippetsScreen.COMPONENT_COMPANY_CHART,
									new LoadingComponent(
											"Graph data not found!",
											AppConstants.screenWidth,
											ChartProperties
													.getDefaultChartProperties()
													.getChartHeight()
													+ ChartProperties
															.getDefaultChartProperties()
															.getChartxAxisHeight()
													* 2, false));
				} else {
					ChartEngine chartCDComponent = ChartEngine
							.getInstance(
									ChartProperties.getDefaultChartProperties(),
									dataVector, false,
									ChartEngine.CHART_COMPANY_DETAIL);
					threadedComponents
							.componentsPrepared(
									CompanyDetailsSnippetsScreen.COMPONENT_COMPANY_CHART,
									chartCDComponent.createChart(vector, Double
											.parseDouble(vector.elementAt(
													vector.size() - 3)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 2)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 1)
													.toString())));
				}
				/*
				 * if(threadedComponents.getComponentData().size()!=0) { Vector
				 * dataVector = threadedComponents.getComponentData();
				 * LOG.print(" Chart Data Size "+dataVector.size()); ChartEngine
				 * chartCDComponent =
				 * ChartEngine.getInstance(ChartProperties.getDefaultChartProperties
				 * (),dataVector,false,ChartEngine.CHART_COMPANY_DETAIL);
				 * threadedComponents
				 * .componentsPrepared(CompanyDetailsSnippetsScreen
				 * .COMPONENT_COMPANY_CHART,
				 * chartCDComponent.createChart(vector,
				 * Double.parseDouble(vector.elementAt(vector.size() -
				 * 3).toString()),
				 * Double.parseDouble(vector.elementAt(vector.size() -
				 * 2).toString
				 * ()),Double.parseDouble(vector.elementAt(vector.size() -
				 * 1).toString()))); }
				 */
				break;
			case ActionCommand.CMD_BSE_GET_GRAPH_DATA:
				if (vector.size() != 0) {
					Vector BseGraphData = new Vector();
					BseGraphData.addElement("BSE Intraday Chart");
					ChartEngine chartBseComponent = ChartEngine.getInstance(
							ChartProperties.getBSEChartProperties(),
							BseGraphData, true, ChartEngine.CHART_BSE_NSE);
					threadedComponents.componentsPrepared(
							HomeScreen.COMPONENT_BSE_CHART, chartBseComponent
									.createChart(vector, Double
											.parseDouble(vector.elementAt(
													vector.size() - 3)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 2)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 1)
													.toString())));
				}
				break;
			case ActionCommand.CMD_NSE_GET_GRAPH_DATA:
				if (vector.size() != 0) {
					Vector NseGraphData = new Vector();
					NseGraphData.addElement("NSE Intraday Chart");
					ChartEngine chartNseComponent = ChartEngine.getInstance(
							ChartProperties.getNSEChartProperties(),
							NseGraphData, false, ChartEngine.CHART_BSE_NSE);
					threadedComponents.componentsPrepared(
							NiftyHomeScreen.COMPONENT_NSE_CHART,
							chartNseComponent.createChart(
									vector,
									Double.parseDouble(vector.elementAt(
											vector.size() - 3).toString()),
									Double.parseDouble(vector.elementAt(
											vector.size() - 2).toString()),
									Double.parseDouble(vector.elementAt(
											vector.size() - 1).toString())));
				}
				break;
			case ActionCommand.CMD_BANNERS_HOME_SCREEN:
				threadedComponents.componentsDataPrepared(
						HomeScreen.BANNERS_DATA, vector);
				break;

			case ActionCommand.CMD_COMMODITY_DATA:
				// threadedComponents.componentsDataPrepared(
				// HomeScreen.BANNERS_DATA, vector);

				// Vector vect=(Vector) action.getCommandData();

				CommodityDetailsSnippetScreen fno = new CommodityDetailsSnippetScreen(
						(HomeJson) vector.elementAt(0), -1, true,
						commodityChartUrl, exchange, symbol);

				break;

			case ActionCommand.CMD_COMPANY_STATICS_BANNERS:
				LOG.print("Company banners data : Vector Size = "
						+ vector.size());
				threadedComponents.componentsDataPrepared(
						HomeScreen.BANNERS_DATA, vector);
				break;
			case ActionCommand.CMD_BSE_GL_SCREEN:
			case ActionCommand.CMD_NSE_GL_SCREEN:
			case ActionCommand.CMD_GLOBAL_GL_SCREEN:
				if (threadedComponents == null) {
					TopGLScreen topBseGLScreen = new TopGLScreen(vector,
							(byte) 0);
					ScreenInvoker.pushScreen(topBseGLScreen, true, true);
				} else {
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {
								public void run() {
									ScreenInvoker.removeRemovableScreen();
								}
							});
					threadedComponents.componentsDataPrepared(dataForCommand,
							vector);
				}
				break;
			case ActionCommand.CMD_FUTURE_SCREEN:
			case ActionCommand.CMD_OPTION_SCREEN:
				LOG.print("futures and options inside setData");
				if (threadedComponents == null) {
					final Vector vc = vector;
					LOG.print("futures and options thread null");
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {
								public void run() {
									FutureOptionScreen topFNOScreen = new FutureOptionScreen(
											vc, (byte) 0);
									ScreenInvoker.pushScreen(topFNOScreen,
											true, true);
								}
							});
					LOG.print("futures and options screen push true");
				} else {
					LOG.print("futures and options thread not null");
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {
								public void run() {
									ScreenInvoker.removeRemovableScreen();
								}
							});
					threadedComponents.componentsDataPrepared(dataForCommand,
							vector);
				}
				break;
			case ActionCommand.CMD_WL_SCREEN:
				LOG.print("need false MyWatchList.isFINISHED : "
						+ MyWatchList.isFINISHED);
				final Vector vecwl = vector;
				if (!MyWatchList.isFINISHED) {
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {
								public void run() {
									MyWatchList myWatchListScreen = new MyWatchList(
											vecwl);
									LOG.print("need f44444alse MyWatchList.isFINISHED : "
											+ MyWatchList.isFINISHED);
									Utils.setWatchListedCompanyRecordsData(vecwl);
									LOG.print("need false MyWatchList.is5555FINISHED : "
											+ MyWatchList.isFINISHED);
									ScreenInvoker.pushScreen(myWatchListScreen,
											true, true);
								}
							});
				} else {
					MyWatchList.isFINISHED = false;
					MyWatchList.REFRESH = false;
				}
				break;

			/*
			 * case ActionCommand.CMD_WL_EDIT_SCREEN: MyWatchListEdit
			 * myWatchListEditScreen = new MyWatchListEdit(vector);
			 * ScreenInvoker.pushScreen(myWatchListEditScreen, true, true);
			 * break;
			 */
			case ActionCommand.CMD_COMPANY_FULL_INTRA_CHART:
				LOG.print("Chart Response true");
				if (vector.size() != 0) {
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {
								public void run() {
									ScreenInvoker.removeRemovableScreen();
								}
							});
					ChartEngine chartCompanyFullIntraChart = ChartEngine
							.getInstance(ChartProperties
									.getFullScreenChartProperties(), null,
									false, ChartEngine.CHART_BSE_NSE);
					threadedComponents.componentsPrepared(
							(byte) dataForCommand, chartCompanyFullIntraChart
									.createChart(vector, Double
											.parseDouble(vector.elementAt(
													vector.size() - 3)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 2)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 1)
													.toString())));
				}
				break;
			case ActionCommand.CMD_COMPANY_FULL_WEEK_CHART:
				if (vector.size() != 0) {
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {
								public void run() {
									ScreenInvoker.removeRemovableScreen();
								}
							});
					ChartEngine chartCompanyFullWeekChart = ChartEngine
							.getInstance(ChartProperties
									.getFullScreenChartProperties(), null,
									false, ChartEngine.CHART_WEEK_CHART);
					threadedComponents.componentsPrepared(
							(byte) dataForCommand, chartCompanyFullWeekChart
									.createChart(vector, Double
											.parseDouble(vector.elementAt(
													vector.size() - 3)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 2)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 1)
													.toString())));
				}
				break;
			case ActionCommand.CMD_COMPANY_FULL_MONTH_CHART:
				if (vector.size() != 0) {
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {

								public void run() {
									ScreenInvoker.removeRemovableScreen();
								}
							});
					ChartEngine chartCompanyFullMonthChart = ChartEngine
							.getInstance(ChartProperties
									.getFullScreenChartProperties(), null,
									false, ChartEngine.CHART_BSE_NSE);
					threadedComponents.componentsPrepared(
							(byte) dataForCommand, chartCompanyFullMonthChart
									.createChart(vector, Double
											.parseDouble(vector.elementAt(
													vector.size() - 3)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 2)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 1)
													.toString())));
				}
				break;
			case ActionCommand.CMD_COMPANY_FULL_6MONTH_CHART:
				if (vector.size() != 0) {
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {
								public void run() {
									ScreenInvoker.removeRemovableScreen();
								}
							});
					ChartEngine chartCompanyFull6MonthChart = ChartEngine
							.getInstance(ChartProperties
									.getFullScreenChartProperties(), null,
									false, ChartEngine.CHART_BSE_NSE);
					threadedComponents.componentsPrepared(
							(byte) dataForCommand, chartCompanyFull6MonthChart
									.createChart(vector, Double
											.parseDouble(vector.elementAt(
													vector.size() - 3)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 2)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 1)
													.toString())));
				}
				break;
			case ActionCommand.CMD_COMPANY_FULL_12MONTH_CHART:
				if (vector.size() != 0) {
					UiApplication.getUiApplication().invokeAndWait(
							new Runnable() {
								public void run() {
									ScreenInvoker.removeRemovableScreen();
								}
							});
					ChartEngine chartCompanyFull12MonthChart = ChartEngine
							.getInstance(ChartProperties
									.getFullScreenChartProperties(), null,
									false, ChartEngine.CHART_BSE_NSE);
					threadedComponents.componentsPrepared(
							(byte) dataForCommand, chartCompanyFull12MonthChart
									.createChart(vector, Double
											.parseDouble(vector.elementAt(
													vector.size() - 3)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 2)
													.toString()), Double
											.parseDouble(vector.elementAt(
													vector.size() - 1)
													.toString())));
				}
				break;
			case ActionCommand.CMD_WL_REFRESH:
				Utils.setWatchListedCompanyRecordsData(vector);
				UiApplication.getUiApplication().invokeAndWait(new Runnable() {
					public void run() {
						ScreenInvoker.removeRemovableScreen();
					}
				});
				threadedComponents.componentsDataPrepared(
						(byte) ActionCommand.CMD_WL_REFRESH, vector);
				break;
			case ActionCommand.CMD_SEARCH_COMPANY:
				/*
				 * UiApplication.getUiApplication().invokeAndWait(new Runnable()
				 * { public void run() { ScreenInvoker.removeRemovableScreen();
				 * } }); threadedComponents.componentsDataPrepared((byte)0,
				 * vector);
				 */
				LOG.print("Before SearchStocks");
				SearchStocks stocks = new SearchStocks(vector);
				ScreenInvoker.pushScreen(stocks, true, true);
				break;

			case ActionCommand.CMD_SEARCH_COMMODITY:
				/*
				 * UiApplication.getUiApplication().invokeAndWait(new Runnable()
				 * { public void run() { ScreenInvoker.removeRemovableScreen();
				 * } }); threadedComponents.componentsDataPrepared((byte)0,
				 * vector);
				 */
				LOG.print("Before SearchStocks");

				SearchCommodity commoditystocks = new SearchCommodity(vector);

				ScreenInvoker.pushScreen(commoditystocks, true, true);
				break;

			case ActionCommand.CMD_FNO_TRADE:
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						if (vector != null) {
							if (vector.size() > 0) {

								HomeJson bannerData = null;
								if (vector.size() > 2) {
									bannerData = (HomeJson) vector.elementAt(2);
								}
								String date = (String) vector.elementAt(vector
										.size() - 1);
								ScreenInvoker.removeRemovableScreen();
								while (true) {
									if (!(UiApplication.getUiApplication()
											.getActiveScreen() instanceof FNOTradeScreen)) {
										try {
											final FNOTradeScreen fnoTradeScreen1 = new FNOTradeScreen(
													(FNOTradeBean) vector
															.elementAt(0),
													"Place Order",
													(FNOTradeConfiguration) vector
															.elementAt(1),
													bannerData, date);
											UiApplication
													.getUiApplication()
													.pushScreen(fnoTradeScreen1);
											break;
										} catch (Exception e) {
											try {
												Thread.sleep(10);
											} catch (InterruptedException e1) {
											}
										}
									}

								}
							}
						}
					}
				});
				break;
			case ActionCommand.CMD_FNO_TRADE_ORDER_CONFIRMATION:
			case ActionCommand.CMD_FNO_TRADE_FRESH_ORDER_CONFIRMATION:
				if (vector != null) {
					if (vector.size() > 0) {

						FNOOrderConfirmationBean tradeOrderConfirmationBean = (FNOOrderConfirmationBean) vector
								.elementAt(0);
						if (dataForCommand == ActionCommand.CMD_FNO_TRADE_FRESH_ORDER_CONFIRMATION)
							tradeOrderConfirmationBean.setFreshOrder(true);
						FNOTradeOrderConfirmationScreen fnoTradeOrderScreen = new FNOTradeOrderConfirmationScreen(
								"Order Confirmation",
								tradeOrderConfirmationBean);

						if (tradeOrderConfirmationBean.getPage() != null) {
							if (tradeOrderConfirmationBean.getPage().trim()
									.toLowerCase().indexOf("turnover") != -1) {
								ScreenInvoker
										.pushScreenWithOutRemovingRemovables(
												fnoTradeOrderScreen, true, true);
							} else {
								ScreenInvoker
										.pushScreenByRemovingFirstRemovable(
												fnoTradeOrderScreen, true, true);
							}
						} else {
							ScreenInvoker.pushScreenByRemovingFirstRemovable(
									fnoTradeOrderScreen, true, true);
						}
					}
				}
				break;
			case ActionCommand.CMD_FNO_TRADE_ORDER_CONFIRM_POST:
				if (vector != null) {
					if (vector.size() > 0) {

						FNOTradeOrderConfirmBean fnoTradeOrderConfirmBean = (FNOTradeOrderConfirmBean) vector
								.elementAt(0);

						FNOTradeOrderNumberViewScreen fnoTradeOrderNumberViewScreen = new FNOTradeOrderNumberViewScreen(
								"Order Result", fnoTradeOrderConfirmBean);
						ScreenInvoker.pushScreen(fnoTradeOrderNumberViewScreen,
								true, true);
					}
				}
				break;
			case ActionCommand.CMD_FNO_TRADE_TURNOVER_CONFIRMATION:
				if (vector != null) {
					if (vector.size() > 0) {

						FNOOrderConfirmationBean tradeOrderConfirmationBean = (FNOOrderConfirmationBean) vector
								.elementAt(0);

						FNOTradeTurnOverConfirmationScreen fnoTradeOrderScreen = new FNOTradeTurnOverConfirmationScreen(
								"Order Confirmation",
								tradeOrderConfirmationBean);

						if (tradeOrderConfirmationBean.getPage() != null) {
							if (tradeOrderConfirmationBean.getPage().trim()
									.toLowerCase().indexOf("turnover") != -1) {
								ScreenInvoker
										.pushScreenWithOutRemovingRemovables(
												fnoTradeOrderScreen, true, true);
							} else {
								ScreenInvoker
										.pushScreenByRemovingFirstRemovable(
												fnoTradeOrderScreen, true, true);
							}
						} else {
							ScreenInvoker.pushScreenByRemovingFirstRemovable(
									fnoTradeOrderScreen, true, true);
						}
					}
				}
				break;
			case ActionCommand.CMD_FNO_TRADE_ORDER_DETAILS:
				if (vector != null) {
					if (vector.size() > 0) {

						FNOOrderConfirmationBean fnoOrderConfirmationBean = (FNOOrderConfirmationBean) vector
								.elementAt(0);
						FNOTradeOrderConfirmBean fnoTradeOrderConfirmBean = (FNOTradeOrderConfirmBean) vector
								.elementAt(1);

						HomeJson bannerData = null;
						if (vector.size() > 2) {

							bannerData = (HomeJson) vector.elementAt(2);
						}

						FNOTradeConfiguration fnoTradeConfiguration = new FNOTradeConfiguration();
						if (fnoOrderConfirmationBean.getInstType().trim()
								.indexOf("FUT") == 0) {
							fnoTradeConfiguration
									.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX);
						} else if (fnoOrderConfirmationBean.getInstType()
								.trim().indexOf("OPT") == 0) {
							fnoTradeConfiguration
									.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX);
						}

						if (fnoOrderConfirmationBean.getOrderType() != null) {
							if (fnoOrderConfirmationBean.getOrderType()
									.toString().toLowerCase().equals("market")) {
								fnoTradeConfiguration
										.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_MARKET);
							} else if (fnoOrderConfirmationBean.getOrderType()
									.toString().toLowerCase().equals("limit")) {
								fnoTradeConfiguration
										.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT);
							}
						} else {
							fnoTradeConfiguration
									.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT);
						}
						String date = (String) vector
								.elementAt(vector.size() - 1);
						FNOTradeOrderModifyScreen fnoTradeOrderModifyScreen = new FNOTradeOrderModifyScreen(
								fnoTradeOrderConfirmBean,
								fnoOrderConfirmationBean, "Place Order",
								fnoTradeConfiguration, bannerData, date);
						ScreenInvoker.pushScreen(fnoTradeOrderModifyScreen,
								true, true);
					}
				}
				break;
			case ActionCommand.CMD_FNO_TRADE_ORDER_MODIFY:
				if (vector != null) {
					if (vector.size() > 0) {
						final String message = vector.elementAt(0).toString();

						UiApplication.getUiApplication().invokeAndWait(
								new Runnable() {
									public void run() {
										ScreenInvoker.removeRemovableScreen();
										Dialog.alert(message);
									}
								});

					}
				}
				break;
			case ActionCommand.CMD_FNO_TRADE_ORDER_TURNOVER:
				if (vector != null) {
					if (vector.size() > 0) {
						FNOOrderConfirmationBean fnoOrderConfirmationBean = (FNOOrderConfirmationBean) vector
								.elementAt(0);
						FNOTradeConfiguration fnoTradeConfiguration = new FNOTradeConfiguration();
						if (fnoOrderConfirmationBean.getInstType().trim()
								.toLowerCase().indexOf("fut") == 0) {
							fnoTradeConfiguration
									.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX);

							if (fnoOrderConfirmationBean.getOrderType() != null) {
								if (fnoOrderConfirmationBean.getOrderType()
										.toString().toLowerCase()
										.equals("market")) {
									fnoTradeConfiguration
											.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_MARKET);
								} else if (fnoOrderConfirmationBean
										.getOrderType().toString()
										.toLowerCase().equals("limit")) {
									fnoTradeConfiguration
											.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT);
								}
							} else {
								fnoTradeConfiguration
										.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT);
							}

						} else if (fnoOrderConfirmationBean.getInstType()
								.trim().toLowerCase().indexOf("opt") == 0) {
							fnoTradeConfiguration
									.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX);
							fnoTradeConfiguration
									.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT);
						}

						HomeJson bannerData = null;
						if (vector.size() > 1) {
							bannerData = (HomeJson) vector.elementAt(1);
						}
						String date = (String) vector
								.elementAt(vector.size() - 1);

						FNOTradeOrderTurnOverScreen fnoTurnOverScreen = new FNOTradeOrderTurnOverScreen(
								fnoOrderConfirmationBean, "Place Order",
								fnoTradeConfiguration, bannerData, date);
						ScreenInvoker.pushScreen(fnoTurnOverScreen, true, true);
					}
				}
				break;
			}
		} catch (Exception ex) {
			Debug.debug("dataForCommand : " + dataForCommand);
			Debug.debug("ActionInvoker.setData : " + ex.toString());
		}
	}

	public void sendHTTPRequestNEWS(final String strAuthURL,
			final NewsScreen newsScreen) {
		animationThread = new Thread(new Runnable() {
			public void run() {
				try {
					// String strAuthURL =
					// Utils.getWatchListURL(i,UserInfo.getUserID());
					String strResponse = HttpProcess
							.getHttpConnection(strAuthURL);
					if (strResponse == null)
						return;
					final Vector result = HomeJsonParser.getVector(strResponse);
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {
								public void run() {
									for (byte i = 0; i < result.size(); i++) {
										HomeJson homeJson = (HomeJson) result
												.elementAt(i);
										LOG.print("News Snippet : homeJson.getCompanyCode() "
												+ homeJson.getCompanyCode());
										if (homeJson.getCompanyCode().equals(
												HomeScreen.BSE_COMPANYCODE)) {
											Utils.bseJsonStore = homeJson;
											Utils.bseJsonStore = (HomeJson) result
													.elementAt(0);
											LOG.print("News Snippet Successfully added BSE data");
											ScreenInvoker.pushScreen(
													newsScreen, true, true);
										}
									}
								}
							});
				} catch (Exception ex) {
					LOG.print("News Snippet Errors");
					ScreenInvoker.pushScreen(newsScreen, true, true);
				}
			}
		});
		animationThread.start();
	}

	/*
	 * public Vector parseAndSendNEWS(String strResponse) { Json js = new
	 * Json(strResponse); return (Vector)js.getdata.elementAt(0); }
	 */

}
