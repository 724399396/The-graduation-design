package com.qunar.liwei.graduation.weibo_crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.qunar.liwei.graduation.weibo_crawler.util.LogHelper;


public class WeiboCrawler {
	private static volatile boolean crawlFinish = false;
	private static ThreadLocal<DataManager> dataManagerHolder
	= new ThreadLocal<DataManager>() {
		@Override
		public DataManager initialValue() {
			return new DataManager();
		}
	};
	public static DataManager getDataManager() {
		return dataManagerHolder.get();
	}
	
	static class Filler implements Runnable {
		private final BlockingQueue<String> fetchingUrl;
		private final CopyOnWriteArraySet<String> fetchedUrl;
		private final BlockingQueue<WeiboUser> users;
		public Filler(BlockingQueue<String> fetchingUrl,
				CopyOnWriteArraySet<String> fetchedUrl,
				BlockingQueue<WeiboUser> users) {
			this.fetchingUrl = fetchingUrl;
			this.fetchedUrl = fetchedUrl;
			this.users = users;
		}
		public void run() {			
			while(!fetchingUrl.isEmpty()) {
				try {
					String url = fetchingUrl.poll();
					if (fetchedUrl.contains(url))
						continue;
					WeiboUser mainUser = WeiboUser.newMainInstance(url);
					if (!users.contains(mainUser))
						users.put(mainUser);
					getDataManager().saveFollow(mainUser);
					System.out.printf("Main user %s has added in%n", 
							mainUser.getName());
					for (String followUrl : mainUser.getFollowsUrl()) {
						WeiboUser followUser = WeiboUser.newFollowInstance(followUrl);
						if (!users.contains(followUser))
							users.put(followUser);
						System.out.printf("Flow user %s has add in%n", 
								followUser.getName());
					}
					fetchedUrl.add(url);
				} catch (InterruptedException e) {
					System.err.println("填充线程获取url失败");
					e.printStackTrace();
					LogHelper.logInFile(Thread.currentThread(), e);
				} catch (IOException e) {
					System.err.println("创建主用户失败");
					e.printStackTrace();
					LogHelper.logInFile(Thread.currentThread(), e);
				}		
			}
		}
	}
	
	static class Fetcher implements Runnable {
		private final BlockingQueue<WeiboUser> users;
		private final BlockingQueue<Weibo> weibos;
		public Fetcher(BlockingQueue<WeiboUser> users,
				BlockingQueue<Weibo> weibos) {
			this.users = users;
			this.weibos = weibos;
		}
		public void run() {
			while(!crawlFinish) {
				WeiboUser user = null;
				try {
					user = users.take();
				} catch (InterruptedException e) {
					e.printStackTrace();
					LogHelper.logInFile(Thread.currentThread(), e);
					/* retry */
				} finally {
					if (user.isFinishCrawler()) {
						if (users.isEmpty()) {
							crawlFinish = true;
							break; // 终止
						} else {
							continue;
						}
					}
				}
				String url = user.getUrl();
				System.out.println("爬取用户： " + user.getName() 
						+ " ,地址: " + url);
				try {
					List<Weibo> weibolist = HtmlPageParse.
							getWeibosFromPage(url, user.getName());
					for (Weibo weibo : weibolist) {
						if (weibo == null)
							continue;
						if (user.weiboIsNew(weibo)) {
							weibos.put(weibo);
						}
					}
				} catch (Throwable t) {
					t.printStackTrace();
					LogHelper.logInFile(Thread.currentThread(), t.getCause());
				} finally {
					while (true) {
						try {
							users.put(user);
							break;
						} catch (InterruptedException e) {
							LogHelper.logInFile(Thread.currentThread(), e);
							/* retry */
						}
					}
				}
			}
		}
	}
	
	static class Saver implements Runnable {
		private final BlockingQueue<Weibo> weibos;
		public Saver(BlockingQueue<Weibo> weibos) {
			super();
			this.weibos = weibos;
		}

		public void run() {
			while (true) {
				try {
					if (crawlFinish && weibos.size() == 0)
						break;
					Weibo weibo = weibos.take();				
					getDataManager().saveWeibo(weibo);
				} catch (InterruptedException e) {
					System.err.println("存入数据库线程中断");
					LogHelper.logInFile(Thread.currentThread(), e);
				} catch (Throwable t) {
					System.err.println("数据库存储错误");
					LogHelper.logInFile(Thread.currentThread(), t.getCause());
				}
			}
		}
	}
	
	private static class Serializer implements Runnable {
		private final BlockingQueue<String> fetchingUrl;
		private final CopyOnWriteArraySet<String> fetchedUrl;
		private final BlockingQueue<WeiboUser> users;
		private final BlockingQueue<Weibo> weibos;
			
		public Serializer(BlockingQueue<String> fetchingUrl,
				CopyOnWriteArraySet<String> fetchedUrl,
				BlockingQueue<WeiboUser> users, BlockingQueue<Weibo> weibos) {
			super();
			this.fetchingUrl = fetchingUrl;
			this.fetchedUrl = fetchedUrl;
			this.users = users;
			this.weibos = weibos;
		}
		@Override
		public void run() {
			while (true) {
				try {
					ObjectOutputStream out = new ObjectOutputStream(
							new FileOutputStream("users.out"));
					out.writeObject(fetchedUrl);
					out.writeObject(fetchingUrl);		
					out.writeObject(users);
					out.writeObject(weibos);
					out.flush();
					out.close();
					System.out.println("用户个数" + users.size());
					System.out.println("剩余的还没存储:" + weibos.size());
					break;
				} catch (IOException e) {
					System.err.printf("序列化失败");
					LogHelper.logInFile(Thread.currentThread(), e);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void deSerialaze(BlockingQueue<String> fetchingUrl,
				CopyOnWriteArraySet<String> fetchedUrl, BlockingQueue<WeiboUser> users,
				BlockingQueue<Weibo> weibos) {		
			File file = new File("users.out");
			if (file.exists()) {
				ObjectInputStream in = null;
				try {
				in = new ObjectInputStream(
						new FileInputStream(file));
				fetchedUrl.addAll((CopyOnWriteArraySet<String>) in.readObject());				
				safelyput(fetchingUrl, (BlockingQueue<String>) in.readObject());
				safelyput(users,(BlockingQueue<WeiboUser>) in.readObject());
				safelyput(weibos,(BlockingQueue<Weibo>) in.readObject());
				} catch (IOException e) {
					System.err.printf("反序列化失败");
					LogHelper.logInFile(Thread.currentThread(), e);
				} catch (ClassNotFoundException e) {
					System.err.printf("反序列化失败");
					LogHelper.logInFile(Thread.currentThread(), e);
				} finally {
					try {
						in.close();
					} catch (IOException e) {
						LogHelper.logInFile(Thread.currentThread(), e);
					}
				}
			}
	}
	private static <E> void safelyput(BlockingQueue<E> queue,
								BlockingQueue<E> readQueue) {
		for(E e : readQueue) {
			if (!queue.contains(e)) {
				try {
					queue.put(e);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public static void start(List<String> urls, int threads) {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {	
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				LogHelper.logInFile(t, e);
			}
		});
		BlockingQueue<String> fetchingUrl = new LinkedBlockingQueue<>();
		CopyOnWriteArraySet<String> fetchedUrl = new CopyOnWriteArraySet<>();
		BlockingQueue<WeiboUser> users = new LinkedBlockingQueue<>();
		BlockingQueue<Weibo> weibos = new LinkedBlockingQueue<>();
		deSerialaze(fetchingUrl, fetchedUrl, users, weibos);
		fetchingUrl.addAll(urls);
		System.out.println(users);
		ExecutorService execSev = Executors.newFixedThreadPool(3);
		execSev.execute(new Filler(fetchingUrl, fetchedUrl, users));
		execSev.execute(new Fetcher(users, weibos));
		execSev.execute(new Saver(weibos));
		ScheduledExecutorService sExec = Executors.newScheduledThreadPool(1);
		sExec.scheduleAtFixedRate(new Serializer(fetchingUrl, fetchedUrl, users, weibos), 
				10, 10, TimeUnit.SECONDS);
		execSev.shutdown();
		while(!crawlFinish)
			;
		sExec.shutdown();
	}
	
	public static void main(String[] args){
		List<String> urls = new LinkedList<>();
		urls.add("http://weibo.cn/pennyliang");
		start(urls, 2);
		//test();
	}
	
	@SuppressWarnings("unused")
	private static void test(){
		ExecutorService exec = Executors.newFixedThreadPool(1);
		BlockingQueue<WeiboUser> users = new LinkedBlockingQueue<>();
		BlockingQueue<Weibo> weibos = new LinkedBlockingQueue<>();
		try {
			users.add(WeiboUser.newFollowInstance("http://weibo.cn/pennyliang"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		exec.execute(new Fetcher(users, new LinkedBlockingQueue<Weibo>()));
	}
}
