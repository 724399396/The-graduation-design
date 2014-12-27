package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestSearch {
        public static void main(String[] args) throws IOException {
                Connection searchConn = Jsoup.connect("http://weibo.cn/search/")
                                .cookies(CookieAbout.getAnotherCookie())
                                .userAgent("Mozilla").method(Method.POST);
                searchConn.data("advancedfilter", "1")
                                .data("keyword", "送的")
                                .data("nick", "华商报")
                                .data("starttime","")
                                .data("endtime", "20141223")
                                .data("sort", "time")
                                .data("smblog", "搜索");
                Document doc = searchConn.post();
                System.out.println(doc.text());
        }
}