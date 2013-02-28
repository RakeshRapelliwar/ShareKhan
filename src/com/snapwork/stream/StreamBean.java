package com.snapwork.stream;

public class StreamBean
{
	public StreamBean()
	{
	}
	
	private int code = 1;
	private String ltp = "UNCHANGED";
	private String perChange = "UNCHANGED";
	private String change = "UNCHANGED";
	
	
	public int getCode() {
		return code;
	}


	public void setCode(int code) {
		this.code = code;
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


	public StreamBean copy(){
		StreamBean copy = new StreamBean();
		copy.code = code;
		copy.ltp = ltp;
		copy.perChange = perChange;
		copy.change = change;
		return copy;
	}

}
