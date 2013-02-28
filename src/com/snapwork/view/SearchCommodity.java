package com.snapwork.view;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.CommodityBean;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CommoditySearchListField;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.NoRecordFoundField;
import com.snapwork.components.SearchListField;
import com.snapwork.components.TitleBar;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.Utils;

public class SearchCommodity extends MainScreen implements ActionListener,
		ReturnString {

	private BottomMenu bottomMenu = null;
	private VerticalFieldManager searchResults = null;
	private CustomBasicEditField txtFieldSearch = null;
	private SearchCommodity screen = this;
	private long time;
	private long timer;
	private String type;
	private String dateTextFNO;
	private String dateTextID;
	

	public SearchCommodity(Vector vector) {

		

		getMainManager().setBackground(
				BackgroundFactory.createSolidBackground(Color.BLACK));
		LOG.print("SearchStocks vector.size() : " + vector.size());
//		if (vector.size() != 0) {
//			this.type = (String) vector.elementAt(0);
//			LOG.print("type : " + type);
//			this.dateTextFNO = (String) vector.elementAt(1);
//			this.dateTextID = (String) vector.elementAt(2);
//			
//			time = System.currentTimeMillis();
//			timer = System.currentTimeMillis();
//
//		}
		
		
		
		
		createUI(vector);
		
		
		
		
		
		
	}

	public void createUI(Vector vector) {
		VerticalFieldManager titleBar = new VerticalFieldManager(USE_ALL_WIDTH);
		titleBar.add(new TitleBar("Search Scrip"));
		setTitle(titleBar);
		VerticalFieldManager mainManager = new VerticalFieldManager(
				Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics) {
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
			}

			protected void sublayout(int maxWidth, int maxHeight) {
				super.sublayout(AppConstants.screenWidth,
						AppConstants.screenHeight);
				setExtent(AppConstants.screenWidth, AppConstants.screenHeight
						- BottomMenu.getItemHeight() - TitleBar.getItemHeight());
			}
		};
		searchResults = new VerticalFieldManager(USE_ALL_WIDTH);
		boolean noElements = true;
		for (int i = 0; i < vector.size(); i++) {
			LOG.print("Search Elements : " + i);
			if (vector.elementAt(i) instanceof CommodityBean) {
				noElements = false;
				CommodityBean CommodityBean = (CommodityBean) vector.elementAt(i);
//				LOG.print("CommodityBean.getName() : " + CommodityBean.getName()
//						+ " , CommodityBean.getId() : " + CommodityBean.getId()
//						+ " , CommodityBean.getSymbol() : "
//						+ CommodityBean.getSymbol() + this.dateTextFNO);
//				if (this.type.equalsIgnoreCase("EQUITY")) {
					CommoditySearchListField searchListField = new CommoditySearchListField(
							FOCUSABLE, CommodityBean.getScripName(),
							CommodityBean.getExpiryDate(), CommodityBean.getExchange(),CommodityBean.getSymbol(),CommodityBean.getToken(),this);
					searchResults.add(searchListField);
//				} else {
//					SearchListField searchListField = new SearchListField(
//							FOCUSABLE, CommodityBean.getDisplayName1() + " "
//									+ CommodityBean.getDisplayName2(),
//							CommodityBean.getCompanyCode(),
//							CommodityBean.getSymbol(), this.type,
//							this.dateTextFNO, this.dateTextID, this);
//					searchResults.add(searchListField);
//				}
			}
		}
		if (noElements) {
			NoRecordFoundField nrf = new NoRecordFoundField(
					"No Available Contracts!", FOCUSABLE);
			searchResults.add(nrf);
		}
		searchResults.add(new NullField(FOCUSABLE));
		mainManager.add(searchResults);

		// fillSearchData(vector);
		add(mainManager);

		bottomMenu = BottomMenu.getBottomMenuInstance(this,
				ImageManager.getBottomMenuImages(true),
				ImageManager.getBottomMenuImages(false),
				ActionCommand.CMD_SEARCH_SCREEN,
				AppConstants.bottomMenuCommands);

	}

	public boolean onSavePrompt() {
		return true;
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

	public boolean keyChar(char key, int status, int time) {
		return super.keyChar(key, status, time);
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
		} else
			return super.keyDown(keyCode, time);

		return true;
	}

	public boolean keyUp(int keyCode, int time) {
		return super.keyUp(keyCode, time);
	}

	public void componentsPrepared(byte componentID, Object component) {
		// TODO Auto-generated method stub

	}

	public Vector getComponentData() {
		return null;
	}

	public void actionPerfomed(byte Command, Object data) {
		switch (Command) {
		case ActionCommand.CMD_NIFTY_COMPANY_DETAILS_SCREEN:
			LOG.print(" Click ");
			Vector vectorCommandData = (Vector) data;
			String code = (String) vectorCommandData.elementAt(0);
			// ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
			ReturnStringParser rts = new ReturnStringParser(
					Utils.getCompanyLatestTradingDetailsURL(code), 725, this);
			break;
		default:
			Debug.debug("data : " + (data == null));
			ActionInvoker.processCommand(new Action(Command, data));
			break;
		}
	}

	public void setReturnString(String string, int id) {
		if (id == 725) {
			Vector vector = HomeJsonParser.getVector(string);
			if (vector.size() > 0) {
				HomeJson hj = (HomeJson) vector.elementAt(0);
				CompanyFNODetailsSnippetsScreen csScreen = new CompanyFNODetailsSnippetsScreen(
						hj, 0, Utils.WATCHLIST_MODE);
			}

		}

	}
}
