package com.snapwork.util;

import javax.microedition.lcdui.Image;

public interface HttpResponse
{
	public void setResponse(String rsponse);
	public void exception(Exception ex);
	public void setResponse(Image img);
	public void setResponse(Image image, int id);
	public void setResponse(Image image, String name);
}
