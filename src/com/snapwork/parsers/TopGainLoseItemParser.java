package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;

import com.snapwork.interfaces.ReturnData;
import com.snapwork.beans.TopGainLoseItem;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;

public class TopGainLoseItemParser implements HttpResponse
{
	public String feedurl = "";
	public Vector data = null;
	final TopGainLoseItem tgl= new TopGainLoseItem();
	private ThreadedComponents threadedComponents = null;
	Screen screen = null;
	private ReturnData refreshData;

	public TopGainLoseItemParser(String url,Screen screen,ThreadedComponents threadedComponents)
	{
		this.feedurl = url;
		data = new Vector();
		this.screen = screen;
		this.threadedComponents = threadedComponents;
	}
	
	public TopGainLoseItemParser(String url, ReturnData refreshData)
	{
		this.feedurl = url;
		data = new Vector();
		this.refreshData = refreshData;
	}

	public void getScreenData()
	{
		HttpProcess.threadedHttpConnection(feedurl, this);
	}

	public void setResponse( String rsponse)
	{
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
			parser.require(XmlPullParser.START_TAG, null, "Application");
			while(parser.nextTag() != XmlPullParser.END_TAG) 
			{ 
				parser.require(XmlPullParser.START_TAG, null, null);
				String tagnamex = parser.getName().trim();
				if(tagnamex.equals("Markets"))
				{
					while(parser.nextTag() != XmlPullParser.END_TAG) 
					{ 
						parser.require(XmlPullParser.START_TAG, null, null);
						String tagname2 = parser.getName().trim();
						if(tagname2.equals("TopGainersNSEList") || tagname2.equals("TopLosersNSEList"))
						{
							while(parser.nextTag() != XmlPullParser.END_TAG)
							{
								parser.require(XmlPullParser.START_TAG, null, null);
								String tagname = parser.getName().trim();
								if(tagname.equals("Company"))
								{
									while(parser.nextTag() != XmlPullParser.END_TAG)
									{
										parser.require(XmlPullParser.START_TAG, null, null);
										String tagnam = parser.getName().trim();
										if(tagnam.equals("CompanyCode"))
										{
											tgl.setCompanyCode(parser.nextText());
										}
										else if(tagnam.equals("ShortCompanyName"))
										{
											tgl.setShortCompanyName(parser.nextText());
										}
										else if(tagnam.equals("ShortCompanyURL"))
										{
											tgl.setShortCompanyURL(parser.nextText());
										}
										else if(tagnam.equals("Price"))
										{
											tgl.setPrice(parser.nextText());
										}
										else if(tagnam.equals("Change"))
										{
											tgl.setChange(parser.nextText());
										}
										else if(tagnam.equals("PercentageChange"))
										{
											tgl.setPercentageChange(parser.nextText());
										}
										else if(tagnam.equals("Symbol"))
										{
											tgl.setSymbol(parser.nextText());
										}
										parser.require(XmlPullParser.END_TAG, null, tagnam);
									}
									parser.require(XmlPullParser.END_TAG, null, "Company");	
									data.addElement(tgl.copy());
								}
							}
						}
					}
				}
			}	
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		if(refreshData!=null)
			refreshData.setData(data);
		else
			screen.setData(data,threadedComponents);	
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