package com.snapwork.view.trade;

import java.util.Hashtable;
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
import com.snapwork.beans.FNOTradeBean;
import com.snapwork.beans.HomeJson;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.CustomLabelField;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TradeScreenFNObanner;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FNOTradeConfiguration;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class FNOTradeTurnOverScreen  extends MainScreen implements ActionListener, RemovableScreen, ReturnDataWithId,FieldChangeListener, ReturnString {

	public final static byte ID_NONE_DDL = 0;
	public final static byte ID_INSTRUMENT_DDL = 1;
	public final static byte ID_EXPIRY_DDL = 2;
	public final static byte ID_ORDER_TYPE_DDL = 3;
	public final static byte ID_STRIKE_PRICE_FNO_DDL = 4;
	private String date;
	private String[] strikePrices;
	private ObjectChoiceField ddlOrderTypes;
	private BottomMenu bottomMenu = null;
	private ReturnString returnString = this;

	private boolean isFuturesScreen;
	private long time;

	public void setFuturesScreen(boolean isFuturesScreen) {
		this.isFuturesScreen = isFuturesScreen;
	}
	public boolean isFuturesScreen() {
		return isFuturesScreen;
	}

	private FNOTradeBean fnoTradeBean;
	public void setFnoTradeBean(FNOTradeBean fnoTradeBean) {
		this.fnoTradeBean = fnoTradeBean;
	}
	public FNOTradeBean getFnoTradeBean() {
		return fnoTradeBean;
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
	private String action;
	private String optType;
	private String orderType;
	private String strike;
	private String qty;
	private String priceTextHash;
	private String triggerTextHash;
	private Hashtable hasht;

	//Constructor
	public FNOTradeTurnOverScreen(FNOTradeBean fnoTradeBean,String title,FNOTradeConfiguration fnoTradeConfiguration,HomeJson bannerData, String date, Hashtable hasht) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		this.fnoTradeBean = fnoTradeBean;
		this.fnoTradeConfiguration = fnoTradeConfiguration;
		this.action = (String)hasht.get("action");
		this.optType = (String)hasht.get("optType");
		this.orderType = (String)hasht.get("orderType");
		this.strike=((String)hasht.get("strikePrice"));
		this.hasht = hasht;
		if((String)hasht.get("qty")==null)
			qty = "";
		else
			qty = (String)hasht.get("qty");
		if((String)hasht.get("limitPrice")==null)
			priceTextHash = "";
		else
			priceTextHash = (String)hasht.get("limitPrice");
		if((String)hasht.get("stopPrice")==null)
			triggerTextHash = "";
		else
			triggerTextHash = (String)hasht.get("stopPrice");
		this.screenTitle = title;
		this.bannerData = bannerData;
		this.date = date;
		time = System.currentTimeMillis();
		createUI();
	}

	//Component Declaration Start
	VerticalFieldManager vfm;

	//Banner
	private TradeScreenFNObanner banner;

	//Index Fields
	CustomLabelField lblIndexName,lblIndexValue;

	//Instrument Fields
	CustomLabelField lblInstrument;
	//ObjectChoiceField ddlInstruments;

	//Expiry Fields
	CustomLabelField lblExpiry;
	//ObjectChoiceField ddlExpiries;

	//Option Type Fields
	CustomLabelField lblOptionType;
	//ObjectChoiceField ddlOptionType;
	String[] optionTypes = {"Call","Put"};

	//Strike Price Fields
	CustomLabelField lblStrikePrice;
	//ObjectChoiceField ddlStrikePrice;

	//Action Fields
	CustomLabelField lblAction;
	//ObjectChoiceField ddlActions;
	String[] actions = {"Buy","Sell"};
	String[] actionPostValue = {"B","S"};

	//Market Lot Fields
	CustomLabelField lblMarketLot;
	CustomLabelField lblMarketLotValue;

	//Qty Fields
	CustomLabelField lblQty;
	CustomBasicEditField txtQty;

	//Order Type fields
	CustomLabelField lblOrderType;
	//ObjectChoiceField ddlOrderTypes;
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
			LOG.print("bannerData FNO Modify ----------->>>>>>>>>> : "+bannerData.getCompanyCode());
			if(bannerData.getCompanyCode().indexOf("_")>-1)
			{
				date = bannerData.getCompanyCode();
				date = date.substring(date.indexOf("_")+1,date.length());
				date = date.substring(0,date.length()>12?date.indexOf("_"):date.length());
			}
			banner = new TradeScreenFNObanner(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),bannerData.getDisplayName1(), Expiry.getText(date),bannerData.getCompanyCode(),true);
			banner.setData(bannerData.getLastTradedPrice(), bannerData.getChange(), (bannerData.getPercentageDiff().indexOf("-") == -1 ? "+"+bannerData.getPercentageDiff() : bannerData.getPercentageDiff()),bannerData.getOpenInterest(), bannerData.getVolume());
			bannerPrefHeight+=banner.getPreferredHeight();
		}
		//final int bannerHeight = banner.getPreferredHeight();//bannerPrefHeight;

		final int totalRows = (fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX ? 13 : (fnoTradeConfiguration.getFnoOrderType()==FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT ? 11 : 10));
		final Bitmap bmpTradeNow = ImageManager.getTradeNow();


		vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {

			private int xMargin = 10;
			private int yMargin = 10;
			private int xPadding = xMargin + 15;
			private int yPadding = yMargin + 5;
			//private int totalRows = 11;
			//private int managerHeight = ((FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4)*totalRows; 

			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, AppConstants.screenWidth, getHeight());
				graphics.setColor(0x333333);
				int difference = 20;
				//graphics.fillRoundRect(xMargin, yMargin-difference, AppConstants.screenWidth - xMargin*2 ,(((FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4)*((getFieldCount()+1)/2))-yMargin*2-difference+5,8,8
				//);
				int rowHeight = (FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4;
				
				graphics.fillRoundRect(xMargin, 0, AppConstants.screenWidth - xMargin*2 ,rowHeight*((getFieldCount()-1)/2)-6,8,8);
				graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				graphics.setColor(0xeeeeee);
				for (int i=0;i<(getFieldCount()-1)/2;i++) {
					// setPositionChild(getField(i*2), xPadding, (rowHeight*i)+yPadding+((rowHeight/2)-(getField(i*2).getHeight()/2)));
					graphics.drawText(":",  (AppConstants.screenWidth/2)-8, (rowHeight*i)+yPadding+((rowHeight/2)-(getField((i*2)+1).getHeight()/2))+((getField((i*2)+1).getHeight()/2)-(graphics.getFont().getHeight()/2))-difference);
				}
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
					int difference = 20;
					for (int i=0;i<(getFieldCount()-1)/2;i++) {
						setPositionChild(getField(i*2), xPadding, (rowHeight*i)+yPadding+((rowHeight/2)-(getField(i*2).getHeight()/2))-difference);
						setPositionChild(getField((i*2)+1),  AppConstants.screenWidth/2, (rowHeight*i)+yPadding+((rowHeight/2)-(getField((i*2)+1).getHeight()/2))-difference);
					}

					if(getField(getFieldCount()-1) instanceof BitmapField) {
						setPositionChild(getField(getFieldCount()-1), AppConstants.screenWidth/2 - bmpTradeNow.getWidth()/2, ((((FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4)*((getFieldCount()+1)/2))+yPadding*2)-difference*3-10);
						setExtent(AppConstants.screenWidth, (((FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4)*((getFieldCount()+1)/2))+bmpTradeNow.getHeight() + yPadding*3-(getField(getFieldCount()-1).getPreferredHeight()*2));
					} else {
						setExtent(AppConstants.screenWidth, (((FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4)*((getFieldCount()+1)/2))-(getField(getFieldCount()-1).getPreferredHeight()*2));
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
				setExtent(AppConstants.screenWidth, banner.getPreferredHeight());
			}

		};
		if(banner!=null) {
			hfm.add(banner);
		}

		TextFilter decimalFilter = new NumericTextFilter(NumericTextFilter.ALLOW_DECIMAL);
		TextFilter intFiler = TextFilter.get(TextFilter.INTEGER);
		//Initialize components
		boolean isStock = false;
		if(fnoTradeBean.getFlag().trim().toLowerCase().equals("stock")) {
			isStock = true;
		} else if(fnoTradeBean.getFlag().trim().toLowerCase().equals("index")) {
			isStock = false;
		}

		if(lblIndexName==null) {
			lblIndexName = new CustomLabelField((isStock == true ? "Stock Name" : "Index Name"), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		if(lblIndexValue == null) {
			lblIndexValue = new CustomLabelField(fnoTradeBean.getSymbolName(),0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		vfm.add(lblIndexName);
		vfm.add(lblIndexValue);

		if(lblInstrument==null) {
			lblInstrument = new CustomLabelField("Instrument", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		vfm.add(lblInstrument);
		//String[] instruments = {"FUT"+(isStock == true ? "STK" : "IDX" ),"OPT"+(isStock == true ? "STK" : "IDX" )};
		CustomLabelField lblInstrumentLocal = new CustomLabelField(/*instruments[fnoTradeConfiguration.getFnoTradeType()]*/fnoTradeBean.getIndexName(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		vfm.add(lblInstrumentLocal);

		if(lblExpiry==null) {
			lblExpiry = new CustomLabelField("Expiry", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		vfm.add(lblExpiry);
		CustomLabelField ddlExpiriesLocal = new CustomLabelField(date, 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		vfm.add(ddlExpiriesLocal);
			if(fnoTradeConfiguration.getFnoTradeType() == FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX){
				if(lblOptionType==null) {
					lblOptionType = new CustomLabelField("Option Type", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				}
				vfm.add(lblOptionType);
				CustomLabelField ddlOptionTypeLocal = new CustomLabelField(optType, 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				vfm.add(ddlOptionTypeLocal);
				CustomLabelField lblStrikePriceLocal = new CustomLabelField("Strike Price", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				vfm.add(lblStrikePriceLocal);
				lblStrikePrice = new CustomLabelField(strike, 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				vfm.add(lblStrikePrice);		
		}

		if(lblAction==null) {
			lblAction = new CustomLabelField("Action", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		vfm.add(lblAction);
		if(action==null)
			action="";
		/*else
			action = actions[action.equalsIgnoreCase("B")?0:1];*/
		CustomLabelField ddlActionsLocal = new CustomLabelField(actions[action.equalsIgnoreCase("B")?0:1], 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		vfm.add(ddlActionsLocal);

		if(lblMarketLot==null) {
			lblMarketLot = new CustomLabelField("Mkt. Lot", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		if(lblMarketLotValue == null) {
			/*if((String)hasht.get("mkt_lot") == null)
				mktlot = "0";
			fnoTradeBean.setMinLot(mktlot);*/
			lblMarketLotValue = new CustomLabelField(fnoTradeBean.getMinLot(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		vfm.add(lblMarketLot);
		vfm.add(lblMarketLotValue);

		if(lblQty==null) {
			lblQty = new CustomLabelField("Qty", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		if(txtQty==null) {
			txtQty = new CustomBasicEditField(CustomBasicEditField.NO_NEWLINE);
			txtQty.setFilter(intFiler);
			//txtQty.setText(fnoTradeBean.getMinLot()==null?"":fnoTradeBean.getMinLot());
			txtQty.setText(qty);
		}
		vfm.add(lblQty);
		vfm.add(txtQty);

		if(lblOrderType==null) {
			lblOrderType = new CustomLabelField("Order Type", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		vfm.add(lblOrderType);
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
		/*if(orderType==null)
		{
			orderType="limit";
		}
		CustomLabelField ddlOrderTypesLocal = new CustomLabelField(orderType.toUpperCase(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		vfm.add(ddlOrderTypesLocal);*/
		vfm.add(ddlOrderTypes);

		if(fnoTradeConfiguration.getFnoOrderType()==FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT) {
			if(lblPrice == null) {
				lblPrice = new CustomLabelField("Price", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
			}
			if(txtPrice == null) {
				txtPrice = new CustomBasicEditField(CustomBasicEditField.NO_NEWLINE| CustomBasicEditField.FILTER_REAL_NUMERIC);
				//orderprice
				txtPrice.setText(priceTextHash);
				txtPrice.setFilter(decimalFilter);
				txtPrice.setChangeListener(this);
			}
			vfm.add(lblPrice);
			vfm.add(txtPrice);
		}

		if(lblTriggerPrice==null) {
			lblTriggerPrice = new CustomLabelField("Trigger Price", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		if(txtTriggerPrice == null) {
			txtTriggerPrice = new CustomBasicEditField(CustomBasicEditField.NO_NEWLINE| CustomBasicEditField.FILTER_REAL_NUMERIC);
			txtTriggerPrice.setText(triggerTextHash);
			txtTriggerPrice.setFilter(decimalFilter);
			txtTriggerPrice.setChangeListener(this);
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
				submitModifyFNOOrder();
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
				graphics.drawText(strTradeNowButton,(bmpTradeNow.getWidth()/2)-(getFont().getAdvance(strTradeNowButton.replace(' ', '_'))/2),(bmpTradeNow.getHeight()/2)-(getFont().getHeight()/2));
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
		add(Utils.BlankField(8, AppConstants.screenWidth, 0x000000));
		add(vfm);

		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
		//final FNOTradeModify robj = this;
		// UiApplication.getUiApplication().invokeLater(new Runnable() {
            // public void run() {
             	//ScreenInvoker.removeRemovableScreen();  
		if(!(UiApplication.getUiApplication().getActiveScreen() instanceof FNOTradeTurnOverScreen))
        {
			ScreenInvoker.pushScreen(this, false, true);
			/*if(ddlInstruments!=null)
			{
				//ddlInstruments.setEditable(false);
				CustomLabelField lblInstrumentLocal = new CustomLabelField(ddlStrikePrice.getChoice(ddlStrikePrice.getSelectedIndex()).toString(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				vfm.replace(ddlInstruments, lblInstrumentLocal);
			}
			if(ddlExpiries!=null)
			{
				//ddlExpiries.setEditable(false);
				CustomLabelField ddlExpiriesLocal = new CustomLabelField(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				vfm.replace(ddlExpiries, ddlExpiriesLocal);
			}
			if(ddlOptionType!=null)
			{
				//ddlOptionType.setEditable(false);
				CustomLabelField ddlOptionTypeLocal = new CustomLabelField(ddlOptionType.getChoice(ddlOptionType.getSelectedIndex()).toString(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				vfm.replace(ddlOptionType, ddlOptionTypeLocal);
			}
			if(ddlStrikePrice!=null)
			{
				//ddlStrikePrice.setEditable(false);
				CustomLabelField ddlStrikePriceLocal = new CustomLabelField(ddlStrikePrice.getChoice(ddlStrikePrice.getSelectedIndex()).toString(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				vfm.replace(ddlStrikePrice, ddlStrikePriceLocal);
			}
			if(ddlActions!=null)
			{
				//ddlActions.setEditable(false);
				CustomLabelField ddlActionsLocal = new CustomLabelField(ddlActions.getChoice(ddlActions.getSelectedIndex()).toString(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				vfm.replace(ddlActions, ddlActionsLocal);
			}*/
        } 
            // }
     //});
	}

	public ObjectChoiceField getCustomObjectChoiceFieldReg(String text,String[] values,final int firstIndex,long style,final byte dropDownID) {

		return new ObjectChoiceField(text, values,firstIndex, ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT | ObjectChoiceField.FORCE_SINGLE_LINE) {

			protected void fieldChangeNotify(int context)
			{
				if(time+20<System.currentTimeMillis())
				dropDownChanged(dropDownID,context);

			}
			public int getPreferredWidth() {
				return (AppConstants.screenWidth/2)-20;
			}
			public int getPreferredHeight() {
				return  FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight();
			}
			protected void layout(int width, int height) {
				super.layout(getPreferredWidth(), getPreferredHeight());
				//setExtent(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance("TradingPassw") + Snippets.padding*2, height);  // force the field to use all available space
			}
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
		else if(key == Keypad.KEY_END)
		{
			LOG.print("KEY_END EXIT from app");
			System.exit(0);
		}
		else
			return super.keyDown(keyCode, time);

		return true;
	}

	public void dropDownChanged(byte dropDownID, int index) {
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
			else
				if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX) {
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
		/*if(dropDownID==ID_INSTRUMENT_DDL) {
			String sbf = bannerData.getCompanyCode();
			String nw = "";
			nw = sbf.substring(0, sbf.indexOf("_")+1) + ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString();
				LOG.print(nw+" \n "+ Utils.getCompanyLatestTradingDetailsURL(nw));
				new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(nw),1233,returnString,false);
		} else if(dropDownID == ID_EXPIRY_DDL) {
			LOG.print(fnoTradeConfiguration.getFnoTradeType() + "Inside EXPIRY -> ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString() : "+ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
			String sbf = bannerData.getCompanyCode();
			String nw = "";
			if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX)
			{
				nw = sbf.substring(0, sbf.indexOf("_")+1) + ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString();
				LOG.print(nw+" \n "+ Utils.getCompanyLatestTradingDetailsURL(nw));
				new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(nw),1212,returnString,false);
			}
			else if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX)
			{
				LOG.print("OPTIONS");
				nw = sbf.substring(0, sbf.indexOf("_")+1) + ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString()+"_"+strikePrices[ddlStrikePrice.getSelectedIndex()]+"_"+optionTypes[ddlOptionType.getSelectedIndex()];
				LOG.print(nw+" \n "+ Utils.getCompanyLatestTradingDetailsURL(nw));
				new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(nw),1213,returnString,false);
			}
		} else if(dropDownID == ID_ORDER_TYPE_DDL) {
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
		
		else if( dropDownID == ID_STRIKE_PRICE_FNO_DDL)
		{
			String sbf = bannerData.getCompanyCode();
			String nw = "";
			LOG.print("OPTIONS STRIKE PRICE");
				nw = sbf.substring(0, sbf.indexOf("_")+1) + date+"_"+strikePrices[strike]+"_"+optionTypes[ddlOptionType.getSelectedIndex()];
				LOG.print(nw+" \n "+ Utils.getCompanyLatestTradingDetailsURL(nw));
				new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(nw),1213,returnString,false);
		}*/
	}

	public void submitModifyFNOOrder() {

		if(validateOrder()) {

			FNOOrderConfirmationBean confirmationBean = new FNOOrderConfirmationBean();
			confirmationBean.setCompCodeRefresh(fnoTradeConfiguration.getCompanyCode());
			if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX) {
				confirmationBean.setOptionType("");
				confirmationBean.setStrikePrice("0");
			} else if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX) {
				confirmationBean.setOptionType(optType);
				confirmationBean.setStrikePrice(strike);
			}

			confirmationBean.setSymbolName(fnoTradeBean.getSymbolName());
			confirmationBean.setCustId(UserInfo.getUserID());
			confirmationBean.setMktLot(fnoTradeBean.getMinLot());
			confirmationBean.setLtp(bannerData.getLastTradedPrice());

			confirmationBean.setExpiry(date);

			
			//Action Are different from UI and post fields 
			confirmationBean.setAction(action);

			confirmationBean.setQty(txtQty.getText());
			confirmationBean.setOrderType(orderTypes[ddlOrderTypes.getSelectedIndex()].toLowerCase());

			if(ddlOrderTypes.getSelectedIndex()==0) {
				if(txtPrice!=null) {
					confirmationBean.setLimitPrice((txtPrice.getText().trim().length()==0 ? "0" : txtPrice.getText()));
				} else {
					confirmationBean.setLimitPrice("0");
				}	
			} else {
				confirmationBean.setLimitPrice("0");
			}

			confirmationBean.setStopPrice((txtTriggerPrice.getText().trim().length()==0 ? "0" : txtTriggerPrice.getText()));
			confirmationBean.setValidity(ddlValidity.getChoice(ddlValidity.getSelectedIndex()).toString());

			confirmationBean.setExchange("NSEFO");
			confirmationBean.setType("NOR");
			confirmationBean.setDiscQty("0");
			confirmationBean.setBannerData(bannerData.copy());
			confirmationBean.setBannerDate(date);
			confirmationBean.setInstType(fnoTradeBean.getIndexName());
			confirmationBean.setPerChange(bannerData.getChangePercent());
			confirmationBean.setChange(bannerData.getChange());
			confirmationBean.setCustId(UserInfo.getUserID());
			if((String)hasht.get("order_id")==null)
				confirmationBean.setOrderNumber("");
			else
				confirmationBean.setOrderNumber((String)hasht.get("order_id"));
			if((String)hasht.get("order_id")==null)
				confirmationBean.setOrderNumber("");
			else
				confirmationBean.setOrderNumber((String)hasht.get("order_id"));
			if((String)hasht.get("order_id")==null)
				confirmationBean.setOrderNumber("");
			else
				confirmationBean.setOrderNumber((String)hasht.get("order_id"));
			if((String)hasht.get("order_qty")==null)
				confirmationBean.setEQ("");
			else
				confirmationBean.setEQ((String)hasht.get("order_qty"));
			if((String)hasht.get("rmscode")==null)
				confirmationBean.setRmsCode("");
			else
				confirmationBean.setRmsCode((String)hasht.get("rmscode"));
			if((String)hasht.get("token")==null)
				confirmationBean.setToken("");
			else
				confirmationBean.setToken((String)hasht.get("token"));
			
			Vector commandDataVector = new Vector();
			commandDataVector.addElement(confirmationBean);
			commandDataVector.addElement(new Integer(1));
			//Vector dataUrl = new Vector();
			//String url = AppConstants.domainUrl + "FOreports.php?custId="+UserInfo.getUserID()+"&rtype=trade&debug=2";
			/*String url = Utils.getFNOPostSQ4TradeURL(confirmationBean);
			LOG.print(url);
			dataUrl.addElement("tradenowf");
			dataUrl.addElement(url);*/
			ActionInvoker.processCommand(new Action(ActionCommand.CMD_FNO_TRADE_TURNOVER_CONFIRMATION,commandDataVector));
			//actionPerfomed(ActionCommand.CMD_FNO_TRADE_ORDER_CONFIRM_POST, commandDataVector);

		}
	}

	protected boolean onSavePrompt() {
		return true;
	}

	public boolean validateOrder() {

		int mktLot = getInteger(fnoTradeBean.getMinLot(), "Invalid Mkt.Lot");

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
		/*if(ddlOrderTypes.getSelectedIndex()==0) { //LIMIT
			if(txtPrice!=null ) {

				float price = getFloat(txtPrice.getText(), null);

				if(txtPrice.getText().trim().length()==0 || price==0.0f) {
					Dialog.alert("Please enter Order Price value");
					txtPrice.setFocus();
					return false;
				}

			}
		}*/

		float price = getFloat(txtPrice.getText(), null);
		float triggerPrice = getFloat(txtTriggerPrice.getText(), null);
		if(txtTriggerPrice.getText().trim().length()!=0 && ddlOrderTypes.getSelectedIndex()==0 ) {
			if(!(txtTriggerPrice.getText().equalsIgnoreCase("0"))){

			if(action.equalsIgnoreCase("B")) {//Buy Action
				if(triggerPrice>price) {
					Dialog.alert("Trigger Price should be less than or equal to order price in case of Buy order.");
					txtTriggerPrice.setFocus();
					return false;
				}
			} else if (action.equalsIgnoreCase("S")) {//Sell Action
				if(triggerPrice<price) {
					Dialog.alert("Trigger Price should be greater than or equal to order price in case of Sell / Short Sell order.");
					txtTriggerPrice.setFocus();
					return false;
				}
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
		case ActionCommand.CMD_FNO_TRADE_TURNOVER_CONFIRMATION:
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
	public void setReturnString(String string, int id) {
		if(id == 1233)
		{
			Vector v = HomeJsonParser.getVector(string);
			bannerData = (HomeJson) v.elementAt(0);
			banner.setName(bannerData.getSymbol());
			//date = Expiry.getText(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
			banner.setSource(date);
			banner.setComp(bannerData.getCompanyCode());
			banner.setData(bannerData.getLastTradedPrice(), bannerData.getChange(), (bannerData.getPercentageDiff().indexOf("-") == -1 ? "+"+bannerData.getPercentageDiff() : bannerData.getPercentageDiff()),bannerData.getOpenInterest(), bannerData.getVolume());
			banner.refresh();
		}
		else if(id ==1213)
		{
			LOG.print("   startOptionsScreen  ");
			Vector v = HomeJsonParser.getVector(string);
			bannerData = (HomeJson) v.elementAt(0);
			banner.setName(bannerData.getSymbol());
			//date = Expiry.getText(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
			banner.setSource(date);
			banner.setComp(bannerData.getCompanyCode());
			banner.setData(bannerData.getLastTradedPrice(), bannerData.getChange(), (bannerData.getPercentageDiff().indexOf("-") == -1 ? "+"+bannerData.getPercentageDiff() : bannerData.getPercentageDiff()),bannerData.getOpenInterest(), bannerData.getVolume());
			banner.refresh();
			//startOptionsScreen();
		}
		else
			if(id ==1212) { 
				Vector v = HomeJsonParser.getVector(string);
				bannerData = (HomeJson) v.elementAt(0);

				if(bannerData!=null) {
					//date = Expiry.getText(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
					LOG.print(bannerData.getLastTradedPrice()+"banner update : "+date);

					//banner = new TradeScreenFNObanner(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),bannerData.getSymbol(), date,bannerData.getCompanyCode(),true);
					banner.setName(bannerData.getSymbol());
					banner.setSource(date);
					banner.setComp(bannerData.getCompanyCode());
					banner.setData(bannerData.getLastTradedPrice(), bannerData.getChange(), (bannerData.getPercentageDiff().indexOf("-") == -1 ? "+"+bannerData.getPercentageDiff() : bannerData.getPercentageDiff()),bannerData.getOpenInterest(), bannerData.getVolume());
					banner.refresh();

				}
				else
					LOG.print("BannerData null");

			}
	}

	/*public void startOptionsScreen()
	{
		if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX) {

            if(!ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).equals(fnoTradeConfiguration.getFnoExpiryDate())) {

                    fnoTradeConfiguration.setFnoExpiryDate(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
                    String sbf = bannerData.getCompanyCode();
                    String infoURL = Utils.getFNOExpiryAndStrikeDataURL(sbf.substring(0, sbf.indexOf("_")+1) + ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString()+"_"+strikePrices[ddlStrikePrice.getSelectedIndex()]+"_"+optionTypes[ddlOptionType.getSelectedIndex()]);

                    Vector dataVector = new Vector();

                    dataVector.addElement(infoURL);
            dataVector.addElement(fnoTradeBean.getIndexName());
            fnoTradeConfiguration.setFnoExpiryDate(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
            dataVector.addElement(fnoTradeConfiguration);
    		if(bannerData!=null) {
    			dataVector.addElement(bannerData);
    		}
    		dataVector.addElement(date);
            actionPerfomed(ActionCommand.CMD_FNO_TRADE, dataVector);
            }
    }
	}*/	
	
	/*public void id_instrument_ddl()
	{
		//if(ddlInstruments.getSelectedIndex()!=fnoTradeConfiguration.getFnoTradeType()) {
	//fnoTradeConfiguration.setFnoTradeType((byte)ddlInstruments.getSelectedIndex());
		//fnoTradeConfiguration.setFnoExpiryDate(null);

		String infoURL = "";

		if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX) {
			infoURL = Utils.getFNOExpiryAndStrikeDataURL(fnoTradeBean.getIndexName()+"_"+date);
		} else if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX) {
			infoURL = Utils.getFNOExpiryAndStrikeDataURL(fnoTradeBean.getIndexName()+"_"+date+"_"+strike+"_"+optType);
		}

		Vector dataVector = new Vector();

		dataVector.addElement(infoURL);
		dataVector.addElement(fnoTradeBean.getIndexName());
		//fnoTradeConfiguration.setFnoExpiryDate(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
		dataVector.addElement(fnoTradeConfiguration);

		////////////////////////////////////
		// fnoTradeConfiguration.setFnoExpiryDate(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
		// dataVector.addElement(fnoTradeConfiguration);
		if(bannerData!=null) {
			dataVector.addElement(bannerData);
		}
		dataVector.addElement(date);
		////////////////////////////////////

		actionPerfomed(ActionCommand.CMD_FNO_TRADE, dataVector);
	//}                    
	}*/
		
}
