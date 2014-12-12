package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import com.qunar.liwei.graduation.weibo_crawler.util.Login;

public class LoginTest {
	
	@Test
	public void testLogin() throws IOException {
		Document doc = Jsoup.connect("http://weibo.cn/pennyliang?page=10")
				  .userAgent("Mozilla")
				  .cookies(Login.getAnotherCookie())
				  .timeout(20000)
				  .get();
		System.out.println(doc.text());
	}
}
