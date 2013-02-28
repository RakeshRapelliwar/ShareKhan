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
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.Expiry;
import com.snapwork.beans.HomeJson;
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
import com.snapwork.view.CompanyFNODetailsSnippetsScreen;

/**
 * 
 * <p>This class is part of Future and Option Screen.
 * <p>This class create custom field which shows details of Futures sections label fields of next three months.
 *
 */

public class CustomFNOLabelField extends Field 
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
	private HomeJson bannerData;
	private String fnoExpiryDate;
	private ActionListener actionListener;
	private boolean showStockScreen;
	private int dateTextID;
	
	/*public CustomFNOLabelField(Font font,String source, String comp_,boolean flag) 
	{
		super(Field.FOCUSABLE | FIELD_HCENTER | FIELD_HCENTER);
		setFont(font);
		crossFlag = flag;

		LOG.print("source : "+source);
		LOG.print("comp_ : "+comp_);

		setSource(source);
		setComp(comp_);

		ht = FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT).getHeight()+6;
	}*/
	private boolean addDirect;
	public CustomFNOLabelField(Font font,String source, String comp_,boolean flag,HomeJson bannerData, String fnoExpiryDate, ActionListener actionListener) 
	{
		super(Field.FOCUSABLE | FIELD_HCENTER | FIELD_HCENTER);
		setFont(font);
		crossFlag = flag;
 this.fnoExpiryDate = fnoExpiryDate;
 this.actionListener = actionListener;
 LOG.print("bannerData.getBuyQty(): - "+bannerData.getBuyQty()[0]+"bannerData.getBuyPrice() - "+ bannerData.getBuyPrice()[0]);
	
 //source = Expiry.dateDec(comp_.substring(comp_.length()-10, comp_.length()-8), comp_.substring(comp_.length()-7, comp_.length()-5));
 source = Expiry.dateDec(comp_.substring(comp_.indexOf("_")+1, comp_.indexOf("_")+3), comp_.substring(comp_.indexOf("_")+4, comp_.indexOf("_")+6));
		LOG.print("source : "+source);
		LOG.print("comp_ : "+comp_);

		setSource(source);
		setComp(comp_);
		this.bannerData = bannerData;
		ht = FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT).getHeight()+6;
	}

	public String getComp() {
		return comp;
	}

	public void setComp(String comp) {
		this.comp = comp;
	}

	public boolean getAddDirect() {
		return addDirect;
	}

	public void setAddDirect(boolean addDirect) {
		this.addDirect = addDirect;
	}

	public int getPreferredHeight() {
		return (getFont().getHeight()*3+AppConstants.padding*2)+ht+6;
	}

	public int getPreferredWidth() {
		return AppConstants.screenWidth;
	}

	public void setData(String value,String changedValue,String percentage, String openInterest, String volume, HomeJson homejson) 
	{
		this.bannerData = homejson;
		LOG.print("LTP : "+value);
		if(value.equalsIgnoreCase("NULL") || value.equalsIgnoreCase("No Data"))
			setValue("No Quote");
		else
			setValue(value);
		LOG.print(":LTP : "+value);
		if(percentage.equalsIgnoreCase("NULL") || percentage.length()==0)
			setPercentage(" - ");
		else
			setPercentage(percentage+"% ");
		LOG.print(percentage);
		if(openInterest.equalsIgnoreCase("NULL") || openInterest.length()==0)
			setOpenInterest("Open Interest 0");
		else
			setOpenInterest("Open Interest "+openInterest.substring(0, openInterest.indexOf('.')));
		
		if(volume.equalsIgnoreCase("NULL") || volume.length()==0)
			setVolume("Volume -");
		else
			setVolume("Volume "+volume);
		dataFeed = true;
		try
		{
			isMarketUp = (changedValue.indexOf("-")==-1);
			if(changedValue.equalsIgnoreCase("NULL") || changedValue.length()==0)
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

	
	public boolean isShowStockScreen() {
		return showStockScreen;
	}

	public void setShowStockScreen(boolean showStockScreen) {
		this.showStockScreen = showStockScreen;
	}

	public int getDateTextID() {
		return dateTextID;
	}

	public void setDateTextID(int dateTextID) {
		this.dateTextID = dateTextID;
	}

	private void createSourceBitmap(int backColor) 
	{
		if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			sourceBitmap = new Bitmap(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getAdvance(getSource()),FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight());
		else sourceBitmap = new Bitmap(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT).getAdvance(getSource()),FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT).getHeight());
		Graphics graphics = new Graphics(sourceBitmap);
		graphics.setColor(backColor);
		graphics.fillRect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight());
		graphics.setColor(Color.WHITE);
		if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640 || AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
		else 
			graphics.setFont(FontLoader.getFont(AppConstants.SMALL_PLAIN_FONT));
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
		graphics.fillRoundRect(borderWidth/2,borderWidth/2, getPreferredWidth()-borderWidth, getPreferredHeight()-borderWidth, 10, 10);
		if(dataFeed)
		{
			graphics.setColor((isMarketUp == true ? Color.GREEN : Color.RED));				
			graphics.fillRoundRect(borderWidth, borderWidth, getPreferredWidth()-borderWidth*2-1, getPreferredHeight()-borderWidth*2-1, 10, 10);
			int length = 0 ;
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
			length = graphics.getFont().getAdvance("##########");
			//Set Colors and font
			graphics.setColor(Color.WHITE);
			graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
			//Draw Value
			graphics.drawText(getValue(), (borderWidth + padding + sourceBitmap.getWidth()) + ((getPreferredWidth()-length)-(borderWidth + padding + sourceBitmap.getWidth()))/2 - graphics.getFont().getAdvance(getValue())/2, (getPreferredHeight()/2 - graphics.getFont().getHeight()/2 + padding/2)-ht/2-padding);
			//Draw Changed Value
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_SPECIAL_BANNER_FONT));
			//Draw Changed Value
			graphics.drawText(getChangedValue(), (getPreferredWidth() - length + 1)+(length + 1 - graphics.getFont().getAdvance(getChangedValue()))/2, (getPreferredHeight()/2 - graphics.getFont().getHeight()-1+ padding/2)-ht/2-padding+padding/2);
			//Draw Perchantage
			graphics.drawText(getPercentage(), (getPreferredWidth() - length + 1)+(length + 1 - graphics.getFont().getAdvance(getPercentage()))/2, (getPreferredHeight()/2 + graphics.getFont().getHeight()+1-padding-padding/2)-ht/2-padding);
			//BSE NSE Image
			graphics.drawBitmap(borderWidth + padding/2, ( borderWidth+padding), sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap, 0, 0);
			graphics.setColor(Color.WHITE);
			//graphics.setColor((isMarketUp == true ? 0x28d52e : 0xd15354));
			graphics.drawLine(borderWidth + padding + sourceBitmap.getWidth(),(borderWidth ), borderWidth + padding + sourceBitmap.getWidth(),( getPreferredHeight()-borderWidth+padding/2)-ht-3);
			//Draw Lines
			graphics.drawLine( getPreferredWidth()-length,(borderWidth) ,getPreferredWidth()-length ,( getPreferredHeight()-borderWidth+padding/2)-ht-3); //Verticle line last
			graphics.drawLine( getPreferredWidth()-length,(getPreferredHeight()-borderWidth-ht)/2+padding/2,getPreferredWidth()-borderWidth-padding/2,(getPreferredHeight()-borderWidth-ht)/2+padding/2); // horizontal line middle
			
			graphics.drawLine(borderWidth,getPreferredHeight()-borderWidth-ht,getPreferredWidth()-(borderWidth)-padding/2,getPreferredHeight()-borderWidth-ht);
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
			
			}
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
		String noquote = getValue().replace(' ', '_');
		if(!noquote.equals("No_Quote"))
		{
			LOG.print("bannerData.getBuyQty() - "+bannerData.getBuyQty()[0]+"bannerData.getBuyPrice() - "+ bannerData.getBuyPrice()[0]);
			//if(isShowStockScreen())
			//{
				CompanyFNODetailsSnippetsScreen csScreen = new CompanyFNODetailsSnippetsScreen(bannerData, dateTextID, Utils.WATCHLIST_MODE); 
			/*}
		else{
			Vector vectorCommandData = new Vector();
			vectorCommandData.addElement(getComp());
			vectorCommandData.addElement(getComp());
			vectorCommandData.addElement("NSEFO");
			actionListener.actionPerfomed(ActionCommand.CMD_NIFTY_COMPANY_DETAILS_SCREEN, vectorCommandData);
			}*/
		}
		return super.navigationClick(arg0, arg1);
	}
}