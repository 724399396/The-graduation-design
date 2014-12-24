package com.qunar.liwei.graduation.weibo_crawler.util;

import java.util.Calendar;

public class ParseTime2String {
    public static String processTime(String time) {
        String result = time;

        if (time.contains("分钟")) {
                int passMinutes = Integer.parseInt(time.substring(0, time.indexOf("分钟")));
                Calendar cal=Calendar.getInstance();
                int nowMinutes = cal.get(Calendar.MINUTE);
                int nowHours = cal.get(Calendar.HOUR);
                int minutes = nowMinutes - passMinutes;
                if (nowMinutes < passMinutes) {
                        nowHours -= 1;
                        minutes = 60 + nowMinutes - passMinutes;
                }
                int month = cal.get(Calendar.MONTH)+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                result = (cal.get(Calendar.YEAR) + "-" + (month < 10 ? ("0" + month) : month)
                                + "-" +  (day < 10 ? ("0" + day) : day) +
                                " " + (nowHours < 10 ? ("0" + nowHours) : nowHours ) + ":" +
                                (minutes < 10 ? ("0" + minutes) : minutes));
        }

        if (time.contains("今天")) {
                Calendar cal=Calendar.getInstance();
                int month = cal.get(Calendar.MONTH)+1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                result = (cal.get(Calendar.YEAR) + "-"  + (month < 10 ? ("0" + month) : month)
                                + "-" +  (day < 10 ? ("0" + day) : day) +
                                " " + time.substring(3));
        }
        if (time.contains("月")) {
                String month = time.substring(0, time.indexOf("月"));
                String day = time.substring(time.indexOf("月")+1,
                                time.indexOf("日"));
                String hoursAndMi = time.substring(time.indexOf(" ")+1
                                );
                Calendar cal=Calendar.getInstance();
                result = (cal.get(Calendar.YEAR) + "-" + month +
                                "-" + day + " "
                                        + hoursAndMi);
        }
        return result;
    }
}