package com.snapwork.view.trade;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.phone.phonelogs.ConferencePhoneCallLog;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.ReportsOrderBean;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.RefreshButton;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TitleBarRefresh;
import com.snapwork.interfaces.ReturnData;
import com.snapwork.parsers.ReportsOrderParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;
import com.snapwork.view.CompanyDetailsSnippetsScreen;
import com.snapwork.view.ReportsMainScreen;

public class ReportsOrderViewScreen extends MainScreen implements ActionListener, ReturnData
{
	private BottomMenu bottomMenu = null;
	private String errormsg;
	private String screenString;
	private TitleBar titleBar;
	private TitleBarRefresh titleBarRefresh;
	private RefreshButton refreshme;
	private boolean refreshFlag;
	private VerticalFieldManager vfm;
	private String url;
	private ReportsOrderViewScreen rthis = this;
	private Vector confirmVector;
	public static long time = 0;
 private boolean refreshReport;
	public ReportsOrderViewScreen(Vector vector) {
		if(time == 0)
		{
			time = System.currentTimeMillis();
		}
		else if((time+1000)>System.currentTimeMillis())
		{
			time = System.currentTimeMillis();
			return;
		}	
		 
			time = System.currentTimeMillis();
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		if(AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			Utils.snippetDiff = 15;
		refreshReport = false;
		screenString = (String)vector.elementAt(0);
		url = (String)vector.elementAt(1);
		LOG.print("You are in ReportsOrderViewScreen title : " + screenString);
		LOG.print(url);
		//System.out.println("thjlsdnbfm dskkl f ---------------------------------------------- screenString : "+screenString);
		//String url = AppConstants.domainUrl+"EQreports.php?custId="+UserInfo.getUserID()+"&rtype=order&page=EQreports&debug=2";
		errormsg = "";
		new ReportsOrderParser(url,this);

	}
	protected void paintBackground(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
	}

	public void createUI(String strTitle, final Vector confirmVector) {
		this.confirmVector = confirmVector;
		//set Title
		if(screenString.equalsIgnoreCase("Holdings"))
		{
			strTitle = "Equity DP/SR Report";
		}
		else if(screenString.equalsIgnoreCase("OrderBKEquity"))
		{
			if(strTitle.length() == 0)
				strTitle = "Equity Order Book";
		}
		else if(screenString.equalsIgnoreCase("OrderBKF&O"))
		{
			if(strTitle.length() == 0)
				strTitle = "F&O Order Book";
		}
		else if(screenString.equalsIgnoreCase("ReportsOrderf"))
		{
			if(strTitle.length() == 0)
				strTitle = "F&O Report";
		}
		else if(screenString.equalsIgnoreCase("tradenow") || screenString.equalsIgnoreCase("tradenowf"))
		{
			if(screenString.equalsIgnoreCase("tradenow"))
				screenString= "ReportsOrdere";
			if(screenString.equalsIgnoreCase("tradenowf"))
				screenString= "ReportsOrderf";
			strTitle = "Order Report";
		}
		else if(screenString.equalsIgnoreCase("ReportsOrdere"))
		{
			strTitle = "Order Report";
		}
		if(screenString.equalsIgnoreCase("ReportsOrdere") || screenString.equalsIgnoreCase("ReportsOrderf") || screenString.equalsIgnoreCase("OrderBKEquity") || screenString.equalsIgnoreCase("OrderBKF&O")
				|| screenString.equalsIgnoreCase("ReportsTradee") || screenString.equalsIgnoreCase("ReportsTradef")
				|| screenString.equalsIgnoreCase("ReportsIntraday")
				|| screenString.equalsIgnoreCase("ReportsTurnover"))
		{
			refreshme = new RefreshButton()
			{
				protected boolean navigationClick(int arg0, int arg1) {
					if(!getLoading())
					{
						setLoading(true);
						refreshFlag = true; 
						if(screenString.equalsIgnoreCase("ReportsOrdere"))
						{
							url = Utils.getReportsEquityURL(UserInfo.getUserID());
						}
						else if(screenString.equalsIgnoreCase("ReportsOrderf"))
						{
							url = Utils.getReportsFNOURL(UserInfo.getUserID());
						}
						else if(screenString.equalsIgnoreCase("OrderBKEquity"))
						{
							url = Utils.getReportsEquityURL(UserInfo.getUserID());
						}
						else if(screenString.equalsIgnoreCase("OrderBKF&O"))
						{
							url = Utils.getReportsFNOURL(UserInfo.getUserID());
						}
						new ReportsOrderParser(url,rthis);
					}
					return super.navigationClick(arg0, arg1);
				}
			};
			//Sets the title
			titleBarRefresh = new TitleBarRefresh(strTitle, refreshme);
		}
		else if(screenString.equalsIgnoreCase("ReportsDP-SR") || screenString.equalsIgnoreCase("Holdings")) //Reports DP-SR
		{
			refreshme = new RefreshButton()
			{
				protected boolean navigationClick(int arg0, int arg1)
				{
					if(!getLoading())
					{
						setLoading(true);
						refreshFlag = true;
						new ReportsOrderParser(url,rthis);
					}
					return super.navigationClick(arg0, arg1);
				}
			};
			//Sets the title
			titleBarRefresh = new TitleBarRefresh(strTitle, refreshme);
		}
		else
			titleBar = new TitleBar(strTitle);
		if(screenString.equalsIgnoreCase("ReportsOrdere") || screenString.equalsIgnoreCase("ReportsOrderf") || screenString.equalsIgnoreCase("OrderBKEquity") || screenString.equalsIgnoreCase("OrderBKF&O") || screenString.equalsIgnoreCase("ReportsDP-SR") || screenString.equalsIgnoreCase("Holdings")
				|| screenString.equalsIgnoreCase("ReportsTradee") || screenString.equalsIgnoreCase("ReportsTradef")
				|| screenString.equalsIgnoreCase("ReportsIntraday")
				|| screenString.equalsIgnoreCase("ReportsTurnover"))
		{
			if(titleBarRefresh != null)
				setTitle(titleBarRefresh);
		}
		else if(screenString.equalsIgnoreCase("ReportNetPosition"))
		{
			ReportsOrderBean rbean = new ReportsOrderBean();
			rbean.setStringHolder("Scrip-Exchange", "Scrip - Exchange");
			rbean.setStringHolder("NetQty", "NetQty");
			VerticalFieldManager titleBar_ = new VerticalFieldManager(USE_ALL_WIDTH);
			refreshme = new RefreshButton()
			{
				protected boolean navigationClick(int arg0, int arg1)
				{
					if(!getLoading())
					{
						setLoading(true);
						refreshFlag = true;
						new ReportsOrderParser(url,rthis);
					}
					return super.navigationClick(arg0, arg1);
				}
			};
			titleBarRefresh = new TitleBarRefresh(strTitle, refreshme);
			titleBar_.add(titleBarRefresh);
			if(confirmVector.size()>0) {
				titleBar_.add(new ReportsNetPositionView(url,rbean.getStringHolder(),this,0));
			}
			setTitle(titleBar_);
		}
		else if(screenString.equalsIgnoreCase("ReportsUnsettled_Position"))
		{
			ReportsOrderBean rbean = new ReportsOrderBean();
			rbean.setStringHolder("Scrip-Exchange", "Scrip - Exchange");
			rbean.setStringHolder("SettleNo", "Settlement No");
			VerticalFieldManager titleBar_ = new VerticalFieldManager(USE_ALL_WIDTH);
			refreshme = new RefreshButton()
			{
				protected boolean navigationClick(int arg0, int arg1)
				{
					if(!getLoading())
					{
						setLoading(true);
						refreshFlag = true;
						new ReportsOrderParser(url,rthis);
					}
					return super.navigationClick(arg0, arg1);
				}
			};
			titleBarRefresh = new TitleBarRefresh(strTitle, refreshme);
			titleBar_.add(titleBarRefresh);
			if(confirmVector.size()>0) {
				titleBar_.add(new ReportsUnsetteledPositionView(url,rbean.getStringHolder(),this,0));
			}
			setTitle(titleBar_);
		}
		else if(/* screenString.equalsIgnoreCase("ReportsMarginf") || */screenString.equalsIgnoreCase("ReportsUL_Marginf"))
		{
			ReportsOrderBean rbean = new ReportsOrderBean();
			rbean.setStringHolder("Scrip-Exchange", "Scrip");
			rbean.setStringHolder("NetQty", "Total Margin");
			VerticalFieldManager titleBar_ = new VerticalFieldManager(USE_ALL_WIDTH);
			refreshme = new RefreshButton()
			{
				protected boolean navigationClick(int arg0, int arg1)
				{
					if(!getLoading())
					{
						setLoading(true);
						refreshFlag = true;
						new ReportsOrderParser(url,rthis);
					}
					return super.navigationClick(arg0, arg1);
				}
			};
			titleBarRefresh = new TitleBarRefresh(strTitle, refreshme);
			titleBar_.add(titleBarRefresh);
			if(confirmVector.size()>0) {
				titleBar_.add(new ReportsFNOMarginView(url, screenString,rbean.getStringHolder(),this,0));
			}
			setTitle(titleBar_);
		}
		else
		{
			setTitle(titleBar);
		}

		vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
			}  
			/*protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(maxWidth,maxHeight);
				setExtent(maxWidth, maxHeight);
			}*/
		};
		LOG.print("confirmVector.size() : "+confirmVector.size());
		if(screenString.equalsIgnoreCase("ReportsOrdere") || screenString.equalsIgnoreCase("OrderBKEquity") || screenString.equalsIgnoreCase("tradenow") )
		{
			/*//TEST START
			LOG.print("OrderBKEquity "+screenString);
			ReportsOrderBean beantest = new ReportsOrderBean();
			beantest.setStringHolder("order_id","120120");
			beantest.setStringHolder("orderstatus","ors_placed");
			beantest.setStringHolder("scripcode","10");
			beantest.setStringHolder("exchange","20");
			beantest.setStringHolder("orderqty","100");
			beantest.setStringHolder("ordertype","TEST");
			vfm.add(Utils.separator(10, 0x000000));
			vfm.add(new ReportsOrderView(url,screenString,beantest.getStringHolder(),this));
			//TEST END*/
			if(screenString.equalsIgnoreCase("tradenow"))
				screenString= "ReportsOrdere";
			if(confirmVector.size() == 0 )
				vfm.add(getLabel("You have 0 Orders."));
			else
				for(int i = 0; i<confirmVector.size(); i++)
				{
					ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
					vfm.add(Utils.separator(10, 0x000000));
					LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
					vfm.add(new ReportsOrderView(url,screenString,bean.getStringHolder(),this));
				}
		}
		else if(screenString.equalsIgnoreCase("ReportsOrderf") || screenString.equalsIgnoreCase("OrderBKF&O") || screenString.equalsIgnoreCase("tradenowf"))
		{
			/*//TEST START
			ReportsOrderBean beantest = new ReportsOrderBean();
			beantest.setStringHolder("order_id","120120");
			beantest.setStringHolder("orderstatus","ors_placed");
			beantest.setStringHolder("scripcode","10");
			beantest.setStringHolder("exchange","20");
			beantest.setStringHolder("orderqty","100");
			beantest.setStringHolder("ordertype","TEST");
			vfm.add(Utils.separator(10, 0x000000));
			vfm.add(new ReportsOrderView(url,screenString,beantest.getStringHolder(),this));
			//TEST END*/
			if(screenString.equalsIgnoreCase("tradenowf"))
				screenString= "ReportsOrderf";
			if(confirmVector.size() == 0 )
				vfm.add(getLabel("You have 0 Orders."));
			else
				for(int i = 0; i<confirmVector.size(); i++)
				{
					ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
					vfm.add(Utils.separator(10, 0x000000));
					LOG.print("screenString "+screenString);
					vfm.add(new ReportsOrderBKFNOView(url,screenString,bean.getStringHolder(),this));
				}
		}
		else if(screenString.equalsIgnoreCase("ReportsDP-SR") || screenString.equalsIgnoreCase("Holdings")){
			if(confirmVector.size() == 0)
				vfm.add(getLabel("No records found in your report."));
			else
				for(int i = 0; i<confirmVector.size(); i++)
				{
					ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
					vfm.add(Utils.separator(10, 0x000000));
					LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
					vfm.add(new ReportsDPSRView(bean.getStringHolder(),this));
				}
		}
		else if(screenString.equalsIgnoreCase("ReportsIntraday")){
			/*//TEST START
			ReportsOrderBean beantest = new ReportsOrderBean(); 
			beantest.setStringHolder("SCRIPCODE","12015");
			beantest.setStringHolder("EXCHANGE","NSEEE");
			beantest.setStringHolder("POSITION","20");
			beantest.setStringHolder("POSVAL","VAL");
			beantest.setStringHolder("MARGIN","MARGIN");
			vfm.add(Utils.separator(10, 0x000000));
			vfm.add(new ReportsUnsetteledPositionView(url,beantest.getStringHolder(),this,1));
			//TEST END
			*/if(confirmVector.size() == 0)
				vfm.add(getLabel("You have 0 reports for intraday."));
			else
				for(int i = 0; i<confirmVector.size(); i++)
				{
					ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
					vfm.add(Utils.separator(10, 0x000000));
					LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
					vfm.add(new ReportsIntradayView(bean.getStringHolder(),this));
				}
		}
		else if(screenString.equalsIgnoreCase("ReportNetPosition"))
		{
			if(confirmVector.size() == 0)
				vfm.add(getLabel("No records found in your report."));
			else
				for(int i = 0; i<confirmVector.size();)
				{
					ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
					vfm.add(Utils.separator(10, 0x000000));
					LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
					vfm.add(new ReportsNetPositionView(url,bean.getStringHolder(),this,++i));
				}
		}
		else if(screenString.equalsIgnoreCase("ReportsTradee")){
			if(confirmVector.size() == 0)
				vfm.add(getLabel("You have 0 trades."));
			else
				for(int i = 0; i<confirmVector.size(); i++)
				{
					ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
					vfm.add(Utils.separator(10, 0x000000));
					LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
					vfm.add(new ReportsTradeView(url,bean.getStringHolder(),this));
				}
			/*//Demo Code
			ReportsOrderBean beans = new ReportsOrderBean();
			beans.setStringHolder("SHAREKHAN_SCRIP_CODE", "xxfdgcvdgs");
			beans.setStringHolder("BUY_SELL", "S");
			beans.setStringHolder("TRADE_QTY", "4");
			beans.setStringHolder("TRADE_PRICE", "453.99");
			beans.setStringHolder("TRADE_AMOUNT", "1999.88");
			vfm.add(Utils.separator(10, 0x000000));
			vfm.add(new ReportsTradeView(url,beans.getStringHolder(),this));
			//Demo Code*/
		}
		else if(screenString.equalsIgnoreCase("ReportsTradef")){
			if(confirmVector.size() == 0)
				vfm.add(getLabel("You have 0 trades."));
			else
				for(int i = 0; i<confirmVector.size(); i++)
				{
					ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
					vfm.add(Utils.separator(10, 0x000000));
					LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
					vfm.add(new ReportsFNOTradeView(url,bean.getStringHolder(),this));
				}
		}
		else if(screenString.equalsIgnoreCase("ReportsUnsettled_Position"))
		{
			/*//TEST START
			ReportsOrderBean beantest = new ReportsOrderBean(); 
			beantest.setStringHolder("Scrip-Exchange","NSE");
			beantest.setStringHolder("SCRIPCODE","12015");
			beantest.setStringHolder("EXCHANGE","NSEEE");
			beantest.setStringHolder("SETTLEMENTNO","20");
			vfm.add(Utils.separator(10, 0x000000));
			vfm.add(new ReportsUnsetteledPositionView(url,beantest.getStringHolder(),this,1));
			//TEST END
			*/if(confirmVector.size() == 0)
				vfm.add(getLabel("No records found in your report."));
			else
				for(int i = 0; i<confirmVector.size();)
				{
					ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
					vfm.add(Utils.separator(10, 0x000000));
					LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
					vfm.add(new ReportsUnsetteledPositionView(url,bean.getStringHolder(),this,++i));
				}
		}
		else if(screenString.equalsIgnoreCase("ReportsTurnover"))
		{
			LOG.print("You are inside Report Turnover");
			//TEST
//			ReportsOrderBean beantest = new ReportsOrderBean();
//			beantest.setStringHolder("CONTRACT","TESTC");
//			beantest.setStringHolder("BUY_SELL","S");
//			beantest.setStringHolder("TRADE_QTY","10");
//			beantest.setStringHolder("TRADE_PRICE","20");
//			beantest.setStringHolder("TRADE_AMOUNT","40");
//			vfm.add(Utils.separator(10, 0x000000));
//			vfm.add(new ReportsFNOTurnoverView(url, beantest.getStringHolder(),this));
			//TEST END
			if(confirmVector.size() == 0)
				vfm.add(getLabel("No records found in your report."));
			else
				for(int i = 0; i<confirmVector.size();i++)
				{
					ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
					vfm.add(Utils.separator(10, 0x000000));
					vfm.add(new ReportsFNOTurnoverView(url, bean.getStringHolder(),this));
				}

		}
		else if(screenString.equalsIgnoreCase("ReportsMarginf") || screenString.equalsIgnoreCase("ReportsUL_Marginf"))
		{
			if(confirmVector.size() == 0)
				vfm.add(getLabel("No records found in your report."));
			else
				for(int i = 0; i<confirmVector.size();i++)
				{
					ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
					vfm.add(Utils.separator(10, 0x000000));
					vfm.add(new ReportsFNOMarginView(url, screenString,bean.getStringHolder(),this,++i));
				}
		}


		vfm.add(Utils.separator(10, 0x000000));
		/*	vfm.add(new ReportsOrderView(confirmVector));
		vfm.add(Utils.separator(10, 0x000000));
			vfm.add(new ReportsOrderView(confirmVector));
		vfm.add(Utils.separator(10, 0x000000));
			vfm.add(new ReportsOrderView(confirmVector));
		vfm.add(Utils.separator(10, 0x000000));*/
		add(vfm);



		/*UiApplication.getUiApplication().invokeAndWait(new Runnable() {
			public void run() {
				ScreenInvoker.removeRemovableScreen();  
			}
		});*/
		/*if(UiApplication.getUiApplication().getActiveScreen() instanceof ReportsOrderViewScreen)
		{
			
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				UiApplication.getUiApplication().getActiveScreen().close();
			}
		});
		}*/
		final ReportsOrderViewScreen reportsOrderViewScreen = this;
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				ScreenInvoker.removeRemovableScreen();  
				ScreenInvoker.pushScreen(reportsOrderViewScreen, true, true);
			}
		});
		if(errormsg!=null)
		if(errormsg.trim().length()>0 & confirmVector.size()>0)
		{
			if(screenString.equalsIgnoreCase("ReportsOrdere") || screenString.equalsIgnoreCase("ReportsOrderf") || screenString.equalsIgnoreCase("OrderBKEquity") || screenString.equalsIgnoreCase("OrderBKF&O"))
			{		
			 UiApplication.getUiApplication().invokeAndWait(new Runnable()
		        {
		            public void run()
		            {
		            	/*if(SlideViewOrderCancel.orderCancel)
		            	{
		            		SlideViewOrderCancel.orderCancel = false;
		            		SlideViewOrderCancel.MESSAGE = errormsg;
		            		//actionPerfomed(ActionCommand.CMD_REPORTSB_SCREEN, null);
		            	}
		            	else*/
		            		Dialog.alert(errormsg);
		            } 
		        });
			}
		}
		LOG.print("ScreenString "+screenString);
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
	public void actionPerfomed(byte Command, Object data) {
		ActionInvoker.processCommand(new Action(Command,data));		
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
		final ReportsOrderViewScreen rthis = this;
		if(key == Keypad.KEY_MENU) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					try {
						if(bottomMenu != null)
						{
							bottomMenu.autoAttachDetachFromScreen();
						}
						else
						{
							bottomMenu = BottomMenu.getBottomMenuInstance(rthis,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
							bottomMenu.autoAttachDetachFromScreen();
						}
					} catch (Exception e) {

					}
				}
			});
		}
		else if(key == Keypad.KEY_ESCAPE) {
			if(screenString.equalsIgnoreCase("ReportsOrdere"))
			{//ReportsEquity
				Vector vectorCommandData = new Vector();
			String string = "ReportsEquity";
			vectorCommandData.addElement(string);
			ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_SCREEN,vectorCommandData));
			}
			else if(screenString.equalsIgnoreCase("ReportsOrderf"))
			{//ReportsF&O
				Vector vectorCommandData = new Vector();
				String string = "ReportsF&O";
				vectorCommandData.addElement(string);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_SCREEN,vectorCommandData));
			}
			else if(screenString.equalsIgnoreCase("OrderBKEquity") || screenString.equalsIgnoreCase("OrderBKF&O"))
			{
				Vector vectorCommandData = new Vector();
			String string = "Order_Book";
			vectorCommandData.addElement(string);
			ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_SCREEN,vectorCommandData));
		
			}
			else{
			synchronized( UiApplication.getEventLock() ){

				if(isDisplayed()) 
				{
					close();
				}
			}
			}
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


	public void setData(Vector vector) {
		if(refreshReport)
		{
			if(!(UiApplication.getUiApplication().getActiveScreen() instanceof ReportsOrderViewScreen))
			{
				return;
			}
		}
		refreshReport = true;
		System.gc();
		String title = (String) vector.elementAt(0);
		errormsg = (String) vector.elementAt(1);
		final Vector vec = (Vector) vector.elementAt(2);
		confirmVector = vec;
		if(!refreshFlag)
			createUI(title, vec);
		else
		{
			if(screenString.equalsIgnoreCase("ReportsOrdere") || screenString.equalsIgnoreCase("OrderBKEquity") || screenString.equalsIgnoreCase("tradenow") )
			{
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						vfm.deleteAll();
						if(screenString.equalsIgnoreCase("tradenow"))
							screenString= "ReportsOrdere";
						if(confirmVector.size() == 0 )
							vfm.add(getLabel("You have 0 Orders."));
						else
							for(int i = 0; i<confirmVector.size(); i++)
							{
								ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
								vfm.add(Utils.separator(10, 0x000000));
								LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
								vfm.add(new ReportsOrderView(url,screenString,bean.getStringHolder(),rthis));
							}} 
				});
				refreshme.setLoading(false);
			}
			else if(screenString.equalsIgnoreCase("ReportsOrderf") || screenString.equalsIgnoreCase("OrderBKF&O") || screenString.equalsIgnoreCase("tradenowf"))
			{
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						vfm.deleteAll();
						if(screenString.equalsIgnoreCase("tradenowf"))
							screenString= "ReportsOrderf";
						
						
						/* ***************************
						 //dummy data
						ReportsOrderBean beans = new ReportsOrderBean();
						beans.setStringHolder("order_id", "92986067");
						beans.setStringHolder("exchange", "NSECURR");
						beans.setStringHolder("scripcode", "USDINR OPTSTK : 29-May : PE : 54.50");
						beans.setStringHolder("orderqty", "10");
						beans.setStringHolder("orderstatus", "InProcess");
						beans.setStringHolder("ors_placed", "InProcess");
						
						
						vfm.add(new ReportsOrderBKFNOView(url, screenString,beans.getStringHolder(),rthis));
						/*************************** */
					if( confirmVector.size() == 0 )
						vfm.add(getLabel("You have 0 Orders."));
					else
					for(int i = 0; i<confirmVector.size(); i++)
					{
						ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
						vfm.add(Utils.separator(10, 0x000000));
						LOG.print("screenString "+screenString);
						vfm.add(new ReportsOrderBKFNOView(url, screenString,bean.getStringHolder(),rthis));
					}
					} 
		});
		refreshme.setLoading(false);
			}
			
			else if(screenString.equalsIgnoreCase("ReportsDP-SR") || screenString.equalsIgnoreCase("Holdings")) //Reports DP-SR
			{
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						vfm.deleteAll();
						if(confirmVector.size() == 0)
							vfm.add(getLabel("No records found in your report."));
						else
							for(int i = 0; i<confirmVector.size(); i++)
							{
								ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
								vfm.add(Utils.separator(10, 0x000000));
								LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
								vfm.add(new ReportsDPSRView(bean.getStringHolder(),rthis));
							}} 
				});
				refreshme.setLoading(false);
			}


			else if(screenString.equalsIgnoreCase("ReportNetPosition"))
			{
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						vfm.deleteAll();if(confirmVector.size() == 0)
							vfm.add(getLabel("No records found in your report."));
						else
							for(int i = 0; i<confirmVector.size();)
							{
								ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
								vfm.add(Utils.separator(10, 0x000000));
								LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
								vfm.add(new ReportsNetPositionView(url,bean.getStringHolder(),rthis,++i));
							}}
				});
				refreshme.setLoading(false);
			}

			else if(screenString.equalsIgnoreCase("ReportsTradee")){
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						vfm.deleteAll();if(confirmVector.size() == 0)
							vfm.add(getLabel("You have 0 trades."));
						else
							for(int i = 0; i<confirmVector.size(); i++)
							{
								ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
								vfm.add(Utils.separator(10, 0x000000));
								LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
								vfm.add(new ReportsTradeView(url, bean.getStringHolder(),rthis));
							}
					}
				});
				refreshme.setLoading(false);
			}
			else if(screenString.equalsIgnoreCase("ReportsTradef")){
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						vfm.deleteAll();if(confirmVector.size() == 0)
							vfm.add(getLabel("You have 0 trades."));
						else
							for(int i = 0; i<confirmVector.size(); i++)
							{
								ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
								vfm.add(Utils.separator(10, 0x000000));
								LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
								vfm.add(new ReportsFNOTradeView(url, bean.getStringHolder(),rthis));
							}
					}
				});
				refreshme.setLoading(false);
			}
			else if(screenString.equalsIgnoreCase("ReportsIntraday")){
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						vfm.deleteAll();if(confirmVector.size() == 0)
						vfm.add(getLabel("You have 0 reports for intraday."));
				else
				for(int i = 0; i<confirmVector.size(); i++)
				{
					ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
					vfm.add(Utils.separator(10, 0x000000));
					LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
					vfm.add(new ReportsIntradayView(bean.getStringHolder(),rthis));
				}}
				});
				refreshme.setLoading(false);
				}
			else if(screenString.equalsIgnoreCase("ReportsUnsettled_Position"))
			{
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						vfm.deleteAll();if(confirmVector.size() == 0)
					vfm.add(getLabel("No records found in your report."));
				else
					for(int i = 0; i<confirmVector.size();)
					{
						ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
						vfm.add(Utils.separator(10, 0x000000));
						LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
						vfm.add(new ReportsUnsetteledPositionView(url,bean.getStringHolder(),rthis,++i));
					}}
				});
				refreshme.setLoading(false);
			}
			else if(screenString.equalsIgnoreCase("ReportsTurnover"))
			{
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						vfm.deleteAll();LOG.print("You are inside Report Turnover");
				if(confirmVector.size() == 0)
					vfm.add(getLabel("No records found in your report."));
				else
					for(int i = 0; i<confirmVector.size();i++)
					{
						ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
						vfm.add(Utils.separator(10, 0x000000));
						vfm.add(new ReportsFNOTurnoverView(url, bean.getStringHolder(),rthis));
					}
			}
		});
		refreshme.setLoading(false);
			}
			else if(screenString.equalsIgnoreCase("ReportsMarginf") || screenString.equalsIgnoreCase("ReportsUL_Marginf"))
			{
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						vfm.deleteAll();if(confirmVector.size() == 0)
					vfm.add(getLabel("No records found in your report."));
				else
					for(int i = 0; i<confirmVector.size();i++)
					{
						ReportsOrderBean bean = (ReportsOrderBean)confirmVector.elementAt(i);
						vfm.add(Utils.separator(10, 0x000000));
						vfm.add(new ReportsFNOMarginView(url, screenString,bean.getStringHolder(),rthis,++i));
					}
			}
		});
		refreshme.setLoading(false);
			}
		}	
	}

	public boolean onClose() {
		/*if(screenString.equalsIgnoreCase("ReportsOrdere") )
		{
			Vector vectorCommandData = new Vector();
			vectorCommandData.addElement("ReportsEquity");
			Action action = new Action(ActionCommand.CMD_REPORTS_SCREEN,null);
			ActionInvoker.processCommand(action);
		}
		else if(screenString.equalsIgnoreCase("ReportsOrderf") )
		{
			Vector vectorCommandData = new Vector();
			vectorCommandData.addElement("ReportsF&O");
			Action action = new Action(ActionCommand.CMD_REPORTS_SCREEN,null);
			ActionInvoker.processCommand(action);
		}
		else
			UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen());
		 */
		Action action = new Action(ActionCommand.CMD_GRID_SCREEN,null);
		ActionInvoker.processCommand(action);
		return true;
	}
	public LabelField getRefreshLink(final String text)
	{
		return new LabelField(text, FOCUSABLE) {

			public int getPreferredHeight() {
				return FontLoader.getFont(AppConstants.REPORTS_FONT).getHeight() + 4;
			}
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}

			protected void paint(Graphics graphics) {
				graphics.setColor(0xeeeeee);
				graphics.setFont(FontLoader.getFont(AppConstants.REPORTS_FONT));
				graphics.drawText("Refresh", 5, (getPreferredHeight()/2)-(graphics.getFont().getHeight()/2));
				graphics.setColor(isFocus()?Color.ORANGE:0xeeeeee);
				graphics.drawLine(4, (getPreferredHeight()/2)-(graphics.getFont().getHeight()/2)+ graphics.getFont().getHeight()+1, 5+graphics.getFont().getAdvance("Refresh"),  (getPreferredHeight()/2)-(graphics.getFont().getHeight()/2)+ graphics.getFont().getHeight()+1);
			}
			protected void drawFocus(Graphics graphics, boolean on) {
			}

			protected void layout(int width, int height) {
				super.layout(width, height);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
			protected void paintBackground(Graphics graphics) 
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
			}
			protected boolean navigationClick(int arg0, int arg1) {
				LOG.print("Refresh "+text+ " screen");
				if(screenString.equalsIgnoreCase("ReportsOrdere"))
				{
					Vector dataUrl = new Vector();
					String url = Utils.getReportsEquityURL(UserInfo.getUserID());;
					dataUrl.addElement(screenString);
					dataUrl.addElement(url);
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
				}
				else if(screenString.equalsIgnoreCase("ReportsOrderf"))
				{
					Vector dataUrl = new Vector(); 
					String url = Utils.getReportsFNOURL(UserInfo.getUserID());//AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnotr01&rtype=trade&custId="+UserInfo.getUserID();
					dataUrl.addElement("ReportsOrderf");
					dataUrl.addElement(url);
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
				}
				else if(screenString.equalsIgnoreCase("OrderBKEquity"))
				{
					Vector dataUrl = new Vector();
					String url = Utils.getReportsEquityURL(UserInfo.getUserID());
					dataUrl.addElement("OrderBKEquity");
					dataUrl.addElement(url);
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
				}
				else if(screenString.equalsIgnoreCase("OrderBKF&O"))
				{
					Vector dataUrl = new Vector();
					String url = Utils.getReportsFNOURL(UserInfo.getUserID());//AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnotr01&rtype=trade&custId="+UserInfo.getUserID();
					dataUrl.addElement("OrderBKF&O");
					dataUrl.addElement(url);
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
				}
				return super.navigationClick(arg0, arg1);
			}
		};
	}
	public LabelField getLabel(final String text)
	{
		return new LabelField(text, FOCUSABLE) {

			public int getPreferredHeight() {
				return FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight() * 3;
			}
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}

			protected void paint(Graphics graphics) {
				graphics.setColor(isFocus()?Color.ORANGE:0xeeeeee);
				graphics.fillRoundRect(3, 3, getPreferredWidth()-6, getPreferredHeight()-6, 8, 8);
				graphics.setColor(0x333333);
				graphics.fillRoundRect(4, 4, getPreferredWidth()-8, getPreferredHeight()-8, 8, 8);
				graphics.setColor(0xeeeeee);
				graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
				graphics.drawText(text, (getPreferredWidth()/2)-(graphics.getFont().getAdvance(text)/2), (getPreferredHeight()/2)-(graphics.getFont().getHeight()/2));
				//super.paint(graphics);
			}
			protected void drawFocus(Graphics graphics, boolean on) {
			}

			protected void layout(int width, int height) {
				super.layout(width, height);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
			protected void paintBackground(Graphics graphics) 
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
			}
		};
	}
}
