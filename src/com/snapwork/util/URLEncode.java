package com.snapwork.util;

public class URLEncode {

	public static String getString(String textUrl)
	{
		if(textUrl.indexOf("&")>-1)
		{
			textUrl = textUrl.substring(0, textUrl.indexOf("&")) + "%26" +textUrl.substring(textUrl.indexOf("&")+1,textUrl.length());
		
		}
		if(textUrl.indexOf("-")>-1)
		{
			textUrl = textUrl.substring(0, textUrl.indexOf("-")) + "%2D" +textUrl.substring(textUrl.indexOf("-")+1,textUrl.length());	
		}
		
		return textUrl;
	}

	public static String replace(String code) {
		StringBuffer sbf = new StringBuffer(code);
		int i = 0;
		while(i < sbf.length()-1)
		{
			if( sbf.charAt(i) == '&' )
			{	
				sbf.deleteCharAt(i);
				sbf.insert(i, '-');
			}
			i++;
		}
		// TODO Auto-generated method stub
		return sbf.toString();
	}
	public static String replace26(String code) {
		StringBuffer sbf = new StringBuffer(code);
		int i = 0;
		while(i < sbf.length()-1)
		{
			if( sbf.charAt(i) == '&' )
			{	
				sbf.deleteCharAt(i);
				sbf.insert(i, "%26");
			}
			i++;
		}
		// TODO Auto-generated method stub
		return sbf.toString();
	}
}
