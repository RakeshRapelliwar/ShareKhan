package com.snapwork.beans;

public class Market {	
	private String exchangeName;
	private String lastTradedPrice;
	private String changePts;
	private String changePtsPer;
	private String marketName;
	private String feedTime;
	private String country;	
	public String getMarketName() {
		return marketName;
	}


	public void setMarketName(String marketName) {
		this.marketName = marketName;
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


	public String getChangePtsPer() {
		return changePtsPer;
	}


	public void setChangePtsPer(String changePtsPer) {
		this.changePtsPer = changePtsPer;
	}


	public String getFeedTime() {
		return feedTime;
	}


	public void setFeedTime(String feedTime) {
		this.feedTime = feedTime;
	}


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public Market copy(){

		Market copy = new Market();
		copy.exchangeName=exchangeName;
		copy.lastTradedPrice=lastTradedPrice;
		copy.changePts=changePts;
		copy.changePtsPer=changePtsPer;
		copy.marketName=marketName;
		copy.feedTime =feedTime;
		copy.country = country;

		return copy;

	}


}
