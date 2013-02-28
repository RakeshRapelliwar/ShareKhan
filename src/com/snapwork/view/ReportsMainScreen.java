package com.snapwork.view;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.Dialog;
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
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.CustomLinkButton;
import com.snapwork.components.ProfileChangeListField;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.ReportsListField;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;
import com.snapwork.view.trade.SlideViewOrderCancel;

public class ReportsMainScreen extends MainScreen implements ActionListener {
	private BottomMenu bottomMenu = null;
	private VerticalFieldManager profileChangeResults = null;
	private String id;
	//public static String URLReportsOrdere = AppConstants.domainUrl+"SK_android/controller.php?RequestId=steqo01&restrictBack=1&custId="+UserInfo.getUserID()+"&rtype=order";
	//public static String URLReportsOrderf = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoo01&rtype=order&custId="+UserInfo.getUserID();//"FOreports.php?rtype=order&custId="+UserInfo.getUserID()+"&page=FOreports&debug=2";
	//public static String URLOrderBKEquity = AppConstants.domainUrl + "SK_android/controller.php?RequestId=steqo01&restrictBack=1&custId="+UserInfo.getUserID()+"&rtype=order";
	//public static String URLOrderBKFnO = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoo01&rtype=order&custId="+UserInfo.getUserID();//"orderBK.php?custId="+UserInfo.getUserID()+"&btype=fo&page=orderBK&debug=2";
	public boolean first;
	public ReportsMainScreen(String id)
	{
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		this.id = id;
		first = true;
		createUI(id);
	}

	public void createUI(String id)
	{
		this.id = id;
		Vector vec = new Vector();
		String screentitle = "";
		boolean flag = true;
		if(this.id.equalsIgnoreCase("Reports"))
		{
			screentitle = "Reports";
			flag = false;
			vec = getReportsScreenVector();
		}
		else if(this.id.equalsIgnoreCase("ReportsEquity"))
		{
			screentitle = "Equity Reports";
			flag = false;
			vec = getReportsEquityScreenVector();
		}
		else if(this.id.equalsIgnoreCase("ReportsF&O"))
		{
			screentitle = "F&O Reports";
			flag = false;
			vec = getReportsFNOScreenVector();
		}
		else if(this.id.equalsIgnoreCase("Order_Book"))
		{
			screentitle = "Order Book";
			flag = false;
			vec = getOrderBookScreenVector();
		}
		VerticalFieldManager titleBar = new VerticalFieldManager(USE_ALL_WIDTH);
		titleBar.add(new TitleBar(screentitle));

		VerticalFieldManager mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR)
		{
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
						setExtent(maxWidth, AppConstants.screenHeight-BottomMenu.BottomMenuImageHeight-TitleBar.getItemHeight());
					}
				} else {
					if(getField(0).getPreferredHeight()<AppConstants.screenHeight-TitleBar.getItemHeight()) {
						setExtent(maxWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
					}            		
				}
				}
		};
		setTitle(titleBar);
		profileChangeResults = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR | USE_ALL_WIDTH);
		mainManager.add(profileChangeResults);
		add(mainManager);
		if(flag || first)
		{
			first = false;
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				ScreenInvoker.removeRemovableScreen();  
			}
		});
		final ReportsMainScreen rScreen = this;
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				ScreenInvoker.pushScreen(rScreen, true, true);
				/*if(SlideViewOrderCancel.orderCancel)
				{
					SlideViewOrderCancel.orderCancel = false;
							Dialog.alert(SlideViewOrderCancel.MESSAGE);
				}*/
			}
		});
		}
		fillProfileChangeData(vec);
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
		/*if(SlideViewOrderCancel.orderCancel)
		{
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					SlideViewOrderCancel.orderCancel = false;
					Dialog.alert(SlideViewOrderCancel.MESSAGE);
				}
			});
			
		}*/
	}

	public boolean onSavePrompt()
	{
		return true;
	}

	public boolean onMenu(int instance) 
	{
		return false;
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
else if(key == Keypad.KEY_ESCAPE) {
	if(this.id.equals("Reports") ||this.id.equals("Order_Book") )
	{
		ActionInvoker.processCommand(new Action(ActionCommand.CMD_GRID_SCREEN,null));
	}
	if(this.id.equals("ReportsEquity") || this.id.equals("ReportsF&O"))
	{
		//Reports
		Vector vectorCommandData = new Vector();
		String string = "Reports";
		vectorCommandData.addElement(string);
		//ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_SCREEN,vectorCommandData));
		deleteAll();
		createUI(string);
	}
	else if(this.id.equals("ReportsOrdere") || this.id.equals("ReportsMargin") || this.id.equals("ReportNetPosition") || this.id.equals("ReportsTradee") || this.id.equals("ReportsIntraday") || this.id.equals("ReportsIntraday") || this.id.equals("ReportsUnsettled_Position")  )
	{
		//ReportsEquity 
		Vector vectorCommandData = new Vector();
		String string = "ReportsEquity";
		vectorCommandData.addElement(string);
		//ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_SCREEN,vectorCommandData));
		deleteAll();
		createUI(string);
	}
	else if(this.id.equals("ReportsMarginf") || this.id.equals("ReportsTradef") || this.id.equals("ReportsTurnover") || this.id.equals("ReportsUL_Marginf"))
	{
		//ReportsF&O
		Vector vectorCommandData = new Vector();
		String string = "ReportsF&O";
		vectorCommandData.addElement(string);
		//ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_SCREEN,vectorCommandData));
		deleteAll();
		createUI(string);
	}
	else if(this.id.equals("OrderBKEquity") || this.id.equals("OrderBKF&O"))
	{
		//Order_Book
		Vector vectorCommandData = new Vector();
		String string = "Order_Book";
		vectorCommandData.addElement(string);
		//ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_SCREEN,vectorCommandData));
		deleteAll();
		createUI(string);
	}
	else
	{
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

	public boolean keyUp( int keyCode, int time )
	{
		return super.keyUp(keyCode, time);
	}

	public void componentsPrepared(byte componentID, Object component) {
		// TODO Auto-generated method stub

	}

	public void fillProfileChangeData(final Vector vector)
	{
		final ActionListener actionListner = this;
		UiApplication.getUiApplication().invokeLater(new Runnable()
		{
			public void run() {
				if(vector==null)
				{
					Dialog.alert("No Records Found!...");
					return;
				}
				else if(vector.size()==0)
				{
					Dialog.alert("No Records Found!...");
					return;
				}
				profileChangeResults.deleteAll();
				Vector stringVector = (Vector) vector.elementAt(0);
				Vector idVector = (Vector) vector.elementAt(1);
				for(int i=0;i<stringVector.size();i++)
				{
					String string = (String)stringVector.elementAt(i);
					String id = (String)idVector.elementAt(i);
					ReportsListField profileListField = new ReportsListField(FOCUSABLE, string, id, actionListner);
					profileChangeResults.add(profileListField);
					}
				profileChangeResults.add(new NullField(FOCUSABLE));
			}
		});
	}
	public void actionPerfomed(byte Command, Object data) {
		  switch(Command) {
		  case ActionCommand.CMD_REPORTSB_SCREEN:
			  if(this.id.equalsIgnoreCase("Reports")){
			  synchronized( UiApplication.getEventLock() ){

					if(isDisplayed()) 
					{
						close();
					}
				}
			  }
			  ActionInvoker.processCommand(new Action(Command,null));
			  break;
  		case ActionCommand.CMD_GRID_SCREEN:
      	case ActionCommand.CMD_WATCHLIST_SCREEN:
      	case ActionCommand.CMD_BSE_GL_SCREEN:
      	case ActionCommand.CMD_NEWS_SCREEN:
      	case ActionCommand.CMD_SEARCH_SCREEN:
			 ActionInvoker.processCommand(new Action(Command,null));
      		break;
          default:
          
		if(!Utils.sessionAlive)
		{
			ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
			return;
		}
		Vector vector = (Vector)data;
		String clickId = (String)vector.elementAt(0);
		LOG.print("Id "+this.id);
		if(this.id.equalsIgnoreCase("Reports") || this.id.equalsIgnoreCase("ReportsEquity") || this.id.equalsIgnoreCase("ReportsF&O") || this.id.equalsIgnoreCase("Order_Book"))
		{
			if(clickId.equalsIgnoreCase("ReportsOrdere"))
			{
				LOG.print("inside clickId "+clickId);
				Vector dataUrl = new Vector();
				String url = Utils.getReportsEquityURL(UserInfo.getUserID());
				//URLReportsOrdere = url;
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
			}
			else if(clickId.equalsIgnoreCase("ReportsMargin"))
			{
				LOG.print("inside clickId "+clickId);
				String url = AppConstants.domainUrl+"SK_android/controller.php?RequestId=steqm01&custId="+UserInfo.getUserID()+"&rtype=margin";
				 Vector vec = new Vector();
                 vec.addElement("ReportsMargin");
                 Vector e = new Vector();
                 e.addElement(url);
                 vec.addElement(e);
                 //actionListener.actionPerfomed(ActionCommand.CMD_SLIDE_VIEW, vec);
                 ActionInvoker.processCommand(new Action(ActionCommand.CMD_SLIDE_VIEW,vec));
         	}
			else if(clickId.equalsIgnoreCase("ReportsDP-SR"))
			{
				Vector dataUrl = new Vector();
				String url = AppConstants.domainUrl+"SK_android/controller.php?RequestId=steqdpsr01&custId="+UserInfo.getUserID()+"&rtype=dpsr";
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
			}
			else if(clickId.equalsIgnoreCase("ReportNetPosition"))
			{
				Vector dataUrl = new Vector();
				String url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=steqnp01&custId="+UserInfo.getUserID()+"&rtype=netpos";
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
			}
			else if(clickId.equalsIgnoreCase("ReportsTradee"))
			{
				Vector dataUrl = new Vector();
				String url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=steqtr01&custId="+UserInfo.getUserID()+"&rtype=trade";
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
			}
			else if(clickId.equalsIgnoreCase("ReportsTradef"))
			{
				Vector dataUrl = new Vector();
				//String url = AppConstants.domainUrl + "FOreports.php?custId="+UserInfo.getUserID()+"&rtype=trade&debug=2";
				String url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnotr01&rtype=trade&custId="+UserInfo.getUserID();
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
				
			}
			else if(clickId.equalsIgnoreCase("ReportsUnsettled_Position"))
			{
				Vector dataUrl = new Vector();
				String url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stequnsetpos01&custId="+UserInfo.getUserID()+"&rtype=unsetpos";
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
			}
			else if(clickId.equalsIgnoreCase("ReportsIntraday"))
			{
				Vector dataUrl = new Vector();
				String url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=steqintra01&custId="+UserInfo.getUserID()+"&rtype=intrada";
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
			}
			else if(clickId.equalsIgnoreCase("ReportsTurnover"))
			{
				Vector dataUrl = new Vector();
				//String url = AppConstants.domainUrl + "FOreports.php?custId="+UserInfo.getUserID()+"&rtype=turnover&debug=2";
				String url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoto01&rtype=turnover&custId="+UserInfo.getUserID();
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
			}
			else if(clickId.equalsIgnoreCase("ReportsMarginf"))
			{
				LOG.print("inside clickId "+clickId);
				//String url = AppConstants.domainUrl+"FOreports.php?custId="+UserInfo.getUserID()+"&rtype=fo-margin&debug=2";
				String url = AppConstants.domainUrl+"SK_android/controller.php?RequestId=stfnom01&rtype=margin&custId="+UserInfo.getUserID();
					Vector vec = new Vector();
                 vec.addElement("ReportsMarginf");
                 Vector e = new Vector();
                 e.addElement(url);
                 vec.addElement(e);
                 //actionListener.actionPerfomed(ActionCommand.CMD_SLIDE_VIEW, vec);
                 ActionInvoker.processCommand(new Action(ActionCommand.CMD_SLIDE_VIEW,vec));
         	}
			else if(clickId.equalsIgnoreCase("ReportsUL_Marginf"))
			{
				Vector dataUrl = new Vector();
				//String url = AppConstants.domainUrl + "FOreports.php?custId="+UserInfo.getUserID()+"&rtype=margin&debug=2";
				String url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoulm01&rtype=fomargin&custId="+UserInfo.getUserID();
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
			}
			else if(clickId.equalsIgnoreCase("OrderBKEquity"))
			{
				//http://50.17.252.160/SK_live/orderBK.php?custId=250037&btype=equity&page=orderBK
				Vector dataUrl = new Vector();
				String url = Utils.getReportsEquityURL(UserInfo.getUserID());
				//URLOrderBKEquity = url;
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
			}
			else if(clickId.equalsIgnoreCase("OrderBKF&O"))
			{
				//http://50.17.252.160/SK_live/orderBK.php?custId=250037&btype=fo&page=orderBK
				Vector dataUrl = new Vector();
				String url = Utils.getReportsFNOURL(UserInfo.getUserID());//AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoo01&rtype=order&custId="+UserInfo.getUserID();//"FOreports.php?rtype=order&custId="+UserInfo.getUserID()+"&page=FOreports&debug=2";
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
			}
			else if(clickId.equalsIgnoreCase("ReportsOrderf"))
			{
				Vector dataUrl = new Vector();
				String url = Utils.getReportsFNOURL(UserInfo.getUserID());//AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoo01&rtype=order&custId="+UserInfo.getUserID();//"FOreports.php?rtype=order&custId="+UserInfo.getUserID()+"&page=FOreports&debug=2";
				dataUrl.addElement(clickId);
				dataUrl.addElement(url);
				ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
			}
			else
				ActionInvoker.processCommand(new Action(Command,data));
		}
	       
        break;
}
		
	}
	
	public Vector getReportsScreenVector()
	{
		Vector v = new Vector();
		v.addElement("Equity");
		v.addElement("F&O");
		Vector idv = new Vector();
		idv.addElement("ReportsEquity");
		idv.addElement("ReportsF&O");
		Vector vec = new Vector();
		vec.addElement(v);
		vec.addElement(idv);
		return vec;
	}
	
	public Vector getReportsEquityScreenVector()
	{
		Vector v = new Vector();
		v.addElement("Order");
		v.addElement("Margin");
		v.addElement("DP / SR");
		v.addElement("Net Position");
		v.addElement("Trade");
		v.addElement("Intraday");
		v.addElement("Unsettled Position");
		Vector idv = new Vector();
		idv.addElement("ReportsOrdere");
		idv.addElement("ReportsMargin");
		idv.addElement("ReportsDP-SR");
		idv.addElement("ReportNetPosition");
		idv.addElement("ReportsTradee");
		idv.addElement("ReportsIntraday");
		idv.addElement("ReportsUnsettled_Position");
		Vector vec = new Vector();
		vec.addElement(v);
		vec.addElement(idv);
		return vec;
	}
	
	public Vector getReportsFNOScreenVector()
	{
		Vector v = new Vector();
		v.addElement("Order");
		v.addElement("Margin");
		v.addElement("Trade");
		v.addElement("Turnover");
		v.addElement("UL Margin");
		Vector idv = new Vector();
		idv.addElement("ReportsOrderf");
		idv.addElement("ReportsMarginf");
		idv.addElement("ReportsTradef");
		idv.addElement("ReportsTurnover");
		idv.addElement("ReportsUL_Marginf");
		Vector vec = new Vector();
		vec.addElement(v);
		vec.addElement(idv);
		return vec;
	}
	
	public Vector getOrderBookScreenVector()
	{
		Vector v = new Vector();
		v.addElement("Equity");
		v.addElement("F&O");
		Vector idv = new Vector();
		idv.addElement("OrderBKEquity");
		idv.addElement("OrderBKF&O");
		Vector vec = new Vector();
		vec.addElement(v);
		vec.addElement(idv);
		return vec;
	}
}
