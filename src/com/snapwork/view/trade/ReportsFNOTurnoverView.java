package com.snapwork.view.trade;

import java.util.Enumeration;
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
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.lightstreamer.javameclient.midp.logger.Logger;
import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.FNOOrderConfirmationBean;
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

public class ReportsFNOTurnoverView extends VerticalFieldManager implements ReturnString, ReturnDataWithId {

        private static byte padding = 4; 
        private final Bitmap bmp = ImageManager.getSmallTradeButton();
        private ActionListener actionListener;
        final ReturnDataWithId returnDataWithId = this;
        final ReturnString returnString = this;
        private HomeJson bannerData;
        private Hashtable hashData;
        private String url;
        private long timer;
        private int SQOFID = 8428;

        public ReportsFNOTurnoverView(String url, Hashtable dataHolder,ActionListener actionListener) {

                ReportsOrderGroup tlv = null;
                timer = System.currentTimeMillis();
                this.actionListener = actionListener;
                dataHolder.put("url", url);
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
                        LOG.print("You are in FNOTurnOver");
                        
                       /* Enumeration enumer = dataHolder.keys();
                        System.out.println("----------------------------------");
                        while(enumer.hasMoreElements()) {
                        	String key = (String)enumer.nextElement();
                          System.out.println("key : " + key);
                          System.out.println("value : "+dataHolder.get(key));
                      	}
                        System.out.println("----------------------------------");*/

                        if(dataHolder == null) return;
                        if(dataHolder.size() == 0) return;
                        String text = "";
                       if((String) dataHolder.get("NETQTY") == null)
                        	text = "0";
                        else
                        	text = "" + (String) dataHolder.get("NETQTY");
                        try{if(Double.parseDouble(text)>0);else text="0";}catch(Exception e){text = "0";}
                        add(getLabel(dataHolder.get("CONTRACT").toString(), FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x6c8889,0));
                        
                        if(text.equalsIgnoreCase("0"))
                        	add(getSmallBitmapFieldBlank("null", 0x6c8889));
                        else {
                        	add(getSmallBitmapField("Square Off", dataHolder));
                        }

                        add(getSmallBitmapField("Details", dataHolder));

                        ////////////////////////////////////
                        if((String) dataHolder.get("NETQTY") == null)
                        	text = "Net Qty : - ";
                        else
                        	text = "Net Qty : " + (String) dataHolder.get("NETQTY");
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x333333,4)); 
                        
                        if((String) dataHolder.get("BPL") == null) // check
                        	text = "BPL : - ";
                        else
                        	text = "BPL : " + (String) dataHolder.get("BPL");
                        
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x333333,4)); 
                        if((String) dataHolder.get("NETRATE") == null) // check
                        	text = "Net Rate : - ";
                        else
                        	text = "Net Rate : " + (String) dataHolder.get("NETRATE");
                        
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x333333,4)); 
                        if((String) dataHolder.get("MTM") == null)
                        	text = "Intraday MTM : - ";
                        else
                        	text = "Intraday : " + (String) dataHolder.get("MTM");
                        
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000,4)); 
                        
                        if((String) dataHolder.get("MKTPRICE") == null)
                        	text = "Mkt. Price : - ";
                        else
                        	text = "Mkt. Price : " + (String) dataHolder.get("MKTPRICE");
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000,4)); 
                        if((String) dataHolder.get("PREVCLOSE") == null)
                        	text = "Pre. Close : - ";
                        else
                        	text = "Pre. Close : " + (String) dataHolder.get("PREVCLOSE");
                        
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000,4)); 
                       
                        ////////////////////////////////////
                        
                        
                       //add(getSmallBitmapField("Details", dataHolder));
                        add(new NullField(NON_FOCUSABLE));
                        
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
                    return (getFont().getHeight()+ 4) * 7;
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

               /* protected void paintBackground(Graphics graphics) {
                        graphics.setColor(0x6c8889);//0x96abab);
                        graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
                        graphics.setColor(0x000000);
                        graphics.fillRect(1, (getPreferredHeight()/3), getPreferredWidth()-2, getPreferredHeight()/3);
                        graphics.setColor(0x333333);//0x5d5d5d); // origional grey 0x333333
                        graphics.fillRect(1, ((getPreferredHeight()/3)*2), getPreferredWidth()-2, getPreferredHeight()/3);
                }*/
                protected void paintBackground(Graphics graphics) {
                    graphics.setColor(0x6c8889);//0x96abab);
                    graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
                    graphics.setColor(0x000000);
                    graphics.fillRect(1,FontLoader.getFont(AppConstants.REPORTS_FONT).getHeight()+(getPreferredHeight()/7)+14, getPreferredWidth()-2, (getPreferredHeight()/7)*4);
                    graphics.setColor(0x333333);//0x5d5d5d); // origional grey 0x333333
                    graphics.fillRect(1, getPreferredHeight()-((getPreferredHeight()/7)), getPreferredWidth()-2, (getPreferredHeight()/7));
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
                        
                        setPositionChild(getField(0), padding, 0 );
                        setPositionChild(getField(1), getPreferredWidth()-((getField(1).getPreferredWidth()+2)+(getField(2).getPreferredWidth()+2)), getField(1).getPreferredHeight()+1);
                        setPositionChild(getField(2), getPreferredWidth()-(getField(2).getPreferredWidth()+2), getField(1).getPreferredHeight()+1);
                        setPositionChild(getField(3), padding, (getPreferredHeight()-(getField(3).getPreferredHeight()-1)*5));
                        setPositionChild(getField(4), getPreferredWidth()-(getField(4).getPreferredWidth()+2), (getPreferredHeight()-(getField(3).getPreferredHeight()-1)*5));
                        //setPositionChild(getField(5), padding, (getPreferredHeight()-(getField(3).getPreferredHeight()-1)*2));
                        //setPositionChild(getField(6), getPreferredWidth()-(getField(6).getPreferredWidth()+2),(getPreferredHeight() -(getField(3).getPreferredHeight()-1)*2));
                        //setPositionChild(getField(7), padding, (getPreferredHeight()-(getField(3).getPreferredHeight()-1)*1));
                        setPositionChild(getField(5), padding, (getPreferredHeight()-(getField(3).getPreferredHeight()-1)*4));
                        setPositionChild(getField(6), padding, (getPreferredHeight()-(getField(3).getPreferredHeight()-1)*3));
                        setPositionChild(getField(7), padding, (getPreferredHeight()-(getField(3).getPreferredHeight()-1)*2));
                        setPositionChild(getField(8), padding, (getPreferredHeight()-(getField(3).getPreferredHeight()-1)*1));
                        
                       
                        setExtent(getPreferredWidth(), getPreferredHeight());

                }
                public LabelField getLabel(final String text, final Font fnt, final int foreColor, final int bgColor,final int i)
                {
                        return new LabelField(text, NON_FOCUSABLE) {

                                public int getPreferredHeight() {
                                	if(i == 0)
                                		return (getFont().getHeight()+4)*2;
                                    return getFont().getHeight()+4;
                                }
                                public int getPreferredWidth() {
                                        return getFont().getAdvance(text)+4;
                                }


                                protected void paint(Graphics graphics) {
                                        graphics.setColor(foreColor);
                                        graphics.setFont(fnt);
                                        if(i==0)
                                        	graphics.drawText(text, 0, (getPreferredHeight()/4)-(graphics.getFont().getHeight()/2));
                                        else if(i%2==0)
                                        	graphics.drawText(text, 0, (getPreferredHeight()/2)-(graphics.getFont().getHeight()/2));
                                        else 
                                        	graphics.drawText(text, getPreferredWidth()-(graphics.getFont().getAdvance(text)+2), (getPreferredHeight()/2)-(graphics.getFont().getHeight()/2));
                                        //super.paint(graphics);
                                }
                                protected void drawFocus(Graphics graphics, boolean on) {
                                }

                                protected void layout(int width, int height) {
                                        super.layout(width, height);
                                        setExtent(getPreferredWidth(), getPreferredHeight());
                                }
                               /* protected void paintBackground(Graphics graphics) 
                                {
                                        graphics.setColor(bgColor);
                                        graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
                                }*/
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
                                	sQclick();
                                	 return super.navigationClick(status, time);
                                }
                                protected boolean touchEvent(TouchEvent message) {
                                	if(message.getEvent() == TouchEvent.CLICK) {
                                		sQclick();
                                	}   return super.touchEvent(message);
                                }
                                private void sQclick()
                                {
                                	if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                            		if(caption.equalsIgnoreCase("Details"))
                                 {
                            			ReportsOrderView.Load = true;
                            		 Vector vec = new Vector();
                                     vec.addElement("FNO Turnover Details");
                                     Vector e = new Vector();
                                     e.addElement(object);
                                     vec.addElement(e);
                                     //actionListener.actionPerfomed(ActionCommand.CMD_SLIDE_VIEW, vec);
                                     ActionInvoker.processCommand(new Action(ActionCommand.CMD_SLIDE_VIEW,vec));
                             }
                            	else
                            	{
                            		ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
                            		/*Vector commandDataVector = new Vector();
            						commandDataVector.addElement((String)hashData.get("CONTRACT"));
            						commandDataVector.addElement((String)hashData.get("EXCHANGE"));		
            						int netQty  = Integer.parseInt((String)hashData.get("NETQTY"));
            						String action = "";
            						String qty = "";
            					    if(netQty > 0){
            					         action = "S";
            					        qty     = Integer.toString(netQty);
            					    }else if(netQty < 0){
            						netQty = netQty * -1;
            					        action = "B";
            					        qty     = Integer.toString(netQty);
            					    } 
            						commandDataVector.addElement(action);
            						commandDataVector.addElement(qty); //RMSCODE
            						commandDataVector.addElement("turnover");
            						
            						Action fnoOrderModifyAction = new Action(ActionCommand.CMD_FNO_TRADE_ORDER_TURNOVER,commandDataVector);
            						ActionInvoker.processCommand(fnoOrderModifyAction);*/
                            		String string = (String)hashData.get("SYMBOL");
                            		LOG.print("(String)hashData.get(\"SYMBOL\") "+string);
                            		//String string = "NIFTY_25-10-2012_6200_CE";
                            		String mainString = string;
                            		int counter = 0;
                            		int len = 0;
                            		if(string.indexOf('.')<0){
                            		if(string.indexOf('_')>-1)
                            		{
                            			len = string.indexOf('_')+1;
                            			string = string.substring(string.indexOf('_')+1, string.length());
                            			counter++;
                            		}
                            		if(string.indexOf('_')>-1)
                            		{
                            			len = len + string.indexOf('_')+1;
                            			string = string.substring(string.indexOf('_')+1, string.length());
                            			counter++;
                            			if(string.indexOf('_')>1)
                            			{
                            				counter++;
                            				mainString = mainString.substring(0, len+string.indexOf('_'))+".00"+mainString.substring(len+string.indexOf('_'),mainString.length());
                            			}
                            		}
                            	}
                            		new ReturnStringParser(Utils.getQuoteURL(mainString), SQOFID, returnString);
            						
                            	}
                            }  
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
                                        graphics.drawText(caption,(bitmap.getWidth()/2)-(getFont().getAdvance(caption)/2),(bitmap.getHeight()/2)-(getFont().getHeight()/2));

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

        public void setData(Vector vector, int id) {
        	if(id == SQOFID)
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
             		 String opts = (String)hashT.get("optType");
    				 if(opts.indexOf("CALL")>-1)
    					 opts = Utils.findAndReplace(opts, "CALL", "CE");
    				 else if(ccode.indexOf("PUT")>-1)
    					 opts = Utils.findAndReplace(opts, "PUT", "PE");
             		ccode = (String)hashT.get("company_code")+"_"+(String)hashT.get("expiry")+"_"+(String)hashT.get("strikePrice")+"_"+opts;
             	}
        		FNOTradeConfiguration fnoTradeConfig = new FNOTradeConfiguration();
        		fnoTradeConfig.setCompanyCode(ccode);
        		fnoTradeConfig.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX);
        		if(hashT.containsKey("instrument"))
        		{
        			if(((String)hashT.get("instrument")).indexOf("OPT")>-1)
        			{
        				fnoTradeConfig.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX);
        			}
        			else
        			{
        				fnoTradeConfig.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX);
        			}
        		}
        		if(hashT.containsKey("orderType"))
        		{
				if(((String)hashT.get("orderType")).equalsIgnoreCase("limit"))
				{
					fnoTradeConfig.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT);
				}
				else
				{

					fnoTradeConfig.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_MARKET);
				}
        		}
				fnoTradeConfig.setFnoExpiryDate((String)hashT.get("expiry"));
				fnoTradeConfig.setFnoStrikePriceValue((String)hashT.get("strikePrice"));
				//v.addElement(fnoTradeConfig.copy());
				//v.addElement(bannerData.copy());
				//v.addElement((String)hashData.get("expiry"));
				
				
				FNOTradeBean fnoTradeBean = new FNOTradeBean();
				 fnoTradeBean.setSymbolName((String)hashT.get("symbolName"));
				 fnoTradeBean.setIndexName((String)hashT.get("instrument"));
				 fnoTradeBean.addExpiryData((String)hashT.get("Expiry"));
				 fnoTradeBean.setMinLot((String)hashT.get("mkt-lots"));
				 fnoTradeBean.setFlag((String)hashT.get("flag"));
				 fnoTradeBean.addStrikeData((String)hashT.get("StrikePrice"));
				 FNOTradeTurnOverScreen fnoTradeScreen = new FNOTradeTurnOverScreen(fnoTradeBean, "Turnover",fnoTradeConfig,bannerData, (String)hashT.get("expiry"), hashT);
					
        	}
        }

        public void setReturnString(String string, int id) {
        	if(id == SQOFID)
        	{
        		Vector v = HomeJsonParser.getVector(string);
        		if(v.size()>0)
        		{
        			if(v.elementAt(0) instanceof HomeJson)
        				bannerData = (HomeJson) v.elementAt(0);
        			FNOOrderConfirmationBean fnoOrderConfirmationBean = new FNOOrderConfirmationBean();
        			fnoOrderConfirmationBean.setSymbolName(bannerData.getCompanyCode());
        			fnoOrderConfirmationBean.setCustId(UserInfo.getUserID());
        			int netQty  = Integer.parseInt((String)hashData.get("NETQTY"));
					String action = "";
					String qty = "";
				    if(netQty > 0){
				         action = "S";
				        qty     = Integer.toString(netQty);
				    }else if(netQty < 0){
					netQty = netQty * -1;
				        action = "B";
				        qty     = Integer.toString(netQty);
				    } 
					//commandDataVector.addElement(action);
					//commandDataVector.addElement(qty);
        			fnoOrderConfirmationBean.setAction(action);
        			fnoOrderConfirmationBean.setQty(qty);
        			new TradeNowMainParser(Utils.getFNOPostSQ2TradeURL(fnoOrderConfirmationBean), returnDataWithId, SQOFID);
        		}
        	}
        	/*
                Vector v = HomeJsonParser.getVector(string);
                bannerData = (HomeJson) v.elementAt(0);

                Vector commandDataVector = new Vector();
				commandDataVector.addElement((String)hashData.get("CONTRACT"));
				commandDataVector.addElement((String)hashData.get("EXCHANGE"));		
				int netQty  = Integer.parseInt((String)hashData.get("NETQTY"));
				String action = "";
				String qty = "";
			    if(netQty > 0){
			         action = "S";
			        qty     = Integer.toString(netQty);
			    }else if(netQty < 0){
				netQty = netQty * -1;
			        action = "B";
			        qty     = Integer.toString(netQty);
			    } 
				commandDataVector.addElement(action);
				commandDataVector.addElement(qty); //RMSCODE
				commandDataVector.addElement("turnover");
				
				if(bannerData!=null) {
					commandDataVector.addElement(bannerData);
				}
				
				Action fnoOrderModifyAction = new Action(ActionCommand.CMD_FNO_TRADE_ORDER_TURNOVER,commandDataVector);
				ActionInvoker.processCommand(fnoOrderModifyAction);
                */
        }
}
