package com.snapwork.beans;

import java.util.Hashtable;

public class ReportsOrderBean {	
	
	private Hashtable stringHolder ;
	
	public void setStringHolder(String key, String Value) {
		if(stringHolder == null)
			stringHolder = new Hashtable();
		stringHolder.put(key, Value);
	}
	public Hashtable getStringHolder() {
		if(stringHolder == null)
			return new Hashtable();
		return stringHolder;
	}
	
	public ReportsOrderBean copy(){
		ReportsOrderBean copy = new ReportsOrderBean();
		copy.stringHolder=stringHolder;
		return copy;
	}	
}
