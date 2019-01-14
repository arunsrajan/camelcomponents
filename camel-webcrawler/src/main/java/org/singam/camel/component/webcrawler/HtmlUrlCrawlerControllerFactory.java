package org.singam.camel.component.webcrawler;

import java.util.Queue;

import edu.uci.ics.crawler4j.crawler.CrawlController.WebCrawlerFactory;
import edu.uci.ics.crawler4j.crawler.WebCrawler;

public class HtmlUrlCrawlerControllerFactory implements WebCrawlerFactory{

	private String pattern;
	private Queue queue;
	
	
	public HtmlUrlCrawlerControllerFactory(String pattern,Queue queue) {
		this.pattern = pattern;
		this.queue = queue;
	}

	@Override
	public WebCrawler newInstance() throws Exception {
		return new CamelWebCrawler(pattern,queue);
	}
	
	
}
