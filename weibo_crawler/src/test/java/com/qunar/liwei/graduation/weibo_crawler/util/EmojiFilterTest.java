package com.qunar.liwei.graduation.weibo_crawler.util;

import org.junit.Test;

import com.qunar.liwei.graduation.weibo_crawler.util.EmojiFilter;

public class EmojiFilterTest {

	@Test
	public void testContainsEmoji() {
		String emoji = EmojiFilter.emojiFilt("＂微博招亲啦[喜]＂：乐乐，泰迪贵宾，女，四岁半。有车[小汽车]有房🏡，[钱]有存款。家里的掌上明珠，大家闺秀，多才多艺，性格温顺可人！经过家庭会议👪，打算给乐乐招名：[男孩]英俊潇洒，体型稍微偏小，聪明伶俐，大眼睛毛色棕红色的帅哥一枚！[相爱]完成人生大事，升级做妈妈！限于北京的宝贝们[狗] ");
		System.out.println(emoji);
	}
}
