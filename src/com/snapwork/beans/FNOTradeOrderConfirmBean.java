package com.snapwork.beans;

public class FNOTradeOrderConfirmBean {
	
	private String orderNumber;
	private String exchange;
	private String custId;
	private String dpId;
	private String errorMessage;
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getErrorMessage() {
		return errorMessage;
	}

	public FNOTradeOrderConfirmBean copy() {

		FNOTradeOrderConfirmBean targetBean = new FNOTradeOrderConfirmBean();
		
		targetBean.setOrderNumber(getOrderNumber());
		targetBean.setExchange(getExchange());
		targetBean.setCustId(getCustId());
		targetBean.setDpId(getDpId());
		targetBean.setErrorMessage(getErrorMessage());
		
		return targetBean;

	}
	
}
