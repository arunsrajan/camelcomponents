package org.singam.camel.component.hornetq;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Hornetq consumer.
 */
public class HornetqConsumer extends ScheduledPollConsumer {
    private final HornetqEndpoint endpoint;
    private final ClientSession session;
    private final ClientConsumer consumer;
    private static final Logger LOG = LoggerFactory.getLogger(HornetqConsumer.class);
    public HornetqConsumer(HornetqEndpoint endpoint, Processor processor, ClientSession session, String queueOrTopicName, String type) throws Exception {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.session = session;
        if (type == null) {
        	throw new Exception("Type "+type+" Not Supported");
        }
        else if(type.equalsIgnoreCase("queue")){
        	consumer = session.createConsumer(queueOrTopicName);
        }
        else if(type.equalsIgnoreCase("topic")){
        	consumer = session.createConsumer(queueOrTopicName);
        }
        else {
        	throw new Exception("Type "+type+" Not Supported");
        }
        session.start();
        
    }

    @Override
    protected int poll() throws Exception {
        
    	Exchange exchange = null;
        try {
        	ClientMessage messageReceived = (ClientMessage) consumer.receive(600);
        	if(messageReceived!=null) {
        		LOG.info("Consumer: "+messageReceived);
        		exchange = endpoint.createExchange();
                exchange.getIn().setBody(messageReceived);
        		getProcessor().process(exchange);
        	}
            return 1; 
        } finally {
            if (exchange != null && exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
}
