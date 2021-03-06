package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.qunar.liwei.graduation.weibo_crawler.util.LoopList;

/**
 * 用来存储所有Cookie相关的类
 * @author li-wei
 *
 */
public class CookieAbout {
	
	/**
	 * 通过HtmlUnit模拟的方式来登陆并拿到Cookie
	 * @param userID
	 * @param passwd
	 * @return
	 */
	public static Map<String,String> 
			loginAndGetCookie(String userID,String passwd){
		Map<String,String> cookiesAsMap = new LinkedHashMap<>();
		WebClient webClient = null;
		try {
			webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			HtmlPage htmlPage = webClient.getPage("http://login.weibo.cn/login/");	
			String xml = htmlPage.asXml();
			Matcher matcher = Pattern.compile("password_.*").matcher(xml);
			if (matcher.find())
				matcher = Pattern.compile("[0-9]{4}").matcher(matcher.group());
			String passwdID = null;
			if (matcher.find())
				passwdID = matcher.group();
			final List<HtmlForm> forms = htmlPage.getForms();
			final HtmlForm form = forms.get(0);
			HtmlTextInput userName = form.getInputByName("mobile");
			HtmlPasswordInput password = form.getInputByName("password_"+passwdID);
			HtmlSubmitInput submit = form.getInputByName("submit");
			userName.setValueAttribute(userID);
			password.setValueAttribute(passwd);
			htmlPage = submit.click();
			Set<Cookie> cookies = new HashSet<Cookie>() ;
			CookieManager cookieMa = webClient.getCookieManager();
			cookies = cookieMa.getCookies();
			for (Cookie cookie : cookies)
				cookiesAsMap.put(cookie.getName(), cookie.getValue());
			System.out.println("登陆账号1,获取cookie成功");
		} catch (FailingHttpStatusCodeException e) {
			System.err.println("Falt status code");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("read url fault");
		} finally {
			webClient.closeAllWindows();
		}
		return cookiesAsMap;
	}

	public static Map<String,String> getCookie3() {
		Map<String,String> cookiesAsMap = new LinkedHashMap<>();
		cookiesAsMap.put("_T_WM", "a40f711f777c779bc7dc8aa07f5bc28e");
		cookiesAsMap.put("SUB", "_2A255jrcgDeTxGeNN71QZ9yjKyz6IHXVbcNlorDV6PUJbrdBeLRb5kW0ewpAlxL9Yh2rrVXNbRnLbFWRxYw..");
		cookiesAsMap.put("gsid_CTandWM", "4utke7771As3c8ITRpWWLmqXW9I");
		return cookiesAsMap;
	}
	
	public static Map<String,String> getAnotherCookie() {
		Map<String,String> cookiesAsMap = new LinkedHashMap<>();
		cookiesAsMap.put("_T_WM", "a40f711f777c779bc7dc8aa07f5bc28e");
		cookiesAsMap.put("SUB", "_2A255j3DpDeTxGeNN6FQQ8ynPyjuIHXVbcBChrDV6PUJbrdAKLXbCkW0UCMWuPZpbi5IjvSsSpGKniAyu8g..");
		cookiesAsMap.put("gsid_CTandWM", "4umX6d5612iUjLJqsCUhjmoaJ57");
		return cookiesAsMap;
	}
	public static Map<String,String> getCookie() {
		Map<String,String> cookiesAsMap = new LinkedHashMap<>();
		cookiesAsMap.put("_T_WM", "a40f711f777c779bc7dc8aa07f5bc28e");
		cookiesAsMap.put("SUB", "_2A255mI4lDeTxGeNN6VcT9SnNzTuIHXVbYhJtrDV6PUJbrdANLXnkkW1FQN0cZwaoDZeD6LOornaSXzC6CQ..");
		cookiesAsMap.put("gsid_CTandWM", "4uzYe7771EX0Elepj34fWmllf2H");
		cookiesAsMap.put("M_WEIBOCN_PARAMS", "rl%3D1");
		return cookiesAsMap;
	}
	
	 /**
	  * return all cookies that all users'
	  * @return cookie as map's List
	  */
	 public static LoopList<Map<String,String>> getCookiesLoopList() {
         LoopList<Map<String,String>> cookiesAsMapList = new LoopList<>();
         cookiesAsMapList.add(getCookie());
         cookiesAsMapList.add(getAnotherCookie());
         cookiesAsMapList.add(getCookie3());
         return cookiesAsMapList;
	 }
	 public static List<Map<String,String>> getCookiesList() {
         List<Map<String,String>> cookiesAsMapList = new LinkedList<>();
         cookiesAsMapList.add(getCookie());
         cookiesAsMapList.add(getAnotherCookie());
         cookiesAsMapList.add(getCookie3());
         return cookiesAsMapList;
	 }
}
