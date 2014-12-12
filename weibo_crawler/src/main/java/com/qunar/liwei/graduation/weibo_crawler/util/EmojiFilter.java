package com.qunar.liwei.graduation.weibo_crawler.util;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;


public class EmojiFilter {
	public static String emojiFilt(String resouce) {
//		Pattern emoji = Pattern.compile (
//		         "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]" ,
//		         Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE ) ;
//		String result = "";
//		Matcher matcher = emoji.matcher(resouce);
//		while (matcher.find())
//			result += matcher.group();
		String result = resouce.replaceAll("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\ud84c\udfb4-\ud869\udea5]|[\u0e2a\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49]"
					, "");
		return result;
	}
}
