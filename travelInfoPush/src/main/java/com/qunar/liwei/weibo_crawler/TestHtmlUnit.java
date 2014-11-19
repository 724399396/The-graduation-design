package com.qunar.liwei.weibo_crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class TestHtmlUnit {
	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		final WebClient webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		HtmlPage htmlPage = webClient.getPage("http://login.weibo.cn/login/");
		//System.out.println(htmlPage.asText());
		final List<HtmlForm> forms = htmlPage.getForms();
		final HtmlForm form = forms.get(0);
		HtmlTextInput userName = form.getInputByName("mobile");
		HtmlPasswordInput password = form.getInputByName("passwod_4240");
		HtmlSubmitInput submit = form.getInputByName("submit");
		userName.setValueAttribute("181212631@163.com");
		password.setValueAttribute("wwee13");
		htmlPage = submit.click();
		System.out.println(htmlPage.asText());
		webClient.closeAllWindows();
	}
}
