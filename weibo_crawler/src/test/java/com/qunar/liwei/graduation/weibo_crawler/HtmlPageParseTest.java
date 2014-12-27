package com.qunar.liwei.graduation.weibo_crawler;

import org.jsoup.nodes.Document;

import static org.junit.Assert.*;

import org.junit.Test;



public class HtmlPageParseTest {
        //@Test
        public void testGetDoc() {
                Document doc = HtmlPageParse.getDoc("http://weibo.cn/u/1497035431");
                assertNotNull(doc);
        }

        @Test
        public void testGetWeiboList() {
                System.out.println(
                                HtmlPageParse.getWeibosFromPage("http://weibo.cn/pennyliang?page=15", "梁斌"));
        }
}