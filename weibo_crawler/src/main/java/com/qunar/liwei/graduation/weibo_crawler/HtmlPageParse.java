package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.qunar.liwei.graduation.weibo_crawler.util.LogHelper;
import com.qunar.liwei.graduation.weibo_crawler.util.LoopList;
import com.qunar.liwei.graduation.weibo_crawler.util.ParseTime2String;
import com.qunar.liwei.graduation.weibo_crawler.util.ParseTime2Timestamp;


public class HtmlPageParse {
    /**
     * all Cookies
     */
    private static LoopList<Map<String,String>> cookiesAsMapList
            = CookieAbout.getCookiesLoopList();

    // cookie循环的计数器 和 cookie的一个外部保存
    static AtomicInteger count = new AtomicInteger(0);
    static Map<String,String> cookie = cookiesAsMapList.next();
    /**
     * 所有获取网页都必须经过的方法
     * @param url 要获取的url
     * @return 返回Document
     */
    public static synchronized Document getDoc(String url){
        Document doc = null;
        while (true) {
        	try {
	            System.out.println(Thread.currentThread().getId() + ":" + count.incrementAndGet());
	            if (count.get() % 50 == 0){
	                    count.set(0);
	                    System.out.println("休眠 ,刚才用的:" + cookie );
	                    cookie = cookiesAsMapList.next();
	                    TimeUnit.SECONDS.sleep(30);
	                    System.out.println("休眠结束");
	            }
	            if (url == null)
	            	break;
	        	doc = Jsoup.connect(url)
	        			.header("Accept", "text/html")
	                    .userAgent("Mozilla")
	                    .cookies(cookie)
	                    .timeout(4000)
	                    .get();
	        	break;
	        } catch (IOException e) {
	            System.err.printf("fetch %s failed%n" , url);
	            LogHelper.logInFile(Thread.currentThread(), e);
	        } catch (InterruptedException e) {
	            LogHelper.logInFile(Thread.currentThread(), e);
	            LogHelper.logInFile(Thread.currentThread(), e);
	        }
        }
        return doc;
    }
    // 开始获取一个页面的微博的主程序和辅助程序
    public static List<Weibo> getWeibosFromPage(String url, String name){
        Document doc = getDoc(url);
//        String name = getUserName(doc);
        List<Weibo> weiboList = new ArrayList<>(10);
        if (doc == null)
        	return weiboList;
        Elements weiboClasses = doc.select("div.c");
        for (Element weiboClass : weiboClasses) {
                if (!weiboClass.id().contains("M_"))
                        continue;
                weiboList.add(analysisPage(weiboClass, name));
        }
        return weiboList;
    }
    private static Weibo analysisPage(Element outEle, String userName) {
        Element firstDiv = outEle.child(0);
        //String userName = getWeiboName(firstDiv);
        String rowContext = getCommentText(firstDiv);
        if (rowContext.contains("抱歉，此微博已被作者删除"))
        	return null;
        Elements forwards = firstDiv.select("span.cmt");
        String type = "原创";
        String from = null;
        String forwardReason = null;
        if (forwards.size() > 0) {
        	int childSize = forwards.get(0).childNodes().size();
        	if (childSize > 2) {
                type = "转发";
                from = forwards.get(0).child(0).text();
                Element forwardReasonDiv = outEle.select("span.cmt:containsOwn(转发理由:)").
                        parents().get(0);
                forwardReason = "";
                for (Node node : forwardReasonDiv.childNodes())
                	if (node.nodeName().equals("#text"))
                		forwardReason += node.toString().replaceAll("&nbsp;", " ");
        	}
        }
        String imageUrl = getImageUrl(outEle);
        Timestamp time = getTime(outEle);
        Weibo weibo = new Weibo(userName, rowContext,
                        type, from, forwardReason, time, imageUrl);
        return weibo;
    }
    @SuppressWarnings("unused")
    // 这个是给通用的写的
	private static String getWeiboName(Element firstDiv) {
        return firstDiv.child(0).text();
    }
    private static String getCommentText(Element firstDiv) {
        return firstDiv.select("span.ctt").get(0).
        text().replaceAll("&nbsp;", " ");
    }
    private static String getImageUrl(Element ele) {
        String imageUrl = ele.select("a:containsOwn(原图)").attr("href");
        if (imageUrl.equals(""))
                return null;
        else {
                String imageId = imageUrl.substring(
                                imageUrl.lastIndexOf("=") + 1);
                return "http://ww2.sinaimg.cn/large/"
                + imageId + ".jpg";
        }
    }
    private static Timestamp getTime(Element ele) {
        String timeString =
                ele.select("span.ct").get(0).text().split(" ")[0];
        Timestamp time = ParseTime2Timestamp.parseTimestamp(
                        ParseTime2String.processTime(timeString));
        return time;
    }
    //结束获取一个页面的部分
    
    public static String getUserName(Document doc) {
//    	String userName = doc.select("div.ut").select("span.ctt").get(0).
//    			childNode(0).toString().replaceAll("&nbsp;", "");
    	String title = null;
    	while (title == null)
    		title = doc.title();
    	String userName = title.substring(0, title.lastIndexOf("的"));
    	return userName;
    }
    
    // 开始分析mainUser的follow的情况
//    public static int getPageNums(Document doc) {
//    	int pageNums = Integer.parseInt(doc.select("div.pa").get(0).
//    			getElementsByAttributeValue("name", "mp").attr("value"));
//    	return pageNums;
//    }
    public static int getPageNums(Document doc) {
		Elements elems = doc.select("div.pa");
		if (elems.size() == 0)
			return 1;
		Matcher matcher = Pattern.compile("[\\d]+/([\\d]+)").matcher(elems.get(0).text());
		if (matcher.find())
			return Integer.parseInt(matcher.group(1));
		throw new RuntimeException("关注页数无法读取");
	}
    private static String getFollowPageUrl(Document doc) {
        String followPage = doc.select("div.tip2").
                        select("a:containsOwn(关注)").get(0).absUrl("href");
        return followPage;
    }
    public static FollowsUrlAndName getFollows(Document doc) {
    	String followPageUrl = getFollowPageUrl(doc);
    	int pageNums = getPageNums(getDoc(followPageUrl));
    	Set<String> followName = new LinkedHashSet<>();
    	Set<String> followUrl = new LinkedHashSet<>();
    	for (int pageIndex = 1; pageIndex <= pageNums; pageIndex++) {
            Document followDoc = null;
            while(followDoc == null)
            	 followDoc = HtmlPageParse.getDoc(
                 		followPageUrl + "?page=" + pageIndex);
            Elements followsTables = followDoc.select("table");
            for (Element followsTable : followsTables) {
                    Element follow = followsTable.child(0).child(0).child(1).child(0);
                    String name = follow.text();
                    String url = follow.absUrl("href");
                    followName.add(name);
                    followUrl.add(url);
            }
    	}
    	String name = getUserName(doc);
    	FollowsUrlAndName fuan = new FollowsUrlAndName(name,followName, followUrl);
    	return fuan;
    }

}