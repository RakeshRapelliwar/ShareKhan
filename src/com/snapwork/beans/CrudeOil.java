package com.snapwork.beans;

public class CrudeOil {
	
	private String crudeVal;
	private String crudeChange;
	
	
	public String getCrudeVal() {
		return crudeVal;
	}
	public void setCrudeVal(String crudeVal) {
		this.crudeVal = crudeVal;
	}
	public String getCrudeChange() {
		return crudeChange;
	}
	public void setCrudeChange(String crudeChange) {
		this.crudeChange = crudeChange;
	}
	
	
	public CrudeOil copy(){
		
		CrudeOil copy = new CrudeOil();
		
		copy.crudeVal = crudeVal;
		copy.crudeChange = crudeChange;
		return copy;
	}

}
