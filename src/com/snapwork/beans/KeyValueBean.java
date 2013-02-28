package com.snapwork.beans;

public class KeyValueBean {
	
	private String key = null;
	private String value = null;
	
	/*public KeyValueBean(String key, String value)
	{
		this.key = key;
		this.value = value;
	}*/
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		if(value == null)
			value = " ";
		else if(value.length() == 0)
			value = " ";
		this.value = value;
	}
	
	public KeyValueBean copy(){
		
		KeyValueBean copy = new KeyValueBean();
		copy.key=key;
		copy.value=value;
		return copy;
	}
	
	
	

}
