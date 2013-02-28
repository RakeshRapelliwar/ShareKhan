package com.snapwork.beans;

public class ChartItemHeading {
	
	private String n;
	private String cC;
	private String iC;
	private String pC;
	private String o;
	private String h;
	private String l;
	private String d;
	private String ttq;
	
	public String getN() {
		return n;
	}

	public void setN(String n) {
		this.n = n;
	}

	public String getcC() {
		return cC;
	}

	public void setcC(String cC) {
		this.cC = cC;
	}

	public String getiC() {
		return iC;
	}

	public void setiC(String iC) {
		this.iC = iC;
	}

	public String getpC() {
		return pC;
	}

	public void setpC(String pC) {
		this.pC = pC;
	}

	public String getO() {
		return o;
	}

	public void setO(String o) {
		this.o = o;
	}

	public String getH() {
		return h;
	}

	public void setH(String h) {
		this.h = h;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public String getD() {
		return d;
	}

	public void setD(String d) {
		this.d = d;
	}

	public String getTtq() {
		return ttq;
	}

	public void setTtq(String ttq) {
		this.ttq = ttq;
	}

	public ChartItemHeading copy(){
		//Log.d("Create Team",Id+" : "+Name+" : "+SName+" : "+Thumbnail);
		
		ChartItemHeading copy = new ChartItemHeading();
		copy.n = n;
		copy.cC = cC;
		copy.iC = iC;
		copy.pC = pC;
		copy.o = o;
		copy.h = h;
		copy.l = l;
		copy.d = d;
		copy.ttq = ttq;
		return copy;
	}
	

}
