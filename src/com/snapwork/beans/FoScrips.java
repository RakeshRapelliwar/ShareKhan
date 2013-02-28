package com.snapwork.beans;

import java.util.Enumeration;
import java.util.Hashtable;

import com.snapwork.util.LOG;

public class FoScrips {
	private static Hashtable fno =  new Hashtable(){};
	public static String getValue(String id)
	{	
	String fovalue = (String) fno.get(id);
	LOG.print("FNO "+id);
	if(fovalue == null)
	{
		Enumeration e = fno.keys();
	       
        while( e. hasMoreElements() ){       
        	String s = (String)fno.get(e.nextElement().toString());
        	LOG.print("- "+s);
        	if(id.equalsIgnoreCase(s))
        	{
        		fovalue = s;
        		break;
        	}
        	
        }
	}
	if(fovalue == null)
		fovalue = id;
		return fovalue;
	}
	
	public static String getValueForCompany(String id)
	{	
		String fovalue = (String) fno.get(id);
		LOG.print("FNO "+id);
		if(fovalue == null)
		{
			Enumeration e = fno.keys();
		       
	        while( e. hasMoreElements() ){       
	        	String s = (String)fno.get(e.nextElement().toString());
	        	LOG.print("- "+s);
	        	if(id.equalsIgnoreCase(s))
	        	{
	        		fovalue = s;
	        		break;
	        	}
	        	
	        }
		}
		
		/*if(fovalue == null) {
			fovalue = id;
		}*/
		
		return fovalue;
	}
	
	
	public static boolean isFound(String id)
	{	
	String fovalue = (String) fno.get(id);
	//LOG.print("FNO "+id);
	if(fovalue == null)
	{
		Enumeration e = fno.keys();
	       
        while( e. hasMoreElements() ){       
        	String s = (String)fno.get(e.nextElement().toString());
        	//LOG.print("- "+s);
        	if(id.equalsIgnoreCase(s))
        	{
        		fovalue = s;
        		break;
        	}
        	
        }
	}
	if(fovalue == null)
		return false;
	else if(fovalue.length() == 0)
		return false;
		return true;
	}
	
	public static Hashtable getHashtable()
	{
		return fno;
	}
	
	public static void putHashTable(Hashtable ht)
	{
		fno = ht;
	}
}
