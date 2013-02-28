package com.snapwork.beans;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

import javax.microedition.lcdui.Image;

import net.rim.device.api.smartcard.ResponseAPDU;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import com.snapwork.components.Screen;
import com.snapwork.interfaces.ReturnString;
import com.snapwork.parsers.Json;
import com.snapwork.util.AppConstants;
import com.snapwork.util.HTTPGetConnection;
import com.snapwork.util.HttpProcess;
import com.snapwork.util.HttpResponse;
import com.snapwork.util.LOG;

public class Expiry implements  HttpResponse{
	private static Hashtable foe =  new Hashtable();
	private static Hashtable foeText =  new Hashtable();
	private ReturnString screen;
	private int id;
	public Expiry(ReturnString screen,int id)
	{
		this.screen = screen;
		this.id = id;
		getData();
	}
	public void getData()
	{
		if(foe.size() == 0)
		{
			LOG.print(AppConstants.getFNODATEURL);
			HttpProcess.threadedHttpConnection(AppConstants.getFNODATEURL, this);
		}
		else
		{
			Calendar cl = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
			cl.setTime(new Date());
			int dom = cl.get(Calendar.DAY_OF_MONTH);
			int month = cl.get(Calendar.MONTH);
			String date = (String) foe.get(new Integer(0));
			int domc = Integer.parseInt(date.substring(0, 2));
			int monthc = Integer.parseInt(date.substring(3, 5));
			if(dom>domc && month == monthc)
			{
				HttpProcess.threadedHttpConnection(AppConstants.getFNODATEURL, this);
			}
			else if(month != monthc)
			{
				HttpProcess.threadedHttpConnection(AppConstants.getFNODATEURL, this);
			}
			else
			{
				screen.setReturnString("SUCCESS", id);
			}
		}
	}
	/*private static void scrips()
	{
		fo.clear();
		//fo.put(new Integer(7),"28-07-2011");
		//fo.put(new Integer(8),"25-08-2011");
		//fo.put(new Integer(9),"29-09-2011");
		//fo.put(new Integer(10),"25-10-2011");
		fo.put(new Integer(111),"24-11-2011");
		fo.put(new Integer(112),"29-12-2011");
		fo.put(new Integer(1),"25-01-2012");
		fo.put(new Integer(2),"23-02-2012");
		fo.put(new Integer(3),"29-03-2012");
		fo.put(new Integer(4),"26-04-2012");
		fo.put(new Integer(5),"31-05-2012");
		fo.put(new Integer(6),"28-06-2012");
		fo.put(new Integer(7),"26-07-2012");
		fo.put(new Integer(8),"30-08-2012");
		fo.put(new Integer(12),"27-12-2012");
		flag = false;
		foe.clear();
		//foe.put(new Integer(7),"28-Jul");
		//foe.put(new Integer(8),"25-Aug");
		//foe.put(new Integer(9),"29-Sep");
		//foe.put(new Integer(10),"25-Oct");
		foe.put(new Integer(111),"24-Nov");
		foe.put(new Integer(112),"29-Dec");
		foe.put(new Integer(1),"25-Jan");
		foe.put(new Integer(2),"23-Feb");
		foe.put(new Integer(3),"29-Mar");
		foe.put(new Integer(4),"26-Apr");
		foe.put(new Integer(5),"31-May");
		foe.put(new Integer(6),"28-Jun");
		foe.put(new Integer(7),"26-Jul");
		foe.put(new Integer(8),"30-Aug");
		foe.put(new Integer(12),"27-Dec");
	}
	*/
	public static String getValue(int id)
	{ 
		return (String) foe.get(new Integer(id));
	}
	public static String getText(int id)
	{ 
		return (String) foeText.get(new Integer(id));
	}
	
	public static String getText(String string)
	{ 
		int startx = 0;
		for(int i = 0; i<string.length();i++)
		{
			if(string.charAt(i)=='-')
			{
				if(startx == 0)
				{
					startx = i;
				}
				else 
				{
					//nw = string.substring(0, startx+1) + Integer.parseInt(string.substring(startx+1, i));
					return dateDec(string.substring(0, startx),string.substring(startx+1, i));
				}
			}
		}
		return string;
	}
	public static String getTextWithYear(int id)
	{ 
		return getText(id)+getValue(id).substring(5, getValue(id).length());
	}
	public static int size()
	{ 
		return foeText.size();
	}
	public void putHashtable(Hashtable hash, Hashtable text)
	{ 
		foe.clear();
		foe = hash;
		foeText = text;
		LOG.print("foe Date Size : "+ foe.size());
		screen.setReturnString("SUCCESS", id);
	}
	public void setResponse(String rsponse) {
		if(rsponse == null)
			return;
		else if(rsponse.length() == 0)
			return;
		else if(rsponse.equals("-2"))
		{
		UiApplication.getUiApplication().invokeAndWait(new Runnable() {
								public void run() {
									Dialog.alert("No Network Connection!");  
								}
							});
			return;
		}
		LOG.print(rsponse);
		Json js = new Json(rsponse);
		String value="";
		Hashtable hash = new Hashtable();
		Hashtable text = new Hashtable();
		LOG.print("js.getdata.size() "+js.getdata.size());
		String last = "";
		int j = 0;
		for(int i=0;i<js.getdata.size();i++)
		{
			Hashtable ht = (Hashtable) js.getdata.elementAt(i);
			value = (String)ht.get("Date");
			String ne = value.substring(2, value.length());
			if(ne.equals(last))
			{
				--j;
				hash.put(new Integer(j),value);
				text.put(new Integer(j),dateDec(value.substring(0, 2),value.substring(3, 5)));
			}
			else
			{
				hash.put(new Integer(j),value);
				text.put(new Integer(j),dateDec(value.substring(0, 2),value.substring(3, 5)));
			}
			j++;
			last = ne;
				
			if(i==js.getdata.size()-1)
				putHashtable(hash, text);
		}
		
	}
public static String dateDec(String first, String month)
{
	int m = Integer.parseInt(month);
	switch(m)
	{
		case 1: month = "Jan"; break;
		case 2: month = "Feb"; break;
		case 3: month = "Mar"; break;
		case 4: month = "Apr"; break;
		case 5: month = "May"; break;
		case 6: month = "Jun"; break;
		case 7: month = "Jul"; break;
		case 8: month = "Aug"; break;
		case 9: month = "Sep"; break;
		case 10: month = "Oct"; break;
		case 11: month = "Nov"; break;
		case 12: month = "Dec"; break;
	}
	return first+"-"+month;
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