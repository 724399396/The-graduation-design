package com.qunar.liwei.graduation.weibo_crawler;

import java.io.Serializable;

public class Bug implements Serializable {
	private String id;
	private String imageUrl;
	public Bug(String id, String imageUrl) {
		super();
		this.id = id;
		this.imageUrl = imageUrl;
	}
	public Bug() {
		super();
	}
	
	public void bugFix() {
		imageUrl = imageUrl.replaceAll("=", "");
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@Override
	public String toString() {
		return "Bug [id=" + id + ", imageUrl=" + imageUrl + "]\n";
	}
	
}
