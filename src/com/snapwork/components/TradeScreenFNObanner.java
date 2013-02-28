package com.snapwork.components;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.beans.Expiry;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.FNOTradeConfiguration;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.URLEncode;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;

/**
 * 
 * <p>This class is part of Future and Option Screen.
 * <p>This class create custom field which shows details of Futures sections label fields of next three months.
 *
 */

public class TradeScreenFNObanner extends Field 
{
	private Bitmap sourceBitmap = null;
	private boolean isFocused = false;
	private String companyID = null;
	private String value = null;
	private String changedValue = null;
	private String percentage = null;
	private String source = null;
	private String openInterest = null;
	private String volume = null;
	private byte padding = 4;
	private byte borderWidth = 3;
	private boolean dataFeed = false;
	private boolean isMarketUp = false;
	private int ht;
	private boolean crossFlag;
	private String comp;
	private String name;
	private int leftMargin;
	private int rightMargin;
	private String date;
	public TradeScreenFNObanner(Font font,String name, String source, String comp_,boolean flag) 
	{
		super(Field.NON_FOCUSABLE | FIELD_HCENTER | FIELD_HCENTER);
		setFont(font);
		crossFlag = flag;

		LOG.print("source : "+source);
		date = source;
		LOG.print("comp_ : "+comp_);
		setName(name);
		if(source.length()==12)
			setSource(Expiry.getText(source));
		else
			setSource(source);
		LOG.print("getSource() : "+getSource());
		setComp(comp_);

		//ht = FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT).getHeight()+6;
		ht = 0;
		
	}

	public String getComp() {
		return comp;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLeftMargin() {
		return leftMargin;
	}

	public void setLeftMargin(int leftMargin) {
		this.leftMargin = leftMargin;
	}

	public int getRightMargin() {
		return rightMargin;
	}

	public void setRightMargin(int rightMargin) {
		this.rightMargin = rightMargin;
	}

	public int getPreferredHeight() {
		return (getFont().getHeight()*3+AppConstants.padding*2)+ht+6;
	}

	public int getPreferredWidth() {
		return AppConstants.screenWidth;
	}

	public void setData(String value,String changedValue,String percentage, String openInterest, String volume) 
	{
		if(value.equalsIgnoreCase("NULL") )
			setValue("No Quote");
		else
			setValue(value);
		if(percentage.equalsIgnoreCase("+NULL") )
			setPercentage(" - ");
		else
			setPercentage(percentage+"% ");
		LOG.print(percentage);
		if(openInterest.equalsIgnoreCase("NULL") )
			setOpenInterest("Open Interest 0");
		else
			setOpenInterest("Open Interest "+openInterest.substring(0, (openInterest.indexOf('.')>-1?openInterest.indexOf('.'): openInterest.length())));
		
		if(volume.equalsIgnoreCase("NULL") )
			setVolume("Volume -");
		else
			setVolume("Volume "+volume);
		dataFeed = true;
		try
		{
			isMarketUp = (changedValue.indexOf("-")==-1);
			if(changedValue.equalsIgnoreCase("NULL") )
				setChangedValue(" - ");
			else
				setChangedValue((isMarketUp == true ? "+" + changedValue : changedValue ));
			
			if(getPercentage().indexOf("+")==-1 && !getPercentage().equalsIgnoreCase(" - "))
				setPercentage((isMarketUp == true ? "+" + getPercentage() : getPercentage() ));
		} 
		catch(Exception ex) 
		{
			LOG.print("Value : "+value);
			LOG.print("changedValue : "+changedValue);
			LOG.print("percentage : "+getPercentage());
		}
		createSourceBitmap((isMarketUp == true ? Color.GREEN : Color.RED));
		invalidate();
	}
	public void refresh()
	{
		invalidate();
	}

	private void createSourceBitmap(int backColor) 
	{
	
		if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			sourceBitmap = new Bitmap(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getAdvance(getSource()),FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight());
		else sourceBitmap = new Bitmap(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getAdvance(getSource()),FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getHeight());
		Graphics graphics = new Graphics(sourceBitmap);
		graphics.setColor(backColor);
		graphics.fillRect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
		graphics.setColor(Color.WHITE);
		if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
		else 
			graphics.setFont(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT));
		graphics.drawText(getSource(), 0, 0);
		sourceBitmap = ImageManager.rotateBitmap(sourceBitmap, 270);
		
	}

	protected void layout(int width, int height) 
	{
		setExtent(getPreferredWidth(), getPreferredHeight());
	}
	protected void onFocus(int direction) 
	{
		isFocused = true;
		invalidate();
	}

	protected void onUnfocus() 
	{
		isFocused = false;
		invalidate();
	}
	protected void paint(Graphics graphics)
	{
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
		graphics.setColor(isFocused() == true ? Color.ORANGE : Color.WHITE);
		graphics.fillRoundRect(getLeftMargin()+borderWidth/2,borderWidth/2, getPreferredWidth()-borderWidth-getLeftMargin()-getRightMargin(), getPreferredHeight()-borderWidth, 10, 10);
		if(dataFeed)
		{
			graphics.setColor((isMarketUp == true ? Color.GREEN : Color.RED));				
			graphics.fillRoundRect(getLeftMargin()+borderWidth, borderWidth, getPreferredWidth()-borderWidth*2-1-getLeftMargin()-getRightMargin(), getPreferredHeight()-borderWidth*2-1, 10, 10);
			int length = 0 ;
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			length = graphics.getFont().getAdvance("##########");
			//Set Colors and font
			graphics.setColor(Color.WHITE);
			graphics.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
			graphics.drawText(getName(), getLeftMargin()+(borderWidth + padding + sourceBitmap.getWidth()) + ((getPreferredWidth()-length)-(borderWidth + padding + sourceBitmap.getWidth()))/2 - graphics.getFont().getAdvance(getName())/2, (getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2)-ht/2-padding - (graphics.getFont().getHeight()/2) - 2);
			//Draw Value
			/*String sltp = getValue().replace(' ', '_');
			if(sltp.equalsIgnoreCase("No_Data"))
			{
				sltp = "0.00";
				graphics.drawText(sltp, getLeftMargin()+(borderWidth + padding + sourceBitmap.getWidth()) + ((getPreferredWidth()-length)-(borderWidth + padding + sourceBitmap.getWidth()))/2 - graphics.getFont().getAdvance(sltp)/2, (getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2)-ht/2-padding + (graphics.getFont().getHeight()/2) + 2);
			}
			else*/
				graphics.drawText(getValue(), getLeftMargin()+(borderWidth + padding + sourceBitmap.getWidth()) + ((getPreferredWidth()-length)-(borderWidth + padding + sourceBitmap.getWidth()))/2 - graphics.getFont().getAdvance(getValue())/2, (getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2)-ht/2-padding + (graphics.getFont().getHeight()/2) + 2);
			//Draw Changed Value
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT));
			//Draw Changed Value
			graphics.drawText(getChangedValue(), (getPreferredWidth() - length + 1)+(length + 1 - graphics.getFont().getAdvance(getChangedValue()))/2 - getRightMargin(), (getPreferredHeight()/2 - graphics.getFont().getHeight()-1+ padding/2)-ht/2-padding+padding/2);
			//Draw Perchantage
			graphics.drawText(getPercentage(), (getPreferredWidth() - length + 1)+(length + 1 - graphics.getFont().getAdvance(getPercentage()))/2 - getRightMargin(), (getPreferredHeight()/2 + graphics.getFont().getHeight()+1-padding-padding/2)-ht/2-padding);
			//BSE NSE Image
			graphics.drawBitmap(getLeftMargin()+borderWidth + padding/2, ( borderWidth+padding), sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap, 0, 0);
			graphics.setColor(Color.WHITE);
			//graphics.setColor((isMarketUp == true ? 0x28d52e : 0xd15354));
			graphics.drawLine(borderWidth + padding + sourceBitmap.getWidth()+getLeftMargin(),(borderWidth ), borderWidth + padding + sourceBitmap.getWidth()+getLeftMargin(),( getPreferredHeight()-borderWidth+padding/2)-ht-3);
			//Draw Lines
			graphics.drawLine( getPreferredWidth()-getRightMargin()-length,(borderWidth) ,getPreferredWidth()-getRightMargin()-length ,( getPreferredHeight()-borderWidth+padding/2)-ht-3); //Verticle line last
			graphics.drawLine( getPreferredWidth()-getRightMargin()-length,(getPreferredHeight()-borderWidth-ht)/2+padding/2,getPreferredWidth()-getRightMargin()-borderWidth-padding/2,(getPreferredHeight()-borderWidth-ht)/2+padding/2); // horizontal line middle
			
			/*graphics.drawLine(borderWidth,getPreferredHeight()-borderWidth-ht,getPreferredWidth()-(borderWidth)-padding/2,getPreferredHeight()-borderWidth-ht);
			if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			{
				graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT_FNO));
				graphics.drawText(getOpenInterest(),borderWidth+padding/2, 4+getPreferredHeight()-ht);
				length = graphics.getFont().getAdvance(getVolume());
				graphics.drawText(getVolume(), getPreferredWidth()-length-(borderWidth+padding/2), 4+getPreferredHeight()-ht);
			}
			else
			{
				graphics.drawText(getOpenInterest(),borderWidth+padding/2, getPreferredHeight()-ht);
				length = graphics.getFont().getAdvance(getVolume());
				graphics.drawText(getVolume(), getPreferredWidth()-length-(borderWidth+padding/2), getPreferredHeight()-ht);
			
			}*/
		}
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
		return date;
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
		String sltp = value.replace(' ', '_');
		if(sltp.equalsIgnoreCase("No_Data"))
		{
			value = "0.00";
		}
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

	public String getOpenInterest() 
	{
		return openInterest;
	}

	public void setOpenInterest(String openInterest) 
	{
		this.openInterest = openInterest;
	}

	public String getVolume() 
	{
		return volume;
	}

	public void setVolume(String volume) 
	{
		this.volume = volume;
	}
	
	protected boolean navigationClick(int arg0, int arg1) {
		if(!Utils.sessionAlive)
		{
			ActionInvoker.processCommand(new Action(ActionCommand.CMD_SESSION_EXPIRED,null));
		}
		else
		{

		ScreenInvoker.showWaitScreen(AppConstants.loadingMessage);

		if(crossFlag) {
			AppConstants.WEBVIEW_URL_TEXT="Futures";
		} else {
			AppConstants.WEBVIEW_URL_TEXT="Options";
		}

		String textUrl = getComp();
		if(textUrl.indexOf("&")>-1)
			textUrl = URLEncode.getString(textUrl);
		
		Vector dataVector = new Vector();

		String indexName = "";
		
		if(textUrl.indexOf("_")!=-1) {
			indexName = textUrl.substring(0, textUrl.indexOf("_"));
		} else {
			indexName = textUrl;
		}

		dataVector.addElement(Utils.getFNOExpiryAndStrikeDataURL(textUrl));
		dataVector.addElement(indexName);

		FNOTradeConfiguration fnoTradeConfig = new FNOTradeConfiguration();
		
		if(textUrl.indexOf("_CE")!=-1) {
			fnoTradeConfig.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_OPTIDX);
		} else  {
			fnoTradeConfig.setFnoTradeType(FNOTradeConfiguration.FNO_TRADE_TYPE_FUTIDX);			
		}
		fnoTradeConfig.setFnoOrderType(FNOTradeConfiguration.FNO_TRADE_ORDER_TYPE_LIMIT);
		dataVector.addElement(fnoTradeConfig);
		
		Action action = new Action(ActionCommand.CMD_FNO_TRADE,dataVector);
		ActionInvoker.processCommand(action);

		/*AppConstants.WEBVIEW_URL=Utils.getFNOURL(textUrl, UserInfo.getUserID());
		        		UiApplication.getUiApplication().invokeLater(new Runnable() {
		                    public void run() {
		                    	Action action = new Action(ActionCommand.CMD_WEBVIEW_SCREEN, null);
		        				ActionInvoker.processCommand(action);
		                    	}
		            });*/
		}
		return true;
		//return super.navigationClick(arg0, arg1);

	}
}