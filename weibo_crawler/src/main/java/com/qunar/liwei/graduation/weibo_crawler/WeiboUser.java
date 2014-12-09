package com.qunar.liwei.graduation.weibo_crawler;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class WeiboUser {
	private Queue<String> urls = new LinkedList<String>();
	private String name;
	private String maxDate;
	private String minDate;
	DataManager dataManager = new DataManager();
	private volatile int hashCode;

	public String getMaxDate() {
		if (maxDate == null) {
			maxDate = dataManager.getMaxDate(name);
		}
		return maxDate;
	}

	public String getMinDate() {
		if (minDate == null) {
			minDate = dataManager.getMinDate(name);
		}
		return minDate;
	}

	private WeiboUser(String name) {
		this.name = name;
	}
	
	public static WeiboUser newInstance(String name) {
		return new WeiboUser(name);
	}

	public String getUrl() {
		return urls.poll();
	}

	public void addUrls(List<String> urls) {
		this.urls.addAll(urls);
	}
	public void addUrl(String url) {
		urls.add(url);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isFinishCrawler() {
		return urls.isEmpty();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof WeiboUser))
			return false;
		WeiboUser other = (WeiboUser)o;
		return other.name.equals(this.name);
	}
	
	@Override
	public int hashCode() {
		int result = hashCode;
		if (result == 0) {
			result = 17;
			result = 31 * result + name.hashCode();
			hashCode = result;
		}
		return result;
	
	}
}
