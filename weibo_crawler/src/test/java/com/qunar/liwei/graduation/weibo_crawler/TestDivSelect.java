package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;




import java.util.LinkedHashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class TestDivSelect {
        public static void main(String[] args) throws IOException {
                //Document doc = Jsoup.parse(new File("1.html"), "utf-8");
                
                Document doc = HtmlPageParse.getDoc("http://weibo.cn/pennyliang");
                System.out.println(getFollows(doc));
                //String followPage =getFollowPageUrl(doc);
                //Document followDoc = HtmlPageParse.getDoc(followPage);
//                Elements followsTables = doc.select("table");
//                for (Element followsTable : followsTables) {
//                        Element follow = followsTable.child(0).child(0).child(1).child(0);
//                        String name = follow.text();
//                        String url = follow.absUrl("href");
//                        System.out.println(name);
//                        System.out.println(url);
//                }
        }
        private static String getUserName(Document doc) {
        	String userName = doc.select("div.ut > span.ctt").get(0).
        			childNode(0).toString().replaceAll("&nbsp;", "");
        	return userName;
        }
        private static int getPageNums(Document doc) {
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
        	int pageNums = getPageNums(doc);
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
