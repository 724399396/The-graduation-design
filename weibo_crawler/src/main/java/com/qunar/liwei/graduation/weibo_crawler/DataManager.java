package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.qunar.liwei.graduation.weibo_crawler.util.EmojiFilter;


public class DataManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4426822852419649539L;
	static SqlSession session;
	
	static {
		try {
			String resource = "dbconf.xml";
			Reader reader;
			reader = Resources.
					getResourceAsReader(resource);
			SqlSessionFactory sessionFactory = 
					new SqlSessionFactoryBuilder().build(reader);
			// 默认非自动提交
			session = sessionFactory.openSession();
		} catch (IOException e) {
			System.err.println("读取Mybatis数据库xml文件错误");
			e.printStackTrace();
		}	
	}
	
	public int saveWeibo(Weibo weibo) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.saveWeibo";
		weibo.setCommontText(EmojiFilter.emojiFilt(weibo.getCommontText()));
		int insertNums = session.insert(statement, weibo);
		session.commit();
		return insertNums;
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
	
	public String getMaxDate(String userName) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.getMaxDate";
		String Date = session.selectOne(statement, userName);
		session.commit();
		return Date;
	}
	
	public String getMinDate(String userName) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.getMinDate";
		String Date = session.selectOne(statement, userName);
		session.commit();
		return Date;
	}
	
	public boolean isWeiboExist(Weibo weibo) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.isWeiboExist";
		Integer count = session.selectOne(statement, weibo);
		session.commit();
		return count > 0 ? true : false;
	}
	
	public List<Bug> bugFixSelect() {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.bugFixSelect";
		List<Bug> list = session.selectList(statement);
		session.commit();
		return list;
	}
	public int bugFixUpdate(Bug bug) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.bugFixUpdate";
		int update = session.update(statement, bug);
		session.commit();
		return update;
	}
	
}
