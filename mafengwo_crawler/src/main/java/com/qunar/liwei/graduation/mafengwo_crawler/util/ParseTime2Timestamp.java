package com.qunar.liwei.graduation.mafengwo_crawler.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseTime2Timestamp {
        private static SimpleDateFormat dateFormat =
                        new SimpleDateFormat("yyyy-MM-dd");
        private static SimpleDateFormat dateMinuteFormat =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm");
        private static SimpleDateFormat dateTimeFormat =
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        public static Timestamp parseTimestamp(String time) {
                int length = time.length();
                try {
                        if (length == 10)
                                return new Timestamp(((Date)
                                                dateFormat.parseObject(time)).getTime());
                        if (length == 16)
                                return new Timestamp(((Date)
                                                dateMinuteFormat.parseObject(time)).getTime());
                        if (length == 19)
                                return new Timestamp(((Date)
                                                dateTimeFormat.parseObject(time)).getTime());
                } catch (ParseException e) {
                        LogHelper.logInFile(Thread.currentThread(), e);
                }
                throw new AssertionError("没有转化" + time);
        }
}