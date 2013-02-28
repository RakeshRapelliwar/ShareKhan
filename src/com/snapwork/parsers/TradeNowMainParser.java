
package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
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
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;
import com.snapwork.view.trade.TradeNowMainScreen;

public class TradeNowMainParser implements HttpResponse
{
	public String feedurl = "";
	public Vector tradedata = null;
	TradeNowMainParser tradepar;
	final TradeNowMainBean currentMessage = new TradeNowMainBean();
	private Hashtable  currentMessageMod = new Hashtable();
	private ReturnDataWithId screen = null;
	private ThreadedComponents threadedComponets = null;
	private Vector dpidHold;
	private boolean httpKill;
	private int id;

	public TradeNowMainParser(String url,ReturnDataWithId screen, int id)
	{
		this.feedurl = url;
		this.id = id;
		tradedata = new Vector();
		this.screen = screen;
		dpidHold = new Vector();
		httpKill = false;
		WaitScreen.HTTPCALL = true;
		LOG.print("TradeNowMainParser start");
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
				if(tagname.equals("dpId_data"))
				{
					while(parser.nextTag() != XmlPullParser.END_TAG)
					{
						parser.require(XmlPullParser.START_TAG, null, null);
						String tagname1 = parser.getName().trim();
						if(tagname1.equals("dpId"))
						{
							/*dpidHold.addElement(parser.nextText());
							Vector vx = dpidHold;*/
							String dpId = parser.nextText();
							LOG.print("dpId : "+dpId);
							currentMessage.setDpId(dpId);
							LOG.print("Value Stored");
						}
						parser.require(XmlPullParser.END_TAG, null, tagname1);
					}
					//parser.require(XmlPullParser.END_TAG, null, "dpId_data");
					//newsdata.addElement(currentMessage.copy());
				}
				else if(tagname.equals("URL"))
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
				{LOG.print(" page ");
					currentMessageMod.put("page",parser.nextText());
				}
				else if(tagname.equals("userAgent"))
				{LOG.print(" userAgent ");
					currentMessageMod.put("userAgent",parser.nextText());
				}
				else if(tagname.equals("order_id"))
				{LOG.print(" order_id ");
					currentMessageMod.put("order_id",parser.nextText());
				}
				else if(tagname.equals("company_code"))
				{LOG.print(" company_code ");
					currentMessageMod.put("company_code",parser.nextText());
				}
				else if(tagname.equals("company_name"))
				{LOG.print(" company_name ");
					currentMessageMod.put("company_name",parser.nextText());
				}
				else if(tagname.equals("ltp"))
				{LOG.print(" ltp ");
					currentMessageMod.put("ltp",parser.nextText());
				}
				else if(tagname.equals("per_change"))
				{LOG.print(" per_change ");
					currentMessageMod.put("per_change",parser.nextText());
				}
				else if(tagname.equals("change"))
				{LOG.print(" change ");
					currentMessageMod.put("change",parser.nextText());
				}
				else if(tagname.equals("order_qty"))
				{LOG.print(" order_qty ");
					currentMessageMod.put("order_qty",parser.nextText());
				}
				else if(tagname.equals("order_price"))
				{LOG.print(" order_price ");
				currentMessageMod.put("order_price",parser.nextText());
				}
				else if(tagname.equals("exchange"))
				{LOG.print(" exchange ");
					currentMessageMod.put("exchange",parser.nextText());
				}
				else if(tagname.equals("dpId"))
				{LOG.print(" dpId ");
					currentMessageMod.put("dpId",parser.nextText());
				}
				else if(tagname.equals("action"))
				{LOG.print(" action ");
				if(!currentMessageMod.containsKey("action"))
					currentMessageMod.put("action",parser.nextText());
				else
					parser.nextText();
				}
				else if(tagname.equals("seldpId"))
				{LOG.print(" seldpId ");
					currentMessageMod.put("seldpId",parser.nextText());
				}
				else if(tagname.equals("qty"))
				{LOG.print(" qty ");
					currentMessageMod.put("qty",parser.nextText());
				}
				else if(tagname.equals("openqty"))
				{LOG.print(" openqty ");
					currentMessageMod.put("openqty",parser.nextText());
				}
				else if(tagname.equals("disc_qty"))
				{LOG.print(" disc_qty ");
					currentMessageMod.put("disc_qty",parser.nextText());
				}
				else if(tagname.equals("stopPrice"))
				{LOG.print(" stopPrice ");
					currentMessageMod.put("stopPrice",parser.nextText());
				}
				else if(tagname.equalsIgnoreCase("orderType"))
				{LOG.print(" orderType ");
					currentMessageMod.put("orderType",parser.nextText());
				}
				else if(tagname.equals("type"))
				{LOG.print(" type ");
					currentMessageMod.put("type",parser.nextText());
				}
				else if(tagname.equals("limitPrice"))
				{LOG.print(" limitPrice ");
					currentMessageMod.put("limitPrice", parser.nextText());
				}
				else if(tagname.equals("custId"))
				{LOG.print(" custId ");
					currentMessageMod.put("custId",parser.nextText());
				}
				else if(tagname.equals("orderPrice"))
				{LOG.print(" orderPrice ");
					currentMessageMod.put("orderPrice",parser.nextText());
				}
				else if(tagname.equals("flag"))
				{LOG.print(" flag ");
					currentMessageMod.put("flag",parser.nextText());
				}
				else if(tagname.equals("orderstatus"))
				{LOG.print(" orderstatus ");
					currentMessageMod.put("orderstatus",parser.nextText());
				}
				else if(tagname.equals("requeststatus"))
				{LOG.print(" requeststatus ");
					currentMessageMod.put("requeststatus",parser.nextText());
				}
				else if(tagname.equals("rmscode"))
				{LOG.print(" rmscode ");
					currentMessageMod.put("rmscode",parser.nextText());
				}
				else if(tagname.equals("userType"))
				{LOG.print(" userType ");
					currentMessageMod.put("userType",parser.nextText());
				}
				else if(tagname.equals("STATUS"))
				{LOG.print(" STATUS ");
					currentMessageMod.put("STATUS",parser.nextText());
				}
				else if(tagname.equals("MSG"))
				{LOG.print(" MSG ");
					currentMessageMod.put("MSG",parser.nextText());
				}
				else if(tagname.equals("title"))
				{LOG.print(" title ");
					currentMessageMod.put("title",parser.nextText());
				}
				else if(tagname.equals("flag"))
				{LOG.print(" flag ");
					currentMessageMod.put("flag",parser.nextText());
				}
				else if(tagname.equals("instrument"))
				{LOG.print(" instrument ");
					currentMessageMod.put("instrument",parser.nextText());
				}
				else if(tagname.equals("expiry"))
				{LOG.print(" expiry ");
					currentMessageMod.put("expiry",parser.nextText());
				}
				else if(tagname.equals("optType"))
				{LOG.print(" optType ");
					currentMessageMod.put("optType",parser.nextText());
				}
				else if(tagname.equals("strikePrice"))
				{LOG.print(" strikePrice ");
					currentMessageMod.put("strikePrice",parser.nextText());
				}
				else if(tagname.equals("requeststatus"))
				{LOG.print(" requeststatus ");
					currentMessageMod.put("requeststatus",parser.nextText());
				}
				else if(tagname.equals("rmscode"))
				{LOG.print(" rmscode ");
					currentMessageMod.put("rmscode",parser.nextText());
				}
				else if(tagname.equals("validity"))
				{LOG.print(" validity ");
					currentMessageMod.put("validity",parser.nextText());
				}
				else
				{LOG.print(tagname);
					currentMessageMod.put(tagname,parser.nextText());
				}
					//parser.nextText();
				parser.require(XmlPullParser.END_TAG, null, tagname);
			}
			parser.require(XmlPullParser.END_TAG, null, "root");
		}
		catch(Exception ex)
		{
			//ex.printStackTrace();
		}
		tradedata.addElement(currentMessage.copy());
		tradedata.addElement(currentMessageMod);
		
		
		if(WaitScreen.HTTPCALL)
			screen.setData(tradedata,id);
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