package org.singam.camel.component.topic;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Topic producer.
 */
public class TopicProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(TopicProducer.class);
    private TopicEndpoint endpoint;
    private long offerdelay;
    public TopicProducer(TopicEndpoint endpoint,long offerdelay) {
        super(endpoint);
        this.endpoint = endpoint;
        this.offerdelay = offerdelay;
    }

    public void process(Exchange exchange) throws Exception {
    		Thread.sleep(offerdelay);
        	endpoint.queue.offer(exchange.getIn().getBody());    
    }

}