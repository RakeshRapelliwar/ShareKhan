package com.snapwork.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;

public class TradeScreenBanner extends Field
{
	private boolean isFocused = false;
	private String companyID = null;
	private String value = null;
	private String companyName = null;
	private String changedValue = null;
	private String percentage = null;
	private String source = null;
	private byte padding = 6;
	private byte borderWidth = 3;
	private Bitmap sourceBitmap = null;
	private boolean dataFeed = false;
	private boolean isMarketUp = false;
	private boolean firstTime = true;
	private boolean startUpdate;
	private int h = 0 ;
	private boolean hLess = false;
	private Bitmap refreshImage;
	private Thread animationThread;
	
	public TradeScreenBanner(long style,String source, boolean exch) 
	{
		super(style);
		firstTime = true;
		setFocused(false);
		if(source.equalsIgnoreCase("null"))
			source = "No Data";
		setCompanyName(source);
		if(exch)
			setSource("BSE");
		else
			setSource("NSE");
		//Create Source Bitmap
		createSourceBitmap(Color.GRAY);
		padding = (byte)ImageManager.CalculateRelativeDimensions(padding, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
		borderWidth = (byte)ImageManager.CalculateRelativeDimensions(borderWidth, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
	}
	
	/*public TradeScreenBanner(long style,String source,boolean flag) 
	{
		super(style);
		setFocused(false);
		if(source.equalsIgnoreCase("null"))
			source = "No Data";
		setSource(source);
		padding = (byte)ImageManager.CalculateRelativeDimensions(padding, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
		borderWidth = (byte)ImageManager.CalculateRelativeDimensions(borderWidth, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
		if(flag)
		{
			firstTime = true;
			//Create Source Bitmap
			createSourceBitmap(Color.GRAY);
		}
		else
		{
			dataFeed = true;
			setValue("No Data");
			setChangedValue(" ");
			setPercentage(" ");
			setNullData();
		}
	}*/

	/*public TradeScreenBanner(long style,String source,String companyID) 
	{
		this(style,source);
		setCompanyID(companyID);
	}*/

	public void reset() 
	{
		createSourceBitmap(Color.GRAY);
		dataFeed = false;
		firstTime = true;
		startUpdate = false;
		invalidate();
	}

	public boolean isBlockLoaded() 
	{
		return dataFeed;
	}

	public void setData(String value,String changedValue,String percentage)
	{
		if(value.equalsIgnoreCase("null"))
		{
			setNullData();
			return;
		}
		else
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
		/*Utils.BSEBANNER = this;
		Utils.BSEBANNER_TIME = System.currentTimeMillis();*/
		/*Utils.LASTUPDATETIME = "";*/
	}
	
	public void setNullData()
	{
		setValue("No Data");
		setChangedValue(" ");
		setPercentage(" ");
		dataFeed = true;
		createSourceBitmap(Color.GREEN);
		isMarketUp = true;
		invalidate();
		/*Utils.BSEBANNER = this;
		Utils.BSEBANNER_TIME = System.currentTimeMillis();*/
		/*Utils.LASTUPDATETIME = "";*/
	}

	public int getPreferredHeight()
	{
		return (sourceBitmap.getHeight() + sourceBitmap.getHeight()/2 + borderWidth*2)+FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight();
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
		//graphics.setColor(isFocused == true && dataFeed ? Color.ORANGE : 0xeeeeee);
		//graphics.fillRoundRect(0, padding/2, getPreferredWidth(), getPreferredHeight(), 10, 10);
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
				graphics.setColor(isFocused == true && dataFeed ? Color.ORANGE : 0xeeeeee);
				graphics.fillRoundRect(0, (getPreferredHeight()/2)-((h+(borderWidth*2))/2), getPreferredWidth(), h+(borderWidth*2), 10, 10);
				graphics.setColor((isMarketUp == true ? Color.GREEN : Color.RED));				
				graphics.fillRoundRect(borderWidth, (((getPreferredHeight())/2)-(h/2)), (getPreferredWidth()-borderWidth*2), h, 10, 10);
			}
			
		}
		else if(dataFeed)
		{
			//Draw White Border Background
			graphics.setColor(isFocused == true && dataFeed ? Color.ORANGE : 0xeeeeee);
			graphics.fillRoundRect(0, padding/2, getPreferredWidth(), getPreferredHeight(), 10, 10);
			
			graphics.setColor((isMarketUp == true ? Color.GREEN : Color.RED));				
			graphics.fillRoundRect(borderWidth, borderWidth + padding/2, getPreferredWidth()-borderWidth*2, getPreferredHeight()-borderWidth*2, 10, 10);
			int length = 0 ;
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			length = graphics.getFont().getAdvance("########");
			//Set Colors and font
			graphics.setColor(0xeeeeee);
			graphics.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			//Draw Value
			//graphics.drawText(getValue(), getPreferredWidth()/2 - graphics.getFont().getAdvance(getValue())/2, getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2);
			graphics.drawText(getCompanyName(), (borderWidth + padding + sourceBitmap.getWidth()) + ((getPreferredWidth()-length)-(borderWidth + padding + sourceBitmap.getWidth()))/2 - graphics.getFont().getAdvance(getValue())/2,( getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2)-((FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight()/2)+2));
			if(!getValue().equalsIgnoreCase("No Data"))
			graphics.drawText(getValue(), (borderWidth + padding + sourceBitmap.getWidth()) + ((getPreferredWidth()-length)-(borderWidth + padding + sourceBitmap.getWidth()))/2 - graphics.getFont().getAdvance(getValue())/2, (getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2)+((FontLoader.getFont(AppConstants.BIG_BOLD_FONT).getHeight()/2)+2));
			//Draw Changed Value
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT));
			//Draw Changed Value
			graphics.drawText(getChangedValue(), (getPreferredWidth() - length + 1)+(length + 1 - graphics.getFont().getAdvance(getChangedValue()))/2, getPreferredHeight()/2 - graphics.getFont().getHeight()-1+ padding/2-2);
			//Draw Perchantage
			graphics.drawText(getPercentage(), (getPreferredWidth() - length + 1)+(length + 1 - graphics.getFont().getAdvance(getPercentage()))/2, getPreferredHeight()/2 + graphics.getFont().getHeight()+1-padding-padding/2+2);
			//BSE NSE Image
			graphics.drawBitmap(borderWidth + padding/2, padding/2 + (getPreferredHeight()/2 - sourceBitmap.getHeight()/2), sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap, 0, 0);
			//Draw Lines
			graphics.setColor(0xeeeeee);
			graphics.drawLine(borderWidth + padding + sourceBitmap.getWidth(),borderWidth + padding/2, borderWidth + padding + sourceBitmap.getWidth(), getPreferredHeight()-borderWidth+padding/2);
			graphics.drawLine( getPreferredWidth()-length,borderWidth + padding/2 ,getPreferredWidth()-length ,getPreferredHeight()-borderWidth+padding/2 );
			graphics.drawLine( getPreferredWidth()-length,padding/2 +getPreferredHeight()/2,getPreferredWidth()-borderWidth,padding/2 + getPreferredHeight()/2 );
		} 
		else if(firstTime)
		{
			//Draw White Border Background
			graphics.setColor(isFocused == true && dataFeed ? Color.ORANGE : 0xeeeeee);
			graphics.fillRoundRect(0, padding/2, getPreferredWidth(), getPreferredHeight(), 10, 10);
			
			graphics.setColor(Color.GRAY);
			graphics.fillRoundRect(borderWidth, borderWidth + padding/2, getPreferredWidth()-borderWidth*2, getPreferredHeight()-borderWidth*2, 10, 10);
			graphics.setColor(0xeeeeee);
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			graphics.drawText("Fetching Data", getPreferredWidth()/2 - graphics.getFont().getAdvance("Fetching Data")/2, getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2);
			graphics.drawBitmap(borderWidth + padding/2, padding/2 + (getPreferredHeight()/2 - sourceBitmap.getHeight()/2), sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap, 0, 0);
			//Draw Lines
			graphics.setColor(0xeeeeee);
			graphics.drawLine(borderWidth + padding + sourceBitmap.getWidth(),borderWidth + padding/2, borderWidth + padding + sourceBitmap.getWidth(), getPreferredHeight()-borderWidth+padding/2);
			
		}
	}

	protected void drawFocus(Graphics graphics, boolean on) 
	{
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
		if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			sourceBitmap = new Bitmap(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getAdvance(getSource()),FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight());
		else sourceBitmap = new Bitmap(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getAdvance(getSource()),FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight());
		Graphics graphics = new Graphics(sourceBitmap);
		graphics.setColor(backColor);
		graphics.fillRect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
		graphics.setColor(0xeeeeee);
		if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
		else graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
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
	
	public String getCompanyName()
	{
		return companyName;
	}
	
	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
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
	public void setStartFlag(boolean flag)
	{
		firstTime = flag;
	}
	public void startUpdate()
	{/*
		int height = getPreferredHeight() + borderWidth + padding/2;
		refreshImage = new Bitmap( getPreferredWidth(), height);
		Graphics graphics = new Graphics(refreshImage);
		graphics.setColor(0x000000);
		graphics.fillRect(0, 0, getPreferredWidth(), height);
		//Draw White Border Background
		graphics.setColor(isFocused == true && dataFeed ? Color.ORANGE : 0xeeeeee);
		graphics.fillRoundRect(0, padding/2, getPreferredWidth(), getPreferredHeight(), 10, 10);
		
		graphics.setColor((isMarketUp == true ? Color.GREEN : Color.RED));				
		graphics.fillRoundRect(borderWidth, borderWidth + padding/2, getPreferredWidth()-borderWidth*2, getPreferredHeight()-borderWidth*2, 10, 10);
		int length = 0 ;
		graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
		length = graphics.getFont().getAdvance("########");
		//Set Colors and font
		graphics.setColor(0xeeeeee);
		graphics.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
		//Draw Value
		//graphics.drawText(getValue(), getPreferredWidth()/2 - graphics.getFont().getAdvance(getValue())/2, getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2);
		graphics.drawText(getValue(), (borderWidth + padding + sourceBitmap.getWidth()) + ((getPreferredWidth()-length)-(borderWidth + padding + sourceBitmap.getWidth()))/2 - graphics.getFont().getAdvance(getValue())/2, getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2);
		//Draw Changed Value
		graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT));
		//Draw Changed Value
		graphics.drawText(getChangedValue(), (getPreferredWidth() - length + 1)+(length + 1 - graphics.getFont().getAdvance(getChangedValue()))/2, getPreferredHeight()/2 - graphics.getFont().getHeight()-1+ padding/2);
		//Draw Perchantage
		graphics.drawText(getPercentage(), (getPreferredWidth() - length + 1)+(length + 1 - graphics.getFont().getAdvance(getPercentage()))/2, getPreferredHeight()/2 + graphics.getFont().getHeight()+1-padding-padding/2);
		//BSE NSE Image
		graphics.drawBitmap(borderWidth + padding/2, padding/2 + (getPreferredHeight()/2 - sourceBitmap.getHeight()/2), sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap, 0, 0);
		//Draw Lines
		graphics.setColor(0xeeeeee);
		graphics.drawLine(borderWidth + padding + sourceBitmap.getWidth(),borderWidth + padding/2, borderWidth + padding + sourceBitmap.getWidth(), getPreferredHeight()-borderWidth+padding/2);
		graphics.drawLine( getPreferredWidth()-length,borderWidth + padding/2 ,getPreferredWidth()-length ,getPreferredHeight()-borderWidth+padding/2 );
		graphics.drawLine( getPreferredWidth()-length,padding/2 +getPreferredHeight()/2,getPreferredWidth()-borderWidth,padding/2 + getPreferredHeight()/2 );
	
		startUpdate = true;
		//firstTime = false;
		//if(animationThread == null)
		//{
			animationThread = new Thread(new Runnable() {
			public void run() {
				invalidate();
				try {
					for(int i=1;i<31;i++){
						if(firstTime)
						{
							LOG.print("firsttime flag recorded-------------------------");
							i=31;
							startUpdate = false;
							invalidate();
							//i=0;
							//firstTime = false;
							}
							else
							{
					Thread.sleep(10);
					if(i==30)
					startUpdate = false;
					invalidate();
							}
					}
				} catch (InterruptedException e) {
					LOG.print("Exception in banner screen");
					startUpdate = false;
					invalidate();
				}
			}});
			animationThread.start();
			//}*/
	}
}
