package com.qunar.liwei.graduation.weibo_crawler.util;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.qunar.liwei.graduation.weibo_crawler.Weibo;


public class HtmlPageParse {
	private static final Map<String,String> coockiesAsMap = 
			Login.loginAndGetCookie("181212631@163.com", "wwee13");
		
	public static List<Weibo> getWeiboList(String url) 
			throws IOException {
		Document doc = Jsoup.connect(url)
				  .userAgent("Mozilla")
				  .cookies(coockiesAsMap)
				  .timeout(3000)
				  .get();
		List<Weibo> weiboList = new LinkedList<>();
		String name = doc.title().substring(0, doc.title().lastIndexOf("的"));
		Elements weiboEls = doc.select("div.c");
		for (Element weiboEl : weiboEls) {
			if (!weiboEl.id().contains("M_"))
				continue;
			Weibo weibo = new Weibo();
			weibo.setUserName(name);
			String text = weiboEl.text();
			weibo.setText(text.substring(0, text.lastIndexOf("赞") - 1));
			parseAndSaveFromAndType(weibo, text);
			parseAndSaveImage(weibo, weiboEl);
			parseAndSaveTime(weibo, text);
    		
			weiboList.add(weibo);
		}
		return weiboList;
	}
	
	private static void parseAndSaveFromAndType(Weibo weibo, String text) {
		String from = null;
		String type = "原创";
		int endIndex = -1;
		if ((endIndex = text.indexOf("的微博")) > 0) {
			from = text.substring(4, endIndex - 1);
			type = "转发";
		}
		weibo.setFrom(from);
		weibo.setType(type);
	}
	
	private static void parseAndSaveTime(Weibo weibo, String text) {
		int timeStartIndex = text.lastIndexOf("收藏");
		int timeEndIndex = text.lastIndexOf("来自");
		String time = text.substring(
				timeStartIndex + 3, timeEndIndex-1);
		time = ParseTime.processTime(time);
		weibo.setTime(time);
	}
	
	private static void parseAndSaveImage(Weibo weibo, Element weiboEl) {
		Elements images = weiboEl.select("a:matches(原图)");
		for (Element image : images) {
			String imageSourceUrl = image.toString();
			String imageId = imageSourceUrl.substring(
					imageSourceUrl.lastIndexOf("="), imageSourceUrl.lastIndexOf("\""));
			weibo.setImageUrl("http://ww2.sinaimg.cn/large/" 
					+ imageId + ".jpg");
		}
	}
	
	
}
