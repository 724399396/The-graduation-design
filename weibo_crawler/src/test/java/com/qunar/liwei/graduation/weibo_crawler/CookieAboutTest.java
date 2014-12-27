package com.qunar.liwei.graduation.weibo_crawler;

import java.util.Map;

import org.junit.Test;

import com.qunar.liwei.graduation.weibo_crawler.CookieAbout;
import com.qunar.liwei.graduation.weibo_crawler.util.LoopList;

public class CookieAboutTest {

        @Test
        public void testGetCookie() {
                LoopList<Map<String,String>> cookies
                        = CookieAbout.getCookiesLoopList();
                System.out.println(cookies.next());
                System.out.println(cookies.next());
                System.out.println(cookies.next());
                System.out.println(cookies.next());
        }
}