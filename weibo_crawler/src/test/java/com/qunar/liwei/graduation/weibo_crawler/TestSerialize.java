package com.qunar.liwei.graduation.weibo_crawler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestSerialize {
	private static BlockingQueue<WeiboUser> users = 
			new LinkedBlockingQueue<WeiboUser>();
	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
		users.put(new WeiboUser("http://weibo.cn/", null, "量", 
				null, null));
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(users);
		oos.close();
		ObjectInputStream ois = new ObjectInputStream(
				new ByteArrayInputStream(os.toByteArray()));
		@SuppressWarnings("unchecked")
		BlockingQueue<WeiboUser> reSerilUsers = (BlockingQueue<WeiboUser>) ois.readObject();
		ois.close();
		System.out.println(reSerilUsers);
	}
}
