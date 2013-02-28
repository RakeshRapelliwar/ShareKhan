package com.snapwork.view.trade;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.KeyValueBean;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TradeScreenBanner;
import com.snapwork.interfaces.ReturnData;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.TradeNowModifyParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.Utils;
import com.snapwork.view.CompanyDetailsSnippetsScreen;

public class TradeNowModifyScreen extends MainScreen implements ActionListener, AutoRefreshableScreen, ReturnData, ReturnDataWithId, RemovableScreen
{
private TradeScreenBanner banner;
private BottomMenu bottomMenu = null;
private HomeJson bann;
private Vector urls;
private String screenString;
private String company_code = "";
private Vector storedVector;
private boolean refresh;
private final static int REFRESH_ID_SENSEX = 442;
private TradeNowModifyScreen object = this;
private String modifyurl;

	public TradeNowModifyScreen(String screenString, Vector urls,HomeJson banner) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		if(AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			Utils.snippetDiff = 15;
		refresh = true;
 this.bann = banner;
 this.urls = urls;
 this.screenString = screenString;
 String url = (String) urls.elementAt(0);
 modifyurl = (String) urls.elementAt(1);
 //url = url + "&debug=2";
  //System.out.println(url);
 LOG.print(url);
 
  new TradeNowModifyParser(url,this);
		
	}
	protected void paintBackground(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());/*
		super.paint(graphics);*/
		
		// graphics.setBackgroundColor(Color.BLACK);
		    
		    // Clears the entire graphic area to the current background
		  //  graphics.clear();   
	}

	public void createUI(String strTitle,final  HomeJson homeJson, final Vector confirmVector) {
		//set Title
		final Bitmap bmp = ImageManager.getTradeNow();
		for(int i=0;i<confirmVector.size();i++)
		{
			KeyValueBean modifyBean = (KeyValueBean)confirmVector.elementAt(i);
			String[] data2 = new String[2];
				data2[0]=modifyBean.getKey();
				data2[1] = modifyBean.getValue();
				if(data2[0].equalsIgnoreCase("Exchange"))
				{
					if(data2[1].equalsIgnoreCase("NSE"))
					{
						bann.setExchange(false);
						bann.setCompanyCode(bann.getSymbol());
						refreshFields();
					}
					else
					{
						bann.setExchange(true);
						bann.setCompanyCode(AppConstants.source);
						refreshFields();
					}
				}
		}
		banner = new TradeScreenBanner(FIELD_HCENTER, bann.getSymbol(), bann.isExchange());
		//banner.setSource(" ");
		banner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()));
		
		
		TitleBar titleBar = new TitleBar(strTitle); 
		setTitle(titleBar);
		final int titleBarHeight = titleBar.getPreferredHeight();

		VerticalFieldManager vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}  
			
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(maxWidth,maxHeight);
			}
		};
		
		
		
		
		HorizontalFieldManager hfmobj = new HorizontalFieldManager(HorizontalFieldManager.HORIZONTAL_SCROLL | USE_ALL_WIDTH)
		{
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}
			
		protected void sublayout(int arg0, int arg1) {
			layoutChild(getField(0), bmp.getWidth(), bmp.getHeight());
			layoutChild(getField(1), bmp.getWidth(), bmp.getHeight());
			setPositionChild(getField(0), 5, 0);
			setPositionChild(getField(1), AppConstants.screenWidth-bmp.getWidth()-5, 0);
			//super.sublayout(AppConstants.screenWidth,arg1);
			setExtent(AppConstants.screenWidth,bmp.getHeight());
		}	
		};
			final String textButton = "Confirm";
			BitmapField btnConfirm = new BitmapField(bmp,  FOCUSABLE | DrawStyle.HCENTER)  {

				protected boolean navigationClick(int status,int time) {
					if(!Utils.sessionAlive)
					{
						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
					}
					else
					{
					Vector second = new Vector();
					String confirm = (String) urls.elementAt(1);
					String modify = (String) urls.elementAt(2);
					urls.removeElementAt(2);
					urls.removeElementAt(1);
					confirm = Utils.findAndReplace(confirm,"##company_code##",company_code);
					//modify = Utils.findAndReplace(modify,"##company_code##",company_code);
					urls.addElement(confirm);
					urls.addElement(modify);
					second.addElement(urls);
					LOG.print("Updated URl's :"+confirm);
					second.addElement(bann);
					second.addElement(screenString);
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_TRADE_RESULT,second));
					}
					return super.navigationClick(status, time);
				}
				private boolean isFocused = false;
				protected void onFocus(int direction) 
				{
					isFocused = true;
					invalidate();
				}
				protected void onUnfocus() {
					isFocused = false;
					invalidate();
				}
				protected void paintBackground(Graphics graphics) {
					graphics.setBackgroundColor(Color.BLACK);
					graphics.clear();
				}
				protected void paint(Graphics graphics) 
				{
					if(isFocused) {
						graphics.setColor(Color.ORANGE);
					} else {
						graphics.setColor(0xeeeeee);
					}
					//graphics.fillRect(0, 0, getWidth(), getHeight());
					graphics.drawBitmap(0,0, bmp.getWidth(), bmp.getHeight(), bmp, 0, 0);                                        
					graphics.drawText(textButton,(bmp.getWidth()/2)-(getFont().getAdvance(textButton)/2),(bmp.getHeight()/2)-(getFont().getHeight()/2));
					
				}
				protected void drawFocus(Graphics graphics, boolean on) 
				{

				}
				public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
				public int getPreferredHeight() {return  bmp.getHeight();}//(getFont().getHeight()*2)-(getFont().getHeight()/2);}
				public int getPreferredWidth() {return bmp.getWidth();}//getFont().getAdvance("Trade Now");}
				
				protected void layout(int arg0, int arg1) {
				setExtent(getPreferredWidth(), getPreferredHeight());
				}
				};
				
				final String textButtonModify = "Modify";
				BitmapField btnModify = new BitmapField(bmp,  FOCUSABLE | DrawStyle.HCENTER)  {

					protected boolean navigationClick(int status,int time) {
						if(!Utils.sessionAlive)
						{
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
						}
						else
						{
							LOG.print("SCreenString "+ screenString);
						if(screenString.equalsIgnoreCase("ReportNetPosition"))
						{
							onClose();
						}
						else if(screenString.equalsIgnoreCase("DPSR"))
						{
							onClose();
						}
						else
						{
							ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
							new TradeNowMainScreenCM("tradenow","",new HomeJson());
							//onClose();
						}

				    	/*if(screenString.equalsIgnoreCase("tradenow")) {
				    		if(UiApplication.getUiApplication().getActiveScreen() instanceof TradeNowMainScreen) {
				    			((TradeNowMainScreen)UiApplication.getUiApplication().getActiveScreen()).setTitle(new TitleBar("Modify Order"));
				    			((TradeNowMainScreen)UiApplication.getUiApplication().getActiveScreen()).setExchangeDisable();
				    		}
				    	}*/

					}
					return super.navigationClick(status, time);
					}
					private boolean isFocused = false;
					protected void onFocus(int direction) 
					{
						isFocused = true;
						invalidate();
					}
					protected void onUnfocus() {
						isFocused = false;
						invalidate();
					}
					protected void paintBackground(Graphics graphics) {
						graphics.setBackgroundColor(Color.BLACK);
						graphics.clear();
					}
					protected void paint(Graphics graphics) 
					{
						if(isFocused) {
							graphics.setColor(Color.ORANGE);
						} else {
							graphics.setColor(0xeeeeee);
						}
						//graphics.fillRect(0, 0, getWidth(), getHeight());
						graphics.drawBitmap(0,0, bmp.getWidth(), bmp.getHeight(), bmp, 0, 0);                                        
						graphics.drawText(textButtonModify,(bmp.getWidth()/2)-(getFont().getAdvance(textButtonModify)/2),(bmp.getHeight()/2)-(getFont().getHeight()/2));
						
					}
					protected void drawFocus(Graphics graphics, boolean on) 
					{

					}
					public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
					public int getPreferredHeight() {return  bmp.getHeight();}//(getFont().getHeight()*2)-(getFont().getHeight()/2);}
					public int getPreferredWidth() {return bmp.getWidth();}//getFont().getAdvance("Trade Now");}
					
					protected void layout(int arg0, int arg1) {
					setExtent(getPreferredWidth(), getPreferredHeight());
					}
					};
			vfm.add(banner);
			vfm.add(Utils.separator(5, 0x000000));
			LOG.print("TradeNowModifyScreen confirmVector size "+confirmVector.size());
			if(confirmVector.size()!=0)
			{
				vfm.add(new ModifyView(confirmVector));
			}
			vfm.add(Utils.separator(10, 0x000000));
			hfmobj.add(btnConfirm);
			hfmobj.add(btnModify);
		vfm.add(hfmobj);
		vfm.add(Utils.separator(10, 0x000000));
		add(vfm);
		final TradeNowModifyScreen rcm = this;
		if(!(UiApplication.getUiApplication().getActiveScreen() instanceof TradeNowMainScreen))
		{
			UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				//ScreenInvoker.removeRemovableScreen(); 
				ScreenInvoker.pushScreen(rcm, true, true);
			}
		});
		}
		else
		{
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
			ScreenInvoker.pushScreen(rcm, true, true);
				}
			});
		}
		/*if(UiApplication.getUiApplication()TradeNowModifyScreen instanceof TradeNowMainScreen && TradeNowMainScreen.CLOSE)
		{UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				TradeNowMainScreen.CLOSE = false;
				ScreenInvoker.removeRemovableScreen();  
			}
		});
		}*/
		/*UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				UiApplication.getUiApplication().pushScreen(object);
			}
		});*/
		
		
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
	}

	
		
	public LabelField getLabels(final String text1, final String text2, final String text3)
	{
		return new LabelField("",FOCUSABLE)
		{
			public void setFont(Font arg0) {
				super.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			}
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x000000);
				graphics.fillRect(0, 0, getPreferredWidth(),getPreferredHeight());
			}
			protected void paint(Graphics graphics) {
				graphics.setColor(0x222222);
				graphics.fillRect(0, 2+(getFont().getHeight()), getPreferredWidth(), 4+(getFont().getHeight()*2));
				graphics.setColor(0x999999);
				graphics.drawText(text1,5,2);
				graphics.drawText(text2,5,4+(getFont().getHeight()));
				graphics.drawText(text3, 5, 6+(getFont().getHeight()*2));
			}
			public int getPreferredHeight() {
				return 6+(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight()*5);
			}
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}
			protected void layout(int width, int height) {
				super.layout(width, getPreferredHeight());
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
		};
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
	public void setData(Vector vector) {
		System.out.println("Response TradeNowModify");
		if(vector.size()!=0)
		{
			storedVector = vector;
			for(int i=0;i<vector.size()-1;i++){
		KeyValueBean bean = (KeyValueBean)vector.elementAt(i);
		//System.out.println(bean.getKey()+" : "+bean.getValue());
		if(i==4)
		{
			vector.removeElementAt(4);
			vector.addElement(bean);
		}
		//System.out.println("------------------------------------------------");
			}
		}
		
		if(vector.size()!=0)
		{
			for(int i=0;i<vector.size()-1;i++){
		KeyValueBean bean = (KeyValueBean)vector.elementAt(i);
		//System.out.println(bean.getKey()+" : "+bean.getValue());
		if(bean.getKey().equalsIgnoreCase("company_code"))
			company_code = bean.getValue();
		//System.out.println("------------------------------------------------");
			}
		}
		createUI("Order Confirmation",bann, vector);
	}
	public void actionPerfomed(byte Command, Object data) {
		 switch(Command) {
	  		case ActionCommand.CMD_GRID_SCREEN:
	      	case ActionCommand.CMD_WATCHLIST_SCREEN:
	      	case ActionCommand.CMD_BSE_GL_SCREEN:
	      	case ActionCommand.CMD_NEWS_SCREEN:
	      	case ActionCommand.CMD_SEARCH_SCREEN:
	      	case ActionCommand.CMD_REPORTSB_SCREEN:
	      		SlideViewOrderCancel.orderCancel = false;
				 ActionInvoker.processCommand(new Action(Command,null));
	      		break;
	          default:
		 };
	}
	public boolean onClose() {
		//tradeNowModifyParser.httpKill();
		//httpKill = true;
		SlideViewOrderCancel.orderCancel = false;
		return super.onClose();	
	}
	public boolean onSavePrompt()
	{
		return true;
	}

	public boolean keyChar( char key, int status, int time )
	{
		return super.keyChar(key, status, time);
	}

	public boolean keyUp( int keyCode, int time )
	{
		return super.keyUp(keyCode, time);
	}
	public boolean keyDown( int keyCode, int time )
	{
		int key = Keypad.key( keyCode );
		final TradeNowModifyScreen tNowModifyScreen = this;
		 if(key == Keypad.KEY_MENU) {
			 	UiApplication.getUiApplication().invokeLater(new Runnable() {
	            		public void run() {
	            			try {
								if(bottomMenu != null)
									bottomMenu.autoAttachDetachFromScreen();
								else
								{
									bottomMenu = BottomMenu.getBottomMenuInstance(tNowModifyScreen,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
									bottomMenu.autoAttachDetachFromScreen();
								}
							} catch (Exception e) {
							}
	            		}
	            	});
 		}
         else if(key == Keypad.KEY_ESCAPE) {
			
			synchronized( UiApplication.getEventLock() ){
            
            if(isDisplayed()) 
         	   {
            	SlideViewOrderCancel.orderCancel = false;
         	   	close();
         	   }
}
			ScreenInvoker.removeRemovableScreen();
			UiApplication.getUiApplication().getActiveScreen().close();
			//actionPerfomed(ActionCommand.CMD_GRID_SCREEN, null);
      	   
		} 
		else if(key == Keypad.KEY_END)
		{
			LOG.print("KEY_END EXIT from app");
			SlideViewOrderCancel.orderCancel = false;
			System.exit(0);
		}
		else
			return super.keyDown(keyCode, time);

		return true;
	}
	public void refreshFields() {
			if(refresh){
				refresh = false;
				ReturnDataWithId idx = this;
			HomeJsonParser hj = new HomeJsonParser(Utils.getCompanyLatestTradingDetailsURL(bann.getCompanyCode()), idx, REFRESH_ID_SENSEX);
			}
	}
	public void setData(Vector vector, int id) {
		LOG.print("REFERSH RESPONSE BEFORE");
		if(!(UiApplication.getUiApplication().getActiveScreen() instanceof TradeNowModifyScreen))
    		return;
    	if(id == REFRESH_ID_SENSEX){
    		LOG.print("REFERSH RESPONSE");
    		refresh = true;
    		final HomeJson homeJson = (HomeJson) vector.elementAt(0);
    		bann = homeJson;
    		UiApplication.getUiApplication().invokeLater(new Runnable() {
    			
    			public void run() {
    				banner.setStartFlag(false);
    				banner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()));
    				banner.startUpdate();
    				}
    			});
    	}
	}

}
