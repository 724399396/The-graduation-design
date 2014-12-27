package com.qunar.liwei.graduation.weibo_crawler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SerializationTest {
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		BlockingQueue<WeiboUser> urls = new LinkedBlockingQueue<WeiboUser>();
		urls.add(new WeiboUser("1", 
				new LinkedList<>(Arrays.asList("kong")), "liwei", 
				"梁斌", new LinkedHashSet<>(Arrays.asList("http://weibo.cn/pennyliang"))));
		ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream("wrom.out"));
		out.writeObject(urls);
		out.close();
		ObjectInputStream in = new ObjectInputStream(
				new FileInputStream("wrom.out"));
		@SuppressWarnings("unchecked")
		BlockingQueue<WeiboUser> urls2 = (BlockingQueue<WeiboUser>) in.readObject();
		System.out.println(urls2);
		in.close();
	}
}
