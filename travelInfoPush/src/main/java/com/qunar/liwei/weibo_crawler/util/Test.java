package com.qunar.liwei.weibo_crawler.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.qunar.liwei.weibo_crawler.DataAbout;



public class Test {
	 private static void fetchImageAndStoreInDisk(String imageUrl, String storePath) throws ClientProtocolException, IOException {
	    try {
//			 CloseableHttpClient httpClient = HttpClients.createDefault();
//			 HttpGet httpget = new HttpGet(imageUrl);
//			 httpget.setHeader('Host', 'weibo.cn');
//			 httpget.setHeader('Connection', 'keep-alive');
//			 httpget.setHeader('Accept','text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8');
//			 httpget.setHeader(
//					 'User-Agent',
//					 'Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36');
//			 httpget.setHeader('Referer', 'http://weibo.cn/pennyliang');
//			 httpget.setHeader('Accept-Encoding', 'gzip, deflate, sdch');
//			 httpget.setHeader('Accept-Language', 'zh-CN,zh;q=0.8,en;q=0.6');
//			 httpget.setHeader('Cookie', '__T_WM=26e3dae473113e7a8ee7849023f86d8c; gsid_CTandWM=4ueW6d5618lkNpV3KIh0Vmllf2H; SUB=_2A255er9aDeTxGeNN6VcT9SnNzTuIHXVahMESrDV6PUJbrdANLU36kW0BiMKN52Vc-FsScdL');
//
//			 CloseableHttpResponse httpReponse = httpClient.execute(httpget);
//			 try{
//			 //获取状态行
//			 System.out.println(httpReponse.getStatusLine());
//			 HttpEntity entity = httpReponse.getEntity();
//			 //返回内容
//			 for(Header h : httpReponse.getAllHeaders())
//				 System.out.println(h);
//			 System.out.println(EntityUtils.toString(entity));
//			 }finally{
//			 httpReponse.close();
//			 }
	    	URL url = new URL(imageUrl);
	    	URLConnection conn = url.openConnection();
	    	//conn.setRequestProperty('Cookie', '_T_WM=e2912b22617e335a0c7ee52f30d3d40c;SUB=_2A255eqmKDeTxGeNN6VcT9SnNzTuIHXVahDfCrDV6PUJbrdANLXbEkW03FJ05OKVgKnR72g7PtwSXDgF-HQ..;gsid_CTandWM=4uMWd55f1SFN7xX5SkKqAmllf2H'); 
	    	//HttpURLConnection httpcon = (HttpURLConnection) url.openConnection(); 
	    	//httpcon.addRequestProperty('User-Agent', 'Mozilla/4.76'); 
	    	OutputStream out =new FileOutputStream(
	    			new File(storePath));
	    	InputStream in = url.openStream();
	    	byte[] buffer = new byte[1024];
	    	int len = 0;    
	        while ((len = in.read(buffer)) != -1) {    
	            out.write(buffer, 0, len);    
	        }    
	    	} catch (FileNotFoundException e) {
	    		e.printStackTrace();
	    	} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
	    }
	
	private static void listAllFile(File file, List<File> list) {
		if(file.isFile()) {
			list.add(file);
			return;
		}else
			for(File f : file.listFiles())
				listAllFile(f, list);
	}
	
	static AtomicReference<String> mainName = new AtomicReference<>();
	
	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
//		String str = '_T_WM=0be54063143943b7f19b296b980dab19;SUB=_2AkMjMfj4a8NlrAZSmv0XyWzjaopH-jyQbfgOAn7oJhMyCBh77nEoqSeHY-2eKLwTPSWXLZhO0VV-JRFLww..;gsid_CTandWM=4urBb5801MC4FfEYkwwMVmllf2H;';
//		System.out.println(str.substring(0, str.length()-1));
//		String filePath = '.\\resource\\data';
//		File file = new File(filePath);
//		List<File> fileList = new LinkedList<File>();
//		listAllFile(file, fileList);
//		for (File f : fileList)
//			System.out.println(f);
//		System.out.println(file.isFile());
//		file.listFiles();
//		System.out.println(file.getName());
//		System.out.println(filePath.lastIndexOf('.'));
//		String procFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + '_proc' + filePath.substring(filePath.lastIndexOf('.'));
//		
//		
//		System.out.println(procFilePath);
		
//		boolean isDTF = file.getName().contains('DTF');
//		System.out.println(isDTF);
//		fetchImageAndStoreInDisk('http://ww4.sinaimg.cn/large/593af2a7gw1emwggay63pj20fs09bdi4.jpg', 
//								'./src/resource/download/1.jpg');
        

		
//    	String date = DataAbout.getLastedTime();
//    	System.out.println(date);
		File file = new File("F:\\code\\The-graduation-design\\travelInfoPush\\src\\resource\\data\\n_a_filter");
		for (File f : file.listFiles())
			new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(
							new File("F:\\code\\The-graduation-design\\travelInfoPush\\src\\resource\\image\\直方图_2014.12.8\\n_a_filter\\" 
									+ f.getName() +".png"))));
	}

}
