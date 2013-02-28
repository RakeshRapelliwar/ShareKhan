package com.snapwork.components;

import java.util.Vector;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionListener;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.URLEncode;

/**
 * 
 * <p>This class is creates WatchList fields and added to WatchList Screen.
 * <p>This class create custom field which shows details of Company like Name, Change in percent, etc.
 *
 */

public class WatchListFieldRemove extends Manager
{
	private LabelField lblCompanyName = null;
	private final static int itemBackColor = 4343106;
	private boolean requireNullField = true;
	private ActionListener actionListner;
	private static byte padding = 4;
	private static short fieldWidth = -1,fieldHeight = -1;
	 private String ccode;
	 private String wl_name;
	 private String exch;
	 private Font font;
	public WatchListFieldRemove(long style,String companyName,String ccode,String wl_name,String exch,ActionListener actionListner)
	{
		super(style);
		/*if(exch.equalsIgnoreCase("NSE"))
			setCcode(companyName);
		else*/
			setCcode(ccode);
		//	if(companyName.length()>26)
			//	companyName = companyName.substring(0, 22)+"...";
		LOG.print("Company code watchlist companyName "+companyName);
		LOG.print("Company code watchlist ccode "+ccode);
		setWl_name(wl_name);
		setExch(exch);
		if(AppConstants.screenHeight<480)
			font = FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT);
		else
			font = FontLoader.getFont(AppConstants.LABEL_FONT);
		setFont(font);
		if(fieldWidth==-1)
			fieldWidth = (short)(font.getAdvance("##########")+padding);
		if(fieldHeight==-1)
			fieldHeight = (short) (getPreferredHeight()-padding);
		this.actionListner = actionListner;
		//final int companyFieldWidth = getPreferredWidth()-fieldWidth+10-padding*2-getFont().getAdvance("########");
		final int companyFieldWidth = getPreferredWidth() - (ImageManager.getRemove().getWidth()+5);
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


		lblCompanyName.setFont(getFont());
		add(lblCompanyName);
		//add(lblValue);
		//add(lblChangedValue);
		//add(lblPercentage);
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

	public String getCcode() {
		return ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public String getWl_name() {
		return wl_name;
	}

	public void setWl_name(String wl_name) {
		this.wl_name = wl_name;
	}

	public String getExch() {
		return exch;
	}

	public void setExch(String exch) {
		this.exch = exch;
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

	/*protected boolean navigationClick(int status, int time)
	{
		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
            public void run() {
             customPopUpScreen = new CustomPopUpScreen("1");
             UiApplication.getUiApplication().pushScreen(customPopUpScreen);
            }
        });
		return super.navigationClick(status, time);
	}
	
	protected boolean keyDown( int keyCode, int time ) {
		int key = Keypad.key( keyCode );
		if(key== Keypad.KEY_ENTER)
        {
        	UiApplication.getUiApplication().invokeLater(new Runnable() {
                public void run() {
                 customPopUpScreen = new CustomPopUpScreen("1");
                 UiApplication.getUiApplication().pushScreen(customPopUpScreen);
                }
            });
        }
        else if(key==Keypad.KEY_ESCAPE)
        {
        	UiApplication.getUiApplication().invokeLater(new Runnable() {
                public void run() {
                	ScreenInvoker.removeRemovableScreen();
                }
            });
        }
		return super.keyDown(keyCode, time);
	}
	*/

	protected void paintBackground(Graphics graphics)
	{
		graphics.setColor(isFocus() == true ? Color.ORANGE : Color.BLACK);
		graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
		graphics.setColor(itemBackColor);
		graphics.fillRoundRect(padding/2, padding/2, getPreferredWidth()-padding, getPreferredHeight()-padding, 10, 10);
		/*graphics.setColor(isFocus() == true ? Color.ORANGE : Color.BLACK);
		graphics.fillRect(getPreferredWidth()-padding/2-fieldWidth, padding/2, fieldWidth, fieldHeight);
		graphics.setColor(isGainer == true ? Color.GREEN : Color.RED);
		graphics.fillRoundRect(getPreferredWidth()-padding/2-fieldWidth, padding/2, fieldWidth, fieldHeight,10,10);
		graphics.setColor(0xeeeeee);
		graphics.drawLine(getPreferredWidth()-padding/2-fieldWidth+10, padding/2, getPreferredWidth()-padding/2-fieldWidth+10, fieldHeight);
		graphics.drawLine(getPreferredWidth()-padding/2-fieldWidth, padding/2+fieldHeight/2, getPreferredWidth()-padding/2-1, padding/2+fieldHeight/2);
		graphics.setColor(itemBackColor);
		graphics.fillRect(getPreferredWidth()-padding/2-fieldWidth, padding/2, 10, fieldHeight);
		*/graphics.drawBitmap(getPreferredWidth() - (ImageManager.getRemove().getWidth()+5), (getPreferredHeight()/2)-(ImageManager.getRemove().getHeight()/2), ImageManager.getRemove().getWidth(), ImageManager.getRemove().getHeight(), ImageManager.getRemove(), 0, 0);
	}

	protected void sublayout(int width, int height)
	{
		layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
	//	layoutChild(getField(1), getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
	//	layoutChild(getField(2), getField(2).getPreferredWidth(), getField(2).getPreferredHeight());
	//	layoutChild(getField(3), getField(3).getPreferredWidth(), getField(3).getPreferredHeight());
		setPositionChild(getField(0), padding, getPreferredHeight()/2 - getField(0).getPreferredHeight()/2);
	//	setPositionChild(getField(1), padding+getField(0).getPreferredWidth(), getPreferredHeight()/2 - getField(1).getPreferredHeight()/2);
	//	setPositionChild(getField(2), getPreferredWidth()-padding/2-fieldWidth+10+((fieldWidth-10)/2-getField(2).getPreferredWidth()/2), padding);
	//	setPositionChild(getField(3), getPreferredWidth()-padding/2-fieldWidth+10+((fieldWidth-10)/2-getField(3).getPreferredWidth()/2), getFont().getHeight()+padding+padding/2);
		if(requireNullField)
		{
			layoutChild(this.getField(this.getFieldCount()-1), 1 , 1);
			setPositionChild(this.getField(this.getFieldCount()-1), AppConstants.screenWidth/2, getPreferredHeight()/2);
		}
		setExtent(getPreferredWidth(), getPreferredHeight());
	}
	/*public class CustomPopUpScreen extends PopupScreen {
	    public CustomPopUpScreen(String text) {
	        super(new VerticalFieldManager(CustomPopUpScreen.NO_HORIZONTAL_SCROLL));
	        add(new ButtonField(text+"CBA"));
	        add(new ButtonField(text+"ZYX"));
	    }
	    
	    protected boolean keyDown( int keyCode, int time ) {
			int key = Keypad.key( keyCode );
			if(key== Keypad.KEY_ENTER)
	        {
	        	/*UiApplication.getUiApplication().invokeLater(new Runnable() {
	                public void run() {
	                 customPopUpScreen = new CustomPopUpScreen("1");
	                 UiApplication.getUiApplication().pushScreen(customPopUpScreen);
	                }
	            });*
	        }
	        else if(key==Keypad.KEY_ESCAPE)
	        {
	        	 synchronized( UiApplication.getEventLock() ){
                     
                     if(isDisplayed()) 
                  	   {
                  	   	close();

                  	   }
	   }
	        }
			return super.keyDown(keyCode, time);
		}
	}*/
}