package com.qunar.liwei.weibo_crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

public class HtmlUnitTest {
	public static Cookie cookie;
	public static String loginAndGetCookie(String userID,String passwd){
		String cookieString = "";
		try {
			final WebClient webClient = new WebClient(BrowserVersion.CHROME);
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
			// "_T_WM=42694920c57d89526c572509877bff1b; SUB=_2AkMjMeSxa8NlrAZSmv0XyWzjaopH-jyQbfRHAn7oJhMyCBh77ldWqSeQ6_I4xZYUTcHJ1rzV6tCMgiTCoQ..	
			Set<Cookie> cookies = new HashSet<Cookie>() ;
			CookieManager cookieMa = webClient.getCookieManager();
			List<String> cookieName = new ArrayList<String>();
			cookieName.add("_T_WM");
			cookieName.add("SUB");
			cookieName.add("gsid_CTandWM");
			for (String str : cookieName) {
				cookie = cookieMa.getCookie(str);
				cookieString += str + "=" +cookie.getValue() + ";";	
			}
			cookieString=cookieString.substring(0, cookieString.length()-1);
			System.out.println(cookieString);
			webClient.closeAllWindows();			
		} catch (FailingHttpStatusCodeException e) {
			System.out.println("Login false");
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException");
		} catch (IOException e) {
			System.out.println("read url fault");
		}
		return cookieString;
	}

	public static void search(String url) {
		loginAndGetCookie("181212631@163.com","wwee13" );
		try {
			final WebClient webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setJavaScriptEnabled(false);
			webClient.getCookieManager().addCookie(cookie);
			HtmlPage htmlPage = webClient.getPage(url);	
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
			userName.setValueAttribute("181212631@163.com");
			password.setValueAttribute("wwee13");
			htmlPage = submit.click();
			webClient.getCookieManager().getCookie("gsid_CTandWM").getValue();
			webClient.closeAllWindows();			
		} catch (FailingHttpStatusCodeException e) {
			System.out.println("Login false");
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException");
		} catch (IOException e) {
			System.out.println("read url fault");
		}
	}
	
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		loginAndGetCookie("181212631@163.com","wwee13" );
	}
}
