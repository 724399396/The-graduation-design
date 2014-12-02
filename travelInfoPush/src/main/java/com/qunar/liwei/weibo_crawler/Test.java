package com.qunar.liwei.weibo_crawler;

import java.io.File;
import java.util.LinkedList;
import java.util.List;



public class Test {
	private static void listAllFile(File file, List<File> list) {
		if(file.isFile()) {
			list.add(file);
			return;
		}else
			for(File f : file.listFiles())
				listAllFile(f, list);
	}
	
	public static void main(String[] args) {
//		String str = "_T_WM=0be54063143943b7f19b296b980dab19;SUB=_2AkMjMfj4a8NlrAZSmv0XyWzjaopH-jyQbfgOAn7oJhMyCBh77nEoqSeHY-2eKLwTPSWXLZhO0VV-JRFLww..;gsid_CTandWM=4urBb5801MC4FfEYkwwMVmllf2H;";
//		System.out.println(str.substring(0, str.length()-1));
		String filePath = ".\\resource\\data";
		File file = new File(filePath);
		List<File> fileList = new LinkedList<File>();
		listAllFile(file, fileList);
		for (File f : fileList)
			System.out.println(f);
//		System.out.println(file.isFile());
//		file.listFiles();
//		System.out.println(file.getName());
//		System.out.println(filePath.lastIndexOf("."));
//		String procFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + "_proc" + filePath.substring(filePath.lastIndexOf("."));
//		
//		
//		System.out.println(procFilePath);
		
//		boolean isDTF = file.getName().contains("DTF");
//		System.out.println(isDTF);
	}
}
