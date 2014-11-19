package com.qunar.liwei.weibo_crawler;

import java.io.IOException;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.util.Config;


public class WeiboCrawler extends BreadthCrawler {
	  public WeiboCrawler() {
		  setUseragent("Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:26.0) Gecko/20100101 Firefox/26.0");
		  setCookie("gsid_CTandWM=4uGlbea51neHdDAVSjA9umllf2H");
	  }

    @Override
    public void visit(Page page) {
    	org.jsoup.select.Elements divs = page.getDoc().select("div.c");
        for (org.jsoup.nodes.Element div : divs) {
            System.out.println(div.text());
        }
    }

    public static void main(String[] args) throws IOException, Exception {
        Config.topN = 0;
        WeiboCrawler crawler=new WeiboCrawler();
        //for (long userId = 1000000000; userId <= 1999999999; userId++) {
	        for (int i = 1; i <= 10; i++) {
	            crawler.addSeed("http://weibo.cn/wenzhang626?page=" + i);
	            //http://zhidao.baidu.com/daily/view?id=3151
	            //http://weibo.cn/u/1616192700?page=1
	        }
        //}
        crawler.addRegex(".*");
        crawler.setThreads(2);
        crawler.start(1);

    }
}
