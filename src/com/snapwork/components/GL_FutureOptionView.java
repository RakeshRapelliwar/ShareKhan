package com.snapwork.components;


import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.Expiry;
import com.snapwork.beans.HomeJson;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.view.SearchEFScreen;

/**
 * 
 * <p>This class provides tab view for Futures and Options screen.
 *
 */

public class GL_FutureOptionView extends VerticalFieldManager
{
	private static byte padding = 4; 
	private static short fieldWidth = -1,fieldHeight = -1;
	private String source;
	private RadioButtonGroup rbg = null;
	private RadioButtonField call = null;
	private RadioButtonField put = null;
	private BitmapField leftArrow = null;
	private BitmapField rightArrow = null;
	private LabelField dateLabel = null;
	private int month = 0;
	private int year = 0;
	private int _year; 
	public static int movemonth = 0; 
	private BasicEditField editField = null;
	private ActionListener actListener;
	private boolean NIFTY = false;
	private boolean MINIFTY = false;
	private boolean BANKNIFTY = false;
	private boolean CNXIT = false;
	private boolean NFTYMCAP50 = false; //"DJIA","CNXINFRA","CNXPSE","S&P500"
	private boolean DJIA = false;
	private boolean CNXINFRA = false;
	private boolean CNXPSE = false;
	private boolean SNP500 = false;
	private CustomObjectChoiceField niftyType;
	public static int niftyIndex  = 0;
	private FieldChangeListener fieldchange;
	public static String StrikePSTATIC = "";
	//private String choices[] = {"NIFTY","MINIFTY","BANKNITFY","CNXIT","NFTYMCAP50"};
	
	//private String choices[] = {"NIFTY","MINIFTY","BANKNITFY","CNXIT","NFTYMCAP50","DJIA","CNXINFRA","CNXPSE","S&P500"};

	private String choices[] = {"NIFTY","MINIFTY","BANKNITFY","CNXIT","NFTYMCAP50","DJIA","CNXINFRA","CNXPSE","S&P500"};
	
	private String optionChoices[] = {"NIFTY","MINIFTY","BANKNITFY","CNXIT","NFTYMCAP50"};
	
	public static String NIFTY_OPTIONS=null;
	protected void paintBackground(Graphics graphics) 
	{
		graphics.fillRect(0,0,getWidth(),getHeight());
		graphics.setColor(Color.BLACK);
	}
	public GL_FutureOptionView(Vector vector,ActionListener actionListener,String source, FieldChangeListener fieldchange)
	{
		this.source = source;
		this.actListener = actionListener;
		this.fieldchange = fieldchange;
		//AppConstants.optionsMonth = "";
		//AppConstants.optionsAmount = "";
		//AppConstants.optionsCEPE = "";
		//add(new ButtonField("Sample"));
		//System.out.println("Future and Options Response..... Status : True");
		LOG.print("source : - "+source);
		if(source.equalsIgnoreCase("Futures") && !AppConstants.OPTIONS_FLAG)
		{
			LOG.print("Futures ");
			StrikePSTATIC = "";
			AppConstants.optionsMonth = "";
			AppConstants.optionsAmount = "";
			AppConstants.optionsCEPE = "";
			movemonth = 0;
			boolean niftyFlag = false;
			if( AppConstants.source.equals("17023929"))
			{
				niftyFlag = true;
			}
			int j = 0;
			if(SearchEFScreen.niftyIndices==null)
			{
				LOG.print("----------------------->");
			for(int i=0;i<vector.size();i++)
			{
				
				final HomeJson homeJson = (HomeJson)vector.elementAt(i);
				LOG.print("-----------------------");
				LOG.print("homeJson.getCompanyCode() : "+homeJson.getCompanyCode());
				if(j==Expiry.size())
					j=0;
				//CustomFNOLabelField cfno = new CustomFNOLabelField(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),Expiry.getText(Integer.parseInt(homeJson.getCompanyCode().charAt(homeJson.getCompanyCode().length()-1)=='1'? homeJson.getCompanyCode().charAt(homeJson.getCompanyCode().length()-1)+homeJson.getCompanyCode().substring(homeJson.getCompanyCode().length()-7,homeJson.getCompanyCode().length()-5):homeJson.getCompanyCode().substring(homeJson.getCompanyCode().indexOf("_")+4,homeJson.getCompanyCode().indexOf("_")+6))),homeJson.getCompanyCode(),true);
				CustomFNOLabelField cfno = new CustomFNOLabelField(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),Expiry.getText(j),homeJson.getCompanyCode(),true,homeJson,Expiry.getValue(j), actListener);
				j++;
				cfno.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()),homeJson.getOpenInterest(), homeJson.getVolume(), homeJson);
				if(niftyFlag)
				{
					String name = homeJson.getCompanyCode().substring(0,homeJson.getCompanyCode().indexOf("_")>-1?homeJson.getCompanyCode().indexOf("_"):homeJson.getCompanyCode().length());
					if(!NIFTY && name.equalsIgnoreCase("NIFTY"))
					{
						add(getLabelField(" NIFTY"));
						NIFTY = true;
					}
					else if(!MINIFTY && name.equalsIgnoreCase("MINIFTY"))
					{
						add(getLabelField(" MINIFTY"));
						MINIFTY = true;
					}
					else if(!BANKNIFTY && name.equalsIgnoreCase("BANKNIFTY"))
					{
						add(getLabelField(" BANKNIFTY"));
						BANKNIFTY = true;
					}
					else if(!CNXIT && name.equalsIgnoreCase("CNXIT"))
					{
						add(getLabelField(" CNXIT"));
						CNXIT = true;
					}
					else if(!NFTYMCAP50 && name.equalsIgnoreCase("NFTYMCAP50"))
					{
						add(getLabelField(" NFTYMCAP50"));
						NFTYMCAP50 = true;
					}
					//"DJIA","CNXINFRA","CNXPSE","S&P500"
					else if(!DJIA && name.equalsIgnoreCase("DJIA"))
					{
						add(getLabelField(" DJIA"));
						DJIA = true;
					}
					else if(!CNXINFRA && name.equalsIgnoreCase("CNXINFRA"))
					{
						add(getLabelField(" CNXINFRA"));
						CNXINFRA = true;
					}
					else if(!CNXPSE && name.equalsIgnoreCase("CNXPSE"))
					{
						add(getLabelField(" CNXPSE"));
						CNXPSE = true;
					}
					else if(!SNP500 && name.equalsIgnoreCase("S-P500"))
					{
						add(getLabelField(" S&P500"));
						SNP500 = true;
					}
					//else
					//	add(getLabelField(name));
					/*//"DJIA","CNXINFRA","CNXPSE","S&P500"
					else if(!DJIA && name.equalsIgnoreCase("DJIA"))
					{
						add(getLabelField(" DJIA"));
						DJIA = true;
					}
					else if(!CNXINFRA && name.equalsIgnoreCase("CNXINFRA"))
					{
						add(getLabelField(" CNXINFRA"));
						CNXINFRA = true;
					}
					else if(!CNXPSE && name.equalsIgnoreCase("CNXPSE"))
					{
						add(getLabelField(" CNXPSE"));
						CNXPSE = true;
					}
					else if(!SNP500 && name.equalsIgnoreCase("S-P500"))
					{
						add(getLabelField(" S&P500"));
						SNP500 = true;
					}*/
					LOG.print(name);
				}
				LOG.print("Futures add labels");
				add(cfno);
			}
				
		}
		else{
			if( AppConstants.source.equals("17023929"))
			{
				int f=0;
				for(int x=0;x<SearchEFScreen.niftyIndices.length;x++)
				{
					add(getLabelField(SearchEFScreen.niftyIndices[x]));
					for(int h=0;h<Expiry.size();h++){
						if(f<vector.size()){
						if(vector.elementAt(f) instanceof HomeJson){
					HomeJson homeJson = (HomeJson)vector.elementAt(f);
					CustomFNOLabelField cfno = new CustomFNOLabelField(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),Expiry.getText(h),homeJson.getCompanyCode(),true,homeJson,Expiry.getValue(h), actListener);
					cfno.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()),homeJson.getOpenInterest(), homeJson.getVolume(), homeJson);
					add(cfno);
					f++;
						}
						}
					}
				}
			}
			else
			{
				int h=0;
				for(int s=0;s<vector.size();s++)
				{
					if(vector.elementAt(s) instanceof HomeJson){
						if(h==Expiry.size())
							h=0;
						HomeJson homeJson = (HomeJson)vector.elementAt(s);
						CustomFNOLabelField cfno = new CustomFNOLabelField(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),Expiry.getText(h),homeJson.getCompanyCode(),true,homeJson,Expiry.getValue(h), actListener);
						cfno.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()),homeJson.getOpenInterest(), homeJson.getVolume(), homeJson);
						add(cfno);
						h++;
							}
				}
			}
		}
			
		}
		else
		{
			/*
			 * Add here dropdown list for NIFTY
			 * Add NIFTY event on Get also instead of COMPANYCODE
			 */
			if( AppConstants.source.equals("17023929"))
			{
				if(SearchEFScreen.niftyIndices!=null)
					optionChoices = SearchEFScreen.niftyIndices;
				if(AppConstants.OS>5)
					niftyType = new CustomObjectChoiceField("", optionChoices,niftyIndex,USE_ALL_WIDTH | FIELD_HCENTER);
				else
					niftyType = new CustomObjectChoiceField("", optionChoices,niftyIndex,ObjectChoiceField.USE_ALL_WIDTH | FIELD_HCENTER);
				niftyType.setId(true);
				/*{
					protected void layout(int width, int height) {
						if(AppConstants.screenHeight>360)
						{	if(AppConstants.OS == 5)
								super.layout(width, height); //setExtent(AppConstants.screenWidth - 20, (getFont().getHeight()*2)+(getFont().getHeight()/3));
							else
								{
								LOG.print("width : "+width);
								LOG.print("height : "+height);
								//setExtent(AppConstants.screenWidth - 20, (getFont().getHeight()*2)+(getFont().getHeight()/3));
								super.layout(AppConstants.screenWidth - 20, height);
									}
						}
						else
							super.layout(width, height); //setExtent(getPreferredWidth(), getPreferredHeight());  // force the field to use all available space
					}
				};*/
				niftyType.setMinimalWidth(AppConstants.screenWidth);
				add(niftyType);
			}
			rbg = new RadioButtonGroup();
			call = new RadioButtonField("Call",rbg,true, RadioButtonField.FIELD_RIGHT)
			{
				protected void paint(Graphics graphics) 
				{
					graphics.setColor(0xeeeeee);
					if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
						graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
					super.paint(graphics);
				}
				protected boolean navigationClick(int keycode,
						int time){
					setSelected(true);
					return true;
				}
				/*protected void layout(int width, int height)
				{
					if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
					{
						super.layout(width, height*3);
					}
					else
					{
						super.layout(width, height);
					}
				};*/
			}; 
			put = new RadioButtonField("Put",rbg,false, RadioButtonField.FIELD_RIGHT)
			{ 
				protected void paint(Graphics graphics) 
				{
					graphics.setColor(0xeeeeee);
					if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
						graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
					super.paint(graphics);
				}
				protected boolean navigationClick(int keycode,
						int time)
				{
					setSelected(true);
					return true;
				}
				/*protected void layout(int width, int height)
				{
					if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
					{
						super.layout(width, height*5);
					}
					else
					{
						super.layout(width, height);
					}
				};*/
			};
			RadioButtonLayout v = new RadioButtonLayout();
			v.add(call);
			v.add(put);
			add(v);
			DateButtonLayout dv = new DateButtonLayout();
			final int arrowWidth = (short)ImageManager.getRightArrowImage().getWidth();
			final int arrowHeight = (short)ImageManager.getRightArrowImage().getHeight();
			this.leftArrow = new BitmapField(null,FOCUSABLE) 
			{
				protected boolean navigationClick(int status, int time) 
				{
					prev();
					return super.navigationClick(status, time);
				}
				protected void drawFocus(Graphics graphics, boolean on) 
				{

				}

				protected void onFocus(int direction) 
				{
					super.onFocus(direction);
					invalidate();
				}

				protected void onUnfocus() 
				{
					super.onUnfocus();
					invalidate();
				}

				protected void paint(Graphics graphics) 
				{
					if(movemonth!=0)
					{if(isFocus()) 
					{
						graphics.setColor(Color.ORANGE);
						graphics.drawLine(0,  arrowHeight+1,arrowWidth+2, arrowHeight+1);
					}
					graphics.drawBitmap(1, 1, arrowWidth, arrowHeight, ImageManager.getLeftArrowImage(), 0, 0);
					}invalidate();
				}
				public int getPreferredWidth() {return arrowWidth+2;
				}

				protected void layout(int width, int height) 
				{
					setExtent(arrowWidth+2, arrowHeight+2);
				}

			};

			this.rightArrow = new BitmapField(ImageManager.getRightArrowImage(),FOCUSABLE)
			{

				protected boolean navigationClick(int status, int time)
				{
					next();
					return super.navigationClick(status, time);
				}

				protected void drawFocus(Graphics graphics, boolean on)
				{

				}

				protected void onFocus(int direction) {
					super.onFocus(direction);
					invalidate();
				}

				protected void onUnfocus() {
					super.onUnfocus();
					invalidate();
				}

				protected void paint(Graphics graphics) {
					if(movemonth!=2)
					{
					if(isFocus()) {
						graphics.setColor(Color.ORANGE);
						graphics.drawLine(0,  arrowHeight+1,arrowWidth+2, arrowHeight+1);
					}
					graphics.drawBitmap(1, 1, arrowWidth, arrowHeight, ImageManager.getRightArrowImage(), 0, 0);
					}
					invalidate();
				}
				public int getPreferredWidth() {return arrowWidth+2;}
				protected void layout(int width, int height) {
					setExtent(arrowWidth+2, arrowHeight+2);
				}

			};
			this.dateLabel = new LabelField(Expiry.getTextWithYear(0), NON_FOCUSABLE | FIELD_HCENTER | FIELD_VCENTER){
				protected void paint(Graphics graphics) 
				{
					graphics.setColor(Color.BLACK);
					graphics.fillRect(0, 0, getWidth(), getHeight());
					graphics.setColor(0xeeeeee);
					if(AppConstants.screenHeight >= 480)
						graphics.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
					else
						graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
					if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
						graphics.drawText(getText(), 10, 5+((arrowHeight+2)/2)-(graphics.getFont().getHeight()/2));
					else 
						graphics.drawText(getText(), 10, ((arrowHeight+2)/2)-(graphics.getFont().getHeight()/2));
					graphics.setColor(Color.BLACK);
					graphics.setFont(FontLoader.getFont(AppConstants.EXTRA_SMALL_BOLD_FONT));
					if(AppConstants.OS<6)
						super.paint(graphics);
				}
				public int getPreferredWidth() {return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance(getText())+20;}
				protected void layout(int width, int height) 
				{
					if(AppConstants.screenHeight >= 480)
						setExtent(FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getAdvance(getText())+20, arrowHeight+2);
					else
						setExtent(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance(getText())+20, arrowHeight+2);
				}
			};
			dv.setHt(arrowHeight+2);
			dv.add(this.leftArrow);
			dv.add(this.dateLabel);
			dv.add(this.rightArrow);
			add(dv);
			HLayout strikep = new HLayout();
			LabelField label;
			//if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
			if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			{
				label = new LabelField("Strike Price", NON_FOCUSABLE | FIELD_HCENTER | FIELD_VCENTER){
					protected void paint(Graphics graphics) {
						graphics.setColor(0xeeeeee);
						if(AppConstants.screenHeight==480)
							graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
						else
							graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
						//graphics.drawText("Strike Price", 0, 2+((arrowHeight+2)/2)-(graphics.getFont().getHeight()/2));
						super.paint(graphics);
					}
					protected void layout(int width, int height) 
					{
						if(AppConstants.screenHeight==480)
							setExtent(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance("Strike Price")+40, arrowHeight+2);
						else
							setExtent(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance("Strike Price")+20, arrowHeight+2);
					}
				};
			}
			else
			{
				label = new LabelField("Strike Price", NON_FOCUSABLE | FIELD_HCENTER | FIELD_VCENTER){
					protected void paint(Graphics graphics) {
						graphics.setColor(0xeeeeee);
						if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
							graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
						else
							graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
						if(AppConstants.OS < 6)
							graphics.drawText(getText(), 10, ((arrowHeight+2)/2)-(graphics.getFont().getHeight()/2));
						super.paint(graphics);
					}
					
					
					protected void layout(int width, int height) {
						setExtent(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance("Strike Price")+20, arrowHeight+2);
					}
				};
			}
			strikep.add(label);
			editField = new BasicEditField("", StrikePSTATIC, 9,BasicEditField.NO_NEWLINE | BasicEditField.FOCUSABLE | BasicEditField.EDITABLE | BasicEditField.FILTER_REAL_NUMERIC) 
			{

				public int getPreferredHeight() 
				{
					return  arrowHeight+2;
				}

				public int getPreferredWidth() 
				{
					return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance("0000000000"); 
				}
				protected void paintBackground(Graphics graphics) 
				{
					graphics.setColor(0xeeeeee);
					graphics.fillRect(0,0,getPreferredWidth(),getPreferredHeight());
					graphics.setColor(Color.BLACK);
				}

				protected void paint(Graphics graphics) 
				{
					graphics.setFont(this.getFont());
					graphics.setColor(Color.BLACK);
					super.paint(graphics);
				}
				/*protected void fieldChangeNotify(int arg0)
				{
					 try {
				        	LOG.print("Field Changed method arg0 " + arg0);
				        	
				                if(getText().indexOf(".")!=-1) {
				                	LOG.print("inner Field Changed method");
				                        int lastIndex = getText().indexOf(".", getText().indexOf(".")+1);
				                        if(lastIndex==-1) {
				                                int dotIndex = getText().indexOf(".");
				                                if((dotIndex+2)<(getText().length()-1)) {
				                                        setText(getText().substring(0, dotIndex+3));
				                                }
				                                if(dotIndex==0) {
				                                        setText("0"+getText());
				                                }
				                        } else {
				                                setText("");
				                        }
				                }
				        } catch(Exception ex) {
				                LOG.print("Exception occured");
				        }
				}*/
				protected boolean keyDown(int keycode, int time)
				{
					int key = Keypad.key(keycode);
					int status = Keypad.status(keycode);
					char ch = Keypad.map(key, status);
					LOG.print("Keypad.map(key, status) : "+ch);
					if(getText().indexOf('.')>-1 && getText().indexOf('.')+3==getText().length())
					{	if(Keypad.key(keycode)==Keypad.KEY_DELETE)
					{
						 if(getText().length()>0)setText(getText().substring(0, getText().length()-1));
					}
					else if(Keypad.key(keycode)==Keypad.KEY_BACKSPACE){ if(getText().length()>0)setText(getText().substring(0, getText().length()-1));}
					else return true;}
					else if(ch == 'w' || ch =='W' || ch =='1') {ch = '1';setText(getText()+ch);}
					else if(ch == 'e' || ch =='E' || ch =='2') {ch = '2';setText(getText()+ch);}
					else if(ch == 'r' || ch =='R' || ch =='3') {ch = '3';setText(getText()+ch);}
					else if(ch == 's' || ch =='S' || ch =='4') {ch = '4';setText(getText()+ch);}
					else if(ch == 'd' || ch =='D' || ch =='5') {ch = '5';setText(getText()+ch);}
					else if(ch == 'f' || ch =='F' || ch =='6') {ch = '6';setText(getText()+ch);}
					else if(ch == 'z' || ch =='Z' || ch =='7') {ch = '7';setText(getText()+ch);}
					else if(ch == 'x' || ch =='X' || ch =='8') {ch = '8';setText(getText()+ch);}
					else if(ch == 'c' || ch =='C' || ch =='9') {ch = '9';setText(getText()+ch);}
					else if(ch == '0' || ch =='?'/* || ch =='0'*/) {ch = '0';setText(getText()+ch);}
					else if(ch == '.'){if(getText().indexOf('.')<0)setText(getText()+ch);}
					else if(Keypad.key(keycode)==Keypad.KEY_DELETE)
					{
						if(getText()!=null)
							{if(getText().indexOf('.')<0 && getText().length()>0)
							{	setText(getText()+ch);}
								else setText(getText()+ch);
									}
					}
					else if(Keypad.key(keycode)==Keypad.KEY_BACKSPACE){ if(getText().length()>0)setText(getText().substring(0, getText().length()-1));}
					return true;
				}
				protected boolean isSymbolScreenAllowed() {return true;}

			};
		/*	editField = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_REAL_NUMERIC)
			{

				public int getPreferredHeight() 
				{
					return  arrowHeight+2;
				}

				public int getPreferredWidth() 
				{
					return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance("0000000000"); 
				}};*/
			//editField.setChangeListener(this.fieldchange);
			strikep.add(editField);
			final ActionListener al = this.actListener;
			ButtonField  fnoButton = new ButtonField ("Get", LabelField.FOCUSABLE | FIELD_HCENTER | FIELD_VCENTER)
			{
				protected boolean navigationClick(int status, int time) 
				{
					AppConstants.OPTIONS_FLAG_First = false;
					if(editField.getText()!=null) {
						if(editField.getText().length()==0) {
							Dialog.alert("Error! Please enter valid strike price");
							return super.navigationClick(status, time);
						}
						double amt = 0;
						if(editField.getText().length()>0)
							amt = Double.parseDouble(editField.getText());
						if(editField.getText().length()>0 && amt!=0) {
							AppConstants.MOVE_MONTH = movemonth;
							AppConstants.optionsMonth=Expiry.getValue(getMoveMonth());
							AppConstants.optionsAmount=editField.getText();
							StrikePSTATIC = editField.getText();
							if(rbg.getSelectedIndex()==0)
								AppConstants.optionsCEPE="CE";
							else
								AppConstants.optionsCEPE="PE";
							if( AppConstants.source.equals("17023929")) {
								NIFTY_OPTIONS = optionChoices[niftyType.getSelectedIndex()];
								niftyIndex =  niftyType.getSelectedIndex();
							} else {
								NIFTY_OPTIONS = null;
							}
							actionPerfomed(ActionCommand.CMD_OPTION_SCREEN, null);
						}
						else{
							actionPerfomed(ActionCommand.CMD_OPTION_SCREEN, null);
						}
					}
					return super.navigationClick(status, time);
				}
				public int getPreferredHeight() 
				{
					return  FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight();
				}

				public int getPreferredWidth() 
				{
					return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance("GET"); 
				}
			};
			strikep.add(fnoButton);
			/*if(vector.size()==0 && !AppConstants.OPTIONS_FLAG_First)
			{
				LabelField zero = new LabelField("Enter Valid Strike Price", NON_FOCUSABLE | FIELD_HCENTER | FIELD_VCENTER){
					protected void paint(Graphics graphics) {
						graphics.setColor(0xeeeeee);
						graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
						graphics.drawText(getText(), (getPreferredWidth()/2)-(graphics.getFont().getAdvance(getText())/2), ((arrowHeight+2)/2)-(graphics.getFont().getHeight()/2));
						super.paint(graphics);
					}
					public int getPreferredWidth() {return AppConstants.screenWidth-10;}
					protected void layout(int width, int height) {
						setExtent(AppConstants.screenWidth-10, arrowHeight+2);
					}
				};
				strikep.add(zero);
			}
			else*/
			if(StrikePSTATIC.length()!=0){
				if(AppConstants.optionsCEPE.length()>0){
					if( AppConstants.source.equals("17023929"))
					{
						if(niftyType != null)
							niftyType.setSelectedIndex(niftyType);
					}
				int j = AppConstants.MOVE_MONTH; 
				movemonth = j;
				editField.setText(AppConstants.optionsAmount);
				dateLabel.setText(Expiry.getTextWithYear(movemonth));
				if(AppConstants.optionsCEPE.equalsIgnoreCase("PE"))
					rbg.setSelectedIndex(1);
				else
					rbg.setSelectedIndex(0);
				HomeJson hjx = (HomeJson)vector.elementAt(0);
				if(hjx.getLastTradedPrice().equalsIgnoreCase("No Data"))
				{
					NoRecordFoundField nrf = new NoRecordFoundField("No Available Contracts!", FOCUSABLE);
					strikep.add(nrf);
				}
			else{
				
				for(int i=0;i<vector.size();i++){
					try {
						HomeJson homeJson = (HomeJson)vector.elementAt(i);
						//CustomFNOLabelField cfno = new CustomFNOLabelField(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),Expiry.getText(Integer.parseInt(homeJson.getCompanyCode().charAt(homeJson.getCompanyCode().length()-1)=='1'? homeJson.getCompanyCode().charAt(homeJson.getCompanyCode().length()-1)+homeJson.getCompanyCode().substring(homeJson.getCompanyCode().length()-7,homeJson.getCompanyCode().length()-5):homeJson.getCompanyCode().substring(homeJson.getCompanyCode().indexOf("_")+4,homeJson.getCompanyCode().indexOf("_")+6))),homeJson.getCompanyCode(),false);
						if(!AppConstants.OPTIONS_FLAG_First)
						if(movemonth<Expiry.size())
						{
							CustomFNOLabelField cfno = new CustomFNOLabelField(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT),Expiry.getText(movemonth),homeJson.getCompanyCode(),false,homeJson,Expiry.getValue(movemonth), actListener)
							{
								public int getPreferredWidth() {
									return AppConstants.screenWidth;
								}
							};
							cfno.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()),homeJson.getOpenInterest(), homeJson.getVolume(), homeJson);
							strikep.add(cfno);
						}
					} catch (Exception e) 
					{
					}
				}
				invalidate();
			}
			}
			}
			add(strikep);
			
		}
	}
	public void actionPerfomed(byte Command, Object sender) 
	{
		switch (Command) {
		case ActionCommand.CMD_COMPANY_DETAILS_SCREEN:
			ActionInvoker.processCommand(new Action(Command, sender));
			break;
		default:
			ActionInvoker.processCommand(new Action(Command, sender));
			break;
		}
	}
	
	public int getPreferredHeight() 
	{
		try 
		{
			return AppConstants.screenHeight;
		} 
		catch(Exception ex) 
		{
		}
		return 0;
	}
	public void next() 
	{
		if(movemonth<2)
		{
			movemonth++;
		}
		dateLabel.setText(Expiry.getTextWithYear(movemonth));
	}
	public void prev() 
	{
		if(movemonth>0)
		{
			movemonth--;
		}
		dateLabel.setText(Expiry.getTextWithYear(movemonth));
	}

	public int getMoveMonth() 
	{
		return movemonth;
	}
	private class TabbedList extends VerticalFieldManager 
	{

		LabelField lblListTitle = null;
		private final static int titleColor = 6521476;
		public TabbedList(String strTitle) 
		{
			super(FOCUSABLE);

			lblListTitle = new LabelField(strTitle, GLTabView.NON_FOCUSABLE) 
			{

				protected void layout(int width, int height) 
				{
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}

				public int getPreferredHeight()
				{
					return getFont().getHeight()+padding;
				}

				public int getPreferredWidth()
				{
					return AppConstants.screenWidth;
				}

				protected void paint(Graphics graphics) 
				{
					graphics.setColor(titleColor);
					graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
					graphics.setColor(0xeeeeee);
					graphics.drawText(getText(), padding/2, padding/2);
				}

				protected void drawFocus(Graphics graphics, boolean on) 
				{

				}
			};
			lblListTitle.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			add(lblListTitle);
		}

		private void addItem(final String companyName,final String price,final String changedPrice,final String perchantage,final String companyCode,final ActionListener actionListner) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					add(new TabbedListItem(FOCUSABLE, companyName, price, changedPrice, perchantage,companyCode,actionListner));
				}
			});
		}
	}
	public class HLayout extends Manager
	{
		private int ht;
		public HLayout()
		{
			super(Manager.VERTICAL_SCROLL | Manager.RIGHTMOST);
		}

		protected void sublayout(int width, int height) 
		{
			if(AppConstants.screenHeight>=480)
			{
				setPositionChild(getField(0),(AppConstants.screenWidth/2)-((getField(0).getPreferredWidth()+getField(1).getPreferredWidth()+getField(2).getPreferredWidth())/2)-28,16); //set the position for the field
				layoutChild(getField(0), width, height); //lay out the field
				setPositionChild(getField(1),getField(0).getPreferredWidth()+(AppConstants.screenWidth/2)-((getField(0).getPreferredWidth()+getField(1).getPreferredWidth()+getField(2).getPreferredWidth())/2)-3,16); //set the position for the field
			}
			else
			{
				setPositionChild(getField(0),(AppConstants.screenWidth/2)-((getField(0).getPreferredWidth()+getField(1).getPreferredWidth()+getField(2).getPreferredWidth())/2)-30,0); //set the position for the field
				layoutChild(getField(0), width, height); //lay out the field
				setPositionChild(getField(1),getField(0).getPreferredWidth()+(AppConstants.screenWidth/2)-((getField(0).getPreferredWidth()+getField(1).getPreferredWidth()+getField(2).getPreferredWidth())/2)-3,8); //set the position for the field
			}
			layoutChild(getField(1), width, height); //lay out the field
			if(AppConstants.screenHeight>=480)
				setPositionChild(getField(2),getField(0).getPreferredWidth()+getField(1).getPreferredWidth()+(AppConstants.screenWidth/2)-((getField(0).getPreferredWidth()+getField(1).getPreferredWidth()+getField(2).getPreferredWidth())/2),4);
			else
				setPositionChild(getField(2),getField(0).getPreferredWidth()+getField(1).getPreferredWidth()+(AppConstants.screenWidth/2)-((getField(0).getPreferredWidth()+getField(1).getPreferredWidth()+getField(2).getPreferredWidth())/2),4);
			layoutChild(getField(2), width, height);
			int numberOfFields = getFieldCount();
			if(numberOfFields>3)
			{
				int i=3;
				int y =  getField(0).getPreferredHeight()+18;
				if(AppConstants.screenHeight>=480)
					y = y+30;
				//else if(AppConstants.screenHeight>240)
				//	y = y +10;
				for(;i<numberOfFields;i++)
				{
					setPositionChild(getField(i),0,y + (getField(i).getPreferredHeight()*(i-3)*((i-3)*4)));
					layoutChild(getField(i), width, height);
				}
			}
			setExtent(width, height);
		}
	}

	public class VLayout extends Manager
	{
		public VLayout() 
		{
			super(Manager.VERTICAL_SCROLL | Manager.RIGHTMOST);
		}

		protected void sublayout(int width, int height)
		{
			setPositionChild(getField(0),0,0); //set the position for the field
			layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight()); //lay out the field
			setExtent(getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
		}
	}
	private class TabbedListItem extends Manager 
	{
		private LabelField lblCompanyName = null;
		private LabelField lblValue = null;
		private LabelField lblChangedValue = null;
		private LabelField lblPercentage = null;
		private final static int itemBackColor = 4343106;
		private boolean requireNullField = true;
		private boolean isGainer = true;
		private ActionListener actionListner;
		private String companyCode = null;
		public TabbedListItem(long style,String companyName,String value,String changedValue,String percentage,String companyCode,ActionListener actionListner) 
		{
			super(style);
			isGainer = (changedValue.indexOf("-")==-1);
			setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			if(fieldWidth==-1)
				fieldWidth = (short)(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getAdvance("##########")+padding);
			if(fieldHeight == -1)
				fieldHeight = (short)(getPreferredHeight()-padding);
			this.actionListner = actionListner;
			this.companyCode = companyCode;
			final int companyFieldWidth = getPreferredWidth()-fieldWidth+10-padding*2-getFont().getAdvance(value);
			lblCompanyName = new LabelField(companyName, 0)
			{
				public int getPreferredHeight() 
				{
					return getFont().getHeight();
				}

				public int getPreferredWidth() 
				{
					return companyFieldWidth;
				}

				protected void paint(Graphics graphics) 
				{
					graphics.setColor(0xeeeeee);
					super.paint(graphics);
				}

				protected void drawFocus(Graphics graphics, boolean on) 
				{
				}

				protected void layout(int width, int height) 
				{
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
			};

			lblValue  = new LabelField(value, 0) 
			{

				public int getPreferredHeight() 
				{
					return getFont().getHeight();
				}

				protected void paint(Graphics graphics) {
					graphics.setColor(0xeeeeee);
					super.paint(graphics);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
				}

				public int getPreferredWidth() {
					return getFont().getAdvance(getText())+5;
				}

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
			};

			lblChangedValue = new LabelField(changedValue, 0) 
			{
				public int getPreferredHeight() 
				{
					return getFont().getHeight();
				}

				protected void paint(Graphics graphics) {
					graphics.setColor(0xeeeeee);
					super.paint(graphics);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
				}

				public int getPreferredWidth() {
					return getFont().getAdvance(getText())+5;
				}

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
			};

			lblPercentage = new LabelField(percentage, 0) {

				public int getPreferredHeight() {
					return getFont().getHeight();
				}

				protected void paint(Graphics graphics) {
					graphics.setColor(0xeeeeee);
					super.paint(graphics);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
				}

				public int getPreferredWidth() {
					return getFont().getAdvance(getText())+5;
				}

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
			};

			lblCompanyName.setFont(getFont());
			lblValue.setFont(getFont());
			lblChangedValue.setFont(getFont());
			lblPercentage.setFont(getFont());
			add(lblCompanyName);
			add(lblValue);
			add(lblChangedValue);
			add(lblPercentage);
			requireNullField = true;
			for(int i=0;i<this.getFieldCount();i++)
			{
				if(this.getField(i).isFocusable())
				{
					requireNullField = false;
					i=this.getFieldCount();
				}
			}

			if(requireNullField)
			{
				NullField objNullField = new NullField(FOCUSABLE) 
				{
					protected void onFocus(int direction) 
					{
						if(direction==-1) 
						{
							this.setPosition(0, 0);
						}
						else
						{
							this.setPosition(this.getManager().getWidth(), this.getManager().getHeight());
						}
						super.onFocus(direction);
					}

					protected void onUnfocus()
					{
						super.onUnfocus();
					}
				};
				add(objNullField);//A Field which will show focus on manager
			}

		}

		public int getTotalFields()
		{
			if(requireNullField)
				return this.getFieldCount()-1;
			else
				return this.getFieldCount(); 
		}

		public int getPreferredHeight() 
		{
			return getFont().getHeight()*2+padding*2;
		}

		public int getPreferredWidth() 
		{
			return AppConstants.screenWidth;
		}

		protected void onFocus(int direction) 
		{
			super.onFocus(direction);
			invalidate();
		}

		protected void onUnfocus()
		{
			super.onUnfocus();
			invalidate();
		}

		protected boolean navigationClick(int status, int time) 
		{
			Vector vectorCommandData = new Vector();
			vectorCommandData.addElement(lblCompanyName.getText());
			vectorCommandData.addElement(companyCode);
			vectorCommandData.addElement(source);
			actionListner.actionPerfomed(ActionCommand.CMD_COMPANY_DETAILS_SCREEN, vectorCommandData);
			return super.navigationClick(status, time);
		}

		protected void paintBackground(Graphics graphics) {
			graphics.setColor(isFocus() == true ? Color.ORANGE : Color.BLACK);
			graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
			graphics.setColor(itemBackColor);
			graphics.fillRoundRect(padding/2, padding/2, getPreferredWidth()-padding, getPreferredHeight()-padding, 10, 10);
			graphics.setColor(isFocus() == true ? Color.ORANGE : Color.BLACK);
			graphics.fillRect(getPreferredWidth()-padding/2-fieldWidth, padding/2, fieldWidth, fieldHeight);
			graphics.setColor(isGainer == true ? Color.GREEN : Color.RED);
			graphics.fillRoundRect(getPreferredWidth()-padding/2-fieldWidth, padding/2, fieldWidth, fieldHeight,10,10);
			graphics.setColor(0xeeeeee);
			graphics.drawLine(getPreferredWidth()-padding/2-fieldWidth+10, padding/2, getPreferredWidth()-padding/2-fieldWidth+10, fieldHeight);
			graphics.drawLine(getPreferredWidth()-padding/2-fieldWidth, padding/2+fieldHeight/2, getPreferredWidth()-padding/2-1, padding/2+fieldHeight/2);
			graphics.setColor(itemBackColor);
			graphics.fillRect(getPreferredWidth()-padding/2-fieldWidth, padding/2, 10, fieldHeight);
		}

		protected void sublayout(int width, int height) 
		{
			layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
			layoutChild(getField(2), getField(2).getPreferredWidth(), getField(2).getPreferredHeight());
			layoutChild(getField(3), getField(3).getPreferredWidth(), getField(3).getPreferredHeight());
			setPositionChild(getField(0), padding, getPreferredHeight()/2 - getField(0).getPreferredHeight()/2);
			setPositionChild(getField(1), padding+getField(0).getPreferredWidth(), getPreferredHeight()/2 - getField(1).getPreferredHeight()/2);
			setPositionChild(getField(2), getPreferredWidth()-padding/2-fieldWidth+10+((fieldWidth-10)/2-getField(2).getPreferredWidth()/2), padding);
			setPositionChild(getField(3), getPreferredWidth()-padding/2-fieldWidth+10+((fieldWidth-10)/2-getField(3).getPreferredWidth()/2), getFont().getHeight()+padding);
			if(requireNullField) 
			{
				layoutChild(this.getField(this.getFieldCount()-1), 1 , 1);
				setPositionChild(this.getField(this.getFieldCount()-1), AppConstants.screenWidth/2, getPreferredHeight()/2);
			}
			setExtent(getPreferredWidth(), getPreferredHeight());
		}
	}
	
	
	public LabelField getLabelField(String strTitle)
	{
		return new LabelField(strTitle, NON_FOCUSABLE) 
		{
			protected void layout(int width, int height) 
			{
				super.layout(width, height);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}

			public int getPreferredHeight() 
			{
				return (FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight()+8)+(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight()/2);
			}

			public int getPreferredWidth() 
			{
				return AppConstants.screenWidth;
			}

			protected void paint(Graphics graphics) 
			{
				graphics.setColor(0x000000);
				graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
				graphics.setColor(0x639084);
				graphics.fillRect(0, 4, getPreferredWidth(), getPreferredHeight()-8);
				graphics.setColor(0xeeeeee);
				graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
				graphics.drawText(getText(), padding/2, (getPreferredHeight()/2)-(graphics.getFont().getHeight()/2));
				
			}

			protected void drawFocus(Graphics graphics, boolean on) 
			{

			}
		};
	}
	public boolean onSavePrompt() {
		return true;
	}
	
}
