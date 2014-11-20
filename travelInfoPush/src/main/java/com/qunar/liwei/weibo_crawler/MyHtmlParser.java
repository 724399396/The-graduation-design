package com.qunar.liwei.weibo_crawler;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.edu.hfut.dmic.webcollector.model.Link;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.parser.LinkUtils;
import cn.edu.hfut.dmic.webcollector.parser.ParseData;
import cn.edu.hfut.dmic.webcollector.parser.ParseResult;
import cn.edu.hfut.dmic.webcollector.parser.ParseText;
import cn.edu.hfut.dmic.webcollector.parser.Parser;
import cn.edu.hfut.dmic.webcollector.util.CharsetDetector;
import cn.edu.hfut.dmic.webcollector.util.RegexRule;

public class MyHtmlParser implements Parser {

    public RegexRule getRegexRule() {
        return regexRule;
    }

    public void setRegexRule(RegexRule regexRule) {
        this.regexRule = regexRule;
    }

    private Integer topN;
    private RegexRule regexRule=null;

    /**
     *构造一个默认的网页解析器，做链接分析时没有数量上限
     */
    public MyHtmlParser() {
        topN = null;
    }

    /**
     * 构造一个默认的网页解析器，做链接分析时只保存前topN条
     * @param topN 保存链接的上限
     */
    public MyHtmlParser(Integer topN) {
        this.topN = topN;
    }

    /**
     * 对一个页面进行解析，获取解析结果
     * @param page 待解析页面
     * @return 解析结果
     * @throws UnsupportedEncodingException
     */
    public ParseResult getParse(Page page) throws Exception{
        String url=page.getUrl();
        
        String charset = CharsetDetector.guessEncoding(page.getContent());
        
        String html=new String(page.getContent(), charset);
        page.setHtml(html);
        
        Document doc=Jsoup.parse(page.getHtml());
        doc.setBaseUri(url);      
        page.setDoc(doc);
        
        String title=doc.title();
        String text=doc.text();
        
        ArrayList<Link> links = null;
        if(topN!=null && topN==0){
            links=new ArrayList<Link>();
        }else{
            links=topNFilter(LinkUtils.getAll(page));
            for(Link link : links)
            	System.out.println(link.getUrl());
        }
        ParseData parsedata = new ParseData(url,title, links);
        ParseText parsetext=new ParseText(url,text);
        
        return new ParseResult(parsedata,parsetext);
    }

    private ArrayList<Link> topNFilter(ArrayList<Link> origin_links) {
        ArrayList<Link> result=new ArrayList<Link>();
        int updatesize;
        if (topN == null) {
            updatesize = origin_links.size();
        } else {
            updatesize = Math.min(topN, origin_links.size());
        }

        int sum = 0;
        for (int i = 0; i < origin_links.size(); i++) {
            if (sum >= updatesize) {
                break;
            }
            Link link = origin_links.get(i);
            if(!regexRule.satisfy(link.getUrl())){
                continue;
            }
            result.add(link);
            sum++;
        }
        return result;
    }

}

