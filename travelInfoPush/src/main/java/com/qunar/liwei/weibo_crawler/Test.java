package com.qunar.liwei.weibo_crawler;

import java.io.File;
import java.math.BigDecimal;

public class Test {
	public static void main(String[] args) {
		String str = "_T_WM=0be54063143943b7f19b296b980dab19;SUB=_2AkMjMfj4a8NlrAZSmv0XyWzjaopH-jyQbfgOAn7oJhMyCBh77nEoqSeHY-2eKLwTPSWXLZhO0VV-JRFLww..;gsid_CTandWM=4urBb5801MC4FfEYkwwMVmllf2H;";
		System.out.println(str.substring(0, str.length()-1));
		File file = new File("F:\\code\\The-graduation-design\\Xi'an_TF.txt.del");
		System.out.println(file.getName());
		
		Double d = Double.valueOf("1.24E-10");
		System.out.println(d);
		
		System.out.println(new BigDecimal(250).divide(new BigDecimal(1)).multiply(new BigDecimal(0.8d)).floatValue());
	}
}
