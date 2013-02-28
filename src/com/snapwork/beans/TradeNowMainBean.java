package com.snapwork.beans;

import java.util.Vector;

public class TradeNowMainBean {	
	
	private Vector dpId ;
	
	

	public void setDpId(String dpId) {
		if(this.dpId == null)
			this.dpId = new Vector();
		this.dpId.addElement(dpId);
	}
	public Vector getDpId() {
		if(dpId == null)
			return new Vector();
		return dpId;
	}
	
	public TradeNowMainBean copy(){
		//Log.d("Create Team",Id+" : "+Name+" : "+SName+" : "+Thumbnail);
		TradeNowMainBean copy = new TradeNowMainBean();
		copy.dpId=dpId;
		return copy;
	}
	public TradeNowMainBean copy(int id){
		//Log.d("Create Team",Id+" : "+Name+" : "+SName+" : "+Thumbnail);
		TradeNowMainBean copy = new TradeNowMainBean();
		copy.dpId=dpId;
		return copy;
	}
	
}
