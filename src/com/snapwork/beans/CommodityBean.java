package com.snapwork.beans;

public class CommodityBean {
	
	private String Token="";
	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getScripName() {
		return ScripName;
	}

	public void setScripName(String string) {
		ScripName = string;
	}

	public String getExpiryDate() {
		return ExpiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		ExpiryDate = expiryDate;
	}

	public String getSymbol() {
		return Symbol;
	}

	public void setSymbol(String symbol) {
		Symbol = symbol;
	}

	public String getExchange() {
		return Exchange;
	}

	public void setExchange(String exchange) {
		Exchange = exchange;
	}

	private String ScripName = "";
	private String ExpiryDate="";
	private String Symbol="";
	
	private String Exchange="";
	
	
	
	
	public CommodityBean copy() {

		CommodityBean bean = new CommodityBean();
		bean.setToken(getToken());
		bean.setScripName(getScripName());
		bean.setExpiryDate(getExpiryDate());
		bean.setSymbol(getSymbol());
		bean.setExchange(getExchange());
		return bean;
	}
	

}
