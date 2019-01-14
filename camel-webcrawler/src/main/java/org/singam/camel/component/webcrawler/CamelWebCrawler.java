package org.singam.camel.component.webcrawler;

import java.util.Queue;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class CamelWebCrawler extends WebCrawler {
	private static Pattern patternFilter = null;
	
	Queue<Page> queue;
	
	public CamelWebCrawler(String pattern,Queue<Page> queue) {
		setPatternExpression(pattern);
		this.queue = queue;
	}
	
	
	@Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        return !patternFilter.matcher(href).matches();
    }
	
	@Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        if (page.getParseData() instanceof HtmlParseData) {
            queue.offer(page);
        }
   }
	
	public void setPatternExpression(String pattern) {
		patternFilter = Pattern.compile(".*(\\.("+pattern+"))$");
	}
	
}
