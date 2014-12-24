package com.qunar.liwei.graduation.weibo_crawler;

public class ParseTimeTest {
    @Test
    public void testfirstConvert() {
            String time = "2013-03-09 23:25:40";
            String proccessTime = ParseTime2String.processTime(time);
            System.out.println(proccessTime);
            System.out.println(ParseTime2Timestamp.parseTimestamp(proccessTime)
    }
}