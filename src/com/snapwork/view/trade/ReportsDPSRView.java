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

public class ReportsDPSRView extends VerticalFieldManager implements ReturnString, ReturnDataWithId {

        private static byte padding = 4; 
        private final Bitmap bmp = ImageManager.getSmallTradeButton();
        private ActionListener actionListener;
        final ReturnDataWithId returnDataWithId = this;
        final ReturnString returnString = this;
        private HomeJson bannerData;
        private Hashtable hashData;
        private long timer;

        public ReportsDPSRView(Hashtable dataHolder,ActionListener actionListener) {

                ReportsOrderGroup tlv = null;
                timer = System.currentTimeMillis();
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
                        if((String) dataHolder.get("SCRIPCODE") == null)
                        	text = " - ";
                        else
                        	text = (String) dataHolder.get("SCRIPCODE") + " : ";

                        if((String) dataHolder.get("EXCHANGE") == null);
                        else
                        	text = text + (String) dataHolder.get("EXCHANGE");

                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x6c8889,0));
                        
                        String aval = (String) dataHolder.get("AVAL");
                        double d = Double.parseDouble(aval);
                        if(d>0)
                        	add(getSmallBitmapField("Sell", dataHolder));
                        else
                        	add(getSmallBitmapFieldBlank("null", 0x6c8889));

                        float holdPrice = 0;
                        if((String) dataHolder.get("HOLDPRICE") == null) {
                        	text = "Hold Price : ";
                        } else {
                        	text = "Hold Price : " + (String) dataHolder.get("HOLDPRICE");
                        	holdPrice = parseStringToFloat((String) dataHolder.get("HOLDPRICE"));
                        }
                        
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 0));

                        float netBal = 0;
                        
                        if((String) dataHolder.get("AVAL") == null) {
                        	text = "Net Bal : ";
                        } else {
                        	text = "Net Bal : " + (String) dataHolder.get("AVAL");
                        	netBal = parseStringToFloat((String) dataHolder.get("AVAL"));
                        }
                        
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000,1)); 
                       
                        double holdVal = netBal*holdPrice;
                        text = "Hold Val : " + roundValue(holdVal,2);
                        
                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 0));

                        if((String) dataHolder.get("MKTPRICE") == null)
                        	text = "Mkt Price : ";
                        else
                        	text = "Mkt Price : " + (String) dataHolder.get("MKTPRICE");

                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 0));
                        
                        float mktValue = 0;
                        if((String) dataHolder.get("MKTVALUE") == null) {
                        	text = "Mkt Val : ";
                        } else {
                        	text = "Mkt Val : " + (String) dataHolder.get("MKTVALUE");
                        	mktValue = parseStringToFloat((String) dataHolder.get("MKTVALUE"));
                        }

                        add(getLabel(text, FontLoader.getFont(AppConstants.REPORTS_FONT), 0xeeeeee, 0x000000, 0));

                        text = "Profit/Loss : " + roundValue((mktValue-holdVal),2);

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

                public float parseStringToFloat(String targetString) {

                	targetString = Utils.findAndReplace(targetString, ",", "");

                	float retValue = 0;
                	
                	try {

                		retValue = Float.parseFloat(targetString);

                	} catch(NumberFormatException nfEx) {
                		retValue = 0;
                	} catch(Exception ex) {
                		retValue = 0;
                	}
                	
                	return retValue;
                }
                
                public String roundValue(double targetValue,int digits) {

                	String targetString = Double.toString(targetValue);
                	if(targetString.lastIndexOf('.')!=-1) {
                		int dotIndex = targetString.lastIndexOf('.');

                		if((dotIndex+digits)<targetString.length() || (dotIndex+digits)==targetString.length()) {

                			if((dotIndex+digits)==targetString.length()) {
                				return targetString;
                			}
                			
                			return targetString.substring(0, (dotIndex+digits+1));
                		}
                		return targetString;
                	}

                	return "";
                }
                
                public int getTotalFields() {
                        if(requireNullField)
                                return this.getFieldCount()-1;
                        else
                                return this.getFieldCount(); 
                }

                public int getPreferredHeight() {
                        return (getFont().getHeight()+ 4) * 6;
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
                        graphics.fillRect(1, (getPreferredHeight()/6), getPreferredWidth()-2, (getPreferredHeight()/6)*4);
                        graphics.setColor(0x333333);//0x5d5d5d); // original grey 0x333333
                        graphics.fillRect(1, (getPreferredHeight()/6)*5, getPreferredWidth()-2,(getPreferredHeight()/6)-1);
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

						setPositionChild(getField(0), padding, 0 );
						setPositionChild(getField(1), getPreferredWidth()-(getField(1).getPreferredWidth()+4), 1);
						setPositionChild(getField(2), padding, (getPreferredHeight()/6)*1);
						setPositionChild(getField(3), getPreferredWidth()-(getField(3).getPreferredWidth()+4), (getPreferredHeight()/6)*1);
						setPositionChild(getField(4), padding, (getPreferredHeight()/6)*2);
						setPositionChild(getField(5), padding, (getPreferredHeight()/6)*3);
						setPositionChild(getField(6), padding, (getPreferredHeight()/6)*4);
						setPositionChild(getField(7), padding, (getPreferredHeight()/6)*5);

                        setExtent(getPreferredWidth(), getPreferredHeight());

                }
                
                
                public BitmapField getSmallBitmapField(final String caption, final Object object)//, final String title, final String url, final Action action)
        		{
                	final String cp = caption.replace(' ', '_');
        			int wd = AppConstants.screenHeight<480?8:16;
        			wd = wd + FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT).getAdvance(cp);
        			if(bmp.getWidth()<wd)
        				wd = bmp.getWidth();
        			
        			final Bitmap bitmap = ImageManager.resizeBitmap(bmp, FontLoader.getFont(AppConstants.REPORTSMARGIN_FONT).getAdvance(cp)+wd, bmp.getHeight());
        			return new BitmapField(bitmap, FOCUSABLE | DrawStyle.HCENTER)
        			{
        				protected boolean navigationClick(int status,int time) {
        					eventHandlar();
        					
        					return super.navigationClick(status, time);
        				}
        				private void eventHandlar() {
        					if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
        					if(!Utils.sessionAlive)
        					{
        						ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
        					}
        					else
        					{
        						if(caption.equalsIgnoreCase("Sell"))
        						{
        							Hashtable h = (Hashtable)object;
        	                        LOG.print("(String)h.get(\"SCRIPCODE\") : "+(String)h.get("SCRIPCODE"));
        	                       ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
        	                       new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL((String)h.get("SCRIPCODE")),1,returnString,false);
        	                    
        						}
        				}}
							
						}
						protected boolean touchEvent(TouchEvent message) {
        					if(message.getEvent() == TouchEvent.CLICK) {
        						eventHandlar();
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
        				/*protected void paintBackground(Graphics graphics) {
        					graphics.setBackgroundColor(Color.BLACK);
        					graphics.clear();
        				}*/
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
                
                public LabelField getSmallBitmapFieldBlank(final String caption,
        				final int color)// , final String title, final String url, final
        								// Action action)
        		{
        			return new LabelField("", NON_FOCUSABLE | DrawStyle.HCENTER) {

        				protected boolean navigationClick(int status, int time) {
        					return super.navigationClick(status, time);
        				}

        				protected boolean touchEvent(TouchEvent message) {
        					return super.touchEvent(message);
        				}

        				private boolean isFocused = false;

        				protected void onFocus(int direction) {
        					isFocused = true;
        					invalidate();
        				}

        				protected void onUnfocus() {
        					isFocused = false;
        					invalidate();
        				}

        				protected void paintBackground(Graphics graphics) {
        					if (getPreferredHeight() == 2) {
        						graphics.setColor(color);
        						graphics.fillRect(0, 0, getPreferredWidth(),
        								getPreferredHeight());
        						return;
        					}
        					graphics.setBackgroundColor(Color.BLACK);
        					graphics.clear();
        				}

        				protected void paint(Graphics graphics) {
        					graphics.setColor(color);
        					graphics.fillRect(0, 0, getPreferredWidth(),
        							getPreferredHeight());
        				}

        				protected void drawFocus(Graphics graphics, boolean on) {

        				}

        				public Font getFont() {
        					return FontLoader.getFont(AppConstants.REPORTS_FONT);
        				}

        				public int getPreferredHeight() {
        					return bmp.getHeight();
        				}// (getFont().getHeight()*2)-(getFont().getHeight()/2);}

        				public int getPreferredWidth() {
        					return 1;
        				}

        				protected void layout(int arg0, int arg1) {
        					setExtent(getPreferredWidth(), getPreferredHeight());
        				}
        			};
        		}
                public LabelField getLabel(final String text, final Font fnt, final int foreColor, final int bgColor, final int id)
                {
                        return new LabelField(text, NON_FOCUSABLE) {

                                public int getPreferredHeight() {
                                        return fnt.getHeight()+4-1;
                                }
                                public int getPreferredWidth() {
                                        return fnt.getAdvance(text)+8;
                                }


                                protected void paint(Graphics graphics) {
                                        graphics.setColor(foreColor);
                                        graphics.setFont(fnt);
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
        	if(id == 41)
        	{
                TradeNowMainScreenCM trade = new TradeNowMainScreenCM(bannerData);
                trade.modifyScreenForReportDPSRsell(vector, id, false);
        	}
        }

        public void setReturnString(String string, int id) {
                Vector v = HomeJsonParser.getVector(string);
                bannerData = (HomeJson) v.elementAt(0);
                //http://50.17.18.243/SK_live/orderEQ.php?ccode=BHEL&exchange=NSE&action=S&custId=250037&page=dpsr
                //String url = AppConstants.domainUrl +"orderTransaction.php?custId="+UserInfo.getUserID()+"&orderId="+(String)hashData.get("order_id")+"&exchange="+(String)hashData.get("exchange")+"&btnModify=Modify&btnDel=&page=reports&userAgent=0&rmsCode="+(String)hashData.get("rmscode")+"&btnModify1.x=25&btnModify1.y=2&debug=2";
                //orderEQ.php?ccode=BHEL&exchange=NSE&action=S&qty=25&custId=250037&page=dpsr
                //String url = AppConstants.domainUrl +"orderEQ.php?ccode="+(String)hashData.get("SCRIPCODE")+"&exchange="+(String)hashData.get("EXCHANGE")+"&action=S&qty="+(String)hashData.get("AVAL")+"&custId="+UserInfo.getUserID()+"&page=dpsr&debug=2";
                String url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=steqdpsr02&ccode="+(String)hashData.get("SCRIPCODE")+"&exchange="+(String)hashData.get("EXCHANGE")+"&qty="+(String)hashData.get("AVAL")+"&action=S&custId="+UserInfo.getUserID()+"&page=dpsr";
                LOG.print(" URL : "+url);
                new TradeNowMainParser(url,returnDataWithId,41);
                
        }
}
