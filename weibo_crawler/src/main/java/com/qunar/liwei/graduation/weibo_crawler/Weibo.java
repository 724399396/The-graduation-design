package com.qunar.liwei.graduation.weibo_crawler;

import java.io.Serializable;

public class Weibo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5570192519696895557L;
	private String userName;
	private String commontText;
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
	public Weibo(String userName, String commontText, String type, String from,
			String time, String imageUrl, String follows) {
		super();
		this.userName = userName;
		this.commontText = commontText;
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
	
	public String getCommontText() {
		return commontText;
	}
	public void setCommontText(String commontText) {
		this.commontText = commontText;
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
		return "Weibo [userName=" + userName + ", commontText=" + commontText
				+ ", type=" + type + ", from=" + from + ", time=" + time
				+ ", imageUrl=" + imageUrl + "]\n";
	}		
}
