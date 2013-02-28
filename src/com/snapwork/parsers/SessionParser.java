
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

public class SessionParser implements HttpResponse
{
	private ReturnDataWithId screen = null;
	private int id;
	private boolean sessionFlag = true;
	

	public SessionParser(String data,ReturnDataWithId screen, int id)
	{
		this.id = id;
		this.screen = screen;
	WaitScreen.HTTPCALL = true;
	sessionFlag = true;
	setResponse(data);
	}

	public void setResponse(String rsponse)
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
			parser.require(XmlPullParser.START_TAG, null, "root");
			while(parser.nextTag() != XmlPullParser.END_TAG)
			{
				parser.require(XmlPullParser.START_TAG, null, null);
				String tagname = parser.getName().trim();
				if(tagname.equals("response"))
				{LOG.print(" response ");
					String session = parser.nextText();
					if(session.equalsIgnoreCase("app://login"))
					{
						sessionFlag = false;
					}
				}
				else
					parser.nextText();
				parser.require(XmlPullParser.END_TAG, null, tagname);
			}
			parser.require(XmlPullParser.END_TAG, null, "root");
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		Vector v = new Vector();
		if(sessionFlag)
		v.addElement("TRUE");
		else
			v.addElement("FALSE");
		if(WaitScreen.HTTPCALL)
			screen.setData(v,id);
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