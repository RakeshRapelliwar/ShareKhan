package com.snapwork.beans;

public class News {	
	
	private String Id ;
	private String Title;
	private String Source;
	private String Thumbnail=null;
	private String Url;
	private String LandingUrl;
	private String contentURL;
	private String Time;
	
	
	public String getLandingUrl() {
		return LandingUrl;
	}
	public void setLandingUrl(String landingUrl) {
		LandingUrl = landingUrl;
	}
	public String getContentURL() {
		return contentURL;
	}
	public void setContentURL(String contentURL) {
		this.contentURL = contentURL;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}

	public void setId(String id) {
		Id = id;
	}
	public String getId() {
		return Id;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getTitle() {
		return Title;
	}
	public void setSource(String source) {
		Source = source;
	}
	public String getSource() {
		return Source;
	}
	public void setThumbnail(String thumbnail) {
		Thumbnail = thumbnail;
	}
	public String getThumbnail() {
		return Thumbnail;
	}

	public void setUrl(String url) {
		Url = url;
	}
	public String getUrl() {
		return Url;
	}
	
	public News copy(){
		//Log.d("Create Team",Id+" : "+Name+" : "+SName+" : "+Thumbnail);
		News copy = new News();
		copy.Id=Id;
		copy.Source = Source;
		copy.Title = Title;
		copy.Thumbnail= Thumbnail;	
		copy.Url = Url;
		copy.LandingUrl=LandingUrl;
		copy.contentURL=contentURL;
		copy.Time=Time;
		return copy;
	}
	public News copy(int id){
		//Log.d("Create Team",Id+" : "+Name+" : "+SName+" : "+Thumbnail);
		News copy = new News();
		copy.Id=Id;
		copy.Source = Source;
		copy.Title = Title;
		copy.Thumbnail= Thumbnail;	
		copy.Url = Url;
		copy.LandingUrl=LandingUrl;
		copy.contentURL=contentURL;
		copy.Time=Time;
		return copy;
	}
	
}
