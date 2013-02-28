package com.snapwork.parsers;

import java.util.Hashtable;
import java.util.Vector;
import javax.microedition.lcdui.Image;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.WatchListJson;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;

public class WatchListJsonParser implements HttpResponse
{
	public String feedurl = "";
	public Vector homeData = null;
	final WatchListJson hJ = new WatchListJson();
	Screen screen = null;
	ThreadedComponents threadedComponents = null;
	private boolean flag;
	public WatchListJsonParser(String url,Screen screen,ThreadedComponents threadedComponents)
	{
	this.feedurl = url;
		homeData = new Vector();
		this.screen = screen;
		this.threadedComponents = threadedComponents;
	}
	
	public WatchListJsonParser(boolean flag)
	{
		 homeData = new Vector();
		this.flag = flag;
	}
	
	public void getScreenData()
	{
		HttpProcess.threadedHttpsMD5Connection(feedurl, this);
	}
	
	public void setResponse( String rsponse)
	{
		if(rsponse == null)
			return;
		LOG.print(rsponse);
		Json js = new Json(rsponse);
		LOG.print("Response Json data size "+js.getdata.size());
		for(int i=0;i<js.getdata.size();i++)
		{
			Hashtable ht = (Hashtable) js.getdata.elementAt(i);
			hJ.setCode((String)ht.get("code"));
			hJ.setSymbol((String)ht.get("symbol"));
			homeData.addElement(hJ.copy());
	}
		if(!this.flag)
			screen.setData(homeData,threadedComponents);	
	}
	
	
	public Vector getResponse(String rsponse)
	{
		LOG.print(rsponse);
		Json js = new Json(rsponse);
		LOG.print("Response Json data size "+js.getdata.size());
		for(int i=0;i<js.getdata.size();i++)
		{
			Hashtable ht = (Hashtable) js.getdata.elementAt(i);
			hJ.setCode((String)ht.get("code"));
			hJ.setSymbol((String)ht.get("symbol"));
			homeData.addElement(hJ.copy());
	}
		return homeData;	
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