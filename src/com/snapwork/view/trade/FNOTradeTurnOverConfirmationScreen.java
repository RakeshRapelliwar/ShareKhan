package com.snapwork.view.trade;

import java.util.Hashtable;
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
import com.snapwork.beans.Expiry;
import com.snapwork.beans.FNOOrderConfirmationBean;
import com.snapwork.beans.FNOTradeBean;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.KeyValueBean;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TradeScreenFNObanner;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.parsers.TradeNowMainParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FNOTradeConfiguration;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;


public class FNOTradeTurnOverConfirmationScreen  extends MainScreen implements ActionListener, RemovableScreen, ReturnDataWithId{

	private BottomMenu bottomMenu = null;
	private String screenTitle;
	private FNOOrderConfirmationBean tradeOrderConfirmationBean;
	private ReturnDataWithId returnDataWithId;
	private TradeScreenFNObanner banner = null;
	private int SQFID = 574;
	public void setScreenTitle(String screenTitle) {
		this.screenTitle = screenTitle;
	}

	public String getScreenTitle() {
		return screenTitle;
	}

	public void setTradeOrderConfirmationBean(FNOOrderConfirmationBean tradeOrderConfirmationBean) {
		this.tradeOrderConfirmationBean = tradeOrderConfirmationBean;
	}

	public FNOOrderConfirmationBean getTradeOrderConfirmationBean() {
		return tradeOrderConfirmationBean;
	}

	public FNOTradeTurnOverConfirmationScreen(String screenTitle,FNOOrderConfirmationBean tradeOrderConfirmationBean) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		setScreenTitle(screenTitle);
		setTradeOrderConfirmationBean(tradeOrderConfirmationBean);
		returnDataWithId = this;
		createUI();
	}

	VerticalFieldManager verticalFieldManager;
	
    public void createUI() {

        //Set Title Bar
        TitleBar titleBar = new TitleBar(getScreenTitle()); 
        setTitle(titleBar);
        
        int blackColor = Color.BLACK;
        int grayColor = 0x333333;
        HomeJson bannerData = getTradeOrderConfirmationBean().getBannerData();
        if(banner==null) {
        	LOG.print("getTradeOrderConfirmationBean().getBannerDate() "+getTradeOrderConfirmationBean().getBannerDate());
        	LOG.print("Expiry.getText(getTradeOrderConfirmationBean().getBannerDate()) "+Expiry.getText(getTradeOrderConfirmationBean().getBannerDate()));
			banner = new TradeScreenFNObanner(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),bannerData.getSymbol(), Expiry.getText(getTradeOrderConfirmationBean().getBannerDate()),bannerData.getCompanyCode(),true);
			banner.setData(bannerData.getLastTradedPrice(), bannerData.getChange(), (bannerData.getPercentageDiff().indexOf("-") == -1 ? "+"+bannerData.getPercentageDiff() : bannerData.getPercentageDiff()),bannerData.getOpenInterest(), bannerData.getVolume());
		}
        Vector vector = new Vector();
        final KeyValueBean kbean = new KeyValueBean();
        LOG.print("tradeOrderConfirmationBean.getSymbolName() : "+tradeOrderConfirmationBean.getSymbolName());
        kbean.setKey("Stock Name"); kbean.setValue(tradeOrderConfirmationBean.getSymbolName()); vector.addElement(kbean.copy());
        kbean.setKey("Stock Name"); kbean.setValue(tradeOrderConfirmationBean.getSymbolName()); vector.addElement(kbean.copy());
        kbean.setKey("Instrument"); kbean.setValue(tradeOrderConfirmationBean.getInstType()); vector.addElement(kbean.copy());
        kbean.setKey("Expiry"); kbean.setValue(tradeOrderConfirmationBean.getExpiry()); vector.addElement(kbean.copy());
        if(tradeOrderConfirmationBean.getInstType().trim().toLowerCase().indexOf("opt")!=-1) {
            kbean.setKey("Opt/Strike Price"); kbean.setValue(tradeOrderConfirmationBean.getOptionType()+"/"+tradeOrderConfirmationBean.getStrikePrice()); vector.addElement(kbean.copy());
            kbean.setKey("Action/Qty"); kbean.setValue(getActionFullName(tradeOrderConfirmationBean.getAction())+"/"+tradeOrderConfirmationBean.getQty()); vector.addElement(kbean.copy());
            kbean.setKey("Limit Price"); kbean.setValue(tradeOrderConfirmationBean.getLimitPrice()); vector.addElement(kbean.copy());
            kbean.setKey("Trigger Price"); kbean.setValue(tradeOrderConfirmationBean.getStopPrice()); vector.addElement(kbean.copy());
            //kbean.setKey("Disclosed Qty"); kbean.setValue(tradeOrderConfirmationBean.getDiscQty()); vector.addElement(kbean.copy());
        } else {
	       kbean.setKey("Action/Qty"); kbean.setValue(getActionFullName(tradeOrderConfirmationBean.getAction())+"/"+tradeOrderConfirmationBean.getQty()); vector.addElement(kbean.copy());
	        kbean.setKey("Limit Price"); kbean.setValue(tradeOrderConfirmationBean.getLimitPrice()); vector.addElement(kbean.copy());
	        kbean.setKey("Trigger Price"); kbean.setValue(tradeOrderConfirmationBean.getStopPrice()); vector.addElement(kbean.copy());
	        //kbean.setKey("Disclosed Qty"); kbean.setValue(tradeOrderConfirmationBean.getDiscQty()); vector.addElement(kbean.copy());
        }
        
        /*final Bitmap bmpTradeNow = ImageManager.getTradeNow();

        final String strConfirm = "Confirm";
		BitmapField btnConfirm = new BitmapField(bmpTradeNow,  FOCUSABLE | DrawStyle.HCENTER | FIELD_LEFT)  {

			protected boolean navigationClick(int status,int time) {
				confirmAction();
				return true;
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
					graphics.setColor(Color.WHITE);
				}

				graphics.drawBitmap(0,0, bmpTradeNow.getWidth(), bmpTradeNow.getHeight(), bmpTradeNow, 0, 0);                                        
				graphics.drawText(strConfirm,(bmpTradeNow.getWidth()/2)-(getFont().getAdvance(strConfirm)/2),(bmpTradeNow.getHeight()/2)-(getFont().getHeight()/2));
			}

			protected void drawFocus(Graphics graphics, boolean on) 
			{

			}
			public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
			public int getPreferredHeight() {return  bmpTradeNow.getHeight();}
			public int getPreferredWidth() {return bmpTradeNow.getWidth();}

			protected void layout(int width, int height) {
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
		};
		
        final String strModify = "Modify";
		BitmapField btnModify = new BitmapField(bmpTradeNow,  FOCUSABLE | DrawStyle.HCENTER | FIELD_RIGHT)  {

			protected boolean navigationClick(int status,int time) {
				modifyAction();
				return true;
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
					graphics.setColor(Color.WHITE);
				}

				graphics.drawBitmap(0,0, bmpTradeNow.getWidth(), bmpTradeNow.getHeight(), bmpTradeNow, 0, 0);                                        
				graphics.drawText(strModify,(bmpTradeNow.getWidth()/2)-(getFont().getAdvance(strConfirm)/2),(bmpTradeNow.getHeight()/2)-(getFont().getHeight()/2));
			}

			protected void drawFocus(Graphics graphics, boolean on) 
			{

			}
			public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
			public int getPreferredHeight() {return  bmpTradeNow.getHeight();}
			public int getPreferredWidth() {return bmpTradeNow.getWidth();}

			protected void layout(int width, int height) {
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
		};
        
        HorizontalFieldManager horizontalFieldManager = new HorizontalFieldManager(USE_ALL_WIDTH) {
        	
        	int xPadding = 10;
        	
            public void paintBackground(Graphics graphics)
            {
                    graphics.setColor(Color.BLACK);
                    graphics.fillRect(0, 0, AppConstants.screenWidth, bmpTradeNow.getHeight()+bmpTradeNow.getHeight()/2);
            }

            protected void sublayout( int maxWidth, int maxHeight )
            {
        			layoutChild(getField(0), bmpTradeNow.getWidth(), bmpTradeNow.getHeight());
        			layoutChild(getField(1), bmpTradeNow.getWidth(), bmpTradeNow.getHeight());

                   	setPositionChild(getField(0), xPadding,bmpTradeNow.getHeight()/4 );
                	setPositionChild(getField(1),  AppConstants.screenWidth-xPadding-bmpTradeNow.getWidth(), bmpTradeNow.getHeight()/4);

           			setExtent(AppConstants.screenWidth, bmpTradeNow.getHeight()+bmpTradeNow.getHeight()/2);
            }
        };
        horizontalFieldManager.add(btnConfirm);
        horizontalFieldManager.add(btnModify);

        horizontalFieldManager.add(new NullField(FOCUSABLE));
        verticalFieldManager.add(horizontalFieldManager);*/
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
        final Bitmap bmp = ImageManager.getTradeNow();
        final String textButton = "Confirm";
		BitmapField btnConfirm = new BitmapField(bmp,  FOCUSABLE | DrawStyle.HCENTER)  {

			protected boolean navigationClick(int status,int time) {
				if(!Utils.sessionAlive)
				{
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
				}
				else
				{
				confirmAction();
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
					//onClose();
						modifyAction();
					}

			    	/*if(screenString.equalsIgnoreCase("tradenow")) {
			    		if(UiApplication.getUiApplication().getActiveScreen() instanceof TradeNowMainScreen) {
			    			((TradeNowMainScreen)UiApplication.getUiApplication().getActiveScreen()).setTitle(new TitleBar("Modify Order"));
			    			((TradeNowMainScreen)UiApplication.getUiApplication().getActiveScreen()).setExchangeDisable();
			    		}
			    	}*/

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
				if(banner!=null)
				vfm.add(banner);
				vfm.add(Utils.separator(5, 0x000000));
				LOG.print("TradeNowModifyScreen confirmVector size "+vector.size());
				if(vector.size()!=0)
				{
					vfm.add(new ModifyView(vector, false));
				}
				vfm.add(Utils.separator(10, 0x000000));
				hfmobj.add(btnConfirm);
				hfmobj.add(btnModify);
			vfm.add(hfmobj);
			vfm.add(Utils.separator(10, 0x000000));
			add(vfm);
        bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
    }
    public boolean onMenu(int instance) 
    {
            return false;
    }
    protected void makeMenu(Menu menu, int instance) {
            //super.makeMenu(menu, instance);
           /* Action action = new Action(ActionCommand.CMD_GRID_SCREEN,null);
            ActionInvoker.processCommand(action);*/
             ContextMenu contextMenu = ContextMenu.getInstance();
         contextMenu.setTarget(this);
         contextMenu.clear();
         this.makeContextMenu(contextMenu);
         menu.deleteAll();
         menu.add(contextMenu);
    }
    /* protected  void makeMenu(Menu menu, int instance)
    {
            ContextMenu contextMenu = ContextMenu.getInstance();
            contextMenu.setTarget(this);
            contextMenu.clear();
            this.makeContextMenu(contextMenu);
            menu.deleteAll();
            menu.add(contextMenu);
    }*/
    public boolean onSavePrompt() {
            return true;
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
			return super.keyDown(keyCode, time);

		return true;
	}

	public boolean keyUp( int keyCode, int time )
	{
		return super.keyUp(keyCode, time);
	}

    public void confirmAction() {
    	ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
    	Vector dataVector = new Vector();
    	dataVector.addElement(tradeOrderConfirmationBean);
    	dataVector.addElement(new Integer(3));
    	actionPerfomed(ActionCommand.CMD_FNO_TRADE_ORDER_CONFIRM_POST, dataVector);
    }
    
    public void modifyAction() {
    	ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
    	//close();
    	new TradeNowMainParser(Utils.getFNOPostSQ4TradeURL(tradeOrderConfirmationBean), returnDataWithId, SQFID);
    }
    
	public void setData(Vector vector, int id) {
		if(id == SQFID)
    	{
    		Hashtable hashT = (Hashtable)vector.elementAt(1);
         	String ccode = (String)hashT.get("optType");
         	if(ccode == null)
         	{
         		ccode = (String)hashT.get("company_code")+"_"+(String)hashT.get("expiry");
         	}
         	else if(ccode.trim().length()==0)
         	{
         		ccode = (String)hashT.get("company_code")+"_"+(String)hashT.get("expiry");
         	}
         	else
         	{
         		ccode = (String)hashT.get("company_code")+"_"+(String)hashT.get("expiry")+"_"+(String)hashT.get("strikePrice")+"_"+(String)hashT.get("optType");
         	}
    		FNOTradeConfiguration fnoTradeConfig = new FNOTradeConfiguration();
			if(((String)hashT.get("instrument")).indexOf("OPT")>-1)
			{
				fnoTradeConfig.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX);
			}
			else
			{
				fnoTradeConfig.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX);
			}
			if(((String)hashT.get("orderType")).equalsIgnoreCase("limit"))
			{
				fnoTradeConfig.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT);
			}
			else
			{

				fnoTradeConfig.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_MARKET);
			}
			fnoTradeConfig.setFnoExpiryDate((String)hashT.get("expiry"));
			fnoTradeConfig.setFnoStrikePriceValue((String)hashT.get("strikePrice"));
			//v.addElement(fnoTradeConfig.copy());
			//v.addElement(bannerData.copy());
			//v.addElement((String)hashData.get("expiry"));
			
			FNOTradeBean fnoTradeBean = new FNOTradeBean();
			 fnoTradeBean.setIndexName((String)hashT.get("instrument"));
			 fnoTradeBean.setSymbolName((String)hashT.get("symbolName"));
			 fnoTradeBean.addExpiryData((String)hashT.get("Expiry"));
			 if(hashT.containsKey("mktLots"))
			 fnoTradeBean.setMinLot((String)hashT.get("mktLots"));
			 fnoTradeBean.setFlag((String)hashT.get("flag"));
			 fnoTradeBean.addStrikeData((String)hashT.get("StrikePrice"));
			 FNOTradeTurnOverScreen fnoTradeScreen = new FNOTradeTurnOverScreen(fnoTradeBean, "Turnover",fnoTradeConfig,getTradeOrderConfirmationBean().getBannerData(), (String)hashT.get("expiry"), hashT);
				
    	}

	}
	
	public String getActionFullName(String value) {
		return value.trim().toLowerCase().equals("b") ? "Buy" : "Sell";
	}

	public void actionPerfomed(byte Command, Object data) {
		// TODO Auto-generated method stub
		switch(Command) {
			case ActionCommand.CMD_FNO_TRADE_ORDER_CONFIRM_POST:
				ActionInvoker.processCommand(new Action(Command,data));
				break;
			default:
        		ActionInvoker.processCommand(new Action(Command, data));
        		break;
		}
	}
	
	private class CustomGridRowField extends  LabelField {

		private String key;
		private String value;
		private int backColor;
		private int borderColor;
		
		private int xPadding = 10;
		
		public CustomGridRowField(String key,String value,int backColor,int borderColor) {
			this.key = key;
			this.value = value;
			this.backColor = backColor;
			this.borderColor = borderColor;
		}

		public Font getFont() {
			return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);
		}

		public int getPreferredWidth() {
			return AppConstants.screenWidth;
		}

		public int getPreferredHeight() {
			return getFont().getHeight()*2;
		}

		protected void paint(Graphics graphics)
		{
			graphics.setColor(borderColor);
	    	graphics.fillRect(0,0,getPreferredWidth(),getPreferredHeight());

			graphics.setColor(backColor);
	    	graphics.fillRect(1,1,getPreferredWidth()-2,getPreferredHeight()-2);

	    	graphics.setColor(Color.WHITE);
	    	graphics.setFont(getFont());
			graphics.drawText(key, xPadding, getPreferredHeight()/2 - getFont().getHeight()/2);			
			graphics.drawText(": "+value, getPreferredWidth()/2, getPreferredHeight()/2 - getFont().getHeight()/2);

			super.paint(graphics);
		}

		protected void layout(int width, int height) {
		    setExtent(getPreferredWidth(), getPreferredHeight());
		}
	}

}
