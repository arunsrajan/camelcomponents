package org.singam.camel.component.memcached;

import javax.naming.OperationNotSupportedException;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Memcached endpoint.
 */
@UriEndpoint(firstVersion = "1.0.0", scheme = "memcached", title = "Memcached", syntax="memcached:name", 
             consumerClass = MemcachedConsumer.class, label = "custom")
public class MemcachedEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;

    @UriParam
    private String host = "127.0.0.1";
    
    @UriParam
    private int port = 11211;
    
    @UriParam
    private String memcachedDefaultOperation = MemcachedOperations.MEMCACHED_GET;
    
    @UriParam
    private String memcachedKey = null;
    
    public MemcachedEndpoint() {
    }

    public MemcachedEndpoint(String uri, MemcachedComponent component) {
        super(uri, component);
    }

    public MemcachedEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new MemcachedProducer(this, host, port, memcachedDefaultOperation, memcachedKey);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
    	throw new UnsupportedOperationException();
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

	public String getHost() {
		return host;
	}

	/**
	 * Memcached Server Host, Default 127.0.0.1
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	/**
	 * Memcached Server port, Default 11211
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public String getMemcachedDefaultOperation() {
		return memcachedDefaultOperation;
	}

	/**
	 * Memcached Default Operation, Default is Get
	 * @param memcachedDefaultOperation
	 */
	public void setMemcachedDefaultOperation(String memcachedDefaultOperation) {
		this.memcachedDefaultOperation = memcachedDefaultOperation;
	}

	public String getMemcachedKey() {
		return memcachedKey;
	}

	/**
	 * Memcached Key to store
	 * @param memcachedKey
	 */
	public void setMemcachedKey(String memcachedKey) {
		this.memcachedKey = memcachedKey;
	}
    
    
    
    
    
}
