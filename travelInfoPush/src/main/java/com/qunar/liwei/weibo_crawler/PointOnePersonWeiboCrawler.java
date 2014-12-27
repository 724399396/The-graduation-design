package com.qunar.liwei.weibo_crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;










import com.qunar.liwei.weibo_crawler.util.ParseString;

public class PointOnePersonWeiboCrawler {
	
	private static BlockingQueue<String> mainPersonUrl
		= new LinkedBlockingQueue<>(1000);
	private static BlockingQueue<String> subPersonUrl 
		= new LinkedBlockingQueue<>(1000);
		
	private static String cookieString = 
			HtmlUnitTest.loginAndGetCookie(
					"181212631@163.com","wwee13" );;
	private static AtomicReference<String> mainName = new AtomicReference<String>();
	private static String maxTime;
	private static String minTime;
	private static AtomicLong id = new AtomicLong();
	
	private static class MainPersonCrawler implements Runnable {
		public void run() {
			while(!Thread.interrupted()) {
				try{
					System.out.println("主线程" + id.incrementAndGet());
					String url = mainPersonUrl.take();
					int pageNum = Integer.parseInt(
							url.substring(url.lastIndexOf("=") + 1));
					if (pageNum % 100 == 0) {
						System.out.println(url);
						TimeUnit.MINUTES.sleep(5);
					}
					System.out.println("fetch: " + url);
					String bodyAsString = getResponse(url);
					processHtmlMain(bodyAsString);
				} catch (InterruptedException e) {
					System.err.println("线程终止");
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("读取网页错误");
					e.printStackTrace();
				}
			
			}
		}
	}
	
	private static class SubPersonCrawler implements Runnable {
		public void run() {
			while(!Thread.interrupted()) {
				try{
					System.out.println("辅线程" + id.incrementAndGet());
					String url = subPersonUrl.take();
					int pageNum = Integer.parseInt(
							url.substring(url.lastIndexOf("=") + 1));
					if (pageNum % 10 == 0) {
						System.out.println(url);
						TimeUnit.MINUTES.sleep(2);
					}
					System.out.println("fetchs: " + url);
					String bodyAsString = getResponse(url);
					processHtmlSub(bodyAsString);
				} catch (InterruptedException e) {
					System.err.println("线程终止");
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("读取网页错误");
					e.printStackTrace();
				}	
			}
		}
	}
	
	private static String getResponse(String url) 
			throws ClientProtocolException, IOException {
		CloseableHttpClient client = 
				HttpClientBuilder.create().build();
//		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(200)
//				.setConnectTimeout(200).setSocketTimeout(200).build();
		HttpGet httpGet = new HttpGet(url);
//		httpGet.setConfig(config);
		httpGet.setHeader("Cookie", cookieString);
		httpGet.setHeader(
				 "User-Agent",
				 "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");
		CloseableHttpResponse response = client.execute(httpGet);
		return EntityUtils.toString(response.getEntity());
	}
	private static void processHtmlMain(String body) {
    	Matcher titleMatcher = Pattern.compile("<title>([^0x00-0xff]+)的微博</title>")
    			.matcher(body);
    	if (mainName.get() ==  null && titleMatcher.find()) {
    		mainName.set(titleMatcher.group(1));
		}
    	Pattern imagePattern = Pattern.compile("\"([\\d\\w\\S]+)u=([\\d\\w\\S]+)\">原图</a>");
    	Matcher imageMatcher = imagePattern.matcher(body);
		while (imageMatcher.find()) {
			DataAbout.saveImageUrl("http://ww4.sinaimg.cn/large/"+ imageMatcher.group(2)+".jpg");
//    		Calendar cal = Calendar.getInstance();
//    		fetchImageAndStoreInDisk("http://ww4.sinaimg.cn/large/"+ matcher.group(2)+".jpg", 
//    				"./src/resource/download/" + name + "/" + (cal.get(Calendar.MONTH)+ 1) 
//    				+ "-" + Calendar.DAY_OF_MONTH);
		}
    	Matcher divMatcher = Pattern.compile("<div class=\"c\" id=\"M_[0-9a-zA-Z]+\">(.+?)<div class")
    			.matcher(body);
    	Pattern infoPattern = Pattern.compile(">([^<>]+?)<");
    	while(divMatcher.find()) {
    		Matcher infoMatcher = infoPattern.matcher(divMatcher.group(1));
    		String info = "";
    		while (infoMatcher.find()) {
	    		info += infoMatcher.group(1);   	
    		}
    		info = info.replaceAll("&nbsp;", " ");
    		int timeStartIndex = info.lastIndexOf("收藏");
    		int timeEndIndex = info.lastIndexOf("来自");
    		String time = info.substring(
    				timeStartIndex + 3, timeEndIndex-1);
    		time = ParseString.processTime(time);
    		if (minTime != null && maxTime != null 
    				&& time.compareTo(maxTime) <= 0 && time.compareTo(minTime) >= 0) {
    			System.out.println("返回");
    			break;   	
    			
    		}
    		parseWeiboInfoAndStore(mainName.get(),info, time, true);
        }
	}
	
	private static void processHtmlSub(String body) {
    	Matcher titleMatcher = Pattern.compile("<title>([^0x00-0xff]+)的微博</title>")
    			.matcher(body);
    	String name = null;
    	if (name == null && titleMatcher.find()) {
    		name = titleMatcher.group(1);
		}
    	Pattern imagePattern = Pattern.compile("\"([\\d\\w\\S]+)u=([\\d\\w\\S]+)\">原图</a>");
    	Matcher imageMatcher = imagePattern.matcher(body);
		while (imageMatcher.find()) {
			DataAbout.saveImageUrl("http://ww4.sinaimg.cn/large/"+ imageMatcher.group(2)+".jpg");
//    		Calendar cal = Calendar.getInstance();
//    		fetchImageAndStoreInDisk("http://ww4.sinaimg.cn/large/"+ matcher.group(2)+".jpg", 
//    				"./src/resource/download/" + name + "/" + (cal.get(Calendar.MONTH)+ 1) 
//    				+ "-" + Calendar.DAY_OF_MONTH);
		}
    	Matcher divMatcher = Pattern.compile("<div class=\"c\" id=\"M_[0-9a-zA-Z]+\">(.+?)<div class")
    			.matcher(body);
    	Pattern infoPattern = Pattern.compile(">([^<>]+?)<");
    	while(divMatcher.find()) {
    		Matcher infoMatcher = infoPattern.matcher(divMatcher.group(1));
    		String info = "";
    		while (infoMatcher.find()) {
	    		info += infoMatcher.group(1);   	
    		}
    		info = info.replaceAll("&nbsp;", " ");
    		int timeStartIndex = info.lastIndexOf("收藏");
    		int timeEndIndex = info.lastIndexOf("来自");
    		String time = info.substring(
    				timeStartIndex + 3, timeEndIndex-1);
    		time = ParseString.processTime(time);
    		if (minTime != null && maxTime != null 
    				&& time.compareTo(maxTime) <= 0 && time.compareTo(minTime) >= 0)
    			return;      	
    		parseWeiboInfoAndStore(name,info, time, false);
        }
	}
	private static void parseWeiboInfoAndStore(String name,
			String text, String time, boolean isMain) {
		if (text.contains("抱歉，此微博已被作者删除"))
			return;
		if (text.contains("此微博已被删"))
			return;
		int fromIndex = text.lastIndexOf(" 的微博");
    	if (text.contains("转发了 ") && fromIndex > 1) {
    		String from = text.substring(3, fromIndex);
    		if (isMain)
    			DataAbout.insertMain(name, text, "转发", from, time);
    		else {
    			String mainNameStr;
    			while ((mainNameStr = mainName.get()) == null)
    				;
    			DataAbout.insertSub(name, text, "转发", from, time, mainNameStr);
    		}
    	} else {
    		if (isMain)
    			DataAbout.insertMain(name, text, "原创", null, time);
    		else {
    			String mainNameStr;
    			while ((mainNameStr = mainName.get()) == null)
    				;
    			DataAbout.insertSub(name, text, "原创", null, time, mainNameStr);
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
		        out.close();
	    	} catch (FileNotFoundException e) {
	    		e.printStackTrace();
	    	} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	

	private static void parsePage(String url) 
			throws ClientProtocolException, IOException, InterruptedException {
		String follow = getResponse(url);
		Matcher matcher = Pattern.compile("<a href=\"([\\S]+)\">[\\S]+</a><img src").matcher(follow);
		while (matcher.find())
			pushToFetchList(subPersonUrl, matcher.group(1), 1);
		matcher = Pattern.compile("<a href=\"([\\S]+)\">[\\S]+</a><br/>粉丝").matcher(follow);
		while (matcher.find())
			pushToFetchList(subPersonUrl, matcher.group(1), 10);
	}
	
	private static void pushToFetchList(
			BlockingQueue<String> queue,String url, int pageTotal) {
		try {
			for (int pageIndex = 215; pageIndex <= pageTotal; pageIndex++)
				queue.put(url + "?page=" +pageIndex);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		maxTime = DataAbout.getMaxTime();
		minTime = DataAbout.getMinTime();
		System.out.println(maxTime + "-" + minTime);
		mainName.set("梁斌penny");
		ExecutorService exec = Executors.newFixedThreadPool(8);
		for (int i = 0; i < 1; i++)	
			exec.execute(new MainPersonCrawler());
		pushToFetchList(mainPersonUrl, "http://weibo.cn/pennyliang", 1000);
		while(mainName.get() == null)
			;
		for (int i = 0; i < 2; i++)
			exec.execute(new SubPersonCrawler());
		for (int followIndex = 1; followIndex <= 20; followIndex++)
			parsePage("http://weibo.cn/1497035431/follow?page=" + followIndex);
		while(!(subPersonUrl.isEmpty() && mainPersonUrl.isEmpty()))
			;
		exec.shutdown();
	}
}
