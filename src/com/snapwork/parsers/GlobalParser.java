package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import com.snapwork.beans.CrudeOil;
import com.snapwork.beans.ForexList;
import com.snapwork.beans.Futures;
import com.snapwork.beans.Market;
import com.snapwork.beans.MarketList;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;

public class GlobalParser implements HttpResponse
{
	public String feedurl = "";
	public Vector marketListData = null;
	public Vector marketData = null;
	public Vector crudeData = null;
	public Vector forexData = null;
	public Vector futuresData = null;
	public Vector marketDataList = null;
	public Vector data = null;
	public Vector asiaData = null;
	public Vector americaData =  null;
	public Vector europeData  = null;
	final MarketList marketList = new MarketList();
	final Market market= new Market();
	final CrudeOil crudeoil = new CrudeOil();
	final Futures futures = new Futures();
	final ForexList forexlist = new ForexList();
	private ThreadedComponents threadedComponets = null;
	public int i=0;
	Screen screen = null;
	ReturnDataWithId rparse = null;
	public GlobalParser(String url,Screen screen,ThreadedComponents threadedComponents)
	{
		this.feedurl = url;
		asiaData = new Vector();
		europeData = new Vector();
		americaData = new Vector();
		marketDataList = new Vector();
		marketData = new Vector();
		crudeData = new Vector();
		forexData = new Vector();
		futuresData = new Vector();
		data = new Vector();
		this.screen = screen;
		this.threadedComponets = threadedComponents;
	}
	
	public GlobalParser(String url, ReturnDataWithId rparse)
	{
		this.feedurl = url;
		asiaData = new Vector();
		europeData = new Vector();
		americaData = new Vector();
		marketDataList = new Vector();
		marketData = new Vector();
		crudeData = new Vector();
		forexData = new Vector();
		futuresData = new Vector();
		data = new Vector();
		this.rparse = rparse;
	}
	public void getScreenData()
	{
		HttpProcess.threadedHttpConnection(this.feedurl, this);
	}
	public void setResponse( String rsponse)
	{
		KXmlParser parser = null;
		ByteArrayInputStream byteArrayInputStream = null;
		InputStreamReader is = null;
		byte[] currentXMLBytes;
		try
		{
			int i=0;
			currentXMLBytes = rsponse.getBytes();
			byteArrayInputStream = new ByteArrayInputStream(currentXMLBytes);
			is = new InputStreamReader(byteArrayInputStream);
			parser = new KXmlParser();
			parser.setInput(is);
			parser.nextTag();
			parser.require(XmlPullParser.START_TAG, null, "Application");
			while(parser.nextTag() != XmlPullParser.END_TAG) 
			{ 
				parser.require(XmlPullParser.START_TAG, null, null);
				String tagnamex = parser.getName().trim();
				if(tagnamex.equals("Market"))
				{
					while(parser.nextTag() != XmlPullParser.END_TAG) 
					{
						parser.require(XmlPullParser.START_TAG, null, null);
						String tagnameMain = parser.getName().trim();
						if(tagnameMain.equals("MarketList"))
						{
							while(parser.nextTag() != XmlPullParser.END_TAG)
							{
								parser.require(XmlPullParser.START_TAG, null, null);
								String tagname = parser.getName().trim();
								if(tagname.equals("MarketName"))
								{
									market.setMarketName(parser.nextText());
									//parser.nextText();
								}
								else if(tagname.equals("Lastupdated"))
								{
									parser.nextText();
								}
								else
								{
									if(tagname.equals("Exchange"))
									{
										while(parser.nextTag() != XmlPullParser.END_TAG)
										{
											parser.require(XmlPullParser.START_TAG, null, null);
											String tagname1 = parser.getName().trim();
											if(tagname1.equals("ExchangeName"))
											{
												market.setExchangeName(parser.nextText());
											}
											else if(tagname1.equals("LastTradedPrice"))
											{
												market.setLastTradedPrice(parser.nextText());
											}
											else if(tagname1.equals("ChangePts"))
											{
												market.setChangePts(parser.nextText());
											}
											else if(tagname1.equals("ChangePtsPerc"))
											{
												market.setChangePtsPer(parser.nextText());
											}
											else if(tagname1.equals("FeedTime"))
											{
												parser.nextText();
											}
											else if(tagname1.equals("Country"))
											{
												parser.nextText();
											}
											parser.require(XmlPullParser.END_TAG, null, tagname1);
										}
										parser.require(XmlPullParser.END_TAG, null, "Exchange");
										if(i==0)
										{
											asiaData.addElement(market.copy());
										}
										else if(i==1)
										{
											europeData.addElement(market.copy());
										}
										else if(i==2)
										{
											americaData.addElement(market.copy());
										}
									}
								}
								parser.require(XmlPullParser.END_TAG, null, tagname);
							}
							parser.require(XmlPullParser.END_TAG, null, "MarketList");
							i++;
						}
						parser.require(XmlPullParser.END_TAG, null, tagnameMain);
					}
					parser.require(XmlPullParser.END_TAG, null, "Market");
				}
				if(tagnamex.equals("CrudeOil"))
				{
					while(parser.nextTag() != XmlPullParser.END_TAG) 
					{ 
						parser.require(XmlPullParser.START_TAG, null, null);
						String tagnameMain = parser.getName().trim();
						if(tagnameMain.equals("CrudeVal"))
						{
							crudeoil.setCrudeVal(parser.nextText());

						}
						else if(tagnameMain.equals("CrudeChange"))
						{
							crudeoil.setCrudeChange(parser.nextText());

						}
						parser.require(XmlPullParser.END_TAG, null, tagnameMain);
					}
					parser.require(XmlPullParser.END_TAG, null, "CrudeOil");	
					crudeData.addElement(crudeoil.copy());
				}
				if(tagnamex.equals("Futures"))
				{
					while(parser.nextTag() != XmlPullParser.END_TAG) 
					{
						parser.require(XmlPullParser.START_TAG, null, null);
						String tagnameMain = parser.getName().trim();
						if(tagnameMain.equals("Lastupdated"))
						{
							parser.nextText();
						}
						else
						{
							if(tagnameMain.equals("Exchange"))
							{
								while(parser.nextTag() != XmlPullParser.END_TAG)
								{
									parser.require(XmlPullParser.START_TAG, null, null);
									String tagname1 = parser.getName().trim();
									if(tagname1.equals("ExchangeName"))
									{
										futures.setExchangeName(parser.nextText());
									}
									else if(tagname1.equals("LastTradedPrice"))
									{
										futures.setLastTradedPrice(parser.nextText());
									}
									else if(tagname1.equals("ChangePts"))
									{
										futures.setChangePts(parser.nextText());
									}
									parser.require(XmlPullParser.END_TAG, null, tagname1);
								}
								parser.require(XmlPullParser.END_TAG, null, "Exchange");	
								futuresData.addElement(futures.copy());
							}
						}
						parser.require(XmlPullParser.END_TAG, null, tagnameMain);
					}
					parser.require(XmlPullParser.END_TAG, null, "Futures");	
				}
				if(tagnamex.equals("ForexList"))
				{
					while(parser.nextTag() != XmlPullParser.END_TAG) 
					{ 
						parser.require(XmlPullParser.START_TAG, null, null);
						String tagnameMain = parser.getName().trim();
						if(tagnameMain.equals("Lastupdated"))
						{
							parser.nextText();
						}
						else
						{
							if(tagnameMain.equals("Forex"))
							{
								while(parser.nextTag() != XmlPullParser.END_TAG)
								{
									parser.require(XmlPullParser.START_TAG, null, null);
									String tagname1 = parser.getName().trim();
									if(tagname1.equals("ForexName"))
									{
										forexlist.setForexName(parser.nextText());
									}
									else if(tagname1.equals("Rate"))
									{
										forexlist.setRate(parser.nextText());
									}
									else if(tagname1.equals("Change"))
									{
										forexlist.setChange(parser.nextText());
									}
									else if(tagname1.equals("Changeper"))
									{
										forexlist.setChangePer(parser.nextText());
									}
									parser.require(XmlPullParser.END_TAG, null, tagname1);
								}
								parser.require(XmlPullParser.END_TAG, null, "Forex");
							}
							forexData.addElement(forexlist.copy());
						}
						parser.require(XmlPullParser.END_TAG, null, tagnameMain);
					}
					parser.require(XmlPullParser.END_TAG, null, "ForexList");
				}
				parser.require(XmlPullParser.END_TAG, null, tagnamex);
			}
			parser.require(XmlPullParser.END_TAG, null, "Application");	
			data.addElement(asiaData);
			data.addElement(europeData);
			data.addElement(americaData);
			data.addElement(crudeData);
			data.addElement(futuresData);
			data.addElement(forexData);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		if(screen!=null)
			screen.setData(data,threadedComponets);
		else
			rparse.setData(data,1);
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