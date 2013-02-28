package com.snapwork.view;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.ObjectChoiceField;//componentdetailsscreen
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.util.Arrays;
import net.rim.device.api.util.Comparator;
import net.rim.device.api.util.StringComparator;

import com.lightstreamer.javameclient.midp.SimpleItemUpdate;
import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.StreamData;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomLinkButtonUnderLine;
import com.snapwork.components.CustomObjectChoiceField;
import com.snapwork.components.RefreshButton;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBarRefresh;
import com.snapwork.components.TitleBarRefreshWL;
import com.snapwork.components.TitleBar;
import com.snapwork.components.WatchListField;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.stream.StreamBean;
import com.snapwork.stream.StreamDataService;
import com.snapwork.stream.StreamDataUpdate;
import com.snapwork.stream.StreamSchemaGroup;
import com.snapwork.util.AppConstants;
import com.snapwork.util.AutoRefreshableStream;
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.AutoScreenRefresherThreadStream;
import com.snapwork.util.DBPackager;
import com.snapwork.util.DBmanager;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class MyWatchList extends MainScreen implements ThreadedComponents,ActionListener, ReturnDataWithId, StreamDataUpdate, AutoRefreshableStream, AutoRefreshableScreen, ReturnString{

	VerticalFieldManager verticalWatchListFieldManager = null;
	private BottomMenu bottomMenu = null;
	private boolean quoteflag = false;
	private RefreshButton refreshme;
	private boolean refreshFlag = true;
	private Vector watchListCompaniesh;
	private boolean firstSelect = false;
	private int stateIndex = 0;
	public static boolean mywl ;
	private int lastStateIndex = 0;
	private boolean inxFlag;
	public static int storecount = 0;
	public static int selectedIndex = 0;
	public static boolean isFINISHED;
	public static boolean REFRESH;
	public String companylistUrl = "";//Market watch
	public static String streamURL = "";
	public static Vector streamURLVector = new Vector();
	public static Hashtable streamHash = new Hashtable();
	public static int counter;
	public static long time;
	public static Vector strmv;
	private int WATCHLISTSCRIPDATA = 889;
	private CustomObjectChoiceField dropdown; 
	private Vector watchlistHolder = null;
	private String[] choice = { "Sort by Name (A-Z)", "Sort by Name (Z-A)", "Sort by Price (Highest)", "Sort by Price (Lowest)", "Sort by Change (Highest)", "Sort by Change (Lowest)", "Sort by % Change (Highest)", "Sort by % Change (Lowest)"};
	private boolean sorting;
	private boolean refreshSort;
	private boolean second=false;
	final MyWatchList instance = this;
	private boolean pushOnce = false;
	private StreamDataService streamDataService;
	public static boolean editflag;
	int jx = 1;
	//SimpleItemUpdate update;
	public MyWatchList(Vector watchListCompanies) {
		
		
		final HomeJson hj5 = (HomeJson)watchListCompanies.elementAt(0);
		
		
		
		pushOnce = true;
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		streamURLVector = new Vector();
		companylistUrl = "";
		streamURL = "";
		streamHash = new Hashtable();
		if(watchListCompanies.size()==0)
		{
			
			
			
			createUI(watchListCompanies, null);
		}
		else{
			for(int i=0;i<watchListCompanies.size();i++)
			{
				HomeJson hj = (HomeJson)watchListCompanies.elementAt(i);
				if(i==0)
				{
					streamURL = streamURL+"item_"+hj.getCompanyCode();
					companylistUrl = hj.getCompanyCode();
				}
				else
				{
					streamURL = streamURL+" item_"+hj.getCompanyCode();
					companylistUrl = companylistUrl+","+hj.getCompanyCode();
				}
				streamURLVector.addElement(hj.getCompanyCode());
				streamHash.put(new Integer(i),hj.getCompanyCode());
				LOG.print("hj.getCompanyCode() : "+i+" - "+hj.getCompanyCode());
			}
			LOG.print("MyWatchList.streamURL "+MyWatchList.streamURL);
			LOG.print("companylistUrl "+companylistUrl);
			AutoScreenRefresherThread.onLoad = false;
			MyWatchList.isFINISHED = false;
			watchlistHolder = new Vector();
			AutoScreenRefresherThreadStream.startThread();
			if(storecount == -1) storecount = 0;
			LOG.print("**MyWatchList size "+watchListCompanies.size());
			mywl = true;
			this.watchListCompaniesh = watchListCompanies;
			Vector vector = DBmanager.getRecords(AppConstants.appDBMyWatchListState);

			if(vector.size()==1) {
				try {
					ByteArrayInputStream bais = new ByteArrayInputStream((byte[]) vector.elementAt(0));
					DataInputStream dis = new DataInputStream(bais);
					storecount = dis.readInt();
					LOG.print("=storecount "+storecount);
					dis.close();
					bais.close();
					sortWatchList(watchListCompanies,null, storecount, false);
				} catch (IOException e) {
					LOG.print("storecount Error"+storecount);
					storecount = 0;
					sortWatchList(watchListCompanies,null, storecount, false);
				}
			}
			else
			{
				storecount = 0;
				sortWatchList(watchListCompanies, null, storecount, false);
			}
		}

	}

	public MyWatchList(Vector watchListCompanies, boolean quoteFlag) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		pushOnce = true;
		streamURLVector = new Vector();
		companylistUrl = "";
		streamURL = "";
		streamHash = new Hashtable();
		if(watchListCompanies.size()==0)
		{
			createUI(watchListCompanies, null);
		}
		else{
			for(int i=0;i<watchListCompanies.size();i++)
			{
				HomeJson hj = (HomeJson)watchListCompanies.elementAt(i);
				if(i==0)
				{
					streamURL = streamURL+"item_"+hj.getCompanyCode();
					companylistUrl = hj.getCompanyCode();
				}
				else
				{
					streamURL = streamURL+" item_"+hj.getCompanyCode();
					companylistUrl = ","+hj.getCompanyCode();
				}
				streamURLVector.addElement(hj.getCompanyCode());
				streamHash.put(new Integer(i),hj.getCompanyCode());
			}
			LOG.print("MyWatchList.streamURL "+MyWatchList.streamURL);
			LOG.print("companylistUrl "+companylistUrl);
			LOG.print("QuoteFlag "+quoteFlag);
			quoteflag = quoteFlag;
			this.watchListCompaniesh = watchListCompanies;
			watchlistHolder = new Vector();
			AutoScreenRefresherThreadStream.startThread();
			sortWatchList(watchListCompanies, null, storecount, false);
		}
	}

	public void createUI(Vector watchListCompanies, Vector holder) {
		counter = 0;
		time = System.currentTimeMillis();
		strmv = new Vector();
		watchListCompaniesh = watchListCompanies;
		if(holder != null)
			watchlistHolder = holder;
		boolean holderFlag = false;
		if(holder == null)
			holderFlag = true;
		dropdown = new CustomObjectChoiceField("", choice,storecount,ObjectChoiceField.USE_ALL_WIDTH | FIELD_HCENTER){

			protected void fieldChangeNotify(int index)
			{
				if (index != FieldChangeListener.PROGRAMMATIC )
				{
					if(getSelectedIndex() != storecount){
						second=true;
						storecount = this.getSelectedIndex();
						setData(watchListCompaniesh, 202);
					}
				}
			}
		};
		dropdown.setId(true);
		dropdown.setMinimalWidth(AppConstants.screenWidth);
		LOG.print("createUI ====== ===== ");
		VerticalFieldManager titleBar_ = new VerticalFieldManager(USE_ALL_WIDTH);
		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(maxWidth,maxHeight);
				if(bottomMenu.isAttached) {
					if(getField(0).getPreferredHeight()<AppConstants.screenHeight-BottomMenu.BottomMenuImageHeight-TitleBar.getItemHeight()) {
						setExtent(maxWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
					}
				} else {
					if(getField(0).getPreferredHeight()<AppConstants.screenHeight-TitleBar.getItemHeight()) {
						setExtent(maxWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
					}                       
				}
			}
		};
		if(watchListCompanies.size() == 0)
		{verticalWatchListFieldManager = new VerticalFieldManager(FOCUSABLE);
		if(Utils.WATCHLIST_NAME.equalsIgnoreCase("NIFTY") || Utils.WATCHLIST_NAME.equalsIgnoreCase("SENSEX"))
		{
			titleBar_.add(new TitleBar(Utils.WATCHLIST_NAME));
			if(watchListCompanies.size()!=0)
				titleBar_.add(dropdown);
			setTitle(titleBar_);
		}
		else{
			String string = Utils.WATCHLIST_NAME; 
			if(string.indexOf(".")>12 && string.length()>15)
				string = string.substring(0, string.indexOf(".")-3) + ".."+  string.substring(string.indexOf("."),string.length());
			titleBar_.add(new TitleBar(string));
			if(watchListCompanies.size()!=0)
				titleBar_.add(dropdown);
			setTitle(titleBar_);
		}
		//mainManager.add(verticalWatchListFieldManager);
		//add(mainManager);
		//ADDED AFTER FNO
		LOG.print(" pushOnce : "+pushOnce);
		if(pushOnce){ pushOnce = false;
		boolean flag = true;
		if(Utils.WATCHLIST_NAME.equalsIgnoreCase("NIFTY"))
			flag = false;
		else if(Utils.WATCHLIST_NAME.equalsIgnoreCase("SENSEX"))
			flag = false;
		if(flag)
		{
			HorizontalFieldManager h = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER)
			{
				public void paintBackground(Graphics graphics)
				{
					graphics.setColor(Color.BLACK);
					graphics.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			h.add(new ButtonField("+Add more stocks", FOCUSABLE | DrawStyle.HCENTER ) {

				protected boolean navigationClick(int status,int time) {
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							isFINISHED = true;
							Utils.WATCHLIST_MODE = true;
							actionPerfomed(ActionCommand.CMD_SEARCH_SCREEN, null);
						}
					}); return super.navigationClick(status, time);
				}
			});
			final int hh = h.getPreferredHeight();
			int hx = 0;
			/*if(AppConstants.screenHeight>=480)
				hx = WatchListField.getFieldHeight()*3;
			if( AppConstants.screenHeight>((verticalWatchListFieldManager.getFieldCount()*WatchListField.getFieldHeight())+TitleBar.getItemHeight()+WatchListField.getFieldHeight()+(hx)))
			{
				verticalWatchListFieldManager.add(new LabelField("",NON_FOCUSABLE){protected void layout(int arg0, int arg1) {
					if(AppConstants.screenHeight>=480)
						setExtent(arg0, AppConstants.screenHeight-((((1>verticalWatchListFieldManager.getFieldCount()-2)?1:(verticalWatchListFieldManager.getFieldCount()-1))*WatchListField.getFieldHeight())+TitleBar.getItemHeight()+(WatchListField.getFieldHeight()*3)+8));
					else
						setExtent(arg0, AppConstants.screenHeight-((((1>verticalWatchListFieldManager.getFieldCount()-2)?1:(verticalWatchListFieldManager.getFieldCount()-1))*WatchListField.getFieldHeight())+TitleBar.getItemHeight()+hh));
				}});
			}*/
			verticalWatchListFieldManager.add(new LabelField("",NON_FOCUSABLE){protected void layout(int arg0, int arg1) {
				setExtent(arg0, AppConstants.screenHeight-TitleBar.getItemHeight()-(AppConstants.screenHeight>320?60:30));
			}});
			verticalWatchListFieldManager.add(h);
			LOG.print(" FieldCount "+verticalWatchListFieldManager.getFieldCount());
		}
		mainManager.add(verticalWatchListFieldManager);
		add(mainManager);
		}
		//
		refreshFlag = false;
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_WL_SCREEN, AppConstants.bottomMenuCommands);
		}
		else{refreshme = new RefreshButton()
		{
			protected boolean navigationClick(int arg0, int arg1) {
				if(!getLoading())
				{
					setLoading(true);
				}
				return super.navigationClick(arg0, arg1);
			}
		};
		int titleHT  = 0;

		ButtonField edit = new ButtonField("Edit", FOCUSABLE | FIELD_RIGHT | USE_ALL_HEIGHT)
		{
			public net.rim.device.api.ui.Font getFont() {
				if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
					return FontLoader.getFont(AppConstants.BIG_BOLD_FONT);

				return FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT);
			}

			public int getPreferredWidth() {
				return 60;
			}

			protected boolean navigationClick(int arg0, int arg1) {
				if(verticalWatchListFieldManager.getFieldCount()>2){
					refreshFlag = false;
					isFINISHED = true;
					MyWatchList.REFRESH = false;
					editflag = true;
					Vector vectorCommandData = new Vector();
					vectorCommandData.addElement(Utils.WATCHLIST_LABEL);
					Utils.WATCHLIST = false;
					MyWatchListEdit mwle = new MyWatchListEdit(watchListCompaniesh);
					ScreenInvoker.pushScreen(mwle, true, true);
				}
				return super.navigationClick(arg0, arg1);
			}
		};
		if(Utils.WATCHLIST_NAME.equalsIgnoreCase("NIFTY") || Utils.WATCHLIST_NAME.equalsIgnoreCase("SENSEX"))
		{

			titleBar_.add(new TitleBarRefresh(Utils.WATCHLIST_NAME, refreshme));
			if(watchListCompanies.size()!=0)
				titleBar_.add(dropdown);
			setTitle(titleBar_);
			titleHT = TitleBar.getItemHeight();
		}
		else{
			String string = Utils.WATCHLIST_NAME; 
			if(string.indexOf(".")>12 && string.length()>15)
				string = string.substring(0, string.indexOf(".")-3) + ".."+  string.substring(string.indexOf("."),string.length());
			titleBar_.add(new TitleBarRefreshWL(Utils.WATCHLIST_NAME, edit,refreshme));
			if(watchListCompanies.size()!=0)
				titleBar_.add(dropdown);
			setTitle(titleBar_);
			titleHT = TitleBarRefreshWL.getItemHeight();
		}
		final int titleHeight = titleHT;
		LOG.print(" watchListCompanies - flag1");
		if(watchListCompanies!=null) {
			LOG.print(" watchListCompanies - flag2");
			if(verticalWatchListFieldManager ==null)
				verticalWatchListFieldManager = new VerticalFieldManager(FOCUSABLE);

			LOG.print("watchListCompanies.size() - "+watchListCompanies.size());
			LOG.print("Utils.getWatchListedCompanyRecords().size() - "+Utils.getWatchListedCompanyRecords().size());
			MyWatchListEdit.storeEditData = new Vector();
			if(watchListCompanies.size()!=0){
				MyWatchListEdit.storeEditData = new Vector();
				LOG.print("watchListCompanies.size() "+watchListCompanies.size());
				synchronized(this)
				{
				for(int i=0;i<watchListCompanies.size();i++) {
					LOG.print("watchListCompanies.size() Counter - "+i);
					final HomeJson homeJSon = (HomeJson)watchListCompanies.elementAt(i);
					
					
					
					
					MyWatchListEdit.storeEditData.addElement(homeJSon);
					String name = "";
					name = homeJSon.getDisplayName1();
					LOG.print("homeJSon.getDisplayName1() : "+homeJSon.getDisplayName1());
					LOG.print("homeJSon.getDisplayName2() : "+homeJSon.getDisplayName2());
					LOG.print("homeJSon.getLastTradedPrice() : "+homeJSon.getLastTradedPrice());
					{
						if(holderFlag)
						{
							WatchListField wf =  new WatchListField(FOCUSABLE, homeJSon.getSymbol(), homeJSon.getLastTradedPrice(), homeJSon.getChange(), homeJSon.getChangePercent(), homeJSon.getCompanyCode(),watchListCompanies.elementAt(i).toString(), this, name,homeJSon,homeJSon.getDisplayName2().equalsIgnoreCase("BSE")?WatchListField.BSE:WatchListField.NSE);
							verticalWatchListFieldManager.add(wf);
							watchlistHolder.addElement(wf);
						}
						else
						{
							((WatchListField)verticalWatchListFieldManager.getField(i)).setUpdatedData(homeJSon.getSymbol(), homeJSon.getLastTradedPrice(), homeJSon.getChange(), homeJSon.getChangePercent(), homeJSon.getCompanyCode(),watchListCompanies.elementAt(i).toString(), this, name,homeJSon,homeJSon.getDisplayName2().equalsIgnoreCase("BSE")?WatchListField.BSE:WatchListField.NSE,homeJSon.isStream());
						}
					}
					if(homeJSon.isStream())
					{
						homeJSon.setStream(false);
						watchListCompaniesh.removeElementAt(i);
						watchListCompaniesh.insertElementAt(homeJSon, i);
					}
				}
				Utils.setWatchListedCompanyRecordsData(MyWatchListEdit.storeEditData);
			}
			}
			LOG.print(" pushOnce : "+pushOnce);
			if(pushOnce){ pushOnce = false;
			boolean flag = true;
			if(Utils.WATCHLIST_NAME.equalsIgnoreCase("NIFTY"))
				flag = false;
			else if(Utils.WATCHLIST_NAME.equalsIgnoreCase("SENSEX"))
				flag = false;
			if(flag)
			{
				HorizontalFieldManager h = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER)
				{
					public void paintBackground(Graphics graphics)
					{
						graphics.setColor(Color.BLACK);
						graphics.fillRect(0, 0, getWidth(), getHeight());
					}
				};
				h.add(new ButtonField("+Add more stocks", FOCUSABLE | DrawStyle.HCENTER ) {

					protected boolean navigationClick(int status,int time) {
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								isFINISHED = true;
								Utils.WATCHLIST_MODE = true;
								actionPerfomed(ActionCommand.CMD_SEARCH_SCREEN, null);
							}
						}); return super.navigationClick(status, time);
					}
				});
				final int hh = h.getPreferredHeight();
				int hx = 0;
				if(AppConstants.screenHeight>=480)
					hx = WatchListField.getFieldHeight()*3;
				if( AppConstants.screenHeight>((verticalWatchListFieldManager.getFieldCount()*WatchListField.getFieldHeight())+titleHeight+WatchListField.getFieldHeight()+(hx)))
				{
					verticalWatchListFieldManager.add(new LabelField("",NON_FOCUSABLE){protected void layout(int arg0, int arg1) {
						if(AppConstants.screenHeight>=480)
							setExtent(arg0, AppConstants.screenHeight-((((1>verticalWatchListFieldManager.getFieldCount()-2)?1:(verticalWatchListFieldManager.getFieldCount()-1))*WatchListField.getFieldHeight())+titleHeight+(WatchListField.getFieldHeight()*3)+8));
						else
							setExtent(arg0, AppConstants.screenHeight-((((1>verticalWatchListFieldManager.getFieldCount()-2)?1:(verticalWatchListFieldManager.getFieldCount()-1))*WatchListField.getFieldHeight())+titleHeight+hh));
					}});
				}
				verticalWatchListFieldManager.add(h);
				LOG.print(" FieldCount "+verticalWatchListFieldManager.getFieldCount());
			}
			mainManager.add(verticalWatchListFieldManager);
			add(mainManager);
			}
		}
		}
		try{
			LOG.print("sort end Utils.WATCHLIST_INDEX : "+Utils.WATCHLIST_INDEX);
			if(Utils.WATCHLIST_INDEX>-1)
				verticalWatchListFieldManager.getField(Utils.WATCHLIST_INDEX).setFocus();
		}
		catch(Exception e)
		{

		}
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_WL_SCREEN, AppConstants.bottomMenuCommands);
		LOG.print("inside MywatchList");
		if(quoteflag)
		{
			LOG.print("Push Screen MyWatchList");
			ScreenInvoker.pushScreen(this, true, true);
			ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
			new WatchListPopUp(Utils.WATCHLIST_QUOTE, Utils.WATCHLIST_MODE);
		}
		else if(!(UiApplication.getUiApplication().getActiveScreen() instanceof MyWatchList))
		{
			ScreenInvoker.pushScreen(this, true, true);
		}
		refreshFlag = true;
		if(watchListCompanies.size()!=0)
			if(streamDataService == null){
				streamDataService = new StreamDataService(this, StreamSchemaGroup.getSchema(), StreamSchemaGroup.getWatchListGroup());
				streamDataService.OpenConnection();
			}
	}

	public boolean onClose() {
		Utils.WATCHLIST_CLOSE = true;
		MyWatchList.REFRESH = false;
		isFINISHED = true;
		streamDataService.closeConnection();
		AutoScreenRefresherThreadStream.stopThread();
		return super.onClose();
	}
	public void componentsPrepared(byte componentID, Object component) {}

	public void componentsDataPrepared(byte componentID,final Object data) {
		try {

			switch(componentID) {
			case ActionCommand.CMD_WL_REFRESH:
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						ScreenInvoker.removeRemovableScreen();
						LOG.print("componentsDP called");
						watchListCompaniesh = (Vector)data;
						createUI((Vector)data, watchlistHolder); 
					}
				});
				break;
			}
		} catch(Exception ex) {
			Debug.debug("componentsDataPrepared,Error : "+ex.toString());
			refreshFlag = false;
			refreshme.setLoading(false);
		}
	}

	public void actionPerfomed(byte Command, Object sender) {
		switch(Command) {
		case ActionCommand.CMD_WL_REFRESH:
			ActionInvoker.processCommand(new Action(Command,null));
			break;
		case ActionCommand.CMD_COMPANY_DETAILS_SCREEN:
			MyWatchList.REFRESH = false;
			Utils.WATCHLIST_MODE = false;
			ActionInvoker.processCommand(new Action(Command,sender));
			break;
		case ActionCommand.CMD_COMPANY_DETAILS_SCREEN_NSE:
			MyWatchList.REFRESH = false;
			Utils.WATCHLIST_MODE = false;
			ActionInvoker.processCommand(new Action(Command,sender));
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
			Utils.WATCHLIST_CLOSE = true;
			LOG.print("KEY_END EXIT from app");
			MyWatchList.isFINISHED = true;
			streamDataService.closeConnection();
			AutoScreenRefresherThreadStream.stopThread();
			System.exit(0);
		}

		else if(key== Keypad.KEY_ESCAPE)
		{
			Utils.WATCHLIST_MODE = false;
			if(streamDataService!=null)
				streamDataService.closeConnection();
			AutoScreenRefresherThreadStream.stopThread();
			Action action = new Action(ActionCommand.CMD_WATCHLIST_SCREEN, null);
			ActionInvoker.processCommand(action);
		}
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
		//jx++;
		//streamDataUpdate(2, update);
		LOG.print("Refresh WatchList");
		if(!(UiApplication.getUiApplication().getActiveScreen() instanceof MyWatchList))
		{
			streamDataService.closeConnection();
			return;
		}
		else
		{
			if(streamDataService.isClose())
			{
				Utils.WATCHLIST_CLOSE = false;
				Utils.WATCHLIST_INDEX = 0;
				MyWatchList.isFINISHED  = false;
				MyWatchList.REFRESH = false;
			ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
			Utils.WATCHLIST = true;
				ReturnStringParser rts = new ReturnStringParser(Utils.getWatchListURL(UserInfo.getUserID(),Utils.WATCHLIST_NAME), WATCHLISTSCRIPDATA, this, true);
				
				//streamDataService.OpenConnection();
			}
			
		}
		
		/*LOG.print("Refresh WatchList");
		if(refreshFlag && !sorting) {
			refreshFlag = false;
			second=true;
			refreshme.setLoading(true);
			LOG.print("Refresh WatchList -->");
			HomeJsonParser h = new HomeJsonParser(companylistUrl, this, 202);
		}*/
	}

	public void setData(final Vector vector, int id) {
		if( id == 202)
		{

			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					ScreenInvoker.removeRemovableScreen();
					if(verticalWatchListFieldManager!=null)
					{
						int inx = verticalWatchListFieldManager.getFieldWithFocusIndex();;
						if(inx>-1)
							Utils.WATCHLIST_INDEX = inx;
					}
					LOG.print("componentsDP called");
					sortWatchList(vector, watchlistHolder, storecount, false);
				}
			});
		}

	}
	public synchronized void sortWatchList(final Vector watchListCompanies,final Vector watchListFieldHolder, int selectedIndex, boolean falsem) {
		sorting = true;
		LOG.print("((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((");
		LOG.print("Sorting started : "+selectedIndex);
		int in = 0;
		LOG.print(storecount+" Selected index >>>>>>>>>>>"+selectedIndex);
		byte[] pack = DBPackager.convertTOByte(selectedIndex);
		DBmanager.dropDatabase(AppConstants.appDBMyWatchListState);
		DBmanager.addData(AppConstants.appDBMyWatchListState, pack);
		boolean reverse = false;

		if(selectedIndex == 1 || selectedIndex == 2 || selectedIndex == 4 || selectedIndex == 6)
		{
			if(selectedIndex==1)
				selectedIndex = 0;
			else if(selectedIndex==2)
				selectedIndex = 1;
			else if(selectedIndex==4)
				selectedIndex = 2;
			else if(selectedIndex==6)
				selectedIndex = 3;
			reverse = true;
		}
		else if(selectedIndex == 0 || selectedIndex == 3 || selectedIndex == 5 || selectedIndex == 7)
		{
			if(selectedIndex==3)
				selectedIndex = 1;
			else if(selectedIndex==5)
				selectedIndex = 2;
			else if(selectedIndex==7)
				selectedIndex = 3;
		}

		final Vector vec = new Vector();
		final Vector holder = new Vector();
		Hashtable toSortElements = new Hashtable();
		Hashtable watchListElements = new Hashtable();

		int intialField = -1;
		int lastField = 0;
		for(int i=0;i<watchListCompanies.size();i++) {
			final HomeJson homeJson = (HomeJson) watchListCompanies.elementAt(i);
			String sortElement = (selectedIndex==0 ? homeJson.getSymbol() : (selectedIndex==1 ? homeJson.getLastTradedPrice() : (selectedIndex==2?homeJson.getChange() : (selectedIndex==3?homeJson.getChangePercent():""))));

			if(intialField==-1) {
				intialField = i;
			}
			lastField = i;

			if(selectedIndex==0){
				toSortElements.put(new Integer(i),sortElement);
			} else {
				sortElement = Utils.findAndReplace(sortElement, ",", "");
				float floatValue = 0;
				try {
					floatValue = Float.parseFloat(sortElement);
				} catch(NumberFormatException nex) {
					LOG.print("Number Format Exception "+sortElement);
				} catch(Exception ex) {
					LOG.print("Simple exception "+sortElement);                                                                     
				}
				toSortElements.put(new Integer(i), new Float(floatValue));
			}
			watchListElements.put(new Integer(i), new Field() {

				protected void paint(Graphics arg0) {
					// TODO Auto-generated method stub

				}

				protected void layout(int arg0, int arg1) {
					// TODO Auto-generated method stub

				}
			});
		}

		LOG.print("toSortElements.size() : "+toSortElements.size());

		final Integer[] toSortKeys = new Integer[toSortElements.size()];
		Enumeration enumerationKeys = toSortElements.keys();
		int indexKey=0;
		while(enumerationKeys.hasMoreElements()) {
			toSortKeys[indexKey] = (Integer)enumerationKeys.nextElement(); 
			LOG.print(indexKey+"------"+toSortKeys[indexKey]);
			indexKey++;
		}

		if(selectedIndex==0){


			LOG.print("Starting sorting string Array");

			String[] toSortArray = new String[toSortElements.size()];
			Enumeration enumeration = toSortElements.elements();
			int index=0;
			while(enumeration.hasMoreElements()) {
				toSortArray[index] = (String)enumeration.nextElement(); 
				index++;
			}
			LOG.print("Before Sorting Array is------");
			for(int i=0;i<toSortArray.length;i++) {
				LOG.print("key : "+toSortKeys[i]+" Value : "+toSortArray[i]);
			}

			StringComparator stringComparator = StringComparator.getInstance(false);

			//                    /Arrays.sort(toSortArray,stringComparator);
			Arrays.sort(toSortArray,0,toSortArray.length,toSortKeys,stringComparator);
			LOG.print("After Sorting Array is------");
			for(int i=0;i<toSortArray.length;i++) {
				LOG.print("key : "+toSortKeys[i]+" Value : "+toSortArray[i]);
			}
		} else {

			LOG.print("Starting sorting float Array");

			Float[] toSortFloatArray = new Float[toSortElements.size()];
			Enumeration enumeration = toSortElements.elements();
			int index=0;
			while(enumeration.hasMoreElements()) {
				toSortFloatArray[index] = (Float)enumeration.nextElement();       
				index++;
			}

			LOG.print("Before Sorting Array is------");
			for(int i=0;i<toSortFloatArray.length;i++) {
				LOG.print("key : "+toSortKeys[i]+" Value : "+toSortFloatArray[i]);
			}

			Comparator floatComparator = new Comparator() {
				/**
				 * Compare two Float objects.
				 * @return -1 if o1 &lt; o2, +1 if o1 &gt; o2, 0 if o1 == o2
				 * @throws ClassCastException if o1 or o2 is not a Long
				 */
				public int compare(Object o1, Object o2) {
					float val1 = ((Float) o1).floatValue();
					float val2 = ((Float) o2).floatValue();
					return val1 < val2 ? -1 : val1 == val2 ? 0 : 1;
				}
			};

			Arrays.sort(toSortFloatArray,0,toSortFloatArray.length,toSortKeys,floatComparator);
			LOG.print("After Sorting Array is------");
			for(int i=0;i<toSortFloatArray.length;i++) {
				LOG.print("key : "+toSortKeys[i]+" Value : "+toSortFloatArray[i]);
			}

		}


		try {

			MyWatchListEdit.storeEditData = new Vector();
			final Enumeration enumerationWatchListElement = watchListElements.keys();
			LOG.print("selectedIndex->->->->->->"+selectedIndex);
			int keyIndex = 0;
			int lastIndex = 0;

			if(reverse)
				lastIndex = watchListCompanies.size()-1;


			while(enumerationWatchListElement.hasMoreElements()) {
				Integer fieldKey = (Integer)enumerationWatchListElement.nextElement();
				final int keyin = reverse?lastIndex-keyIndex:keyIndex;
				final int fiel = fieldKey.intValue();
				LOG.print("keyIndex :"+ keyIndex);
				LOG.print("fieldKey.intValue() :"+ fieldKey.intValue());
				LOG.print("toSortKeys[keyin] :"+ toSortKeys[keyin]);
				try {          
					vec.addElement(watchListCompanies.elementAt(toSortKeys[keyin].intValue())) ;
					if(watchListFieldHolder != null)
						holder.addElement(watchListFieldHolder.elementAt(toSortKeys[keyin].intValue()));

				} catch (Exception e) {
					LOG.print("Error wf");
				}


				MyWatchListEdit.storeEditData.addElement(watchListCompanies.elementAt(toSortKeys[keyin].intValue()));
				keyIndex++;
			}
		} catch(Exception ex) {
			LOG.print("Exception in replacing fields");
		}
		Utils.setWatchListedCompanyRecordsData(MyWatchListEdit.storeEditData);
		sorting = false; 
		if(watchListFieldHolder == null)
			createUI(vec, null);
		else
			createUI(vec, holder);
	}


	public synchronized void streamDataUpdate(final int item,final SimpleItemUpdate update) {
LOG.print("..--"+"update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE) : "+(update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE)==null?"":update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE)));
		
		LOG.print((String)streamHash.get(new Integer(item-1))+".--"+"update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE) : "+update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE)==null?"":update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE));
		
		if(sorting) return;
		
		if(!(UiApplication.getUiApplication().getActiveScreen() instanceof MyWatchList))
		{
			streamDataService.closeConnection();
			return;
		}
		String chk = update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE);
		if(update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE).equals("UNCHANGED")) 
		{
			chk = (String)streamHash.get(new Integer(item-1));
		}
		final String check = chk;
		
		
		if(verticalWatchListFieldManager.getFieldCount()>15)
		{
			if(time+500>System.currentTimeMillis())
			{
				LOG.print("if");
				if(strmv ==null)
					strmv = new Vector();
				strmv.addElement(new StreamData(check, update.getFieldNewValue(StreamSchemaGroup.LTP), update.getFieldNewValue(StreamSchemaGroup.CHANGE_VALUE), update.getFieldNewValue(StreamSchemaGroup.PERCENT_CHANGE)));
				return;
			}
			else
			{
				LOG.print("else");
				jx++;
				if(strmv ==null)
					return;
				UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Vector vec = strmv;
						strmv = new Vector();
						int cnt = verticalWatchListFieldManager.getFieldCount();
						for(int i=0; i<cnt; i++)
						{
							if(verticalWatchListFieldManager.getField(i) instanceof WatchListField)
							{
								for(int j = 0; j<vec.size();j++){
									StreamData bean = ((StreamData)vec.elementAt(j));
									String ck = ((WatchListField)verticalWatchListFieldManager.getField(i)).getCompanyCode();
								if((ck).equals(bean.getCode())){
									if(!bean.getChange().equals("UNCHANGED"))
										((WatchListField)verticalWatchListFieldManager.getField(i)).setChange(bean.getChange());
									if(!bean.getChangeper().equals("UNCHANGED"))
										((WatchListField)verticalWatchListFieldManager.getField(i)).setPercentageDiff(bean.getChangeper());
									if(!bean.getLtp().equals("UNCHANGED"))
									{
										((WatchListField)verticalWatchListFieldManager.getField(i)).setLastTradedPrice(bean.getLtp());
									}
									}
								}
								
							}
							if(cnt-1==i)
								time = System.currentTimeMillis();
						}
			
				return;
					}
				});
				
			}
		}
		
		
		else{
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
			int cnt = verticalWatchListFieldManager.getFieldCount();
			if(item==streamHash.size())
				return;
			String check = null;
			try {
				check = update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE);
				LOG.print((String)streamHash.get(new Integer(item-1))+".CHECK : "+check);
				if(check.equalsIgnoreCase("UNCHANGED"))
					check = (String)streamHash.get(new Integer(item-1));
			} catch (Exception e1) {
			}
			if(check == null)
			{
				check = (String)streamHash.get(new Integer(item-1));
			}
			else if(check.equalsIgnoreCase("UNCHANGED") || check.equalsIgnoreCase("null"))
			{
				check = "";
				return;
			}
			
			if(check == null)
			{
				check = "";
				return;
			}
			else
			{
				LOG.print("CHECK : "+check);
			}
			
			String changeValue = "UNCHANGED";
			String perValue = "UNCHANGED";
			String ltp = "UNCHANGED";
			try {
				 changeValue = update.getFieldNewValue(StreamSchemaGroup.CHANGE_VALUE);
				 perValue = update.getFieldNewValue(StreamSchemaGroup.PERCENT_CHANGE);
				 ltp = update.getFieldNewValue(StreamSchemaGroup.LTP);
			} catch (Exception e) {
				ltp = "null";
				return;
			}
			for(int i=0; i<cnt; i++)
			{
				if(verticalWatchListFieldManager.getField(i) instanceof WatchListField)
				{
					LOG.print(update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE)+" : "+check+" : "+(item)+" : "+((WatchListField)verticalWatchListFieldManager.getField(i)).getCompanyCode());
					if((check).equals(((WatchListField)verticalWatchListFieldManager.getField(i)).getCompanyCode())){
						LOG.print(update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE));
						LOG.print(((WatchListField)verticalWatchListFieldManager.getField(i)).getCompanyCode());
						//LOG.print("LTP : "+update.getFieldNewValue(StreamSchemaGroup.LTP));
						LOG.print("CHANGE_VALUE : "+changeValue);
	
						if(!update.getFieldNewValue(StreamSchemaGroup.LTP).equalsIgnoreCase("null"))
						{
							if(!changeValue.equals("UNCHANGED"))
								((WatchListField)verticalWatchListFieldManager.getField(i)).setChange(changeValue);
							if(!perValue.equals("UNCHANGED"))
								((WatchListField)verticalWatchListFieldManager.getField(i)).setPercentageDiff(perValue);
							if(!ltp.equals("UNCHANGED"))
								((WatchListField)verticalWatchListFieldManager.getField(i)).setLastTradedPrice(ltp);
						}
					}
				}
			}
				}});
		}
		
		
		//((WatchListField)verticalWatchListFieldManager.getField(i)).setUpdatedData(homeJSon.getSymbol(), homeJSon.getLastTradedPrice(), homeJSon.getChange(), homeJSon.getChangePercent()+"%", homeJSon.getCompanyCode(),watchListCompanies.elementAt(i).toString(), this,null,homeJSon,WatchListField.BSE,homeJSon.isStream());
		
	}

	
	public synchronized void streamDataUpdate(final Hashtable hashx)
	{
		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				
				int cnt = verticalWatchListFieldManager.getFieldCount();
				for(int i=0; i<cnt; i++)
				{
					if(verticalWatchListFieldManager.getField(i) instanceof WatchListField)
					{
						String compcode = ((WatchListField)verticalWatchListFieldManager.getField(i)).getCompanyCode();
						for(int j=0; j<streamHash.size()+1; j++)
						{
							//new Integer(j) replace compcode
							if(hashx.containsKey(new Integer(j)))
							{
								if(hashx.get(new Integer(j)) instanceof StreamBean){
									final StreamBean bean = (StreamBean) hashx.get(new Integer(j));
									LOG.print(bean.getLtp()+" -- LTP -- "+bean.getCode());
									if(((String)streamHash.get(new Integer(bean.getCode()-1))).equals(compcode))
									{
										
										LOG.print("."+compcode);

										LOG.print(j+":j -------- i:"+i);
										LOG.print("ltp : "+bean.getLtp());
										LOG.print(" -------- ");
										if(!bean.getChange().equals("UNCHANGED"))
											((WatchListField)verticalWatchListFieldManager.getField(i)).setChange(bean.getChange());
										if(!bean.getPerChange().equals("UNCHANGED"))
											((WatchListField)verticalWatchListFieldManager.getField(i)).setPercentageDiff(bean.getPerChange());
										if(!bean.getLtp().equals("UNCHANGED"))
											((WatchListField)verticalWatchListFieldManager.getField(i)).setLastTradedPrice(bean.getLtp());
										break;
									}
								}
							}
						}
					}
				}
				}
		});
		
	}
//	public synchronized void streamDataUpdate(final int item,final SimpleItemUpdate update) {
//		LOG.print("=-=-=-=-=-=--==-=-=-===-=--==-=-=-=-==--=-=-=-=-==-=-=-=");
//
//		LOG.print(update.getNumFields()+"ON UPDATE");
//		if(sorting) return;
//		LOG.print(update.getFieldNewValue(item)+"ON UPDATE AFTER SORTING"+streamHash.get(new Integer(item-1)));
//
//		if(!(UiApplication.getUiApplication().getActiveScreen() instanceof MyWatchList))
//		{
//			streamDataService.closeConnection();
//			return;
//		}
//		String check = update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE);
//		if(update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE).equals("UNCHANGED")) 
//		{
//			check = (String)streamHash.get(new Integer(item-1));
//			LOG.print("update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE) : check"+check);
//
//		}
//		//LOG.print("(String)streamURLVector.elementAt(item-1) : "+(String)streamURLVector.elementAt(item-1));
//		LOG.print("update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE) : "+update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE));
//		//LOG.print("update.getFieldNewValue(StreamSchemaGroup.SYMBOL) : "+update.getFieldNewValue(StreamSchemaGroup.SYMBOL));
//		if(update.getFieldNewValue(StreamSchemaGroup.LTP).equals("UNCHANGED")/* || update.getFieldNewValue(StreamSchemaGroup.CHANGE_VALUE).equals("UNCHANGED") || update.getFieldNewValue(StreamSchemaGroup.PERCENT_CHANGE).equals("UNCHANGED")*/)
//			return;
//		//((WatchListField)verticalWatchListFieldManager.getField(i)).setUpdatedData(homeJSon.getSymbol(), homeJSon.getLastTradedPrice(), homeJSon.getChange(), homeJSon.getChangePercent()+"%", homeJSon.getCompanyCode(),watchListCompanies.elementAt(i).toString(), this,null,homeJSon,WatchListField.BSE,homeJSon.isStream());
//		int cnt = verticalWatchListFieldManager.getFieldCount();
//		for(int i=0; i<cnt; i++)
//		{
//			if(verticalWatchListFieldManager.getField(i) instanceof WatchListField)
//			{
//				if((check).equals(((WatchListField)verticalWatchListFieldManager.getField(i)).getCompanyCode())){
//					LOG.print(update.getFieldNewValue(StreamSchemaGroup.COMPANYCODE));
//					LOG.print(((WatchListField)verticalWatchListFieldManager.getField(i)).getCompanyCode());
//					LOG.print("LTP : "+update.getFieldNewValue(StreamSchemaGroup.LTP));
//					LOG.print("CHANGE_VALUE : "+update.getFieldNewValue(StreamSchemaGroup.CHANGE_VALUE));
//
//					if(!update.getFieldNewValue(StreamSchemaGroup.LTP).equalsIgnoreCase("null"))
//					{
//						if(!update.getFieldNewValue(StreamSchemaGroup.CHANGE_VALUE).equals("UNCHANGED"))
//							((WatchListField)verticalWatchListFieldManager.getField(i)).setChange(update.getFieldNewValue(StreamSchemaGroup.CHANGE_VALUE));
//						if(!update.getFieldNewValue(StreamSchemaGroup.PERCENT_CHANGE).equals("UNCHANGED"))
//							((WatchListField)verticalWatchListFieldManager.getField(i)).setPercentageDiff(update.getFieldNewValue(StreamSchemaGroup.PERCENT_CHANGE));
//						((WatchListField)verticalWatchListFieldManager.getField(i)).setLastTradedPrice(update.getFieldNewValue(StreamSchemaGroup.LTP));
//						//((WatchListField)verticalWatchListFieldManager.getField(i)).setStream();
//					}
//				}
//			}
//		}
//	}
	public void refreshComponentsFields() {
		if(refreshme.getLoading())
			refreshme.setLoading(false);
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
		int cnt = verticalWatchListFieldManager.getFieldCount();
		//LOG.print("RESET");
		jx++;
		for(int i=0; i<cnt; i++)
		{
			if(verticalWatchListFieldManager.getField(i) instanceof WatchListField)
			{
				if(((WatchListField)verticalWatchListFieldManager.getField(i)).getStream()>0)
				{
					((WatchListField)verticalWatchListFieldManager.getField(i)).setStream(0);
				}
				
			}
		}
			}});
	}

	public void setReturnString(String strResponse, int id) {
		if(id == WATCHLISTSCRIPDATA)
		{
			LOG.print(strResponse);
			if(strResponse.equalsIgnoreCase("0"))
			{
				Utils.setWatchListedCompanyRecords(new Vector());
				MyWatchList myWatchList = new MyWatchList(new Vector());
			}
			else
			{
			Vector v = HomeJsonParser.getVector(strResponse);
			if(v.size()==0)
			{
				Utils.setWatchListedCompanyRecords(new Vector());
			}else{
				Vector vx = new Vector();
			for(int i=0; i<v.size(); i++)
			{
				vx.addElement(((HomeJson) v.elementAt(i)).getDisplayName1());
			}
			Utils.setWatchListedCompanyRecords(vx);
			}
			MyWatchList myWatchList = new MyWatchList(v);
			}
		}	
	
}


}


