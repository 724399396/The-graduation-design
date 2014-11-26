package com.qunar.liwei.weibo_crawler;

import java.io.File;



public class Test {
	public static void main(String[] args) {
//		String str = "_T_WM=0be54063143943b7f19b296b980dab19;SUB=_2AkMjMfj4a8NlrAZSmv0XyWzjaopH-jyQbfgOAn7oJhMyCBh77nEoqSeHY-2eKLwTPSWXLZhO0VV-JRFLww..;gsid_CTandWM=4urBb5801MC4FfEYkwwMVmllf2H;";
//		System.out.println(str.substring(0, str.length()-1));
		String filePath = "/home/liwei/javaCode/The-graduation-design/travelInfoPush/Xi'an_TFIDF.txt";
		File file = new File(filePath);
		System.out.println(file.getName());
		System.out.println(filePath.lastIndexOf("."));
		String procFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + "_proc" + filePath.substring(filePath.lastIndexOf("."));
		
		
		System.out.println(procFilePath);
		
//		boolean isDTF = file.getName().contains("DTF");
//		System.out.println(isDTF);
	}
}
