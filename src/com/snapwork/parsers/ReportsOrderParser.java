
package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import com.snapwork.beans.News;
import com.snapwork.beans.ReportsOrderBean;
import com.snapwork.beans.TradeNowMainBean;
import com.snapwork.beans.KeyValueBean;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.components.WaitScreen;
import com.snapwork.interfaces.ReturnData;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;
import com.snapwork.view.trade.TradeNowMainScreen;

public class ReportsOrderParser implements HttpResponse
{
	public String feedurl = "";
	public Vector data = null;
	ReportsOrderParser tradepar;
	final ReportsOrderBean currentMessage = new ReportsOrderBean();
	final Hashtable currentMessageMod = new Hashtable();
	private ReturnData screen = null;
	private ReturnDataWithId screenWithId = null;
	private String title = "";
	private String error = "";
	private int id;

	public ReportsOrderParser(String url,ReturnData screen)
	{
		this.feedurl = url;
		data = new Vector();
		this.screen = screen;
	WaitScreen.HTTPCALL = true;
		getScreenData();
	}
	
	public ReportsOrderParser(String url, ReturnDataWithId screen, int id)
	{
		this.feedurl = url;
		data = new Vector();
		this.screenWithId = screen;
	WaitScreen.HTTPCALL = true;
	this.id = id;
		getScreenData();
	}


	public void getScreenData()
	{
		HttpProcess.threadedHttpsMD5Connection(feedurl, this);
	}

	public void setResponse(String rsponse)
	{
		if(rsponse == null)
			return;
		LOG.print(rsponse);
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
						if(tagname1.equals("WEB_URL") || tagname1.equals("IPHONE_WEB_URL") || tagname1.equals("USER_AGENT"))
						{
							parser.nextText();
						}
						else
							parser.nextText();
						parser.require(XmlPullParser.END_TAG, null, tagname1);
					}
				}
				else if(tagname.equals("custId"))
				{
					parser.nextText();
				}
				else if(tagname.equals("pagetitle"))
				{
					title = parser.nextText();
				}
				else if(tagname.equals("userAgent"))
				{
					parser.nextText();
				}
				else if(tagname.equals("errormsg"))
				{
					error = parser.nextText();
				}
				else if(tagname.equals("resultCount"))
				{
					parser.nextText();
				}
				else if(tagname.equals("sortby"))
				{
					parser.nextText();
				}
				else if(tagname.equals("data"))
				{
					ReportsOrderBean cmBean = new ReportsOrderBean();
				
					while(parser.nextTag() != XmlPullParser.END_TAG)
					{
						parser.require(XmlPullParser.START_TAG, null, null);
						String tagname1 = parser.getName().trim();
						/*if(tagname1.equals("customer_id"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("order_id"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("exchange"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("scripcode"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("ordertype"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("segment"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("rmscode"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("orderqty"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("orderprice"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("execqty"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("execprice"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("disclosedqty"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("trigprice"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("orderstatus"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("requeststatus"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("orderdatetime"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("exchorderid"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("exchdatetime"))
						{
							parser.nextText();
						}*/
						LOG.print("-------------- "+tagname1);
						cmBean.setStringHolder(tagname1, parser.nextText());
						parser.require(XmlPullParser.END_TAG, null, tagname1);
					}
					data.addElement(cmBean.copy());
				}
				else if(tagname.equals("status_list"))
				{
					while(parser.nextTag() != XmlPullParser.END_TAG)
					{
						parser.require(XmlPullParser.START_TAG, null, null);
						String tagname1 = parser.getName().trim();
						if(tagname1.equals("data"))
						{
							while(parser.nextTag() != XmlPullParser.END_TAG)
							{
								parser.require(XmlPullParser.START_TAG, null, null);
								String tagname2 = parser.getName().trim();
								if(tagname2.equals("status_nm"))
								{
									parser.nextText();
								}
								parser.require(XmlPullParser.END_TAG, null, tagname2);
							}
						}
						parser.require(XmlPullParser.END_TAG, null, tagname1);
					}
				}
				else
					parser.nextText();
				parser.require(XmlPullParser.END_TAG, null, tagname);
				//if(currentMessageMod.getKey()!=null)
				//	tradedata.addElement(currentMessage.copy());
				//currentMessageMod.setKey(null);
				//currentMessageMod.setValue(null);
			}
			parser.require(XmlPullParser.END_TAG, null, "root");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		if(WaitScreen.HTTPCALL)
		{
			//data.insertElementAt(title,0);
			Vector vec = new Vector();
			vec.addElement(title);
			vec.addElement(error);
			vec.addElement(data);
			if(screenWithId == null)
				screen.setData(vec);
			else
				screenWithId.setData(vec, id);
			}
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