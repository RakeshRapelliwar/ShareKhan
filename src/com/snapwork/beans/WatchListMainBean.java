package com.snapwork.beans;

public class WatchListMainBean {
	private String displayName;
	private String templateName;
	private String exchange;
	private String type;
	public WatchListMainBean()
	{
		this.displayName = "";
		this.templateName = "";
		this.exchange = "";
		this.type = "";
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public WatchListMainBean copy()
	{
		WatchListMainBean copy = new WatchListMainBean();
		copy.displayName = displayName;
		copy.templateName = templateName;
		copy.exchange = exchange;
		copy.type = type;
		return copy;
	}
	

}
