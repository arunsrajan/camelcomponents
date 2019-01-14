package org.singam.camel.component.webcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.Page;

/**
 * The Webcrawler consumer.
 */
public class WebcrawlerConsumer extends ScheduledPollConsumer {
    private final WebcrawlerEndpoint endpoint;
    private final CrawlController controller;
    private final int numCrawlers;
    private final String pattern;
    private final Queue<Page> queue;
    private final int batchSize;
    public WebcrawlerConsumer(WebcrawlerEndpoint endpoint, Processor processor, CrawlController controller,
    		int numCrawlers, String pattern,int batchSize) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.controller = controller;
        this.numCrawlers = numCrawlers;
        this.pattern = pattern;
        this.queue = new LinkedBlockingQueue<>();
        this.batchSize = batchSize;
    }

    @Override
    protected int poll() throws Exception {
    	Exchange exchange = null;
        try {
        	if(queue.size()>=batchSize) {
	        	exchange = endpoint.createExchange();
	        	List<Page> pages = new ArrayList<>();
	        	int numSize=0;
	        	while(numSize<batchSize) {
	        		pages.add(queue.poll());
	        		numSize++;
	        	}
	        	exchange.getIn().setBody(pages);
	            getProcessor().process(exchange);
        	}
	            return 1;
        } finally {
            if (exchange != null && exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public void doStart() throws Exception {
    	super.doStart();
    	controller.startNonBlocking(new HtmlUrlCrawlerControllerFactory(pattern, queue), numCrawlers);
    	controller.waitUntilFinish();

    }
    @Override
    public void doStop() throws Exception {
    	super.doStop();
    	controller.shutdown();
    }
    
}
