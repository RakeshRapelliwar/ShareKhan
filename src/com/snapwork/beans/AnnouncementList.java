package com.snapwork.beans;

public class AnnouncementList {
	private String ann_code;
	private String scriptCode;
	private String companyCode;
	private String companyName;
	private String systemDateTime;
	private String typeOfAnnouncement;
	private String typeOfMeeting;
	private String dateOfMeeting;
	private String descriptor;
	private String headline;
	private String details;
	private String attachmentName;
	private String descriptorId;
	public AnnouncementList() 
	{

	}
	public String getAnn_code() 
	{
		return ann_code;
	}
	public void setAnn_code(String annCode) 
	{
		ann_code = annCode;
	}
	public String getScriptCode() 
	{
		return scriptCode;
	}
	public void setScriptCode(String scriptCode) 
	{
		this.scriptCode = scriptCode;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) 
	{
		this.companyCode = companyCode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) 
	{
		this.companyName = companyName;
	}
	public String getSystemDateTime() 
	{
		return systemDateTime;
	}
	public void setSystemDateTime(String systemDateTime) 
	{
		this.systemDateTime = systemDateTime;
	}
	public String getTypeOfAnnouncement() 
	{
		return typeOfAnnouncement;
	}
	public void setTypeOfAnnouncement(String typeOfAnnouncement) 
	{
		this.typeOfAnnouncement = typeOfAnnouncement;
	}
	public String getTypeOfMeeting() 
	{
		return typeOfMeeting;
	}
	public void setTypeOfMeeting(String typeOfMeeting) 
	{
		this.typeOfMeeting = typeOfMeeting;
	}
	public String getDateOfMeeting() 
	{
		return dateOfMeeting;
	}
	public void setDateOfMeeting(String dateOfMeeting) 
	{
		this.dateOfMeeting = dateOfMeeting;
	}
	public String getDescriptor() 
	{
		return descriptor;
	}
	public void setDescriptor(String descriptor) 
	{
		this.descriptor = descriptor;
	}
	public String getHeadline() 
	{
		return headline;
	}
	public void setHeadline(String headline) 
	{
		this.headline = headline;
	}
	public String getDetails() 
	{
		return details;
	}
	public void setDetails(String details) 
	{
		this.details = details;
	}
	public String getAttachmentName() 
	{
		return attachmentName;
	}
	public void setAttachmentName(String attachmentName) 
	{
		this.attachmentName = attachmentName;
	}
	public String getDescriptorId() 
	{
		return descriptorId;
	}
	public void setDescriptorId(String descriptorId) 
	{
		this.descriptorId = descriptorId;
	}

	public AnnouncementList copy()
	{
		AnnouncementList copy = new AnnouncementList();
		copy.ann_code = ann_code;
		copy.attachmentName = attachmentName;
		copy.companyCode = companyCode;
		copy.companyName = companyName;
		copy.dateOfMeeting = dateOfMeeting;
		copy.descriptor = descriptor;
		copy.descriptorId = descriptorId;
		copy.details = details;
		copy.headline = headline;
		copy.scriptCode = scriptCode;
		copy.systemDateTime = systemDateTime;
		copy.typeOfAnnouncement = typeOfAnnouncement;
		copy.typeOfMeeting = typeOfMeeting;
		return copy;
	}
}
