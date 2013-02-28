package com.snapwork.beans;

import com.snapwork.util.Utils;

public class HomeJson {
	
	private String companyCode;
	private String lastTradedPrice;
	private String volume;
	private String percentageDiff;
	private String fiftyTwoWeekHigh;
	private String fiftyTwoWeekLow;
	private String lastTradedTime;
	private String changePercent;
	private String change;
	private String marketCap;
	private String high;
	private String low;
	private String prevClose;
	private String openInterest;
	private String marketLot;
	private String changeInOpenInterest;
	private String symbol;
	private boolean stream;
	private boolean exchange;
	private String displayName1;
	private String displayName2;
	private String religareCode;
	private String marketStatus;
	
	
	private String a,d,s;
	
	public static String NODATALTP = "0.00";
	
	//For Market Depth
	private String[] buyOrder = new String[5];
	private String[] buyQty = new String[5];
	private String[] buyPrice = new String[5];
	private String[] sellOrder = new String[5];
	private String[] sellQty = new String[5];
	private String[] sellPrice = new String[5];
	
	public boolean isStream() {
		return stream;
	}

	public boolean isExchange() {
		return exchange;
	}

	public void setExchange(boolean exchange) {
		this.exchange = exchange;
	}

	public void setStream(boolean stream) {
		this.stream = stream;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setA(String a) {
		this.a = a;
	}
	
	
	public String getA() {
		return a;
	}
	
	
	
	public void setD(String d) {
		this.d = d;
	}
	
	
	public String getD() {
		return d;
	}
	
	
	
	public void setS(String s) {
		this.s = s;
	}
	
	
	public String getS() {
		return s;
	}
	
	
	public void setCompanyCode(String companyCode) {
		if(companyCode==null)
			companyCode = "";
		this.companyCode = companyCode;
	}

	public String getMarketStatus() {
		if(marketStatus==null)
			marketStatus = "";
		return marketStatus;
	}

	public void setMarketStatus(String marketStatus) {
		if(marketStatus==null)
			marketStatus = "";
		if(marketStatus.equalsIgnoreCase("CLOSED"))
			Utils.MARKET_CLOSED = true;
		else
			Utils.MARKET_CLOSED = false;
		this.marketStatus = marketStatus;
	}

	public String getLastTradedPrice() {
		return lastTradedPrice;
	}

	public void setLastTradedPrice(String lastTradedPrice) {
		if(lastTradedPrice==null)
			lastTradedPrice = "";
		else if(lastTradedPrice.equalsIgnoreCase("null"))
			//lastTradedPrice = "No Data";
			lastTradedPrice = "0.00";
		this.lastTradedPrice = lastTradedPrice;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		if(volume==null)
			volume = "";
		else if(volume.equalsIgnoreCase("null"))
			volume = "";
		this.volume = volume;
	}

	public String getPercentageDiff() {
		return percentageDiff;
	}

	public void setPercentageDiff(String percentageDiff) {
		if(percentageDiff==null)
			percentageDiff = "0.00";
		else if(percentageDiff.equalsIgnoreCase("null"))
			percentageDiff = "0.00";
		this.percentageDiff = percentageDiff;
	}

	public String getFiftyTwoWeekHigh() {
		return fiftyTwoWeekHigh;
	}

	public void setFiftyTwoWeekHigh(String fiftyTwoWeekHigh) {
		if(fiftyTwoWeekHigh==null)
			fiftyTwoWeekHigh = "";
		else if(fiftyTwoWeekHigh.equalsIgnoreCase("null"))
			fiftyTwoWeekHigh = "";
		this.fiftyTwoWeekHigh = fiftyTwoWeekHigh;
	}

	public String getFiftyTwoWeekLow() {
		return fiftyTwoWeekLow;
	}

	public void setFiftyTwoWeekLow(String fiftyTwoWeekLow) {
		if(fiftyTwoWeekLow==null)
			fiftyTwoWeekLow = "";
		else if(fiftyTwoWeekLow.equalsIgnoreCase("null"))
			fiftyTwoWeekLow = "";
		this.fiftyTwoWeekLow = fiftyTwoWeekLow;
	}

	public String getLastTradedTime() {
		return lastTradedTime;
	}

	public void setLastTradedTime(String lastTradedTime) {
		if(lastTradedTime==null)
			lastTradedTime = "";
		else if(lastTradedTime.equalsIgnoreCase("null"))
			lastTradedTime = "";
		this.lastTradedTime = lastTradedTime;
	}

	public String getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(String changePercent) {
		if(changePercent==null)
			changePercent = "0.00";
		else if(changePercent.equalsIgnoreCase("null"))
			changePercent = "0.00";
		this.changePercent = changePercent;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		if(change==null)
			change = "0.00";
		else if(change.equalsIgnoreCase("null"))
			change = "0.00";
		this.change = change;
	}

	public String getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(String marketCap) {
		if(marketCap==null)
			marketCap = "";
		else if(marketCap.equalsIgnoreCase("null"))
			marketCap = "";
		this.marketCap = marketCap;
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		if(high==null)
			high = "";
		else if(high.equalsIgnoreCase("null"))
			high = "";
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		if(low==null)
			low = "";
		else if(low.equalsIgnoreCase("null"))
			low = "";
		this.low = low;
	}

	public String getPrevClose() {
		return prevClose;
	}

	public void setPrevClose(String prevClose) {
		if(prevClose==null)
			prevClose = "";
		else if(prevClose.equalsIgnoreCase("null"))
			prevClose = "";
		this.prevClose = prevClose;
	}

	public String getOpenInterest() {
		return openInterest;
	}

	public void setOpenInterest(String openInterest) {
		if(openInterest==null)
			openInterest = "";
		else if(openInterest.equalsIgnoreCase("null"))
			openInterest = "";
		this.openInterest = openInterest;
	}

	public String getMarketLot() {
		return marketLot;
	}

	public void setMarketLot(String marketLot) {
		if(marketLot==null)
			marketLot = "";
		else if(marketLot.equalsIgnoreCase("null"))
			marketLot = "";
		this.marketLot = marketLot;
	}

	public String getChangeInOpenInterest() {
		return changeInOpenInterest;
	}

	public void setChangeInOpenInterest(String changeInOpenInterest) {
		if(changeInOpenInterest==null)
			changeInOpenInterest = "";
		else if(changeInOpenInterest.equalsIgnoreCase("null"))
			changeInOpenInterest = "";
		this.changeInOpenInterest = changeInOpenInterest;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		if(symbol==null)
			symbol = "";
		else if(symbol.equalsIgnoreCase("null"))
			symbol = "";
		this.symbol = symbol;
	}
	
	

	public String[] getBuyOrder() {
		return buyOrder;
	}

	public void setBuyOrder(String buyOrder, int i) {
		if(buyOrder==null)
			buyOrder = "0";
		else if(buyOrder.equalsIgnoreCase("null"))
			buyOrder = "0";
		this.buyOrder[i] = buyOrder;
	}

	public String[] getBuyQty() {
		return buyQty;
	}

	public void setBuyQty(String buyQty, int i) {
		if(buyQty==null)
			buyQty = "0";
		else if(buyQty.equalsIgnoreCase("null"))
			buyQty = "0";
		this.buyQty[i] = buyQty;
	}

	public String[] getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(String buyPrice,int i) {
		if(buyPrice==null)
			buyPrice = "0";
		else if(buyPrice.equalsIgnoreCase("null"))
			buyPrice = "0";
		this.buyPrice[i] = buyPrice;
	}

	public String[] getSellOrder() {
		return sellOrder;
	}

	public void setSellOrder(String sellOrder, int i) {
		if(sellOrder==null)
			sellOrder = "0";
		else if(sellOrder.equalsIgnoreCase("null"))
			sellOrder = "0";
		this.sellOrder[i] = sellOrder;
	}

	public String[] getSellQty() {
		return sellQty;
	}

	public void setSellQty(String sellQty, int i) {
		if(sellQty==null)
			sellQty = "0";
		else if(sellQty.equalsIgnoreCase("null"))
			sellQty = "0";
		this.sellQty[i] = sellQty;
	}

	public String[] getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice, int i) {
		if(sellPrice==null)
			sellPrice = "0";
		else if(sellPrice.equalsIgnoreCase("null"))
			sellPrice = "0";
		this.sellPrice[i] = sellPrice;
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

	public String getReligareCode() {
		return religareCode;
	}

	public void setReligareCode(String religareCode) {
		this.religareCode = religareCode;
	}

	public HomeJson copy(){
		//Log.d("Create Team",Id+" : "+Name+" : "+SName+" : "+Thumbnail);
		
		HomeJson copy = new HomeJson();
		copy.companyCode = companyCode;
		copy.lastTradedPrice=lastTradedPrice;
		copy.volume=volume;
		copy.percentageDiff=percentageDiff;
		copy.fiftyTwoWeekHigh=fiftyTwoWeekHigh;
		copy.fiftyTwoWeekLow=fiftyTwoWeekLow;
		copy.lastTradedTime=lastTradedTime;
		copy.changePercent=changePercent;
		copy.change=change;
		copy.marketCap=marketCap;
		copy.high=high;
		copy.low=low;
		copy.prevClose=prevClose;
		copy.openInterest=openInterest;
		copy.marketLot=marketLot;
		copy.changeInOpenInterest=changeInOpenInterest;
		copy.symbol=symbol;
		copy.buyOrder=buyOrder;
		copy.buyQty=buyQty;
		copy.buyPrice=buyPrice;
		copy.sellOrder=sellOrder;
		copy.sellQty=sellQty;
		copy.sellPrice=sellPrice;
		copy.displayName1=displayName1;
		copy.displayName2=displayName2;
		copy.religareCode=religareCode;
		copy.a=a;
		copy.d=d;
		copy.s=s;
		return copy;
	}
	

}
