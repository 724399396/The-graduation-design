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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
            = CookieAbout.getCookiesList();

    // cookie循环的计数器 和 cookie的一个外部保存
    static AtomicInteger count = new AtomicInteger();
    static Map<String,String> cookie = cookiesAsMapList.next();
    /**
     * 所有获取网页都必须经过的方法
     * @param url 要获取的url
     * @return 返回Document
     */
    public static Document getDoc(String url){
        Document doc = null;
        try {
            count.incrementAndGet();
            if (count.get() % 50 == 0){
                    count.set(0);
                    System.out.println("休眠" + ",刚才用的:" + cookie );
                    cookie = cookiesAsMapList.next();
                    TimeUnit.SECONDS.sleep(30);
            }
            while(doc == null)
            	doc = Jsoup.connect(url)
            			.header("Accept", "text/html")
                        .userAgent("Mozilla")
                        .cookies(cookie)
                        .timeout(20000)
                        .get();
        } catch (IOException e) {
            System.err.printf("fetch %s failed" , url);
            LogHelper.logInFile(Thread.currentThread(), e);
        } catch (InterruptedException e) {
            LogHelper.logInFile(Thread.currentThread(), e);
            LogHelper.logInFile(Thread.currentThread(), e);
        }
        return doc;
    }
    // 开始获取一个页面的微博的主程序和辅助程序
    public static List<Weibo> getWeibosFromPage(String url){
        Document doc = getDoc(url);
        String name = getUserName(doc);
        Elements weiboClasses = doc.select("div.c");
        List<Weibo> weiboList = new ArrayList<>(10);
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
                type = "转发";
                from = forwards.get(0).child(0).text();
                forwardReason = outEle.select("span.cmt:containsOwn(转发理由:)").
                                parents().get(0).childNode(1).toString().replaceAll("&nbsp;", " ");;
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
    	String userName = doc.title().substring(0, doc.title().lastIndexOf("的"));
    	return userName;
    }
    
    // 开始分析mainUser的follow的情况
    public static int getPageNums(Document doc) {
    	int pageNums = Integer.parseInt(doc.select("div.pa").get(0).
    			getElementsByAttributeValue("name", "mp").attr("value"));
    	return pageNums;
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
            Document followDoc = HtmlPageParse.getDoc(
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