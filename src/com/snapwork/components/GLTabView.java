package com.snapwork.components;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.CrudeOil;
import com.snapwork.beans.ForexList;
import com.snapwork.beans.Market;
import com.snapwork.interfaces.ReturnData;
import com.snapwork.beans.TopGainLoseItem;
import com.snapwork.parsers.Json;
import com.snapwork.parsers.TopGainLoseItemParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

/**
 * 
 *<p>This class provides tab view for Top Gainers-Losers View.
 *<p>Tab Options are BSE, NSE and Global
 */
public class GLTabView extends VerticalFieldManager
{

	public static byte selectedIndex = 0;
	private static byte padding = 4; 
	private static short fieldWidth = -1,fieldHeight = -1;
	private String source;
	private Vector asiaData;
	private Vector europeData;
	private Vector americaData;
	private Vector forexData;
	private Vector crudeData;
	private ActionListener actionListenRefresh;
	private boolean refreshFlag;
	private boolean startUpdate;
	private byte borderWidth = 3;
	private int h = 0 ;
	private boolean hLess = false;
	private Thread animationThread;
	private long timer;
	private Font font;
	public GLTabView(Vector vector,ActionListener actionListener,String source)
	{
		this.source = source;
		AppConstants.source = source;
		actionListenRefresh = actionListener;
		refreshFlag = true;
		timer = System.currentTimeMillis();
		LOG.print("TopGL vector.size() "+vector.size());
		if(source.equalsIgnoreCase("Global"))
		{
			TabbedList tabbedAsia = new TabbedList("Asia");
			TabbedList tabbedEurope = new TabbedList("Europe");
			TabbedList tabbedAmerica = new TabbedList("America");
			TabbedList tabbedForex = new TabbedList("Forex");

			this.asiaData= (Vector) vector.elementAt(0);
			this.europeData= (Vector) vector.elementAt(1);
			this.americaData= (Vector) vector.elementAt(2);
			this.crudeData =  (Vector) vector.elementAt(3);
			this.forexData= (Vector) vector.elementAt(5);
			for(int j=0;j<this.asiaData.size();j++)
			{
				Market marketBean = (Market)this.asiaData.elementAt(j);
				String txt = marketBean.getExchangeName();
				/*if(txt.indexOf('&')>-1)
				{
					String txt2="";
					txt2 = txt.substring(0, txt.indexOf('&')+1) + txt.substring(txt.indexOf('&')+5, txt.length());
					txt=txt2;
				}*/
				String change = marketBean.getChangePts();
				String changePer = marketBean.getChangePtsPer().substring(0, marketBean.getChangePtsPer().length()-1);
				if(change.indexOf("+")>-1 )
				{
					change = marketBean.getChangePts().substring(1, marketBean.getChangePts().length());
				}
				if(changePer.indexOf("+")>-1 )
				{
					changePer = marketBean.getChangePtsPer().substring(1, marketBean.getChangePtsPer().length()-1);
				}
				if(marketBean.getChangePtsPer().indexOf("-")==-1)
				{
					tabbedAsia.addItem(txt, marketBean.getLastTradedPrice(), "+"+change, "+"+changePer+"%","0","",actionListener);
				} 
				else 
				{
					tabbedAsia.addItem(txt, marketBean.getLastTradedPrice(), change, changePer+"%","0","",actionListener);				
				}

			}
			for(int j=0;j<this.europeData.size();j++)
			{
				Market marketBean = (Market)this.europeData.elementAt(j);
				String txt = marketBean.getExchangeName();
				/*if(txt.indexOf('&')>-1)
				{
					String txt2="";
					txt2 = txt.substring(0, txt.indexOf('&')+1) + txt.substring(txt.indexOf('&')+5, txt.length());
					txt=txt2;
				}*/
				String change = marketBean.getChangePts();
				String changePer = marketBean.getChangePtsPer().substring(0, marketBean.getChangePtsPer().length()-1);
				if(change.indexOf("+")>-1 )
				{
					change = marketBean.getChangePts().substring(1, marketBean.getChangePts().length());
				}
				if(changePer.indexOf("+")>-1 )
				{
					changePer = marketBean.getChangePtsPer().substring(1, marketBean.getChangePtsPer().length()-1);
				}
				if(marketBean.getChangePtsPer().indexOf("-")==-1) 
				{
					tabbedEurope.addItem(txt, marketBean.getLastTradedPrice(), "+"+change, "+"+changePer+"%","0","",actionListener);
				} 
				else 
				{
					tabbedEurope.addItem(txt, marketBean.getLastTradedPrice(), change, changePer+"%","0","",actionListener);				
				}

			}
			for(int j=0;j<=this.americaData.size();j++)
			{
				Market marketBean = new Market();
				if(j==this.americaData.size())
				{
					CrudeOil crd = (CrudeOil)this.crudeData.elementAt(0);
					marketBean.setExchangeName("Crude");
					marketBean.setChangePts(crd.getCrudeChange());
					double val = Double.parseDouble(crd.getCrudeVal().replace('$', ' '));
					String ch = crd.getCrudeChange().replace('+', ' ');
					boolean fl = crd.getCrudeChange().indexOf("+")>-1?true:false;
					double change = Double.parseDouble(ch.replace('-', ' '));
					val = fl?(val - change):(val + change);
					double ltp = (val/100);
					System.out.println(ltp);
					double per = change/ltp;
					marketBean.setChangePtsPer(fl?"+":"-"+Utils.DecimalRoundString(per, 2)+"%");
					marketBean.setLastTradedPrice(crd.getCrudeVal().replace('$', ' '));
				}
				else
				{
					marketBean = (Market)this.americaData.elementAt(j);
				}
				String txt = marketBean.getExchangeName();
				/*if(txt.indexOf('&')>-1)
				{
					String txt2="";
					txt2 = txt.substring(0, txt.indexOf('&')+1) + txt.substring(txt.indexOf('&')+5, txt.length());
					txt=txt2;
				}*/
				String change = marketBean.getChangePts();
				String changePer = marketBean.getChangePtsPer().substring(0, marketBean.getChangePtsPer().length()-1);
				if(change.indexOf("+")>-1 )
				{
					change = marketBean.getChangePts().substring(1, marketBean.getChangePts().length());
				}
				if(changePer.indexOf("+")>-1 )
				{
					changePer = marketBean.getChangePtsPer().substring(1, marketBean.getChangePtsPer().length()-1);
				}
				if(marketBean.getChangePtsPer().indexOf("-")==-1) {
					tabbedAmerica.addItem(txt, marketBean.getLastTradedPrice(), "+"+change, "+"+changePer+"%","0", "",actionListener);
				} 
				else
				{
					tabbedAmerica.addItem(txt, marketBean.getLastTradedPrice(), change, changePer+"%","0", "",actionListener);				
				}

			}
			for(int j=0;j<this.forexData.size();j++)
			{
				ForexList forexBean = (ForexList)forexData.elementAt(j);
				String txt = forexBean.getForexName();
				/*if(txt.indexOf('&')>-1)
				{
					String txt2="";
					txt2 = txt.substring(0, txt.indexOf('&')+1) + txt.substring(txt.indexOf('&')+5, txt.length());
					txt=txt2;
				}*/
				String change = forexBean.getChange();
				String changePer = forexBean.getChangePer().substring(0, forexBean.getChangePer().length()-1);
				if(change.indexOf("+")>-1 )
				{
					change = forexBean.getChange().substring(1, forexBean.getChange().length());
				}
				if(changePer.indexOf("+")>-1 )
				{
					changePer = forexBean.getChangePer().substring(1, forexBean.getChangePer().length()-1);
				}
				if(forexBean.getChangePer().indexOf("-")==-1) 
				{
					tabbedForex.addItem(txt, forexBean.getRate(), "+"+change, "+"+changePer+"%","0","",actionListener);
				}
				else
				{
					tabbedForex.addItem(txt, forexBean.getRate(), change, changePer+"%","0","",actionListener);				
				}
			}
			add(tabbedAsia);
			add(tabbedEurope);
			add(tabbedAmerica);
			add(tabbedForex);
		}
		else
		{
			TabbedList tabbedGainersList = new TabbedList("Gainers");
			TabbedList tabbedLosersList = new TabbedList("Losers");
			int i;
			for(i=0;i<vector.size();i++) 
			{
				TopGainLoseItem topGainLooseItem = (TopGainLoseItem)vector.elementAt(i);
				if(topGainLooseItem.getChange().indexOf("-")==-1) 
				{
					tabbedGainersList.addItem(topGainLooseItem.getShortCompanyName(), topGainLooseItem.getPrice(), "+"+topGainLooseItem.getChange(), "+"+topGainLooseItem.getPercentageChange()+"%",topGainLooseItem.getCompanyCode(), topGainLooseItem.getSymbol(),actionListener);
				} 
				else
				{
					tabbedLosersList.addItem(topGainLooseItem.getShortCompanyName(), topGainLooseItem.getPrice(), topGainLooseItem.getChange(), topGainLooseItem.getPercentageChange()+"%",topGainLooseItem.getCompanyCode(), topGainLooseItem.getSymbol() , actionListener);				
				}

			}
			add(tabbedGainersList);
			add(tabbedLosersList);
		}
	}

	/*public int getPreferredHeight() 
	{
		try {
			return AppConstants.screenHeight;
		} catch(Exception ex) {
		}
		return 0;
	}*/
	protected void paintBackground(Graphics graphics) 
	{
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getWidth(), getHeight());
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
			if(AppConstants.screenHeight<480)
				lblListTitle.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			else
				lblListTitle.setFont(FontLoader.getFont(AppConstants.LABEL_FONT));
			add(lblListTitle);
		}

		private void addItem(final String companyName,final String price,final String changedPrice,final String perchantage,final String companyCode,final String symbol,final ActionListener actionListner) {
			UiApplication.getUiApplication().invokeLater(new Runnable() 
			{
				public void run() 
				{
					add(new TabbedListItem(FOCUSABLE, companyName, price, changedPrice, perchantage,companyCode,symbol,actionListner));
				}
			});
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
		private String symbol;
		private Bitmap refreshImage;
		public TabbedListItem(long style,String companyName,String value,String changedValue,String percentage,String companyCode,String symbol_,ActionListener actionListner) 
		{
			super(style);
			symbol = symbol_;
			if(companyName.length()>12)
			{
				companyName = companyName.substring(0, 12) + "...";
			}
			isGainer = (changedValue.indexOf("-")==-1);
			if(AppConstants.screenHeight<480)
				font = FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT);
			else
				font = FontLoader.getFont(AppConstants.LABEL_FONT);
			setFont(font);
			if(fieldWidth==-1)
				fieldWidth = (short)(font.getAdvance("##########")+padding);
			if(fieldHeight == -1)
				fieldHeight = (short)(getPreferredHeight()-padding);
			this.actionListner = actionListner;
			this.companyCode = companyCode;
			final int companyFieldWidth = getPreferredWidth()-fieldWidth+10-padding*2-getFont().getAdvance(value);
			lblCompanyName = new LabelField(/*AppConstants.screenWidth==480?companyName.length()<13?companyName:companyName.substring(0, 12):*/companyName, 0) 
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

				protected void paint(Graphics graphics)
				{
					graphics.setColor(0xeeeeee);
					super.paint(graphics);
				}

				protected void drawFocus(Graphics graphics, boolean on) 
				{
				}

				public int getPreferredWidth()
				{
					return getFont().getAdvance(getText())+5;
				}

				protected void layout(int width, int height) 
				{
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

				protected void paint(Graphics graphics) 
				{
					graphics.setColor(0xeeeeee);
					super.paint(graphics);
				}

				protected void drawFocus(Graphics graphics, boolean on) 
				{
				}

				public int getPreferredWidth() 
				{
					return getFont().getAdvance(getText())+5;
				}

				protected void layout(int width, int height) 
				{
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
			};

			lblPercentage = new LabelField(percentage, 0) 
			{
				public int getPreferredHeight() {
					return getFont().getHeight();
				}

				protected void paint(Graphics graphics) 
				{
					graphics.setColor(0xeeeeee);
					super.paint(graphics);
				}

				protected void drawFocus(Graphics graphics, boolean on) 
				{
				}

				public int getPreferredWidth() 
				{
					return getFont().getAdvance(getText())+5;
				}

				protected void layout(int width, int height) 
				{
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
			};

			lblCompanyName.setFont(getFont());
			lblValue.setFont(getFont());
			lblChangedValue.setFont(getFont());
			lblPercentage.setFont(getFont());
			/*add(lblCompanyName);
			add(lblValue);
			add(lblChangedValue);
			add(lblPercentage);*/
			
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
			startUpdate();
		}

		public int getPreferredHeight() {
			return getFont().getHeight()*2+padding*2;
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
			if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
			if(!AppConstants.source.equalsIgnoreCase("Global"))
			{	Utils.NSE_SYMBOL = symbol;
				Vector vectorCommandData = new Vector();
				vectorCommandData.addElement(lblCompanyName.getText());
				vectorCommandData.addElement(companyCode);
				vectorCommandData.addElement(source);
				actionListner.actionPerfomed(ActionCommand.CMD_COMPANY_DETAILS_SCREEN, vectorCommandData);
			}
			}
			return super.navigationClick(status, time);
		}

		protected boolean touchEvent(TouchEvent message) 
		{
			if(message.getEvent() == TouchEvent.CLICK) {
				if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
				if(!AppConstants.source.equalsIgnoreCase("Global"))
			{	Utils.NSE_SYMBOL = symbol;
				Vector vectorCommandData = new Vector();
				vectorCommandData.addElement(lblCompanyName.getText());
				vectorCommandData.addElement(companyCode);
				vectorCommandData.addElement(source);
				actionListner.actionPerfomed(ActionCommand.CMD_COMPANY_DETAILS_SCREEN, vectorCommandData);
			}
		}
			}
			return super.touchEvent(message);
		}

		protected void paint(Graphics graphics) 
		{
			if(startUpdate)
			{
				//Draw White Border Background
				//graphics.setColor(isFocused == true && dataFeed ? Color.ORANGE : 0xeeeeee);
				//graphics.fillRoundRect(0, padding/2, getPreferredWidth(), getPreferredHeight(), 10, 10);
				if(h>getPreferredHeight()-borderWidth*2)
				{
					hLess = true;
					h=getPreferredHeight()-borderWidth*2;
				}
				if(h<3)
				{
					hLess = false;
				}
				if(hLess)
					h=h-2;
				else
					h=h+2;
				if(refreshImage!=null)
				{
					Bitmap image = ImageManager.resizeBitmap(refreshImage,refreshImage.getWidth(),h);
					graphics.drawBitmap(0, (getPreferredHeight()/2)-((h+(borderWidth*2))/2), image.getWidth(), image.getHeight(), image, 0, 0);
				}
				else
				{
					graphics.setColor(isFocus() == true ? Color.ORANGE : 0xeeeeee);
					graphics.fillRoundRect(0, (getPreferredHeight()/2)-((h+(borderWidth*2))/2), getPreferredWidth(), h+(borderWidth*2), 10, 10);
					//graphics.setColor((isGainer == true ? Color.GREEN : Color.RED));				
					//graphics.fillRoundRect(borderWidth, (((getPreferredHeight())/2)-(h/2)), (getPreferredWidth()-borderWidth*2), h, 10, 10);
				}
				
			}
		else
		{graphics.setColor(isFocus() == true ? Color.ORANGE : Color.BLACK);
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
			graphics.setColor(0xeeeeee);
			graphics.setFont(font);
			graphics.drawText(lblCompanyName.getText(), padding, getPreferredHeight()/2 - graphics.getFont().getHeight()/2);
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			graphics.drawText(lblValue.getText(), padding + getPreferredWidth() - fieldWidth - graphics.getFont().getAdvance(lblValue.getText()), getPreferredHeight()/2 - graphics.getFont().getHeight()/2);
			graphics.drawText(lblChangedValue.getText(), getPreferredWidth()-padding/2-fieldWidth+10+((fieldWidth-10)/2-graphics.getFont().getAdvance(lblChangedValue.getText())/2), padding);
			graphics.drawText(lblPercentage.getText(), getPreferredWidth()-padding/2-fieldWidth+10+((fieldWidth-10)/2-graphics.getFont().getAdvance(lblPercentage.getText())/2), getFont().getHeight()+padding);
			}
		}

		protected void sublayout(int width, int height) 
		{
			/*layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
			layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
			layoutChild(getField(2), getField(2).getPreferredWidth(), getField(2).getPreferredHeight());
			layoutChild(getField(3), getField(3).getPreferredWidth(), getField(3).getPreferredHeight());
			setPositionChild(getField(0), padding, getPreferredHeight()/2 - getField(0).getPreferredHeight()/2);
			setPositionChild(getField(1), padding+getField(0).getPreferredWidth(), getPreferredHeight()/2 - getField(1).getPreferredHeight()/2);
			setPositionChild(getField(2), getPreferredWidth()-padding/2-fieldWidth+10+((fieldWidth-10)/2-getField(2).getPreferredWidth()/2), padding);
			setPositionChild(getField(3), getPreferredWidth()-padding/2-fieldWidth+10+((fieldWidth-10)/2-getField(3).getPreferredWidth()/2), getFont().getHeight()+padding);
			*/if(requireNullField)
			{
				layoutChild(this.getField(this.getFieldCount()-1), 1 , 1);
				setPositionChild(this.getField(this.getFieldCount()-1), AppConstants.screenWidth/3, getPreferredHeight()/2);
			}
			setExtent(getPreferredWidth(), getPreferredHeight());
		}
	
/*public boolean getRefreshFlag()
{
	return refreshFlag;
	}
	public void refreshFields() {
		LOG.print("Refreshing Top GL Tab");
		if(refreshFlag){
		TopGainLoseItemParser topGainerLoserItemParser = new TopGainLoseItemParser(AppConstants.nseTopGainersLoosersUrl,this);
		topGainerLoserItemParser.getScreenData();
		}
	}
	
	public void setData(Vector vector) {
		TabbedList tabbedGainersList = new TabbedList("Gainers");
		TabbedList tabbedLosersList = new TabbedList("Losers");
		int i;
		for(i=0;i<vector.size();i++) 
		{
			TopGainLoseItem topGainLooseItem = (TopGainLoseItem)vector.elementAt(i);
			if(topGainLooseItem.getChange().indexOf("-")==-1) 
			{
				tabbedGainersList.addItem(topGainLooseItem.getShortCompanyName(), topGainLooseItem.getPrice(), "+"+topGainLooseItem.getChange(), "+"+topGainLooseItem.getPercentageChange()+"%",topGainLooseItem.getCompanyCode(), topGainLooseItem.getSymbol(),actionListenRefresh);
			} 
			else
			{
				tabbedLosersList.addItem(topGainLooseItem.getShortCompanyName(), topGainLooseItem.getPrice(), topGainLooseItem.getChange(), topGainLooseItem.getPercentageChange()+"%",topGainLooseItem.getCompanyCode(), topGainLooseItem.getSymbol() , actionListenRefresh);				
			}

		}
		//deleteAll();
		add(tabbedGainersList);
		add(tabbedLosersList);
		refreshFlag = false;
	}*/
	private void startUpdate()
	{
		/*//int height = getPreferredHeight() + borderWidth + padding/2;
		refreshImage = new Bitmap( getPreferredWidth(), getPreferredHeight());
		Graphics graphics = new Graphics(refreshImage);
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
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
		graphics.setColor(0xeeeeee);
		graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
		graphics.drawText(lblCompanyName.getText(), padding, getPreferredHeight()/2 - graphics.getFont().getHeight()/2);
		graphics.drawText(lblValue.getText(), padding + getPreferredWidth() - fieldWidth - graphics.getFont().getAdvance(lblValue.getText()), getPreferredHeight()/2 - graphics.getFont().getHeight()/2);
		graphics.drawText(lblChangedValue.getText(), getPreferredWidth()-padding/2-fieldWidth+10+((fieldWidth-10)/2-graphics.getFont().getAdvance(lblChangedValue.getText())/2), padding);
		graphics.drawText(lblPercentage.getText(), getPreferredWidth()-padding/2-fieldWidth+10+((fieldWidth-10)/2-graphics.getFont().getAdvance(lblPercentage.getText())/2), getFont().getHeight()+padding);
		
		startUpdate = true;
		animationThread = new Thread(new Runnable() {
			public void run() {
				invalidate();
				try {
					for(int i=1;i<31;i++){
						Thread.sleep(10);
					if(i==30)
					startUpdate = false;
					invalidate();
					}
				} catch (InterruptedException e) {
					LOG.print("Exception in banner screen");
					startUpdate = false;
					invalidate();
				}
			}});
		animationThread.start();*/
	}
}
}
