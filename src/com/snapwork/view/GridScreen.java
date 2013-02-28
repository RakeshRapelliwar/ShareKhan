package com.snapwork.view;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomLinkButtonUnderLine;
import com.snapwork.components.GridFieldManager;
import com.snapwork.components.HomeScreenBanner;
import com.snapwork.components.TitleBar;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.Json;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class GridScreen extends MainScreen implements ActionListener,ReturnString,AutoRefreshableScreen {

	public static final byte COMPONENT_BSE_CHART = 0;
	public static final byte BANNERS_DATA = 1;

	public static final String BSE_COMPANYCODE = "17023928";
	public static final String NSE_COMPANYCODE = "17023929";

	private MenuItem _logoutAndExit = null;
	private BottomMenu bottomMenu = null;
	private GridScreen instance;
	private boolean status;

	public GridScreen() {
		super(NO_SYSTEM_MENU_ITEMS);
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		AutoScreenRefresherThread.onLoad = false;
		instance = this;
		createUI();
	}

	public HomeScreenBanner bseBanner = null;
	public HomeScreenBanner nseBanner = null;
	MarketStatusField lblMarketStatusL = null;
	MarketStatusField lblMarketStatusR = null;
	CustomLinkButtonUnderLine refreshButton = null;

	public void createUI() {
		//Sets the title
		setTitle(new TitleBar("ShareMobile"));

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
		Bitmap[] bitmapTrue = getBottomMenuImagesSetting(true);
		Bitmap[] bitmapFalse = getBottomMenuImagesSetting(false);
		// Bitmap[] bitmapFalse = getBottomMenuImagesSetting(false);
		lblMarketStatusR = new MarketStatusField("", FOCUSABLE | FIELD_HCENTER);
		lblMarketStatusR.setText("EQ : Pending...");

		lblMarketStatusR.setFont(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT));
		
		lblMarketStatusL = new MarketStatusField("", FOCUSABLE | FIELD_HCENTER);
		lblMarketStatusL.setText("COM : Pending...");

		lblMarketStatusL.setFont(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT));
		new ReturnStringParser(Utils.getMarketStatusUrl(), lblMarketStatusL.COMP_REQ_ID, this);
		//String args[] = {"Markets","Market Watch","Order Book", "Holdings","Reports","Trade", "Profile","Account","Quotes"};
		String args[] = {"Markets","Market Watch","Order Book", "Holdings","Reports","Trade", "Profile","Account","FundTransfer","SpanCalc"};
		GridFieldManager grid = new GridFieldManager(3,0); 
		for(int i=0;i<bitmapTrue.length;i++){
			//boolean webhold = false;
			String text = "";
			if(i==2)
			{
				text = "Order Book";
				//webhold = true;
			}
			else if(i==3)
			{
				text = "Holdings";
				//webhold = true;
			}
			else if(i==4)
			{
				text = "Reports";
				//webhold = true;
			}
			else if(i==6)
			{
				text = "Profile";
				//webhold = true;
			}
			else if(i==7)
			{
				text = "Accounts";
				//webhold = true;
			}


			grid.add(createBitmapField(bitmapFalse[i],bitmapTrue[i],args[i],AppConstants.gridMenuCommands[i], false,text));
		}
		if(AppConstants.screenHeight>400)
			mainManager.add(Utils.separator(8, 0x000000));
		else
			mainManager.add(Utils.separator(3, 0x000000));
		
		HorizontalFieldManager hz = new HorizontalFieldManager(FIELD_HCENTER);
		hz.add(lblMarketStatusL);
		hz.add(lblMarketStatusR);
		mainManager.add(hz);
		if(AppConstants.screenHeight>400)
			mainManager.add(Utils.separator(10, 0x000000));
		else
			mainManager.add(Utils.separator(3, 0x000000));
		mainManager.add(grid);
		add(mainManager);

		_logoutAndExit = new MenuItem("Logout & Exit",100,100) {
			public void run() {
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_EXIT,null));
			}
		};

		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_GRID_SCREEN, AppConstants.bottomMenuCommands);

	}


	public void actionPerfomed(byte Command, Object sender) {

		ActionInvoker.processCommand(new Action(Command,sender));
	}

	protected  void makeMenu(Menu menu, int instance)
	{
		if(status)
		{
			ContextMenu contextMenu = ContextMenu.getInstance();
			contextMenu.setTarget(this);
			contextMenu.clear();
			this.makeContextMenu(contextMenu);
			menu.deleteAll();
			menu.add(contextMenu);
			status = false;
		}
		else
			menu.add(_logoutAndExit);
	}
	public boolean keyChar( char key, int status, int time )
	{
		return false;
	}

	public boolean keyDown( int keyCode, int time )
	{

		int key = Keypad.key( keyCode );
		/*if(key == Keypad.KEY_MENU) {
                        UiApplication.getUiApplication().invokeLater(new Runnable() {
                                public void run() {
                                }
                        });
                }
                else */if(key == Keypad.KEY_END)
                {
                	LOG.print("KEY_END EXIT from app");
                	System.exit(0);
                }else
                	return super.keyDown(keyCode, time);

                return true;
	}

	public boolean onClose()
	{
		Action action = new Action(ActionCommand.CMD_EXIT, null);
		ActionInvoker.processCommand(action);
		return false;
	}

	public boolean keyUp( int keyCode, int time )
	{
		return super.keyUp(keyCode, time);
	}

	public Vector getComponentData() {
		return null;
	}

	public BitmapField createBitmapField(final Bitmap bmpFocusIcon, final Bitmap bmpUnFocusIcon,String text, final byte command, final boolean webload, final String webtext) {
		final String aText = text;
		BitmapField objField = new BitmapField(bmpUnFocusIcon, BitmapField.FOCUSABLE | BitmapField.FIELD_HCENTER | BitmapField.FIELD_VCENTER) {

			private boolean isFocused = false;

			protected void onFocus(int direction) {
				isFocused = true;
				invalidate();
			}

			protected void onUnfocus() {
				isFocused = false;
				invalidate();
			}

			protected void paint(Graphics graphics) {
				int d=0;
				if(isFocused) {
					d=0;
					if(bmpFocusIcon!=null)
						graphics.drawBitmap(/*(AppConstants.screenWidth/AppConstants.bottomMenuCommands.length-getBitmapWidth())/2*/0, d, bmpFocusIcon.getWidth()/*getBitmapWidth()*/, bmpFocusIcon.getHeight()/*getBitmapHeight()*/, bmpFocusIcon, 0, 0);
					graphics.setColor(0xef9e10);
					graphics.drawRect(0, 0, getWidth()-10, getHeight()-10);
				} else {
					d=0;
					if(bmpUnFocusIcon!=null)
						graphics.drawBitmap(/*(AppConstants.screenWidth/AppConstants.bottomMenuCommands.length-getBitmapWidth())/2*/0, d, bmpUnFocusIcon.getWidth() /*getBitmapWidth()*/, bmpFocusIcon.getHeight()/*getBitmapHeight()*/, bmpUnFocusIcon, 0, 0);
					// graphics.setColor(0xeeeeee);
				}
				//graphics.setFont(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT));
				//int x = (AppConstants.screenWidth/AppConstants.bottomMenuCommands.length-getBitmapWidth())/2+(getBitmapWidth()/2)-(graphics.getFont().getAdvance(aText)/2);
				//if(x<1)x=1;
				//graphics.drawText(aText, x,getBitmapHeight() - (graphics.getFont().getHeight()+2)+4+d);
			}

			protected void drawFocus(Graphics graphics, boolean on) {
			}

			protected void layout(int width, int height) {
				setExtent(bmpUnFocusIcon.getWidth()+10,getBitmapHeight()+10);
			}

			protected boolean navigationClick(int status, int time) {
				if(webload)
				{
					ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							
							
							
							actionPerfomed(command, null);
						}
					});
				}
				else
				{
					String tx = aText;
					tx = tx.replace(' ', '_');
					if(tx.equalsIgnoreCase("Account"))
					{
						//Account implementation not yet added
						//String url = AppConstants.domainUrl+"EQreports.php?custId="+UserInfo.getUserID()+"&rtype=margin&debug=2";
						String url = AppConstants.domainUrl+"SK_android/controller.php?RequestId=steqm01&custId="+UserInfo.getUserID()+"&rtype=margin";
						Vector vec = new Vector();
						vec.addElement("Account");
						Vector e = new Vector();
						e.addElement(url);
						vec.addElement(e);
						//actionListener.actionPerfomed(ActionCommand.CMD_SLIDE_VIEW, vec);
						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SLIDE_VIEW,vec));

					}
					else if(tx.equalsIgnoreCase("Reports") || tx.equalsIgnoreCase("Order_Book"))
					{
						Vector vec = new Vector();
						vec.addElement(tx);
						actionPerfomed(ActionCommand.CMD_REPORTS_SCREEN, vec);
					}
					else if(tx.equalsIgnoreCase("Holdings"))
					{
						Vector dataUrl = new Vector();
						//String url = AppConstants.domainUrl+"EQreports.php?custId="+UserInfo.getUserID()+"&rtype=dpsr&debug=2";
						String url = AppConstants.domainUrl+"SK_android/controller.php?RequestId=steqm01&custId="+UserInfo.getUserID()+"&rtype=margin";
						dataUrl.addElement(tx);
						dataUrl.addElement(url);
						ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));

					}
					else if(tx.equalsIgnoreCase("FundTransfer"))
					{
						Browser.getDefaultSession().displayPage(AppConstants.fundTransferUrl);
					}
					else if(tx.equalsIgnoreCase("SpanCalc"))
					{
						Dialog.alert("Span Calc.");
						//Browser.getDefaultSession().displayPage(AppConstants.fundTransferUrl);
					}
					else
					{
						
						Utils.FNO_SEARCH = false;
						actionPerfomed(command, null);
					}

				}       
				return true;
			}

		};

		return objField;
	}

	public Bitmap[] getBottomMenuImagesSetting(boolean isFocusImage) {
		//String[] strImagePath = {"market","watchlist2","orderbook","Holdings","reports","trade","profile","account","getquote2"};
		String[] strImagePath = {"market","watchlist2","orderbook","holdings","reports","trade","profile","account","fundtransfer","SpanCalc"};
		Bitmap[] bitmap = new Bitmap[strImagePath.length];
		LOG.print("DeviceInfo.getDeviceName() : "+DeviceInfo.getDeviceName());
		LOG.print("DeviceInfo.getPlatformVersion() : "+DeviceInfo.getPlatformVersion());
		LOG.print("DeviceInfo.getSoftwareVersion() : "+DeviceInfo.getSoftwareVersion());
		String str = "-S.png";
		if(!isFocusImage) {
			str = "-R.png";
		}
		for(int i=0;i<strImagePath.length;i++) {
			if(AppConstants.screenHeight>240)
			{
				try {
					bitmap[i] = Bitmap.getBitmapResource("h"+strImagePath[i]+str);
				} catch (Exception e) {
					LOG.print("Image Error "+"h"+strImagePath[i]+str);
				}
			}
			else
			{
				try {
					bitmap[i] = Bitmap.getBitmapResource(strImagePath[i]+str);
				} catch (Exception e) {
					LOG.print("Image Error "+strImagePath[i]+str);
				}

			}
		}

		return bitmap;
	}

	protected void paintBackground(Graphics graphics) {
		graphics.fillRect(0,0,getWidth(),getHeight());
		graphics.setColor(Color.BLACK);
	}


	public void setReturnString(String string, int id) {
		if(!(UiApplication.getUiApplication().getActiveScreen() instanceof GridScreen))
			return;

		if(id==lblMarketStatusL.COMP_REQ_ID) {
			if(string.equalsIgnoreCase("-1") || string.equalsIgnoreCase("-2"))
			{
				return;
			}
			else{
				LOG.print("responseString : "+string);
				Json js = new Json(string);
				for(int i=0;i<js.getdata.size();i++){
				Hashtable ht = (Hashtable) js.getdata.elementAt(i);
				final String exchange = (String)ht.get("EXCHANGE");
				final String marketStatus = (String)ht.get("STATUS");
				if(marketStatus!=null){
				LOG.print("marketStatus : "+marketStatus);
				if(exchange.equalsIgnoreCase("BSE"))
					lblMarketStatusL.marketStatus = (marketStatus.toLowerCase().equals("closed")==true ?  0 :  1);
				else if(exchange.equalsIgnoreCase("MCX"))
					lblMarketStatusR.marketStatus = (marketStatus.toLowerCase().equals("closed")==true ?  0 :  1);
				LOG.print("EQ :"+marketStatus);
				
				UiApplication.getUiApplication().invokeLater(new Runnable() {

					public void run() {
						if(exchange.equalsIgnoreCase("BSE"))
							lblMarketStatusL.setText("EQ :"+marketStatus+"     ");//Space added to avoid measurement coding
						else if(exchange.equalsIgnoreCase("MCX"))
							lblMarketStatusR.setText("COM :"+marketStatus+"     ");//Space added to avoid measurement coding
					}
				});
				}
				else
				{
					//todo
				}
				}
			}
		}
	}

	class MarketStatusField extends LabelField {
		int COMP_REQ_ID = 123453;
		int marketStatus = -1;

		MarketStatusField(String text,long style) {
			super(text,style);
		}
		protected void drawFocus(Graphics arg0, boolean arg1) {
		}
		protected void paint(Graphics graphics) {
			if(marketStatus==-1) {
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			} else if(marketStatus==0) {
				graphics.setColor(Color.RED);
				graphics.fillArc(0,0,getHeight(),getHeight(),0,360);
				graphics.setColor(Color.WHITE);
				graphics.drawText(this.getText(), getHeight()+3, 0);
			} else if(marketStatus==1) {
				graphics.setColor(Color.GREEN);
				graphics.fillArc(0,0,getHeight(),getHeight(),0,360);
				graphics.setColor(Color.WHITE);
				graphics.drawText(this.getText(), getHeight()+3, 0);
			}
		}               
		protected boolean navigationClick(int arg0, int arg1) {
			status = true;
			return super.navigationClick(arg0, arg1);
		}
	}

	public void refreshFields() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				LOG.print("refreshing field market status.........");
				new ReturnStringParser(Utils.getMarketStatusUrl(), lblMarketStatusL.COMP_REQ_ID, instance);	
			}
		});
	}

}
