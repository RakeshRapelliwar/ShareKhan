package com.snapwork.beans;

public class Announcement
{
	public Announcement()
	{
	}
	private String totalRecord;
	private String totalPages;
	private String startIndex;
	private String endIndex;
	private String currentPage;
	private String rowPerPage;
	public String getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(String totalRecord) {
		this.totalRecord = totalRecord;
	}
	public String getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(String totalPages) {
		this.totalPages = totalPages;
	}
	public String getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}
	public String getEndIndex() {
		return endIndex;
	}
	public void setEndIndex(String endIndex) {
		this.endIndex = endIndex;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getRowPerPage() {
		return rowPerPage;
	}
	public void setRowPerPage(String rowPerPage) {
		this.rowPerPage = rowPerPage;
	}
	public Announcement copy(){
		Announcement copy = new Announcement();
		copy.currentPage = currentPage;
		copy.endIndex = endIndex;
		copy.rowPerPage = rowPerPage;
		copy.startIndex = startIndex;
		copy.totalPages = totalPages;
		copy.totalRecord = totalRecord;
		return copy;
	}

}
