package com.snapwork.view.trade;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.text.NumericTextFilter;
import net.rim.device.api.ui.text.TextFilter;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.Expiry;
import com.snapwork.beans.FNOOrderConfirmationBean;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.CustomLabelField;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TradeScreenFNObanner;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FNOTradeConfiguration;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.UserInfo;

public class FNOTradeOrderTurnOverScreen extends MainScreen implements ActionListener, RemovableScreen, ReturnDataWithId,FieldChangeListener {
        
	public final static byte ID_NONE_DDL = 0;
        public final static byte ID_INSTRUMENT_DDL = 1;
        public final static byte ID_EXPIRY_DDL = 2;
        public final static byte ID_ORDER_TYPE_DDL = 3;
        
    	private BottomMenu bottomMenu = null;
    	private String date;
        
        private boolean isFuturesScreen;

        public void setFuturesScreen(boolean isFuturesScreen) {
                this.isFuturesScreen = isFuturesScreen;
        }
        public boolean isFuturesScreen() {
                return isFuturesScreen;
        }

        private FNOOrderConfirmationBean fnoOrderConfirmationBean;
        public void setFnoOrderConfirmationBean(FNOOrderConfirmationBean fnoOrderConfirmationBean) {
                this.fnoOrderConfirmationBean = fnoOrderConfirmationBean;
        }
        public FNOOrderConfirmationBean getFnoOrderConfirmationBean() {
                return fnoOrderConfirmationBean;
        }
        
        private FNOTradeConfiguration fnoTradeConfiguration;
        public void setFnoTradeConfiguration(FNOTradeConfiguration fnoTradeConfiguration) {
                this.fnoTradeConfiguration = fnoTradeConfiguration;
        }
        public FNOTradeConfiguration getFnoTradeConfiguration() {
                return fnoTradeConfiguration;
        }

        private String screenTitle;
        public void setScreenTitle(String screenTitle) {
                this.screenTitle = screenTitle;
        }
        public String getScreenTitle() {
                return screenTitle;
        }
        
        private HomeJson bannerData;
        public void setBannerData(HomeJson bannerData) {
            this.bannerData = bannerData;
        }
	    public HomeJson getBannerData() {
	            return bannerData;
	    }
        //Constructor
        public FNOTradeOrderTurnOverScreen(FNOOrderConfirmationBean fnoOrderConfirmationBean,String title,FNOTradeConfiguration fnoTradeConfiguration,HomeJson bannerData, String date) {
        	getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
        	this.fnoOrderConfirmationBean = fnoOrderConfirmationBean;
                this.fnoTradeConfiguration = fnoTradeConfiguration;
                this.screenTitle = title;
                this.date = date;
                setBannerData(bannerData);
                createUI();
        }

        //Component Declaration Start

        VerticalFieldManager vfm;
        
        //Banner
        private TradeScreenFNObanner banner;

        //Index Fields
        CustomLabelField lblIndexName,lblIndexValue;
        
        //Instrument Fields
        CustomLabelField lblInstrument,lblInstrumentValue;

        //Expiry Fields
        CustomLabelField lblExpiry,lblExpiryValue;

        //Option Type Fields
        CustomLabelField lblOptionType,lblOptionTypeValue;
        
        //Strike Price Fields
        CustomLabelField lblStrikePrice,lblStrikePriceValue;
        
        //Action Fields
        CustomLabelField lblAction,lblActionValue;
        
        //Market Lot Fields
        CustomLabelField lblMarketLot,lblMarketLotValue;

        //Qty Fields
        CustomLabelField lblQty;
        CustomBasicEditField txtQty;
        
        //Order Type fields
        CustomLabelField lblOrderType;
        ObjectChoiceField ddlOrderTypes;
        String[] orderTypes = {"LIMIT","MARKET"};

        //Price Fields
        CustomLabelField lblPrice;
        CustomBasicEditField txtPrice;
        
        //Trigger Price Fields
        CustomLabelField lblTriggerPrice;
        CustomBasicEditField txtTriggerPrice;
        
        //Validity Fields
        CustomLabelField lblValidity;
        ObjectChoiceField ddlValidity;
        String[] validities = {"GFD","IOC"};
        
        //Component Declaration end

        public void createUI() {

                //Set Title Bar
                TitleBar titleBar = new TitleBar(getScreenTitle()); 
                setTitle(titleBar);
                
                int bannerPrefHeight = 8;
                if(bannerData!=null) {
                	banner = new TradeScreenFNObanner(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),bannerData.getSymbol(), date,bannerData.getCompanyCode(),true);
                	banner.setData(bannerData.getLastTradedPrice(), bannerData.getChange(), (bannerData.getPercentageDiff().indexOf("-") == -1 ? "+"+bannerData.getPercentageDiff() : bannerData.getPercentageDiff()),bannerData.getOpenInterest(), bannerData.getVolume());
                	bannerPrefHeight+=banner.getPreferredHeight();
                }
                final int bannerHeight = bannerPrefHeight;
                
                final int totalRows = (fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX ? 13 : (fnoTradeConfiguration.getFnoOrderType()==FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT ? 11 : 10));
                final Bitmap bmpTradeNow = ImageManager.getTradeNow();

                vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {

                        private int xMargin = 10;
                        private int yMargin = 10;
                        private int xPadding = xMargin + 15;
                        private int yPadding = yMargin + 5;
                        //private int totalRows = 11;
                        private int managerHeight = ((FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4)*totalRows; 

                        public void paintBackground(Graphics graphics)
                        {
                                graphics.setColor(Color.BLACK);
                                graphics.fillRect(0, 0, AppConstants.screenWidth, getHeight());
                                graphics.setColor(0x333333);
                                graphics.fillRoundRect(xMargin, yMargin, AppConstants.screenWidth - xMargin*2 ,managerHeight-yMargin*2,8,8);
                        }

                        protected void sublayout( int maxWidth, int maxHeight )
                        {
                                try {

                                        if(getFieldCount()==0) {
                                            return;
                                        }

                                        for (int i=0;i<(getFieldCount()-1)/2;i++) {
                                            layoutChild(getField(i*2), AppConstants.screenWidth/2, getField(i*2).getPreferredHeight());
                                            layoutChild(getField((i*2)+1), AppConstants.screenWidth/2-xPadding, getField((i*2)+1).getPreferredHeight());
                                        }

                                        if(getField(getFieldCount()-1) instanceof BitmapField) {
                                            layoutChild(getField(getFieldCount()-1),bmpTradeNow.getWidth(), bmpTradeNow.getHeight());
                                        } 

                                        int rowHeight = (FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4;

                                		for (int i=0;i<(getFieldCount()-1)/2;i++) {
                                			setPositionChild(getField(i*2), xPadding, (rowHeight*i)+yPadding);
                                			setPositionChild(getField((i*2)+1),  AppConstants.screenWidth/2, (rowHeight*i)+yPadding);
                                        }

                                        if(getField(getFieldCount()-1) instanceof BitmapField) {
                                            setPositionChild(getField(getFieldCount()-1), AppConstants.screenWidth/2 - bmpTradeNow.getWidth()/2, managerHeight+yPadding*2);
                                            setExtent(AppConstants.screenWidth, managerHeight+bmpTradeNow.getHeight() + yPadding*3);
                                        } else {
                                            setExtent(AppConstants.screenWidth, managerHeight);
                                        }
                                } catch (Exception ex) {
                                        LOG.print("Exception occured in Layout drawing");       
                                }
                        }
                };
                
                HorizontalFieldManager hfm = new HorizontalFieldManager(USE_ALL_WIDTH) {
              	  protected void paintBackground(Graphics graphics) {
              		  graphics.setColor(Color.BLACK);
              		  graphics.fillRect(0, 0, getWidth(), getHeight());
              	  }
              	  
              	  protected void sublayout(int arg0, int arg1) {
              		layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
              		setPositionChild(getField(0),AppConstants.screenWidth/2-getField(0).getPreferredWidth()/2, 0);
              		setExtent(AppConstants.screenWidth, bannerHeight);
              	}
         	
              };
              if(banner!=null) {
              	hfm.add(banner);
              }


                TextFilter decimalFilter = new NumericTextFilter(NumericTextFilter.ALLOW_DECIMAL);
                TextFilter intFiler = TextFilter.get(TextFilter.INTEGER);
                //Initialize components
                if(lblIndexName==null) {
                        lblIndexName = new CustomLabelField("Index Name", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                if(lblIndexValue == null) {
                        lblIndexValue = new CustomLabelField(fnoOrderConfirmationBean.getSymbolName(),0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                vfm.add(lblIndexName);
                vfm.add(lblIndexValue);

                if(lblInstrument==null) {
                        lblInstrument = new CustomLabelField("Instrument", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                if(lblInstrumentValue==null) {
                	lblInstrumentValue = new CustomLabelField(fnoOrderConfirmationBean.getInstType(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                vfm.add(lblInstrument);
                vfm.add(lblInstrumentValue);

                if(lblExpiry==null) {
                        lblExpiry = new CustomLabelField("Expiry", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                if(lblExpiryValue==null) {
                	lblExpiryValue = new CustomLabelField(fnoOrderConfirmationBean.getExpiry(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                vfm.add(lblExpiry);
                vfm.add(lblExpiryValue);
                
                if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX) {
                        
                    if(lblOptionType==null) {
                        lblOptionType = new CustomLabelField("Option Type", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                    }
                    if(lblOptionTypeValue==null) {
                    	lblOptionTypeValue = new CustomLabelField(fnoOrderConfirmationBean.getOptionType(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                    }                    
                    vfm.add(lblOptionType);
                    vfm.add(lblOptionTypeValue);
                        
                        if(lblStrikePrice==null) {
                                lblStrikePrice = new CustomLabelField("Strike Price", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                        }
                        if(lblStrikePriceValue==null) {
                        	lblStrikePriceValue = new CustomLabelField(fnoOrderConfirmationBean.getStrikePrice(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                        }
                        vfm.add(lblStrikePrice);
                        vfm.add(lblStrikePriceValue);

                }

                if(lblAction==null) {
                        lblAction = new CustomLabelField("Action", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                if(lblActionValue==null) {
                	lblActionValue = new CustomLabelField(fnoOrderConfirmationBean.getAction().trim().toLowerCase().equals("b") ? "Buy" : "Sell", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                vfm.add(lblAction);
                vfm.add(lblActionValue);

                if(lblMarketLot==null) {
                        lblMarketLot = new CustomLabelField("Mkt. Lot", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                if(lblMarketLotValue == null) {
                        lblMarketLotValue = new CustomLabelField(fnoOrderConfirmationBean.getMktLot(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                vfm.add(lblMarketLot);
                vfm.add(lblMarketLotValue);

                if(lblQty==null) {
                        lblQty = new CustomLabelField("Qty", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                if(txtQty==null) {
                        txtQty = new CustomBasicEditField(CustomBasicEditField.NO_NEWLINE);
                        txtQty.setFilter(intFiler);
                        txtQty.setText(fnoOrderConfirmationBean.getQty());
                }
                vfm.add(lblQty);
                vfm.add(txtQty);

                if(lblOrderType==null) {
                        lblOrderType = new CustomLabelField("Order Type", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                if(ddlOrderTypes == null) {
                        int tradeOrderSelectedIndex = 0;
                        if(fnoTradeConfiguration.getFnoOrderType()==FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT) {
                                tradeOrderSelectedIndex = 0;
                        } else if(fnoTradeConfiguration.getFnoOrderType()==FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_MARKET) {
                                tradeOrderSelectedIndex = 1;
                        }
                        ddlOrderTypes = getCustomObjectChoiceFieldReg("", orderTypes, 0, 0,ID_ORDER_TYPE_DDL);

                        if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX) {
                            ddlOrderTypes.setSelectedIndex(0);
                            ddlOrderTypes.setEditable(false);
                        } else if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX) {
                            ddlOrderTypes.setSelectedIndex(tradeOrderSelectedIndex);
                        }

                }
                vfm.add(lblOrderType);
                vfm.add(ddlOrderTypes);

                if(fnoTradeConfiguration.getFnoOrderType()==FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT) {
                        if(lblPrice == null) {
                                lblPrice = new CustomLabelField("Price", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                        }
                        if(txtPrice == null) {
                                txtPrice = new CustomBasicEditField(CustomBasicEditField.NO_NEWLINE| CustomBasicEditField.FILTER_REAL_NUMERIC);
                                txtPrice.setFilter(decimalFilter);
                                txtPrice.setChangeListener(this);
                                txtPrice.setText(fnoOrderConfirmationBean.getLimitPrice());
                        }
                        vfm.add(lblPrice);
                        vfm.add(txtPrice);
                }

                if(lblTriggerPrice==null) {
                        lblTriggerPrice = new CustomLabelField("Trigger Price", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                if(txtTriggerPrice == null) {
                        txtTriggerPrice = new CustomBasicEditField(CustomBasicEditField.NO_NEWLINE| CustomBasicEditField.FILTER_REAL_NUMERIC);
                        txtTriggerPrice.setFilter(decimalFilter);
                        txtTriggerPrice.setChangeListener(this);
                        txtTriggerPrice.setText(fnoOrderConfirmationBean.getStopPrice());
                }
                vfm.add(lblTriggerPrice);
                vfm.add(txtTriggerPrice);

                if(lblValidity == null) {
                        lblValidity = new CustomLabelField("Validity", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                }
                if(ddlValidity == null) {
                        ddlValidity = getCustomObjectChoiceFieldReg("", validities, 0, 0,ID_NONE_DDL);
                }
                vfm.add(lblValidity);
                vfm.add(ddlValidity);

                final String strTradeNowButton = "Trade Now";
                        BitmapField btnTradeNow = new BitmapField(bmpTradeNow,  FOCUSABLE | DrawStyle.HCENTER)  {

                                protected boolean navigationClick(int status,int time) {
                                        submiteFnoTradeOrder();
                                        return true;
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
                                                graphics.setColor(Color.WHITE);
                                        }

                                        graphics.drawBitmap(0,0, bmpTradeNow.getWidth(), bmpTradeNow.getHeight(), bmpTradeNow, 0, 0);                                        
                                        graphics.drawText(strTradeNowButton,(bmpTradeNow.getWidth()/2)-(getFont().getAdvance(strTradeNowButton)/2),(bmpTradeNow.getHeight()/2)-(getFont().getHeight()/2));
                                }

                                protected void drawFocus(Graphics graphics, boolean on) 
                                {

                                }
                                public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
                                public int getPreferredHeight() {return  bmpTradeNow.getHeight();}
                                public int getPreferredWidth() {return bmpTradeNow.getWidth();}

                                protected void layout(int width, int height) {
                                        setExtent(getPreferredWidth(), getPreferredHeight());
                                }
	            };
	            vfm.add(btnTradeNow);
	            add(new NullField(FOCUSABLE));
	    		if(banner!=null) {
	    			add(hfm);
	    		}
	    		add(vfm);
                bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
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
        
        protected void paintBackground(Graphics graphics) {
        	graphics.setBackgroundColor(Color.BLACK);
        	graphics.clear();
        }

        public ObjectChoiceField getCustomObjectChoiceFieldReg(String text,String[] values,final int firstIndex,long style,final byte dropDownID) {

                return new ObjectChoiceField(text, values,firstIndex, ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT | ObjectChoiceField.FORCE_SINGLE_LINE) {

                        protected void fieldChangeNotify(int context)
                        {
                                dropDownChanged(dropDownID);

                        }
                };

        }

        public void dropDownChanged(byte dropDownID) {

                if(dropDownID == ID_ORDER_TYPE_DDL) {
                        if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX) {
                                if(ddlOrderTypes.getSelectedIndex()!=fnoTradeConfiguration.getFnoOrderType()) {
                                        fnoTradeConfiguration.setFnoOrderType((byte)ddlOrderTypes.getSelectedIndex());
                                        vfm.deleteAll();
                                        deleteAll();
                                        invalidate();
                                        createUI();
                                        ddlOrderTypes.setFocus();
                                }
                        }
                }
        }

        public void submiteFnoTradeOrder() {

                if(validateOrder()) {
                		
                		fnoOrderConfirmationBean.setCustId(UserInfo.getUserID());
                        fnoOrderConfirmationBean.setQty(txtQty.getText());
                        fnoOrderConfirmationBean.setOrderType(ddlOrderTypes.getChoice(ddlOrderTypes.getSelectedIndex()).toString().toLowerCase());

                        if(ddlOrderTypes.getSelectedIndex()==0) {
							if(txtPrice!=null) {
								fnoOrderConfirmationBean.setLimitPrice((txtPrice.getText().trim().length()==0 ? "0" : txtPrice.getText()));
							} else {
								fnoOrderConfirmationBean.setLimitPrice("0");
							}	
                        } else {
                        	fnoOrderConfirmationBean.setLimitPrice("0");
                        }

                        fnoOrderConfirmationBean.setStopPrice((txtTriggerPrice.getText().trim().length()==0 ? "0" : txtTriggerPrice.getText()));
                        fnoOrderConfirmationBean.setValidity(ddlValidity.getChoice(ddlValidity.getSelectedIndex()).toString());

                        //Need to confirm with Server Team (Jyoti)
                        fnoOrderConfirmationBean.setExchange("NSEFO");
                        fnoOrderConfirmationBean.setType("NOR");
                        fnoOrderConfirmationBean.setDiscQty("0");
                        fnoOrderConfirmationBean.setBannerDate(date);

                        Vector commandDataVector = new Vector();
                        commandDataVector.addElement(fnoOrderConfirmationBean);

                        ActionInvoker actionInvoker = new ActionInvoker(ActionCommand.CMD_FNO_TRADE_ORDER_CONFIRMATION);
                        actionInvoker.setData(commandDataVector, null);

                }
        }

        protected boolean onSavePrompt() {
                        return true;
        }

		public boolean validateOrder() {

			int mktLot = getInteger(fnoOrderConfirmationBean.getMktLot(), "Invalid Mkt.Lot");

			if(mktLot==0) {
			        return false;
			}

			int qty = getInteger(txtQty.getText(), null);

			if(txtQty.getText().trim().length()==0) {
			        Dialog.alert("Please enter Qty value");
			        txtQty.setFocus();
			        return false;
			}

			if(qty<1) {
			        Dialog.alert("Order quantity cannot be zero or negative.");
			        txtQty.setFocus();
			        return false;
			}

			if((qty%mktLot)!=0) {
			        Dialog.alert("Qty is not in multiples of Market lot.");
			        txtQty.setFocus();
			        return false;
			}

			if(ddlOrderTypes.getSelectedIndex()==0) { //LIMIT
				if(txtPrice!=null ) {

				    float price = getFloat(txtPrice.getText(), null);

				    if(txtPrice.getText().trim().length()==0 || price==0.0f) {
				            Dialog.alert("Please enter Order Price value");
				                txtPrice.setFocus();
				                return false;
				   }

				}
			}
		
			float price = getFloat(txtPrice.getText(), null);
			float triggerPrice = getFloat(txtTriggerPrice.getText(), null);
			if(txtTriggerPrice.getText().trim().length()!=0 && ddlOrderTypes.getSelectedIndex()==0) {

				if(lblActionValue.getText().trim().toLowerCase().equals("buy")) {//Buy Action
	    			if(triggerPrice>price) {
	    				Dialog.alert("Trigger Price should be less than or equal to order price in case of Buy order.");
	    				txtTriggerPrice.setFocus();
	    				return false;
	    			}
	    		} else if (lblActionValue.getText().trim().toLowerCase().equals("sell")) {//Sell Action
	    			if(triggerPrice<price) {
	    				Dialog.alert("Trigger Price should be greater than or equal to order price in case of Sell / Short Sell order.");
	    				txtTriggerPrice.setFocus();
	    				return false;
	    			}
	    		}
			
			}
			
			return true;
		}

        public int getInteger(String targetString,String errorMessage) {
                try {
                        return Integer.parseInt(targetString);
                } catch(Exception ex) {
                        if(errorMessage!=null) {
                                Dialog.alert(errorMessage);
                        }
                }
                return 0;
        }

        public float getFloat(String targetString,String errorMessage) {
                try {
                        return Float.parseFloat(targetString);
                } catch(Exception ex) {
                        if(errorMessage!=null) {
                                Dialog.alert(errorMessage);
                        }
                }
                return 0.0f;
        }

        public void setData(Vector vector, int id) {
                // TODO Auto-generated method stub
        }

        public void actionPerfomed(byte Command, Object data) {
                switch(Command) {
                        case ActionCommand.CMD_FNO_TRADE:
                                ActionInvoker.processCommand(new Action(Command, data));
                                break;
                        case ActionCommand.CMD_FNO_TRADE_ORDER_CONFIRMATION:
                                ActionInvoker.processCommand(new Action(Command,data));
                                break;
                        default:
                                break;
                }
                
        }

        public void fieldChanged(Field field, int context) {
                try {
                        CustomBasicEditField targetField = (CustomBasicEditField) field;
                        if(targetField.getText().indexOf(".")!=-1) {
                                int lastIndex = targetField.getText().indexOf(".", targetField.getText().indexOf(".")+1);
                                if(lastIndex==-1) {
                                        int dotIndex = targetField.getText().indexOf(".");
                                        if((dotIndex+2)<(targetField.getText().length()-1)) {
                                                targetField.setText(targetField.getText().substring(0, dotIndex+3));
                                        }
                                        if(dotIndex==0) {
                                                targetField.setText("0"+targetField.getText());
                                        }
                                } else {
                                        targetField.setText("");
                                }
                        }
                } catch(Exception ex) {
                        LOG.print("Exception occured");
                }
        }

}
