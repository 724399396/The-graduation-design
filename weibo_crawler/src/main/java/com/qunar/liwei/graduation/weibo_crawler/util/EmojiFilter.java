package com.qunar.liwei.graduation.weibo_crawler.util;

public class EmojiFilter {
	/**
	 * special characters regular expression
	 */
	private static final String pattern =
		  "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\ud84c\udfb4-\ud869\udea5]|[\u0e2a\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49\u0e49]";
  /**
   *
   * Remove the special characters
   * @param resouce
   * @return the string already remove special characters
   */
  public static String emojiFilt(String resouce) {
          String result = resouce.replaceAll(pattern, "");
          return result;
  }
}
