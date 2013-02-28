package com.snapwork.view.trade;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.Expiry;
import com.snapwork.beans.FNOTradeBean;
import com.snapwork.beans.HomeJson;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.parsers.TradeNowMainParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FNOTradeConfiguration;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class ReportsOrderBKFNOView extends VerticalFieldManager implements ReturnString, ReturnDataWithId {

	private static byte padding = 4; 
	private final Bitmap bmp = ImageManager.getSmallTradeButton();
	private ActionListener actionListener;
	final ReturnDataWithId returnDataWithId = this;
	final ReturnString returnString = this;
	private HomeJson bannerData;
	private Hashtable hashData;
	public static String screenString;
	private String url;
	private long timer;
	private int MODIFY_ID = 796;
	private FNOTradeConfiguration fnoTradeConfig;
	private FNOTradeBean fnoTradeBean;
	private Hashtable hashT;

	public ReportsOrderBKFNOView(String url, String screenString, Hashtable dataHolder,ActionListener actionListener) {
		this.url = url;
		timer = System.currentTimeMillis();
		ReportsOrderGroup tlv = null;
		this.screenString = screenString;
		this.actionListener = actionListener;
		dataHolder.put("url",url);
		this.hashData = dataHolder;
		tlv = new ReportsOrderGroup();
		tlv.addItem(dataHolder);
		add(tlv);
	}

	public int getPreferredHeight() {
		try {
			return AppConstants.screenHeight;
		} catch(Exception ex) {
		}
		return 0;
	}

	private class ReportsOrderGroup extends VerticalFieldManager {

		public ReportsOrderGroup() {
			super(FOCUSABLE);
		}

		private void addItem(final Hashtable dataHolder) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					add(new ReportsOrderGroupItem(FOCUSABLE, dataHolder));
				}
			});
		}


	}

	private class ReportsOrderGroupItem extends Manager {

		private boolean requireNullField = true;
		public ReportsOrderGroupItem(long style,Hashtable dataHolder) {
			super(style);
			String text = "";
			if((String) dataHolder.get("order_id") == null)
				text = " - ";
			else
				text = (String) dataHolder.get("order_id") ;

			add(getLabel("#"+text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x6c8889, 0));
			if((String) dataHolder.get("orderstatus") == null)
				text = "-";
			else
				text = (String) dataHolder.get("orderstatus") ;

			Vector v = new Vector();
			text = text.replace(' ', '_');
			String text1 = text.toLowerCase();
			text1 = text1.replace(' ', '_');
			LOG.print("0-====-=-=-=--=-=-\n"+text1);
			if(text1.equalsIgnoreCase("ors_placed") || text1.equalsIgnoreCase("InProcess"))
				text1 = "InProcess";
			else if(text1.equalsIgnoreCase("exchange_acknowledged") || text1.equalsIgnoreCase("Pending"))
				text1 = "Pending";
			else if(text1.equalsIgnoreCase("exchange_rejected") || text1.equalsIgnoreCase("rejected"))
				text1 = "Rejected";
			else if(text1.equalsIgnoreCase("partly_executed") || text1.equalsIgnoreCase("PartlyExecuted"))
				{
					//PARTLY_EXEC_FLAG = true;
					text1 = "PartlyExecuted";
				}
			else if(text1.equalsIgnoreCase("executed") || text1.equalsIgnoreCase("FullyExecuted"))
				text1 = "FullyExecuted";
			text = text1;
			if (text.equalsIgnoreCase("-")) {
				text = " - ";
				add(getSmallBitmapFieldBlank("null", 0x6c8889));
				add(getSmallBitmapFieldBlank("null", 0x6c8889));
				add(getSmallBitmapFieldBlank("null", 0x6c8889));
			}
			//orderstatus = 'Pending' or orderstatus = 'PartlyExecuted' or orderstatus = 'Triggered' or orderstatus = 'AfterHour NSE' or orderstatus = 'Exchange Acknowledged'
			else if(text.equalsIgnoreCase("Pending") || text.equalsIgnoreCase("PartlyExecuted")
				||	text.equalsIgnoreCase("Triggered") || text.equalsIgnoreCase("AfterHour")
				||	text.equalsIgnoreCase("Exchange_Acknowledged"))
			{
				add(getSmallBitmapField("Details", dataHolder));
				add(getSmallBitmapField("Modify", dataHolder));
				add(getSmallBitmapField("Cancel", v));
			} 
			/*else if(text.equalsIgnoreCase("PartlyExecuted"))
			{
				add(getSmallBitmapFieldBlank("null", 0x6c8889));
				add(getSmallBitmapField("Details", dataHolder));
				add(getSmallBitmapField("Cancel", v));
			}*/
			else {
				add(getSmallBitmapFieldBlank("null", 0x6c8889));
				add(getSmallBitmapFieldBlank("null", 0x6c8889));
				add(getSmallBitmapField("Details", dataHolder));
			}
			LOG.print(" ssssss screenString "+screenString);
			if(screenString.equalsIgnoreCase("OrderBKF&O"))
			{
				if((String) dataHolder.get("scripcode") == null)
					text = " - ";
				else
					text = (String) dataHolder.get("scripcode") ;
			}
			else{
			if((String) dataHolder.get("contract") == null)
				text = " - ";
			else
				text = (String) dataHolder.get("contract") ;
			}
			add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 0)); 
			 if((String) dataHolder.get("exchange") == null)
                        	text = " - ";
                        else
                        	text = (String) dataHolder.get("exchange") ;
			// Place here NSECURR
			add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 1)); 
			if((String) dataHolder.get("orderqty") == null)
				text = " - ";
			else
				text = (String) dataHolder.get("orderqty") ;
			add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 0)); 
			if((String) dataHolder.get("ordertype") == null)
				text = " - ";
			else
				text = (String) dataHolder.get("ordertype") ;
			add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 1)); 
			add(getLabel("Status", FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x333333, 0));
			int colorChange = 0x66cc00;
			/*if((String) dataHolder.get("orderstatus") == null)
				text = " - ";
			else
				text = (String) dataHolder.get("orderstatus") ;
			String text1 = text;*/
			/*text1 = text1.replace(' ', '_');
			if(text1.equalsIgnoreCase("ors_placed"))
				text1 = "InProcess";
			else if(text1.equalsIgnoreCase("exchange_acknowledged"))
				text1 = "Pending";
			else if(text1.equalsIgnoreCase("exchange_rejected"))
				text1 = "Rejected";
			else if(text1.equalsIgnoreCase("partly_executed"))
				text1 = "Partly Executed";
			else if(text1.equalsIgnoreCase("executed"))
				text1 = "Fully Executed";
			text = text1;*/
			text1 = text1.replace(' ', '_');
			if(text1.equalsIgnoreCase("InProcess"))
				colorChange = 0xcc3333;
			else if(text1.equalsIgnoreCase("Pending"))
				colorChange = 0xcc9933;
			else if(text1.equalsIgnoreCase("Triggered"))
				colorChange = 0x66cc00;
			else if(text1.equalsIgnoreCase("Partially_Executed"))
				colorChange = 0x9FB7FF;
			else if(text1.equalsIgnoreCase("Fully_Executed"))
				colorChange = 0x66cc00;
			else if(text1.equalsIgnoreCase("Rejected"))
				colorChange = 0xcc3333;

			add(getLabel(text1, FontLoader.getFont(AppConstants.REPORTS_FONT), colorChange, 0x333333, 1));
			requireNullField = true;
			for(int i=0;i<this.getFieldCount();i++) {
				if(this.getField(i).isFocusable()) {
					requireNullField = false;
					i=this.getFieldCount();
				}
			}

			if(requireNullField) {
				NullField objNullField = new NullField(FOCUSABLE) {

					protected void onFocus(int direction) {
						if(direction==-1) {
							this.setPosition(0, 0);
						}
						else {
							this.setPosition(this.getManager().getWidth(), this.getManager().getHeight());
						}
						super.onFocus(direction);
					}

					protected void onUnfocus() {
						super.onUnfocus();
					}
				};
				add(objNullField);//A Field which will show focus on manager
			}

		}

		public int getTotalFields() {
			if(requireNullField)
				return this.getFieldCount()-1;
			else
				return this.getFieldCount(); 
		}

		public int getPreferredHeight() {
			return (getFont().getHeight()+ 4) * 4;
		}

		public int getPreferredWidth() {
			return AppConstants.screenWidth;
		}

		protected void onFocus(int direction) {
			super.onFocus(direction);
			invalidate();
		}

		protected void onUnfocus() {
			super.onUnfocus();
			invalidate();
		}

		protected boolean navigationClick(int status, int time) {
			return super.navigationClick(status, time);
		}

		protected void paintBackground(Graphics graphics) {
			graphics.setColor(0x6c8889);//0x96abab);
			graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
			graphics.setColor(0x000000);
			graphics.fillRect(1, (getPreferredHeight()/4)+2, getPreferredWidth()-2, (getPreferredHeight()/4)*2);
			graphics.setColor(0x333333);//0x5d5d5d); // origional grey 0x333333
			graphics.fillRect(1, ((getPreferredHeight()/4)*3)+2, getPreferredWidth()-2, getPreferredHeight()/4);
		}

		protected void sublayout(int width, int height) {
			layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
			layoutChild(getField(2), getField(2).getPreferredWidth(), getField(2).getPreferredHeight());
			layoutChild(getField(3), getField(3).getPreferredWidth(), getField(3).getPreferredHeight());
			layoutChild(getField(4), getField(4).getPreferredWidth(), getField(4).getPreferredHeight());
			layoutChild(getField(5), getField(5).getPreferredWidth(), getField(5).getPreferredHeight());
			layoutChild(getField(6), getField(6).getPreferredWidth(), getField(6).getPreferredHeight());
			layoutChild(getField(7), getField(7).getPreferredWidth(), getField(7).getPreferredHeight());
			layoutChild(getField(8), getField(8).getPreferredWidth(), getField(8).getPreferredHeight());
			layoutChild(getField(9), getField(9).getPreferredWidth(), getField(9).getPreferredHeight());

			setPositionChild(getField(0), padding, 2 );
            setPositionChild(getField(1), getPreferredWidth()-(((getField(1).getPreferredWidth()+2)+(getField(2).getPreferredWidth()+2)+(getField(3).getPreferredWidth()+2))), ((getPreferredHeight()/4)/2) - (getField(1).getPreferredHeight()/2));
            setPositionChild(getField(2), getPreferredWidth()-((getField(2).getPreferredWidth()+2)+(getField(3).getPreferredWidth()+2)),  ((getPreferredHeight()/4)/2) - (getField(2).getPreferredHeight()/2));
            setPositionChild(getField(3), getPreferredWidth()-((getField(3).getPreferredWidth()+2)),  ((getPreferredHeight()/4)/2) - (getField(3).getPreferredHeight()/2) );
            setPositionChild(getField(4), padding, (getPreferredHeight()/4)+2);
            setPositionChild(getField(5), getPreferredWidth()-(getField(5).getPreferredWidth()+2), (getPreferredHeight()/4)+2);
            setPositionChild(getField(6), padding, (getPreferredHeight()/2)+2);
            setPositionChild(getField(7), getPreferredWidth()-(getField(7).getPreferredWidth()+2), (getPreferredHeight()/2)+2);
            setPositionChild(getField(8), padding, (getPreferredHeight()-(getPreferredHeight()/4)+2));
            setPositionChild(getField(9), getPreferredWidth()-(getField(9).getPreferredWidth()+2), (getPreferredHeight()-(getPreferredHeight()/4)+2));

			setExtent(getPreferredWidth(), getPreferredHeight());

		}
		public LabelField getLabel(final String text, final Font fnt, final int foreColor, final int bgColor, final int id)
		{
			return new LabelField(text, NON_FOCUSABLE) {

				public int getPreferredHeight() {
					return getFont().getHeight()+4;
				}
				public int getPreferredWidth() {
					return getFont().getAdvance(text)+4;
				}


				protected void paint(Graphics graphics) {
					graphics.setColor(foreColor);
					graphics.setFont(FontLoader.getFont(AppConstants.REPORTS_FONT));
					if(id == 1)
                    	graphics.drawText(text, getPreferredWidth() - graphics.getFont().getAdvance(text) - 4, 2);
                    else
                    	graphics.drawText(text, 2, 2);
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
					graphics.setColor(bgColor);
					graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
				}
			};
		}

		public BitmapField getSmallBitmapField(final String caption, final Object object)//, final String title, final String url, final Action action)
		{
			final String cp = caption.replace(' ', '_');
			int wd = AppConstants.screenHeight<480?8:16;
			final Bitmap bitmap = ImageManager.resizeBitmap(bmp, FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT).getAdvance(cp)+wd, bmp.getHeight());
			return new BitmapField(bitmap, FOCUSABLE | DrawStyle.HCENTER)
			{
				protected boolean navigationClick(int status,int time) {
					if(!Utils.sessionAlive)
					{
						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
					}
					else
					{ handleEvent();
					}return super.navigationClick(status, time);
				}
				protected boolean touchEvent(TouchEvent message) {
					if(message.getEvent() == TouchEvent.CLICK) {
						if(!Utils.sessionAlive)
						{
							ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
						}
						else
						{ handleEvent();
						}
						} return super.touchEvent(message);
				}
				private void handleEvent()
				{
					
					if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
					if(!Utils.sessionAlive)
				{
					ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
				}
				else
				{
					if(caption.equalsIgnoreCase("Details"))
				{
						ReportsOrderView.Load = true;
					Hashtable h = (Hashtable)object;
					if(h.size() == 0)
					{
						Dialog.alert("You have 0 Orders.");
					}
					else
					{
						Vector vec = new Vector();
						vec.addElement("F&O Order Details");
						Vector e = new Vector();
						e.addElement(object);
						vec.addElement(e);
						//actionListener.actionPerfomed(ActionCommand.CMD_SLIDE_VIEW, vec);
						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SLIDE_VIEW,vec));
					}
				}
				else if(caption.equalsIgnoreCase("Modify"))
				{
					Hashtable h = (Hashtable)object;
					LOG.print("(String)h.get(\"scripcode\") : "+(String)h.get("scripcode"));
					ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
					String infourl = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoo02&custId="+UserInfo.getUserID()+"&orderId="+(String)hashData.get("order_id")+"&exchange="+(String)hashData.get("exchange")+"&btnModify=&btnDel=&page=reports&userAgent=&rmsCode="+(String)hashData.get("userid");
					 LOG.print("URL :-> "+infourl);
					new TradeNowMainParser(infourl, returnDataWithId, MODIFY_ID);
					
					//
				}
				else if(caption.equalsIgnoreCase("Cancel"))
				{
					 ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
                	String url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoo04&custId="+UserInfo.getUserID()+"&orderId="+(String)hashData.get("order_id")+"&exchange="+(String)hashData.get("exchange")+"&btnModify=&btnDel=&page=reports&userAgent=bb&rmsCode="+(String)hashData.get("userid");
                	 /*if(screenString.equalsIgnoreCase("ReportsOrderf"))
                		 url = AppConstants.domainUrl + "orderTransaction.php?custId="+UserInfo.getUserID()+"&orderId="+(String)hashData.get("order_id")+"&exchange="+(String)hashData.get("exchange")+"&btnModify=&btnDel=Del&page=reports&rmsCode="+(String)hashData.get("userid")+"&debug=2";
                	 
                	else if(screenString.equalsIgnoreCase("OrderBKF&O"))
                		 url = AppConstants.domainUrl + "orderTransaction.php?custId="+UserInfo.getUserID()+"&orderId="+(String)hashData.get("order_id")+"&exchange="+(String)hashData.get("exchange")+"&btnModify=&btnDel=Del&page=orderBK&rmsCode="+(String)hashData.get("userid")+"&debug=2";
                	 */
                	 LOG.print("URL :-> "+url);
                	 new TradeNowMainParser(url,returnDataWithId,2);                                         	
				}
				}}
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
					graphics.drawBitmap(0,0, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);
					 graphics.setFont(FontLoader.getFont(AppConstants.REPORTS_FONT));
                     graphics.drawText(caption,(bitmap.getWidth()/2)-(graphics.getFont().getAdvance(caption)/2),(bitmap.getHeight()/2)-(getFont().getHeight()/2));

				}
				protected void drawFocus(Graphics graphics, boolean on) 
				{

				}
				public Font getFont() { return FontLoader.getFont(AppConstants.REPORTS_FONT);}
				public int getPreferredHeight() {return  bitmap.getHeight();}//(getFont().getHeight()*2)-(getFont().getHeight()/2);}
			public int getPreferredWidth() {
				return bitmap.getWidth();
			}
			protected void layout(int arg0, int arg1) {
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
			};
		}

		public LabelField getSmallBitmapFieldBlank(final String caption, final int color)//, final String title, final String url, final Action action)
		{
			return new LabelField("", NON_FOCUSABLE | DrawStyle.HCENTER)
			{

				protected boolean navigationClick(int status,int time) {
					return super.navigationClick(status, time);
				}
				protected boolean touchEvent(TouchEvent message) {
					return super.touchEvent(message);
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
					if(getPreferredHeight()==2)
					{
						graphics.setColor(color);
						graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
						return;
					}
					graphics.setBackgroundColor(Color.BLACK);
					graphics.clear();
				}
				protected void paint(Graphics graphics) 
				{
					graphics.setColor(color);
					graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
				}
				protected void drawFocus(Graphics graphics, boolean on) 
				{

				}
				public Font getFont() { return FontLoader.getFont(AppConstants.REPORTS_FONT);}
				public int getPreferredHeight() {return  bmp.getHeight();}//(getFont().getHeight()*2)-(getFont().getHeight()/2);}
			public int getPreferredWidth() {
				return 1;
			}
			protected void layout(int arg0, int arg1) {
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
			};
		}


	}

	public void setData(final Vector vector, int id) {
		if(id == 111)
		{
			//Replace following class with TradeNowFNOMainScreen
			//TradeNowMainScreen trade = new TradeNowMainScreen(bannerData);
			//trade.modifyScreenForReport(screenString, vector, id,);
			//Following Url use for Modify Action
			// http://50.17.252.160/SK_live/modifyOrder.php?scripcode=MINIFTY&exchange=NSEFO&ltp=0.00&per_change=0.00&change=0.00&custId=250037&orderId=58294333&action=B&instType=FUTIDX&optType=&strikePrice=-0.01&page=orderBK&expiry=29-03-2012&userAgent=bb&order_exe_qty=0&order_exe_price=&btnConfirm=Confirm&mkt-lots=20&rmsCode=&qty=20&disc-qty=0&limitPrice=4500.00&stopPrice=
		}
		else if(id == 2)
		{
			Vector vec = new Vector();
            vec.addElement("Confirm Cancellation !");
            Vector e = new Vector();
            e.addElement(vector.elementAt(1));
            e.addElement(screenString);
            vec.addElement(e);
            //actionListener.actionPerfomed(ActionCommand.CMD_SLIDE_VIEW, vec);
            ActionInvoker.processCommand(new Action(ActionCommand.CMD_SLIDE_VIEW,vec));
   
		}
		else
			if(id == MODIFY_ID)
			{
				//Vector v = new Vector();
				//v.addElement(Utils.getFNOExpiryAndStrikeDataURL(bannerData.getCompanyCode()));
				//v.addElement("");
				// UiApplication.getUiApplication().invokeLater(new Runnable() {
                   //  public void run() {
                     //	ScreenInvoker.removeRemovableScreen();  
                     	hashT = (Hashtable)vector.elementAt(1);
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
                     	
				fnoTradeConfig = new FNOTradeConfiguration();
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
				
				
				 fnoTradeBean = new FNOTradeBean();
				 fnoTradeBean.setIndexName((String)hashT.get("company_code"));
				 fnoTradeBean.addExpiryData((String)hashT.get("Expiry"));
				 fnoTradeBean.setMinLot((String)hashT.get("MinLot"));
				 fnoTradeBean.setFlag((String)hashT.get("flag"));
				 fnoTradeBean.addStrikeData((String)hashT.get("StrikePrice"));
				 ccode = ccode.toUpperCase();
				 if(ccode.indexOf("CALL")>-1)
					 ccode = Utils.findAndReplace(ccode, "CALL", "CE");
				 else if(ccode.indexOf("PUT")>-1)
					 ccode = Utils.findAndReplace(ccode, "PUT", "PE");
				 new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(ccode),121,returnString,false);
					
				//ScreenInvoker.pushScreen(fnoTradeScreen, true, true);
    			
                   //  }
            // });
			}
	}

	public void setReturnString(String string, int id) {
		if(id==121){
		Vector v = HomeJsonParser.getVector(string);
		bannerData = (HomeJson) v.elementAt(0);
		FNOTradeModify fnoTradeScreen = new FNOTradeModify(fnoTradeBean, "Modify Order",fnoTradeConfig,bannerData, (String)hashT.get("expiry"), hashT);
		
		/*Vector commandDataVector = new Vector();
		commandDataVector.addElement((String)hashData.get("order_id"));
		commandDataVector.addElement((String)hashData.get("scripcode"));						
		commandDataVector.addElement((String)hashData.get("exchange"));
		commandDataVector.addElement((String)hashData.get("userid"));
		commandDataVector.addElement(screenString);
		commandDataVector.addElement(bannerData);

		Action fnoOrderModifyAction = new Action(ActionCommand.CMD_FNO_TRADE_ORDER_DETAILS,commandDataVector);
		ActionInvoker.processCommand(fnoOrderModifyAction);*/
		}
		
	}
}
