package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jsoup.nodes.Document;

class WeiboUser implements Serializable {
	private static final long serialVersionUID = 5000717139101568459L;
	private String baseUrl;
	private Queue<String> urls = new LinkedList<String>();
	private String name;
	private String followsName = null;
	private Set<String> followsUrl = null;
	private transient Timestamp maxDate;
	private transient Timestamp minDate;
//	private static ThreadLocal<DataManager> dataManagerHolder
//	= new ThreadLocal<DataManager>() {
//		@Override
//		public DataManager initialValue() {
//			return new DataManager();
//		}
//	};
//	public static DataManager getDataManager() {
//		return dataManagerHolder.get();
//	}
	private static DataManager dataManager = new DataManager();
	private volatile int hashCode;
	
	// 构造器和工厂方法
	public WeiboUser(String baseUrl, Queue<String> urls, String name,
			String followsName, Set<String> followsUrl) {
		super();
		this.baseUrl = baseUrl;
		this.urls = urls;
		this.name = name;
		this.followsName = followsName;
		this.followsUrl = followsUrl;
		// 日期相关
		maxDate = dataManager.getMaxDate(name);
		minDate = dataManager.getMinDate(name);
	}
	
	public static WeiboUser newMainInstance(String baseUrl) 
			throws IOException {
		Document doc = HtmlPageParse.getDoc(baseUrl);
		while (doc == null)
			doc = HtmlPageParse.getDoc(baseUrl);
		FollowsUrlAndName fuan = HtmlPageParse.getFollows(doc);
		int pageNums = HtmlPageParse.getPageNums(doc);
		Queue<String> tempUrls = new LinkedList<>();
		for (int pageIndex = 1; pageIndex <= pageNums; pageIndex++)
			tempUrls.add(baseUrl + "?page=" + pageIndex);
		return new WeiboUser(baseUrl, tempUrls, fuan.getName()
				, fuan.getFollowName().toString(), fuan.getFollowUrl());
	}
	
	public static WeiboUser newFollowInstance(String baseUrl) 
			throws IOException {
		Document doc = HtmlPageParse.getDoc(baseUrl);
		while (doc == null)
			doc = HtmlPageParse.getDoc(baseUrl);
		int pageNums = HtmlPageParse.getPageNums(doc);
		pageNums = pageNums > 50 ? 50 : pageNums;
		Queue<String> tempUrls = new LinkedList<>();
		for (int pageIndex = 1; pageIndex <= pageNums; pageIndex++)
			tempUrls.add(baseUrl + "?page=" + pageIndex);
		return new WeiboUser(baseUrl, tempUrls, HtmlPageParse.getUserName(doc)
				, null, null);
	}


	public boolean weiboIsNew(Weibo weibo) {
		Timestamp date =  weibo.getTime();
		if (date == null)
			return false;
		maxDate = dataManager.getMaxDate(name);
		minDate = dataManager.getMinDate(name);	
		System.out.println(name +  ":" + date);
		System.out.println(minDate + "-" + maxDate);
		if (minDate == null && maxDate == null) {
			return true;
		}
		if (date.compareTo(minDate) < 0 || date.compareTo(maxDate) > 0) {
			return true;
		} else {
			return !dataManager.isWeiboExist(weibo);
		}
	}
	
	// 获取user中存着的链接
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
	
	public Set<String> getFollowsUrl() {
		return followsUrl;
	}

	public void setFollowsUrl(Set<String> followsUrl) {
		this.followsUrl = followsUrl;
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

	@Override
	public String toString() {
		return "WeiboUser [baseUrl=" + baseUrl + ", urls=" + urls + ", name="
				+ name + ", followsName=" + followsName + ", followsUrl="
				+ followsUrl + ", maxDate=" + maxDate + ", minDate=" + minDate
				+ ", dataManager=" + dataManager + ", hashCode=" + hashCode
				+ "]\n";
	}
	
	
}
