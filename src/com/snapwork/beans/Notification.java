package com.snapwork.beans;

public class Notification {
	
	private String Nid = "";
	private String Id = "";
	private String Eid = "";
	private String Type = "";
	private String Title = "";
	private String ImageUrl = "";
	private String Status = "";
	
	public String getNid() {
		return Nid;
	}
	public void setNid(String nid) {
		Nid = nid;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getEid() {
		return Eid;
	}
	public void setEid(String eid) {
		Eid = eid;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getImageUrl() {
		return ImageUrl;
	}
	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	
	public Notification copy() {
		Notification notification = new Notification();
		
		notification.setEid(this.getEid());
		notification.setId(this.getId());
		notification.setNid(this.getNid());
		notification.setImageUrl(this.getImageUrl());
		notification.setStatus(this.getStatus());
		notification.setTitle(this.getTitle());
		notification.setType(this.getType());
		
		return notification;
	}

}
