package com.qunar.liwei.weibo_crawler;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.net.HttpRequest;
import cn.edu.hfut.dmic.webcollector.net.HttpResponse;
import cn.edu.hfut.dmic.webcollector.net.Request;
import cn.edu.hfut.dmic.webcollector.net.Response;
import cn.edu.hfut.dmic.webcollector.parser.Parser;
import cn.edu.hfut.dmic.webcollector.util.CommonConnectionConfig;
import cn.edu.hfut.dmic.webcollector.util.Config;
import cn.edu.hfut.dmic.webcollector.util.ConnectionConfig;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;


public class CopyOfWeiboCrawler extends BreadthCrawler {
	 /**
     * 自定义Http请求
     *
     */
	private static class MyRequest implements Request {
		private URL url;
		private Proxy proxy=null;
		private ConnectionConfig config=null;
		public URL getURL() {
			return url;
		}

		public void setURL(URL url) {
			this.url = url;
		}
		public Response getResponse(CrawlDatum datum) throws Exception {	
		
			 /**
		     * 这里采用httpclient来取代原来的方法，获取http相应，需要导入httpclient4.x的相关jar包
		     */
			HttpResponse response = new HttpResponse(url);;
			HttpURLConnection con;
			 if(proxy==null){
		            con=(HttpURLConnection) url.openConnection();
		        }else{
		            con=(HttpURLConnection) url.openConnection(proxy);
		        }
			   	//con.setRequestProperty("Cookie", "gsid_CTandWM=4uGlbea51neHdDAVSjA9umllf2H");  
		        con.setDoInput(true);
		        con.setDoOutput(true);
		        con.setRequestMethod("POST");
		        if(config!=null){
		            config.config(con);
		        }
//		        con.setUseCaches(false);
//		        con.setInstanceFollowRedirects(false);
//		        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//		        con.connect();

//		        OutputStreamWriter out =new OutputStreamWriter(con.getOutputStream(), "utf-8");
//  	          	out.write("keyword=ki");
//		        out.flush();
//		        out.close();

		        InputStream is;
		        
		        response.setCode(con.getResponseCode());
       
		        is=con.getInputStream();

		        byte[] buf = new byte[2048];
		        int read;
		        int sum=0;
		        int maxsize=Config.maxsize;
		        ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        while ((read = is.read(buf)) != -1) {
		            if(maxsize>0){
		            sum=sum+read;
		                if(sum>maxsize){
		                    read=maxsize-(sum-read);
		                    bos.write(buf, 0, read);                    
		                    break;
		                }
		            }
		            bos.write(buf, 0, read);
		        }

		        is.close();       
		        
		        response.setContent(bos.toByteArray());
		        response.setHeaders(con.getHeaderFields());
		        bos.close();
		        return response;
			
//			/*HttpClient client = new DefaultHttpClient();		
//            HttpPost httpPost = new HttpPost(getURL().toString());
//            httpPost.
//            List<NameValuePair> nvps = new ArrayList <NameValuePair>();
//            Map<String,String> params = new HashMap<String, String>();
//        	params.put("mobile", "181212631@163.com");
//            Set<String> keySet = params.keySet();  
//            for(String key : keySet) {  
//                nvps.add(new BasicNameValuePair(key, params.get(key)));  
//            }  
//              
//            try {        
//                httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
//            } catch (UnsupportedEncodingException e) {  
//                e.printStackTrace();  
//            }  
//            /*这里用的是httpclient的HttpResponse，与WebCollector中的HttpResponse无关*/
//            org.apache.http.HttpResponse httpClientResponse = client.execute(httpPost);
//            HttpEntity entity = httpClientResponse.getEntity();
//
//            /*
//             *将httpclient获取的http响应头信息放入Response
//             *Response接口中要求http头是Map<String,List<String>>类型，所以需要做个转换
//             */
//            Map<String, List<String>> headers = new HashMap<String, List<String>>();
//            for (Header header : httpClientResponse.getAllHeaders()) {
//                List<String> values = new ArrayList<String>();
//                values.add(header.getValue());
//                headers.put(header.getName(), values);
//            }
//            response.setHeaders(headers);
//
//            /*设置http响应码，必须设置http响应码，否则会影响抓取器对抓取状态的判断*/
//            response.setCode(httpClientResponse.getStatusLine().getStatusCode());
//
//            /*设置http响应内容，为网页(文件)的byte数组*/
//            response.setContent(EntityUtils.toByteArray(entity));
//
//            /*
//             这里返回的是HttpResponse类型，它的getContentType()方法会自动从getHeader()方法中
//             获取网页响应的content-type,如果自定义Response，一定要实现getContentType()方法，因
//             为网页解析器的生成需要依赖content-type
//             */
//            return response;
		}
	    public void setProxy(Proxy proxy) {
	        this.proxy=proxy;
	    }
	    public Proxy getProxy() {
	        return proxy;
	    }
	    
	    public void setConnectionConfig(ConnectionConfig config) {
	        this.config=config;
	    }
	    
	    public ConnectionConfig getConconfig() {
	        return config;
	    }		
	}

    
    /**
     * 覆盖Fetcher类的createRequest方法，可以自定义http请求
     * 一般需要自定义一个实现Request接口的类（这里是MyRequest)
     */
    @Override
    public Request createRequest(String url) throws Exception {
        MyRequest request = new MyRequest();
        URL _URL = new URL(url);
        request.setURL(_URL);
        request.setConnectionConfig(new CommonConnectionConfig(
        		"Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:26.0) Gecko/20100101 Firefox/26.0",
        		"gsid_CTandWM=4uGlbea51neHdDAVSjA9umllf2H"));
        return request;
    }
    /**
     * 这里可以根据http响应的url和contentType来生成网页解析器 contentType可以用来区分相应是网页、图片还是文件
     * 这里直接用父类的方法，可以参照父类的方法，来自己生成需要的网页解析器
     */
    @Override
    public Parser createParser(String url, String contentType) throws Exception {
        return super.createParser(url, contentType);
    }
    @Override
    public void visit(Page page) {
    	org.jsoup.select.Elements divs = page.getDoc().select("div.c");
        for (org.jsoup.nodes.Element div : divs) {
            System.out.println(div.text());
        }
    }

    public static void main(String[] args) throws IOException, Exception {
        Config.topN = 0;
        CopyOfWeiboCrawler crawler=new CopyOfWeiboCrawler();
        //for (long userId = 1000000000; userId <= 1999999999; userId++) {
	        //for (int i = 1; i <= 10; i++) {
	            crawler.addSeed("http://weibo.cn/yankuan?vt=4");
	            //http://zhidao.baidu.com/daily/view?id=3151
	            //http://weibo.cn/u/1616192700?page=1
	        //}
        //}
        crawler.addRegex(".*");
        crawler.setThreads(2);
        crawler.start(1);

    }
}
