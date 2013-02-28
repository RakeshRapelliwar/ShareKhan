package com.snapwork.beans;

public class TopGainLoseItem {
	
	private String companyCode;
	private String shortCompanyName;
	private String shortCompanyURL;
	private String price;
	private String change;
	private String percentageChange;
	private String symbol;
	

	public String getCompanyCode() {
		return companyCode;
	}


	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getSymbol() {
		return companyCode;
	}


	public void setSymbol(String symbol_) {
		symbol = symbol_;
	}
	
	public String getShortCompanyName() {
		return shortCompanyName;
	}


	public void setShortCompanyName(String shortCompanyName) {
		this.shortCompanyName = shortCompanyName;
	}


	public String getShortCompanyURL() {
		return shortCompanyURL;
	}


	public void setShortCompanyURL(String shortCompanyURL) {
		this.shortCompanyURL = shortCompanyURL;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public String getChange() {
		return change;
	}


	public void setChange(String change) {
		this.change = change;
	}


	public String getPercentageChange() {
		return percentageChange;
	}


	public void setPercentageChange(String percentageChange) {
		this.percentageChange = percentageChange;
	}


	public TopGainLoseItem copy(){
		TopGainLoseItem copy = new TopGainLoseItem();
		copy.companyCode = companyCode;
		copy.shortCompanyName = shortCompanyName;
		copy.shortCompanyURL = shortCompanyURL;
		copy.price = price;
		copy.change = change;
		copy.percentageChange = percentageChange;
		copy.symbol = symbol;
		return copy;
	}
	

}
