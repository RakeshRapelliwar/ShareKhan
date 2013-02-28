package com.snapwork.parsers;

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.lcdui.Image;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.snapwork.beans.CommodityBean;
import com.snapwork.beans.Expiry;
import com.snapwork.beans.FNOSearchBean;
import com.snapwork.beans.SearchBean;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;
import com.snapwork.util.Utils;

public class CommodityParser implements HttpResponse{
	public String feedurl = "";
	public Vector homeData = null;
	final CommodityBean searchBean = new CommodityBean();
	Screen screen = null;
	ThreadedComponents threadedComponents = null;
	private String text ;
	private String typedText;
//	private String typedateTextID ;
//	private FNOSearchBean bean;
	public CommodityParser(String type, String typedateText, String url,Screen screen)
	{
		this.feedurl = url;
		this.text = type;
		this.typedText = typedateText;
//		this.typedateTextID = typedateTextID;
		homeData = new Vector();
		this.screen = screen;
	}
//	public CommodityParser(String type, String typedateText, String typedateTextID, String url, FNOSearchBean bean, Screen screen,ThreadedComponents threadedComponents)
//	{
//		this.feedurl = url;
//		this.text = type;
//		this.typedText = typedateText;
////		this.typedateTextID = typedateTextID;
//		homeData = new Vector();
//		this.screen = screen;
//		this.bean = bean;
//		LOG.print("bean.getExpiry() : "+bean.getExpiry());
//		LOG.print("bean.getCode() : "+bean.getCode());
//		LOG.print("bean.getOption() : "+bean.getOption());
//		if(bean.getCode().length()>0)
//		{
//			if(bean.getExpiry()==-1)
//			{
//				this.feedurl = Utils.getCompanySearchURL(bean.getCode().toUpperCase());
//			}
//			else if(bean.getOption().trim().length()>0)
//			{
//				if(bean.getStrike().equalsIgnoreCase("Z"))
//					bean.setStrike("");
//				this.feedurl = Utils.getCompanyFNOSearchURL_STRIKE(bean.getCode().toUpperCase(), Expiry.getValue(bean.getExpiry()), bean.getOption(), bean.getStrike());
//			}
//			else 
//			{
//				//this.feedurl = Utils.getCompanyFNOSearchURL(bean.getCode(), Expiry.getValue(bean.getExpiry()));
//				this.feedurl = Utils.getCompanyFNOSearchURL(bean.getCode().toUpperCase(),Expiry.getValue(bean.getExpiry()));
//			}
//		}
//		this.threadedComponents = threadedComponents;
//	}
	public void getScreenData()
	{
		HttpProcess.threadedHttpConnection(feedurl, this);
	}
	public void setResponse(final String rsponse)
	{
		LOG.print(rsponse);
		
		
		
		
		Json js = new Json(rsponse);
//		homeData.addElement(type);
//		homeData.addElement(typedateText);
//		homeData.addElement(typedateTextID);
//		homeData.addElement(bean.copy());
//		if(bean.getExpiry()!=-1)
//		{
			for(int i=0;i<js.getdata.size();i++)
			{
				final Hashtable ht = (Hashtable) js.getdata.elementAt(i);
				searchBean.setToken((String)ht.get("Token"));
				
				
				
				
				
				searchBean.setScripName((String)ht.get("ScripName"));
				searchBean.setExpiryDate((String)ht.get("ExpiryDate"));
				searchBean.setSymbol((String)ht.get("Symbol"));
				searchBean.setExchange((String)ht.get("Exchange"));
				homeData.addElement(searchBean.copy());
			}
//		}
//		else
//		{
//			for(int i=0;i<js.getdata.size();i++)
//			{
//				Hashtable ht = (Hashtable) js.getdata.elementAt(i);
//				searchBean.setName((String)ht.get("CompanyName"));
//				//searchBean.setId((String)ht.get("CompanyCode"));
//				searchBean.setId((String)ht.get("ReligareCode"));
//				searchBean.setSymbol((String)ht.get("NSECode"));
//				homeData.addElement(searchBean.copy());
//			}
//		}
		screen.setData(homeData,null);	
	}
	public void exception(Exception ex)
	{
	}
	public void setResponse(Image img, int id)
	{

	}
	public void setResponse(Image img)
	{
	}
	public void setResponse(Image image, String name)
	{
	}
}
