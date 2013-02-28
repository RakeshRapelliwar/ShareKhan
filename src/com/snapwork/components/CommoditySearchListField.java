package com.snapwork.components;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.Utils;

public class CommoditySearchListField extends VerticalFieldManager{
	
	
	private LabelField lblCompanyName = null;
	private LabelField lblValue = null;
	private final static int itemBackColor = 4343106;
	private boolean requireNullField = true;
	private ActionListener actionListner;
	private static byte padding = 4;
	
	private long timer;
	private Font font;
	LabelField lblExchange;
	private String companyName,value,exchange,symbol,token;
	
//	private String dateTextType;
//	private String dateText;
//	private String dateTextID;
//	private String companyCode;
	public CommoditySearchListField(long style,String companyName,String companyCode, String exchange,String symbol,String token, ActionListener actionListner)
	{
		super(style);
		
		this.companyName=companyName;
		this.value=companyCode;
		
		
		timer = System.currentTimeMillis();
		if(AppConstants.screenHeight<480)
			font = FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT);
		else
			font = FontLoader.getFont(AppConstants.LABEL_FONT);
		setFont(font);
//		setCompanyCode(companyCode);
		this.actionListner = actionListner;
//		this.dateTextType = dateTextType;
//		this.dateText = dateText;
//		this.dateTextID = dateTextID;
		this.exchange = exchange;
		this.symbol=symbol;
		this.token=token;
		Utils.NSE_SYMBOL = symbol;
		
		
		
		
		
		lblCompanyName = new LabelField(companyName/*.length()<17?companyName:companyName.substring(0, 16)+"..."*/, 0)
		{
//			public int getPreferredHeight()
//			{
//				return getFont().getHeight();
//			}
//
//			public int getPreferredWidth()
//			{
//				return AppConstants.screenWidth;
//			}

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

		lblValue  = new LabelField(companyCode, 0)
		{
//			public int getPreferredHeight()
//			{
//				return getFont().getHeight();
//			}

			protected void paint(Graphics graphics)
			{
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}

			protected void drawFocus(Graphics graphics, boolean on)
			{
			}

//			public int getPreferredWidth()
//			{
//				return getFont().getAdvance(getText());
//			}

			protected void layout(int width, int height) {
				super.layout(width, height);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
		};
		
		
		
		lblExchange  = new LabelField(exchange, 0)
		{
//			public int getPreferredHeight()
//			{
//				return getFont().getHeight();
//			}

			protected void paint(Graphics graphics)
			{
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}

			protected void drawFocus(Graphics graphics, boolean on)
			{
			}

//			public int getPreferredWidth()
//			{
//				return getFont().getAdvance(getText());
//			}

			protected void layout(int width, int height) {
				super.layout(width, height);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
		};
		
		
		
		

		lblCompanyName.setFont(FontLoader.getFont(AppConstants.LABEL_FONT));
		lblValue.setFont(FontLoader.getFont(AppConstants.LABEL_FONT));
		lblExchange.setFont(getFont());
		
		HorizontalFieldManager hfm=new HorizontalFieldManager();
		
		hfm.add(lblCompanyName);
		hfm.add(lblValue);
		hfm.setMargin(5, 0, 5, 0);
		lblExchange.setMargin(5, 5, 5, 5);
		add(hfm);
		add(lblExchange);
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

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		Utils.NSE_SYMBOL = symbol;
		this.symbol = symbol;
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
		return getFont().getHeight()*2+padding*2+(FontLoader.getFont(AppConstants.LABEL_FONT)).getHeight();
//		return 70;
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
		
		
		
		handleEvent();
		
		return super.navigationClick(status, time);
	}

	private void handleEvent() {
		if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
		
		   Vector data=new Vector();
		   
		     data.addElement(this);
		     data.addElement(Utils.replaceString(AppConstants.commoditySpecificSearchURL, "##KEYWORD##", symbol));
		    
		     String chartURL=Utils.getCommodityChartData(token, symbol);
		     
		     data.addElement(chartURL);
		     data.addElement(exchange);
		     data.addElement(symbol);
		     
//		     Dialog.alert(Utils.replaceString(AppConstants.commoditySpecificSearchURL, "##KEYWORD##", "MX_GOLDPTLDEL_28-02-2013"));
		     
             Action act=new Action(ActionCommand.CMD_COMMODITY_DATA, data);
             ActionInvoker.processCommand(act);
		
		      
		
//		if(dateTextType.equalsIgnoreCase("Equity"))
//			{
//				Vector vectorCommandData = new Vector();
//				vectorCommandData.addElement(lblCompanyName.getText());
//				vectorCommandData.addElement(lblValue.getText());
//				vectorCommandData.addElement("BSE");
//				//CompanyDetailsSnippetsScreen.addDirect = flg;
//				actionListner.actionPerfomed(ActionCommand.CMD_COMPANY_DETAILS_SCREEN, vectorCommandData);
//			}
//			else
//			{
//				Vector vectorCommandData = new Vector();
//				vectorCommandData.addElement(getCompanyCode());
//				vectorCommandData.addElement(getCompanyCode());
//				vectorCommandData.addElement("NSEFO");
//				actionListner.actionPerfomed(ActionCommand.CMD_NIFTY_COMPANY_DETAILS_SCREEN, vectorCommandData);
//			}
		}
		
	}

	protected boolean touchEvent(TouchEvent message)
	{setFocus();
		if(message.getEvent() == TouchEvent.CLICK) {
//			handleEvent();
	}
		return super.touchEvent(message);
	}
	protected void paintBackground(Graphics graphics)
	{
		graphics.setColor(isFocus() == true ? Color.ORANGE : Color.BLACK);
		graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
		graphics.setColor(itemBackColor);
		graphics.fillRoundRect(padding/2, padding/2, getPreferredWidth()-padding, getPreferredHeight()-padding, 10, 10);
	}

	protected void sublayout(int width, int height)
	{
//		layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
//		layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
//		layoutChild(getField(2), getField(2).getPreferredWidth(), getField(2).getPreferredHeight());
		
		
//		setPositionChild(getField(0), padding, padding+getField(0).getPreferredHeight()/2);
//		setPositionChild(getField(1), getField(0).getPreferredWidth()+padding, padding+getField(0).getPreferredHeight()/2);
//		setPositionChild(getField(2), padding, padding+getField(2).getPreferredHeight()/2+getField(0).getPreferredHeight());
		
		
//		setPositionChild(getField(0), padding, padding+getField(0).getPreferredHeight()/2);
//		setPositionChild(getField(1), 100,20);
//		setPositionChild(getField(2), 200, 20);
		
//		if(requireNullField)
//		{
//			layoutChild(this.getField(this.getFieldCount()-1), 1 , 1);
//			setPositionChild(this.getField(this.getFieldCount()-1), AppConstants.screenWidth/2, getPreferredHeight()/2);
//		}
		
		super.sublayout(getPreferredWidth(), getPreferredHeight());
		setExtent(getPreferredWidth(), getPreferredHeight());
	}

//	public String getCompanyCode() {
//		return companyCode;
//	}
//
//	public void setCompanyCode(String companyCode) {
//		this.companyCode = companyCode;
//	}
	
	
	

}
