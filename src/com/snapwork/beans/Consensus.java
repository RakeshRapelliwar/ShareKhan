package com.snapwork.beans;

public class Consensus {
	
	private String recommendation;
	private String nextEarnings;
	private String companyFiscalYearEndMonth;
	private String lastUpdated;
	public String getRecommendation() {
		return recommendation;
	}
	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}
	public String getNextEarnings() {
		return nextEarnings;
	}
	public void setNextEarnings(String nextEarnings) {
		this.nextEarnings = nextEarnings;
	}
	public String getCompanyFiscalYearEndMonth() {
		return companyFiscalYearEndMonth;
	}
	public void setCompanyFiscalYearEndMonth(String companyFiscalYearEndMonth) {
		this.companyFiscalYearEndMonth = companyFiscalYearEndMonth;
	}
	public String getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	
	public Consensus copy(){
		
		Consensus copy = new Consensus();
		
		copy.recommendation = recommendation;
		copy.nextEarnings = nextEarnings; 
		copy.companyFiscalYearEndMonth = companyFiscalYearEndMonth;
		copy.lastUpdated = lastUpdated;
		
		return copy;
	}
	
	

}
