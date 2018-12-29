package org.singam.camel.component.topic;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CyclicBarrier;
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
 * Represents a Topic endpoint.
 */
@UriEndpoint(firstVersion = "1.0.0", scheme = "topic", title = "Topic", syntax="topic:name", 
             consumerClass = TopicConsumer.class, label = "custom")
public class TopicEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;
    
    @UriParam
    private long polldelay = 1000;
    
    String urlpattern;
    Queue queue;
    CyclicBarrier barrier = null;
    static Map<String,Queue> urlQueueMap = new Hashtable<>();
    static Map<String,CyclicBarrier> barrierMap = new Hashtable<>();
    static Map<String,Integer> countMap = new Hashtable<>();
    static Map<String,List<TopicEndpoint>> endpointMap = new Hashtable<>();
    public TopicEndpoint() {
    }

    public TopicEndpoint(String urlpattern, String uri, TopicComponent component) {
        super(uri, component);
        this.urlpattern = urlpattern;
    }

    public TopicEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public void queueConfigure() {
    	if(urlQueueMap.get(urlpattern)!=null) {
    		queue = urlQueueMap.get(urlpattern);
    	}
    	else {
    		queue = new LinkedBlockingQueue<>();
    		urlQueueMap.put(urlpattern, queue);
    	}
    }
    
    public void configure() {
    	synchronized (this){
        	
        	Integer count = countMap.get(urlpattern);
    		if(count==null) {
    			count = new Integer(1);
    		}
    		else {
    			count++;
    		}
    		countMap.put(urlpattern, count);
    		barrier = new CyclicBarrier(countMap.get(urlpattern),()-> {
    			queue.poll();
    		});
    		barrierMap.put(urlpattern, barrier);
    		if(endpointMap.get(urlpattern)==null) {
    			endpointMap.put(urlpattern, new ArrayList<>());
    		}
    		List<TopicEndpoint> topicList = endpointMap.get(urlpattern);
    		topicList.add(this);
    		topicList.stream().forEach(topicEndpoint -> topicEndpoint.barrier=barrier);
		}
    }
    
    
    public Producer createProducer() throws Exception {
    	queueConfigure();
        return new TopicProducer(this,polldelay);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
    	queueConfigure();
    	configure();
        return new TopicConsumer(this, processor, polldelay);
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
