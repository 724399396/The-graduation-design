package com.qunar.liwei.weibo_crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.select.Elements;

import com.qunar.liwei.weibo_crawler.util.ParseString;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.crawler.Crawler;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.parser.HtmlParser;
import cn.edu.hfut.dmic.webcollector.parser.Parser;
import cn.edu.hfut.dmic.webcollector.util.Config;

public class WeiboCrawler extends BreadthCrawler {
	private static String cookieString = HtmlUnitTest.loginAndGetCookie(
			"181212631@163.com","wwee13" );
	private static AtomicLong id = new AtomicLong();
    @Override
    public void visit(Page page) {
    	String html = page.getHtml();
    	String name = "";
    	Matcher matcher = Pattern.compile("<title>([^0x00-0xff]+)的微博</title>").matcher(html);
    	if (matcher.find())
    		name = matcher.group(1);
    	if (!name.equals("梁斌penny"))
    		return;
    	matcher = Pattern.compile("\"([\\d\\w\\S]+)u=([\\d\\w\\S]+)\">原图</a>").matcher(html);
    	while (matcher.find()) {
    		Calendar cal = Calendar.getInstance();
//    		fetchImageAndStoreInDisk("http://ww4.sinaimg.cn/large/"+ matcher.group(2)+".jpg", 
//    				"./src/resource/download/" + name + "/" + (cal.get(Calendar.MONTH)+ 1) 
//    				+ "-" + Calendar.DAY_OF_MONTH);
    		
    	}
    	Elements divs = page.getDoc().select("div.c");
        for (org.jsoup.nodes.Element div : divs) {
	    	if(div.id().contains("M_")) {
	    		String info = div.text();
	    		System.out.println(info);
	    		parseWeiboInfoAndStore(name,info);
	    	}
        }
    }
    
    private void fetchImageAndStoreInDisk(String imageUrl, String storePath) {
    	try {
    	URL url = new URL(imageUrl);
    	File file = new File(storePath);
    	boolean fileNotExist = !file.exists();
    	boolean mkdirFalse = true;
    	if (fileNotExist)
    		mkdirFalse = !file.mkdirs();
    	if (fileNotExist && mkdirFalse)
    		throw new RuntimeException("mkdirs false");
    	OutputStream out =new FileOutputStream(new File(file.getAbsolutePath()
    			+ "/"+id.incrementAndGet()+".jpg"));
    	InputStream in = url.openStream();
    	byte[] buffer = new byte[1024];
    	int len = 0;    
        while ((len = in.read(buffer)) != -1) {    
            out.write(buffer, 0, len);    
        }    
    	} catch (FileNotFoundException e) {
    		e.printStackTrace();
    	} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private static void parseWeiboInfoAndStore(String name,String text) {
    	int timeStartIndex = text.indexOf("收藏");
		int timeEndIndex = text.indexOf("来自");
		String time = text.substring(
				timeStartIndex + 3, timeEndIndex-1);
		time = ParseString.processTime(time);
		System.out.println(time);
    	if (text.contains("转发了")) {
    		int fromIndex = text.indexOf("的微博");
    		String from = text.substring(3, fromIndex);   		
    		DataAbout.insertMain(name, text, "转发", from, time);
    	} else {
    		DataAbout.insertMain(name, text, "原创", null, time);
    	}
    }


    public static void main(String[] args) throws IOException, Exception {
        Config.topN = 1;
        WeiboCrawler crawler=new WeiboCrawler();
        crawler.setCookie(cookieString);
        for (int i = 1; i <= 2000; i++) {
        	//http://weibo.cn/pennyliang?page=
        	//http://weibo.cn/search/mblog?hideSearchFrame=&keyword=%E6%97%85%E6%B8%B8&page=1&vt=4
            crawler.addSeed("http://weibo.cn/pennyliang?page="+i);
        }
	    crawler.addRegex("+^http://weibo.cn/.*");
        crawler.setThreads(8);
        crawler.start(1);

    }
}
