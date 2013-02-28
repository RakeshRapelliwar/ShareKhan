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
import com.snapwork.beans.HomeJson;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.parsers.TradeNowMainParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class ReportsTradeView extends VerticalFieldManager implements ReturnString, ReturnDataWithId {

        private static byte padding = 4; 
        private final Bitmap bmp = ImageManager.getSmallTradeButton();
        private ActionListener actionListener;
        final ReturnDataWithId returnDataWithId = this;
        final ReturnString returnString = this;
        private HomeJson bannerData;
        private Hashtable hashData;
        private String url;
        private long timer;

        public ReportsTradeView(String url, Hashtable dataHolder,ActionListener actionListener) {
        		this.url = url;
        		timer = System.currentTimeMillis();
                ReportsOrderGroup tlv = null;
                this.actionListener = actionListener;
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
                        dataHolder.put("url",url);
                        if((String) dataHolder.get("SHAREKHAN_SCRIP_CODE") == null)
                        	text = " - ";
                        else
                        	text = (String) dataHolder.get("SHAREKHAN_SCRIP_CODE") ;
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x6c8889, 0));
                        
                        if((String) dataHolder.get("BUY_SELL") == null)
                        {
                        	text = " - ";
                        }
                        else
                        {
                        	text = (String) dataHolder.get("BUY_SELL");
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
                        }
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 0)); 
                        
                        //asdasd
                         
                        if((String) dataHolder.get("TRADE_QTY") == null)
                        	text = " - ";
                        else
                        	text = (String) dataHolder.get("TRADE_QTY");
                        
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 1)); 
                       // add(getSmallBitmapFieldBlank("null", 0x6c8889));
                       // add(getSmallBitmapFieldBlank("null", 0x6c8889));
                        if((String) dataHolder.get("TRADE_PRICE") == null)
                        	text = " - ";
                        else
                        	{
                        		text = (String) dataHolder.get("TRADE_PRICE");
                        		double num = Double.parseDouble(text) / 100;
                        		text = Utils.DecimalRoundString(num, 2);
                        	}
                        
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 0)); 
                       
                        if((String) dataHolder.get("TRADE_AMOUNT") == null) // check
                        	text = "Trade Value : - ";
                        else
                        	{
                        		text =  (String) dataHolder.get("TRADE_AMOUNT");
                        		double num = Double.parseDouble(text) / 100;
                        		text = Utils.DecimalRoundString(num, 2);
                        		text = "Trade Value : " + text;
                        	}
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x333333, 0)); 
                        add(getSmallBitmapField("Details", dataHolder));
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
                        graphics.fillRect(1, ((getPreferredHeight()/4)*3), getPreferredWidth()-2, getPreferredHeight()/4);
                }

                protected void sublayout(int width, int height) {
                        layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
                 //       layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
                        //layoutChild(getField(2), getField(2).getPreferredWidth(), getField(2).getPreferredHeight());
                        //layoutChild(getField(3), getField(3).getPreferredWidth(), getField(3).getPreferredHeight());
                        layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
                        layoutChild(getField(2), getField(2).getPreferredWidth(), getField(2).getPreferredHeight());
                        layoutChild(getField(3), getField(3).getPreferredWidth(), getField(3).getPreferredHeight()-2);
                //      layoutChild(getField(5), getField(5).getPreferredWidth(), getField(5).getPreferredHeight()-2);
                        layoutChild(getField(4), getField(4).getPreferredWidth(), getField(4).getPreferredHeight()-2);
                        layoutChild(getField(5), getField(5).getPreferredWidth(), getField(5).getPreferredHeight());

                        setPositionChild(getField(0), padding, 2 );
             //           setPositionChild(getField(1), getPreferredWidth()-((bmp.getWidth()+2)*3), 2);
                        //setPositionChild(getField(2), getPreferredWidth()-((bmp.getWidth()+2)*2), 2);
                       // setPositionChild(getField(3), getPreferredWidth()-((bmp.getWidth()+2)*1), 2 );
                        setPositionChild(getField(1), padding, (getPreferredHeight()/4)+2);
                        setPositionChild(getField(2), getPreferredWidth()-((getField(2).getWidth()+2)), (getPreferredHeight()/4)+2);
                        setPositionChild(getField(3), padding, (getPreferredHeight()/2));
           //             setPositionChild(getField(5), getPreferredWidth()-((bmp.getWidth()+2)*3), (getPreferredHeight()/2));
                        setPositionChild(getField(4), padding, getPreferredHeight()-(((getPreferredHeight()/4))/2)-(getField(4).getPreferredHeight()/2));
                       // setPositionChild(getField(5), getPreferredWidth()-((getField(5).getWidth()+2)), (getPreferredHeight()-((getPreferredHeight()/4)/2))-(getField(5).getPreferredHeight()/2));
                        setPositionChild(getField(5), getPreferredWidth()-((getField(5).getPreferredWidth()+2)*1), 2 );
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
                                        	graphics.drawText(text, getPreferredWidth() - graphics.getFont().getAdvance(text) - 4, (getPreferredHeight()/2)-(graphics.getFont().getHeight()/2));
                                        else
                                        	graphics.drawText(text, 2, (getPreferredHeight()/2)-(graphics.getFont().getHeight()/2));
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
        			   return new BitmapField(bmp, FOCUSABLE | DrawStyle.HCENTER)
                        {
                                protected boolean navigationClick(int status,int time) {
                                	if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                                	if(!Utils.sessionAlive)
                					{
                						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
                					}
                					else
                					{  if(caption.equalsIgnoreCase("Details"))
                                        {
                						ReportsOrderView.Load = true;
                                        	 Vector vec = new Vector();
                                             vec.addElement("Equity Trade Position");
                                             Vector e = new Vector();
                                             e.addElement(object);
                                             vec.addElement(e);
                                             //actionListener.actionPerfomed(ActionCommand.CMD_SLIDE_VIEW, vec);
                                             ActionInvoker.processCommand(new Action(ActionCommand.CMD_SLIDE_VIEW,vec));
                                    
                                     }
                                } } return super.navigationClick(status, time);
                                }
                                protected boolean touchEvent(TouchEvent message) {
                                	if(message.getEvent() == TouchEvent.CLICK) {	
                                		if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                                		if(!Utils.sessionAlive)
                					{
                						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
                					}
                					else
                					{	 if(caption.equalsIgnoreCase("Details"))
                                     {
                						ReportsOrderView.Load = true;
                                		 Vector vec = new Vector();
                                         vec.addElement("Equity Trade Position");
                                         Vector e = new Vector();
                                         e.addElement(object);
                                         vec.addElement(e);
                                         //actionListener.actionPerfomed(ActionCommand.CMD_SLIDE_VIEW, vec);
                                         ActionInvoker.processCommand(new Action(ActionCommand.CMD_SLIDE_VIEW,vec));
                                
                                  }
                                }   }} return super.touchEvent(message);
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

                


        }

        public void setData(Vector vector, int id) {
        	if(id == 41)
        	{
              //  TradeNowMainScreen trade = new TradeNowMainScreen(bannerData);
              //  trade.modifyScreenForReportDPSRsell(vector, id);
        	}
        }

        public void setReturnString(String string, int id) {
                Vector v = HomeJsonParser.getVector(string);
                bannerData = (HomeJson) v.elementAt(0);
                //http://50.17.18.243/SK_live/orderEQ.php?ccode=BHEL&exchange=NSE&action=S&custId=250037&page=dpsr
                //String url = AppConstants.domainUrl +"orderTransaction.php?custId="+UserInfo.getUserID()+"&orderId="+(String)hashData.get("order_id")+"&exchange="+(String)hashData.get("exchange")+"&btnModify=Modify&btnDel=&page=reports&userAgent=0&rmsCode="+(String)hashData.get("rmscode")+"&btnModify1.x=25&btnModify1.y=2&debug=2";
                
                String url = AppConstants.domainUrl +"orderEQ.php?ccode="+(String)hashData.get("SCRIPCODE")+"&exchange="+(String)hashData.get("EXCHANGE")+"&action="+(String)hashData.get("action")+"&custId="+UserInfo.getUserID()+"&page=dpsr";
                LOG.print(" URL : "+url);
                new TradeNowMainParser(url,returnDataWithId,41);
                
        }
}
