package com.qunar.liwei.graduation.weibo_crawler.util;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.*;

public class LoopListTest {
        @Test
        public void testLoopList() {
                LoopList<Integer> loopList = new LoopList<>();
                loopList.add(1);        loopList.add(2);        loopList.add(3);
                assertThat(loopList.next(), is(1));
                assertThat(loopList.next(), is(2));
                assertThat(loopList.next(), is(3));
                assertThat(loopList.next(), is(1));
                assertThat(loopList.next(), is(2));
                assertThat(loopList.next(), is(3));
        }
}