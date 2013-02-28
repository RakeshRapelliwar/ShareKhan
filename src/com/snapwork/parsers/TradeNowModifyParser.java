
package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Image;

import net.rim.device.api.ui.UiApplication;

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
import com.snapwork.util.ScreenInvoker;
import com.snapwork.view.trade.TradeNowMainScreen;

public class TradeNowModifyParser implements HttpResponse
{
	public String feedurl = "";
	public Vector tradedata = null;
	TradeNowModifyParser tradepar;
	final KeyValueBean currentMessage = new KeyValueBean();
	private Hashtable  currentMessageMod = new Hashtable();
	private ReturnData screen = null;
	private ThreadedComponents threadedComponets = null;
	private String[] actionChoiceText = {"Buy","Sell","Sell against Margin","Buy Max","Sell Max"};
	private boolean httpKill;
	private Vector vec;
	private int counter;

	public TradeNowModifyParser(String url,ReturnData screen)
	{
		this.feedurl = url;
		tradedata = new Vector();
		vec = new Vector();
		this.screen = screen;
		counter = 0;
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
				else if(tagname.equals("page"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
				}
				else if(tagname.equals("company_code"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
					currentMessage.setKey("company_code");
					currentMessage.setValue(text);
				}
				else if(tagname.equals("company_name"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
				}
				else if(tagname.equals("exchange"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
					currentMessage.setKey("Exchange");
					currentMessage.setValue(text);
				}
				else if(tagname.equals("ltp"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
				}
				else if(tagname.equals("per_change"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
				}
				else if(tagname.equals("change"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
				}
				else if(tagname.equals("custId"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
				}
				else if(tagname.equals("dpId"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
				}
				else if(tagname.equals("action"))
				{
					currentMessage.setKey("Action");
					String parseText = parser.nextText();
					String text = "";
					if(parseText.equalsIgnoreCase("B"))
						text = actionChoiceText[0];
					else if(parseText.equalsIgnoreCase("S"))
						text = actionChoiceText[1];
					else if(parseText.equalsIgnoreCase("SS"))
						text = actionChoiceText[2];
					/*else
						currentMessage.setValue(parseText);*/
					else if(parseText.equalsIgnoreCase("BM"))
						text = actionChoiceText[3];
					else if(parseText.equalsIgnoreCase("SM"))
						text = actionChoiceText[4];
					else
						text = parseText;
					
					currentMessage.setValue(text);
					currentMessageMod.put(tagname, parseText);
				}
				else if(tagname.equals("qty"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
					currentMessage.setKey("Qty");
					currentMessage.setValue(text);
				}
				else if(tagname.equals("disc_qty"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
					currentMessage.setKey("Disclosed Qty");
					currentMessage.setValue(text);
				}
				else if(tagname.equals("stopPrice"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
					currentMessage.setKey("Trigger Price");
					currentMessage.setValue(text);
				}
				else if(tagname.equals("orderType"))
				{
					currentMessage.setKey("Order Type");
					String texts = parser.nextText();
					String text = texts.replace(texts.charAt(0), Character.toUpperCase(texts.charAt(0)));
					currentMessage.setValue(text);
					currentMessageMod.put(tagname, texts);
				}
				else if(tagname.equals("type"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
				}
				else if(tagname.equals("limitPrice"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
					currentMessage.setKey("Price");
					currentMessage.setValue(text);
				}
				else if(tagname.equals("userAgent"))
				{
					String text = parser.nextText();
					currentMessageMod.put(tagname, text);
				}
				else
					parser.nextText();
				parser.require(XmlPullParser.END_TAG, null, tagname);
				if(currentMessage.getKey()!=null)
				{
					//if(counter<3)
					{
						tradedata.addElement(currentMessage.copy());
						counter++;
					}
					//else
						//vec.addElement(currentMessage.copy());
				}
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
		{
			//tradedata.addElement(currentMessageMod);
			/*KeyValueBean kb = (KeyValueBean) tradedata.elementAt(tradedata.size()-1);
			tradedata.removeElementAt(tradedata.size()-1);
			tradedata.insertElementAt(kb, 3);
			kb = (KeyValueBean) tradedata.elementAt(tradedata.size()-1);
			tradedata.removeElementAt(tradedata.size()-1);
			tradedata.insertElementAt(kb, 2);
			*/
			/*kb = (KeyValueBean) tradedata.elementAt(4);
			tradedata.addElement(kb);*/
			/*if(vec!=null)
			{
				tradedata.addElement(vec.elementAt(3));
				tradedata.addElement(vec.elementAt(1));
				tradedata.addElement(vec.elementAt(2));
				tradedata.addElement(vec.elementAt(0));
			}*/
				
			screen.setData(tradedata);
				}
	
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