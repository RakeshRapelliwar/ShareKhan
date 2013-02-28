package com.snapwork.parsers;

import javax.microedition.lcdui.Image;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.snapwork.interfaces.ReturnString;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;

public class ReturnStringParser implements HttpResponse
{
	public String feedurl = "";
	private ReturnString screen = null;
	private int id;
	public ReturnStringParser(final String url,int id,ReturnString screen)
	{
		LOG.print("ReturnString constructor ");
		
		
		
		
		this.feedurl = url;
		this.screen = screen;
		this.id = id;
		getScreenData();
	}
	
	public ReturnStringParser(String url,int id,ReturnString screen,boolean flag)
	{
		LOG.print("ReturnString constructor ");
		this.feedurl = url;
		this.screen = screen;
		this.id = id;
		if(flag)
			getScreenMD5Data();
		else
			getScreenData();
	}
	public void getScreenData()
	{
		HttpProcess.threadedHttpConnection(feedurl, this);
	}
	
	public void getScreenMD5Data()
	{
		HttpProcess.threadedHttpsMD5Connection(feedurl, this);
	}
	
	public void httpKill()
	{
		screen = null;
	}
	
	public void setResponse( String rsponse)
	{
		LOG.print("ReturnString response "+rsponse);
		if(rsponse == null)
			return;
		if(screen != null)
			screen.setReturnString(rsponse,id);	
	}

	public void exception(Exception ex) {
		// TODO Auto-generated method stub
		
	}

	public void setResponse(Image img) {
		// TODO Auto-generated method stub
		
	}

	public void setResponse(Image image, int id) {
		// TODO Auto-generated method stub
		
	}

	public void setResponse(Image image, String name) {
		// TODO Auto-generated method stub
		
	}
}