package com.snapwork.view.trade;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.FNOTradeOrderConfirmBean;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.TitleBar;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class FNOTradeOrderNumberViewScreen extends MainScreen implements ActionListener, RemovableScreen, ReturnDataWithId{

	private BottomMenu bottomMenu = null;    
	private String screenTitle;
	private long timer;
        private FNOTradeOrderConfirmBean tradeOrderConfirmBean;
        
        public void setScreenTitle(String screenTitle) {
                this.screenTitle = screenTitle;
        }

        public String getScreenTitle() {
                return screenTitle;
        }

        public void setTradeOrderConfirmBean(FNOTradeOrderConfirmBean tradeOrderConfirmBean) {
                this.tradeOrderConfirmBean = tradeOrderConfirmBean;
        }

        public FNOTradeOrderConfirmBean getTradeOrderConfirmBean() {
                return tradeOrderConfirmBean;
        }

        public FNOTradeOrderNumberViewScreen(String screenTitle,FNOTradeOrderConfirmBean tradeOrderConfirmBean) {
        	getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        	setScreenTitle(screenTitle);
        	timer = System.currentTimeMillis();
                setTradeOrderConfirmBean(tradeOrderConfirmBean);
                createUI();
        }

        VerticalFieldManager verticalFieldManager;
        
    public void createUI() {

        //Set Title Bar
        TitleBar titleBar = new TitleBar(getScreenTitle()); 
        setTitle(titleBar);

        verticalFieldManager = new VerticalFieldManager(USE_ALL_WIDTH | USE_ALL_HEIGHT)  {

            public void paintBackground(Graphics graphics)
            {
                    graphics.setColor(Color.BLACK);
                    graphics.fillRect(0, 0, AppConstants.screenWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
            }

            protected void sublayout( int maxWidth, int maxHeight )
            {
                     super.sublayout(maxWidth, maxHeight);
                     setExtent(AppConstants.screenWidth, AppConstants.screenHeight-TitleBar.getItemHeight());
            }
        };

        verticalFieldManager.add(new NullField(FOCUSABLE));
        
        VerticalFieldManager fieldManager = new VerticalFieldManager(USE_ALL_WIDTH);
        fieldManager.setBackground(BackgroundFactory.createSolidBackground(0x222222));
        
        boolean isOrderFailed = (tradeOrderConfirmBean.getOrderNumber()==null || tradeOrderConfirmBean.getOrderNumber().trim().length()==0);

        if(isOrderFailed) {
        	fieldManager.add(getLabelWithTextColorForBoldText("Order Fail", 0xcc3333));
        	fieldManager.add(getLabelWithTextColorForBoldTextWrap(tradeOrderConfirmBean.getErrorMessage(), 0xcc3333));
        } else {
        	fieldManager.add(getLabelWithTextColorForBoldText("Order Success", 0x66cc00));
        	fieldManager.add(getLabelWithTextColorForNormalText("We have successfully submitted your order.","Please check the view order screen for further details."));
        	fieldManager.add(getLabelWithTextColorForBoldText("Order Number : "+tradeOrderConfirmBean.getOrderNumber().trim(), Color.WHITE));
        }

        verticalFieldManager.add(fieldManager);

        final Bitmap bmp = ImageManager.getTradeNow();

//        final String strConfirm = "View Order";
//
//        BitmapField btnViewOrder = new BitmapField(bmpTradeNow,  FOCUSABLE | DrawStyle.HCENTER | FIELD_LEFT)  {
//
//                protected boolean navigationClick(int status,int time) {
//                	if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
//       			 viewOrderAction();
//       	}
//                        return true;
//                }
//                
//                protected boolean touchEvent(TouchEvent message)
//            	{ setFocus();
//            		if(message.getEvent() == TouchEvent.CLICK) {
//            			if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
//            			 viewOrderAction();
//            	}
//            		}
//            	return super.touchEvent(message);
//            	}
//
//                private boolean isFocused = false;
//                protected void onFocus(int direction) 
//                {
//                        isFocused = true;
//                        invalidate();
//                }
//
//                protected void onUnfocus() {
//                        isFocused = false;
//                        invalidate();
//                }
//
//                protected void paintBackground(Graphics graphics) {
//                        graphics.setBackgroundColor(Color.BLACK);
//                        graphics.clear();
//                }
//
//                protected void paint(Graphics graphics) 
//                {
//                        if(isFocused) {
//                                graphics.setColor(Color.ORANGE);
//                        } else {
//                                graphics.setColor(Color.WHITE);
//                        }
//
//                        graphics.drawBitmap(0,0, bmpTradeNow.getWidth(), bmpTradeNow.getHeight(), bmpTradeNow, 0, 0);                                        
//                        graphics.drawText(strConfirm,(bmpTradeNow.getWidth()/2)-(getFont().getAdvance(strConfirm)/2),(bmpTradeNow.getHeight()/2)-(getFont().getHeight()/2));
//                }
//
//                protected void drawFocus(Graphics graphics, boolean on) 
//                {
//
//                }
//                public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
//                public int getPreferredHeight() {return  bmpTradeNow.getHeight();}
//                public int getPreferredWidth() {return bmpTradeNow.getWidth();}
//
//                protected void layout(int width, int height) {
//                        setExtent(getPreferredWidth(), getPreferredHeight());
//                }
//        };
//
//		HorizontalFieldManager horizontalFieldManager = new HorizontalFieldManager(USE_ALL_WIDTH) {
//		        
//		        int xPadding = 10;
//		        
//		    public void paintBackground(Graphics graphics)
//		    {
//		            graphics.setColor(Color.BLACK);
//		            graphics.fillRect(0, 0, AppConstants.screenWidth, bmpTradeNow.getHeight()+bmpTradeNow.getHeight()/2);
//		    }
//		
//		    protected void sublayout( int maxWidth, int maxHeight )
//		    {
//		                layoutChild(getField(0), bmpTradeNow.getWidth(), bmpTradeNow.getHeight());
//		
//		               // setPositionChild(getField(0), xPadding,bmpTradeNow.getHeight()/4 );
//		                setPositionChild(getField(0),  AppConstants.screenWidth/2-bmpTradeNow.getWidth()/2, bmpTradeNow.getHeight()/4);
//		
//		                setExtent(AppConstants.screenWidth, bmpTradeNow.getHeight()+bmpTradeNow.getHeight()/2);
//		    }
//		};
//		horizontalFieldManager.add(btnViewOrder);
//
//		horizontalFieldManager.add(new NullField(FOCUSABLE));
        final String textButtonViewOrder = "View Order";
		BitmapField btnViewOrder = new BitmapField(bmp,  FOCUSABLE | DrawStyle.HCENTER)  {

			protected boolean navigationClick(int status,int time) {
        	if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
			 viewOrderAction();
	}
                return true;
        }
        
        protected boolean touchEvent(TouchEvent message)
    	{ setFocus();
    		if(message.getEvent() == TouchEvent.CLICK) {
    			if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
    			 viewOrderAction();
    	}
    		}
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
			HorizontalFieldManager horizontalFieldManager = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER)
			{
				public void paintBackground(Graphics graphics)
				{
					graphics.setColor(Color.BLACK);
					graphics.fillRect(0, 0, getWidth(), getHeight());
				}
				
				};
				horizontalFieldManager.add(btnViewOrder);
		verticalFieldManager.add(horizontalFieldManager);

        add(verticalFieldManager);
        bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
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

    public void viewOrderAction() {
        Vector dataUrl = new Vector();
		String url = Utils.getReportsFNOURL(UserInfo.getUserID());//AppConstants.domainUrl + "SK_android/controller.php?RequestId=stfnoo01&rtype=order&custId="+UserInfo.getUserID();//"FOreports.php?rtype=order&custId="+UserInfo.getUserID()+"&page=FOreports&debug=2";
		dataUrl.addElement("ReportsOrderf");
		dataUrl.addElement(url);
		ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));
        
    }
    
        public void setData(Vector vector, int id) {
                // TODO Auto-generated method stub

        }

        public void actionPerfomed(byte Command, Object data) {
                // TODO Auto-generated method stub
                switch(Command) {
                        case ActionCommand.CMD_FNO_TRADE_ORDER_CONFIRM_POST:
                                ActionInvoker.processCommand(new Action(Command,data));
                                break;
                        default:
                                break;
                }
        }
        
        public LabelField getLabelWithTextColorForBoldText(final String labelText,final int labelColor) {
        	int xPadding = 5;
        	LabelField labelField = new LabelField(labelText) {
    			public void setFont(Font arg0) {
    				super.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
    			}
    			protected void paintBackground(Graphics graphics) {
    				graphics.setColor(0x222222);
    				graphics.fillRect(0, 0, getPreferredWidth(),getPreferredHeight());
    			}
    			protected void paint(Graphics graphics) {
    					graphics.setColor(labelColor);
    					graphics.drawText(labelText,25,2);
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
        	labelField.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
        	labelField.setPadding(new XYEdges(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight()/2, xPadding, FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight()/2, xPadding));
        	return labelField;
        }
        
        public LabelField getLabelWithTextColorForBoldTextWrap(final String labelText,final int labelColor) {
        	int xPadding = 5;
        	LabelField labelField = new LabelField(labelText) {
    			public void setFont(Font arg0) {
    				super.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
    			}
    			protected void paintBackground(Graphics graphics) {
    				graphics.setColor(0x222222);
    				graphics.fillRect(0, 0, getPreferredWidth(),getPreferredHeight());
    			}
    			protected void paint(Graphics graphics) {
    					graphics.setColor(labelColor);
    					super.paint(graphics);
    			}
    		};
        	labelField.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
        	labelField.setPadding(new XYEdges(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight()/2, xPadding, FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight()/2, xPadding));
        	return labelField;
        }
        
        public LabelField getLabelWithTextColorForNormalText(final String labelText, final String labelText2)
    	{
    		return new LabelField("",NON_FOCUSABLE)
    		{
    			public void setFont(Font arg0) {
    				super.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
    			}
    			protected void paint(Graphics graphics) {
    				graphics.setColor(0x222222);
    				graphics.fillRect(0, 2+(getFont().getHeight()), getPreferredWidth(), 4+(getFont().getHeight()*2));
    				//graphics.setColor(0x999999);
    				/*if(text1.equalsIgnoreCase("Order Success"))
    						graphics.setColor(0x66cc00);
    				else
    					graphics.setColor(0xcc3333);
    				graphics.drawText(text1,5,2);*/
    				graphics.setColor(0xeeeeee);
    				setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
    				graphics.drawText(labelText,5,4);
    				graphics.drawText(labelText2, 5, 6+(getFont().getHeight()));
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
        

}
