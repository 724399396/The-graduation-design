package com.qunar.liwei.graduation.forwarding_analyze.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogHelper {
        public static void logInFile(Thread t, Throwable e) {
                FileHandler fh = null;
                try {
                        File logDirectory = new File(".\\log");
                        if (!logDirectory.exists())
                                logDirectory.mkdirs();
                        try {
                                fh = new FileHandler(".\\log\\exception.txt", true);
                                fh.setFormatter(new SimpleFormatter());//输出格式
                                Logger logger = Logger.getAnonymousLogger();
                                logger.addHandler(fh);
                                logger.log(Level.SEVERE,
                                                t.getName() + " Thread terminated with exception: " +
                                                e.toString() + " cause by " + e.getCause());
                        } finally {
                                fh.close();
                        }
                } catch (SecurityException e1) {
                        System.err.println("日志错误");
                        e1.printStackTrace();
                } catch (IOException e1) {
                        System.err.println("日志错误");
                        e1.printStackTrace();
                }
        }
}