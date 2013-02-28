package com.snapwork.beans;

public class ChartItem {
	
	private String time;
	private double value;	
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public ChartItem copy(){
		//Log.d("Create Team",Id+" : "+Name+" : "+SName+" : "+Thumbnail);
		
		ChartItem copy = new ChartItem();
		copy.time=time;
		copy.value = value;
		return copy;
	}
	

}
