package com.qunar.liwei.graduation.weibo_crawler;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DataManagerTest {
	DataManager dataManager;
	
	
	@Before
	@SuppressWarnings("static-access")
	public void testCreateSession() throws IOException{
		dataManager = new DataManager();
		assertNotNull(dataManager.session);
	}
	
	//@Test
	public void testAddWeibo() {
		int insertNums = dataManager.saveWeibo(new Weibo(
			"李为", "测试", "原创", null, "2014-12-9 15:00", 
			"http://ww2.sinaimg.cn/large/928fc4ddjw1en3fcwcxd8j21hc0u07e8.jpg"
			, "梁斌"));
		assertThat(insertNums, is(1));
	}
	//@Test
	public void testAddFollow() {
		int insertNums = dataManager.saveWeibo(new Weibo(
				"李为", "测试", "原创", null, "2014-12-9 15:00", 
				"http://ww2.sinaimg.cn/large/928fc4ddjw1en3fcwcxd8j21hc0u07e8.jpg"
				, "梁斌"));
		assertThat(insertNums, is(1));
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
	public void testGetMinDate() {
		String minDate = dataManager.getMinDate("梁斌penny");
		assertThat(minDate, is("2014-02-07 18:57"));
	}
	
	//@Test
	public void testGetMaxDate() {
		String maxDate = dataManager.getMaxDate("梁斌penny");
		assertThat(maxDate, is("2014-12-08 20:52"));
	}
	
	@Test
	public void testIsWeiboExist() {
		WeiboUser user = new WeiboUser("http://weibo.cn/pennyliang", null, "巨鹿曹阳", null, null);
		Weibo weibo = new Weibo("巨鹿曹阳", 
				"【阿里巴巴HR干货分享】721理论：员工70%依靠自学获取知识，20%依靠培训获取知识，10%依靠COACH等方式；借人成事，借事炼人；业绩是假，团队是真；团队是假，个人提升是真。为什么做，比做什么、怎么做更重要！平台、机制和氛围不是规划出来的，而是生长出来的..http://t.cn/zYNRurT ", "转发", 
				"中国电商行业网", "2013-02-24 13:09:56", "http://ww2.sinaimg.cn/large/=57159b85jw1e232w7htorj.jpg", null);
		boolean isNew = user.weiboIsNew(weibo);
		System.out.println(isNew);
	}
	
	//@Test
	public void testInserEmoji() {

		Weibo weibo = new Weibo("梁斌" ,"微博招亲啦[喜]：乐乐，泰迪贵宾，女，四岁半。有车[小汽车]有房🏡，[钱]有存款。家里的掌上明珠，大家闺秀，多才多艺，性格温顺可人！经过家庭会议👪，打算给乐乐招名：[男孩]英俊潇洒，体型稍微偏小，聪明伶俐，大眼睛毛色棕红色的帅哥一枚！[相爱]完成人生大事，升级做妈妈！限于北京的宝贝们[狗] "
		,"转发", "转发", "2014-04-19 11:56",null, null);
		dataManager.saveWeibo(weibo);
	}
	
}
