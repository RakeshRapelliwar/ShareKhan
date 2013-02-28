package com.snapwork.beans;

import java.util.Vector;

public class FNOTradeBean {

	private String flag="";

	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getFlag() {
		return flag;
	}
	
	private String indexName="";

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getIndexName() {
		return indexName;
	}
	private String symbolName="";
	
	public String getSymbolName() {
		return symbolName;
	}
	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}

	private Vector expiryData;

	public void setExpiryData(Vector expiryData) {
		this.expiryData = expiryData;
	}
	public Vector getExpiryData() {
		return expiryData;
	}
	public void addExpiryData(String expiryDate) {
		if(expiryData==null) {
			expiryData = new Vector();
		}
		expiryData.addElement(expiryDate);
	}
	
	
	private Vector strikeData;
	public void setStrikeData(Vector strikeData) {
		this.strikeData = strikeData;
	}
	public Vector getStrikeData() {
		return strikeData;
	}
	public void addStrikeData(String strikeValue) {
		if(strikeData==null) {
			strikeData = new Vector();
		}
		strikeData.addElement(strikeValue);
	}

	private String minLot = null;
	public void setMinLot(String minLot) {
		this.minLot = minLot;
	}
	public String getMinLot() {
		return minLot;
	}

	public FNOTradeBean copy() {

		FNOTradeBean tradeBean = new FNOTradeBean();
		
		tradeBean.setIndexName(getIndexName());
		tradeBean.setExpiryData(getExpiryData());
		tradeBean.setStrikeData(getStrikeData());
		tradeBean.setMinLot(getMinLot());

		return tradeBean;
	}


	
	
}
