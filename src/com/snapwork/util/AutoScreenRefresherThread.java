package com.snapwork.util;

import java.util.Calendar;
import java.util.TimeZone;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.view.CommodityDetailsSnippetScreen;
import com.snapwork.view.CompanyDetailsSnippetsScreen;
import com.snapwork.view.CompanyFNODetailsSnippetsScreen;
import com.snapwork.view.CurrentStat;
import com.snapwork.view.GridScreen;
import com.snapwork.view.HomeScreen;
import com.snapwork.view.MarketDepthScreen;
import com.snapwork.view.MyWatchList;
import com.snapwork.view.NewsSnippetsScreen;
import com.snapwork.view.NiftyHomeScreen;
import com.snapwork.view.TopGLScreen;
import com.snapwork.view.trade.FNOTradeScreen;
import com.snapwork.view.trade.TradeNowMainScreen;
import com.snapwork.view.trade.TradeNowModifyScreen;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class AutoScreenRefresherThread {
	private static Thread autoScreenRefresherThread = null;
	private static int counter = 0;
	public static boolean skipme;
	public static boolean flagSet;
	public static boolean onLoad;

	public AutoScreenRefresherThread() {

	}

	public static void startThread() {
		if (autoScreenRefresherThread == null) {
			autoScreenRefresherThread = new Thread(new Runnable() {

				public void run() {
					try {
						while (true) {

							if (!Utils.MARKET_CLOSED) {
								
								
//								UiApplication.getUiApplication().invokeLater(new Runnable() {
//									
//									public void run() {
//										// TODO Auto-generated method stub
//									   Dialog.alert("gg");	
//									}
//								});
								
								
								

								if (UiApplication.getUiApplication()
										.getActiveScreen() instanceof GridScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof CurrentStat
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof NiftyHomeScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof CompanyDetailsSnippetsScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof TopGLScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof NiftyHomeScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof CompanyDetailsSnippetsScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof TopGLScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof NewsSnippetsScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof TradeNowMainScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof TradeNowModifyScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof CompanyFNODetailsSnippetsScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof MyWatchList
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof MarketDepthScreen
										|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof FNOTradeScreen
												|| UiApplication.getUiApplication()
												.getActiveScreen() instanceof CommodityDetailsSnippetScreen) {
									LOG.print("Refreshing screen---------------------------------");
									((AutoRefreshableScreen) (UiApplication
											.getUiApplication()
											.getActiveScreen()))
											.refreshFields();
								}
							}

							Thread.sleep(5000);
						}
					} catch (Exception ex) {
						LOG.print("Screen Refresher Thread Error : "
								+ ex.toString());
						autoScreenRefresherThread = null;
					}
				}
			});
			autoScreenRefresherThread.start();
		}
	}
}