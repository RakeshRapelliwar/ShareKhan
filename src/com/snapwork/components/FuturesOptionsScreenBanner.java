package com.snapwork.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;

public class FuturesOptionsScreenBanner extends Field
{
	private boolean isFocused = false;
	private String companyID = null;
	private String value = null;
	private String changedValue = null;
	private String percentage = null;
	private String source = null;
	private byte padding = 6;
	private byte borderWidth = 3;
	private Bitmap sourceBitmap = null;
	private boolean dataFeed = false;
	private boolean isMarketUp = false;
	private int ht;
	public FuturesOptionsScreenBanner(long style,String source)
	{
		super(style);
		setFocused(false);
		setSource(source);
		ht = getFont().getHeight()+6;
		createSourceBitmap(Color.GRAY);
		padding = (byte)ImageManager.CalculateRelativeDimensions(padding, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
		borderWidth = (byte)ImageManager.CalculateRelativeDimensions(borderWidth, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
	}

	public FuturesOptionsScreenBanner(long style,String source,String companyID) 
	{
		this(style,source);
		ht = getFont().getHeight()+6;
		setCompanyID(companyID);
	}

	public void reset() 
	{
		createSourceBitmap(Color.GRAY);
		dataFeed = false;
	}

	public boolean isBlockLoaded() 
	{
		return dataFeed;
	}

	public void setData(String value,String changedValue,String percentage) 
	{
		setValue(value);
		setPercentage(percentage+" %");
		dataFeed = true;
		try 
		{
			isMarketUp = (changedValue.indexOf("-")==-1);
			setChangedValue((isMarketUp == true ? "+" + changedValue : changedValue ));			
			if(getPercentage().indexOf("+")==-1)
				setPercentage((isMarketUp == true ? "+" + getPercentage() : getPercentage() ));
		} 
		catch(Exception ex) 
		{
			Debug.debug("Value : "+value);
			Debug.debug("changedValue : "+changedValue);
			Debug.debug("percentage : "+getPercentage());
		}
		createSourceBitmap((isMarketUp == true ? Color.GREEN : Color.RED));
		invalidate();
	}

	public int getPreferredHeight() 
	{
		return (sourceBitmap.getHeight() + sourceBitmap.getHeight()/2 + borderWidth*2)+ht;
	}

	public int getPreferredWidth() 
	{
		return (AppConstants.screenWidth - (AppConstants.screenWidth*10)/100);
	}

	protected void onFocus(int direction) 
	{
		isFocused = true;
		super.onFocus(direction);
		invalidate();
	}

	protected void onUnfocus() 
	{
		isFocused = false;
		super.onUnfocus();
		invalidate();
	}

	protected void paint(Graphics graphics) 
	{
		//Draw White Border Background
		graphics.setColor(isFocused == true && dataFeed ? Color.ORANGE : 0xeeeeee);
		graphics.fillRoundRect(0, padding/2, getPreferredWidth(), getPreferredHeight(), 10, 10);

		if(dataFeed) 
		{
			graphics.setColor((isMarketUp == true ? Color.GREEN : Color.RED));				
			graphics.fillRoundRect(borderWidth, borderWidth + padding/2, getPreferredWidth()-borderWidth*2, getPreferredHeight()-borderWidth*2, 10, 10);
			int length = 0 ;
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			length = graphics.getFont().getAdvance("########");
			//Set Colors and font
			graphics.setColor(0xeeeeee);
			graphics.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			//Draw Value
			graphics.drawText(getValue(), (borderWidth + padding + sourceBitmap.getWidth()) + ((getPreferredWidth()-length)-(borderWidth + padding + sourceBitmap.getWidth()))/2 - graphics.getFont().getAdvance(getValue())/2, (getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2)-ht);
			//Draw Changed Value
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT));
			//Draw Changed Value
			graphics.drawText(getChangedValue(), (getPreferredWidth() - length + 1)+(length + 1 - graphics.getFont().getAdvance(getChangedValue()))/2, (getPreferredHeight()/2 - graphics.getFont().getHeight()-1+ padding/2)-ht);
			//Draw Perchantage
			graphics.drawText(getPercentage(), (getPreferredWidth() - length + 1)+(length + 1 - graphics.getFont().getAdvance(getPercentage()))/2, (getPreferredHeight()/2 + graphics.getFont().getHeight()+1-padding-padding/2)-ht);
			//BSE NSE Image
			graphics.drawBitmap(borderWidth + padding/2, (padding/2 + (getPreferredHeight()/2 - sourceBitmap.getHeight()/2))-ht, sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap, 0, 0);
			//Draw Lines
			graphics.setColor(0xeeeeee);
			graphics.drawLine(borderWidth + padding + sourceBitmap.getWidth(),(borderWidth + padding/2)-ht, borderWidth + padding + sourceBitmap.getWidth(),( getPreferredHeight()-borderWidth+padding/2)-ht);
			graphics.drawLine( getPreferredWidth()-length,(borderWidth + padding/2)-ht ,getPreferredWidth()-length ,(getPreferredHeight()-borderWidth+padding/2)-ht );
			graphics.drawLine( getPreferredWidth()-length,(padding/2 +getPreferredHeight()/2)-ht,getPreferredWidth()-borderWidth,(padding/2 + getPreferredHeight()/2)-ht);
			graphics.drawLine(borderWidth + padding,getPreferredHeight()-ht+3,getPreferredWidth()-length,getPreferredHeight()-ht+3);
		} 
		else 
		{
			graphics.setColor(Color.GRAY);
			graphics.fillRoundRect(borderWidth, borderWidth + padding/2, getPreferredWidth()-borderWidth*2, getPreferredHeight()-borderWidth*2, 10, 10);
			graphics.setColor(0xeeeeee);
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			graphics.drawText("Fetching Data", getPreferredWidth()/2 - graphics.getFont().getAdvance("Fetching Data")/2, getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2);
			graphics.drawBitmap(borderWidth + padding/2, (padding/2 + (getPreferredHeight()/2 - sourceBitmap.getHeight()/2))-ht, sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap, 0, 0);
			//Draw Lines
			graphics.setColor(0xeeeeee);
			graphics.drawLine(borderWidth + padding + sourceBitmap.getWidth(),(borderWidth + padding/2)-ht, borderWidth + padding + sourceBitmap.getWidth(), (getPreferredHeight()-borderWidth+padding/2)-ht);
		}
	}

	protected void drawFocus(Graphics graphics, boolean on) {

	}

	protected void paintBackground(Graphics graphics) 
	{
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getWidth(), getHeight());
	}

	protected void layout(int width, int height) 
	{
		setExtent(getPreferredWidth(), getPreferredHeight() + padding);
	}

	private void createSourceBitmap(int backColor) 
	{
		sourceBitmap = new Bitmap(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT).getAdvance(getSource()),FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getHeight());
		Graphics graphics = new Graphics(sourceBitmap);
		graphics.setColor(backColor);
		graphics.fillRect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
		graphics.setColor(0xeeeeee);
		graphics.setFont(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT));
		graphics.drawText(getSource(), 0, 0);
		sourceBitmap = ImageManager.rotateBitmap(sourceBitmap, 270);
	}

	protected void changeValue() 
	{

	}

	public boolean isFocused() 
	{
		return isFocused;
	}

	public void setFocused(boolean isFocused) 
	{
		this.isFocused = isFocused;
	}

	public String getCompanyID() 
	{
		return companyID;
	}

	public void setCompanyID(String companyID) 
	{
		this.companyID = companyID;
	}

	public String getSource() 
	{
		return source;
	}

	public void setSource(String value) 
	{
		this.source = value;
	}

	public String getValue() 
	{
		return value;
	}

	public void setValue(String value) 
	{
		this.value = value;
	}

	public String getChangedValue() 
	{
		return changedValue;
	}

	public void setChangedValue(String changedValue) 
	{
		this.changedValue = changedValue;
	}

	public String getPercentage() 
	{
		return percentage;
	}

	public void setPercentage(String percentage) 
	{
		this.percentage = percentage;
	}
}
