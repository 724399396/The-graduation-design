package com.qunar.liwei.graduation.weibo_crawler;

public class Weibo {
	private String userName;
	private String text;
	private String type;
	private String from;
	private String time;
	private String imageUrl;
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Weibo() {
		super();
	}
	
	public Weibo(String userName, String text, String type, String from,
			String time, String imageUrl) {
		super();
		this.userName = userName;
		this.text = text;
		this.type = type;
		this.from = from;
		this.time = time;
		this.imageUrl = imageUrl;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Weibo [userName=" + userName + ", text=" + text + ", type="
				+ type + ", from=" + from + ", time=" + time + ", imagrUrl="
				+ imageUrl + "]\n";
	}
	
	
	
}
