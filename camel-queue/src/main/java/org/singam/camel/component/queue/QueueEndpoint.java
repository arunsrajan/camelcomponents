package org.singam.camel.component.queue;

import java.util.Hashtable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Queue endpoint.
 */
@UriEndpoint(firstVersion = "1.0.0", scheme = "queue", title = "Queue", syntax="queue:name", 
             consumerClass = QueueConsumer.class, label = "custom")
public class QueueEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;
    
    @UriParam
    private long polldelay = 1000;
    
    Queue queue;
    
    static Map<String,Queue> urlQueueMap = new Hashtable<>();
    
    public QueueEndpoint() {
    }

    public QueueEndpoint(String uri, QueueComponent component) {
        super(uri, component);
        int index = uri.indexOf("?");
        String urlpattern;
        if(index!=-1) {
        	urlpattern = uri.substring(0, index);
        }
        else {
        	urlpattern = uri;
        }
        synchronized (this){
        	if(urlQueueMap.get(urlpattern)!=null) {
        		queue = urlQueueMap.get(urlpattern);
        	}
        	else {
        		queue = new LinkedBlockingQueue<>();
        		urlQueueMap.put(urlpattern, queue);
        	}
		}
    }

    public QueueEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new QueueProducer(this,polldelay);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new QueueConsumer(this, processor, polldelay);
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setOption(int option) {
        this.option = option;
    }

    public int getOption() {
        return option;
    }

	public long getPolldelay() {
		return polldelay;
	}
	
	/**
	 * The consumer delay in fetching the data from the queue
	 * @param polldelay
	 */
	public void setPolldelay(long polldelay) {
		this.polldelay = polldelay;
	}

	public Queue getQueue() {
		return queue;
	}
	/**
	 * The queue Type
	 * @param queue
	 */
	public void setQueue(Queue queue) {
		this.queue = queue;
	}
    
    
}
