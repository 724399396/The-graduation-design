package com.qunar.liwei.graduation.weibo_crawler;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
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
			"http://ww2.sinaimg.cn/large/928fc4ddjw1en3fcwcxd8j21hc0u07e8.jpg"));
		assertThat(insertNums, is(1));
	}
	
	//@Test
	public void testAddImage() {
		int insertNums = dataManager.saveImage("www.image.com");
		assertThat(insertNums, is(1));
	}
	
	@Test
	public void testGetMinDate() {
		String minDate = dataManager.getMinDate("梁斌penny");
		assertThat(minDate, is("2014-02-07 18:57"));
	}
	
	@Test
	public void testGetMaxDate() {
		String maxDate = dataManager.getMaxDate("梁斌penny");
		assertThat(maxDate, is("2014-12-08 20:52"));
	}
	
}
