package com.qunar.liwei.weibo_crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Page;

public class TrafficQuery extends BreadthCrawler {
	@Override
	public void visit(Page page) {
		String html = page.getHtml();
		String result = "";
		Matcher matcher = Pattern.compile("已用流量.*|可用流量.*").matcher(html);
		while (matcher.find())
			result+=matcher.group();
		outToFile(result);
	}
	private void outToFile(String str) {
		File file = new File("流量.txt");
		FileOutputStream out = null ;
		try {
			out = new FileOutputStream(file);
			out.write(str.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {		
				e.printStackTrace();
			}
		}
		
	}
		//PHPSESSID=84e88ec48cbf9650042c39b2642b85d0
	public static void main(String[] args) throws Exception  {
		TrafficQuery query = new TrafficQuery();
		query.setCookie("PHPSESSID=84e88ec48cbf9650042c39b2642b85d0");
		query.addSeed("http://zyzfw.xidian.edu.cn:8800/index.php");
		query.addRegex(".*");
		query.setThreads(1);
		query.setIsContentStored(false);
		query.start(1);
	}
}
