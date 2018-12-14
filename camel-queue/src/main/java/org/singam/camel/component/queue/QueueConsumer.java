package org.singam.camel.component.queue;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

/**
 * The Queue consumer.
 */
public class QueueConsumer extends ScheduledPollConsumer {
    private final QueueEndpoint endpoint;
    private final long delay;
    
    public QueueConsumer(QueueEndpoint endpoint, Processor processor,long delay) {
        super(endpoint, processor);
        this.endpoint = endpoint;
       //setDelay(delay);
        this.delay = delay;
    }

    @Override
    protected int poll() throws Exception {
    	Exchange exchange = null;
    	 try {
    		Thread.sleep(delay);
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
