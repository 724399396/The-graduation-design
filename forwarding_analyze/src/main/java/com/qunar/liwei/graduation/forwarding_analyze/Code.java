package com.qunar.liwei.graduation.forwarding_analyze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class Code {
	private final BlockingQueue<Document> docToParse = new LinkedBlockingQueue<Document>();
	private final BlockingQueue<String> nickToSearch = new LinkedBlockingQueue<String>();
	private final AtomicInteger deepAto = new AtomicInteger();
	private final ExecutorService exec = null;
	private String keyword;
	private String weibo;
	class Searcher implements Runnable {
		public void run() {
			while (true) {
				try {
					Document doc = search(nickToSearch.take());
					docToParse.put(doc);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	class Saver implements Runnable {
		public void run() {
			while (true) {
				try {
					Document doc = docToParse.take();
					processSearchPage(doc, deepAto.get());
				} catch (InterruptedException e) {
				}
			}
		}
	}
	public void start() {
		Document doc = HtmlGet.search(keyword, "", "hot");
		weibo = getHotWeiboText(doc);
		System.out.println("原始weibo:" + weibo);
		processSearchPage(doc, 1);
		for (String nick : nicks2Search)
			processSearchPage(search(nick), 0);
	}

	private String getHotWeiboText(Document doc) {
		Elements weiboClasses = doc.select("div.c");
		for (Element weiboClass : weiboClasses) {
			if (weiboClass.id().contains("M_")) {
				Element firstDiv = weiboClass.child(0);
				String rowContext = getCommentText(firstDiv);
				return rowContext;
			}
		}
		System.out.println("搜索的微博不存在");
		System.exit(0);
		return null;
	}

	private static String getCommentText(Element firstDiv) {
		return firstDiv.select("span.ctt").get(0).text()
				.replaceAll("&nbsp;", " ");
	}

	

	public Document search(String nickName) {
		Document doc = HtmlGet.search(weibo, nickName, "time");
		return doc;
	}

	

	private void processSearchPage(Document doc, int deep) {
		Elements hotWeibos = doc.select("a:contains(转发)");
		if (hotWeibos.size() == 0) {
			System.out.println("搜索的微博不存在");
			return;
		}
		Element hotWeibo = hotWeibos.get(0);
		String url = hotWeibo.absUrl("href");
		int forwardingNums = Integer.parseInt(hotWeibo.text().substring(3,
				hotWeibo.text().length() - 1));
		if (forwardingNums == 0)
			return;
		int pageNums = forwardingNums / 10
				+ ((forwardingNums % 10 == 0) ? 0 : 1);
		System.out.println(url);
		System.out.println(pageNums);
		processForwardingPage(url, pageNums, deep);
	}
	List<String> nicks2Search = new LinkedList<String>();

	private void processForwardingPage(String url, int pageNums, int deep) {
		List<Document> docList = new LinkedList<Document>();
		for (int pageIndex = 1; pageIndex <= pageNums; pageIndex++) {
			docList.add(HtmlGet.getDoc(url + "&page=" + pageIndex));
		}	
		for (Document row : docList) {
			Elements forwardings = row.select("div.c:has(span.cc)");
			for (Element forwarding : forwardings) {
				String name = null;
				StringBuilder text = new StringBuilder();
				for (Node node : forwarding.childNodesCopy()) {
					if (node.nodeName().equals("#text")) {
						text.append(node.toString());
					} else if (node.nodeName().equals("a")) {
						if (name == null)
							name = getInsideText(node.toString());
						else
							text.append(getInsideText(node.toString()));
					}
				}
				System.out.println("层数:" + deep);
				System.out.println("name: " + name);
				System.out.println("text: " + text.toString());
				nicks2Search.add(name);
			}
			if (deep == 3)
				return;
			deep++;
		}
	}

	private String getInsideText(String source) {
		Matcher m = Pattern.compile(">(\\S+)<").matcher(source);
		if (m.find())
			return m.group(1).replaceAll("&nbsp;", " ");
		throw new AssertionError();
	}

	public static void main(String[] args) throws FileNotFoundException {
		System.setOut(new PrintStream(new File("outPrint.txt")));

	}
}
