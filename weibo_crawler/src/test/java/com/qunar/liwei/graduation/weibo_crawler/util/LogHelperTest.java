package com.qunar.liwei.graduation.weibo_crawler.util;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.*;

public class LogHelperTest {
        @Test
        public void testLog() {
                LogHelper.logInFile(Thread.currentThread(), new IndexOutOfBoundsException());
                File logFile = new File(".\\log\\exception.txt");
                assertTrue(logFile.exists());
        }
}