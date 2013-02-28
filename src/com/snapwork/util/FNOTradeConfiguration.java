package com.snapwork.util;

public class FNOTradeConfiguration {
	
	public final static byte FNO_TRADE_TYPE_FUTIDX = 0;
	public final static byte FNO_TRADE_TYPE_OPTIDX = 1;
	
	public final static byte FNO_TRADE_ORDER_TYPE_LIMIT = 0;
	public final static byte FNO_TRADE_ORDER_TYPE_MARKET = 1;
	
	public final static byte FNO_OPTION_TYPE_CE = 0;
	public final static byte FNO_OPTION_TYPE_PE = 1;
	
	private byte fnoTradeType;
	private byte optioncepeType;
	private String companyCode;
	
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public byte getOptioncepeType() {
		return optioncepeType;
	}

	public void setOptioncepeType(byte optioncepeType) {
		this.optioncepeType = optioncepeType;
	}

	public void setFnoTradeType(byte fnoTradeType) {
		this.fnoTradeType = fnoTradeType;
	}

	public byte getFnoTradeType() {
		return fnoTradeType;
	}

	//Only matters if fnoTradeTYpe == FNO_TRADE_TYPE_FUTIDX
	private byte fnoOrderType;

	public void setFnoOrderType(byte fnoOrderType) {
		this.fnoOrderType = fnoOrderType;
	}

	public byte getFnoOrderType() {
		return fnoOrderType;
	}

	//Only matters if fnoTradeTYpe == FNO_TRADE_TYPE_OPTIDX
	private String fnoExpiryDate;

	public void setFnoExpiryDate(String fnoExpiryDate) {
		this.fnoExpiryDate = fnoExpiryDate;
	}

	public String getFnoExpiryDate() {
		return fnoExpiryDate;
	}

	private String fnoStrikePriceValue;
	
	public void setFnoStrikePriceValue(String fnoStrikePriceValue) {
		this.fnoStrikePriceValue = fnoStrikePriceValue;
	}

	public String getFnoStrikePriceValue() {
		return fnoStrikePriceValue;
	}

	public FNOTradeConfiguration copy()
	{
		FNOTradeConfiguration copy = new FNOTradeConfiguration();
		copy.fnoExpiryDate = fnoExpiryDate;
		copy.fnoOrderType = fnoOrderType;
		copy.fnoStrikePriceValue = fnoStrikePriceValue;
		copy.fnoTradeType = fnoTradeType;
		copy.optioncepeType = optioncepeType;
		return copy;
	}
}
