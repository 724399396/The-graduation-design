package com.qunar.liwei.graduation.weibo_crawler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.qunar.liwei.graduation.weibo_crawler.util.EmojiFilter;
import com.qunar.liwei.graduation.weibo_crawler.util.HtmlPageParse;
import com.qunar.liwei.graduation.weibo_crawler.util.Login;
import com.qunar.liwei.graduation.weibo_crawler.util.LoopList;

public class Test {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException, InterruptedException {

		ObjectInputStream in = new ObjectInputStream(
				new FileInputStream("users.out"));
		List<WeiboUser> users =  (List<WeiboUser>) in.readObject();
		in.close();
		Writer out = new OutputStreamWriter(
				new FileOutputStream("all.txt"));
		String result = "";
		for (WeiboUser weiboUser : users)
			result += weiboUser.getName() + "\r\n";
		out.write(users.size() + "\r\n" + result);
		out.close();
//		int endIndex;
//		String from = null;
//		String type;
//		if ((text.indexOf("转发了") == 0) 
//				&&(endIndex = text.indexOf("的微博")) > 0) {
//			from = text.substring(4, endIndex - 1);
//			type = "转发";
//			System.out.println("有");
//		}
//		System.out.println(from);
		//String text = "＂微博招亲啦[喜]＂：乐乐，泰迪贵宾，女，四岁半。有车[小汽车]有房🏡，[钱]有存款。家里的掌上明珠，大家闺秀，多才多艺，性格温顺可人！经过家庭会议👪，打算给乐乐招名：[男孩]英俊潇洒，体型稍微偏小，聪明伶俐，大眼睛毛色棕红色的帅哥一枚！[相爱]完成人生大事，升级做妈妈！限于北京的宝贝们[狗] ";
//		int pageNums = 1000;
//		pageNums = pageNums > 100 ? 100 : pageNums;
//		System.out.println(pageNums);
		//System.out.println(isNew());
//		String emoji = "我发表了博客：《字符编码的那些事——原来C#、Java的一个char并不是对应一个显示字符》。讲述了C#中一个char存不下的“𪚥”字，以及排版破坏神器“ส้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้้”的形成，也介绍了怎么用C#来处理这些奇葩。http://t.cn/zTUzve5";
//		System.out.println(EmojiFilter.emojiFilt(emoji));
//		LoopList<Map<String,String>> cookiesAsMapList = new LoopList<>();
//		cookiesAsMapList.add(Login.loginAndGetCookie("181212631@163.com", "wwee13"));
//		cookiesAsMapList.add(Login.getCookie());
//		cookiesAsMapList.add(Login.getAnotherCookie());
//
//		int cookieIndex = 0;
//		
//		for(cookieIndex = 0; cookieIndex < 6; cookieIndex++) {
//			Map<String,String> cookie = cookiesAsMapList.next();
//			Document doc = Jsoup.connect("http://weibo.cn/pennyliang?page=10"+cookieIndex)
//					  .userAgent("Mozilla")
//					  .cookies(cookie)
//					  .timeout(20000)
//					  .get();
//			System.out.println(cookie);
//			System.out.println(doc.text());
//		}
//		LoopList<Integer> cookiesOfDiffUsers
//			= new LoopList<>();
//		cookiesOfDiffUsers.add(1);
//		cookiesOfDiffUsers.add(2);
//		cookiesOfDiffUsers.add(3);
//		while(true) {
//			System.out.println(cookiesOfDiffUsers.next());
//		}
		
	}
	
	private static boolean isNew(){
		DataManager dataManager = new DataManager();
		Weibo weibo = new Weibo("杜雪骞", "【A8音乐欲发行新股融资2.7亿元】A8音乐今日公告称，建议通过供股方式融资3.4亿港元(折合人民币2.7亿元)，用于偿还集团银行贷款、A8大厦的建设以及一般营运资金。供股(Rights Issue)又称附加股，是上市公司发行新股票让现有股东按持股比例认购的一种融资方式。http://t.cn/zjmMjUY 赞[1] 原文转发[19] 原文评论[6] 转发理由:[失望] ", "原创", null, "2013-01-07 11:45:53"
				, null, null);
		String name = weibo.getUserName();
		String date = weibo.getTime();
		String minDate = null;
		String maxDate = null;
		if (minDate == null || maxDate == null) {
			maxDate = dataManager.getMaxDate(name);
			minDate = dataManager.getMinDate(name);			
		}

		System.out.println(name +  ":" + date);
		System.out.println(minDate + "-" + maxDate);
		if (minDate == null && maxDate == null) {
			return true;
		}
		if (maxDate == null && minDate != null) {
			maxDate = minDate;
		}
		if (date.compareTo(minDate) < 0 || date.compareTo(maxDate) > 0) {
			return true;
		} else {
			if (dataManager.isWeiboExist(weibo)) {
				return false;
			} else {
				return true;
			}
		}
	}
}
