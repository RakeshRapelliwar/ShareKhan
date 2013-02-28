package com.snapwork.beans;

public class FNOSearchBean {
	private String code="";
	private int expiry = -1;
	private String option="";
	private String strike="";	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getExpiry() {
		return expiry;
	}
	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getStrike() {
		return strike;
	}
	public void setStrike(String strike) {
		this.strike = strike;
	}
	
	public FNOSearchBean copy() {

		FNOSearchBean bean = new FNOSearchBean();
		bean.setCode(getCode());
		bean.setExpiry(getExpiry());
		bean.setOption(getOption());
		bean.setStrike(getStrike());
		return bean;
	}

}
