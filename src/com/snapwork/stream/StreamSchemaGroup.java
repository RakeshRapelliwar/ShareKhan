package com.snapwork.stream;

import java.util.Vector;

import com.snapwork.util.LOG;
import com.snapwork.util.URLEncode;
import com.snapwork.view.MyWatchList;

public class StreamSchemaGroup {
	public static int COMPANYCODE = 1;
	public static int SYMBOL = 2;
	public static int LTP = 3;
	public static int TIMESTAMP = 4;
	public static int PREVCLOSE = 5;
	public static int DAYHIGH = 6;
	public static int DAYLOW = 7;
	public static int PERCENT_CHANGE = 8;
	public static int CHANGE_VALUE = 9;
	public static int TOTAL = 9;
	
	public static String getSchema()
	{
		//return "symbol ltp timestamp";
		return "companycode symbol ltp timestamp prevclose dayhigh daylow percentchange changevalue";
	}
	public static String getHomeGroup()
	{
		return "item_13020033 item_12150008";
	}
	public static String getWatchListGroup()
	{
		MyWatchList.streamURL = URLEncode.replace(MyWatchList.streamURL);
		LOG.print(MyWatchList.streamURL);
		return MyWatchList.streamURL;
	}
	public static String getMultipleGroup(Vector vector)
	{
		String item = "item_";
		String returnString = "";
		for(int i=0; i<vector.size(); i++)
		{
			returnString = returnString + item + (String)vector.elementAt(i) + " ";
		}
		if(returnString.length()>0)
			returnString = returnString.substring(0, returnString.length()-1);
		return URLEncode.replace(returnString);
	}
}
