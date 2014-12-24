package com.qunar.liwei.graduation.mafengwo_crawler;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.qunar.liwei.graduation.mafengwo_crawler.QueryCondition;
import com.qunar.liwei.graduation.mafengwo_crawler.QueryResult;
import com.qunar.liwei.graduation.mafengwo_crawler.Travel;

public class DataManager {
        private static SqlSession session;
        static {
                String resource = "dbconf.xml";
                Reader reader;
                try {
                        reader = Resources.getResourceAsReader(resource);
                        SqlSessionFactory sessionFactory =
                                        new SqlSessionFactoryBuilder().build(reader);
                        session = sessionFactory.openSession();
                } catch (IOException e) {
                        System.err.println("Mabits 配置文件读取错误");
                        e.printStackTrace();
                }
        }

        public static int addTravel(Travel travel) {
                String statement =
                                "com.qunar.liwei.graduation.mafengwo_crawler.addTravel";
                int insert = session.insert(statement, travel);
                session.commit();
                return insert;
        }

        public static boolean isTravelExist(String url) {
                String statement =
                                "com.qunar.liwei.graduation.mafengwo_crawler.isTravelExist";
                String id = session.selectOne(statement, url);
                session.commit();
                return id != null ? true : false;
        }

        public static List<QueryResult> getUrl(int cityId, int nums) {
                String statement =
                                "com.qunar.liwei.graduation.mafengwo_crawler.getUrls";
                List<QueryResult> urlList = session.selectList(
                                statement, new QueryCondition(cityId, nums));
                session.commit();
                return urlList;
        }

        public static Travel getTravel(long id) {
                String statement =
                                "com.qunar.liwei.graduation.mafengwo_crawler.getTravels";
                Travel travel = session.selectOne(statement, id);
                session.commit();
                return travel;
        }
}