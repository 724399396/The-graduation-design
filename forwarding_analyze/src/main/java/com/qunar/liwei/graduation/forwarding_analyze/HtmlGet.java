package com.qunar.liwei.graduation.forwarding_analyze;

import java.io.IOException;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;

import com.qunar.liwei.graduation.forwarding_analyze.util.LogHelper;
import com.qunar.liwei.graduation.forwarding_analyze.util.LoopList;

public class HtmlGet {
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
		    	count.incrementAndGet();
	     		//System.out.println(Thread.currentThread().getId() + ":" + count.incrementAndGet());
		        if (count.get() % 50 == 0){
		                count.set(0);
		                System.out.println("休眠 ,刚才用的:" + cookie );
		                cookie = cookiesAsMapList.next();
		                TimeUnit.SECONDS.sleep(30);
		                System.out.println("休眠结束");
		        }
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
	
	static Connection searchConn = Jsoup.connect("http://weibo.cn/search/")
            .cookies(CookieAbout.getAnotherCookie())
            .userAgent("Mozilla").method(Method.POST);
	public static synchronized Document search(String keyword, String user, String type) {
     	searchConn.data("advancedfilter", "1")
         .data("keyword", keyword)
         .data("nick", user == null ? "" : user)
         .data("starttime","")
         .data("endtime", "20141223")
         .data("sort", "hot")
         .data("smblog", "搜索");
     	String url = "http://weibo.cn/search/mblog?hideSearchFrame=&"
     			+ "keyword="+ keyword + 
     			"&advancedfilter=1"
     			+ (user == null ? "" : "&nick=回首向来萧瑟出")
     			+ "&endtime=20141226"
     			+ "&sort=" + type;
     	Document doc = null;
     	while (true) {
	     	try {	
	     		count.incrementAndGet();
	     		//System.out.println(Thread.currentThread().getId() + ":" + count.incrementAndGet());
	 	        if (count.get() % 50 == 0){
	 	                count.set(0);
	 	                System.out.println("休眠 ,刚才用的:" + cookie );
	 	                cookie = cookiesAsMapList.next();
	 	                TimeUnit.SECONDS.sleep(30);
	 	                System.out.println("休眠结束");
	 	        }
				//doc = searchConn.post();
	 	        doc = Jsoup.connect(url)
		    			.header("Accept", "text/html")
		                .userAgent("Mozilla")
		                .cookies(cookie)
		                .timeout(4000)
		                .get();
				break;
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} catch (Throwable t) {
				
			}
     	}
     	return doc;
     }
}
