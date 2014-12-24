package com.qunar.liwei.graduation.mafengwo_crawler;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.qunar.liwei.graduation.mafengwo_crawler.util.EmojiFilter;
import com.qunar.liwei.graduation.mafengwo_crawler.util.LogHelper;
import com.qunar.liwei.graduation.mafengwo_crawler.util.ParseTime2Timestamp;

public class CrawlerFrame {
    private static BlockingQueue<QueryResult> urls =
                    new LinkedBlockingQueue<QueryResult>();
    private static volatile boolean finish = false;
    private static CopyOnWriteArraySet<String> fetchingUrl =
                    new CopyOnWriteArraySet<String>();

    public void start(int threads, int cityId) throws InterruptedException {
        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
        	@Override
			public void uncaughtException(Thread t, Throwable e) {
              LogHelper.logInFile(t, e);
			}
                
        });
        ExecutorService exec = Executors.newFixedThreadPool(threads + 1);
        exec.execute(new UrlGetter(cityId, 100));
        for (int thread = 0; thread < threads; thread++)
                exec.execute(new Crawler());
        while (!(urls.isEmpty() && finish)) {
                ;
        }
        exec.shutdownNow();
        exec.awaitTermination(30, TimeUnit.SECONDS);
    }

    private static class Crawler implements Runnable {
	    public void run() {
	        while(!Thread.interrupted()) {
	            try {
	                QueryResult query = urls.take();
	                String url= query.getUrl();
	                if(fetchingUrl.contains(url))
	                        continue;
	                if (DataManager.isTravelExist(url)) {
	                        continue;
	                }
	                fetchingUrl.add(url);
	                Document doc = Jsoup.connect(url)
	                                .header("Accept", "text/html").timeout(0).get();
	                while(doc == null)
	                        doc = Jsoup.connect(url).
	                        header("Accept", "text/html").timeout(0).get();
	                System.out.println("start:" + url);
	                Travel t;
	                if (doc.select("div.main").size() > 0)
	                        t = vipParse(query.getCityId(), url, doc);
	                else
	                        t = normalParse(query.getCityId(),url, doc);
	                DataManager.addTravel(t);
	                System.out.println("end:  " + url);
	
	            } catch (InterruptedException e) {
	                    System.err.println("爬取线程被中断");
	                    e.printStackTrace();
	            } catch (IOException e) {
	                    System.err.println("爬取超时");
	                    e.printStackTrace();
	            }
	        }
	    }
    }

    private static class UrlGetter implements Runnable {
        private int cityId;
        private int queryNums;
        public UrlGetter(int cityId, int queryNums) {
                this.cityId = cityId;
                this.queryNums = queryNums;
        }
        public void run() {
            Set<QueryResult> result = new CopyOnWriteArraySet<QueryResult>();
            result.addAll(DataManager.getUrl(cityId, queryNums));
            try {
                    for (QueryResult qr : result) {                              
                            urls.put(qr);
                    }
            } catch (InterruptedException e) {
                    System.err.println("从数据库读url线程中断");
                    e.printStackTrace();
            }
            finish = true;
        }
    }

    public static void main(String[] args) throws InterruptedException{
            CrawlerFrame cf = new CrawlerFrame();
            cf.start(8, 13083);
    }

    private static Travel vipParse(int cityId,String url, Document doc) {
            Elements authorAbout = doc.select("div.person");
            String author = authorAbout.select("strong").select("a").get(0).text();
            String pubTime = authorAbout.select("div.vc_time > span.time").get(0).text();
            Elements travelTimeEles = doc.select("div.guide_con")
                            .select("li").select("strong");
            String travelTime;
            Timestamp travelTimestamp;
            if (travelTimeEles.size() == 0) {
                    travelTime = null;
                    travelTimestamp = null;
            }
            else {
                    travelTime = travelTimeEles.get(0).text();
                    travelTimestamp = ParseTime2Timestamp.parseTimestamp(travelTime);
            }
            String title = doc.title();
            Elements eles = doc.select("div.va_con");
            StringBuilder result = new StringBuilder();
            for (Element ele : eles) {
                    List<Node> nodes = ele.childNodes();
                    int pIndex = 0;
                    int divIndex = 0;
                    Elements ps = ele.select("p");
                    Elements divs = ele.select("div");
                    for(Node node : nodes) {
                            if (node.nodeName().equals("p")) {
                                    Element p = ps.get(pIndex++);
                                    int aIndex = 0;
                                    Elements as = p.select("a");
                                    List<Node> insideNodes = node.childNodes();
                                    for (Node insideNode : insideNodes) {

                                            if (insideNode.nodeName().equals("#text")) {
                                                    result.append(insideNode.toString());
                                            }
                                            else if (insideNode.nodeName().equals("a")) {
                                                    Element a = as.get(aIndex++);
                                                    result.append(a.text()).append("(")
                                                    .append(a.absUrl("href")).append(")");
                                            }
                                            else if (insideNode.nodeName().equals("br")) {
                                                    result.append("\r\n");
                                            }

                                    }
                            } else if (node.nodeName().equals("div")) {
                                    for (Element e : divs.get(divIndex++).select("a"))
                                    result.append(e.text()).append("(")
                                                    .append(e.absUrl("href")).append(")\r\n");
                            }
                            result.append("\r\n");
                    }
            }
            return new Travel(cityId, title,
                            EmojiFilter.emojiFilt(result.toString().replaceAll("&nbsp;", " ")),
                            author, ParseTime2Timestamp.parseTimestamp(pubTime),
                            travelTimestamp, url);
    }

    private static Travel normalParse (int cityId, String url, Document doc) {
            String title = doc.title();
            Elements authorAbout = doc.select("div.fl");
            String author = authorAbout.select("a").get(0).text();
            String pubTime = authorAbout.select("span.date").get(0).text();
            Elements travelTimeEles = doc.select("div.guide_con")
                            .select("li").select("strong");
            String travelTime;
            Timestamp travelTimestamp;
            if (travelTimeEles.size() == 0) {
                    travelTime = null;
                    travelTimestamp = null;
            }
            else {
                    travelTime = travelTimeEles.get(0).text();
                    travelTimestamp = ParseTime2Timestamp.parseTimestamp(travelTime);
            }
            StringBuilder result = new StringBuilder();
            Elements eles = doc.select("div.post_wrap "
                            + "> div.post_main > div.post_item > div.post_info"
                            + " > div.a_con_text.cont > p");
            for (Element ele : eles) {
                    List<Node> nodes = ele.childNodes();
                    Elements places = ele.select("a");
                    Elements images = ele.select("img");
                    int placeIndex = 0;
                    int imgIndex = 0;
                    for(Node node : nodes) {
                            if (node.nodeName().equals("#text")) {
                                    result.append(node.toString());
                            } else if (node.nodeName().equals("a")) {
                                    Element place = places.get(placeIndex++);
                                    result.append(place.text()).append("(")
                                                    .append(place.absUrl("href")).append(")");
                            } else if (node.nodeName().equals("img")) {
                                    Element image = images.get(imgIndex++);
                                    result.append("(").append(image.absUrl("src")).append(")");
                            }
                    }
                    result.append("\r\n");
            }
            return new Travel(cityId, title,
                            EmojiFilter.emojiFilt(result.toString().replaceAll("&nbsp;", " ")),
                                author, ParseTime2Timestamp.parseTimestamp(pubTime),
                                travelTimestamp, url);
        }
}