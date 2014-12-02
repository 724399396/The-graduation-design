package com.qunar.liwei.weibo_crawler;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.select.Elements;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.parser.HtmlParser;
import cn.edu.hfut.dmic.webcollector.parser.Parser;
import cn.edu.hfut.dmic.webcollector.util.Config;


public class WeiboCrawler extends BreadthCrawler {
	public static String cookieString = HtmlUnitTest.loginAndGetCookie(
			"181212631@163.com","wwee13" );
//	  @Override
//	    public Parser createParser(String url, String contentType) throws Exception {
//	        if (contentType == null) {
//	            return null;
//	        }
//	        if (contentType.contains("text/html")) {
//	        	MyHtmlParser parser=new MyHtmlParser(Config.topN);
//	            parser.setRegexRule(getRegexRule());
//	            return parser;
//	        }
//	        return null;
//	    }
    @Override
    public void visit(Page page) {
    	//System.out.println(page.getDoc().text());
    	System.out.println(page.getHtml());
    	Matcher matcher = Pattern.compile("<img src=\"([\\d\\w\\S]+.jpg)\"").matcher(page.getHtml());
    	Page imagePage = new Page();
    	while (matcher.find()) {
    		System.out.println(matcher.group(1));
    	}
    	Elements divs = page.getDoc().select("div.c");
        for (org.jsoup.nodes.Element div : divs) {
	    	if(div.id().contains("M_"))
	    		System.out.println(div.text());
        }
    }

    public static void main(String[] args) throws IOException, Exception {
        Config.topN = 1;
        WeiboCrawler crawler=new WeiboCrawler();
        crawler.setCookie(cookieString);
        for (int i = 1; i <= 1; i++) {
        	//http://weibo.cn/liyundi?page=2
        	//http://weibo.cn/search/mblog?hideSearchFrame=&keyword=%E6%97%85%E6%B8%B8&page=1&vt=4
            crawler.addSeed("http://weibo.cn/liyundi?page=1");
            //http://zhidao.baidu.com/daily/view?id=3151
            //http://weibo.cn/u/1616192700?page=1
        }
	    crawler.addRegex("+^http://weibo.cn/.*");
        crawler.addRegex("-^http://weibo.cn/reg.*");
        crawler.addRegex("-^http://weibo.cn/login.*");
        crawler.addRegex("-^http://weibo.cn/sinaurl.*");
        crawler.setThreads(2);
        crawler.start(1);

    }
}
