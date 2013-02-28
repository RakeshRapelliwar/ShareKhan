package com.snapwork.view;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
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
import com.snapwork.beans.CommodityBean;
import com.snapwork.beans.Expiry;
import com.snapwork.beans.FNOSearchBean;
import com.snapwork.beans.FoScrips;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.SearchBean;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.CustomLabelField;
import com.snapwork.components.CustomLinkButton;
import com.snapwork.components.CustomObjectChoiceField;
import com.snapwork.components.CustomObjectChoiceFieldReg;
import com.snapwork.components.GLTabView;
import com.snapwork.components.RadioButtonLayout;
import com.snapwork.components.SearchListField;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.TitleBar;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.ReturnStringParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.Utils;


public class SearchEFScreen extends MainScreen implements ActionListener, FieldChangeListener ,ReturnString{

	private String[] strGroup= {"BSE","NSE","Commodity"};
	
	
	private BottomMenu bottomMenu = null;
	private CustomBasicEditField txtFieldSearch = null;
	private CustomBasicEditField commoditytxtFieldSearch = null;
	private CustomBasicEditField txtFieldSearchFNO = null;
	private CustomBasicEditField txtFieldStrike = null;
	private SearchEFScreen screen = this;
	private long time;
	private long timer;
//	private RadioButtonGroup searchType ;
	private VerticalFieldManager mainManager;
	//private Manager searchBarManager;
	private VerticalFieldManager searchBarManager,commoditySearchBarManager;
	private VerticalFieldManager indexField;
	private VerticalFieldManager stockField;
	private ObjectChoiceField niftyType;
	private String searchTypeText[] = {"Equity","F&O"};
	public static String niftyChoicesg[] = {"NIFTY","MINIFTY","BANKNIFTY","CNXIT","CNXINFRA","CNXPSE","DJIA","FTSE100","NFTYMCAP50","S&P500"};
	private String niftyChoices[] = niftyChoicesg;
	public static String niftyIndices[] = null;
	private String[] instruText = {"Future Index","Future Stock","Option Index","Option Stock"};
	private String[] cepeText = {"Call","Put"};
	private String[] dateText;
	private ObjectChoiceField instruChoice;
	private ObjectChoiceField dateChoice;
	private ObjectChoiceField cepeChoice;
	private CustomLabelField actionLabel;
	private CustomLabelField eqLabel;
	private CustomLabelField niftyLabel;
	private CustomLabelField cepeLabel;
	private CustomLabelField dateLabel;
	private CustomLabelField strikeLabel;
	private HorizontalFieldManager instru;
	private HorizontalFieldManager dateManager;
	private HorizontalFieldManager txtManager,commoditytxtManager;
	private HorizontalFieldManager txtManagerFNO;
	private HorizontalFieldManager niftyManager;
	private HorizontalFieldManager cepe;
	private HorizontalFieldManager strike;
	private ButtonField objBtnSignUp, objBtnSignUpFNO,commodityobjBtnSignUp;
	private int ID_NIFTY_CHOICE = 100;
	private int ID_INSTRU_CHOICE = 101;
	private int ID_DATE_CHOICE = 102;
	private int ID_CEPE_CHOICE = 103;
	
	
	static byte selectedIndex = 0;
	
	private TopTabber topTabber;
	
	//private String[] dateText = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	public SearchEFScreen() {
		getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
//		searchType = new RadioButtonGroup();
		SearchEFScreen.selectedIndex=0;
		time = System.currentTimeMillis();
		timer = System.currentTimeMillis();
		if(niftyIndices!=null)
		{
			if(niftyIndices.length!=0)
				niftyChoices = niftyIndices;
		}
		createUI();
	}

	public void createUI() {
		//VerticalFieldManager titleBar = new VerticalFieldManager(USE_ALL_WIDTH);
		//titleBar.add(new TitleBar("Search scrip"));

		/*mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setBackgroundColor(Color.BLACK);
				graphics.clear();
			}            
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(AppConstants.screenWidth, AppConstants.screenHeight);
				setExtent(AppConstants.screenWidth, AppConstants.screenHeight-BottomMenu.getItemHeight()-TitleBar.getItemHeight());
			}
		};*/
		mainManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			public void paintBackground(Graphics graphics)
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
			}  
			
			protected void sublayout( int maxWidth, int maxHeight )
			{
				super.sublayout(maxWidth,maxHeight);
			}
		};
		searchBarManager = new VerticalFieldManager(USE_ALL_WIDTH)
		{
			protected void paintBackground(Graphics graphics) 
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
				graphics.setColor(Color.GRAY);
				graphics.drawRoundRect(5, 5, getWidth()-10, getHeight()-5,8,8);
			};
		};
		
		
		
		commoditySearchBarManager = new VerticalFieldManager(USE_ALL_WIDTH)
		{
			protected void paintBackground(Graphics graphics) 
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
				graphics.setColor(Color.GRAY);
				graphics.drawRoundRect(5, 5, getWidth()-10, getHeight()-5,8,8);
			};
		};
		
		
		

		//VerticalFieldManager titleBar = new VerticalFieldManager(USE_ALL_WIDTH);
		//titleBar.add(new TitleBar("Search Bar"));
		LOG.print("SearchEFScreen Error 1");
		setTitle(new TitleBar("Search Scrip"));
		LOG.print("SearchEFScreen Error 2");
		/*HorizontalFieldManager line = new HorizontalFieldManager(FIELD_HCENTER)
		{
			public int getPreferredWidth() {
				return AppConstants.screenWidth;
			}
			protected void sublayout(int width, int height) {
				layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getHeight());
				layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getHeight());
				setPositionChild(getField(0),(getPreferredWidth()/2)-(getField(0).getPreferredWidth()/2),0);
				setPositionChild(getField(1),(getPreferredWidth()/2)+((getPreferredWidth()/4)-(getField(1).getPreferredWidth()/2)),0);
				super.sublayout(getPreferredWidth(), height);
			}
		};*/


		//new radio to the group
	/*	RadioButtonField radio = new RadioButtonField("Equity ", searchType, true)
		{
			public int getPreferredWidth() {
				return getFont().getAdvance("###Equity###");
			}
			protected void paint(Graphics gra) {
				gra.setColor(0xeeeeee);
				super.paint(gra);
			}
			protected void fieldChangeNotify(int index) {
				if (index != FieldChangeListener.PROGRAMMATIC )
				{
					/*if(mainManager.getFieldCount()>1)
					{
						mainManager.deleteRange(1, mainManager.getFieldCount()-1);
						mainManager.add(searchBarManager);
					}
					else
						mainManager.add(searchBarManager);*
				}
			}
			protected void layout(int width, int height) {
				// TODO Auto-generated method stub
				super.layout(getPreferredWidth(), height);
			}
		};
		radio.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
		line.add(radio);
		//line.add(Utils.separatorWidth(20));
		//another radio to the group
		RadioButtonField radio2 = new RadioButtonField("F&O ", searchType, false)
		{
			public int getPreferredWidth() {
				return getFont().getAdvance("###F&O###");
			}
			protected void paint(Graphics gra) {
				gra.setColor(0xeeeeee);
				super.paint(gra);
			}
			protected void fieldChangeNotify(int index) {
				if (index != FieldChangeListener.PROGRAMMATIC )
				{
					/*if(mainManager.getFieldCount()>1)
					{
						mainManager.deleteRange(1, mainManager.getFieldCount()-1);
						mainManager.add(indexField);
					}
					else
						mainManager.add(indexField);*
				}
			}
			protected void layout(int width, int height) {
				// TODO Auto-generated method stub
				super.layout(getPreferredWidth(), height);
			}
		};
		radio2.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
		line.add(radio2);*/
//		RadioButtonField call = new RadioButtonField("Equity",searchType,true, RadioButtonField.FIELD_RIGHT)
//		{
//			protected void paint(Graphics graphics) 
//			{
//				graphics.setColor(0xeeeeee);
//				if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
//					graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
//				super.paint(graphics);
//			}
//			protected boolean navigationClick(int keycode,
//					int time){
//				setSelected(true);
//				if(mainManager.getFieldCount()>1)
//						{
//							mainManager.deleteRange(1, mainManager.getFieldCount()-1);
//							mainManager.add(searchBarManager);
//						}
//						else
//							mainManager.add(searchBarManager);
//				return true;
//			}
//			/*protected void layout(int width, int height)
//			{
//				if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
//				{
//					super.layout(width, height*3);
//				}
//				else
//				{
//					super.layout(width, height);
//				}
//			};*/
//		}; 
//		LOG.print("SearchEFScreen Error 3");
//		RadioButtonField put = new RadioButtonField("F&O",searchType,false, RadioButtonField.FIELD_RIGHT)
//		{ 
//			protected void paint(Graphics graphics) 
//			{
//				graphics.setColor(0xeeeeee);
//				if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
//					graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
//				super.paint(graphics);
//			}
//			protected boolean navigationClick(int keycode,
//					int time)
//			{
//				setSelected(true);
//				if(mainManager.getFieldCount()>1)
//				{
//					mainManager.deleteRange(1, mainManager.getFieldCount()-1);
//					mainManager.add(indexField);
//				}
//				else
//					mainManager.add(indexField);
//				return true;
//			}
//			/*protected void layout(int width, int height)
//			{
//				if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
//				{
//					super.layout(width, height*5);
//				}
//				else
//				{
//					super.layout(width, height);
//				}
//			};*/
//		};
//		LOG.print("SearchEFScreen Error 4");
		RadioButtonLayout line = new RadioButtonLayout(80);
//		LOG.print("SearchEFScreen Error 5");
//		line.add(call);
//		line.add(put);
		
		
		
		
		
		
		
//		mainManager.add(line);
		
		topTabber = new TopTabber();
		mainManager.add(topTabber);
		
		
		
//		mainManager.add(new GLTabView(vector,this,strGroup[selectedIndex_]));
		
		
		LOG.print("SearchEFScreen Error 6");

		txtFieldSearch = new CustomBasicEditField(BasicEditField.NO_NEWLINE | BasicEditField.NO_COMPLEX_INPUT) {

			public int getPreferredHeight() {
				return (getFont().getHeight()+(AppConstants.padding/3)*2);
			}

			public int getPreferredWidth() {
				return AppConstants.screenWidth - 40 ;
			}
			protected void paintBackground(Graphics graphics) 
			{
				graphics.setColor(0xeeeeee);
				graphics.fillRect(0,0,getWidth(),getHeight());
				graphics.setColor(Color.GRAY);
				graphics.drawRect(0,0,getWidth(),getHeight());
				graphics.setColor(Color.BLACK);
			}
		};
		
		
		
		commoditytxtFieldSearch = new CustomBasicEditField(BasicEditField.NO_NEWLINE | BasicEditField.NO_COMPLEX_INPUT) {

			public int getPreferredHeight() {
				return (getFont().getHeight()+(AppConstants.padding/3)*2);
			}

			public int getPreferredWidth() {
				return AppConstants.screenWidth - 40 ;
			}
			protected void paintBackground(Graphics graphics) 
			{
				graphics.setColor(0xeeeeee);
				graphics.fillRect(0,0,getWidth(),getHeight());
				graphics.setColor(Color.GRAY);
				graphics.drawRect(0,0,getWidth(),getHeight());
				graphics.setColor(Color.BLACK);
			}
		};
		
		
		
		
		LOG.print("SearchEFScreen Error 7");
		txtFieldSearch.setFilter(TextFilter.get(TextFilter.DEFAULT));
		if(AppConstants.screenHeight>320)
			txtFieldSearch.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT22));
		else
			txtFieldSearch.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
		txtManager = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR)
		{
			protected void sublayout( int maxWidth, int maxHeight )
			{	layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			setPositionChild(getField(0), 20, 0);
			setExtent(AppConstants.screenWidth,getField(0).getHeight());
			}
		};
		
		
		commoditytxtManager = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR)
		{
			protected void sublayout( int maxWidth, int maxHeight )
			{	layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			setPositionChild(getField(0), 20, 0);
			setExtent(AppConstants.screenWidth,getField(0).getHeight());
			}
		};
		
		
		
		
		
		txtManager.add(txtFieldSearch);
		commoditytxtManager.add(commoditytxtFieldSearch);
		LOG.print("SearchEFScreen Error 8");
		objBtnSignUp = new ButtonField(FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {
				actionPerfomed(ActionCommand.CMD_SEARCH_COMPANY, null);
				return true;
			}
		};
		
		
		commodityobjBtnSignUp = new ButtonField(FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {
				actionPerfomed(ActionCommand.CMD_SEARCH_COMMODITY, null);
				
				return true;
			}
		};
		
		objBtnSignUp.setLabel("Go");
		objBtnSignUp.setMinimalWidth(objBtnSignUp.getFont().getAdvance("###Go##"));
		
		commodityobjBtnSignUp.setLabel("Go");
		commodityobjBtnSignUp.setMinimalWidth(objBtnSignUp.getFont().getAdvance("###Go##"));
		
		searchBarManager.add(Utils.separator(12));
		LOG.print("SearchEFScreen Error 9");
		searchBarManager.add(txtManager);
		searchBarManager.add(Utils.separator(6));
		searchBarManager.add(objBtnSignUp);
		searchBarManager.add(Utils.separator(6));
		mainManager.add(searchBarManager);
		LOG.print("SearchEFScreen Error 10");
		
		
		
		commoditySearchBarManager.add(Utils.separator(12));
		LOG.print("SearchEFScreen Error 9");
		commoditySearchBarManager.add(commoditytxtManager);
		commoditySearchBarManager.add(Utils.separator(6));
		commoditySearchBarManager.add(commodityobjBtnSignUp);
		commoditySearchBarManager.add(Utils.separator(6));
		
		
		
		
		
		
		
		
		
		
		
		

		instru = new HorizontalFieldManager(USE_ALL_WIDTH)
		{
			protected void sublayout( int maxWidth, int maxHeight )
			{ layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			layoutChild(getField(1), AppConstants.screenWidth/2, getField(1).getPreferredHeight());
			//layoutChild(getField(1), AppConstants.screenWidth/2, getField(1).getPreferredHeight());
			//setPositionChild(getField(0), 20, ((getField(1).getPreferredHeight()+8)/2)-(getField(0).getHeight()/2));
			//setPositionChild(getField(1), AppConstants.screenWidth/2, 4);
			//setExtent(AppConstants.screenWidth,getField(1).getPreferredHeight()>getField(0).getHeight()?(getField(1).getPreferredHeight()+8):(getField(0).getHeight()+8));
			setPositionChild(getField(0), 20, 2+((getField(1).getHeight()+8)/2)-(getField(0).getHeight()/2));
			setPositionChild(getField(1), AppConstants.screenWidth/2-20, 4);
			setExtent(AppConstants.screenWidth,getField(1).getPreferredHeight()+8);
			}
		};
		actionLabel = new CustomLabelField("Instrument", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		eqLabel = new CustomLabelField("FO Scrips", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		dateLabel = new CustomLabelField("Expiry Date", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		cepeLabel = new CustomLabelField("Call/Put", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		strikeLabel = new CustomLabelField("Strike Price", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		/*instruChoice = new CustomObjectChoiceField("", instruText,0, ObjectChoiceField.FORCE_SINGLE_LINE | FIELD_HCENTER)
		{
			protected void fieldChangeNotify(int context)
			{
				if (context != FieldChangeListener.PROGRAMMATIC )
				{
				if(getSelectedIndex()==0) //Future Index
				{
					addFI();
				}
				else if(getSelectedIndex()==1) //Future Stock
				{
					addFS();
				}
				if(getSelectedIndex()==2) //Option Index
				{
					addOI();
				}
				if(getSelectedIndex()==3) //Option Stock
				{
					addOS();
				}
				mainManager.invalidate();
			}
			}
		};*/
		instruChoice = getCustomObjectChoiceFieldReg("", instruText, 0, 0,ID_INSTRU_CHOICE);
		/*if(AppConstants.OS<7)
			instruChoice.setMinimalWidth((AppConstants.screenWidth/2)-40/*actionChoice.getFont().getAdvance("##############")*);
		else
			instruChoice.setMinimalWidth((AppConstants.screenWidth/2)-100);*/
		instru.add(actionLabel);
		//instru.add(Utils.separatorWidth(20));
		instru.add(instruChoice);
		indexField = new VerticalFieldManager(USE_ALL_WIDTH)
		{
			protected void paintBackground(Graphics graphics) 
			{
				graphics.setColor(Color.BLACK);
				graphics.fillRect(0, 0, getWidth(), getHeight());
				graphics.setColor(Color.GRAY);
				graphics.drawRoundRect(5, 5, getWidth()-10, getHeight()-5,8,8);
			};
		};
		//niftyType = new CustomObjectChoiceField("", niftyChoices,0,ObjectChoiceField.FORCE_SINGLE_LINE | FIELD_HCENTER)
		//if(AppConstants.OS>5)
	//		niftyType = new CustomObjectChoiceField("", niftyChoices,0,USE_ALL_WIDTH | FIELD_HCENTER);
		//else
			//niftyType = new CustomObjectChoiceField("", niftyChoices,0,ObjectChoiceField.USE_ALL_WIDTH | FIELD_HCENTER);
		niftyType = getCustomObjectChoiceFieldReg("", niftyChoices, 0, 0,ID_NIFTY_CHOICE);
		/*{
			public int getPreferredWidth() 
			{
				return AppConstants.screenWidth-60;
			}
			;
		}*/
		;
		//niftyType.setId(true);
		/*if(AppConstants.OS<7)
			niftyType.setMinimalWidth(AppConstants.screenWidth-100);
		else
			niftyType.setMinimalWidth(AppConstants.screenWidth-200);*/
		indexField.add(Utils.separator(10));
		indexField.add(instru);
		/*niftyManager = new HorizontalFieldManager(USE_ALL_WIDTH)
		{
			protected void sublayout( int maxWidth, int maxHeight )
			{	layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			if(AppConstants.OS>5)
				setPositionChild(getField(0), (AppConstants.screenWidth/2-((getField(0).getHeight()>getField(0).getPreferredHeight()?getField(0).getHeight():getField(0).getPreferredHeight())/2))-10, 0);
			else
				setPositionChild(getField(0), 10, 0);
			setExtent(AppConstants.screenWidth,getField(0).getPreferredHeight());
			}
		}
		;*/
		niftyManager = new HorizontalFieldManager(USE_ALL_WIDTH)
		{
			protected void sublayout( int maxWidth, int maxHeight )
			{ layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			layoutChild(getField(1), AppConstants.screenWidth/2, getField(1).getPreferredHeight());
			//layoutChild(getField(1), AppConstants.screenWidth/2, getField(1).getPreferredHeight());
			//setPositionChild(getField(0), 20, ((getField(1).getPreferredHeight()+8)/2)-(getField(0).getHeight()/2));
			//setPositionChild(getField(1), AppConstants.screenWidth/2, 4);
			//setExtent(AppConstants.screenWidth,getField(1).getPreferredHeight()>getField(0).getHeight()?(getField(1).getPreferredHeight()+8):(getField(0).getHeight()+8));
			setPositionChild(getField(0), 20, 2+((getField(1).getHeight()+8)/2)-(getField(0).getHeight()/2));
			setPositionChild(getField(1), AppConstants.screenWidth/2-20, 4);
			setExtent(AppConstants.screenWidth,getField(1).getPreferredHeight()+8);
			}
		};
		niftyLabel = new CustomLabelField("Nifty Indices", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		niftyManager.add(niftyLabel);
		niftyManager.add(niftyType);
		indexField.add(niftyManager);

		dateManager = new HorizontalFieldManager(USE_ALL_WIDTH)
		{
			protected void sublayout( int maxWidth, int maxHeight )
			{ layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			layoutChild(getField(1), AppConstants.screenWidth/2, getField(1).getPreferredHeight());
			setPositionChild(getField(0), 20, 2+((getField(1).getHeight()+8)/2)-(getField(0).getHeight()/2));
			setPositionChild(getField(1), AppConstants.screenWidth/2-20, 4);
			setExtent(AppConstants.screenWidth,getField(1).getPreferredHeight()+8);
			}
		};
		dateText = new String[Expiry.size()];
		for(int i=0;i<Expiry.size();i++)
		{
			dateText[i]=Expiry.getTextWithYear(i);
		}
		//dateChoice = new CustomObjectChoiceField("",dateText,0, FIELD_HCENTER)
		dateChoice = getCustomObjectChoiceFieldReg("", dateText, 0, 0,ID_DATE_CHOICE);
		/*if(AppConstants.OS<7)
			dateChoice.setMinimalWidth((AppConstants.screenWidth/2)-40/*actionChoice.getFont().getAdvance("##############")*);
		else
			dateChoice.setMinimalWidth((AppConstants.screenWidth/2)-60);*/
		dateManager.add(dateLabel);
		//instru.add(Utils.separatorWidth(20));
		dateManager.add(dateChoice);
		indexField.add(dateManager);
		cepe = new HorizontalFieldManager(USE_ALL_WIDTH)
		{
			protected void sublayout( int maxWidth, int maxHeight )
			{ layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
			setPositionChild(getField(0), 20, 2+((getField(1).getHeight()+8)/2)-(getField(0).getHeight()/2));
			setPositionChild(getField(1), AppConstants.screenWidth/2-20, 4);
			setExtent(AppConstants.screenWidth,getField(1).getHeight()+8);
			}
		};
		//cepeChoice = new CustomObjectChoiceField("",cepeText,0, ObjectChoiceField.FORCE_SINGLE_LINE | FIELD_HCENTER)
		cepeChoice = getCustomObjectChoiceFieldReg("", cepeText, 0, 0,ID_CEPE_CHOICE);
		/*if(AppConstants.OS<7)
			cepeChoice.setMinimalWidth((AppConstants.screenWidth/2)-40/*actionChoice.getFont().getAdvance("##############")*);
		else
			cepeChoice.setMinimalWidth((AppConstants.screenWidth/2)-60);*/
		cepe.add(cepeLabel);
		//instru.add(Utils.separatorWidth(20));
		cepe.add(cepeChoice);
		//indexField.add(cepe);
		//HorizontalFieldManager horizontalFieldManager = new HorizontalFieldManager(VERTICAL_SCROLL | VERTICAL_SCROLLBAR | HORIZONTAL_SCROLL | HORIZONTAL_SCROLLBAR);


		//Add Sign Up Button
		objBtnSignUpFNO = new ButtonField(FIELD_HCENTER) {
			protected boolean navigationClick(int status, int time) {
				actionPerfomed(ActionCommand.CMD_SEARCH_COMPANY, null);
				return true;
			}
		};
		objBtnSignUpFNO.setLabel("Go");
		objBtnSignUpFNO.setMinimalWidth(objBtnSignUpFNO.getFont().getAdvance("###Go##"));
		//horizontalFieldManager.add(objBtnSignUp);
		indexField.add(objBtnSignUpFNO);
		indexField.add(Utils.separator(10));

//		//horizontalFieldManager.add(objBtnSignUp);
//		txtFieldSearchFNO = new CustomBasicEditField(BasicEditField.NO_NEWLINE | BasicEditField.NO_COMPLEX_INPUT) {
//
//			public int getPreferredHeight() {
//				return (getFont().getHeight()+(AppConstants.padding/3)*2);
//			}
//
//			public int getPreferredWidth() {
//				return AppConstants.screenWidth - 40 ;
//			}
//			protected void paintBackground(Graphics graphics) 
//			{
//				graphics.setColor(0xeeeeee);
//				graphics.fillRect(0,0,getWidth(),getHeight());
//				graphics.setColor(Color.GRAY);
//				graphics.drawRect(0,0,getWidth(),getHeight());
//				graphics.setColor(Color.BLACK);
//			}
//		};
//		txtFieldSearchFNO.setFilter(TextFilter.get(TextFilter.DEFAULT));
//		txtFieldSearchFNO.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));//MEDIUM_SPECIAL_FONT));
//		txtManagerFNO = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR)
//		{
//			protected void sublayout( int maxWidth, int maxHeight )
//			{	layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
//			setPositionChild(getField(0), 20, 0);
//			setExtent(AppConstants.screenWidth,getField(0).getHeight());
//			}
//		}
//		;
//		txtManagerFNO.add(txtFieldSearchFNO);
		txtFieldSearchFNO = new CustomBasicEditField(BasicEditField.NO_NEWLINE | BasicEditField.NO_COMPLEX_INPUT) {

			public int getPreferredHeight() {
				return (getFont().getHeight()+(AppConstants.padding/3)*2);
			}

			public int getPreferredWidth() {
				return AppConstants.screenHeight>320?((AppConstants.screenWidth/2)-30):((AppConstants.screenWidth/2)-5);
			}
			protected void paintBackground(Graphics graphics) 
			{
				graphics.setColor(0xeeeeee);
				graphics.fillRect(0,0,getWidth(),getHeight());
				graphics.setColor(Color.GRAY);
				graphics.drawRect(0,0,getWidth(),getHeight());
				graphics.setColor(Color.BLACK);
			}
		};
		txtFieldSearchFNO.setFilter(TextFilter.get(TextFilter.DEFAULT));
		if(AppConstants.screenHeight>320)
			txtFieldSearchFNO.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT22));
		else
			txtFieldSearchFNO.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
		txtManagerFNO = new HorizontalFieldManager(USE_ALL_WIDTH)
		{
			protected void sublayout( int maxWidth, int maxHeight )
			{ layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getHeight());
			setPositionChild(getField(0), 20, ((getField(1).getHeight()+8)/2)-(getField(0).getHeight()/2));
			setPositionChild(getField(1), AppConstants.screenHeight>320?(AppConstants.OS<6?(AppConstants.screenWidth-getField(1).getPreferredWidth()-20):(AppConstants.screenWidth/2-10)):(AppConstants.screenWidth-getField(1).getPreferredWidth()-20), 4);
			setExtent(AppConstants.screenWidth,getField(1).getHeight()+8);
			}
		};
		txtManagerFNO.add(eqLabel);
		//instru.add(Utils.separatorWidth(20));
		txtFieldSearchFNO.setChangeListener(this);
		txtManagerFNO.add(txtFieldSearchFNO);
		txtFieldStrike = new CustomBasicEditField(BasicEditField.NO_NEWLINE | CustomBasicEditField.FILTER_REAL_NUMERIC) {

			public int getPreferredHeight() {
				return (getFont().getHeight()+(AppConstants.padding/3)*2);
			}

			public int getPreferredWidth() {
				return AppConstants.screenHeight>320?((AppConstants.screenWidth/2)-30):((AppConstants.screenWidth/2)-5);
			}
			protected void paintBackground(Graphics graphics) 
			{
				graphics.setColor(0xeeeeee);
				graphics.fillRect(0,0,getWidth(),getHeight());
				graphics.setColor(Color.GRAY);
				graphics.drawRect(0,0,getWidth(),getHeight());
				graphics.setColor(Color.BLACK);
			}
		};
		txtFieldStrike.setFilter(new NumericTextFilter(NumericTextFilter.ALLOW_DECIMAL));
		if(AppConstants.screenHeight>320)
			txtFieldStrike.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT22));
		else
			txtFieldStrike.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
		strike = new HorizontalFieldManager(USE_ALL_WIDTH)
		{
			protected void sublayout( int maxWidth, int maxHeight )
			{ layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getHeight());
			setPositionChild(getField(0), 20, ((getField(1).getHeight()+8)/2)-(getField(0).getHeight()/2));
			setPositionChild(getField(1), AppConstants.screenHeight>320?(AppConstants.OS<6?(AppConstants.screenWidth-getField(1).getPreferredWidth()-20):(AppConstants.screenWidth/2-10)):(AppConstants.screenWidth-getField(1).getPreferredWidth()-20), 4);
			setExtent(AppConstants.screenWidth,getField(1).getHeight()+8);
			}
		};
		strike.add(strikeLabel);
		//instru.add(Utils.separatorWidth(20));
		txtFieldStrike.setChangeListener(this);
		strike.add(txtFieldStrike);
		stockField = new VerticalFieldManager(USE_ALL_WIDTH);
		add(mainManager);
		bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_SEARCH_SCREEN, AppConstants.bottomMenuCommands);

		
		
		txtFieldSearch.setFocus();
		
		
	}

	public boolean onSavePrompt() {
		return true;
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

	public void componentsPrepared(byte componentID, Object component) {
		// TODO Auto-generated method stub

	}

	public Vector getComponentData() {
		return null;
	}

	public void actionPerfomed(byte Command, Object data) {
		switch(Command) {
		case ActionCommand.CMD_SEARCH_COMPANY:
		if(data==null){
			String text = "";
			String textFNO = "";
			String textDateId = "";
			boolean go = false;
			boolean searchMessage = false;
			boolean strikeMessage = false;
			if(SearchEFScreen.selectedIndex==0 && txtFieldSearch.getText().trim().length()==0)
				searchMessage = true;
			else if(SearchEFScreen.selectedIndex==1)
			{ 
				if(txtFieldSearchFNO.getText().trim().length()<1 && instruChoice.getSelectedIndex()==3)
					searchMessage = true;
				/*else if(instruChoice.getSelectedIndex()>1 && txtFieldStrike.getText().trim().length()<2)
					strikeMessage = true;*/
			}
			if(SearchEFScreen.selectedIndex==0 && !searchMessage)
			{
				go = true;
				text = txtFieldSearch.getText().trim();
			}
			else if(SearchEFScreen.selectedIndex==1 && !searchMessage)
			{
				textDateId = dateChoice.getSelectedIndex()+"";
				if(instruChoice.getSelectedIndex()==0)
				{
					textFNO = niftyChoices[niftyType.getSelectedIndex()].replace('&', '-')+"_"+Expiry.getValue(dateChoice.getSelectedIndex());
					//SearchStocksFNOFields searchStocksFNOFields =  new SearchStocksFNOFields(textFNO, dateChoice.getSelectedIndex()+"",Utils.WATCHLIST_MODE);
					ReturnStringParser rts = new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(textFNO), 8258, this);
				}
				else if(instruChoice.getSelectedIndex()==1)
				{
					go = true;
						text = txtFieldSearchFNO.getText().trim().toUpperCase();
						textFNO = "_"+Expiry.getValue(dateChoice.getSelectedIndex());
				}
				else if(instruChoice.getSelectedIndex()==2 && !strikeMessage)
				{
					/*if(txtFieldStrike.getText().length()!=0)
					{
					textFNO = niftyChoices[niftyType.getSelectedIndex()].replace('&', '-')+"_"+Expiry.getValue(dateChoice.getSelectedIndex())+"_"+txtFieldStrike.getText().trim()+"_"+(cepeChoice.getSelectedIndex()==0?"CE":"PE");
					//textFNO = Utils.getCompanyFNOSearchURL_STRIKE(niftyChoices[niftyType.getSelectedIndex()], Expiry.getValue(dateChoice.getSelectedIndex()), (cepeChoice.getSelectedIndex()==0?"CE":"PE"), txtFieldStrike.getText().trim());
					//SearchStocksFNOFields searchStocksFNOFields =  new SearchStocksFNOFields(textFNO, dateChoice.getSelectedIndex()+"",Utils.WATCHLIST_MODE);
					ReturnStringParser rts = new ReturnStringParser(Utils.getCompanyLatestTradingDetailsURL(textFNO), 8258, this);
					}
					else*/
					{
						go = true;
						text = niftyChoices[niftyType.getSelectedIndex()];
						textFNO = "_"+Expiry.getValue(dateChoice.getSelectedIndex())+"_"+txtFieldStrike.getText().trim()+"_"+(cepeChoice.getSelectedIndex()==0?"CE":"PE");
					}
				}else if(instruChoice.getSelectedIndex()==3 && !strikeMessage)
				{
					
						go = true;
						text = txtFieldSearchFNO.getText().trim().toUpperCase();
						textFNO = "_"+Expiry.getValue(dateChoice.getSelectedIndex())+"_"+txtFieldStrike.getText().trim()+"_"+(cepeChoice.getSelectedIndex()==0?"CE":"PE");	
				}
			}
			/*try{
			LOG.print(txtFieldSearch.getText());
			}
			catch(Exception e)
			{
				flag = false;
				LOG.print("Error");
				ScreenInvoker.showDialog("Please enter some keyword");
			}*/
			if(searchMessage || strikeMessage) {
					if((time+100)<System.currentTimeMillis())
					{time = System.currentTimeMillis();
					String msg = "";;
					if(searchMessage)
					{
						msg = "Please enter some keyword!";
						/*if(searchType.getSelectedIndex()==0 && txtFieldSearch.getText().trim().length()<2)
							msg = "Please enter at least two keyword!";*/
					}
					else if(strikeMessage)
						msg = "Please enter at least two digits strike price!";
					final String message = msg;
					UiApplication.getUiApplication().invokeLater(new Runnable()
					{
						public void run()
						{
							ScreenInvoker.showDialog(message);
						}
					});
					}

				}
				else if(go)
				{
					Vector vectorSearchData = new Vector();
					vectorSearchData.addElement(searchTypeText[SearchEFScreen.selectedIndex]);
					vectorSearchData.addElement(text);
					vectorSearchData.addElement(textFNO);
					vectorSearchData.addElement(textDateId);
					FNOSearchBean bean = new FNOSearchBean();
					bean.setCode(text);
					if(SearchEFScreen.selectedIndex==1)
						bean.setExpiry(dateChoice.getSelectedIndex());
					if(SearchEFScreen.selectedIndex==1 && !searchMessage )
					{
						if(instruChoice.getSelectedIndex()==2 || instruChoice.getSelectedIndex()==3)
						{
							bean.setOption((cepeChoice.getSelectedIndex()==0?"CE":"PE"));
							if(txtFieldStrike.getText().trim().length()==0)
							{
								bean.setStrike("Z");
							}
							else
								bean.setStrike(txtFieldStrike.getText().trim());
						}
						else
						{
							bean.setOption("");
							bean.setStrike("");
						}
					}
					vectorSearchData.addElement(bean.copy());
//					Dialog.alert("here  ");
					ActionInvoker.processCommand(new Action(Command, vectorSearchData));
				}
		}
		else
		{
//			Dialog.alert("here  else");
			
			ActionInvoker.processCommand(new Action(Command,data));
		}
			break;
			
		case ActionCommand.CMD_SEARCH_COMMODITY:
//			boolean searchMessage=false;
			if(SearchEFScreen.selectedIndex==2 && commoditytxtFieldSearch.getText().trim().length()==0)
			{
//				searchMessage = true;
				
				UiApplication.getUiApplication().invokeLater(new Runnable()
				{
					public void run()
					{
						ScreenInvoker.showDialog("Please enter some keyword!");
					}
				});
			}
			else
			{
				Vector commodityTextVector=new Vector();
				
				commodityTextVector.addElement("Commodity");
				
				commodityTextVector.addElement(commoditytxtFieldSearch.getText().trim());
				
				
				
				CommodityBean bean=new CommodityBean();
				
				commodityTextVector.addElement(bean.copy());
//				Dialog.alert("here  ");
				ActionInvoker.processCommand(new Action(Command, commodityTextVector));
			}
				
			
			break;
			
			
			
		default:
			Debug.debug("data : "+(data==null));
			ActionInvoker.processCommand(new Action(Command,data));
			break;
		}
	}

	public void addFI()
	{
		indexField.deleteAll();
		indexField.add(Utils.separator(10));
		indexField.add(instru);
		indexField.add(niftyManager);
		indexField.add(dateManager);
		indexField.add(objBtnSignUpFNO);
		indexField.add(Utils.separator(10));
		invalidate();
	}
	public void addFS()
	{
		UiApplication.getUiApplication().invokeLater(new Runnable()
		{
			public void run()
			{
		indexField.deleteAll();
		indexField.add(Utils.separator(10));
		indexField.add(instru);
		indexField.add(txtManagerFNO);
		indexField.add(dateManager);
		indexField.add(objBtnSignUpFNO);
		indexField.add(Utils.separator(10));
		invalidate();
			}});
	}
	public void addOI()
	{
		UiApplication.getUiApplication().invokeLater(new Runnable()
		{
			public void run()
			{
		indexField.deleteAll();
		indexField.add(Utils.separator(10));
		indexField.add(instru);
		indexField.add(niftyManager);
		indexField.add(dateManager);
		indexField.add(cepe);
		indexField.add(strike);
		indexField.add(Utils.separator(4));
		indexField.add(objBtnSignUpFNO);
		indexField.add(Utils.separator(10));
		invalidate();
			}});
	}
	public void addOS()
	{
		UiApplication.getUiApplication().invokeLater(new Runnable()
		{
			public void run()
			{
		indexField.deleteAll();
		indexField.add(Utils.separator(10));
		indexField.add(instru);
		indexField.add(txtManagerFNO);
		indexField.add(dateManager);
		indexField.add(cepe);
		indexField.add(strike);
		indexField.add(Utils.separator(4));
		indexField.add(objBtnSignUpFNO);
		indexField.add(Utils.separator(10));
		invalidate();
			}});
	}
	
	public ObjectChoiceField getCustomObjectChoiceFieldReg(String text,String[] values,final int firstIndex,long style,final int dropDownID) {

		return new ObjectChoiceField(text, values,firstIndex, ObjectChoiceField.FORCE_SINGLE_LINE | USE_ALL_WIDTH | FIELD_HCENTER) {
		public void setMinimalWidth(int width) {
			if(dropDownID == ID_INSTRU_CHOICE)
			{
				width = (AppConstants.screenWidth/2);
			}
			else if(dropDownID == ID_NIFTY_CHOICE)
			{
				width = AppConstants.screenWidth-40;
			}
			else if(dropDownID == ID_DATE_CHOICE)
			{
				width = (AppConstants.screenWidth/2);
			}
			else if(dropDownID == ID_CEPE_CHOICE)
			{
				width = (AppConstants.screenWidth/2);
			}
			super.setMinimalWidth(width);
		}
			protected void fieldChangeNotify(int context)
			{
				if(time+20<System.currentTimeMillis())
				dropDownChanged(dropDownID, getSelectedIndex());

			}
			public int getPreferredWidth() {
				if(dropDownID == ID_INSTRU_CHOICE)
				{
					return (AppConstants.screenWidth/2);
				}
				else if(dropDownID == ID_NIFTY_CHOICE)
				{
					return AppConstants.screenWidth/2;
				}
				else if(dropDownID == ID_DATE_CHOICE)
				{
					return (AppConstants.screenWidth/2);
				}
				else if(dropDownID == ID_CEPE_CHOICE)
				{
					return (AppConstants.screenWidth/2);
				}
				return (AppConstants.screenWidth/2)-20;
			}
			public int getPreferredHeight() {
				return  (FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4;
			}
			protected void layout(int width, int height) {
				setMinimalWidth(width);
				if(AppConstants.OS>5)
				super.layout(getPreferredWidth(), getPreferredHeight());
				setExtent(getPreferredWidth(), getPreferredHeight());
				//setExtent(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance("TradingPassw") + Snippets.padding*2, height);  // force the field to use all available space
			}
		};

	}
	public void dropDownChanged(int dropDownID, int selectedIndex)
	{
		if(dropDownID == ID_INSTRU_CHOICE)
		{
			if(selectedIndex==0) //Future Index
			{
				addFI();
			}
			else if(selectedIndex==1) //Future Stock
			{
				addFS();
			}
			else if(selectedIndex==2) //Option Index
			{
				addOI();
			}
			else if(selectedIndex==3) //Option Stock
			{
				addOS();
			}
		}
		else if(dropDownID == ID_NIFTY_CHOICE)
		{
			
		}
		else if(dropDownID == ID_DATE_CHOICE)
		{
			
		}
		else if(dropDownID == ID_CEPE_CHOICE)
		{
			
		}
	}

	public void fieldChanged(Field field, int arg1) {
		 try {
             if(field instanceof CustomBasicEditField){
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
             }
     } catch(Exception ex) {
             LOG.print("Exception occured");
     }
	}

	public void setReturnString(String string, int id) {
		if(id == 8258)
		{
			Vector vector = HomeJsonParser.getVector(string);
			if(vector.size()>0)
			{
				HomeJson hj = (HomeJson)vector.elementAt(0);
				if(hj.getLastTradedPrice()==null)
				{
					//actionPerfomed(ActionCommand.CMD_SEARCH_COMPANY, new Vector());
					SearchStocks stocks = new SearchStocks(new Vector());
					ScreenInvoker.pushScreen(stocks, true, true);
				}
				else if(hj.getLastTradedPrice().length()==0 || hj.getLastTradedPrice().equalsIgnoreCase("null") || hj.getLastTradedPrice().equalsIgnoreCase("No Data"))
				{
					//actionPerfomed(ActionCommand.CMD_SEARCH_COMPANY, new Vector());
					SearchStocks stocks = new SearchStocks(new Vector());
					ScreenInvoker.pushScreen(stocks, true, true);
				}
				else{
				CompanyFNODetailsSnippetsScreen csScreen = new CompanyFNODetailsSnippetsScreen(hj, 0, Utils.WATCHLIST_MODE);
				}
			}
			
		}
		
	}

	
	
	
	
	
	
	
	
	
	
	private class TopTabber extends Manager {

		LabelField lblBSETab = null;
		LabelField lblNSETab = null;
		LabelField lblGLOBALTab = null;
		private byte padding = 4;
		

		public TopTabber() {
			super(FOCUSABLE);

//			SearchEFScreen.selectedIndex = selectedIndex;

			
//			Dialog.alert("here");
			
			lblBSETab = new LabelField("Equity", FOCUSABLE) {

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getFont().getAdvance(getText())+2, getFont().getHeight());
				}

				public int getPreferredHeight() {
					return getFont().getHeight()+2;
				}

				public int getPreferredWidth() {
					return getFont().getAdvance(getText())+2;
				}

				protected void paint(Graphics graphics) {
					if(isFocus()) {
						graphics.setColor(Color.ORANGE);
						//graphics.drawRect(0, 0, getWidth(), getHeight());
						graphics.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
					}
					graphics.setColor(0xeeeeee);
					graphics.drawText(getText(), 0, 0);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
					//Dont do anything and let the system
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
					GLTabView.selectedIndex = 0;
//					actionListner.actionPerfomed(ActionCommand.CMD_BSE_GL_SCREEN, threadedComponents);
					setIndex((byte) 0);
					
					
					if(mainManager.getFieldCount()>1)
					{
						mainManager.deleteRange(1, mainManager.getFieldCount()-1);
						mainManager.add(searchBarManager);
					}
					else
						mainManager.add(searchBarManager);
					
					txtFieldSearch.setFocus();
					return super.navigationClick(status, time);
				}
				protected boolean touchEvent(TouchEvent arg0) 
				{
					return super.touchEvent(arg0);
				};
			};
			if(AppConstants.screenHeight>=480)
				lblBSETab.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			else
				lblBSETab.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));

			
//			Dialog.alert("here   1");
			
			
			lblNSETab = new LabelField("FNO", FOCUSABLE) {

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getFont().getAdvance(getText())+2, getFont().getHeight());
				}

				protected void paint(Graphics graphics) {
					if(isFocus()) {
						graphics.setColor(Color.ORANGE);
						//graphics.drawRect(0, 0, getWidth(), getHeight());
						graphics.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
					}
					graphics.setColor(0xeeeeee);
					graphics.drawText(getText(), 0, 0);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
					//Dont do anything and let the system
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
					GLTabView.selectedIndex = 1;
//					actionListner.actionPerfomed(ActionCommand.CMD_NSE_GL_SCREEN, threadedComponents);
					setIndex((byte) 1);
					if(mainManager.getFieldCount()>1)
					{
						mainManager.deleteRange(1, mainManager.getFieldCount()-1);
						mainManager.add(indexField);
					}
					else
						mainManager.add(indexField);
//					instruChoice.setFocus();
					
					return false;
				}
				protected boolean touchEvent(TouchEvent arg0) 
				{
					return super.touchEvent(arg0);
				};
			};
			if(AppConstants.screenHeight>=480)
				lblNSETab.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			else
				lblNSETab.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));

			
			
//			Dialog.alert("here     2");
			
			
			
			lblGLOBALTab = new LabelField("Commodity", FOCUSABLE) {

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getFont().getAdvance(getText())+2, getFont().getHeight());
				}

				protected void paint(Graphics graphics) {
					if(isFocus()) {
						graphics.setColor(Color.ORANGE);
						//graphics.drawRect(0, 0, getWidth(), getHeight());
						graphics.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
					}
					graphics.setColor(0xeeeeee);
					graphics.drawText(getText(), 0, 0);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
					//Dont do anything and let the system
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
					GLTabView.selectedIndex = 2;
					setIndex((byte) 2);
//					actionListner.actionPerfomed(ActionCommand.CMD_GLOBAL_GL_SCREEN, threadedComponents);
					
					
					if(mainManager.getFieldCount()>1)
					{
						
						mainManager.deleteRange(1, mainManager.getFieldCount()-1);
						
						mainManager.add(commoditySearchBarManager);
						
					}
					else
						mainManager.add(commoditySearchBarManager);
					
					commoditytxtFieldSearch.setFocus();
					return super.navigationClick(status, time);
				}
				protected boolean touchEvent(TouchEvent arg0) 
				{
					return super.touchEvent(arg0);
				};
			};
			if(AppConstants.screenHeight>=480)
				lblGLOBALTab.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			else
				lblGLOBALTab.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			add(lblBSETab);
			add(lblNSETab);
			add(lblGLOBALTab);
			
			
//			Dialog.alert("here     3");
			

		}
		

		protected void sublayout(int width, int height) {
			layoutChild(getField(0), width, height);
			layoutChild(getField(1), width, height);
			layoutChild(getField(2), width, height);
			setPositionChild(getField(0),0+ ((AppConstants.screenWidth/3)/2)-(getField(0).getPreferredWidth()/2), padding/2);
			setPositionChild(getField(1),(AppConstants.screenWidth/3)+ ((AppConstants.screenWidth/3)/2)-(getField(1).getPreferredWidth()/2), padding/2);
			setPositionChild(getField(2),((AppConstants.screenWidth/3)*2)+ ((AppConstants.screenWidth/3)/2)-(getField(2).getPreferredWidth()/2), padding/2);

			setExtent(AppConstants.screenWidth, getField(0).getPreferredHeight()+padding*2);
		}

		protected void drawFocus(Graphics graphics, boolean on) {

		}
public void setIndex(byte selectedIndex)
{
	SearchEFScreen.selectedIndex = selectedIndex;
	invalidate();
	}
		protected void paintBackground(Graphics graphics) {

			graphics.setColor(Color.GRAY);
			graphics.fillRect(0, 0, getWidth(), getHeight());

			graphics.setColor(Color.BLACK);
			if(selectedIndex==1)
			{
				graphics.fillRect(0, 0, (AppConstants.screenWidth/3), getField(0).getPreferredHeight()+padding);
				graphics.fillRect((AppConstants.screenWidth/3)*2, 0, (AppConstants.screenWidth/3), getField(0).getPreferredHeight()+padding);
			}
			else if(selectedIndex==2)
			{
				graphics.fillRect(0, 0, (AppConstants.screenWidth/3)*2, getField(0).getPreferredHeight()+padding);
				graphics.setColor(Color.GRAY);
				graphics.drawLine((AppConstants.screenWidth/3), 0, (AppConstants.screenWidth/3), getField(0).getPreferredHeight()+padding);
			}
			else
			{
				graphics.fillRect((AppConstants.screenWidth/3), 0, (AppConstants.screenWidth/3)*2, getField(0).getPreferredHeight()+padding);
				graphics.setColor(Color.GRAY);
				graphics.drawLine((AppConstants.screenWidth/3)*2, 0, (AppConstants.screenWidth/3)*2, getField(0).getPreferredHeight()+padding);
			}


		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
