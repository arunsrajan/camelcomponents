package org.singam.camel.component.webcrawler;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Webcrawler producer.
 */
public class WebcrawlerProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(WebcrawlerProducer.class);
    private WebcrawlerEndpoint endpoint;

    public WebcrawlerProducer(WebcrawlerEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());    
    }

}
