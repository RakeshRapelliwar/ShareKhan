package com.snapwork.beans;

import com.snapwork.util.LOG;
import com.snapwork.util.Utils;

public class CommentaryBean {

	private String dc_Subject;
	private String description;
	private String source;
	private String dateTime;
	public String getDc_Subject() {
		return dc_Subject;
	}
	public void setDc_Subject(String dcSubject) {
		dc_Subject = dcSubject;
	}
	public String getDescription() {
		return Utils.removeAnchorTags(description);
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
	public CommentaryBean copy()
	{
		CommentaryBean copy=new CommentaryBean();
		copy.dc_Subject=dc_Subject;
		copy.description=description;
		copy.dateTime=dateTime;
		copy.source=source;
		
		return copy;
	}
}
