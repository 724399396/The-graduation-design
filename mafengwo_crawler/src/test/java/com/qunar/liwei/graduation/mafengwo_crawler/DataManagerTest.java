package com.qunar.liwei.graduation.mafengwo_crawler;


import java.util.List;

import org.junit.Test;

import com.qunar.liwei.graduation.mafengwo_crawler.DataManager;

import static org.junit.Assert.*;

public class DataManagerTest {
        @Test
        public void testIsTravelExist() {
                boolean exist = DataManager.isTravelExist("http://www.mafengwo.cn/i/319794.html");
                assertTrue(exist);
        }

        @Test
        public void testGetUrls() {
                List<QueryResult> url = DataManager.getUrl(13083, 10);
                System.out.println(url);
        }
}