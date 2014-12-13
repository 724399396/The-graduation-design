package com.qunar.liwei.graduation.weibo_crawler.util;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.qunar.liwei.graduation.weibo_crawler.NameAndFollows;
import com.qunar.liwei.graduation.weibo_crawler.Weibo;


public class HtmlPageParse {
	private static List<Map<String,String>> cookiesAsMapList = new LinkedList<>();
	static {
		cookiesAsMapList.add(Login.loginAndGetCookie("181212631@163.com", "wwee13"));
		cookiesAsMapList.add(Login.getCookie());
		cookiesAsMapList.add(Login.getAnotherCookie());
	}
	
	static AtomicLong count = new AtomicLong();
		
	public static List<Weibo> getWeiboList(String url) 
			throws IOException {
		Document doc = getDoc(url);
		while(doc == null)
			doc = getDoc(url);
		List<Weibo> weiboList = new LinkedList<>();
		String name = doc.title().substring(0, doc.title().lastIndexOf("的"));
		Elements weiboEls = doc.select("div.c");
		for (Element weiboEl : weiboEls) {
			if (!weiboEl.id().contains("M_"))
				continue;
			Weibo weibo = new Weibo();
			weibo.setUserName(name);
			String text = weiboEl.text();
			if (text.contains("抱歉，此微博已被作者删除"))
				continue;
			if (text.contains("此微博已被删"))
				continue;
			boolean copy = text.startsWith("转发了");
			int start = text.indexOf("的微博");
			int end = text.lastIndexOf("原图") - 1;
			if (end < 0)
				end = text.lastIndexOf("赞") - 1;
			if (copy && start > 0)
				weibo.setCommontText(text.substring(start+4, end));
			else
				weibo.setCommontText(text.substring(0, end));
			parseAndSaveFromAndType(weibo, text);
			parseAndSaveImage(weibo, weiboEl);
			parseAndSaveTime(weibo, text);
    		
			weiboList.add(weibo);
		}
		return weiboList;
	}
	static int cookieIndex = 0;
	public static Document getDoc(String url){
		Document doc = null;
		try {		
			count.incrementAndGet();
			if (count.get() % 50 == 0){
				count.set(0);
				System.out.println("休眠" + ",刚才用的:" + cookieIndex);
				if (cookieIndex == 0)
					cookieIndex = 1;
				else {
					if (cookieIndex == 1)
						cookieIndex = 2;
					else { 
						if (cookieIndex == 2)
							cookieIndex = 0;
					}
				} 
				TimeUnit.SECONDS.sleep(30);
			}
			doc = Jsoup.connect(url)
					  .userAgent("Mozilla")
					  .cookies(cookiesAsMapList.get(cookieIndex))
					  .timeout(20000)
					  .get();
		} catch (IOException e) {
			System.err.printf("fetch %s failed" , url);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return doc;
	} 
	
	private static void parseAndSaveFromAndType(Weibo weibo, String text) {
		String from = null;
		String type = "原创";
		int endIndex = -1;
		if ((text.indexOf("转发了") == 0) 
				&&(endIndex = text.indexOf("的微博")) > 0) {
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
					imageSourceUrl.lastIndexOf("=") + 1, imageSourceUrl.lastIndexOf("\""));
			weibo.setImageUrl("http://ww2.sinaimg.cn/large/" 
					+ imageId + ".jpg");
		}
	}
	
	public static NameAndFollows parseMainUser(Document doc) throws IOException {
		String name = doc.title().substring(0, doc.title().lastIndexOf("的"));
		Elements userAbout = doc.select("div.tip2");
		Elements follow = userAbout.get(0).select("a:matches(关注)");
		String followHref = follow.get(0).absUrl("href");
		Set<String> followNameSet = new LinkedHashSet<String>();
		Set<String> followUrlSet = new LinkedHashSet<String>();
		preParseAndAddFollow(followHref, followNameSet, followUrlSet);
		return new NameAndFollows(name, followNameSet, followUrlSet);
	}
	
	private static void preParseAndAddFollow(
			String followHref, Set<String> followNameSet, Set<String> followUrlSet) 
			throws IOException{
		int pageNums = getPageNums(followHref);
		for (int pageIndex = 1; pageIndex <= pageNums; pageIndex++) {
			parseAndAddFollow(followHref + "?page=" + pageIndex, 
					followNameSet, followUrlSet);
		}
	}
	private static int getPageNums(String followHref) 
			throws IOException {
		Document doc = getDoc(followHref);
		while (doc == null)
			doc = getDoc(followHref);
		Elements elems = doc.select("div.pa");
		Matcher matcher = Pattern.compile("[\\d]+/([\\d]+)").matcher(elems.get(0).text());
		if (matcher.find())
			return Integer.parseInt(matcher.group(1));
		throw new RuntimeException("关注页数无法读取");
	}
	private static void parseAndAddFollow(String url, Set<String> followNameSet
			, Set<String> followUrlSet) 
			throws IOException {
		Document doc = getDoc(url);
		while (doc == null)
			doc = getDoc(url);
		Elements elms = doc.select("table > tbody > tr");
		for (Element elm : elms) {
			String followUrl = elm.select("td").get(1).select("a").get(0).attr("href");
			String followName = elm.select("td").get(1).select("a").get(0).text();
			followNameSet.add(followName);
			followUrlSet.add(followUrl);
		}
	}
	public static int getPageNums(Document doc) 
			throws IOException {
		Elements elems = doc.select("div.pa");
		if (elems.size() < 1)
			return 1;
		Matcher matcher = Pattern.compile("[\\d]+/([\\d]+)").matcher(elems.get(0).text());
		if (matcher.find())
			return Integer.parseInt(matcher.group(1));
		throw new RuntimeException("关注页数无法读取");
	}
	
	public static String getName(Document doc) 
			throws IOException {
		return doc.title().substring(0, doc.title().lastIndexOf("的"));
	}
	
}
