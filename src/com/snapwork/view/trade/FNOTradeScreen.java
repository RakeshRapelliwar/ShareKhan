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
import com.snapwork.components.AutoRefreshableScreen;
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
import com.snapwork.util.AutoScreenRefresherThread;
import com.snapwork.util.FNOTradeConfiguration;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

public class FNOTradeScreen  extends MainScreen implements ActionListener, RemovableScreen, ReturnDataWithId,FieldChangeListener, ReturnString ,AutoRefreshableScreen{

	public final static byte ID_NONE_DDL = 0;
	public final static byte ID_INSTRUMENT_DDL = 1;
	public final static byte ID_EXPIRY_DDL = 2;
	public final static byte ID_ORDER_TYPE_DDL = 3;
	public final static byte ID_OPTION_TYPE_DDL = 5;
	public final static byte ID_STRIKE_PRICE_FNO_DDL = 4;
	public static Vector storeForModify;
	public static boolean storeForModifyFlag;
	public static Hashtable storeForModifyHash;
	public static String InstrumentR = "";
	public static String expiryTextR = "";
	public static String optionR = "";
	public static String strikpR = "";
	public static String textDate = "";
	private String date;
	private String[] strikePrices;

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
	private static HomeJson bannerData;

	//Constructor
	public FNOTradeScreen(FNOTradeBean fnoTradeBean,String title,FNOTradeConfiguration fnoTradeConfiguration,HomeJson bannerData, String date) {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		this.fnoTradeBean = fnoTradeBean;
		this.fnoTradeConfiguration = fnoTradeConfiguration;
		this.screenTitle = title;
		this.date = date;
		time = System.currentTimeMillis();
		if(storeForModifyHash == null)
		{
			storeForModifyHash = new Hashtable();
			this.bannerData = bannerData;
		}
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
	CustomLabelField lblInstrumentR;
	ObjectChoiceField ddlInstruments;

	//Expiry Fields
	CustomLabelField lblExpiry;
	CustomLabelField lblExpiryR;
	ObjectChoiceField ddlExpiries;

	//Option Type Fields
	CustomLabelField lblOptionType;
	CustomLabelField lblOptionTypeR;
	ObjectChoiceField ddlOptionType;
	String[] optionTypes = {"Call","Put"};

	//Strike Price Fields
	CustomLabelField lblStrikePrice;
	CustomLabelField lblStrikePriceR;
	ObjectChoiceField ddlStrikePrice;

	//Action Fields
	CustomLabelField lblAction;
	ObjectChoiceField ddlActions;
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
			String d = Expiry.getText(date);
			if(FNOTradeScreen.storeForModifyFlag)
			{
				d = textDate;
			}
			else
				textDate = d;
			banner = new TradeScreenFNObanner(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),bannerData.getSymbol(), d,bannerData.getCompanyCode(),true);
			banner.setData(bannerData.getLastTradedPrice(), bannerData.getChange(), (bannerData.getPercentageDiff().indexOf("-") == -1 ? "+"+bannerData.getPercentageDiff() : bannerData.getPercentageDiff()),bannerData.getOpenInterest(), bannerData.getVolume());
			bannerPrefHeight+=banner.getPreferredHeight();
			banner.setLeftMargin(9);
			banner.setRightMargin(8);
		}
		final int bannerHeight = bannerPrefHeight;

		final int totalRows = (fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX ? 13 : (fnoTradeConfiguration.getFnoOrderType()==FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT ? 11 : 10));
		final Bitmap bmpTradeNow = ImageManager.getTradeNow();

LOG.print("FNOTradeScreen");
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
				int difference = 20;
				graphics.fillRoundRect(xMargin, yMargin-difference, AppConstants.screenWidth - xMargin*2 ,managerHeight-yMargin*2-difference+5,8,8);
				graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				graphics.setColor(0xeeeeee);
				int rowHeight = (FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4;
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
						setPositionChild(getField(getFieldCount()-1), AppConstants.screenWidth/2 - bmpTradeNow.getWidth()/2, (managerHeight+yPadding*2)-difference*3-10);
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
		LOG.print("FNOTradeScreen 1");
		TextFilter decimalFilter = new NumericTextFilter(NumericTextFilter.ALLOW_DECIMAL);
		TextFilter intFiler = TextFilter.get(TextFilter.INTEGER);
		//Initialize components
		boolean isStock = false;
		if(fnoTradeBean.getFlag().trim().toLowerCase().equals("stock")) {
			isStock = true;
		} else if(fnoTradeBean.getFlag().trim().toLowerCase().equals("index")) {
			isStock = false;
		}
		LOG.print("FNOTradeScreen 2");
		if(lblIndexName==null) {
			lblIndexName = new CustomLabelField((isStock == true ? "Stock Name" : "Index Name"), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		if(lblIndexValue == null) {
			lblIndexValue = new CustomLabelField(fnoTradeBean.getIndexName(),0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		vfm.add(lblIndexName);
		vfm.add(lblIndexValue);

		if(lblInstrument==null) {
			lblInstrument = new CustomLabelField("Instrument", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		vfm.add(lblInstrument);
		if(FNOTradeScreen.storeForModifyFlag)
		{
			if(lblInstrumentR==null)
			lblInstrumentR = new CustomLabelField(InstrumentR, 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
			vfm.add(lblInstrumentR);
		}
		else{
		if(ddlInstruments==null) {

			int tradeSelectedIndex = 0;
			if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX) {
				tradeSelectedIndex = 0;
			} else if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX) {
				tradeSelectedIndex = 1;
			}

			String[] instruments = {"FUT"+(isStock == true ? "STK" : "IDX" ),"OPT"+(isStock == true ? "STK" : "IDX" )};

			ddlInstruments = getCustomObjectChoiceFieldReg("", instruments, 0, 0,ID_INSTRUMENT_DDL);
			ddlInstruments.setSelectedIndex(tradeSelectedIndex);
		}

		vfm.add(ddlInstruments);
		}
		LOG.print("FNOTradeScreen 3");
		if(lblExpiry==null) {
			lblExpiry = new CustomLabelField("Expiry", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		vfm.add(lblExpiry);
		if(FNOTradeScreen.storeForModifyFlag)
		{
			if(lblExpiryR==null) 
				lblExpiryR = new CustomLabelField(expiryTextR, 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				vfm.add(lblExpiryR);
		}else{
		if(ddlExpiries==null) {

			String[] expiries = new String[fnoTradeBean.getExpiryData().size()];

			for(int i=0;i<expiries.length;i++) {
				expiries[i] = fnoTradeBean.getExpiryData().elementAt(i).toString();
			}

			int selectedExpiryIndex = 0;
			for(int i=0;i<expiries.length;i++) {
				if(fnoTradeConfiguration.getFnoExpiryDate()==null) {
					fnoTradeConfiguration.setFnoExpiryDate(expiries[0]);
					selectedExpiryIndex = 0;
					i=expiries.length;
				}
				else {
					if(fnoTradeConfiguration.getFnoExpiryDate().equals(expiries[i])) {
						selectedExpiryIndex = i;
						i=expiries.length;
					}
				}
			}

			ddlExpiries = getCustomObjectChoiceFieldReg("", expiries, 0, 0,ID_EXPIRY_DDL);
			ddlExpiries.setSelectedIndex(selectedExpiryIndex);
		}
		vfm.add(ddlExpiries);
		}
		LOG.print("FNOTradeScreen 4");
		boolean flag = false;
		if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX) {
			flag = true;
			if(lblOptionType==null) {
				lblOptionType = new CustomLabelField("Option Type", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
			}
			vfm.add(lblOptionType);
			if(FNOTradeScreen.storeForModifyFlag)
			{
				String s = optionR;
				lblOptionTypeR = new CustomLabelField(s, 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				vfm.add(lblOptionTypeR);
			}
			else{
			if(ddlOptionType==null) {
				LOG.print("(fnoTradeConfiguration.getOptioncepeType()==FNOTradeConfiguration.FNO_OPTION_TYPE_CE?0:1) "+(fnoTradeConfiguration.getOptioncepeType()==FNOTradeConfiguration.FNO_OPTION_TYPE_CE?0:1));
				ddlOptionType = getCustomObjectChoiceFieldReg("", optionTypes, 0, 0,ID_OPTION_TYPE_DDL);
				ddlOptionType.setSelectedIndex((fnoTradeConfiguration.getOptioncepeType()==FNOTradeConfiguration.FNO_OPTION_TYPE_CE?0:1));
			}
			vfm.add(ddlOptionType);
			}
			LOG.print("FNOTradeScreen 5");
			if(lblStrikePrice==null) {
				lblStrikePrice = new CustomLabelField("Strike Price", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
			}
			
			/*if(strikePrices==null)
			{
				strikePrices = new String[1];
				strikePrices[0] = "0";
				//flag = true;
			}*/
			vfm.add(lblStrikePrice);
			
			
			if(FNOTradeScreen.storeForModifyFlag)
			{
				String s = strikpR;
				lblStrikePriceR = new CustomLabelField(s, 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
				vfm.add(lblStrikePriceR);
				ddlStrikePrice=null;
				fnoTradeConfiguration.setFnoStrikePriceValue(s);
			}
			else{if(ddlStrikePrice==null) {

				 strikePrices = new String[fnoTradeBean.getStrikeData().size()];

				for(int i=0;i<strikePrices.length;i++) {
					strikePrices[i] = fnoTradeBean.getStrikeData().elementAt(i).toString();
				}

				int selectedSPIndex = 0;
				if(fnoTradeConfiguration.getFnoStrikePriceValue()==null) {
					LOG.print("fnoTradeConfiguration : ONE");
					fnoTradeConfiguration.setFnoStrikePriceValue(strikePrices[0]);
					selectedSPIndex = 0;
				} 
				else if(fnoTradeConfiguration.getFnoStrikePriceValue().equals("-1")) 
				{
					LOG.print("fnoTradeConfiguration : TWO");
					fnoTradeConfiguration.setFnoStrikePriceValue(strikePrices[0]);
					selectedSPIndex = 0;
				}
				else {
					
					double first = Double.parseDouble(fnoTradeConfiguration.getFnoStrikePriceValue());
					int i;
					LOG.print("FIRST : "+first);
					for(i=0;i<strikePrices.length;i++) {
					double second = Double.parseDouble(strikePrices[i]);
					if(first == second) {
						LOG.print("fnoTradeConfiguration : THREE "+strikePrices[i]);
						fnoTradeConfiguration.setFnoStrikePriceValue(strikePrices[i]);
						selectedSPIndex = i;
						break;
						//i=strikePrices.length;
					}
					}
					if(i==strikePrices.length)
					{
						LOG.print("fnoTradeConfiguration : FOUR");
						String mainstring = bannerData.getCompanyCode();
						int len = mainstring.indexOf("_")+1;
						String string = mainstring.substring(len, mainstring.length());
						len = len + string.indexOf("_")+1;
						string = string.substring(len+1, string.length());
						//if(strikePrices[0].indexOf(".")<2)
						mainstring = mainstring.substring(0, len)+strikePrices[0]+string;
						fnoTradeConfiguration.setFnoStrikePriceValue(strikePrices[0]);
						new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(mainstring),121212,returnString,false);
					}
				}
				ddlStrikePrice = getCustomObjectChoiceFieldReg("", strikePrices, 0, 0,ID_STRIKE_PRICE_FNO_DDL);
				

				ddlStrikePrice.setSelectedIndex(selectedSPIndex);
			}
			vfm.add(ddlStrikePrice);
		}
			/*if(storeForModifyHash.get("ddlStrikePrice")!=null)
			{
				String s = (String)storeForModifyHash.get("ddlStrikePrice");
				for(int i=0; i<ddlStrikePrice.getSize(); i++)
				{
					ddlStrikePrice.get
				}
				ddlOptionType.setSelectedIndex(s.equalsIgnoreCase("CE")?0:1);
			}*/
		}
		LOG.print("FNOTradeScreen 6");
		if(lblAction==null) {
			lblAction = new CustomLabelField("Action", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		if(ddlActions==null) {
			ddlActions = getCustomObjectChoiceFieldReg("", actions, 0, 0,ID_NONE_DDL);
		}
		vfm.add(lblAction);
		vfm.add(ddlActions);
		if(storeForModifyHash.get("actionPostValue")!=null)
		{
			String s = (String)storeForModifyHash.get("actionPostValue");
			LOG.print(" ACTION : "+s);
			ddlActions.setSelectedIndex(s.equalsIgnoreCase("0")?0:1);
			//ddlActions.setSelectedIndex(s.equalsIgnoreCase("B")?0:1);
		}

		if(lblMarketLot==null) {
			lblMarketLot = new CustomLabelField("Mkt. Lot", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		if(lblMarketLotValue == null) {
			lblMarketLotValue = new CustomLabelField(fnoTradeBean.getMinLot(), 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		vfm.add(lblMarketLot);
		vfm.add(lblMarketLotValue);
		LOG.print("FNOTradeScreen 7");
		if(lblQty==null) {
			lblQty = new CustomLabelField("Qty", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		if(txtQty==null) {
			txtQty = new CustomBasicEditField(CustomBasicEditField.NO_NEWLINE);
			txtQty.setFilter(intFiler);
			txtQty.setText(fnoTradeBean.getMinLot());
		}
		vfm.add(lblQty);
		vfm.add(txtQty);

		if(lblOrderType==null) {
			lblOrderType = new CustomLabelField("Order Type", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		if(ddlOrderTypes == null) {
			int tradeOrderSelectedIndex = 0;
			if(storeForModifyHash.get("lblOrderType")!=null)
			{
				String s = (String)storeForModifyHash.get("lblOrderType");
				tradeOrderSelectedIndex = s.equalsIgnoreCase("0")?0:1;
			}
			else{
			if(fnoTradeConfiguration.getFnoOrderType()==FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT) {
				tradeOrderSelectedIndex = 0;
			} else if(fnoTradeConfiguration.getFnoOrderType()==FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_MARKET) {
				tradeOrderSelectedIndex = 1;
			}
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
			}
			vfm.add(lblPrice);
			vfm.add(txtPrice);
			if(storeForModifyHash.get("txtPrice")!=null)
			{
				txtPrice.setText((String)storeForModifyHash.get("txtPrice"));
			}
		}

		if(lblTriggerPrice==null) {
			lblTriggerPrice = new CustomLabelField("Trigger Price", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		if(txtTriggerPrice == null) {
			txtTriggerPrice = new CustomBasicEditField(CustomBasicEditField.NO_NEWLINE| CustomBasicEditField.FILTER_REAL_NUMERIC);
			txtTriggerPrice.setFilter(decimalFilter);
			txtTriggerPrice.setChangeListener(this);
		}
		vfm.add(lblTriggerPrice);
		vfm.add(txtTriggerPrice);
		if(storeForModifyHash.get("txtPrice")!=null)
		{
			txtPrice.setText((String)storeForModifyHash.get("txtPrice"));
		}
		if(storeForModifyHash.get("txtTriggerPrice")!=null)
		{
			txtTriggerPrice.setText((String)storeForModifyHash.get("txtTriggerPrice"));
		}
//if()//Set price triggerprice strike price ce pe if modify true
		if(lblValidity == null) {
			lblValidity = new CustomLabelField("Validity", 0, Color.WHITE, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		}
		if(ddlValidity == null) {
			ddlValidity = getCustomObjectChoiceFieldReg("", validities, 0, 0,ID_NONE_DDL);
		}
		vfm.add(lblValidity);
		vfm.add(ddlValidity);
		
		if(storeForModifyHash.get("validity")!=null)
		{
			if(((String)storeForModifyHash.get("validity")).equalsIgnoreCase("1"))
				ddlValidity.setSelectedIndex(1);
		}
		LOG.print("FNOTradeScreen 8");
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
		AutoScreenRefresherThread.startThread();
		refreshFields();
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
	}

	public ObjectChoiceField getCustomObjectChoiceFieldReg(String text,String[] values,final int firstIndex,long style,final byte dropDownID) {

		return new ObjectChoiceField(text, values,firstIndex, ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT | ObjectChoiceField.FORCE_SINGLE_LINE) {

			protected void fieldChangeNotify(int context)
			{
				if(time+20<System.currentTimeMillis())
				dropDownChanged(dropDownID,context);

			}
			public int getPreferredWidth() {
				if(AppConstants.OS>5)
					return	(AppConstants.screenWidth/2);
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

	public synchronized void dropDownChanged(byte dropDownID, int index) {
		if(storeForModifyFlag) return;

		if(dropDownID==ID_INSTRUMENT_DDL) {
			String sbf = bannerData.getCompanyCode();
			String nw = "";
			nw = sbf.substring(0, sbf.indexOf("_")+1) + ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString();
				LOG.print(nw+" \n "+ Utils.getCompanyLatestTradingDetailsURL(nw));
				new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(nw),1233,returnString,false);
		} else if(dropDownID == ID_EXPIRY_DDL ) {
			//ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);
			LOG.print(fnoTradeConfiguration.getFnoTradeType() + "Inside EXPIRY -> ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString() : "+ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
			String sbf = bannerData.getCompanyCode();
			String nw = "";
			if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX)
			{
				nw = sbf.substring(0, sbf.indexOf("_")+1) + ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString();
				LOG.print(nw+" \n "+ Utils.getCompanyLatestTradingDetailsURL(nw));
				startOptionsScreen();
				refreshFields();
				//new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(nw),1212,returnString,false);
			}
			else if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX)
			{
				LOG.print("OPTIONS");
				nw = sbf.substring(0, sbf.indexOf("_")+1) + ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString()+"_"+strikePrices[ddlStrikePrice.getSelectedIndex()]+"_"+(ddlOptionType.getSelectedIndex()==0?"CE":"PE");
				LOG.print(nw+" \n "+ Utils.getCompanyLatestTradingDetailsURL(nw));
				startOptionsScreen();
				//new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(nw),1213,returnString,false);
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
		
		else if( dropDownID == ID_STRIKE_PRICE_FNO_DDL || dropDownID == ID_OPTION_TYPE_DDL)
		{
			String sbf = bannerData.getCompanyCode();
			fnoTradeConfiguration.setFnoStrikePriceValue(strikePrices[ddlStrikePrice.getSelectedIndex()]);
			fnoTradeConfiguration.setOptioncepeType((ddlOptionType.getSelectedIndex()==0?FNOTradeConfiguration.FNO_OPTION_TYPE_CE:FNOTradeConfiguration.FNO_OPTION_TYPE_PE));
			String nw = "";
			LOG.print("OPTIONS STRIKE PRICE");
				nw = sbf.substring(0, sbf.indexOf("_")+1) + ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString()+"_"+strikePrices[ddlStrikePrice.getSelectedIndex()]+"_"+(ddlOptionType.getSelectedIndex()==0?"CE":"PE");
				LOG.print(nw+" \n "+ Utils.getCompanyLatestTradingDetailsURL(nw));
				new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(nw),121212,returnString,false);
		}
	}

	public void submiteFnoTradeOrder() {
		String strp = strikpR;
		if(ddlStrikePrice!=null)
		strp = (storeForModifyFlag?strikpR:ddlStrikePrice.getChoice(ddlStrikePrice.getSelectedIndex()).toString());
		if(validateOrder()) {
			String instrumentsText ;
			if(storeForModifyFlag)
			{
				instrumentsText = InstrumentR;
				}
			else
			{
				instrumentsText = ddlInstruments.getChoice(ddlInstruments.getSelectedIndex()).toString();
				InstrumentR = instrumentsText;
				}
			String expiryText ;
if(storeForModifyFlag)
{
	expiryText = expiryTextR;
	}
else
{
	expiryText = ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString();
	expiryTextR = expiryText;
	}

			String infoURL = "";
			if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX) {
				infoURL = fnoTradeBean.getIndexName()+"_"+expiryText;
			} else if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX) {
				infoURL = fnoTradeBean.getIndexName()+"_"+expiryText+"_"+/*(storeForModifyFlag?strikpR:ddlStrikePrice.getChoice(ddlStrikePrice.getSelectedIndex()).toString())*/strp+"_"+(storeForModifyFlag?optionR:(ddlOptionType.getSelectedIndex()==0?"CE":"PE"));
			}
			
			FNOOrderConfirmationBean confirmationBean = new FNOOrderConfirmationBean();
			confirmationBean.setSymbolName(fnoTradeBean.getIndexName());
			confirmationBean.setCustId(UserInfo.getUserID());
			confirmationBean.setMktLot(fnoTradeBean.getMinLot());

			confirmationBean.setExpiry(expiryTextR);
			boolean flag = false;
if(storeForModifyFlag)
	{
	if(InstrumentR.indexOf("FUT")>-1)
	{
		flag = false;
	}
	else
		flag = true;
	}
	else
	{
		if(ddlInstruments.getSelectedIndex()==1)
			flag = true;
	}
			if(flag) {
				if(ddlOptionType!=null) {
					//ddlStrikePrice = getCustomObjectChoiceFieldReg("", strikePrices, 0, 0,ID_STRIKE_PRICE_FNO_DDL);
					if(storeForModifyFlag)
					{if(storeForModifyHash.get("lblOptionType")!=null)
					{
						confirmationBean.setOptionType(optionR);
					}}
					else
					{
						storeForModifyHash.put("lblOptionType",ddlOptionType.getChoice(ddlOptionType.getSelectedIndex()).toString());
						confirmationBean.setOptionType(ddlOptionType.getChoice(ddlOptionType.getSelectedIndex()).toString());
						optionR = ddlOptionType.getChoice(ddlOptionType.getSelectedIndex()).toString();
						}
				} else {
					confirmationBean.setOptionType(optionR);
				}

				if(ddlStrikePrice!=null) {
					if(storeForModifyFlag)
						{if( storeForModifyHash.get("lblStrikePrice")!=null)
					{
						confirmationBean.setStrikePrice(strikpR);
					}
				}
					else
					{
						
						LOG.print("Strike Price : "+strp);
						storeForModifyHash.put("lblStrikePrice",strp);
						confirmationBean.setStrikePrice(strp);
						strikpR =strp;
					}                                
				} else {
					confirmationBean.setStrikePrice(strikpR);
				}
			} else {
				confirmationBean.setOptionType("");
				confirmationBean.setStrikePrice("0");
			}

			//Action Are different from UI and post fields 
			confirmationBean.setAction(actionPostValue[ddlActions.getSelectedIndex()]);
			storeForModifyHash.put("actionPostValue",ddlActions.getSelectedIndex()+"");

			confirmationBean.setQty(txtQty.getText());
			confirmationBean.setOrderType(ddlOrderTypes.getChoice(ddlOrderTypes.getSelectedIndex()).toString().toLowerCase());
			storeForModifyHash.put("lblOrderType",ddlOrderTypes.getSelectedIndex()+"");
			if(ddlOrderTypes.getSelectedIndex()==0) {
				if(txtPrice!=null) {
					storeForModifyHash.put("txtPrice",txtPrice.getText());
					confirmationBean.setLimitPrice((txtPrice.getText().trim().length()==0 ? "0" : txtPrice.getText()));
				} else {
					confirmationBean.setLimitPrice("0");
				}	
			} else {
				confirmationBean.setLimitPrice("0");
			}
			storeForModifyHash.put("txtTriggerPrice",txtTriggerPrice.getText());
			confirmationBean.setStopPrice((txtTriggerPrice.getText().trim().length()==0 ? "0" : txtTriggerPrice.getText()));
			storeForModifyHash.put("validity",ddlValidity.getSelectedIndex()+"");
			confirmationBean.setValidity(ddlValidity.getChoice(ddlValidity.getSelectedIndex()).toString());
			//Need to confirm with Server Team ()
			confirmationBean.setExchange("NSEFO");
			confirmationBean.setType("NOR");
			confirmationBean.setDiscQty("0");
			confirmationBean.setBannerData(bannerData);
			confirmationBean.setBannerDate(date);
			confirmationBean.setFreshOrder(true);
			infoURL = AppConstants.domainUrl + "SK_android/controller.php?RequestId=trfno02&symbolName="+infoURL+"&exchange=NSEFO&custId="+UserInfo.getUserID()+"&disc-qty="+confirmationBean.getDiscQty()+"&mkt-lots="+confirmationBean.getMktLot()+"&instType="+InstrumentR+"&expiry="+expiryText+"&optType="+confirmationBean.getOptionType()+"&strikePrice="+confirmationBean.getStrikePrice()+"&action="+confirmationBean.getAction()+"&qty="+confirmationBean.getQty()+"&orderType="+confirmationBean.getOrderType()+"&limitPrice="+confirmationBean.getLimitPrice()+"&stopPrice="+confirmationBean.getStopPrice()+"&validity="+confirmationBean.getValidity();
			LOG.print(infoURL);
			Vector commandDataVector = new Vector();
			commandDataVector.addElement(confirmationBean);
			commandDataVector.addElement(infoURL);
			actionPerfomed(ActionCommand.CMD_FNO_TRADE_FRESH_ORDER_CONFIRMATION, commandDataVector);

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

			if(ddlActions.getSelectedIndex()==0) {//Buy Action
				if(triggerPrice>price) {
					Dialog.alert("Trigger Price should be less than or equal to order price in case of Buy order.");
					txtTriggerPrice.setFocus();
					return false;
				}
			} else if (ddlActions.getSelectedIndex()==1) {//Sell Action
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
		case ActionCommand.CMD_FNO_TRADE_FRESH_ORDER_CONFIRMATION:
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
	public synchronized void setReturnString(String string, int id) {
		if(id == 1233)
		{
			Vector v = HomeJsonParser.getVector(string);
			bannerData = (HomeJson) v.elementAt(0);
			banner.setName(bannerData.getSymbol());
			if(ddlExpiries!=null)
			date = Expiry.getText(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
			banner.setSource(date);
			banner.setComp(bannerData.getCompanyCode());
			banner.setData(bannerData.getLastTradedPrice(), bannerData.getChange(), (bannerData.getPercentageDiff().indexOf("-") == -1 ? "+"+bannerData.getPercentageDiff() : bannerData.getPercentageDiff()),bannerData.getOpenInterest(), bannerData.getVolume());
			banner.refresh();
			id_instrument_ddl();
		}
		else if(id ==1213)
		{
			LOG.print("   startOptionsScreen  "+string);
			Vector v = HomeJsonParser.getVector(string);
			bannerData = (HomeJson) v.elementAt(0);
			banner.setName(bannerData.getSymbol());
			if(ddlExpiries!=null)
			date = Expiry.getText(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
			banner.setSource(date);
			banner.setComp(bannerData.getCompanyCode());
			banner.setData(bannerData.getLastTradedPrice(), bannerData.getChange(), (bannerData.getPercentageDiff().indexOf("-") == -1 ? "+"+bannerData.getPercentageDiff() : bannerData.getPercentageDiff()),bannerData.getOpenInterest(), bannerData.getVolume());
			banner.refresh();
			startOptionsScreen();
		}
		else if(id ==121212)
		{
			Vector v = HomeJsonParser.getVector(string);
			bannerData = (HomeJson) v.elementAt(0);
			banner.setName(bannerData.getSymbol());
			if(ddlExpiries!=null)
			date = Expiry.getText(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
			banner.setSource(date);
			banner.setComp(bannerData.getCompanyCode());
			banner.setData(bannerData.getLastTradedPrice(), bannerData.getChange(), (bannerData.getPercentageDiff().indexOf("-") == -1 ? "+"+bannerData.getPercentageDiff() : bannerData.getPercentageDiff()),bannerData.getOpenInterest(), bannerData.getVolume());
			banner.refresh();
		}
		else
			if(id ==1212) { 
				Vector v = HomeJsonParser.getVector(string);
				bannerData = (HomeJson) v.elementAt(0);

				if(bannerData!=null) {
					date = Expiry.getText(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
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

	public void startOptionsScreen()
	{
		if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX && !storeForModifyFlag) {

            if(!ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).equals(fnoTradeConfiguration.getFnoExpiryDate())) {

                    fnoTradeConfiguration.setFnoExpiryDate(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
                    String sbf = bannerData.getCompanyCode();
                    String infoURL ;
                    if(sbf!=null)
                    	infoURL = Utils.getFNOExpiryAndStrikeDataURL(sbf.substring(0, sbf.indexOf("_")+1) + ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString()+"_"+strikePrices[ddlStrikePrice.getSelectedIndex()]+"_"+(ddlOptionType.getSelectedIndex()==0?"CE":"PE"));
                    else
                    	infoURL = Utils.getFNOExpiryAndStrikeDataURL(fnoTradeConfiguration.getCompanyCode());
                    Vector dataVector = new Vector();

                    dataVector.addElement(infoURL);
            dataVector.addElement(fnoTradeBean.getIndexName());
            fnoTradeConfiguration.setFnoExpiryDate(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
            dataVector.addElement(fnoTradeConfiguration);
            /////////////
    		if(bannerData!=null) {
    			dataVector.addElement(bannerData);
    		}
    		dataVector.addElement(date);
    		///////////
            actionPerfomed(ActionCommand.CMD_FNO_TRADE, dataVector);
            }
    }
	}	
	
	public void id_instrument_ddl()
	{
		//if(ddlInstruments.getSelectedIndex()!=fnoTradeConfiguration.getFnoTradeType()) {
		fnoTradeConfiguration.setFnoTradeType((byte)ddlInstruments.getSelectedIndex());
		//fnoTradeConfiguration.setFnoExpiryDate(null);

		String infoURL = "";

		if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX) {
			infoURL = Utils.getFNOExpiryAndStrikeDataURL(fnoTradeBean.getIndexName()+"_"+ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
		} else if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX) {
			if(strikePrices==null || ddlStrikePrice==null || ddlOptionType==null)
				infoURL = Utils.getFNOExpiryAndStrikeDataURL(fnoTradeBean.getIndexName()+"_"+ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString()+"_200.00_CE");
			else
				infoURL = Utils.getFNOExpiryAndStrikeDataURL(fnoTradeBean.getIndexName()+"_"+ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString()+"_"+strikePrices[ddlStrikePrice.getSelectedIndex()]+"_"+(ddlOptionType.getSelectedIndex()==0?"CE":"PE"));
		}

		Vector dataVector = new Vector();

		dataVector.addElement(infoURL);
		dataVector.addElement(fnoTradeBean.getIndexName());
		fnoTradeConfiguration.setFnoExpiryDate(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
		dataVector.addElement(fnoTradeConfiguration);

		////////////////////////////////////
		// fnoTradeConfiguration.setFnoExpiryDate(ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString());
		// dataVector.addElement(fnoTradeConfiguration);
		if(bannerData!=null) {
			dataVector.addElement(bannerData);
		}
		dataVector.addElement(date);
		////////////////////////////////////
		//refreshFields();
		actionPerfomed(ActionCommand.CMD_FNO_TRADE, dataVector);
	//}                    
	}
	public void refreshFields() {
		String compCode=bannerData.getCompanyCode();
		if(!FNOTradeScreen.storeForModifyFlag)
		{
		LOG.print("Refresh Start");
		
		if(fnoTradeConfiguration.getFnoTradeType()==FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX) 
		{
			compCode = bannerData.getSymbol().replace('&', '-')+"_"+ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString();
		}
		else if(strikePrices==null || ddlStrikePrice==null || ddlOptionType==null);
		else
		{
			compCode = bannerData.getSymbol().replace('&', '-')+"_"+ddlExpiries.getChoice(ddlExpiries.getSelectedIndex()).toString()+"_"+strikePrices[ddlStrikePrice.getSelectedIndex()]+"_"+(ddlOptionType.getSelectedIndex()==0?"CE":"PE");
		}
		}
		new ReturnStringParser(Utils.getQuoteURL(compCode),121212,returnString,false);
	}
		
}
