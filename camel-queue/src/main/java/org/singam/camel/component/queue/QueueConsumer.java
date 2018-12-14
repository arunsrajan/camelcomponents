package org.singam.camel.component.queue;

import java.util.Date;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

/**
 * The Queue consumer.
 */
public class QueueConsumer extends ScheduledPollConsumer {
    private final QueueEndpoint endpoint;

    public QueueConsumer(QueueEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected int poll() throws Exception {
    	Exchange exchange = null;
    	 try {
	        if(endpoint.queue.peek()!=null) {
	        	exchange = endpoint.createExchange();
	        	exchange.getIn().setBody(endpoint.queue.poll());
	            getProcessor().process(exchange);
	        }
       
            
            return 1;
        } finally {
            if (exchange!=null && exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
}
