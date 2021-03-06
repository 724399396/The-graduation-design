package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.qunar.liwei.graduation.weibo_crawler.util.EmojiFilter;


public class DataManager implements Serializable {
	private static final long serialVersionUID = 4426822852419649539L;
	private static SqlSessionFactory sessionFactory;
	private static ExecutorService exec = Executors.newFixedThreadPool(2);
	private SqlSession session;
	
	static {
		try {
			String resource = "dbconf.xml";
			Reader reader;
			reader = Resources.
					getResourceAsReader(resource);
			sessionFactory = 
					new SqlSessionFactoryBuilder().build(reader);			
		} catch (IOException e) {
			System.err.println("读取Mybatis数据库xml文件错误");
			e.printStackTrace();
		}	
	}
	public DataManager() {
		// 默认非自动提交
		session = sessionFactory.openSession();
	}
	
	public int saveWeibo(Weibo weibo) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.saveWeibo";
		weibo.setCommontText(EmojiFilter.emojiFilt(weibo.getCommontText()));
		int insertNums = session.insert(statement, weibo);
		session.commit();
		return insertNums;
	}
	
	public boolean isWeiboExist(Weibo weibo) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.isWeiboExist";
		Integer count = session.selectOne(statement, weibo);
		session.commit();
		return count > 0 ? true : false;
	}	
	
	public int saveFollow(WeiboUser weiboUser) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.saveFollow";
		if (weiboUser.getFollowsUrl() == null)
			return 0;
		if (isFollowSaved(weiboUser))
			return 0;
		int insertNums = session.insert(statement, weiboUser);
		session.commit();
		return insertNums;
	}
	
	private boolean isFollowSaved(WeiboUser weiboUser) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.isFollowSaved";
		String name = session.selectOne(statement, weiboUser);
		session.commit();
		return name == null ? false : true;
	}
	
	public Timestamp getMaxDate(final String userName) {
		final String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.getMaxDate";
		FutureTask<Timestamp> future = new FutureTask<Timestamp>(
				new Callable<Timestamp>() {
					public Timestamp call() {
						Timestamp date = session.selectOne(statement, userName);
						session.commit();
						return date;
					}
				}
		);
		try {
			exec.submit(future);
			return future.get(5, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return new Timestamp(0);			
		}
	}
	
	public Timestamp getMinDate(final String userName) {
		final String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.getMinDate";
		FutureTask<Timestamp> future = new FutureTask<Timestamp>(
				new Callable<Timestamp>() {
					public Timestamp call() {
						Timestamp date = session.selectOne(statement, userName);
						session.commit();
						return date;
					}
				}
		);
		try {
			exec.submit(future);
			return future.get(5, TimeUnit.SECONDS);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
			return new Timestamp(0);			
		}
	}
	
	
}
