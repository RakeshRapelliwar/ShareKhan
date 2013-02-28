package com.snapwork.parsers;

import java.util.Vector;
import javax.microedition.lcdui.Image;
import com.snapwork.components.Screen;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;

public class RegistrationParser implements HttpResponse
{
	public String feedurl = "";
	public Vector data = null;
	Screen screen = null;
	public RegistrationParser(String url,Screen screen)
	{
		this.feedurl = url;
		data = new Vector();
		this.screen = screen;
	}
	public void getScreenData()
	{
		HttpProcess.threadedHttpConnection(feedurl, this);
	}
	public void setResponse( String rsponse)
	{
		data.addElement(rsponse);
		screen.setData(data,null);	
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