package com.qunar.liwei.graduation.weibo_crawler;

import java.io.File;
import java.io.IOException;




import java.sql.Timestamp;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class TestDivSelect {
        public static void main(String[] args) throws IOException {
                Document doc = Jsoup.parse(new File("1.html"), "utf-8");
                //Document doc = HtmlPageParse.getDoc("http://weibo.cn/pennyliang");
                //String followPage =getFollowPageUrl(doc);
                //Document followDoc = HtmlPageParse.getDoc(followPage);
                Elements followsTables = doc.select("table");
                for (Element followsTable : followsTables) {
                        Element follow = followsTable.child(0).child(0).child(1).child(0);
                        String name = follow.text();
                        String url = follow.absUrl("href");
                        System.out.println(name);
                        System.out.println(url);
                }
        }
        private static String getFollowPageUrl(Document doc) {
                String followPage = doc.select("div.tip2").
                                select("a:containsOwn(关注)").get(0).absUrl("href");
                return followPage;
        }
        private static void getFollows(String followPageUrl) {
                Document doc = HtmlPageParse.getDoc(followPageUrl);
                Elements followsTables = doc.select("table");
                for (Element followsTable : followsTables) {
                        Element follow = followsTable.child(0).child(0).child(1).child(0);
                        String name = follow.text();
                        String url = follow.absUrl("href");
                }
        }
}
