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
import net.rim.device.api.ui.component.Dialog;
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
import com.snapwork.beans.Expiry;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomLinkButtonUnderLine;
import com.snapwork.components.HomeScreenBanner;
import com.snapwork.components.LoadingComponent;
import com.snapwork.components.RefreshButton;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TitleBarRefresh;
import com.snapwork.util.AppConstants;
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.Utils;

public class CurrentStat extends MainScreen implements ThreadedComponents,
		ActionListener, AutoRefreshableScreen {
	public static final byte COMPONENT_BSE_CHART = 0;
	public static final byte BANNERS_DATA = 1;

	public static final String BSE_COMPANYCODE = "17023928";
	public static final String NSE_COMPANYCODE = "17023929";

	// public static final String NSEFO_COMPANYCODE = "17023929";
	// public static final String MCX_COMPANYCODE = "17023929";
	// public static final String NCDEX_COMPANYCODE = "17023929";

	public static final String MCX_COMPANYCODE = "MCXFO";
	public static final String NCDEX_COMPANYCODE = "NCDEXFO";
	public static final String NSEFO_COMPANYCODE = "NIFTY_28-02-2013";
	private BottomMenu bottomMenu = null;
	private boolean isChartLoaded = false;
	private boolean isLoaded = false;
	private boolean refreshPressed = false;
	private int graphRefreshRate = 0;
	private long graphRefreshTime = 0;
	private Field field;
	private Hashtable hasht;

	private HomeJson forNSEFO;

	public CurrentStat() {
		// super(NO_VERTICAL_SCROLL);
		getMainManager().setBackground(
				BackgroundFactory.createSolidBackground(Color.BLACK));
		AutoScreenRefresherThread.onLoad = false;
		hasht = new Hashtable();
		// NSEFO_COMPANYCODE ="NIFTY_"+Expiry.getTextWithYear(0);
		createUI();
	}

	// Fields for the screen
	HorizontalFieldManager horizontalFieldManager = null;
	VerticalFieldManager verticalFieldManager = null;
	public HomeScreenBanner bseBanner = null;
	public HomeScreenBanner nseBanner = null;
	public HomeScreenBanner nsefoBanner = null;
	public HomeScreenBanner mcxBanner = null;
	public HomeScreenBanner ncdexBanner = null;
	LabelField lastUpdatedDateTime = null;
	private RefreshButton refreshme;
	CustomLinkButtonUnderLine refreshButton = null;
	private int blockAutoRefresh = 0;

	public void createUI() {
		refreshme = new RefreshButton() {
			protected boolean navigationClick(int arg0, int arg1) {
				isChartLoaded = true;
				AutoScreenRefresherThread.skipme = true;
				refreshFields();
				return super.navigationClick(arg0, arg1);
			}
		};
		// Sets the title
		setTitle(new TitleBarRefresh("Markets Today", refreshme));

		// VerticalFieldManager mainManager = new VerticalFieldManager(
		// Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
		// public void paintBackground(Graphics graphics) {
		// graphics.setColor(Color.BLACK);
		// graphics.fillRect(0, 0, getWidth(), getHeight());
		// }
		//
		// protected void sublayout(int maxWidth, int maxHeight) {
		//
		// super.sublayout(AppConstants.screenWidth,
		// AppConstants.screenHeight-
		// BottomMenu.getItemHeight()-TitleBarRefresh.getItemHeight());
		//
		//
		// setExtent(AppConstants.screenWidth,
		// AppConstants.screenHeight- BottomMenu.getItemHeight()
		// - TitleBarRefresh.getItemHeight());
		// //
		// }
		// };

		VerticalFieldManager mainManager = new VerticalFieldManager(
				Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			// public void paintBackground(Graphics graphics) {
			// // graphics.setColor(Color.YELLOW);
			// graphics.fillRect(0, 0, getWidth(), getHeight());
			//
			// graphics.drawText(""+AppConstants.screenWidth,
			// AppConstants.screenHeight
			// - BottomMenu.getItemHeight() - TitleBar.getItemHeight(),0, 0);
			// }

			protected void sublayout(int maxWidth, int maxHeight) {
				super.sublayout(AppConstants.screenWidth,
						AppConstants.screenHeight + 40);
				setExtent(AppConstants.screenWidth,
						AppConstants.screenHeight + 40);// -
														// BottomMenu.getItemHeight()
														// -
														// TitleBar.getItemHeight()
			}
		};
		// mainManager.setBackground(BackgroundFactory.createSolidBackground(Color.YELLOW));

		// Dialog.alert(""+(AppConstants.screenHeight
		// - BottomMenu.getItemHeight() - TitleBar.getItemHeight()));

		verticalFieldManager = new VerticalFieldManager(
				Manager.NO_VERTICAL_SCROLL

				| Manager.NO_VERTICAL_SCROLLBAR) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.GRAY);
				graphics.fillRect(0, 0, AppConstants.screenWidth,
						AppConstants.screenHeight);
			}

			// protected void sublayout(int maxWidth, int maxHeight) {
			// super.sublayout(AppConstants.screenWidth,
			// AppConstants.screenHeight);
			// // AppConstants.screenHeight - BottomMenu.getItemHeight()
			// // - TitleBarRefresh.getItemHeight());
			// }

		};

		verticalFieldManager.setMargin(0, 0, 0, 6);

		// Create Horizontal Field Manager
		horizontalFieldManager = new HorizontalFieldManager(FIELD_HCENTER
				| FOCUSABLE) {
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, AppConstants.screenWidth,
						AppConstants.screenHeight);
			}
		};

		// Create Vertical Field Manager

		// };

		bseBanner = new HomeScreenBanner(FOCUSABLE | FIELD_HCENTER, "BSE", 0) {
			protected boolean navigationClick(int status, int time) {
				Action action = new Action(ActionCommand.CMD_NIFTY_SCREEN,
						"BSE");
				ActionInvoker.extra = ActionCommand.CMD_BSE_GET_GRAPH_DATA;

				ActionInvoker.processCommand(action);

				return super.navigationClick(status, time);
			}
		};

		nseBanner = new HomeScreenBanner(FOCUSABLE | FIELD_HCENTER, "NSE", 0) {
			protected boolean navigationClick(int status, int time) {
				Action action = new Action(ActionCommand.CMD_NIFTY_SCREEN,
						"NSE");

				ActionInvoker.extra = ActionCommand.CMD_NSE_GET_GRAPH_DATA;

				ActionInvoker.processCommand(action);

				return super.navigationClick(status, time);
			}
		};

		nsefoBanner = new HomeScreenBanner(FOCUSABLE | FIELD_HCENTER, "NSEFO",
				0) {
			protected boolean navigationClick(int status, int time) {
				// Action action = new Action(ActionCommand.CMD_NIFTY_SCREEN,
				// "NSEFO");
				//
				// ActionInvoker.extra = ActionCommand.CMD_NSEFO_GET_GRAPH_DATA;
				//
				// ActionInvoker.processCommand(action);

				// Dialog.alert("here");
				CompanyFNODetailsSnippetsScreen fno = new CompanyFNODetailsSnippetsScreen(
						forNSEFO, -1, true);

				// Dialog.alert("here   1");

				// UiApplication.getUiApplication().pushScreen(fno);
				//
				// Dialog.alert("here    2");

				return super.navigationClick(status, time);
			}
		};

		mcxBanner = new HomeScreenBanner(FOCUSABLE | FIELD_HCENTER, "MCX", 0) {
			protected boolean navigationClick(int status, int time) {
				Action action = new Action(ActionCommand.CMD_NIFTY_SCREEN,
						"MCX");

				ActionInvoker.extra = ActionCommand.CMD_MCX_GET_GRAPH_DATA;

				ActionInvoker.processCommand(action);

				return super.navigationClick(status, time);
			}
		};

		ncdexBanner = new HomeScreenBanner(FOCUSABLE | FIELD_HCENTER, "NCDEX",
				0) {
			protected boolean navigationClick(int status, int time) {
				Action action = new Action(ActionCommand.CMD_NIFTY_SCREEN,
						"NCDEX");

				ActionInvoker.extra = ActionCommand.CMD_NCDEX_GET_GRAPH_DATA;

				ActionInvoker.processCommand(action);

				return super.navigationClick(status, time);
			}
		};

		lastUpdatedDateTime = new LabelField("", NON_FOCUSABLE | FIELD_RIGHT) {
			protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
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
		lastUpdatedDateTime.setFont(FontLoader
				.getFont(AppConstants.SMALL_PLAIN_FONT));

		// verticalFieldManager.add(lastUpdatedDateTime);
		String strButtonText = ("Refresh");

		refreshButton = new CustomLinkButtonUnderLine(strButtonText, FOCUSABLE
				| FIELD_LEFT | FIELD_VCENTER, 0xeeeeee,
				lastUpdatedDateTime.getFont()) {

			protected boolean navigationClick(int status, int time) {
				/*
				 * Action action = new Action(ActionCommand.CMD_HOME_SCREEN,
				 * null); ActionInvoker.processCommand(action);
				 */
				// bseBanner.reset();
				// nseBanner.reset();
				refreshPressed = true;
				refreshFields();
				return super.navigationClick(status, time);
			}

		};

		topManager.add(/* refreshButton */new NullField(NON_FOCUSABLE));
		topManager.add(lastUpdatedDateTime);

		verticalFieldManager.add(topManager);

		verticalFieldManager.add(bseBanner);
		verticalFieldManager.add(nseBanner);

		verticalFieldManager.add(nsefoBanner);
		verticalFieldManager.add(mcxBanner);
		verticalFieldManager.add(ncdexBanner);
		// verticalFieldManager.add(new LabelField("",FOCUSABLE));

		horizontalFieldManager.add(new LoadingComponent(
				AppConstants.loadingMessage, AppConstants.screenWidth,
				ChartProperties.getBSEChartProperties().getChartHeight()
						+ ChartProperties.getBSEChartProperties()
								.getChartxAxisHeight() * 2));

		// verticalFieldManager.add(horizontalFieldManager);

		mainManager.add(verticalFieldManager);
		add(mainManager);
		bottomMenu = BottomMenu.getBottomMenuInstance(this,
				ImageManager.getBottomMenuImages(true),
				ImageManager.getBottomMenuImages(false),
				ActionCommand.CMD_CURRENTSTAT, AppConstants.bottomMenuCommands);

	}

	public void componentsPrepared(byte componentID, final Object component) {

		switch (componentID) {
		case COMPONENT_BSE_CHART:
			/*
			 * if(field != null) { field = (Field)component; isChartLoaded =
			 * true; graphRefreshTime = System.currentTimeMillis(); return; }
			 */
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					if (component == null) {
						horizontalFieldManager.deleteAll();
					} else {
						horizontalFieldManager.deleteAll();
						horizontalFieldManager.add((Field) component);
						field = (Field) component;
					}
					// isChartLoaded = true;
					if (!isLoaded) {
						UiApplication.getUiApplication().invokeAndWait(
								new Runnable() {
									public void run() {
										ScreenInvoker.removeRemovableScreen();
									}
								});
						isLoaded = true;
					}

					// isChartLoaded = true;
					graphRefreshTime = System.currentTimeMillis();
				}

			});
			break;
		}
	}

	// http://mtrade.sharekhan.com/getQuote_sample.php?companylist=17023928|17023929

	public void componentsDataPrepared(byte componentID, final Object data) {
		switch (componentID) {
		case BANNERS_DATA:
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					LOG.print("HOME SCREEN componentsDataPrepared ");
					Vector vector = (Vector) data;
					LOG.print("BSE Banner Vector Size " + vector.size());

					for (byte i = 0; i < vector.size(); i++) {
						HomeJson homeJson = (HomeJson) vector.elementAt(i);
						if (homeJson.getCompanyCode().equals(BSE_COMPANYCODE)) {
							Utils.bseJsonStore = homeJson;
							lastUpdatedDateTime.setText("Last Updated : "
									+ homeJson.getLastTradedTime());
							/*
							 * if(hasht.get(BSE_COMPANYCODE)!=null) { int stream
							 * = 0; double dx ; double dx2 ; try { dx =
							 * Double.parseDouble
							 * ((String)hasht.get(BSE_COMPANYCODE)); dx2 =
							 * Double
							 * .parseDouble((String)homeJson.getLastTradedPrice
							 * ()); if(dx<dx2) stream = 1; else if(dx2<dx)
							 * stream = -1; } catch (NumberFormatException e) {
							 * stream = 0; } bseBanner.setStream(stream); //test
							 * if(bseBanner.getStream() == 0)
							 * bseBanner.setStream(1); else
							 * if(bseBanner.getStream() == 1)
							 * bseBanner.setStream(-1); else
							 * if(bseBanner.getStream() == -1)
							 * bseBanner.setStream(0); //test } else
							 * hasht.put(BSE_COMPANYCODE
							 * ,homeJson.getLastTradedPrice());
							 */
							bseBanner.setStartFlag(false);
							bseBanner.setData(homeJson.getLastTradedPrice(),
									homeJson.getChange(),
									homeJson.getPercentageDiff(),
									homeJson.getA(), homeJson.getD(),
									homeJson.getS());
							bseBanner.startUpdate();

							// doPaint();
						} else if (homeJson.getCompanyCode().equals(
								NSE_COMPANYCODE)) {
							/*
							 * if(hasht.get(NSE_COMPANYCODE)!=null) { int
							 * stream; double dx ; double dx2 ; try { dx =
							 * Double
							 * .parseDouble((String)hasht.get(NSE_COMPANYCODE));
							 * String str =
							 * (String)homeJson.getLastTradedPrice();
							 * LOG.print("pstr "+str); while(true) {
							 * if(str.indexOf(",")>-1) { str = str.substring(0,
							 * str.indexOf(",")) +
							 * str.substring(str.indexOf(",")+1, str.length());
							 * } else break; } LOG.print("nstr "+str); dx2 =
							 * Double.parseDouble(str);
							 * LOG.print("dx="+dx+" : dx2"+dx2); stream = 0;
							 * if(dx<dx2) stream = 1; else if(dx2<dx) stream =
							 * -1; LOG.print("stream "+stream);
							 * hasht.put(NSE_COMPANYCODE,str);
							 * nseBanner.setStream(stream); } catch
							 * (NumberFormatException e) {
							 * LOG.print("Error NIfTY stream");
							 * nseBanner.setStream(0); }
							 * 
							 * } else { String strm = (String)
							 * homeJson.getLastTradedPrice(); while(true) {
							 * if(strm.indexOf(",")>-1) { strm =
							 * strm.substring(0, strm.indexOf(",")) +
							 * strm.substring(strm.indexOf(",")+1,
							 * strm.length()); } else break; }
							 * hasht.put(NSE_COMPANYCODE,strm); }
							 */
							nseBanner.setStartFlag(false);
							nseBanner.setData(homeJson.getLastTradedPrice(),
									homeJson.getChange(),
									homeJson.getPercentageDiff(),
									homeJson.getA(), homeJson.getD(),
									homeJson.getS());
							nseBanner.startUpdate();

						} else if (homeJson.getCompanyCode().equals(
								MCX_COMPANYCODE)) {
							mcxBanner.setStartFlag(false);
							mcxBanner.setData(homeJson.getLastTradedPrice(),
									homeJson.getChange(),
									homeJson.getPercentageDiff(),
									homeJson.getA(), homeJson.getD(),
									homeJson.getS());
							mcxBanner.startUpdate();
						} else if (homeJson.getCompanyCode().equals(
								NCDEX_COMPANYCODE)) {
							ncdexBanner.setStartFlag(false);
							ncdexBanner.setData(homeJson.getLastTradedPrice(),
									homeJson.getChange(),
									homeJson.getPercentageDiff(),
									homeJson.getA(), homeJson.getD(),
									homeJson.getS());
							ncdexBanner.startUpdate();
						} else if (homeJson.getCompanyCode().equals(
								NSEFO_COMPANYCODE)) {
							nsefoBanner.setStartFlag(false);
							nsefoBanner.setData(homeJson.getLastTradedPrice(),
									homeJson.getChange(),
									homeJson.getPercentageDiff(),
									homeJson.getA(), homeJson.getD(),
									homeJson.getS());
							nsefoBanner.startUpdate();

							forNSEFO = homeJson.copy();

						}
						/*
						 * if(field!=null) { horizontalFieldManager.deleteAll();
						 * horizontalFieldManager.add(field); }
						 */
					}
					// Dialog.alert("here");
					if (!isLoaded) {
						UiApplication.getUiApplication().invokeAndWait(
								new Runnable() {
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
		switch (Command) {
		case ActionCommand.CMD_BANNERS_HOME_SCREEN:
		case ActionCommand.CMD_BSE_GET_GRAPH_DATA:
			ActionInvoker.processCommand(new Action(Command,
					(ThreadedComponents) this));
			break;
		default:
			ActionInvoker.processCommand(new Action(Command, sender));
			break;
		}
	}

	public boolean onMenu(int instance) {
		return false;
	}

	protected void makeMenu(Menu menu, int instance) {
		ContextMenu contextMenu = ContextMenu.getInstance();
		contextMenu.setTarget(this);
		contextMenu.clear();
		this.makeContextMenu(contextMenu);
		menu.deleteAll();
		menu.add(contextMenu);
	}

	/*
	 * public void makeContextMenu(ContextMenu contextMenu) {
	 * contextMenu.addItem(YourMenuItem);
	 * 
	 * }
	 */

	public boolean keyChar(char key, int status, int time) {
		return false;
	}

	public boolean keyDown(int keyCode, int time) {

		int key = Keypad.key(keyCode);
		if (key == Keypad.KEY_MENU) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					try {
						if (bottomMenu != null)
							bottomMenu.autoAttachDetachFromScreen();
					} catch (Exception e) {
					}
				}
			});
		} else if (key == Keypad.KEY_END) {
			LOG.print("KEY_END EXIT from app");
			System.exit(0);
		}

		else
			return super.keyDown(keyCode, time);

		return true;
	}

	public boolean keyUp(int keyCode, int time) {
		return super.keyUp(keyCode, time);
	}

	public Vector getComponentData() {
		return null;
	}

	public void refreshFields() {
		if (blockAutoRefresh == 1) {
			blockAutoRefresh--;
			return;
		} else if (blockAutoRefresh == 2)
			blockAutoRefresh--;
		if (bseBanner.isBlockLoaded() == true
				&& nseBanner.isBlockLoaded() == true) {
			refreshme.setLoading(true);
			/*
			 * UiApplication.getUiApplication().invokeLater(new Runnable() {
			 * public void run() { if(refreshPressed) { bseBanner.reset();
			 * nseBanner.reset(); refreshPressed = false; }
			 * actionPerfomed(ActionCommand.CMD_BANNERS_HOME_SCREEN, null); }
			 * });
			 */
			// bseBanner.reset();
			// nseBanner.reset();
			refreshPressed = false;
			actionPerfomed(ActionCommand.CMD_BANNERS_HOME_SCREEN, null);

			// UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			//
			// public void run() {
			// // TODO Auto-generated method stub
			// Dialog.alert("here");
			// }
			// });

		} else {
			refreshPressed = false;
			refreshme.setLoading(false);
		}
		LOG.print("isChartLoaded............." + isChartLoaded);
		LOG.print("graphRefreshRate............." + graphRefreshRate);
		LOG.print("ChartComponent.isDrawFinished............."
				+ ChartComponent.isDrawFinished);
		// if(graphRefreshRate==5){
		// graphRefreshRate = 0;
		if (isChartLoaded == true /*
								 * &&
								 * (graphRefreshTime+10000)<System.currentTimeMillis
								 * () && graphRefreshTime!=0/*ChartComponent.
								 * isDrawFinished
								 */) {
			// ChartComponent.isDrawFinished = false;
			isChartLoaded = false;
			refreshme.setLoading(true);
			// UiApplication.getUiApplication().invokeLater(new Runnable() {
			// public void run() {
			actionPerfomed(ActionCommand.CMD_BSE_GET_GRAPH_DATA, null);

			// UiApplication.getUiApplication().invokeLater(new Runnable() {
			//
			// public void run() {
			// // TODO Auto-generated method stub
			// Dialog.alert("here   1");
			// }
			// });

			// isChartLoaded = false;
			// }
			// });
		}
		// }
		// else
		// {
		// graphRefreshRate++;
		// }
	}
}
