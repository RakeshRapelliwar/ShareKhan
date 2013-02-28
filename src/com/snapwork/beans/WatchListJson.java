package com.snapwork.beans;

public class WatchListJson {
	
	private String code;
	private String symbol;
	public String getCode() {
		return code;
	}

	public void setCode(String companyCode) {
		if(companyCode==null)
			companyCode = "";
		this.code = companyCode;
	}

	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		if(symbol==null)
			symbol = "";
		this.symbol = symbol;
	}
	
	public WatchListJson copy(){
		//Log.d("Create Team",Id+" : "+Name+" : "+SName+" : "+Thumbnail);
		
		WatchListJson copy = new WatchListJson();
		copy.code = code;
		copy.symbol=symbol;
		return copy;
	}
	

}
