package com.qunar.liwei.graduation.forwarding_analyze;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.qunar.liwei.graduation.forwarding_analyze.util.LoopList;


/**
 * 用来存储所有Cookie相关的类
 * @author li-wei
 *
 */
public class CookieAbout {
		
	public static Map<String,String> getCookie3() {
		Map<String,String> cookiesAsMap = new LinkedHashMap<>();
		cookiesAsMap.put("_T_WM", "a40f711f777c779bc7dc8aa07f5bc28e");
		cookiesAsMap.put("SUB", "_2A255jrcgDeTxGeNN71QZ9yjKyz6IHXVbcNlorDV6PUJbrdBeLRb5kW0ewpAlxL9Yh2rrVXNbRnLbFWRxYw..");
		cookiesAsMap.put("gsid_CTandWM", "4utke7771As3c8ITRpWWLmqXW9I");
		return cookiesAsMap;
	}
	
	public static Map<String,String> getAnotherCookie() {
		Map<String,String> cookiesAsMap = new LinkedHashMap<>();
		cookiesAsMap.put("_T_WM", "a40f711f777c779bc7dc8aa07f5bc28e");
		cookiesAsMap.put("SUB", "_2A255j3DpDeTxGeNN6FQQ8ynPyjuIHXVbcBChrDV6PUJbrdAKLXbCkW0UCMWuPZpbi5IjvSsSpGKniAyu8g..");
		cookiesAsMap.put("gsid_CTandWM", "4umX6d5612iUjLJqsCUhjmoaJ57");
		return cookiesAsMap;
	}
	public static Map<String,String> getCookie() {
		Map<String,String> cookiesAsMap = new LinkedHashMap<>();
		cookiesAsMap.put("_T_WM", "a40f711f777c779bc7dc8aa07f5bc28e");
		cookiesAsMap.put("SUB", "_2A255niQUDeTxGeNN6VcT9SnNzTuIHXVbYUxcrDV6PUJbrdBeLU_AkW1wvcrt_GsjMDv6PMI9BzD33Ry9NQ");
		cookiesAsMap.put("gsid_CTandWM", "4uzYe7771EX0Elepj34fWmllf2H");
		cookiesAsMap.put("M_WEIBOCN_PARAMS", "rl%3D1");
		return cookiesAsMap;
	}
	
	 /**
	  * return all cookies that all users'
	  * @return cookie as map's List
	  */
	 public static LoopList<Map<String,String>> getCookiesLoopList() {
         LoopList<Map<String,String>> cookiesAsMapList = new LoopList<>();
         cookiesAsMapList.add(getAnotherCookie());
         cookiesAsMapList.add(getCookie());
         cookiesAsMapList.add(getCookie3());
         return cookiesAsMapList;
	 }
	 public static List<Map<String,String>> getCookiesList() {
         List<Map<String,String>> cookiesAsMapList = new LinkedList<>();
         cookiesAsMapList.add(getCookie());
         cookiesAsMapList.add(getAnotherCookie());
         cookiesAsMapList.add(getCookie3());
         return cookiesAsMapList;
	 }
}
