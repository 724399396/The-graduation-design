package com.qunar.liwei.weibo_crawler;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

public class FileWordFilter {
	static boolean isIDF;
	private static String englishWordAndNumberDelete(
			String filePath) throws IOException {		
		File file = new File(filePath);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "gbk"));
		String lineTxt = null;
		isIDF = file.getName().contains("IDF");
		StringBuilder result = new StringBuilder();
		Pattern pattern = Pattern.compile("([^0x00-0xff&&\\S]+)=([0-9\\.eE-]+)");
		while((lineTxt = in.readLine())!= null) {
			int titleIndex = lineTxt.indexOf("\t");
			result.append(lineTxt.substring(0, titleIndex)).append(":\r\n");
			Matcher matcher = pattern.matcher(lineTxt);
			if (!isIDF) {
				Map<String,Integer> map = new LinkedHashMap<String,Integer>();		
				while (matcher.find()) {
					String key = matcher.group(1);
					//System.out.println(matcher.group(2));
					Integer newValue = Integer.valueOf(matcher.group(2));
					Integer oldValue = map.get(key);
					map.put(key, oldValue == null? newValue : 
						(Integer.valueOf(oldValue) + Integer.valueOf(newValue)));
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
						(Double.valueOf(oldValue) + Double.valueOf(newValue)));
				}
				for (Entry<String, Double> entry : map.entrySet()) {
					String key = entry.getKey();				
					if (key.matches("[0-9a-z\\.]+"))
						continue;
					result.append(key).append("=")
						.append(entry.getValue()).append("\t");
				}
			}
			result.append("\r\n");
		}
		in.close();
		return result.toString();
	}
	
	
	private static String getHistogramData(String outFilePath,String str) 
			throws IOException {
		File file = new File(outFilePath);	
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "gbk"));
		String lineTxt = str;
		Pattern patternOfEq = Pattern.compile("=");
		
		Matcher matcherOfEq = patternOfEq.matcher(lineTxt);
		lineTxt = matcherOfEq.replaceAll(" ");
		out.write(lineTxt);

		out.close();
		return lineTxt;
	}
	
	private static void draw(String data) {
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
	        DrawHistogram.createHistogram(words, nums, title);
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
		        DrawHistogram.createHistogram(words, nums, title);
		}
	}
	
	public static void handlerDataAndDraw(String filePath) throws IOException {
		String procFilePath = filePath.substring(0, filePath.lastIndexOf(".")) 
				+ "_proc" + filePath.substring(filePath.lastIndexOf("."));	//设置格式化后的文件输出路径
		String result = getHistogramData(procFilePath
				, englishWordAndNumberDelete(filePath));
	
		System.out.println(result);
		
		draw(result);
	}
	
	public static void main(String[] args) throws IOException {		
		handlerDataAndDraw("/home/liwei/javaCode/The-graduation-design/travelInfoPush/Xi'an_TF.txt");
	}

}
