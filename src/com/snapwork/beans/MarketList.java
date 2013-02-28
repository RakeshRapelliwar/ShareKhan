package com.snapwork.beans;

import java.util.Vector;

import com.snapwork.util.LOG;





public class MarketList {
	
	private String marketName;
	private String lastUpdated;
	private Vector Markets;
	private Vector MarketListData;
	
	
	public String getMarketName() {
		return marketName;
	}


	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}


	public String getLastUpdated() {
		return lastUpdated;
	}


	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	
	public void addMarket(Market mkt) {
		if(mkt == null)
			return;
		if(Markets == null)
			Markets = new Vector();
		 	Markets.addElement(mkt);
	}
	public void clearMarket() {
		Markets = null;
		
	}

	public Vector getMarket() {
		return Markets;
	}

	public void setMarket(Vector markets) {
		Markets = markets;
	}
	
	public void addMarketList(MarketList marketList) {
		if(marketList == null)
			return;
		if(MarketListData == null)
			MarketListData = new Vector();
		MarketListData.addElement(marketList);
	}

	public Vector getMarketList() {
		return MarketListData;
	}

	
	public MarketList copy(){
		//System.out.println("u r inside copy of marketList......");
		
		MarketList copy= new MarketList();
		
		copy.lastUpdated=lastUpdated;
		copy.marketName=marketName;
		
		
		if((Markets!=null)&&(Markets.size()>0))
		{
			copy.Markets = new Vector();
			for(int i=0;i<Markets.size();i++)
				copy.Markets.addElement(((Market)Markets.elementAt(i)));
		}
		
		if((MarketListData!=null)&&(MarketListData.size()>0))
		{
			copy.MarketListData = new Vector();
			for(int i=0;i<MarketListData.size();i++)
				copy.MarketListData.addElement(((MarketList)MarketListData.elementAt(i)).copy());
		}
		
		return copy();
		
	}

}
