package com.snapwork.beans;

public class Futures {
	
	private String lastUpdated;
	private String exchangeName;
	private String lastTradedPrice;
	private String changePts;
	
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getExchangeName() {
		return exchangeName;
	}
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
	public String getLastTradedPrice() {
		return lastTradedPrice;
	}
	public void setLastTradedPrice(String lastTradedPrice) {
		this.lastTradedPrice = lastTradedPrice;
	}
	public String getChangePts() {
		return changePts;
	}
	public void setChangePts(String changePts) {
		this.changePts = changePts;
	}
	
	
	public Futures copy(){
		
		Futures copy = new Futures();
		
		copy.changePts=changePts;
		copy.exchangeName=exchangeName;
		copy.lastTradedPrice=lastTradedPrice;
		copy.lastUpdated=lastUpdated;
		
		return copy;
	}
	
	
	

}
