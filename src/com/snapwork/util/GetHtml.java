package com.snapwork.util;

import java.util.Vector;
import javax.microedition.lcdui.Image;
import com.snapwork.beans.News;
import com.snapwork.components.Screen;
import com.snapwork.components.ThreadedComponents;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;

public class GetHtml implements HttpResponse
{
	public String feedurl = "";
	public Vector newsdata = null;
	GetHtml newpar;
	final News currentMessage = new News();
	private Screen screen = null;
	private ThreadedComponents threadedComponets = null;
	private String pageNo = null;

	public GetHtml(String url,Screen screen,String pageNo)
	{
		this.feedurl = url;
		newsdata = new Vector();
		this.screen = screen;
		this.pageNo = pageNo;
	}

	public GetHtml(String url,Screen screen,ThreadedComponents threadedComponets)
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
		Vector vector = new Vector();
		vector.addElement(rsponse);
		if(pageNo!=null)
		{
			screen.setData(vector,null);
		}
		else
		{
			screen.setData(vector,threadedComponets);
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