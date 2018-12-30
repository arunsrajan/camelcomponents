package org.singam.camel.component.flume;

import java.nio.charset.Charset;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.apache.flume.Event;
import org.apache.flume.api.RpcClient;
import org.apache.flume.event.EventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Flume producer.
 */
public class FlumeProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(FlumeProducer.class);
    private FlumeEndpoint endpoint;
    private RpcClient client;
    public FlumeProducer(FlumeEndpoint endpoint, RpcClient client) {
        super(endpoint);
        this.endpoint = endpoint;
        this.client = client;
    }

    public void process(Exchange exchange) throws Exception {
        Event event = EventBuilder.withBody((String) exchange.getIn().getBody(),Charset.forName("UTF8"));
        client.append(event);
    }

    
    @Override
    public void doStop() throws Exception {
    	client.close();
    	super.doStop();
    }
    
    
}
