package com.snapwork.beans;

public class SearchBean {
	private String name;
	private String id;
	private String symbol;
	public SearchBean()
	{
		this.name = "";
		this.id = "";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	private String companyCode;
	private String religareCode;
	private String displayName1;
	private String displayName2;
	
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getReligareCode() {
		return religareCode;
	}
	public void setReligareCode(String religareCode) {
		this.religareCode = religareCode;
	}
	public String getDisplayName1() {
		return displayName1;
	}
	public void setDisplayName1(String displayName1) {
		this.displayName1 = displayName1;
	}
	public String getDisplayName2() {
		return displayName2;
	}
	public void setDisplayName2(String displayName2) {
		this.displayName2 = displayName2;
	}
	public SearchBean copy()
	{
		SearchBean copy = new SearchBean();
		copy.name = name;
		copy.id = id;
		copy.symbol = symbol;
		copy.companyCode = companyCode;
		copy.religareCode = religareCode;
		copy.displayName1 = displayName1;
		copy.displayName2 = displayName2;
		return copy;
	}
	

}
