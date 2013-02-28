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
import net.rim.device.api.ui.ScrollChangeListener;
import net.rim.device.api.ui.TouchEvent;
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
import com.snapwork.beans.KeyValueBean;
import com.snapwork.beans.ReportsOrderBean;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.RefreshButton;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TitleBarRefresh;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.parsers.ReportsOrderParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class SlideViewScreen extends MainScreen implements ActionListener,RemovableScreen, ReturnDataWithId, ScrollChangeListener
{
	private Hashtable hash ;
	private BottomMenu bottomMenu = null;
	private String stringId;
	private String screenClick;
	private RefreshButton refreshme;
	private String url;
	private SlideViewScreen rthis = this;
	private boolean refreshCall;
	private VerticalFieldManager vfm;
	private HorizontalFieldManager hfm;
	private Vector holder;
	private String storedString = "";
	private static int ID_OrderDetails = 10;
	private static int ID_OrderDetails_FNO = 9;
	private static int ID_ReportsMarginEF = 111;
	private static int ID_Equity_Net_Position = 12;
	private static int ID_Equity_Unsetteled_Position = 13;
	private static int ID_Equity_Trade_Position = 14;
	private static int ID_FNO_Trade_Position = 15;
	private static int ID_FNO_Turnover_Details = 16;
	private static int ID_FNO_Margin_ReportUL = 17;
	String[] actionChoiceText = {"Buy","Sell","Sell against Margin","Buy Max","Sell Max"};
    String[] actionChoiceTextShort = {"B","S","SS","BM","SM"};
	private long timer;
	public SlideViewScreen( String title, Vector vector) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		if(AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			Utils.snippetDiff = 15;
		timer = System.currentTimeMillis();
		stringId = title.replace(' ', '_');
		LOG.print("------------>>>> "+stringId);
		if(stringId.equalsIgnoreCase("Confirm_Cancellation_!"))
		{
			screenClick = (String) vector.elementAt(1); 
		}
		else
			screenClick = "";
		//ReportsMargin
			if(stringId.equalsIgnoreCase("ReportsMargin") || stringId.equalsIgnoreCase("ReportsMarginf")  || stringId.equalsIgnoreCase("Account"))
			{
				url = (String) vector.elementAt(0); 
				LOG.print(url);
				new ReportsOrderParser(url,this,11);
			}
		else
		{
			createUI(title, vector);
		}
	}
	/*protected void paintBackground(Graphics graphics) {
		graphics.setBackgroundColor(0x000000);
		graphics.clear();
		super.paintBackground(graphics);
	}*/

	public void createUI(String strTitle, final Vector confirmVector) {
		String title = strTitle.replace(' ', '_');
		//set Title
		if(stringId.equalsIgnoreCase("Account"))
			strTitle = "Equity Margin Report";
//		if(title.equalsIgnoreCase("Order_Details") || title.equalsIgnoreCase("tradenow"))
//		{
//			refreshme = new RefreshButton()
//			{
//				protected boolean navigationClick(int arg0, int arg1) {
//					if(!getLoading())
//					{
//						refreshCall = true;
//						LOG.print("Order ID :> "+storedString);
//						setLoading(true);
//						new ReportsOrderParser(url,rthis,ID_OrderDetails);
//					}
//					return super.navigationClick(arg0, arg1);
//					}
//			};
//			setTitle(new TitleBarRefresh(strTitle, refreshme));
//		}
//		else if(title.equalsIgnoreCase("F&O_Order_Details"))
//		{
//			refreshme = new RefreshButton()
//			{
//				protected boolean navigationClick(int arg0, int arg1) {
//					if(!getLoading())
//					{
//						refreshCall = true;
//						setLoading(true);
//						new ReportsOrderParser(url,rthis,ID_OrderDetails_FNO);
//					}
//					return super.navigationClick(arg0, arg1);
//					}
//			};
//			setTitle(new TitleBarRefresh(strTitle, refreshme));
//		}
//		else 
			if(stringId.equalsIgnoreCase("ReportsMargin") || stringId.equalsIgnoreCase("ReportsMarginf")  || stringId.equalsIgnoreCase("Account"))
		{
			refreshme = new RefreshButton()
			{
				protected boolean navigationClick(int arg0, int arg1) {
					if(!getLoading())
					{
						refreshCall = true;
						setLoading(true);
						LOG.print(url);
						new ReportsOrderParser(url,rthis,ID_ReportsMarginEF);
					}
					return super.navigationClick(arg0, arg1);
					}
			};
			//Sets the title
			setTitle(new TitleBarRefresh(strTitle, refreshme));
		}
//		else if(title.equalsIgnoreCase("Equity_Net_Position"))
//		{
//			refreshme = new RefreshButton()
//			{
//				protected boolean navigationClick(int arg0, int arg1) {
//					if(!getLoading())
//					{
//						setLoading(true);
//						refreshCall = true;
//						new ReportsOrderParser(url,rthis,ID_Equity_Net_Position);
//					}
//					return super.navigationClick(arg0, arg1);
//					}
//			};
//			setTitle(new TitleBarRefresh(strTitle, refreshme));
//		}
//		else if(title.equalsIgnoreCase("Equity_Unsettled_Position"))
//		{
//			refreshme = new RefreshButton()
//			{
//				protected boolean navigationClick(int arg0, int arg1) {
//					if(!getLoading())
//					{
//						setLoading(true);
//						refreshCall = true;
//						new ReportsOrderParser(url,rthis,ID_Equity_Unsettled_Position);
//					}
//					return super.navigationClick(arg0, arg1);
//					}
//			};
//			setTitle(new TitleBarRefresh(strTitle, refreshme));
//		}
//		else if(title.equalsIgnoreCase("Equity_Trade_Position"))
//		{
//			refreshme = new RefreshButton()
//			{
//				protected boolean navigationClick(int arg0, int arg1) {
//					if(!getLoading())
//					{
//						setLoading(true);
//						refreshCall = true;
//						new ReportsOrderParser(url,rthis,ID_Equity_Trade_Position);
//					}
//					return super.navigationClick(arg0, arg1);
//					}
//			};
//			setTitle(new TitleBarRefresh(strTitle, refreshme));
//		}
//		else if(title.equalsIgnoreCase("FNO_Trade_Position"))
//		{
//			refreshme = new RefreshButton()
//			{
//				protected boolean navigationClick(int arg0, int arg1) {
//					if(!getLoading())
//					{
//						setLoading(true);
//						refreshCall = true;
//						new ReportsOrderParser(url,rthis,ID_FNO_Trade_Position);
//					}
//					return super.navigationClick(arg0, arg1);
//					}
//			};
//			setTitle(new TitleBarRefresh(strTitle, refreshme));
//		}
//		else if(title.equalsIgnoreCase("FNO_Turnover_Details"))
//		{
//			refreshme = new RefreshButton()
//			{
//				protected boolean navigationClick(int arg0, int arg1) {
//					if(!getLoading())
//					{
//						setLoading(true);
//						refreshCall = true;
//						new ReportsOrderParser(url,rthis,ID_FNO_Turnover_Details);
//					}
//					return super.navigationClick(arg0, arg1);
//					}
//			};
//			setTitle(new TitleBarRefresh(strTitle, refreshme));
//		}
//		else if(title.equalsIgnoreCase("F&O_Margin_Report")  || title.equalsIgnoreCase("F&O_UL_Margin_Report") )
//		{
//			refreshme = new RefreshButton()
//			{
//				protected boolean navigationClick(int arg0, int arg1) {
//					if(!getLoading())
//					{
//						setLoading(true);
//						refreshCall = true;
//						new ReportsOrderParser(url,rthis,ID_FNO_Margin_ReportUL);
//					}
//					return super.navigationClick(arg0, arg1);
//					}
//			};
//			setTitle(new TitleBarRefresh(strTitle, refreshme));
//		}
		else
		{
			setTitle(new TitleBar(strTitle));
		}
		//final int titleBarHeight = titleBar.getPreferredHeight();

		vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR ) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}  
			
			/*protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(maxWidth,maxHeight);
				setExtent(maxWidth, AppConstants.screenHeight-titleBarHeight);
			}*/
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(maxWidth,maxHeight);
				if(getFieldCount()>0)
				{
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
				else
				{
					if(bottomMenu.isAttached) 
							setExtent(maxWidth, AppConstants.screenHeight-BottomMenu.BottomMenuImageHeight-TitleBar.getItemHeight());
					 else 	
						 setExtent(maxWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
					
				}
					//setExtent(maxWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
			}
		};
		//vfm.setScrollListener(this);
		
		hash = (Hashtable)confirmVector.elementAt(0);
		holder = new Vector();

		if(title.equalsIgnoreCase("Order_Details") || title.equalsIgnoreCase("Order_Detail")|| title.equalsIgnoreCase("tradenow"))
		{
			if(title.equalsIgnoreCase("Order_Detail"))
				title = "Order_Details";
			try{
			hash = (Hashtable)confirmVector.elementAt(0);
			}
			catch(Exception e)
			{
				hash = new Hashtable();
			}
			holder = new Vector();
			url = (String)hash.get("url");
			storedString = (String)hash.get("order_id");
				if(hash.containsKey("exchorderid"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exch Id");
					bean.setValue((String)hash.get("exchorderid"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exch Id");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("order_id"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Id");
					bean.setValue((String)hash.get("order_id"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Id");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("scripcode"))
				{
					KeyValueBean bean = new KeyValueBean();
					String sm = (String)hash.get("scripcode");
					if(sm.length()<22)
					{
						bean.setKey("Scrip");
						bean.setValue(sm);
					}
					else
					{
						bean.setKey("@Scrip");
						bean.setValue(sm);
					}
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Scrip");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("segment"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Segment");
					bean.setValue((String)hash.get("segment"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Segment");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("orderdatetime"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order DateTime");
					bean.setValue((String)hash.get("orderdatetime"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order DateTime");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("orderstatus"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Status");
					bean.setValue((String)hash.get("orderstatus"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Status");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("requeststatus"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Request Status");
					bean.setValue((String)hash.get("requeststatus"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Request Status");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("disclosedqty"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Dis. Qty");
					bean.setValue((String)hash.get("disclosedqty"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Dis. Qty");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("orderprice"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Price");
					String t = (String)hash.get("orderprice");
					if(t.length()==0)
						t = "Mkt";
					else
					{
						double d = Double.parseDouble(t);
						if(d<=0)
							t = "Mkt";
					}
					bean.setValue(t);
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Price");
					bean.setValue("Mkt");
					holder.addElement(bean);
				}
				if(hash.containsKey("trigprice"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Trigger Price");
					bean.setValue((String)hash.get("trigprice"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Trigger Price");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("execqty"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exec. Qty");
					bean.setValue((String)hash.get("execqty"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exec. Qty");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("execprice"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exec. Price");
					bean.setValue((String)hash.get("execprice"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exec. Price");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("exchdatetime"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exch DateTime");
					bean.setValue((String)hash.get("exchdatetime"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exch DateTime");
					bean.setValue(" - ");
					holder.addElement(bean);
				}	
				if(hash.containsKey("rmscode"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("RMS Code");
					bean.setValue((String)hash.get("rmscode"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("RMS Code");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				SlideView s = new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT));
				s.setScrollListener(this);
				vfm.add(s);
		}
		else if(title.equalsIgnoreCase("Confirm_Cancellation_!"))
		{
			LOG.print("Confirm_Cancellation_! "+screenClick);
			final Bitmap bmp = ImageManager.getTradeNow();
			String text = "";
			if(hash.containsKey("company_code")) //Scrip - Exchange
			{
				text = (String)hash.get("company_code");
			}
			if(hash.containsKey("exchange")) // Exhchange
			{
				text = text + " - " + (String)hash.get("exchange");				
			}
			KeyValueBean b = new KeyValueBean();
			b.setKey("Scrip - Exchange");
			b.setValue("");
			holder.addElement(b);
			KeyValueBean beanval = new KeyValueBean();
			beanval.setKey(text);
			beanval.setValue("");
			holder.addElement(beanval);
			text = "";
			if(hash.containsKey("qty")) // Quantity - Action
			{
				text = (String)hash.get("qty");
			}
			if(hash.containsKey("action")) // Exchange
			{
				String actn = (String)hash.get("action");
				for(int i=0;i<actionChoiceTextShort.length;i++)
    			{
    				if(actn.equalsIgnoreCase(actionChoiceTextShort[i]))
    				{
    					actn = actionChoiceText[i];
    					break;
    				}
    			}
				text = text + " - " + actn;	
			}
			KeyValueBean bean2 = new KeyValueBean();
			bean2.setKey("Quantity - Action");
			bean2.setValue("");
			holder.addElement(bean2);
			KeyValueBean bean2val = new KeyValueBean();
			bean2val.setKey(text);
			bean2val.setValue("");
			holder.addElement(bean2val);
			text = "";
			if(hash.containsKey("orderstatus")) // Status
			{
				text = (String)hash.get("orderstatus");
			}
			LOG.print(" Order Status "+text);
			KeyValueBean beanx = new KeyValueBean();
			beanx.setKey("Status");
			beanx.setValue("");
			holder.addElement(beanx);
			KeyValueBean beanxval = new KeyValueBean();
			beanxval.setKey(text);
			beanxval.setValue("");
			holder.addElement(beanxval);
			SlideViewOrderCancel s = new SlideViewOrderCancel(holder,FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
			s.setScrollListener(this);
			vfm.add(s);
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
						if((timer+1000)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
						if(!Utils.sessionAlive)
    					{
    						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
    					}
    					else
    					{ 
						//http://50.17.18.243/SK_live/EQreports.php?rtype=order&exchange=BSE&custId=250037&errormsg=mod_err&msg=Normal%20Market%20is%20Closed&order_number=243531874
							//String url = AppConstants.domainUrl + "EQreports.php?rtype=order&exchange="+exchLabelValue+"&custId="+UserInfo.getUserID()+"&errormsg=mod_err&msg=Only%20AfterHour%20order%20is%20allowed%20to%20Modify/Cancel&order_number="+orderNoLabelValue+"&debug=2";
    						String url = "";
    						String rmscode = (String)hash.get("rmscode");
    						if(rmscode == null)
    							rmscode = (String)hash.get("userid");
    						else if(rmscode.length()==0)
    							rmscode = (String)hash.get("userid");
    						LOG.print("RMSCODE "+rmscode);
    					if(screenClick.equals("ReportsOrdere") || screenClick.equals("OrderBKEquity"))
						{
							url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=steqo05&restrictBack=1&btnDelete=Delete&btnCancel=&orderId="+(String)hash.get("order_id")+"&custId="+UserInfo.getUserID()+"&exchange="+(String)hash.get("exchange")+"&page=reports&rmsCode="+rmscode;
						}
						else if(screenClick.equals("ReportsOrderf") || screenClick.equals("OrderBKF&O"))
						{
							//url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoo04&custId="+UserInfo.getUserID()+"&orderId="+(String)hash.get("order_id")+"&exchange=NSEFO&btnModify=&btnDel=&page=reports&userAgent=bb&rmsCode="+rmscode;
							url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoo05&orderId="+(String)hash.get("order_id")+"&exchange="+(String)hash.get("exchange")+"&custId="+UserInfo.getUserID()+"&page=reports&rmsCode="+(String)hash.get("rmscode")+"&btnDelete=Delete&btnCancel=&rtype=order";
						}
						/*else if(screenClick.equals("ReportsOrderf"))
						{
							//http://50.17.252.160/SK_live/orderTransaction.php?orderId=58294431&exchange=NSEFO&custId=250037&page=FOreports&rmsCode=SKSIMFO1&btnDelete=Delete&btnCancel=
							url = AppConstants.domainUrl + "orderTransaction.php?orderId="+(String)hash.get("order_id")+"&exchange="+(String)hash.get("exchange")+"&custId="+UserInfo.getUserID()+"&page=FOreports&rmsCode="+rmscode+"&btnDelete=Delete&btnCancel=&debug=2";
						}
						else if(screenClick.equals("OrderBKF&O"))
						{
							url = AppConstants.domainUrl + "orderTransaction.php?orderId="+(String)hash.get("order_id")+"&exchange="+(String)hash.get("exchange")+"&custId="+UserInfo.getUserID()+"&page=orderBK&rmsCode="+rmscode+"&btnDelete=Delete&btnCancel=&debug=2";
							
						}*/
    					SlideViewOrderCancel.orderCancel = true;
						Vector dataUrl = new Vector();
						dataUrl.addElement(screenClick);
						dataUrl.addElement(url);
						LOG.print("Confirmclick<--->"+url);
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
						
    					}	}return super.navigationClick(status, time);
					}
					protected boolean touchEvent(TouchEvent message) 
					{
						if(message.getEvent() == TouchEvent.CLICK) {
							if((timer+1000)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
							if(!Utils.sessionAlive)
    					{
    						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
    					}
    					else
    					{ 	String url = "";
    					if(screenClick.equals("ReportsOrdere") || screenClick.equals("OrderBKEquity"))
						{
							url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=steqo05&restrictBack=1&btnDelete=Delete&btnCancel=&orderId="+(String)hash.get("order_id")+"&custId="+UserInfo.getUserID()+"&exchange="+(String)hash.get("exchange")+"&page=reports&rmsCode="+hash.get("rmscode");
						}
    					
						else if(screenClick.equals("ReportsOrderf") || screenClick.equals("OrderBKF&O"))
						{
							//url = AppConstants.domainUrl + "orderTransaction.php?orderId="+(String)hash.get("order_id")+"&exchange="+(String)hash.get("exchange")+"&custId="+UserInfo.getUserID()+"&page=orderBK&rmsCode="+hash.get("rmscode")+"&btnDelete=Delete&btnCancel=&debug=2";
							url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoo05&orderId="+(String)hash.get("order_id")+"&exchange="+(String)hash.get("exchange")+"&custId="+UserInfo.getUserID()+"&page=reports&rmsCode="+hash.get("rmscode")+"&btnDelete=Delete&btnCancel=&rtype=order";
						}
    					SlideViewOrderCancel.orderCancel = true;
						Vector dataUrl = new Vector();
						dataUrl.addElement(screenClick);
						dataUrl.addElement(url);
							LOG.print("Confirmtouch<--->"+url);
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
						
    					}
						}
					}return super.touchEvent(message);
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
					
					final String textButtonBack = "Back";
					BitmapField btnBack = new BitmapField(bmp,  FOCUSABLE | DrawStyle.HCENTER)  {

						protected boolean navigationClick(int status,int time) {
							if((timer+1000)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
							if(!Utils.sessionAlive)
        					{
        						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
        					}
        					else
        					{ 
        						String url = "";
        						String rmscode = (String)hash.get("rmscode");
        						if(rmscode == null)
        							rmscode = (String)hash.get("userid");
        						else if(rmscode.length()==0)
        							rmscode = (String)hash.get("userid");
        						LOG.print("RMSCODE "+rmscode);
							if(screenClick.equalsIgnoreCase("ReportsOrdere") || screenClick.equalsIgnoreCase("OrderBKEquity"))
							{
								 //url = AppConstants.domainUrl + "orderTransaction.php?orderId="+(String)hash.get("order_id")+"&exchange="+(String)hash.get("exchange")+"&custId="+UserInfo.getUserID()+"&page=orderStatement&rmsCode="+rmscode+"&btnDelete=&btnCancel=Cancel&debug=2";
								 url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=steqo04&custId="+UserInfo.getUserID()+"&orderId="+(String) hash.get("order_id")+"&exchange="+(String) hash.get("exchange")+"&btnModify=&btnDel=&page=reports&userAgent=bb&rmsCode="+(String) hash.get("rmscode");
							}
							else if(screenClick.equalsIgnoreCase("ReportsOrderf") || screenClick.equalsIgnoreCase("OrderBKF&O"))
							{
								url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoo04&custId="+UserInfo.getUserID()+"&orderId="+(String)hash.get("order_id")+"&exchange="+(String)hash.get("exchange")+"&btnModify=&btnDel=&page=reports&userAgent=bb&rmsCode="+(String)hash.get("userid");
							}
							
							Vector dataUrl = new Vector();
								//http://220.226.189.186/SK_live/EQreports.php?rtype=order&exchange=NSE&custId=250037
								//http://220.226.189.186/SK_live/orderTransaction.php?orderId=304248929&exchange=NSE&custId=250037&page=EQreports&rmsCode=SKDRNSE1&btnDelete=Delete&btnCancel=&btnDelete1.x=85&btnDelete1.y=7&btnDelete1=Delete
								//String url = AppConstants.domainUrl + "orderTransaction.php?orderId="+hash.containsKey("order_id")+"&exchange="+hash.containsKey("exchange")+"&custId="+UserInfo.getUserID()+"&page=EQreports&rmsCode="+hash.containsKey("rmscode")+"&btnDelete=Delete&btnCancel=&btnDelete1.x=85&btnDelete1.y=7&btnDelete1=Delete";
								
								//http://50.17.252.160/SK_live/orderTransaction.php?orderId=243531961&exchange=NSE&custId=250037&page=orderStatement&rmsCode=SKSIMNSE1&btnDelete=&btnCancel=Cancel
								dataUrl.addElement(screenClick);
								dataUrl.addElement(url);
								LOG.print(url);
								ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
							
        					}}return super.navigationClick(status, time);
						}
						protected boolean touchEvent(TouchEvent message) 
						{
							if(message.getEvent() == TouchEvent.CLICK) {
								if((timer+1000)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
								if(!Utils.sessionAlive)
	        					{
	        						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
	        					}
	        					else
	        					{ 
	        						String url = "";
								if(screenClick.equalsIgnoreCase("ReportsOrdere"))
								{
									 url = AppConstants.domainUrl + "orderTransaction.php?orderId="+(String)hash.get("order_id")+"&exchange="+(String)hash.get("exchange")+"&custId="+UserInfo.getUserID()+"&page=orderStatement&rmsCode="+(String)hash.get("rmscode")+"&btnDelete=&btnCancel=Cancel&debug=2";
								}		
								else if(screenClick.equalsIgnoreCase("OrderBKEquity"))
								{
									 url = AppConstants.domainUrl + "orderTransaction.php?orderId="+(String)hash.get("order_id")+"&exchange="+(String)hash.get("exchange")+"&custId="+UserInfo.getUserID()+"&page=orderBK&rmsCode="+(String)hash.get("rmscode")+"&btnDelete=&btnCancel=Cancel&debug=2";
								}
								else if(screenClick.equalsIgnoreCase("ReportsOrderf"))
								{
									//http://50.17.252.160/SK_live/orderTransaction.php?orderId=58294430&exchange=NSEFO&custId=250037&page=FOreports&rmsCode=SKSIMFO1&btnDelete=&btnCancel=Cancel
									 url = AppConstants.domainUrl + "orderTransaction.php?orderId="+(String)hash.get("order_id")+"&exchange="+(String)hash.get("exchange")+"&custId="+UserInfo.getUserID()+"&page=FOreports&rmsCode="+(String)hash.get("rmscode")+"&btnDelete=&btnCancel=Cancel&debug=2";
								}		
								else if(screenClick.equalsIgnoreCase("OrderBKF&O"))
								{
									//http://50.17.252.160/SK_live/orderTransaction.php?orderId=58294429&exchange=NSEFO&custId=250037&page=orderBK&rmsCode=SKSIMFO1&btnDelete=&btnCancel=Cancel
									 url = AppConstants.domainUrl + "orderTransaction.php?orderId="+(String)hash.get("order_id")+"&exchange="+(String)hash.get("exchange")+"&custId="+UserInfo.getUserID()+"&page=orderBK&rmsCode="+(String)hash.get("rmscode")+"&btnDelete=&btnCancel=Cancel&debug=2";
								}
								
								Vector dataUrl = new Vector();
									//http://220.226.189.186/SK_live/EQreports.php?rtype=order&exchange=NSE&custId=250037
									//http://220.226.189.186/SK_live/orderTransaction.php?orderId=304248929&exchange=NSE&custId=250037&page=EQreports&rmsCode=SKDRNSE1&btnDelete=Delete&btnCancel=&btnDelete1.x=85&btnDelete1.y=7&btnDelete1=Delete
									//String url = AppConstants.domainUrl + "orderTransaction.php?orderId="+hash.containsKey("order_id")+"&exchange="+hash.containsKey("exchange")+"&custId="+UserInfo.getUserID()+"&page=EQreports&rmsCode="+hash.containsKey("rmscode")+"&btnDelete=Delete&btnCancel=&btnDelete1.x=85&btnDelete1.y=7&btnDelete1=Delete";
									
									//http://50.17.252.160/SK_live/orderTransaction.php?orderId=243531961&exchange=NSE&custId=250037&page=orderStatement&rmsCode=SKSIMNSE1&btnDelete=&btnCancel=Cancel
									dataUrl.addElement(screenClick);
									dataUrl.addElement(url);
									LOG.print(url);
									ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
								
	        					}
							}}return super.touchEvent(message);
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
							graphics.drawText(textButtonBack,(bmp.getWidth()/2)-(getFont().getAdvance(textButtonBack)/2),(bmp.getHeight()/2)-(getFont().getHeight()/2));
							
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
						vfm.add(Utils.separator(10, 0x000000));
						hfmobj.add(btnConfirm);
						hfmobj.add(btnBack);
					vfm.add(hfmobj);
					/*UiApplication.getUiApplication().invokeLater(new Runnable() {
	                    public void run() {
	                            Dialog.alert(screenClick);
	                    }
	            });*/
		}
		else if(stringId.equalsIgnoreCase("ReportsMargin") || stringId.equalsIgnoreCase("ReportsMarginf") || stringId.equalsIgnoreCase("Account") )
		{
			hash = (Hashtable)confirmVector.elementAt(0);
			holder = new Vector();
			//url = (String)hash.get("url");
			if(hash.containsKey("CURRENTCASHBALANCE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Current cash balance");
				bean.setValue((String)hash.get("CURRENTCASHBALANCE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Current cash balance");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("PENDINGWITHDRAWALREQUEST"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Pending Withdrawal Requests");
				bean.setValue((String)hash.get("PENDINGWITHDRAWALREQUEST"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Pending Withdrawal Requests");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("NONCASHLIMIT"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Non Cash Limit");
				bean.setValue((String)hash.get("NONCASHLIMIT"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Non Cash Limit");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("LIMITAGAINSTSHARES"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) DP Margin");
				bean.setValue((String)hash.get("LIMITAGAINSTSHARES"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) DP Margin");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("CASHBPL"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Cash BPL");
				bean.setValue((String)hash.get("CASHBPL"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Cash BPL");
				bean.setValue("");
				holder.addElement(bean);
			}
			/*if(hash.containsKey("CASHMTM"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Cash MTM");
				bean.setValue((String)hash.get("CASHMTM"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Cash MTM");
				bean.setValue("");
				holder.addElement(bean);
			}*/
			if(hash.containsKey("CASHPREVIOUSSETTLEMENTEXPOSURE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Cash Prev Settlement Exp.");
				bean.setValue((String)hash.get("CASHPREVIOUSSETTLEMENTEXPOSURE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Cash Prev Settlement Exp.");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("INTRADAYMARGINCASH"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Intraday Margin Cash");
				bean.setValue((String)hash.get("INTRADAYMARGINCASH"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Intraday Margin Cash");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("FNOMTM"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("FNO MTM");
				bean.setValue((String)hash.get("FNOMTM"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("FNO MTM");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("FNOPREMIUM"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) FNO Premium");
				bean.setValue((String)hash.get("FNOPREMIUM"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) FNO Premium");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("FNOBPL"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) FNO BPL");
				bean.setValue((String)hash.get("FNOBPL"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) FNO BPL");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("INTRADAYMARGINFNO"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Intraday Margin FNO");
				bean.setValue((String)hash.get("INTRADAYMARGINFNO"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Intraday Margin FNO");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("HOLD_FUNDS"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Hold Funds");
				bean.setValue((String)hash.get("HOLD_FUNDS"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Hold Funds");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("TOTAL"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Total Amount");
				bean.setValue((String)hash.get("TOTAL"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Total Amount");
				bean.setValue("");
				holder.addElement(bean);
			}
			SlideViewMargin s = new SlideViewMargin(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT),SlideViewMargin.ALIGN_RIGHT_WITH_LAST_BOLD);
			s.setScrollListener(this);
			vfm.add(s);
		}
		else if(title.equalsIgnoreCase("Equity_Net_Position"))
		{
			url = (String)hash.get("url");
			storedString = (String)hash.get("SCRIPCODE");
			if(hash.containsKey("SCRIPCODE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Scrip");
				bean.setValue((String)hash.get("SCRIPCODE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Scrip");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("EXCHANGE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exchange");
				bean.setValue((String)hash.get("EXCHANGE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exchange");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("SEGMENT"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Segment");
				bean.setValue((String)hash.get("SEGMENT"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Segment");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("NETQTY"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Qty");
				bean.setValue((String)hash.get("NETQTY"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Qty");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("PRODUCTTYPE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Product Type");
				bean.setValue((String)hash.get("PRODUCTTYPE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Product Type");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("BUYQTY"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Buy Qty");
				bean.setValue((String)hash.get("BUYQTY"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Buy Qty");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("BUYRATE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Buy Rate");
				bean.setValue((String)hash.get("BUYRATE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Buy Rate");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("SELLQTY"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Sell Qty");
				bean.setValue((String)hash.get("SELLQTY"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Sell Qty");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("SELLRATE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Sell Rate");
				bean.setValue((String)hash.get("SELLRATE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Sell Rate");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("BPL"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Booked PL");
				bean.setValue((String)hash.get("BPL"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Booked PL");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("MTM"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("MTM");
				bean.setValue((String)hash.get("MTM"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("MTM");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			SlideView s = new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT));
			s.setScrollListener(this);
			vfm.add(s);
	}
		else if(title.equalsIgnoreCase("Equity_Trade_Position"))
		{
			url = (String)hash.get("url");
			storedString = (String)hash.get("SHAREKHAN_SCRIP_CODE");
			if(hash.containsKey("TRADE_ID"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade ID");
				bean.setValue((String)hash.get("TRADE_ID"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade ID");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("ORDER_ID"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Order ID");
				bean.setValue((String)hash.get("ORDER_ID"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Order ID");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("EXCH_ORDER_ID"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exch. Order ID");
				bean.setValue((String)hash.get("EXCH_ORDER_ID"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exch. Order ID");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("SHAREKHAN_SCRIP_CODE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Scrip ");
				bean.setValue((String)hash.get("SHAREKHAN_SCRIP_CODE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Scrip");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("BUY_SELL"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Buy/Sell");
				String text = (String)hash.get("BUY_SELL");
				String[] actionChoiceText = {"Buy","Sell","Sell against Margin","Buy Max","Sell Max"};
            	String[] actionChoiceTextShort = {"B","S","SS","BM","SM"};
            	for(int i=0;i<actionChoiceTextShort.length;i++)
    			{
    				if(text.equalsIgnoreCase(actionChoiceTextShort[i]))
    				{
    					text = actionChoiceText[i];
    					break;
    				}
    			}
				bean.setValue(text);
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Buy/Sell");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("TRADE_QTY"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exec. Qty");
				bean.setValue((String)hash.get("TRADE_QTY"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exec. Qty");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("TRADE_PRICE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exec. Price");
				String text = (String)hash.get("TRADE_PRICE");
				double num = Double.parseDouble(text) / 100;
        		text = Utils.DecimalRoundString(num, 2);
				bean.setValue(text);
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exec. Price");
				bean.setValue(" 0.00 ");
				holder.addElement(bean);
			}
			if(hash.containsKey("TRADE_AMOUNT"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade Val");
				String text = (String)hash.get("TRADE_AMOUNT");
				double num = Double.parseDouble(text) / 100;
        		text = Utils.DecimalRoundString(num, 2);
				bean.setValue(text);
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade Val");
				bean.setValue(" 0.00 ");
				holder.addElement(bean);
			}
			if(hash.containsKey("TRADE_DATE_TIME"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade Time");
				bean.setValue((String)hash.get("TRADE_DATE_TIME"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade Time");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			SlideView s = new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT));
			s.setScrollListener(this);
			vfm.add(s);
	}
		else if(title.equalsIgnoreCase("FNO_Trade_Position"))
		{
			url = (String)hash.get("url");
			storedString = (String)hash.get("SHAREKHAN_SCRIP_CODE");
			if(hash.containsKey("TRADE_ID"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade ID");
				bean.setValue((String)hash.get("TRADE_ID"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade ID");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("ORDER_ID"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Order ID");
				bean.setValue((String)hash.get("ORDER_ID"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Order ID");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("EXCH_ORDER_ID"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exch. Order ID");
				bean.setValue((String)hash.get("EXCH_ORDER_ID"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exch. Order ID");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("SHAREKHAN_SCRIP_CODE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Scrip ");
				bean.setValue((String)hash.get("SHAREKHAN_SCRIP_CODE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Scrip");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("BUY_SELL"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Buy/Sell");
				String text = (String)hash.get("BUY_SELL");
				String[] actionChoiceText = {"Buy","Sell","Sell against Margin","Buy Max","Sell Max"};
            	String[] actionChoiceTextShort = {"B","S","SS","BM","SM"};
            	for(int i=0;i<actionChoiceTextShort.length;i++)
    			{
    				if(text.equalsIgnoreCase(actionChoiceTextShort[i]))
    				{
    					text = actionChoiceText[i];
    					break;
    				}
    			}
				bean.setValue(text);
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Buy/Sell");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("TRADE_QTY"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exec. Qty");
				bean.setValue((String)hash.get("TRADE_QTY"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exec. Qty");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("TRADE_PRICE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exec. Price");
				String text = (String)hash.get("TRADE_PRICE");
				double num = Double.parseDouble(text) / 100;
        		text = Utils.DecimalRoundString(num, 2);
				bean.setValue(text);
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Exec. Price");
				bean.setValue(" 0.00 ");
				holder.addElement(bean);
			}
			if(hash.containsKey("TRADE_AMOUNT"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade Val");
				String text = (String)hash.get("TRADE_AMOUNT");
				double num = Double.parseDouble(text) / 100;
        		text = Utils.DecimalRoundString(num, 2);
				bean.setValue(text);
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade Val");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("TRADE_DATE_TIME"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade Time");
				bean.setValue((String)hash.get("TRADE_DATE_TIME"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Trade Time");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			SlideView s = new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT));
			s.setScrollListener(this);
			vfm.add(s);
	}
		else if(title.equalsIgnoreCase("Equity_Unsettled_Position"))
		{
			url = (String)hash.get("url");
			storedString = (String)hash.get("SCRIPCODE");
			if(hash.containsKey("SCRIPCODE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Scrip");
				bean.setValue((String)hash.get("SCRIPCODE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Scrip");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
				if(hash.containsKey("EXCHANGE"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exchange");
					bean.setValue((String)hash.get("EXCHANGE"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exchange");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("SETTLEMENTNO"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Settlement No");
					bean.setValue((String)hash.get("SETTLEMENTNO"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Settlement No");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("SETTLEMENTDATE"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Settlement Date");
					bean.setValue((String)hash.get("SETTLEMENTDATE"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Settlement Date");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("TOTALBUYQTY"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Total Buy Qty");
					bean.setValue((String)hash.get("TOTALBUYQTY"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Total Buy Qty");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("TOTALBUYVALUE"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Total Buy Value");
					bean.setValue((String)hash.get("TOTALBUYVALUE"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Total Buy Value");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("TOTALSELLQTY"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Total Sell Qty");
					bean.setValue((String)hash.get("TOTALSELLQTY"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Total Sell Qty");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("TOTALSELLVALUE"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Total Sell Value");
					bean.setValue((String)hash.get("TOTALSELLVALUE"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Total Sell Value");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("BPL"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("BPL");
					bean.setValue((String)hash.get("BPL"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("BPL");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("NETPOSQTY"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Net Pos Qty");
					bean.setValue((String)hash.get("NETPOSQTY"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Net Pos Qty");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("NETVALUE"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Net Value");
					bean.setValue((String)hash.get("NETVALUE"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Net Value");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("MARGIN"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Margin");
					bean.setValue((String)hash.get("MARGIN"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Margin");
					bean.setValue(" - ");
					holder.addElement(bean);
				}	
				if(hash.containsKey("LIMITONPOS"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Limit On Pos");
					bean.setValue((String)hash.get("LIMITONPOS"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Limit On Pos");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("NETLIMIT"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Net Limit");
					bean.setValue((String)hash.get("NETLIMIT"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Net Limit");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				SlideView s = new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT));
				s.setScrollListener(this);
				vfm.add(s);
		}
		else if(title.equalsIgnoreCase("FNO_Turnover_Details"))
		{
			url = (String)hash.get("url");
			storedString = (String)hash.get("SHAREKHAN_SCRIP_CODE");
			
			if(hash.containsKey("NET_SETTLED_MTM"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("MTM");
				bean.setValue((String)hash.get("NET_SETTLED_MTM"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("MTM");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("OPENQTY"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Open Qty");
				bean.setValue((String)hash.get("OPENQTY"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Open Qty");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("OPENINGRATE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Open Rate");
				bean.setValue((String)hash.get("OPENINGRATE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Open Rate");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			
			if(hash.containsKey("BUYQTY"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Buy Qty");
				bean.setValue((String)hash.get("BUYQTY"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Buy Qty");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
				if(hash.containsKey("BUYRATE"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Buy Rate");
					bean.setValue((String)hash.get("BUYRATE"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Buy Rate");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("SELLQTY"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Sell Qty");
					bean.setValue((String)hash.get("SELLQTY"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Sell Qty");
					bean.setValue(" - ");
					holder.addElement(bean);
				}

				if(hash.containsKey("SALERATE"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Sell Rate");
					bean.setValue((String)hash.get("SALERATE"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Sell Rate");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
			if(hash.containsKey("SQOFFQTY"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Sq Off Qty");
					bean.setValue((String)hash.get("SQOFFQTY"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Sq Off Qty");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				SlideView s = new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT));
				s.setScrollListener(this);
				vfm.add(s);
		}
		else if(title.equalsIgnoreCase("F&O_Margin_Report") || title.equalsIgnoreCase("F&O_UL_Margin_Report"))
		{
			url = (String)hash.get("url");
			storedString = (String)hash.get("SHAREKHAN_SCRIP_CODE");
			if(hash.containsKey("SPANMARGIN"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Span Margin");
				bean.setValue((String)hash.get("SPANMARGIN"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Span Margin");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
				if(hash.containsKey("GEMARGIN"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("GE Margin");
					bean.setValue((String)hash.get("GEMARGIN"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("GE Margin");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("DIRECTMARGIN"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Direct Margin");
					bean.setValue((String)hash.get("DIRECTMARGIN"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Direct Margin");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("PREMIUM"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Premium");
					bean.setValue((String)hash.get("PREMIUM"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Premium");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("INVST_TYPE"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Investment Type");
					bean.setValue((String)hash.get("INVST_TYPE"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Investment Type");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				SlideView s = new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT));
				s.setScrollListener(this);
				vfm.add(s);
		}
		//F&O Order Details
		else if(title.equalsIgnoreCase("F&O_Order_Details") || title.equalsIgnoreCase("F&O_Order_Detail"))
		{
			hash = (Hashtable)confirmVector.elementAt(0);
			holder = new Vector();
			url = (String)hash.get("url");
			storedString = (String)hash.get("order_id");
			
				if(hash.containsKey("exchorderid"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exch Id");
					bean.setValue((String)hash.get("exchorderid"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exch Id");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("order_id"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Id");
					bean.setValue((String)hash.get("order_id"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Id");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(	ReportsOrderBKFNOView.screenString.equalsIgnoreCase("OrderBKF&O"))
				{
					LOG.print("----------1");
					if(hash.containsKey("scripcode"))
					{
						KeyValueBean bean = new KeyValueBean();
						String sm = (String)hash.get("scripcode");
						if(sm.length()<22)
						{
							bean.setKey("Scrip");
							bean.setValue(sm);
							LOG.print("----------2");
						}
						else
						{
							bean.setKey("@Scrip");
							bean.setValue(sm);
							LOG.print("----------3");
						}
						holder.addElement(bean);
					}
					else
					{
						KeyValueBean bean = new KeyValueBean();
						bean.setKey("Scrip");
						bean.setValue(" - ");
						holder.addElement(bean);
					}
				}
				
				else{
					LOG.print("----------4");
				if(hash.containsKey("contract"))
				{
					LOG.print("----------5");
					KeyValueBean bean = new KeyValueBean();
					String sm = (String)hash.get("contract");
					if(sm.length()<22)
					{
						bean.setKey("Scrip");
						bean.setValue(sm);
						LOG.print("----------6"+sm);
					}
					else
					{
						bean.setKey("@Scrip");
						String st = sm;
						if(sm.indexOf(" CE")>-1)
						{
							st = sm.substring(0, sm.indexOf(" CE"))+": "+sm.substring(sm.indexOf("CE"),sm.length());
						}
						else if(sm.indexOf(" PE")>-1)
						{
							st = sm.substring(0, sm.indexOf(" PE"))+": "+sm.substring(sm.indexOf("PE"),sm.length());
						}
						bean.setValue(st);
						LOG.print("----------7"+st);
					}
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Scrip");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				
				}
				if(hash.containsKey("orderdatetime"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order DateTime");
					bean.setValue((String)hash.get("orderdatetime"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order DateTime");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("orderstatus"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Status");
					bean.setValue((String)hash.get("orderstatus"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Status");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("requeststatus"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Request Status");
					bean.setValue((String)hash.get("requeststatus"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Request Status");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("disclosedqty"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Dis. Qty");
					bean.setValue((String)hash.get("disclosedqty"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Dis. Qty");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("orderprice"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Price");
					String t = (String)hash.get("orderprice");
					if(t.length()==0)
						t = "Mkt";
					else
					{
						double d = Double.parseDouble(t);
						if(d<=0)
							t = "Mkt";
					}
					bean.setValue(t);
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Order Price");
					bean.setValue("Mkt");
					holder.addElement(bean);
				}
				if(hash.containsKey("trigprice"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Trigger Price");
					bean.setValue(((String)hash.get("trigprice")).length()==0?"0 ":(String)hash.get("trigprice"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Trigger Price");
					bean.setValue("0 ");
					holder.addElement(bean);
				}
				if(hash.containsKey("execqty"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exec. Qty");
					bean.setValue((String)hash.get("execqty"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exec. Qty");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				if(hash.containsKey("execprice"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exec. Price");
					/*if((String)hash.get("execprice") == null)
						LOG.print("(String)hash.get(\"execprice\") null");
					else
						LOG.print("(String)hash.get(\"execprice\") NOT null "+((String)hash.get("execprice")).length());
					*/bean.setValue(((String)hash.get("execprice")).length()==0?"0.00 ":(String)hash.get("execprice"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Exec. Price");
					bean.setValue("0.00 ");
					holder.addElement(bean);
				}
				if(hash.containsKey("validity"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Validity");
					bean.setValue((String)hash.get("validity"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("Validity");
					bean.setValue(" - ");
					holder.addElement(bean);
				}	
				if(hash.containsKey("userid"))
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("User ID");
					bean.setValue((String)hash.get("userid"));
					holder.addElement(bean);
				}
				else
				{
					KeyValueBean bean = new KeyValueBean();
					bean.setKey("User ID");
					bean.setValue(" - ");
					holder.addElement(bean);
				}
				SlideView s = new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT));
				s.setScrollListener(this);
				vfm.add(s);
		}
		add(vfm);
		LOG.print("SlideViewScreen End");
		final SlideViewScreen slideViewScreen = this;
		if(refreshCall)
		{
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					UiApplication.getUiApplication().getActiveScreen().close();
					ScreenInvoker.pushScreen(slideViewScreen, true, true);
				}
			});
		}
		else
		{
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					ScreenInvoker.removeRemovableScreen();  
					ScreenInvoker.pushScreen(slideViewScreen, true, true);
				}
			});
		}
		/*final SlideViewScreen slideViewScreen = this;
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				ScreenInvoker.pushScreen(slideViewScreen, true, true);
			}
		});*/
		
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
	}

	protected boolean touchEvent(TouchEvent message) 
	{
		return super.touchEvent(message);
	}
		
	public LabelField getLabels(final String text1, final String text2, final String text3)
	{
		return new LabelField("",FOCUSABLE)
		{
			public void setFont(Font arg0) {
				super.setFont(FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT));
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
				return 6+(FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT).getHeight()*5);
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
		 switch(Command) {
	  		case ActionCommand.CMD_GRID_SCREEN:
	      	case ActionCommand.CMD_WATCHLIST_SCREEN:
	      	case ActionCommand.CMD_BSE_GL_SCREEN:
	      	case ActionCommand.CMD_NEWS_SCREEN:
	      	case ActionCommand.CMD_SEARCH_SCREEN:
	      	case ActionCommand.CMD_REPORTSB_SCREEN:
				 ActionInvoker.processCommand(new Action(Command,null));
	      		break;
	          default:
		 };
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
			
			synchronized( UiApplication.getEventLock() ){
            
            if(isDisplayed()) 
         	   {
            	close();
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
	public void setData(Vector vector, int id) {
		if(id == 11 || id == ID_ReportsMarginEF)
		{
			String title = (String) vector.elementAt(0);
			//errormsg = (String) vector.elementAt(1);
			Vector vec = (Vector) vector.elementAt(2);
			if(vec.size() == 0)
			{
				if(id == 11)
				UiApplication.getUiApplication().invokeAndWait(new Runnable()
		        {
		            public void run()
		            {
		    			ScreenInvoker.removeRemovableScreen();
		            } 
		        });
				if(id == 11 )
				UiApplication.getUiApplication().invokeLater(new Runnable()
		        {
		            public void run()
		            {
		    			Dialog.alert("Record not found!");
		            } 
		        });
				if(refreshme!=null)
				refreshme.setLoading(false);
			}
			else{
				ReportsOrderBean cmBean = (ReportsOrderBean)vec.elementAt(0);
				vec = new Vector();
				vec.addElement(cmBean.getStringHolder());
				if(id == ID_ReportsMarginEF)
					refreshMargin(cmBean.getStringHolder());
				else
					createUI(title, vec);
			}
		}
	}
	
//	public void setData(Vector vector, int id) {
//		if(id == ID_ReportsMarginEF || id == ID_Equity_Net_Position || id == ID_Equity_Unsetteled_Position /*|| id == ID_Equity_Trade_Position || id == ID_FNO_Trade_Position || id == ID_FNO_Turnover_Details || id == ID_FNO_Margin_ReportUL*/
//				|| id == ID_OrderDetails
//				|| id == ID_OrderDetails_FNO)
//		{
//			Hashtable confirmV = null ;
//			
//			String title = (String) vector.elementAt(0);
//			//errormsg = (String) vector.elementAt(1);
//			Vector vec = (Vector) vector.elementAt(2);
//			if(vec.size() == 0)
//			{
//				if(!refreshCall)
//				UiApplication.getUiApplication().invokeAndWait(new Runnable()
//		        {
//		            public void run()
//		            {
//		    			ScreenInvoker.removeRemovableScreen();
//		            } 
//		        });
//				if(id==ID_Equity_Net_Position || id==ID_ReportsMarginEF 
//						|| id == ID_OrderDetails
//						|| id == ID_OrderDetails_FNO);
//				else UiApplication.getUiApplication().invokeLater(new Runnable()
//		        {
//		            public void run()
//		            {
//		    			Dialog.alert("Record not found!");
//		            } 
//		        });
//				if(refreshCall) refreshme.setLoading(false);
//			}
//			else{
//				if(id==ID_Equity_Unsetteled_Position /*|| id == ID_Equity_Trade_Position || id == ID_FNO_Trade_Position || id == ID_FNO_Turnover_Details || id == ID_FNO_Margin_ReportUL*/
//						|| id == ID_OrderDetails
//						|| id == ID_OrderDetails_FNO){
//				for(int i = 0; i<vec.size();i++)
//				{
//					ReportsOrderBean bean = (ReportsOrderBean)vec.elementAt(i);
//					
//					LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("scripcode"));
//					LOG.print("bean.getStringHolder() "+bean.getStringHolder().get("SCRIPCODE"));
//					if(storedString.equals(bean.getStringHolder().get("order_id")))
//					{
//						confirmV = bean.getStringHolder();
//						break;
//					}
//					else if(storedString.equals(bean.getStringHolder().get("SETTLEMENTNO")))
//					{
//						confirmV = bean.getStringHolder();
//						break;
//					}
//					
//					else  if(storedString.equals(bean.getStringHolder().get("SHAREKHAN_SCRIP_CODE")))
//					{
//						confirmV = bean.getStringHolder();
//						break;
//					}
//					else if(storedString.equals(bean.getStringHolder().get("scripcode")))
//					{
//						confirmV = bean.getStringHolder();
//						break;
//					}
//					else if(storedString.equals(bean.getStringHolder().get("SCRIPCODE")))
//					{
//						confirmV = bean.getStringHolder();
//						break;
//					}
//				}
//				}
//				else if(id==ID_Equity_Net_Position || id==ID_ReportsMarginEF )
//				{
//					ReportsOrderBean bean = (ReportsOrderBean)vec.elementAt(0);
//					confirmV = bean.getStringHolder();
//				}
//				if(!refreshCall)
//					{
//						Vector v = new Vector();
//						v.addElement(confirmV);
//						createUI(title, v);
//					}
//				else
//					{
//						if(id==ID_ReportsMarginEF)
//							refreshMargin(confirmV);
//						else if(id==ID_Equity_Net_Position)
//							refreshNetPosition(confirmV);
//						else if(id==ID_Equity_Unsetteled_Position)
//							refreshUnsettledPosition(confirmV);
//						else if(id==ID_Equity_Trade_Position)
//							refreshEquityTradePosition(confirmV);
//						else if(id==ID_FNO_Trade_Position)
//							refreshFNOTradePosition(confirmV);
//						else if(id==ID_FNO_Turnover_Details)
//							refreshFNOTurnoverDetails(confirmV);
//						else if(id==ID_FNO_Margin_ReportUL)
//							refreshFNOMarginReportUL(confirmV);
//						else if(id == ID_OrderDetails)
//							refreshOrderDetails(confirmV);
//						else if(id == ID_OrderDetails_FNO)
//							refreshOrderDetailsFNO(confirmV);
//					}
//			}
//		}
//	}
	
	public void refreshMargin(Hashtable confirmVector)
	{
		hash = confirmVector;
		holder = new Vector();
		
		if(stringId.equalsIgnoreCase("ReportsMargin") || stringId.equalsIgnoreCase("ReportsMarginf") || stringId.equalsIgnoreCase("Account") )
		{
			if(hash.containsKey("CURRENTCASHBALANCE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Current cash balance");
				bean.setValue((String)hash.get("CURRENTCASHBALANCE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Current cash balance");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("PENDINGWITHDRAWALREQUEST"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Pending Withdrawal Requests");
				bean.setValue((String)hash.get("PENDINGWITHDRAWALREQUEST"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Pending Withdrawal Requests");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("NONCASHLIMIT"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Non Cash Limit");
				bean.setValue((String)hash.get("NONCASHLIMIT"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Non Cash Limit");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("LIMITAGAINSTSHARES"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) DP Margin");
				bean.setValue((String)hash.get("LIMITAGAINSTSHARES"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) DP Margin");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("CASHBPL"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Cash BPL");
				bean.setValue((String)hash.get("CASHBPL"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Cash BPL");
				bean.setValue("");
				holder.addElement(bean);
			}
			/*if(hash.containsKey("CASHMTM"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Cash MTM");
				bean.setValue((String)hash.get("CASHMTM"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Cash MTM");
				bean.setValue("");
				holder.addElement(bean);
			}*/
			if(hash.containsKey("CASHPREVIOUSSETTLEMENTEXPOSURE"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Cash Prev Settlement Exp.");
				bean.setValue((String)hash.get("CASHPREVIOUSSETTLEMENTEXPOSURE"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) Cash Prev Settlement Exp.");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("INTRADAYMARGINCASH"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Intraday Margin Cash");
				bean.setValue((String)hash.get("INTRADAYMARGINCASH"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Intraday Margin Cash");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("FNOMTM"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("FNO MTM");
				bean.setValue((String)hash.get("FNOMTM"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("FNO MTM");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("FNOPREMIUM"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) FNO Premium");
				bean.setValue((String)hash.get("FNOPREMIUM"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) FNO Premium");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("FNOBPL"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) FNO BPL");
				bean.setValue((String)hash.get("FNOBPL"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(+) FNO BPL");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("INTRADAYMARGINFNO"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Intraday Margin FNO");
				bean.setValue((String)hash.get("INTRADAYMARGINFNO"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("(-) Intraday Margin FNO");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("HOLD_FUNDS"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Hold Funds");
				bean.setValue((String)hash.get("HOLD_FUNDS"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Hold Funds");
				bean.setValue(" - ");
				holder.addElement(bean);
			}
			if(hash.containsKey("TOTAL"))
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Total Amount");
				bean.setValue((String)hash.get("TOTAL"));
				holder.addElement(bean);
			}
			else
			{
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("Total Amount");
				bean.setValue("");
				holder.addElement(bean);
			}
			UiApplication.getUiApplication().invokeLater(new Runnable()
	        {
	            public void run()
	            {
	            	vfm.deleteAll();
	            	vfm.add(new SlideViewMargin(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT),SlideViewMargin.ALIGN_RIGHT_WITH_LAST_BOLD));
	    		} 
	        });
			
			refreshme.setLoading(false);
		}
	}
	
	public void refreshNetPosition(Hashtable confirmVector)
	{
		hash = confirmVector;
		holder = new Vector();
		if(hash.containsKey("SCRIPCODE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Scrip");
			bean.setValue((String)hash.get("SCRIPCODE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Scrip");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("EXCHANGE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Exchange");
			bean.setValue((String)hash.get("EXCHANGE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Exchange");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("SEGMENT"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Segment");
			bean.setValue((String)hash.get("SEGMENT"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Segment");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("NETQTY"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Qty");
			bean.setValue((String)hash.get("NETQTY"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Qty");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("PRODUCTTYPE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Product Type");
			bean.setValue((String)hash.get("PRODUCTTYPE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Product Type");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("BUYQTY"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Buy Qty");
			bean.setValue((String)hash.get("BUYQTY"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Buy Qty");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("BUYRATE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Buy Rate");
			bean.setValue((String)hash.get("BUYRATE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Buy Rate");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("SELLQTY"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Sell Qty");
			bean.setValue((String)hash.get("SELLQTY"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Sell Qty");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("SELLRATE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Sell Rate");
			bean.setValue((String)hash.get("SELLRATE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Sell Rate");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("BPL"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Booked PL");
			bean.setValue((String)hash.get("BPL"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Booked PL");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("MTM"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("MTM");
			bean.setValue((String)hash.get("MTM"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("MTM");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		UiApplication.getUiApplication().invokeLater(new Runnable()
        {
            public void run()
            {
            	vfm.deleteAll();
            	vfm.add(new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT)));
        	} 
        });
		
		refreshme.setLoading(false);
	}
public void refreshUnsettledPosition(Hashtable confirmVector)
{
	hash = confirmVector;
	holder = new Vector();
	if(hash.containsKey("SCRIPCODE"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Scrip");
		bean.setValue((String)hash.get("SCRIPCODE"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Scrip");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
		if(hash.containsKey("EXCHANGE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Exchange");
			bean.setValue((String)hash.get("EXCHANGE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Exchange");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("SETTLEMENTNO"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Settlement No");
			bean.setValue((String)hash.get("SETTLEMENTNO"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Settlement No");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("SETTLEMENTDATE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Settlement Date");
			bean.setValue((String)hash.get("SETTLEMENTDATE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Settlement Date");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("TOTALBUYQTY"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Total Buy Qty");
			bean.setValue((String)hash.get("TOTALBUYQTY"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Total Buy Qty");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("TOTALBUYVALUE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Total Buy Value");
			bean.setValue((String)hash.get("TOTALBUYVALUE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Total Buy Value");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("TOTALSELLQTY"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Total Sell Qty");
			bean.setValue((String)hash.get("TOTALSELLQTY"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Total Sell Qty");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("TOTALSELLVALUE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Total Sell Value");
			bean.setValue((String)hash.get("TOTALSELLVALUE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Total Sell Value");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("BPL"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("BPL");
			bean.setValue((String)hash.get("BPL"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("BPL");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("NETPOSQTY"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Net Pos Qty");
			bean.setValue((String)hash.get("NETPOSQTY"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Net Pos Qty");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("NETVALUE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Net Value");
			bean.setValue((String)hash.get("NETVALUE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Net Value");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("MARGIN"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Margin");
			bean.setValue((String)hash.get("MARGIN"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Margin");
			bean.setValue(" - ");
			holder.addElement(bean);
		}	
		if(hash.containsKey("LIMITONPOS"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Limit On Pos");
			bean.setValue((String)hash.get("LIMITONPOS"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Limit On Pos");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("NETLIMIT"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Net Limit");
			bean.setValue((String)hash.get("NETLIMIT"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Net Limit");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		UiApplication.getUiApplication().invokeLater(new Runnable()
        {
            public void run()
            {
            	vfm.deleteAll();
        		vfm.add(new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT)));
        	} 
        });
		refreshme.setLoading(false);
		LOG.print("data updated");
	}


public void refreshEquityTradePosition(Hashtable confirmVector)
{
	hash = confirmVector;
	holder = new Vector();	
	if(hash.containsKey("TRADE_ID"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade ID");
		bean.setValue((String)hash.get("TRADE_ID"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade ID");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("ORDER_ID"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order ID");
		bean.setValue((String)hash.get("ORDER_ID"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order ID");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("EXCH_ORDER_ID"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exch. Order ID");
		bean.setValue((String)hash.get("EXCH_ORDER_ID"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exch. Order ID");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("SHAREKHAN_SCRIP_CODE"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Scrip ");
		bean.setValue((String)hash.get("SHAREKHAN_SCRIP_CODE"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Scrip");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("BUY_SELL"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Buy/Sell");
		String text = (String)hash.get("BUY_SELL");
		String[] actionChoiceText = {"Buy","Sell","Sell against Margin","Buy Max","Sell Max"};
    	String[] actionChoiceTextShort = {"B","S","SS","BM","SM"};
    	for(int i=0;i<actionChoiceTextShort.length;i++)
		{
			if(text.equalsIgnoreCase(actionChoiceTextShort[i]))
			{
				text = actionChoiceText[i];
				break;
			}
		}
		bean.setValue(text);
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Buy/Sell");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("TRADE_QTY"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Qty");
		bean.setValue((String)hash.get("TRADE_QTY"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Qty");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("TRADE_PRICE"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Price");
		String text = (String)hash.get("TRADE_PRICE");
		double num = Double.parseDouble(text) / 100;
		text = Utils.DecimalRoundString(num, 2);
		bean.setValue(text);
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Price");
		bean.setValue(" 0.00 ");
		holder.addElement(bean);
	}
	if(hash.containsKey("TRADE_AMOUNT"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade Val");
		String text = (String)hash.get("TRADE_AMOUNT");
		double num = Double.parseDouble(text) / 100;
		text = Utils.DecimalRoundString(num, 2);
		bean.setValue(text);
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade Val");
		bean.setValue(" 0.00 ");
		holder.addElement(bean);
	}
	if(hash.containsKey("TRADE_DATE_TIME"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade Time");
		bean.setValue((String)hash.get("TRADE_DATE_TIME"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade Time");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	UiApplication.getUiApplication().invokeLater(new Runnable()
    {
        public void run()
        {
        	vfm.deleteAll();
    		vfm.add(new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT)));
    	} 
    });
	refreshme.setLoading(false);
	LOG.print("data updated");
}

public void refreshFNOTradePosition(Hashtable confirmVector)
{
	hash = confirmVector;
	holder = new Vector();
	if(hash.containsKey("TRADE_ID"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade ID");
		bean.setValue((String)hash.get("TRADE_ID"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade ID");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("ORDER_ID"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order ID");
		bean.setValue((String)hash.get("ORDER_ID"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order ID");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("EXCH_ORDER_ID"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exch. Order ID");
		bean.setValue((String)hash.get("EXCH_ORDER_ID"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exch. Order ID");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("SHAREKHAN_SCRIP_CODE"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Scrip ");
		bean.setValue((String)hash.get("SHAREKHAN_SCRIP_CODE"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Scrip");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("BUY_SELL"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Buy/Sell");
		String text = (String)hash.get("BUY_SELL");
		String[] actionChoiceText = {"Buy","Sell","Sell against Margin","Buy Max","Sell Max"};
    	String[] actionChoiceTextShort = {"B","S","SS","BM","SM"};
    	for(int i=0;i<actionChoiceTextShort.length;i++)
		{
			if(text.equalsIgnoreCase(actionChoiceTextShort[i]))
			{
				text = actionChoiceText[i];
				break;
			}
		}
		bean.setValue(text);
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Buy/Sell");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("TRADE_QTY"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Qty");
		bean.setValue((String)hash.get("TRADE_QTY"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Qty");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("TRADE_PRICE"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Price");
		String text = (String)hash.get("TRADE_PRICE");
		double num = Double.parseDouble(text) / 100;
		text = Utils.DecimalRoundString(num, 2);
		bean.setValue(text);
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Price");
		bean.setValue(" 0.00 ");
		holder.addElement(bean);
	}
	if(hash.containsKey("TRADE_AMOUNT"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade Val");
		String text = (String)hash.get("TRADE_AMOUNT");
		double num = Double.parseDouble(text) / 100;
		text = Utils.DecimalRoundString(num, 2);
		bean.setValue(text);
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade Val");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("TRADE_DATE_TIME"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade Time");
		bean.setValue((String)hash.get("TRADE_DATE_TIME"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trade Time");
		bean.setValue(" - ");
		holder.addElement(bean);
	}vfm.add(new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT)));
UiApplication.getUiApplication().invokeLater(new Runnable()
{
    public void run()
    {
    	vfm.deleteAll();
    	vfm.add(new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT)));
	} 
});
refreshme.setLoading(false);
LOG.print("data updated");
}

public void refreshFNOTurnoverDetails(Hashtable confirmVector)
{
	hash = confirmVector;
	holder = new Vector();
	if(hash.containsKey("BUYQTY"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Buy Qty");
		bean.setValue((String)hash.get("BUYQTY"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Buy Qty");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
		if(hash.containsKey("BUYRATE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Buy Rate");
			bean.setValue((String)hash.get("BUYRATE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Buy Rate");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("SELLQTY"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Sell Qty");
			bean.setValue((String)hash.get("SELLQTY"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Sell Qty");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
	if(hash.containsKey("BPL"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("BPL");
			bean.setValue((String)hash.get("BPL"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("BPL");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("INVST_TYPE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("INVST TYPE");
			bean.setValue((String)hash.get("INVST_TYPE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("INVST TYPE");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		
	UiApplication.getUiApplication().invokeLater(new Runnable()
{
    public void run()
    {
    	vfm.deleteAll();
    	vfm.add(new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT)));
	} 
});
refreshme.setLoading(false);
LOG.print("data updated");
}

public void refreshFNOMarginReportUL(Hashtable confirmVector)
{
	hash = confirmVector;
	holder = new Vector();
	if(hash.containsKey("SPANMARGIN"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Span Margin");
		bean.setValue((String)hash.get("SPANMARGIN"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Span Margin");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
		if(hash.containsKey("GEMARGIN"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("GE Margin");
			bean.setValue((String)hash.get("GEMARGIN"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("GE Margin");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("DIRECTMARGIN"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Direct Margin");
			bean.setValue((String)hash.get("DIRECTMARGIN"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Direct Margin");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("PREMIUM"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Premium");
			bean.setValue((String)hash.get("PREMIUM"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Premium");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
		if(hash.containsKey("INVST_TYPE"))
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Investment Type");
			bean.setValue((String)hash.get("INVST_TYPE"));
			holder.addElement(bean);
		}
		else
		{
			KeyValueBean bean = new KeyValueBean();
			bean.setKey("Investment Type");
			bean.setValue(" - ");
			holder.addElement(bean);
		}
UiApplication.getUiApplication().invokeLater(new Runnable()
{
public void run()
{
	vfm.deleteAll();
	vfm.add(new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT)));
} 
});
refreshme.setLoading(false);
LOG.print("data updated");
}

public void refreshOrderDetails(Hashtable confirmVector)
{
	hash = confirmVector;
	holder = new Vector();
	if(hash.containsKey("exchorderid"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exch Id : ");
		bean.setValue((String)hash.get("exchorderid"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exch Id");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("order_id"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Id");
		bean.setValue((String)hash.get("order_id"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Id");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("scripcode"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Scrip");
		bean.setValue((String)hash.get("scripcode"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Scrip");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("segment"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Segment");
		bean.setValue((String)hash.get("segment"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Segment");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("orderdatetime"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order DateTime");
		bean.setValue((String)hash.get("orderdatetime"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order DateTime");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("orderstatus"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Status");
		bean.setValue((String)hash.get("orderstatus"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Status");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("requeststatus"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Request Status");
		bean.setValue((String)hash.get("requeststatus"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Request Status");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("disclosedqty"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Dis. Qty");
		bean.setValue((String)hash.get("disclosedqty"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Dis. Qty");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("orderprice"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Price");
		String t = (String)hash.get("orderprice");
		if(t.length()==0)
			t = "Mkt";
		else
		{
			double d = Double.parseDouble(t);
			if(d<0)
				t = "Mkt";
		}
		bean.setValue(t);
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Price");
		bean.setValue("Mkt");
		holder.addElement(bean);
	}
	if(hash.containsKey("trigprice"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trigger Price");
		bean.setValue((String)hash.get("trigprice"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trigger Price");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("execqty"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Qty");
		bean.setValue((String)hash.get("execqty"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Qty");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("execprice"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Price");
		bean.setValue((String)hash.get("execprice"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Price");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("exchdatetime"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exch DateTime");
		bean.setValue((String)hash.get("exchdatetime"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exch DateTime");
		bean.setValue(" - ");
		holder.addElement(bean);
	}	
	if(hash.containsKey("rmscode"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("RMS Code");
		bean.setValue((String)hash.get("rmscode"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("RMS Code");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
UiApplication.getUiApplication().invokeLater(new Runnable()
{
public void run()
{
	vfm.deleteAll();
	vfm.add(new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT)));
} 
});
refreshme.setLoading(false);
LOG.print("data updated");
}

public void refreshOrderDetailsFNO(Hashtable confirmVector)
{
	hash = confirmVector;
	holder = new Vector();
	if(hash.containsKey("exchorderid"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exch Id");
		bean.setValue((String)hash.get("exchorderid"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exch Id");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("order_id"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Id");
		bean.setValue((String)hash.get("order_id"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Id");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("scripcode"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Scrip");
		bean.setValue((String)hash.get("scripcode"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Scrip");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("orderdatetime"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order DateTime");
		bean.setValue((String)hash.get("orderdatetime"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order DateTime");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("orderstatus"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Status");
		bean.setValue((String)hash.get("orderstatus"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Status");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("requeststatus"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Request Status");
		bean.setValue((String)hash.get("requeststatus"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Request Status");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("disclosedqty"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Dis. Qty");
		bean.setValue((String)hash.get("disclosedqty"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Dis. Qty");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("orderprice"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Price");
		String t = (String)hash.get("orderprice");
		if(t.length()==0)
			t = "Mkt";
		else
		{
			double d = Double.parseDouble(t);
			if(d<0)
				t = "Mkt";
		}
		bean.setValue(t);
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Order Price");
		bean.setValue("Mkt");
		holder.addElement(bean);
	}
	if(hash.containsKey("trigprice"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trigger Price");
		bean.setValue(((String)hash.get("trigprice")).length()==0?"0 ":(String)hash.get("trigprice"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Trigger Price");
		bean.setValue("0 ");
		holder.addElement(bean);
	}
	if(hash.containsKey("execqty"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Qty");
		bean.setValue((String)hash.get("execqty"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Qty");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
	if(hash.containsKey("execprice"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Price");
		/*if((String)hash.get("execprice") == null)
			LOG.print("(String)hash.get(\"execprice\") null");
		else
			LOG.print("(String)hash.get(\"execprice\") NOT null "+((String)hash.get("execprice")).length());
		*/bean.setValue(((String)hash.get("execprice")).length()==0?"0.00 ":(String)hash.get("execprice"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Exec. Price");
		bean.setValue("0.00 ");
		holder.addElement(bean);
	}
	if(hash.containsKey("validity"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Validity");
		bean.setValue((String)hash.get("validity"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("Validity");
		bean.setValue(" - ");
		holder.addElement(bean);
	}	
	if(hash.containsKey("userid"))
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("User ID");
		bean.setValue((String)hash.get("userid"));
		holder.addElement(bean);
	}
	else
	{
		KeyValueBean bean = new KeyValueBean();
		bean.setKey("User ID");
		bean.setValue(" - ");
		holder.addElement(bean);
	}
UiApplication.getUiApplication().invokeLater(new Runnable()
{
public void run()
{
	vfm.deleteAll();
	vfm.add(new SlideView(holder,FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT)));
} 
});
refreshme.setLoading(false);
LOG.print("data updated");
}
public void scrollChanged(Manager mgr, int newHorizScroll, int newVertScroll) {
    if (newHorizScroll < 0) {
        setHorizontalScroll(0);
    }
   
    // and so on
}

}
