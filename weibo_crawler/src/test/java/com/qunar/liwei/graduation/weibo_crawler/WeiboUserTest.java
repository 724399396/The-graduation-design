package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;

import org.junit.Test;

public class WeiboUserTest {
	@Test
	public void testCreateMainUser() throws IOException {
		WeiboUser weiboUser = WeiboUser.
				newMainInstance("http://weibo.cn/pennyliang");
		System.out.println(weiboUser);
	}
	
	@Test
	public void testCreateSubUser() throws IOException {
		WeiboUser weiboUser = WeiboUser.
					newFollowInstance("http://weibo.cn/u/5182575519");
		System.out.println(weiboUser);
	}
}
