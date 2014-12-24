package com.qunar.liwei.graduation.weibo_crawler;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.qunar.liwei.graduation.weibo_crawler.util.LogHelper;
import com.qunar.liwei.graduation.weibo_crawler.util.LoopList;
import com.qunar.liwei.graduation.weibo_crawler.util.ParseTime2String;
import com.qunar.liwei.graduation.weibo_crawler.util.ParseTime2Timestamp;


public class HtmlPageParse {
        /**
         * all Cookies
         */
        private static LoopList<Map<String,String>> cookiesAsMapList
                = CookieAbout.getCookiesList();

        // cookie循环的计数器 和 cookie的一个外部保存
        static AtomicInteger count = new AtomicInteger();
        static Map<String,String> cookie = cookiesAsMapList.next();
        /**
         * 所有获取网页都必须经过的方法
         * @param url 要获取的url
         * @return 返回Document
         */
        public static Document getDoc(String url){
                Document doc = null;
                try {
                        count.incrementAndGet();
                        if (count.get() % 50 == 0){
                                count.set(0);
                                System.out.println("休眠" + ",刚才用的:" + cookie );
                                cookie = cookiesAsMapList.next();
                                TimeUnit.SECONDS.sleep(30);
                        }
                        doc = Jsoup.connect(url)
                                          .userAgent("Mozilla")
                                          .cookies(cookie)
                                          .timeout(20000)
                                          .get();
                } catch (IOException e) {
                        System.err.printf("fetch %s failed" , url);
                        LogHelper.logInFile(Thread.currentThread(), e);
                } catch (InterruptedException e) {
                        LogHelper.logInFile(Thread.currentThread(), e);
                        e.printStackTrace();
                }
                return doc;
        }
        // 开始获取一个页面的微博的主程序和辅助程序
        public static List<Weibo> getWeibosFromPage(String url){
                Document doc = getDoc(url);
                Elements weiboClasses = doc.select("div.c");
                List<Weibo> weiboList = new ArrayList<>(10);
                for (Element weiboClass : weiboClasses) {
                        if (!weiboClass.id().contains("M_"))
                                continue;
                        weiboList.add(analysisPage(weiboClass));
                }
                return weiboList;
        }
        private static Weibo analysisPage(Element outEle) {
                Element firstDiv = outEle.child(0);
                String userName = getName(firstDiv);
                String rowContext = getCommentText(firstDiv);
                Elements forwards = firstDiv.select("span.cmt");
                String type = "原创";
                String from = null;
                String forwardReason = null;
                if (forwards.size() > 0) {
                        type = "转发";
                        from = forwards.get(0).child(0).text();
                        forwardReason = outEle.select("span.cmt:containsOwn(转发理由:)").
                                        parents().get(0).childNode(1).toString().replaceAll("&nbsp;", " ");;
                }
                String imageUrl = getImageUrl(outEle);
                Timestamp time = getTime(outEle);
                Weibo weibo = new Weibo(userName, rowContext,
                                type, from, forwardReason, time, imageUrl);
                return weibo;
        }
        private static String getName(Element firstDiv) {
                return firstDiv.child(0).text();
        }
        private static String getCommentText(Element firstDiv) {
                return firstDiv.select("span.ctt").get(0).
                text().replaceAll("&nbsp;", " ");
        }
        private static String getImageUrl(Element ele) {
                String imageUrl = ele.select("a:containsOwn(原图)").attr("href");
                if (imageUrl.equals(""))
                        return null;
                else {
                        String imageId = imageUrl.substring(
                                        imageUrl.lastIndexOf("=") + 1);
                        return "http://ww2.sinaimg.cn/large/"
                        + imageId + ".jpg";
                }
        }
        private static Timestamp getTime(Element ele) {
                String timeString =
                        ele.select("span.ct").get(0).text().split(" ")[0];
                Timestamp time = ParseTime2Timestamp.parseTimestamp(
                                ParseTime2String.processTime(timeString));
                return time;
        }
        //结束获取一个页面的部分


        public static NameAndFollows parseMainUser(Document doc) throws IOException {
                String name = doc.title().substring(0, doc.title().lastIndexOf("的"));
                Elements userAbout = doc.select("div.tip2");
                Elements follow = userAbout.get(0).select("a:matches(关注)");
                String followHref = follow.get(0).absUrl("href");
                Set<String> followNameSet = new LinkedHashSet<String>();
                Set<String> followUrlSet = new LinkedHashSet<String>();
                preParseAndAddFollow(followHref, followNameSet, followUrlSet);
                return new NameAndFollows(name, followNameSet, followUrlSet);
        }

        private static void preParseAndAddFollow(
                        String followHref, Set<String> followNameSet, Set<String> followUrlSet)
                        throws IOException{
                int pageNums = getPageNums(followHref);
                for (int pageIndex = 1; pageIndex <= pageNums; pageIndex++) {
                        parseAndAddFollow(followHref + "?page=" + pageIndex,
                                        followNameSet, followUrlSet);
                }
        }
        private static int getPageNums(String followHref)
                        throws IOException {
                Document doc = getDoc(followHref);
                while (doc == null)
                        doc = getDoc(followHref);
                Elements elems = doc.select("div.pa");
                Matcher matcher = Pattern.compile("[\\d]+/([\\d]+)").matcher(elems.get(0).text());
                if (matcher.find())
                        return Integer.parseInt(matcher.group(1));
                throw new RuntimeException("关注页数无法读取");
        }
        private static void parseAndAddFollow(String url, Set<String> followNameSet
                        , Set<String> followUrlSet)
                        throws IOException {
                Document doc = getDoc(url);
                while (doc == null)
                        doc = getDoc(url);
                Elements elms = doc.select("table > tbody > tr");
                for (Element elm : elms) {
                        String followUrl = elm.select("td").get(1).select("a").get(0).attr("href");
                        String followName = elm.select("td").get(1).select("a").get(0).text();
                        followNameSet.add(followName);
                        followUrlSet.add(followUrl);
                }
        }
        public static int getPageNums(Document doc)
                        throws IOException {
                Elements elems = doc.select("div.pa");
                if (elems.size() < 1)
                        return 1;
                Matcher matcher = Pattern.compile("[\\d]+/([\\d]+)").matcher(elems.get(0).text());
                if (matcher.find())
                        return Integer.parseInt(matcher.group(1));
                throw new RuntimeException("关注页数无法读取");
        }

        public static String getName(Document doc)
                        throws IOException {
                return doc.title().substring(0, doc.title().lastIndexOf("的"));
        }

}