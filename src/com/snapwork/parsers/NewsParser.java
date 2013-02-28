
package com.snapwork.parsers;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import com.snapwork.beans.News;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;

public class NewsParser implements HttpResponse
{
	public String feedurl = "";
	public Vector newsdata = null;
	NewsParser newpar;
	final News currentMessage = new News();
	private Screen screen = null;
	private ThreadedComponents threadedComponets = null;
	private String pageNo = null;

	public NewsParser(String url,Screen screen,String pageNo)
	{
		this.feedurl = url;
		newsdata = new Vector();
		this.screen = screen;
		this.pageNo = pageNo;
	}

	public NewsParser(String url,Screen screen,ThreadedComponents threadedComponets)
	{
		this.feedurl = url;
		newsdata = new Vector();
		this.screen = screen;
		this.threadedComponets = threadedComponets;
	} 

	public void getScreenData()
	{
		HttpProcess.threadedHttpConnection(feedurl, this);
	}

	public void setResponse(String rsponse)
	{
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
			parser.require(XmlPullParser.START_TAG, null, "G");
			while(parser.nextTag() != XmlPullParser.END_TAG)
			{
				parser.require(XmlPullParser.START_TAG, null, null);
				String tagname = parser.getName().trim();
				if(tagname.equals("I"))
				{
					while(parser.nextTag() != XmlPullParser.END_TAG)
					{
						parser.require(XmlPullParser.START_TAG, null, null);
						String tagname1 = parser.getName().trim();
						if(tagname1.equals("ID"))
						{
							currentMessage.setId(parser.nextText());
						}
						else if(tagname1.equals("T"))
						{
							currentMessage.setTitle(parser.nextText());
						}
						else if(tagname1.equals("U"))
						{
							currentMessage.setUrl(parser.nextText());
						}
						else if(tagname1.equals("TN"))
						{
							currentMessage.setThumbnail(parser.nextText());
						}
						else if(tagname1.equals("TN2"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("DU"))
						{
							currentMessage.setLandingUrl(parser.nextText());
						}
						else if(tagname1.equals("CT"))
						{
							currentMessage.setContentURL(parser.nextText());
						}
						else if(tagname1.equals("C"))
						{
							currentMessage.setSource(parser.nextText());
						}
						else if(tagname1.equals("BURL"))
						{
							parser.nextText();
						}
						else if(tagname1.equals("DT"))
						{
							currentMessage.setTime(parser.nextText());
						}
						parser.require(XmlPullParser.END_TAG, null, tagname1);
					}
					parser.require(XmlPullParser.END_TAG, null, "I");
					newsdata.addElement(currentMessage.copy());
				}
				else
				{
					if(!tagname.equals("I"))
						parser.nextText();
					parser.require(XmlPullParser.END_TAG, null, tagname);
				}
			}
			parser.require(XmlPullParser.END_TAG, null, "G");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		if(pageNo!=null)
		{
			Vector vector = new Vector();
			vector.addElement(pageNo);
			vector.addElement(newsdata);
			screen.setData(vector,null);
		}
		else
		{
			screen.setData(newsdata,threadedComponets);
		}
	}
	public void getNewsData()
	{
		HttpProcess.threadedHttpConnection(feedurl, this);
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