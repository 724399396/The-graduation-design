package com.qunar.liwei.graduation.forwarding_analyze;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;



public class TestSearch {
		
	
        public static void main(String[] args) throws IOException {
        	System.setOut(new PrintStream(new File("outPrint.txt")));
        	start("西电", 1);
//              	Document doc = HtmlGet.search("兵马俑", null, "hot");
//               	rootPageAnalyze(doc);
//                //System.out.println(doc.text());
////                int maxForwardingNums = 0;
//                Elements weibos = doc.select("a:contains(转发)");
//                for(Element weibo : weibos)
//                	System.out.println(weibo.text().
//                			substring(3, weibo.text().length()-1));
//                int page = 113 / 10 + ( (113 % 10 == 0) ? 0 : 1);
//                String url = weibos.get(0).absUrl("href");
//                System.out.println(url);
//                System.out.println(HtmlGet.getDoc("http://weibo.cn/repost/BCsZLmfYh?uid=2660877923&rl=1&page=3"));
//                Document row = Jsoup.connect(url).
//                				cookies(CookieAbout.getCookie())
//                				.userAgent("Mozilla").get();
//                Elements forwardings = row.select("div.c:has(span.cc)");
//             
//                for (Element forwarding : forwardings) {
//                	System.out.println(forwarding);
//                	String name = null;
//                	StringBuilder text = new StringBuilder();
//                	for (Node node : forwarding.childNodesCopy()) {
//                		if(node.nodeName().equals("#text")) {
//                			text.append(node.toString());
//                		}
//                		else if (node.nodeName().equals("a")) {
//                			if (name == null)
//                				name = getInsideText(node.toString());
//                			else
//                				text.append(getInsideText(node.toString()));
//                		}
//                	}
//                	System.out.println("name: " + name);
//                	System.out.println("text: " + text.toString());
//                	//Document forwardingDoc = HtmlGet.search(text.toString(), name, "time");
//                	
//                }
//         
        }

        public static void start(String keyword, int deep) {
        	Document doc = HtmlGet.search(keyword, "", "hot");
        	searchPageProcess(doc, deep);
        }
        public static void start(String keyword, String nickName,int deep) {
        	Document doc = HtmlGet.search(keyword, nickName, "time");
        	searchPageProcess(doc, deep);
        }
        private static void searchPageProcess(Document doc, int deep) {
        	Elements hotWeibos = doc.select("a:contains(转发)");
        	if (hotWeibos.size() == 0) {
        		System.out.println("搜索的微博不存在");
        		System.exit(0);
        	}
        	Element hotWeibo = hotWeibos.get(0);
        	String url = hotWeibo.absUrl("href");
        	int forwardingNums = Integer.parseInt(
        			hotWeibo.text().substring(3, hotWeibo.text().length()-1));
   			int pageNums = forwardingNums / 10 + ( (forwardingNums % 10 == 0) ? 0 : 1);
	        System.out.println(url);
	        System.out.println(pageNums);
	        processForwardingPage(url, pageNums, deep);
        }
        
        private static void processForwardingPage(String url, int pageNums, int deep) {
			List<Document> docList = new LinkedList<Document>();
			for (int pageIndex = 1; pageIndex <= pageNums; pageIndex++) {
				docList.add(HtmlGet.getDoc(url + "&page=" + pageIndex));
			}
			for (Document row : docList) {
				Elements forwardings = row.select("div.c:has(span.cc)");             
                for (Element forwarding : forwardings) {
                	String name = null;
                	StringBuilder text = new StringBuilder();
                	for (Node node : forwarding.childNodesCopy()) {
                		if(node.nodeName().equals("#text")) {
                			text.append(node.toString());
                		}
                		else if (node.nodeName().equals("a")) {
                			if (name == null)
                				name = getInsideText(node.toString());
                			else
                				text.append(getInsideText(node.toString()));
                		}
                	}
                	System.out.println("层数:" + deep);
                	System.out.println("name: " + name);
                	System.out.println("text: " + text.toString());
                	start(text.toString(), name, deep++);
                }
			}
        }
        
        private static String getInsideText(String source) {
        	Matcher m = Pattern.compile(">(\\S+)<").matcher(source);
        	if (m.find())
        		return m.group(1).replaceAll("&nbsp;", " ");
        	throw new AssertionError();
        }
        
        private static void rootPageAnalyze(Document doc) {
        	 Elements weibos = doc.select("a:contains(转发)");
        	 if(weibos.size() < 1)
            	 return;
        	 Element weibo = weibos.get(0);
        	 int forwardingNums = Integer.parseInt(
        			 weibo.text().substring(3, weibo.text().length()-1));
        	 int page = forwardingNums / forwardingNums + ( (forwardingNums % 10 == 0) ? 0 : 1);
        	 List<Document> docList = new LinkedList<Document>();
        	 docList.add(doc);
        	 for (int pageIndex = 2; pageIndex <= page; pageIndex++) {
        		 docList.add(HtmlGet.getDoc(doc.html()));
        	 }
        }
        
        private static void analyzeAndGoOnFetch(Document doc) throws IOException {
        	
             Elements weibos = doc.select("a:contains(转发)");
             if(weibos.size() < 1)
            	 return;
             for(Element weibo : weibos)
             	System.out.println(weibo.text().
             			substring(3, weibo.text().length()-1));
             int page = 113 / 10 + ( (113 % 10 == 0) ? 0 : 1);
             String url = weibos.get(0).absUrl("href");
             System.out.println(url);
             Document row = Jsoup.connect(url).
             				cookies(CookieAbout.getCookie())
             				.userAgent("Mozilla").get();
             Elements forwardings = row.select("div.c:has(span.cc)");
          
             for (Element forwarding : forwardings) {
             	System.out.println(forwarding);
             	String name = null;
             	StringBuilder text = new StringBuilder();
             	for (Node node : forwarding.childNodesCopy()) {
             		if(node.nodeName().equals("#text")) {
             			text.append(node.toString());
             		}
             		else if (node.nodeName().equals("a")) {
             			if (name == null)
             				name = getInsideText(node.toString());
             			else
             				text.append(getInsideText(node.toString()));
             		}
             	}
             	System.out.println("name: " + name);
             	System.out.println("text: " + text.toString());
             	Document forwardingDoc = HtmlGet.search(text.toString(), name, "time");
             	
             }
        }
   

}