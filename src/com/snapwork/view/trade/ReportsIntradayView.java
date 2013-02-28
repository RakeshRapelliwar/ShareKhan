package com.snapwork.view.trade;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
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
import com.snapwork.parsers.TradeNowMainParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.UserInfo;

public class ReportsIntradayView extends VerticalFieldManager implements ReturnString, ReturnDataWithId {

        private static byte padding = 4; 
        private final Bitmap bmp = ImageManager.getSmallTradeButton();
        private ActionListener actionListener;
        final ReturnDataWithId returnDataWithId = this;
        final ReturnString returnString = this;
        private HomeJson bannerData;
        private Hashtable hashData;
        

        public ReportsIntradayView(Hashtable dataHolder,ActionListener actionListener) {

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
                        if((String) dataHolder.get("SCRIPCODE")== null)
                        	text = " - ";
                        else
                        	text = (String) dataHolder.get("SCRIPCODE");
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x6c8889, 0));
                        	 	
                        if((String) dataHolder.get("EXCHANGE")== null)
                        	text = " - ";
                        else
                        	text = (String) dataHolder.get("EXCHANGE");
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 0)); 
                        if((String) dataHolder.get("PRODUCTTYPE")== null)
                        	text = " - ";
                        else
                        	text = (String) dataHolder.get("PRODUCTTYPE");
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 1)); 
                        if((String) dataHolder.get("POSITION")== null)
                        	text = "Position : - ";
                        else
                        	text = "Position : " + (String) dataHolder.get("POSITION");
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 0)); 
                        if((String) dataHolder.get("POSVAL")== null)
                        	text = "Val : - ";
                        else
                        	text = "Val : " + (String) dataHolder.get("POSVAL");
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 1)); 
                        if((String) dataHolder.get("MARGIN")== null)
                        	text = "Scrip Margin : - ";
                        else
                        	text = "Scrip Margin : " + (String) dataHolder.get("MARGIN");
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x333333, 0));
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
                    graphics.fillRect(1, (getPreferredHeight()/4), getPreferredWidth()-2, (getPreferredHeight()/4)*2);
                    graphics.setColor(0x333333);//0x5d5d5d); // origional grey 0x333333
                    graphics.fillRect(1, ((getPreferredHeight()/4)*3), getPreferredWidth()-2, getPreferredHeight()/4);
            }

                protected void sublayout(int width, int height) {
                        layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
                       // layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
                       // layoutChild(getField(2), getField(2).getPreferredWidth(), getField(2).getPreferredHeight());
                       // layoutChild(getField(3), getField(3).getPreferredWidth(), getField(3).getPreferredHeight());
                        layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
                        layoutChild(getField(2), getField(2).getPreferredWidth(), getField(2).getPreferredHeight());
                        layoutChild(getField(3), getField(3).getPreferredWidth(), getField(3).getPreferredHeight());
                        layoutChild(getField(4), getField(4).getPreferredWidth(), getField(4).getPreferredHeight());
                        layoutChild(getField(5), getField(5).getPreferredWidth(), getField(5).getPreferredHeight());
                        layoutChild(getField(6), getField(6).getPreferredWidth(), getField(6).getPreferredHeight());

                        setPositionChild(getField(0), padding, ((getPreferredHeight()/4)/2) -(getField(0).getPreferredHeight()/2) );
                        //setPositionChild(getField(1), getPreferredWidth()-((bmp.getWidth()+2)*3), 2);
                        //setPositionChild(getField(2), getPreferredWidth()-((bmp.getWidth()+2)*2), 2);
                        //setPositionChild(getField(3), getPreferredWidth()-((bmp.getWidth()+2)*1), 2 );
                        setPositionChild(getField(1), padding, (getPreferredHeight()/4)+((getPreferredHeight()/4)/2) -(getField(1).getPreferredHeight()/2));
                        setPositionChild(getField(2), getPreferredWidth()-((getField(2).getWidth()+2)), (getPreferredHeight()/4)+((getPreferredHeight()/4)/2) -(getField(2).getPreferredHeight()/2));
                        setPositionChild(getField(3), padding, (getPreferredHeight()/2)+((getPreferredHeight()/4)/2) -(getField(3).getPreferredHeight()/2));
                        setPositionChild(getField(4),getPreferredWidth()-((getField(4).getWidth()+2)), (getPreferredHeight()/2)+((getPreferredHeight()/4)/2) -(getField(4).getPreferredHeight()/2));
                        setPositionChild(getField(5), padding, getPreferredHeight()-(((getPreferredHeight()/4)/2))-(getField(5).getPreferredHeight()/2));
                        setPositionChild(getField(6), getPreferredWidth()-((getField(6).getWidth()+2)), (getPreferredHeight()-((getPreferredHeight()/4)/2))-(getField(6).getPreferredHeight()/2));
                        
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



        }

        public void setData(Vector vector, int id) {
        	if(id == 1)
        	{
               // TradeNowMainScreen trade = new TradeNowMainScreen(bannerData);
                //trade.modifyScreenForReportDPSRsell(vector, id);
        	}
        	else if(id == 2)
        	{
        		 Vector vec = new Vector();
                 vec.addElement("Confirm Cancellation !");
                 Vector e = new Vector();
                 e.addElement(vector.elementAt(1));
                 vec.addElement(e);
                 //actionListener.actionPerfomed(ActionCommand.CMD_SLIDE_VIEW, vec);
                 ActionInvoker.processCommand(new Action(ActionCommand.CMD_SLIDE_VIEW,vec));
        
        	}
        }

        public void setReturnString(String string, int id) {
                Vector v = HomeJsonParser.getVector(string);
                bannerData = (HomeJson) v.elementAt(0);
                String url = AppConstants.domainUrl +"orderTransaction.php?custId="+UserInfo.getUserID()+"&orderId="+(String)hashData.get("order_id")+"&exchange="+(String)hashData.get("exchange")+"&btnModify=Modify&page=reports&rmsCode="+(String)hashData.get("rmscode")+"&debug=2";
                LOG.print(" URL : "+url);
                new TradeNowMainParser(url,returnDataWithId,1);
                
        }
}
