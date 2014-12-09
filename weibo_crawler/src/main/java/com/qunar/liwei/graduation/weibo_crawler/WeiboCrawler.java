package com.qunar.liwei.graduation.weibo_crawler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WeiboCrawler {
	
	static BlockingQueue<String> urls = new LinkedBlockingQueue<>();
	static BlockingQueue<WeiboUser> users = new LinkedBlockingQueue<WeiboUser>();
	static BlockingQueue<Weibo> weibos = new LinkedBlockingQueue<Weibo>();
	
	
	static class Filler implements Runnable {
		public void run() {
			while(!Thread.interrupted()) {
				try {
					String url = urls.take();
				} catch (InterruptedException e) {
					System.err.println("填充线程获取url失败");
					e.printStackTrace();
				}
			}
		}
	}
	
	static class Fetcher implements Runnable {
		public void run() {
			
		}
	}
	
	static class Saver implements Runnable {
		public void run() {
			
		}
	}
}
