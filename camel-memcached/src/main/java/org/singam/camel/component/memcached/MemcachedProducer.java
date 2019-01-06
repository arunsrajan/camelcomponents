package org.singam.camel.component.memcached;

import java.net.InetSocketAddress;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.spy.memcached.MemcachedClient;

/**
 * The Memcached producer.
 */
public class MemcachedProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(MemcachedProducer.class);
    private MemcachedEndpoint endpoint;
    private String host; 
    private int port; 
    private String memcachedDefaultOperation;
    private MemcachedClient memcachedClient;
    private String memcachedKey; 
    public MemcachedProducer(MemcachedEndpoint endpoint, String host, int port, String memcachedDefaultOperation,
    		String memcachedKey) {
        super(endpoint);
        this.endpoint = endpoint;
        this.host = host;
        this.port = port;
        this.memcachedDefaultOperation = memcachedDefaultOperation;
        this.memcachedKey = memcachedKey;
    }

    public void process(Exchange exchange) throws Exception {
        String operationHeader = (String) exchange.getIn().getHeader(MemcachedOperations.MEMCACHED_OPERATION);
        if(operationHeader==null) {
        	operationHeader = memcachedDefaultOperation;
        }
        if(!(operationHeader.equals(MemcachedOperations.MEMCACHED_GET)||
        		operationHeader.equals(MemcachedOperations.MEMCACHED_SET)||
        		operationHeader.equals(MemcachedOperations.MEMCACHED_PREPEND)||
        		operationHeader.equals(MemcachedOperations.MEMCACHED_APPEND)||
        		operationHeader.equals(MemcachedOperations.MEMCACHED_DELETE)||
        		operationHeader.equals(MemcachedOperations.MEMCACHED_STATUS))) {
        	throw new UnsupportedOperationException();
        }
        else {
        	Message message = exchange.getIn();
        	String key = memcachedKey == null?(String) message.getHeader(MemcachedOperations.MEMCACHED_KEY):memcachedKey;
        	if(key == null) {
        		throw new Exception("Key must be provided for Memcached Operation");
        	}
        	Object value = message.getBody();
        	if(operationHeader.equals(MemcachedOperations.MEMCACHED_GET)) {
        		message.setBody(memcachedClient.get(key));
        	}
        	if(operationHeader.equals(MemcachedOperations.MEMCACHED_DELETE)) {
        		message.setBody(memcachedClient.delete(key));
        	}
        	if(operationHeader.equals(MemcachedOperations.MEMCACHED_STATUS)) {
        		message.setBody(memcachedClient.getStats());
        	}
        	else if(operationHeader.equals(MemcachedOperations.MEMCACHED_SET)) {
        		String exp = (String) message.getHeader(MemcachedOperations.MEMCACHED_EXPIRES);
        		if(exp ==null) {
        			throw new Exception("Expires must be provided for Memcached Set Operation");
        		}
        		else if(!exp.matches("\\d+")) {
        			throw new Exception("Expires must be number");
        		}
        		Integer expires = Integer.parseInt(exp);
        		message.setBody(memcachedClient.set(key, expires, value));
        	}
        	else if(operationHeader.equals(MemcachedOperations.MEMCACHED_PREPEND)) {
        		message.setBody(memcachedClient.prepend(key, value));
        	}
        	else if(operationHeader.equals(MemcachedOperations.MEMCACHED_APPEND)) {
        		message.setBody(memcachedClient.append(key, value));
        	}
        }
    }

    @Override
    protected void doStart() throws Exception {
    	memcachedClient = new MemcachedClient(new InetSocketAddress(host, port));
    	super.doStart();
    }
    @Override
    protected void doStop() throws Exception {
    	if(memcachedClient != null) {
    		memcachedClient.shutdown();
    	}
    	super.doStop();
    }
}
