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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.qunar.liwei.graduation.weibo_crawler.util.EmojiFilter;
import com.qunar.liwei.graduation.weibo_crawler.util.LoopList;

@SuppressWarnings("unused")
public class TestWillDele {
        @SuppressWarnings("unchecked")
        public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

                ObjectInputStream in = new ObjectInputStream(
                                new FileInputStream("users.out"));
            	CopyOnWriteArraySet<String> fetchedUrl = (CopyOnWriteArraySet<String>) in.readObject();
        		//System.out.println(fetchedUrl);
                BlockingQueue<String> fetchingUrl = (BlockingQueue<String>) in.readObject();
                //System.out.println(fetchingUrl);
                System.out.println(fetchingUrl.isEmpty());
                BlockingQueue<WeiboUser> users =  (BlockingQueue<WeiboUser>) in.readObject();
                System.out.println(users);
                System.out.println(users.poll().isFinishCrawler());
                in.close();
                Writer out = new OutputStreamWriter(
                                new FileOutputStream("all.txt"));
                String result = "";
                for (WeiboUser weiboUser : users)
                        result += weiboUser.getName() + "\r\n";
                out.write(users.size() + "\r\n" + result);
                out.close();
                ScheduledExecutorService sExec = Executors.newScheduledThreadPool(1);
                sExec.scheduleAtFixedRate(new Runnable() {
					@Override
					public void run() {
						System.out.println("Hi!");
					}
				}, 0, 5, TimeUnit.SECONDS);
                try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                sExec.shutdownNow();
//              int endIndex;
//              String from = null;
//              String type;
//              if ((text.indexOf("转发了") == 0)
//                              &&(endIndex = text.indexOf("的微博")) > 0) {
//                      from = text.substring(4, endIndex - 1);
//                      type = "转发";
//                      System.out.println("有");
//              }
//              System.out.println(from);
                //String text = "＂微博招亲啦[喜]＂：乐乐，泰迪贵宾，女，四岁半。有车[小汽车//              
          //      int pageN= 1000;
//              pageNums = pageNums > 100 ? 100 : pageNums;
//              System.out.println(pageNums);
                //System.out.println(isNew());
//              String emoji = "我发表了博客：《字符编码的那些事——原来C#、Java的一个char并不//              
// System.ourintln(EmojiFilter.emojiFilt(emoji));
//              LoopList<Map<String,String>> cookiesAsMapList = new LoopList<>();
//              cookiesAsMapList.add(Login.loginAndGetCookie("181212631@163.com", "wwee13"));
//              cookiesAsMapList.add(Login.getCookie());
//              cookiesAsMapList.add(Login.getAnotherCookie());
//
//              int cookieIndex = 0;
//
//              for(cookieIndex = 0; cookieIndex < 6; cookieIndex++) {
//                      Map<String,String> cookie = cookiesAsMapList.next();
//                      Document doc = Jsoup.connect("http://weibo.cn/pennyliang?page=10"+cookieIndex)
//                                        .userAgent("Mozilla")
//                                        .cookies(cookie)
//                                        .timeout(20000)
//                                        .get();
//                      System.out.println(cookie);
//                      System.out.println(doc.text());
//              }
//              LoopList<Integer> cookiesOfDiffUsers
//                      = new LoopList<>();
//              cookiesOfDiffUsers.add(1);
//              cookiesOfDiffUsers.add(2);
//              cookiesOfDiffUsers.add(3);
//              while(true) {
//                      System.out.println(cookiesOfDiffUsers.next());
//              }

        }

}