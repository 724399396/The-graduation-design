package com.qunar.liwei.graduation.weibo_crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class WeiboCrawler {
	
	static BlockingQueue<String> urls = new LinkedBlockingQueue<>();
	static BlockingQueue<WeiboUser> users = new LinkedBlockingQueue<WeiboUser>();
	static BlockingQueue<Weibo> weibos = new LinkedBlockingQueue<Weibo>();
	
	static List<WeiboUser> usersList = Collections.synchronizedList(new LinkedList<WeiboUser>());
	static List<Weibo> weibosList = Collections.synchronizedList(new LinkedList<Weibo>());
	
	static DataManager dataManager = new DataManager();
	
	
	static class Filler implements Runnable {
		public void run() {
			while(!Thread.interrupted()) {
				try {
					String url = urls.take();
					WeiboUser weiboUser = WeiboUser.newMainInstance(url);
					users.put(weiboUser);
					usersList.add(weiboUser);
					System.out.println(weiboUser.getName() + " add in queue");
					for (String followUrl : weiboUser.getFollowsUrl()) {
						System.out.println("follow " + followUrl);
						WeiboUser followUser = WeiboUser.newFollowInstance(followUrl);
						users.put(followUser);
						usersList.add(followUser);
					}
				} catch (InterruptedException e) {
					System.err.println("填充线程获取url失败");
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("创建主用户失败");
					e.printStackTrace();
				}
			}
		}
	}
	
	static class Fetcher implements Runnable {
		public void run() {
			while(!Thread.interrupted()) {
				try {
					WeiboUser user = users.take();
					usersList.remove(user);
					if (user.isFinishCrawler())
						continue;
					System.out.println("爬取用户： " + user.getName());
					dataManager.saveFollow(user);
					List<Weibo> weibolist = HtmlPageParse.getWeiboList(user.getUrl());
					for (Weibo weibo : weibolist) {
						if (user.weiboIsNew(weibo)) {
							weibos.put(weibo);
							weibosList.add(weibo);
						}
					}
					users.put(user);
					usersList.add(user);
				} catch (InterruptedException e) {
					System.err.println("爬网页线程中断");
					e.printStackTrace();
				} catch (IOException e) {
					System.err.println("爬网页超时");
					e.printStackTrace();
				}
			}
		}
	}
	
	static class Saver implements Runnable {
		public void run() {
			while (!Thread.interrupted()) {
				try {
					Weibo weibo = weibos.take();
					dataManager.saveWeibo(weibo);
					weibosList.remove(weibo);
				} catch (InterruptedException e) {
					System.err.println("存入数据库线程中断");
					e.printStackTrace();
				}
			}
		}
	}
	
	private static class Serializer extends TimerTask {
		@Override
		public void run() {
			try {
				ObjectOutputStream out = new ObjectOutputStream(
						new FileOutputStream("users.out"));
				out.writeObject(usersList);
				out.writeObject(weibosList);
				out.flush();
				out.close();
				System.out.println("用户个数" + usersList.size());
				System.out.println("剩余的还没存储:" + weibosList.size());
			} catch (IOException e) {
				System.err.printf("序列化失败");
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void deSerialazer() throws FileNotFoundException, IOException, ClassNotFoundException {
		File file = new File("users.out");
		if (file.exists()) {
			ObjectInputStream in = new ObjectInputStream(
					new FileInputStream(file));
			usersList =  (List<WeiboUser>) in.readObject();
			weibosList = (List<Weibo>) in.readObject();
			in.close();
			users.addAll(usersList);
			weibos.addAll(weibosList);
		}
	}
	
	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
		urls.put("http://weibo.cn/pennyliang");
		//deSerialazer();
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				try {
					FileHandler fh = new FileHandler("exception_log.txt", true);
					fh.setFormatter(new SimpleFormatter());//输出格式
					Logger logger = Logger.getAnonymousLogger();
					logger.addHandler(fh);
					logger.log(Level.SEVERE, 
							"Thread terminated with exception: " + t.getName()
							+ " cause by" + e.getCause());
				} catch (SecurityException | IOException e1) {
					System.err.println("建立日志文件失败");
					e1.printStackTrace();
				}
				
			}
		});
		ExecutorService exec = Executors.newFixedThreadPool(3);	
		exec.execute(new Filler());
		exec.execute(new Fetcher());
		exec.execute(new Saver());
		Timer timer = new Timer();
		timer.schedule(new Serializer(), 0, 1 * 60000);
//		Thread.sleep(50000);
//		while (!(urls.isEmpty() && weibos.isEmpty() && users.isEmpty()))
//			;
//		exec.shutdownNow();
//		exec.awaitTermination(30, TimeUnit.SECONDS);
	}
}
