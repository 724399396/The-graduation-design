package com.qunar.liwei.graduation.forwarding_analyze;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class ForwardingCrawler {
	private final String keyword;
	private String weibo;
	LinkedList<Nick> nicks2Search = new LinkedList<Nick>();
	
	public ForwardingCrawler(String keyword) {
		this.keyword = keyword;
	}

	public void start() {
		Document doc = HtmlGet.search(keyword, null, "hot");
		weibo = getHotWeiboText(doc);
		System.out.println("原始weibo:" + weibo);
		String name = getHotWeiboNickName(doc);
		System.out.println("发送的人: " + name);
		processSearchPage(-1, name, doc, 1);
		while (!nicks2Search.isEmpty()) {
			Nick nick = nicks2Search.poll();
			processSearchPage(nick.id, nick.nickName,
					search(nick.nickName), nick.deep);
		}
	}

	
	/**
	 * 通过搜索到的页面分析要打开的链接还有被转发的次数
	 */
	private void processSearchPage(
			int parentId, String name, Document doc, int deep) {
		if (deep == 3)
			return;
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
		processForwardingPage(url, pageNums, parentId, deep);
	}
	/*
	 * 这个是具体的处理点开转发后界面
	 */
	private void processForwardingPage(String url, int pageNums, 
			int parentId, int deep) {
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
				System.out.println("父id: " + parentId);
				System.out.println("name: " + name);
				System.out.println("text: " + text.toString());
				nicks2Search.add(new Nick(parentId, deep + 1, name, text.toString()));
			}
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
		ForwardingCrawler fc = new ForwardingCrawler(
				"中国西电的那位");
		fc.start();
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
	
	private String getHotWeiboNickName(Document doc) {
		Elements weiboClasses = doc.select("div.c");
		for (Element weiboClass : weiboClasses) {
			if (weiboClass.id().contains("M_")) {
				Element firstDiv = weiboClass.child(0);
				return firstDiv.child(0).text();
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
}
