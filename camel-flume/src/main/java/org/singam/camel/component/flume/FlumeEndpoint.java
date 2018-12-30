package org.singam.camel.component.flume;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;

/**
 * Represents a Flume endpoint.
 */
@UriEndpoint(firstVersion = "1.0.0", scheme = "flume", title = "Flume", syntax="flume:name", 
             consumerClass = FlumeConsumer.class, label = "custom")
public class FlumeEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;

    
    @UriParam
    protected String host = "127.0.0.1";
    
    @UriParam
    protected int port= 34343;
    
    @UriParam
    protected String protocol = "avro";
    
    RpcClient client = null;
    
    public FlumeEndpoint() {
    }

    public FlumeEndpoint(String uri, FlumeComponent component) {
        super(uri, component);        
    }

    public void initialize() {
    	if(protocol.equals("avro")) {
        	client = RpcClientFactory.getDefaultInstance(host, port); // Avro
        }
        else if(protocol.equals("thrift")) {
        	client = RpcClientFactory.getThriftInstance(host, port);  // Thrift
        }
    }
    
    public FlumeEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
    	initialize();
        return new FlumeProducer(this, client);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new FlumeConsumer(this, processor);
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
	 * host name or ip of flume avro or thrift server 
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	/**
	 * port of flume avro or thrift server
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	/**
	 * Communication Protocol thrift or avro
	 * @param protocol
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
    
}
