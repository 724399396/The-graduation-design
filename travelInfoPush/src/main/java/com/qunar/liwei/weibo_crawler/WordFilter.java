package com.qunar.liwei.weibo_crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class WordFilter {
	public static void main(String[] args) throws IOException{
		List<File> list = new ArrayList<File>();
		File file = new File("F:\\code\\The-graduation-design\\travelInfoPush\\src\\resource\\data\\n_a_filter\\1");
		com.qunar.liwei.weibo_crawler.util.FileUtils.listAllFile(file, list);
		Map<String,Double> total = new HashMap<>();
		for (File f : list) {
			if (!f.getName().contains("TFIDF"))
				continue;
			BufferedReader in = null;
			BufferedWriter out = null;
			in = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), "utf-8"));
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(f.getAbsolutePath()+".proc.txt")), "utf-8"));
			String lineTxt = null;
			StringBuilder result = new StringBuilder();
//			if (f.getName().contains("TFIDF")) {
			String preStr = "";
			int nums = 0;
			Map<String,Double> map = new HashMap<>();
			while((lineTxt = in.readLine())!= null) {
				nums++;
				if (lineTxt.matches("^[\\d]+$"))	// 空数据则继续下一条
					continue;
				lineTxt = lineTxt.replaceFirst("[\\d]+\t", ""); // 去除头部的 ID
				lineTxt = lineTxt.replaceAll("\t[^(\u4E00-\u9FA5)]*=[\\S]+", ""); //去除错误
				lineTxt = lineTxt.replaceAll("^[^(\u4E00-\u9FA5)]*=[\\S]+", "");//去除错误
				lineTxt = lineTxt.replaceAll("\t", " ");	// 转化为scanner
				lineTxt = lineTxt.replaceAll("=", " ");		// 方便处理的格式
				Scanner sc = new Scanner(preStr);		
				// 合并重复
				while (sc.hasNext()) {
					String str = sc.next();
					double times = sc.nextDouble();
					Double old = map.get(str);
					Double totalOld = total.get(str);
					map.put(str, old == null ? times / nums : ((double)old + (times / nums)));
					total.put(str, totalOld == null ? times / nums : ((double)totalOld + (times / nums)));
				}
				sc.close();
			}	
			// 排序
			List<Map.Entry<String, Double>> entrys 
				= new ArrayList<>(map.entrySet());
			Collections.sort(entrys, new Comparator<Map.Entry<String,Double>>() {
				public int compare(Entry<String, Double> arg0,
						Entry<String, Double> arg1) {
					return arg1.getValue() > arg0.getValue() ? 1 :
						(arg1.getValue() < arg0.getValue() ? -1 : 0);
				}	
			});
			for (Map.Entry<String,Double> entry : entrys)
				result.append(entry.getKey()).append(":")
					.append((double)(entry.getValue() * 10)).append("\r\n");
			out.write(result.toString());
			out.close();
			in.close();
		}
		List<Map.Entry<String, Double>> totalEntrys 
			= new ArrayList<>(total.entrySet());
		Collections.sort(totalEntrys, new Comparator<Map.Entry<String,Double>>() {
			public int compare(Entry<String, Double> arg0,
					Entry<String, Double> arg1) {
				return arg1.getValue() > arg0.getValue() ? 1 :
					(arg1.getValue() < arg0.getValue() ? -1 : 0);
			}	
		});
		StringBuilder totalSB = new StringBuilder();
		for (Map.Entry<String,Double> entry : totalEntrys)
			totalSB.append(entry.getKey()).append("\t")
				.append((double)(entry.getValue())).append("\r\n");
		BufferedWriter totalOut = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(new File("F:\\code\\The-graduation-design\\travelInfoPush\\src\\resource\\data\\n_a_filter\\1\\total.txt")), "utf-8"));
		totalOut.write(totalSB.toString());
		totalOut.close();
	}
}
