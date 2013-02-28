package com.snapwork.beans;

public class ForexList {
	
	private String lastUpdated;
	private String forexName;
	private String rate;
	private String change;
	private String changePer;
	
	
	
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getForexName() {
		return forexName;
	}
	public void setForexName(String forexName) {
		this.forexName = forexName;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}
	public String getChangePer() {
		return changePer;
	}
	public void setChangePer(String changePer) {
		this.changePer = changePer;
	}
	
	
	public ForexList copy(){
		
		ForexList copy = new ForexList();
		
		copy.change = change;
		copy.changePer=changePer;
		copy.forexName=forexName;
		copy.lastUpdated=lastUpdated;
		copy.rate=rate;
		
		return copy;
	}

}
