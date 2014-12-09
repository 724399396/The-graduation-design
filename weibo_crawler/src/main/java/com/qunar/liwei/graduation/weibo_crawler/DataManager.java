package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class DataManager {
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
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.addWeibo";
		int insertNums = session.insert(statement, weibo);
		session.commit();
		return insertNums;
	}
	
	public int saveImage(String imgUrl) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.saveImgUrl";
		int insertNums = session.insert(statement, imgUrl);
		session.commit();
		return insertNums;
	}
	
	public String getMaxDate(String userName) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.getMaxDate";
		String Date = session.selectOne(statement, userName);
		return Date;
	}
	
	public String getMinDate(String userName) {
		String statement = 
				"com.qunar.liwei.graduation.weibo_crawler.weiboMapper.getMinDate";
		String Date = session.selectOne(statement, userName);
		return Date;
	}
	
}
