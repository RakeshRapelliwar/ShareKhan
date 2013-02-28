package com.snapwork.view;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.text.TextFilter;
import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.Expiry;
import com.snapwork.beans.FoScrips;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.SearchBean;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.CustomFNOLabelField;
import com.snapwork.components.CustomLinkButton;
import com.snapwork.components.NoRecordFoundField;
import com.snapwork.components.SearchListField;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.Utils;

public class SearchStocksFNOFields extends MainScreen implements ActionListener, ReturnString {

	private BottomMenu bottomMenu = null;
	private VerticalFieldManager searchResults = null;
	private SearchStocksFNOFields object = this;
	private ActionListener actionListener = this;
	private int dateTextID;
	public SearchStocksFNOFields(String dateText, String dateTextID, boolean addDirect) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		LOG.print("SearchStocks");
		try{
			this.dateTextID = Integer.parseInt(dateTextID);
		}
		catch(Exception e)
		{

		}
		VerticalFieldManager titleBar = new VerticalFieldManager(USE_ALL_WIDTH);
		titleBar.add(new TitleBar("Search Scrip"));
		setTitle(titleBar);
		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
			}            
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(AppConstants.screenWidth, AppConstants.screenHeight);
				setExtent(AppConstants.screenWidth, AppConstants.screenHeight-BottomMenu.getItemHeight()-TitleBar.getItemHeight());
			}
		};
		searchResults = new VerticalFieldManager(USE_ALL_WIDTH);
		//searchResults.add(new NullField(FOCUSABLE));
		mainManager.add(searchResults);

		//fillSearchData(vector);
		add(mainManager);
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_SEARCH_SCREEN, AppConstants.bottomMenuCommands);

		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				try {
					ScreenInvoker.pushScreen(object, true, true);
					ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
				} catch (Exception e) {
				}
			}
		});
		ReturnStringParser rparse;
		if(dateText.indexOf("http")>-1)
			rparse = new ReturnStringParser(dateText, 500, this);
		else
			rparse = new ReturnStringParser(Utils.getQuoteURL(dateText), 500, this);
	}

	public void createUI(Vector vector) {

		if(vector.size()==0)
		{
			NoRecordFoundField nrf = new NoRecordFoundField("No Records Found!", FOCUSABLE);
			searchResults.add(nrf);
		}
		else{
			LOG.print("vector.size() : "+vector.size());
			for(int i=0;i<vector.size();i++) {
			if(vector.elementAt(i) instanceof HomeJson){
				LOG.print("HomeJson instance i : "+i);
				HomeJson homeJson = (HomeJson)vector.elementAt(i);
				LOG.print(homeJson.getCompanyCode());
				LOG.print(homeJson.getSymbol());
				LOG.print(homeJson.getLastTradedPrice());
				LOG.print(Expiry.getValue(dateTextID));
				CustomFNOLabelField cfno = new CustomFNOLabelField(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),Expiry.getText(dateTextID),homeJson.getCompanyCode(),true,homeJson,Expiry.getValue(dateTextID), actionListener);
				LOG.print("HomeJson instance i Error 1 : "+i);
				cfno.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()),homeJson.getOpenInterest(), homeJson.getVolume(), homeJson);
				LOG.print("HomeJson instance i Error 2 : "+i);
				cfno.setDateTextID(dateTextID);
				LOG.print("HomeJson instance i Error 3 : "+i);
				cfno.setShowStockScreen(true);
				LOG.print("HomeJson instance i Error 4 : "+i);
				searchResults.add(cfno);
				LOG.print("HomeJson instance i Error 5 : "+i);
			}
		}
		}
	}

	public boolean onSavePrompt() {
		return true;
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

	public boolean keyChar( char key, int status, int time )
	{
		return super.keyChar(key, status, time);
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

	public void componentsPrepared(byte componentID, Object component) {
		// TODO Auto-generated method stub

	}
	public Vector getComponentData() {
		return null;
	}

	public void actionPerfomed(byte Command, Object data) {
		switch(Command) {
		default:
			Debug.debug("data : "+(data==null));
			ActionInvoker.processCommand(new Action(Command,data));
			break;
		}
	}

	public void setReturnString(final String string, int id) {
		if(id == 500)
		{
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					try {
						ScreenInvoker.removeRemovableScreen();
						Vector v = HomeJsonParser.getVector(string);
						createUI(v);
					} catch (Exception e) {
					}
				}
			});

			/*Vector v = HomeJsonParser.getVector(string);
			bannerData = (HomeJson) v.elementAt(0);
			banner.setName(bannerData.getSymbol());
			date = Expiry.getText(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
			banner.setSource(date);
			banner.setComp(bannerData.getCompanyCode());
			banner.setData(bannerData.getLastTradedPrice(), bannerData.getChange(), (bannerData.getPercentageDiff().indexOf("-") == -1 ? "+"+bannerData.getPercentageDiff() : bannerData.getPercentageDiff()),bannerData.getOpenInterest(), bannerData.getVolume());
			banner.refresh();
			id_instrument_ddl();*/
		} 

	}
}
