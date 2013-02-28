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
import com.snapwork.beans.KeyValueBean;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.TitleBar;
import com.snapwork.interfaces.ReturnData;
import com.snapwork.parsers.TradeNowOrderResultParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class TradeNowOrderResultScreen extends MainScreen implements ActionListener,ReturnData,RemovableScreen
{
//private HomeScreenBanner banner;
private HomeJson bann;
private BottomMenu bottomMenu = null;
private TradeNowOrderResultParser tradeNowOrderResultParser;
private boolean httpKill = false;
private String screenString;
	public TradeNowOrderResultScreen(String screenString, Vector urls,HomeJson banner) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));

		if(AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			Utils.snippetDiff = 15;
		this.screenString = screenString;
 this.bann = banner;
 String url = (String)urls.elementAt(1);
		//createUI(AppConstants.appTitle+" : Registration");
		//parser + url
 //Test url
 //url = "http://50.17.18.243/SK/placeOrder.php?companyId=RELIANCE&exchange=NSE&ltp=819.55&action=B&qty=5&disc-qty=2&orderType=market&limitPrice=0&stopPrice=0&per_change=0.20&type=NEW&change=1.65&custId=250037&dpId=13185788&btnConfirm=Confirm&btnModify=&x=114&y=11";
 //url = "http://50.17.18.243/SK/placeOrder.php?companyId=RELIANCE&exchange=NSE&ltp=814.00&action=&qty=3&disc-qty=1&orderType=limit&limitPrice=80.12&stopPrice=0&per_change=-0.77&type=NEW&change=-6.35&custId=250037&dpId=13185788&btnConfirm=Confirm&btnModify=&userAgent=bb&x=29&y=24";
 // url = url + "&debug=2";
  //System.out.println(url);
  tradeNowOrderResultParser = new TradeNowOrderResultParser(url,this);
		
	}
	protected void paintBackground(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());/*
		super.paint(graphics);*/
		
		// graphics.setBackgroundColor(Color.BLACK);
		    
		    // Clears the entire graphic area to the current background
		  //  graphics.clear();   
	}

	public void createUI(String strTitle,final  HomeJson homeJson, final java.util.Hashtable hashTable) {
		final Bitmap bmp = ImageManager.getTradeNow();
		
		//set Title
		TitleBar titleBar = new TitleBar(strTitle); 
		setTitle(titleBar);
		final int titleBarHeight = titleBar.getPreferredHeight();
		/*if(AppConstants.NSE)
			banner = new HomeScreenBanner(FIELD_HCENTER, "NSE");
		else
			banner = new HomeScreenBanner(FIELD_HCENTER, "BSE");
		banner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()));
		*/
		VerticalFieldManager vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}  
			/*protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(maxWidth,maxHeight);
				setExtent(maxWidth,maxHeight<AppConstants.screenHeight-TitleBar.getItemHeight()?AppConstants.screenHeight:maxHeight);
			}*/
		};
		final String textButtonViewOrder = "View Order";
				BitmapField btnViewOrder = new BitmapField(bmp,  FOCUSABLE | DrawStyle.HCENTER)  {

					protected boolean navigationClick(int status,int time) {
						if(!Utils.sessionAlive)
						{
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
						}
						else
						{
						if(screenString.equalsIgnoreCase("tradenow"))
						{
							/*Vector dataUrl = new Vector();
							//http://50.17.252.160/SK_live/orderStatement.php?exchange=NSE&custId=250037&userAgent=bb&btnView=View+Order
							String url = AppConstants.domainUrl+"orderStatement.php?exchange=NSE&custId="+UserInfo.getUserID()+"&userAgent=bb&btnView=View+Order&debug=2";
							dataUrl.addElement(screenString);
							dataUrl.addElement(url);
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
						*/
							Vector dataUrl = new Vector();
							String url = Utils.getReportsEquityURL(UserInfo.getUserID());
							dataUrl.addElement("tradenow");
							dataUrl.addElement(url);
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
						
						}
						else if(screenString.equalsIgnoreCase("ReportNetPosition"))
						{
						/*	Vector dataUrl = new Vector();
							//http://50.17.252.160/SK_live/orderStatement.php?exchange=NSE&custId=250037&userAgent=bb&btnView=View+Order
							String url = AppConstants.domainUrl+"orderStatement.php?exchange=NSE&custId="+UserInfo.getUserID()+"&userAgent=bb&btnView=View+Order&debug=2";
							dataUrl.addElement("tradenow");
							dataUrl.addElement(url);
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
						*/
							Vector dataUrl = new Vector();
							String url = Utils.getReportsEquityURL(UserInfo.getUserID());
							dataUrl.addElement("tradenow");
							dataUrl.addElement(url);
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
						
						}
						else if(screenString.equalsIgnoreCase("DPSR"))
						{
							/*Vector dataUrl = new Vector();
							//http://50.17.252.160/SK_live/orderStatement.php?order_number=243532043&exchange=NSE&custId=250037&userAgent=&dpId=13185788&x=55&y=20
							String url = AppConstants.domainUrl+"orderStatement.php?exchange=NSE&custId="+UserInfo.getUserID()+"&userAgent=bb&btnView=View+Order&debug=2";
							dataUrl.addElement("tradenow");
							dataUrl.addElement(url);
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
						*/
							Vector dataUrl = new Vector();
							String url = Utils.getReportsEquityURL(UserInfo.getUserID());
							dataUrl.addElement("tradenow");
							dataUrl.addElement(url);
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
						
						}
						
						
						//ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);

						//new TradeNowOrderResultScreen(AppConstants.WEBVIEW_URL,bann);
						/*synchronized( UiApplication.getEventLock() ){
		                    
		                    if(isDisplayed()) 
		                 	   {
		                    	//sendHTTPRequest(choice[dropdown.getSelectedIndex()],editfield.getText());
		    					
		                 	   	close();

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
						graphics.drawText(textButtonViewOrder,(bmp.getWidth()/2)-(getFont().getAdvance("Trade Now")/2),(bmp.getHeight()/2)-(getFont().getHeight()/2));
						
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
					HorizontalFieldManager hfmobj = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER)
					{
						public void paintBackground(Graphics graphics)
						{
							graphics.setColor(Color.BLACK);
							graphics.fillRect(0, 0, getWidth(), getHeight());
						}
						
						};
						hfmobj.add(btnViewOrder);
	String id = (String)hashTable.get("order_number");
	long idLong = 0;
	try {
		idLong = Long.parseLong(id);
	} catch (NumberFormatException e) {
		idLong = 0;
	}
			vfm.add(new LabelField("",NON_FOCUSABLE));
			if(id == null)
			{
				vfm.add(getLabels("Order Fail"));
				String error = (String)hashTable.get("error_msg");
				if(error == null)
					error = "";
				vfm.add(getLabels(error));
			}
			else if(id.length()>0 && idLong != 0)
			{ 
				vfm.add(getLabels("Order Success"));
			vfm.add(getLabels("We have successfully submitted your order.",AppConstants.screenWidth<321?"Please check the view order screen for futher\n\tdetails.":"Please check the view order screen for futher details."));
			
			}
			else
			{
				vfm.add(getLabels("Order Fail"));
				String str = (String)hashTable.get("error_msg");
				if(str.indexOf("at this")>-1){
				int x = str.indexOf("at this");
				vfm.add(getLabels("",str.substring(0, x-1)+"\n\n\t"+str.substring(x,str.length())));
				}else
					vfm.add(getLabels("",str));
				
					
			}
			if(idLong != 0)
				vfm.add(getLabelOrder((String)hashTable.get("order_number")));
			
			
			vfm.add(new LabelField("",NON_FOCUSABLE));
			 vfm.add(hfmobj);
		//vfm.add(vfmInner);
		add(vfm);
		btnViewOrder.setFocus();
		/*UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				ScreenInvoker.removeRemovableScreen();  
			}
		});
		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
			}
		});
		final TradeNowOrderResultScreen rparse = this;
		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				ScreenInvoker.pushScreen(rparse, true, true);
			}
		});*/
		final TradeNowOrderResultScreen rparse = this;
		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				ScreenInvoker.removeRemovableScreen();  
				UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
				ScreenInvoker.pushScreen(rparse, true, true);
			}
		});
		
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
	}

	public LabelField getLabels(final String text2, final String text3)
	{
		return new LabelField("\t"+text2+"\n\n\t"+text3,NON_FOCUSABLE)
		//\\return new LabelField("",NON_FOCUSABLE)
		{
			public void setFont(Font arg0) {
				super.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			}
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x222222);
				graphics.fillRect(0, 0, getPreferredWidth(),getPreferredHeight());
			}
			protected void paint(Graphics graphics) {
				//graphics.setColor(0x222222);
				//graphics.fillRect(0, 2+(getFont().getHeight()), getPreferredWidth(), 4+(getFont().getHeight()*2));
				//graphics.setColor(0x999999);
				/*if(text1.equalsIgnoreCase("Order Success"))
						graphics.setColor(0x66cc00);
				else
					graphics.setColor(0xcc3333);
				graphics.drawText(text1,5,2);*/
				graphics.setColor(0xeeeeee);
				setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
				/*graphics.drawText(text2,5,4);
				graphics.drawText(text3, 5, 6+(getFont().getHeight()));*/
				//graphics.setColor(labelColor);
				super.paint(graphics);
				
				
			}
			public int getPreferredHeight() {
				return 6+(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*3);
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
	
	public LabelField getLabels(final String text1)
	{
		return new LabelField("",FOCUSABLE)
		{
			public void setFont(Font arg0) {
				super.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
			}
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x222222);
				graphics.fillRect(0, 0, getPreferredWidth(),getPreferredHeight());
			}
			protected void paint(Graphics graphics) {
				//graphics.setColor(0x222222);
				//graphics.fillRect(0, 2+(getFont().getHeight()), getPreferredWidth(), 4+(getFont().getHeight()*2));
				//graphics.setColor(0x999999);
				if(text1.equalsIgnoreCase("Order Success"))
						graphics.setColor(0x66cc00);
				else
					graphics.setColor(0xcc3333);
				graphics.drawText(text1,25,2);
			}
			public int getPreferredHeight() {
				return 6+(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight());
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
	
	public LabelField getLabelOrder(final String text1)
	{
		return new LabelField("",FOCUSABLE)
		{
			public void setFont(Font arg0) {
				super.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
			}
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x222222);
				graphics.fillRect(0, 0, getPreferredWidth(),getPreferredHeight());
			}
			protected void paint(Graphics graphics) {
				//graphics.setColor(0x222222);
				//graphics.fillRect(0, 2+(getFont().getHeight()), getPreferredWidth(), 4+(getFont().getHeight()*2));
				//graphics.setColor(0x999999);
				graphics.setColor(0xeeeeee);
				graphics.drawText("Order Number : "+text1,25,2);
			}
			public int getPreferredHeight() {
				return 6+(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight());
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
	/**
	 * 	
	 * @param text1 Order ID : If order id blank order failed, show the error message
	 * @param text2 Error Message
	 * @return
	 */
	/*public LabelField getLabels(final String text1, final String text2)
	{
		return new LabelField("",NON_FOCUSABLE)
		{
			
			protected void paintBackground(Graphics graphics) {
				graphics.setColor(0x666666);
				graphics.fillRect(10, 0, getPreferredWidth()-20,getPreferredHeight());
				//super.paintBackground(graphics);
			}
			protected void paint(Graphics graphics) {
				if(text1!=null)
				{
					graphics.setColor(0x66cc00);
					setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
					graphics.drawText("Order Success",25,2);
					setFont(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT));
					graphics.drawText("We have successfully submitted your order.", 25, 4+FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight());
					setFont(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT));
					graphics.drawText("Please check the view order screen for futher details.", 25, (4+FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight())*2);
				}
				else
				{
					graphics.setColor(0xcc3333);
					setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
					graphics.drawText("Order fail",25,2);
					setFont(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT));
					graphics.drawText(text2, 25, 4+FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight());
					}
				
				//setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
				//super.paint(graphics);
				//if(getFont().getAdvance(text)+50<AppConstants.screenWidth)
					//graphics.drawText(text,25,2);
				/*else
				{
					StringBuffer sbf = new StringBuffer();
					Vector vector = new Vector();
					for(int i=0;i<text.length();i++)
					{
						if(getFont().getAdvance(sbf.toString())+45>AppConstants.screenWidth)
						{
							vector.addElement(sbf.toString());
							sbf = new StringBuffer();
						}
					}
					vector.addElement(sbf.toString());
				}*/
				/*	graphics.drawText("Order Success",5,2);
					setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
					graphics.setColor(0xeeeeee);
					graphics.drawText(text2,5,4+(getFont().getHeight()));
				}
				else
				{
					graphics.setColor(0xcc3333);
					setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
					graphics.drawText("Order fail",5,2);
					setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
					graphics.setColor(0xeeeeee);
					graphics.drawText(text2,5,4+(getFont().getHeight()));
				}
				
				
				graphics.setColor(0x222222);
				graphics.fillRect(0, 2+(getFont().getHeight()), getPreferredWidth(), 4+(getFont().getHeight()*2));
				graphics.setColor(0x999999);
				graphics.drawText(text1,5,2);
				graphics.drawText(text2,5,4+(getFont().getHeight()));
				graphics.drawText(text3, 5, 6+(getFont().getHeight()*2));*
			}
			public int getPreferredHeight() {
				return FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight()*3;
			}
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}
			protected void layout(int width, int height) {
				super.layout(width, getPreferredHeight());
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
		};
	}*/
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
		if(httpKill)
			return;
		//System.out.println("Response TradeNowOrderResult");
		java.util.Hashtable hashTable= new java.util.Hashtable(); 
		if(vector.size()!=0)
		{
			for(int i=0;i<vector.size();i++){
		KeyValueBean bean = (KeyValueBean)vector.elementAt(i);
		//System.out.println(bean.getKey()+" : "+bean.getValue());
		hashTable.put(bean.getKey(), bean.getValue());
		
		//System.out.println("------------------------------------------------");
			}
		}
		
		createUI("Order Result",bann, hashTable);
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
	
	/*public boolean onClose() {
		tradeNowOrderResultParser.httpKill(); 
		httpKill = true;
		if(SlideViewOrderCancel.orderCancel)
		{	
			SlideViewOrderCancel.orderCancel = false;
			ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTSB_SCREEN,null));
		}
		else
			ActionInvoker.processCommand(new Action(ActionCommand.CMD_GRID_SCREEN,null));
		return false;
	}*/
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
		final TradeNowOrderResultScreen torderscreen = this;
		 if(key == Keypad.KEY_MENU) {
			 	UiApplication.getUiApplication().invokeLater(new Runnable() {
	            		public void run() {
	            			try {
								if(bottomMenu != null)
									bottomMenu.autoAttachDetachFromScreen();
								else
								{
									bottomMenu = BottomMenu.getBottomMenuInstance(torderscreen,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
									bottomMenu.autoAttachDetachFromScreen();
								}
							} catch (Exception e) {
							}
	            		}
	            	});
 		}
         /*else if(key == Keypad.KEY_ESCAPE) {
			synchronized( UiApplication.getEventLock() ){
            
            if(isDisplayed()) 
         	   {
            	tradeNowOrderResultParser.httpKill();
    			httpKill = true;
    			close();
    			if(SlideViewOrderCancel.orderCancel)
    			{	
    				SlideViewOrderCancel.orderCancel = false;
    				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTSB_SCREEN,null));
    			}
         	   }
          //  actionPerfomed(ActionCommand.CMD_GRID_SCREEN, null);
}
		} */
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
}
