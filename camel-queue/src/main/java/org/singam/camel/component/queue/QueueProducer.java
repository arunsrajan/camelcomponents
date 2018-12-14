package org.singam.camel.component.queue;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Queue producer.
 */
public class QueueProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(QueueProducer.class);
    private QueueEndpoint endpoint;

    public QueueProducer(QueueEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        endpoint.queue.offer(exchange.getIn().getBody());    
    }

}
