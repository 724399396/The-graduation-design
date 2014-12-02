package com.qunar.liwei.weibo_crawler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileWordFilter {
	static boolean isIDF;
	private static String englishWordAndNumberDelete(
			File file) throws IOException {		
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "gbk"));
		String lineTxt = null;
		isIDF = file.getName().contains("IDF");
		StringBuilder result = new StringBuilder();
		Map<String,Number> total = new LinkedHashMap<String, Number>();
		Pattern pattern = Pattern.compile("([^0x00-0xff&&\\S]+)=([0-9\\.eE-]+)");
		while((lineTxt = in.readLine())!= null) {
			StringBuilder single = new StringBuilder();
			int titleIndex = lineTxt.indexOf("\t");
			String title = lineTxt.substring(0, titleIndex);
			result.append(title).append(":\r\n");
			Matcher matcher = pattern.matcher(lineTxt);
			if (!isIDF) {
				Map<String,Integer> map = new LinkedHashMap<String,Integer>();		
				while (matcher.find()) {
					String key = matcher.group(1);
					//System.out.println(matcher.group(2));
					Integer newValue = Integer.valueOf(matcher.group(2));
					Integer oldValue = map.get(key);
					map.put(key, oldValue == null? newValue : 
						(newValue + oldValue));	
					total.put(key, total.get(key) == null? newValue : 
						(newValue + (Integer)total.get(key)));	
				}
				for (Entry<String, Integer> entry : map.entrySet()) {
					String key = entry.getKey();				
					if (key.matches("[0-9a-z\\.]+"))
						continue;
					result.append(key).append("=")
						.append(entry.getValue()).append("\t");
				}
			} else {
				Map<String,Double> map = new LinkedHashMap<String,Double>();		
				while (matcher.find()) {
					String key = matcher.group(1);
					//System.out.println(matcher.group(2));
					Double newValue = Double.valueOf(matcher.group(2));
					Double oldValue = map.get(key);
					map.put(key, oldValue == null? newValue : 
						(oldValue + newValue));
					total.put(key, total.get(key) == null? newValue : 
						(newValue + (Double)total.get(key)));	
				}
				for (Entry<String, Double> entry : map.entrySet()) {
					String key = entry.getKey();				
					if (key.matches("[0-9a-z\\.]+"))
						continue;
					result.append(key).append("=")
						.append(entry.getValue()).append("\t");
					single.append(key).append("\t")
					.append(entry.getValue()).append("\r\n");
				}
			}
			result.append("\r\n");
			File fileOutDir = new File(".\\resource\\result\\data");
			if (!fileOutDir.exists())
				fileOutDir.mkdirs();
			File fileOut = new File(".\\resource\\result\\data\\"+ title + ".txt");
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileOut), "gbk"));
			out.write(single.toString());
			out.close();
		}
		in.close();
		// 统计总和
		for (Entry<String, Number> entry : total.entrySet()) {
			String key = entry.getKey();				
			if (key.matches("[0-9a-z\\.]+"))
				continue;
			//System.out.println(entry.getKey() + "=" + entry.getValue());
		}
		return result.toString();
	}
	
	
	private static String getHistogramData(String outFilePath,String str) 
			throws IOException {
		File file = new File(outFilePath);	
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "gbk"));
		String lineTxt = str;
		out.write(lineTxt);
		Pattern patternOfEq = Pattern.compile("=");
		
		Matcher matcherOfEq = patternOfEq.matcher(lineTxt);
		lineTxt = matcherOfEq.replaceAll(" ");
		

		out.close();
		return lineTxt;
	}
	

	
	private static void draw(String data, String directoryName) {
		if (!isIDF) {
	        Scanner scanner = new Scanner(data);
	        List<String> title = new LinkedList<String>();
	        List<List<String>> words = new LinkedList<List<String>>();
	        List<List<Integer>> nums = new LinkedList<List<Integer>>();     
	    	List<Integer> num = null;
	    	List<String> word = null;
	        while(scanner.hasNext()) {
	        	String str = scanner.next();
	        	if(str.contains(":")) {
	        		title.add(str.substring(0, str.length()-1));
	        		num = new LinkedList<Integer>();
	        		word = new LinkedList<String>();
	        		words.add(word);
	        		nums.add(num);
	        	} else {
	        		word.add(str);
	        		num.add(scanner.nextInt());
	        	}
	        }
	        scanner.close();
	        
	        DrawHistogram.createHistogram(words, nums, title,directoryName);
		} else {
			  Scanner scanner = new Scanner(data);
		        List<String> title = new LinkedList<String>();
		        List<List<String>> words = new LinkedList<List<String>>();
		        List<List<Double>> nums = new LinkedList<List<Double>>();     
		    	List<Double> num = null;
		    	List<String> word = null;
		        while(scanner.hasNext()) {
		        	String str = scanner.next();
		        	if(str.contains(":")) {
		        		title.add(str.substring(0, str.length()-1));
		        		num = new LinkedList<Double>();
		        		word = new LinkedList<String>();
		        		words.add(word);
		        		nums.add(num);
		        	} else {
		        		word.add(str);
		        		num.add(scanner.nextDouble());
		        	}
		        }
		        scanner.close();
		        DrawHistogram.createHistogram(words, nums, title, directoryName);
		}
	}
	
	public static void handlerDataAndDraw(File file) throws IOException {
		String fileName = file.getName();
		String procFilePath = ".\\resource\\result\\" + fileName.substring(0, fileName.lastIndexOf(".")) 
				+ "_proc" + fileName.substring(fileName.lastIndexOf("."));	//设置格式化后的文件输出路径
		String result = getHistogramData(procFilePath
				, englishWordAndNumberDelete(file));
	
		String directory = file.getName().split("\\.")[0];
		draw(result, directory);
	}
	
	public static void main(String[] args) throws IOException {
		List<File> list = new LinkedList<File>();
		File file = new File(".\\resource\\data");
		com.qunar.liwei.weibo_crawler.util.FileUtils.listAllFile(file, list);
		for (File f : list)
			;
			//System.out.println(f);
			//handlerDataAndDraw(f);
		handlerDataAndDraw(new File(".\\resource\\data\\Xi'an_TFIDF.txt"));
	}

}
