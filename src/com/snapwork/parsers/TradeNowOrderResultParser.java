
package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import com.snapwork.beans.News;
import com.snapwork.beans.TradeNowMainBean;
import com.snapwork.beans.KeyValueBean;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.WaitScreen;
import com.snapwork.interfaces.ReturnData;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;
import com.snapwork.view.trade.TradeNowMainScreen;

public class TradeNowOrderResultParser implements HttpResponse
{
	public String feedurl = "";
	public Vector tradedata = null;
	TradeNowOrderResultParser tradepar;
	final KeyValueBean currentMessage = new KeyValueBean();
	private ReturnData screen = null;
	private boolean httpKill; 
	private ThreadedComponents threadedComponets = null;
	
	public TradeNowOrderResultParser(String url,ReturnData screen)
	{
		this.feedurl = url;
		tradedata = new Vector();
		this.screen = screen;
		httpKill = false;
		WaitScreen.HTTPCALL = true;
		getScreenData();
	}


	public void getScreenData()
	{
		LOG.print(feedurl);
		HttpProcess.threadedHttpsMD5Connection(feedurl, this);
	}

	public void setResponse(String rsponse)
	{
		if(rsponse == null)
			return;
		KXmlParser parser = null;
		ByteArrayInputStream byteArrayInputStream = null;
		InputStreamReader is = null;
		byte[] currentXMLBytes;
		try
		{
			currentXMLBytes = rsponse.getBytes();
			byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes);
			is = new InputStreamReader(byteArrayInputStream);
			parser = new KXmlParser();
			parser.setInput(is);
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "root");
			while(parser.nextTag() != XmlPullParser.END_TAG)
			{
				parser.require(XmlPullParser.START_TAG, null, null);
				String tagname = parser.getName().trim();
				if(tagname.equals("URL"))
				{
					while(parser.nextTag() != XmlPullParser.END_TAG)
					{
						parser.require(XmlPullParser.START_TAG, null, null);
						String tagname1 = parser.getName().trim();
						if(tagname1.equals("WEB_URL"))
						{
							parser.nextText();
						}
						else
							parser.nextText();
						parser.require(XmlPullParser.END_TAG, null, tagname1);
					}
				}
				else if(tagname.equals("exchange"))
				{
					currentMessage.setKey("exchange");
					currentMessage.setValue(parser.nextText());
				}
				else if(tagname.equals("error_msg"))
				{
					currentMessage.setKey("error_msg");
					currentMessage.setValue(parser.nextText());
				}
				else if(tagname.equals("order_number"))
				{
					currentMessage.setKey("order_number");
					currentMessage.setValue(parser.nextText());
				}
				else if(tagname.equals("custId"))
				{
					currentMessage.setKey("custId");
					currentMessage.setValue(parser.nextText());
				}
				else if(tagname.equals("dpId"))
				{
					currentMessage.setKey("dpId");
					currentMessage.setValue(parser.nextText());
				}
				else if(tagname.equals("userAgent"))
				{
					parser.nextText();
				}
				else
					parser.nextText();
				/*else
					parser.nextText();*/
				parser.require(XmlPullParser.END_TAG, null, tagname);
				if(currentMessage.getKey()!=null)
				tradedata.addElement(currentMessage.copy());
				currentMessage.setKey(null);
				currentMessage.setValue(null);
				//System.out.println("inner tradedata.size() : "+tradedata.size());
				}
			parser.require(XmlPullParser.END_TAG, null, "root");
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		//tradedata.addElement(currentMessage.copy());
		
		
		//System.out.println("tradedata.size() : "+tradedata.size());
		if(WaitScreen.HTTPCALL)
			screen.setData(tradedata);
	}
	public void httpKill()
	{
		httpKill = true;
	}
	public void getTradeData()
	{
		HttpProcess.threadedHttpsMD5Connection(feedurl, this);
	}
	public void exception(Exception ex)
	{
	}
	public void setResponse(Image img)
	{
	}
	public void setResponse(Image image, int id)
	{
	}
	public void setResponse(Image image, String name)
	{
	}
}