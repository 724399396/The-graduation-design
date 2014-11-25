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
	public static String englishWordAndNumberDelete(
			String filePath) throws IOException {		
		File file = new File(filePath);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "gbk"));
		String lineTxt = null;
		StringBuilder result = new StringBuilder();
		Pattern pattern = Pattern.compile("([^0x00-0xff&&\\S]+)=([0-9\\.eE-]+)");
		if((lineTxt = in.readLine())!= null) {
			int titleIndex = lineTxt.indexOf("\t");
			result.append(lineTxt.substring(0, titleIndex)).append(":\r\n");
			Map<String,Double> map = new LinkedHashMap<String,Double>();
			Matcher matcher = pattern.matcher(lineTxt);
			while (matcher.find()) {
				String key = matcher.group(1);
				System.out.println(matcher.group(2));
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
			result.append("\r\n");
		}
		in.close();
		return result.toString();
	}
	
	public static String getHistogramData(String outFilePath,String str) 
			throws IOException {
		File file = new File(outFilePath);	
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), "gbk"));
		String lineTxt = str;
		//Pattern patternOfTa = Pattern.compile("\t");
		Pattern patternOfEq = Pattern.compile("=");
		
		//Matcher matcherOfTa = pa0tternOfTa.matcher(lineTxt);
		//lineTxt = matcherOfTa.replaceAll("\r\n");
		Matcher matcherOfEq = patternOfEq.matcher(lineTxt);
		lineTxt = matcherOfEq.replaceAll(" ");
		out.write(lineTxt);

		out.close();
		return lineTxt;
	}
	
	public static void main(String[] args) throws IOException {
		
		String result = englishWordAndNumberDelete(
				"F:\\code\\The-graduation-design\\Xi'an_TF.txt");
		result = getHistogramData(
				"F:\\code\\The-graduation-design\\Xi'an_TF_proc.txt"
				,result);
	
		//System.out.println(result);
		
        Scanner scanner = new Scanner(result);
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
		
        DrawHistogram planeHistogram = new DrawHistogram();
        
        for (int titleIndex = 0; titleIndex < title.size(); titleIndex++) {
        	int size = nums.get(titleIndex).size();
        	double[] numArray = new double[size];
        	String[] wordArray = new String[size];
        	for (int index = 0; index < size; index++) {
        		numArray[index] = nums.get(titleIndex).get(index);
        		wordArray[index] = words.get(titleIndex).get(index);
        	}
        	String titleStr = title.get(titleIndex);
	    	 BufferedImage image = planeHistogram.paintPlaneHistogram(
	        		titleStr,numArray,wordArray,new Color[] {Color.BLUE });
	         File output = new File("F:\\code\\The-graduation-design\\image\\" + titleStr +".jpg");
	         try {
	             ImageIO.write(image, "jpg", output);
	         } catch (IOException e) {
	             e.printStackTrace();
	         }
        }
	}
}
