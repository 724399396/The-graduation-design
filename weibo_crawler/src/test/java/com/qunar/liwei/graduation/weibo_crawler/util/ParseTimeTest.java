package com.qunar.liwei.graduation.weibo_crawler.util;

import org.junit.Test;

public class ParseTimeTest {
	@Test
	public void testOnMinitue() {
		String time = "1分钟前";
		System.out.println(ParseTime2Timestamp.parseTimestamp(
				ParseTime2String.processTime(time)));
	}
	
	@Test
	public void testOnDay() {
		String time = "今天 09:17";
		System.out.println(ParseTime2Timestamp.parseTimestamp(
				ParseTime2String.processTime(time)));
	}
	
	@Test
	public void testOnMonth() {
		String time = "09月26日 13:25";
		System.out.println(ParseTime2Timestamp.parseTimestamp(
				ParseTime2String.processTime(time)));
	}
	
	@Test 
	public void testOnYear() {
		String time = "2010-08-17 09:45:20";
		System.out.println(ParseTime2Timestamp.parseTimestamp(
				ParseTime2String.processTime(time)));
	}
}
