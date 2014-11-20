package com.qunar.liwei.weibo_crawler;

import java.io.IOException;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.parser.HtmlParser;
import cn.edu.hfut.dmic.webcollector.parser.Parser;
import cn.edu.hfut.dmic.webcollector.util.Config;


public class WeiboCrawler extends BreadthCrawler {
	public static String cookieString = HtmlUnitTest.loginAndGetCookie(
			"181212631@163.com","wwee13" );
	  @Override
	    public Parser createParser(String url, String contentType) throws Exception {
	        if (contentType == null) {
	            return null;
	        }
	        if (contentType.contains("text/html")) {
	        	MyHtmlParser parser=new MyHtmlParser(Config.topN);
	            parser.setRegexRule(getRegexRule());
	            return parser;
	        }
	        return null;
	    }
    @Override
    public void visit(Page page) {
    	org.jsoup.select.Elements divs = page.getDoc().select("div.c");
        for (org.jsoup.nodes.Element div : divs) {
            System.out.println(div.text());
        }
    }

    public static void main(String[] args) throws IOException, Exception {
        Config.topN = 100;
        WeiboCrawler crawler=new WeiboCrawler();
        crawler.setCookie(cookieString);
        //for (long userId = 1000000000; userId <= 1999999999; userId++) {
	        for (int i = 1; i <= 1; i++) {
	            crawler.addSeed("http://weibo.cn/wenzhang626?page=" + i);
	            //http://zhidao.baidu.com/daily/view?id=3151
	            //http://weibo.cn/u/1616192700?page=1
	        }
        //}
	    crawler.addRegex("+^http://weibo.cn/.*");
        crawler.addRegex("-^http://weibo.cn/reg.*");
        crawler.addRegex("-^http://weibo.cn/login.*");
        crawler.addRegex("-^http://weibo.cn/sinaurl.*");
        crawler.setThreads(4);
        crawler.start(2);

    }
}
