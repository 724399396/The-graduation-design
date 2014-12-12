package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.qunar.liwei.graduation.weibo_crawler.util.HtmlPageParse;

public class HtmlPageParseTest {
	//@Test
	public void testWeiboList() throws IOException {
		List<Weibo> weiboList = HtmlPageParse.
				getWeiboList("http://weibo.cn/pennyliang");
		System.out.println(weiboList);
		
	}
	
	@Test
	public void parseMain() throws IOException {
	}
}
