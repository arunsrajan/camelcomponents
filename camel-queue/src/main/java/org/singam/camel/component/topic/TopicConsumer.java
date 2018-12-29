package org.singam.camel.component.topic;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

/**
 * The Topic consumer.
 */
public class TopicConsumer extends ScheduledPollConsumer {
    private final TopicEndpoint endpoint;
    private final long delay;
    public TopicConsumer(TopicEndpoint endpoint, Processor processor,long delay) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.delay = delay;
    }

    @Override
    protected int poll() throws Exception {
    	Exchange exchange = null;
    	 try {  
	    		Thread.sleep(delay);
    			if(endpoint.queue.peek()!=null) {
    	        	exchange = endpoint.createExchange();
    	        	exchange.getIn().setBody(endpoint.queue.peek());
    	            getProcessor().process(exchange);
    	            endpoint.barrier.await();
    	            endpoint.barrier.reset();
    	        }
            return 1;
        } finally {
            if (exchange!=null && exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
}
