package com.snapwork.beans;

public class FNOOrderConfirmationBean {

	private String symbolName;
	private String exchange;
	private String ltp;
	private String perChange;
	private String change;
	private String strikePrice;
	private String custId;
	private String type;
	private String discQty;
	private String dpId;
	private String mktLot;
	private String page;
	private String instType;
	private String expiry;
	private String optionType;
	private String action;
	private String qty;
	private String orderType;
	private String limitPrice;
	private String stopPrice;
	private String validity;
	
	private String scripCode;
	private String rmsCode;
	private HomeJson bannerData;
	private String bannerDate;
	
	private String orderNumber;
	private String eQ;
	private String ep;
	private String token;
	private String compCodeRefresh;
	private boolean freshOrder;
	
	public boolean isFreshOrder() {
		return freshOrder;
	}
	public void setFreshOrder(boolean freshOrder) {
		this.freshOrder = freshOrder;
	}
	public String getCompCodeRefresh() {
		return compCodeRefresh;
	}
	public void setCompCodeRefresh(String compCodeRefresh) {
		this.compCodeRefresh = compCodeRefresh;
	}
	public String getSymbolName() {
		return symbolName;
	}
	public void setSymbolName(String symbolName) {
		this.symbolName = symbolName;
	}
	public String getExchange() {
		return exchange;
	}
	public void setExchange(String exchange) {
		this.exchange = exchange;
	}
	public String getLtp() {
		return ltp;
	}
	public void setLtp(String ltp) {
		this.ltp = ltp;
	}
	public String getPerChange() {
		return perChange;
	}
	public void setPerChange(String perChange) {
		this.perChange = perChange;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}
	public String getStrikePrice() {
		return strikePrice;
	}
	public void setStrikePrice(String strikePrice) {
		this.strikePrice = strikePrice;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDiscQty() {
		return discQty;
	}
	public void setDiscQty(String discQty) {
		this.discQty = discQty;
	}
	public String getDpId() {
		return dpId;
	}
	public void setDpId(String dpId) {
		this.dpId = dpId;
	}
	public String getMktLot() {
		return mktLot;
	}
	public void setMktLot(String mktLot) {
		this.mktLot = mktLot;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getInstType() {
		return instType;
	}
	public void setInstType(String instType) {
		this.instType = instType;
	}
	public String getExpiry() {
		return expiry;
	}
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}
	public String getOptionType() {
		return optionType;
	}
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getLimitPrice() {
		return limitPrice;
	}
	public void setLimitPrice(String limitPrice) {
		this.limitPrice = limitPrice;
	}
	public String getStopPrice() {
		return stopPrice;
	}
	public void setStopPrice(String stopPrice) {
		this.stopPrice = stopPrice;
	}
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}

	public void setScripCode(String scripCode) {
		this.scripCode = scripCode;
	}
	public String getScripCode() {
		return scripCode;
	}
	public void setRmsCode(String rmsCode) {
		this.rmsCode = rmsCode;
	}
	public String getRmsCode() {
		return rmsCode;
	}
	
	public HomeJson getBannerData() {
		return bannerData;
	}
	public void setBannerData(HomeJson bannerData) {
		this.bannerData = bannerData;
	}
	
	
	public String getBannerDate() {
		return bannerDate;
	}
	public void setBannerDate(String bannerDate) {
		this.bannerDate = bannerDate;
	}
	
	
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getEQ() {
		return eQ;
	}
	public void setEQ(String eQ) {
		this.eQ = eQ;
	}
	public String getEP() {
		return ep;
	}
	public void setEP(String ep) {
		this.ep = ep;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public FNOOrderConfirmationBean copy() {

		FNOOrderConfirmationBean targetBean = new FNOOrderConfirmationBean();

		targetBean.setSymbolName(getSymbolName());
		targetBean.setExchange(getExchange());
		targetBean.setLtp(getLtp());
		targetBean.setPerChange(getPerChange());
		targetBean.setChange(getChange());
		targetBean.setStrikePrice(getStrikePrice());
		targetBean.setCustId(getCustId());
		targetBean.setType(getType());
		targetBean.setDiscQty(getDiscQty());
		targetBean.setDpId(getDpId());
		targetBean.setMktLot(getMktLot());
		targetBean.setPage(getPage());
		targetBean.setInstType(getInstType());
		targetBean.setExpiry(getExpiry());
		targetBean.setOptionType(getOptionType());
		targetBean.setAction(getAction());
		targetBean.setQty(getQty());
		targetBean.setOrderType(getOrderType());
		targetBean.setLimitPrice(getLimitPrice());
		targetBean.setStopPrice(getStopPrice());
		targetBean.setValidity(getValidity());
		
		targetBean.setScripCode(getScripCode());
		targetBean.setRmsCode(getRmsCode());
		targetBean.setBannerData(getBannerData());
		targetBean.setBannerDate(getBannerDate());
		targetBean.setOrderNumber(getOrderNumber());
		targetBean.setEQ(getEQ());
		targetBean.setEP(getEP());
		targetBean.setToken(getToken());
		return targetBean;
	}
	
}
