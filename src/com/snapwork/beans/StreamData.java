package com.snapwork.beans;

public class StreamData {
	private String code;
	private String ltp;
	private String change;
	private String changeper;
	
	public StreamData(String code, String ltp, String change, String changeper)
	{
		setCode(code);
		setLtp(ltp);
		setChange(change);
		setChangeper(changeper);
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLtp() {
		return ltp;
	}
	public void setLtp(String ltp) {
		this.ltp = ltp;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}
	public String getChangeper() {
		return changeper;
	}
	public void setChangeper(String changeper) {
		this.changeper = changeper;
	}

}
