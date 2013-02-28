package com.snapwork.components;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;

import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionListener;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.LOG;
import com.snapwork.util.Utils;
import com.snapwork.view.CompanyDetailsSnippetsScreen;
import com.snapwork.view.SearchStocksFNOFields;

/**
 * 
 * <p>This class is a creates components for Search Screen.
 * <p>This class Shows the company Name and Company code as as a label field, its clickable.
 *
 */

public class SearchListField extends Manager
{
	private LabelField lblCompanyName = null;
	private LabelField lblValue = null;
	private final static int itemBackColor = 4343106;
	private boolean requireNullField = true;
	private ActionListener actionListner;
	private static byte padding = 4;
	private String symbol;
	private long timer;
	private Font font;
	private String dateTextType;
	private String dateText;
	private String dateTextID;
	private String companyCode;
	public SearchListField(long style,String companyName,String companyCode, String symbol, String dateTextType, String dateText, String dateTextID, ActionListener actionListner)
	{
		super(style);
		timer = System.currentTimeMillis();
		if(AppConstants.screenHeight<480)
			font = FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT);
		else
			font = FontLoader.getFont(AppConstants.LABEL_FONT);
		setFont(font);
		setCompanyCode(companyCode);
		this.actionListner = actionListner;
		this.dateTextType = dateTextType;
		this.dateText = dateText;
		this.dateTextID = dateTextID;
		this.symbol = symbol;
		Utils.NSE_SYMBOL = symbol;
		lblCompanyName = new LabelField(companyName/*.length()<17?companyName:companyName.substring(0, 16)+"..."*/, 0)
		{
			public int getPreferredHeight()
			{
				return getFont().getHeight();
			}

			public int getPreferredWidth()
			{
				return AppConstants.screenWidth;
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

		lblValue  = new LabelField(companyCode, 0)
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
				return getFont().getAdvance(getText());
			}

			protected void layout(int width, int height) {
				super.layout(width, height);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
		};

		lblCompanyName.setFont(getFont());
		lblValue.setFont(getFont());
		add(lblCompanyName);
		//add(lblValue);
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
		handleEvent();
		
		return super.navigationClick(status, time);
	}

	private void handleEvent() {
		if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
		
		if(dateTextType.equalsIgnoreCase("Equity"))
			{
				Vector vectorCommandData = new Vector();
				vectorCommandData.addElement(lblCompanyName.getText());
				vectorCommandData.addElement(lblValue.getText());
				vectorCommandData.addElement("BSE");
				//CompanyDetailsSnippetsScreen.addDirect = flg;
				actionListner.actionPerfomed(ActionCommand.CMD_COMPANY_DETAILS_SCREEN, vectorCommandData);
			}
			else
			{
				Vector vectorCommandData = new Vector();
				vectorCommandData.addElement(getCompanyCode());
				vectorCommandData.addElement(getCompanyCode());
				vectorCommandData.addElement("NSEFO");
				actionListner.actionPerfomed(ActionCommand.CMD_NIFTY_COMPANY_DETAILS_SCREEN, vectorCommandData);
			}
		}
		
	}

	protected boolean touchEvent(TouchEvent message)
	{setFocus();
		if(message.getEvent() == TouchEvent.CLICK) {
			handleEvent();
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
		layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
		//layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
		setPositionChild(getField(0), padding, padding+getField(0).getPreferredHeight()/2);
		//setPositionChild(getField(1), padding, padding+getField(0).getPreferredHeight());
		if(requireNullField)
		{
			layoutChild(this.getField(this.getFieldCount()-1), 1 , 1);
			setPositionChild(this.getField(this.getFieldCount()-1), AppConstants.screenWidth/2, getPreferredHeight()/2);
		}
		setExtent(getPreferredWidth(), getPreferredHeight());
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
}