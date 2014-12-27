package com.qunar.liwei.graduation.weibo_crawler;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.qunar.liwei.graduation.weibo_crawler.util.ParseTime2Timestamp;

public class DataManagerTest {
	DataManager dataManager;
	
	
	@Before
	public void testCreateSession() throws IOException{
		dataManager = new DataManager();
		assertNotNull(dataManager);
	}
	
	//@Test
	public void testSaveWeibo() {
		dataManager = new DataManager();
		int insertNums = dataManager.saveWeibo(
				new Weibo("梁斌penny", "最近总上围脖，不发表什么，也会看看名人们言论。也发现，以前天天上的校内，很少上了，也只有登录一些连接网站的时候，才想起来，我还有个校内账号，可以使用。", 
						"转发", "张成_ICT", "回复@人民搜索-张成:校内急需创新啊 //@人民搜索-张成:回复@pennyliang_梁斌:是啊，校内还弄了个登录奖励的机制，可惜没有吸引力了。 ", 
						ParseTime2Timestamp.parseTimestamp("2010-08-23 10:23:46"), 
						null));
		System.out.println(insertNums);
	}
	//@Test
	public void testAddFollow() {
	}
	
	//@Test
	public void testSaveFollow() throws IOException {
		Set<String> set = new LinkedHashSet<>();
		set.add("http://weibo.cn/fanservice");
		int insertNums = dataManager.saveFollow(
				new WeiboUser("http://weibo.cn/pennyliang", null, "梁斌penny", 
						"[李为,微博]", set));
		System.out.println(insertNums);
	}
	//@Test
	public void testIsWeiboExist() {
		dataManager = new DataManager();
		boolean exists = dataManager.isWeiboExist(new Weibo("梁斌penny", "最近总上围脖，不发表什么，也会看看名人们言论。也发现，以前天天上的校内，很少上了，也只有登录一些连接网站的时候，才想起来，我还有个校内账号，可以使用。", 
						"转发", "张成_ICT", "回复@人民搜索-张成:校内急需创新啊 //@人民搜索-张成:回复@pennyliang_梁斌:是啊，校内还弄了个登录奖励的机制，可惜没有吸引力了。 ", 
						ParseTime2Timestamp.parseTimestamp("2010-08-23 10:23:46"), 
						null));
		assertThat(exists, is(true));
	}
	
	
	@Test
	public void testGetMinDate() {
		for (int i = 0; i < 10; i++)
			new Thread(new Runnable() {
				@Override
				public void run() {
					Timestamp minDate = dataManager.getMinDate("林芝旅游局");
					System.out.println(minDate);
					Timestamp maxDate = dataManager.getMaxDate("林芝旅游局");
					System.out.println(maxDate);
				}
			}).start();;
		Timestamp minDate = dataManager.getMinDate("林芝旅游局");
		System.out.println(minDate);
		Timestamp maxDate = dataManager.getMaxDate("林芝旅游局");
		System.out.println(maxDate);
	}
	
	//@Test
	public void testGetMaxDate() {
		Timestamp maxDate = dataManager.getMaxDate("梁斌penny");
		System.out.println(maxDate);
	}
	
}
