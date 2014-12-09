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
		File file = new File("F:\\code\\The-graduation-design\\travelInfoPush\\src\\resource\\data\\n_a_filter");
		com.qunar.liwei.weibo_crawler.util.FileUtils.listAllFile(file, list);
		for (File f : list) {
			if (!f.getName().contains("TF"))
				continue;
			BufferedReader in = null;
			BufferedWriter out = null;
			in = new BufferedReader(new InputStreamReader(
					new FileInputStream(f), "utf-8"));
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(new File(f.getAbsolutePath()+".proc.txt")), "utf-8"));
			String lineTxt = null;
			StringBuilder result = new StringBuilder();
			if (f.getName().contains("TFIDF")) {
				String preStr = "";
				while((lineTxt = in.readLine())!= null) {
					if (lineTxt.matches("^[\\d]+$"))
						continue;
					lineTxt = lineTxt.replaceFirst("[\\d]+\t", "");
					lineTxt = lineTxt.replaceAll("\t", " ");
					lineTxt = lineTxt.replaceAll("=", " ");
					preStr += lineTxt + " ";
				}
				Map<String,Double> map = new HashMap<>();
				Scanner sc = new Scanner(preStr);		
				while (sc.hasNext()) {
					String str = sc.next();
					double times = sc.nextDouble();
					Double old = map.get(str);
					map.put(str, old == null ? times : (double)old + times);
				}
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
					result.append(entry.getKey()).append("\t")
						.append(entry.getValue()).append("\r\n");
			} else {
				while((lineTxt = in.readLine())!= null) {
					lineTxt = lineTxt.replaceFirst("[\\d]+\t", "");
					lineTxt = lineTxt.replaceAll("\t", "\r\n");
					lineTxt = lineTxt.replaceAll("=", ":");
					result.append(lineTxt).append("\r\n");
				}
			}
			out.write(result.toString());
			out.close();
			in.close();
		}
	}
}
