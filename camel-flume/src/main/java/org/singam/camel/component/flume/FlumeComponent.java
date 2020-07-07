package org.singam.camel.component.flume;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link FlumeEndpoint}.
 */
public class FlumeComponent extends DefaultComponent {
    
    protected String host = "127.0.0.1";
    
    protected int port= 34343;
    
    protected String protocol = "avro";
	
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
    	FlumeEndpoint endpoint = new FlumeEndpoint(uri, this);
    	Map<String,Object> propertyMap = new HashMap<>();
        propertyMap.put("host", host);
        propertyMap.put("port", port);       
        propertyMap.put("protocol", protocol);
        setProperties(endpoint, propertyMap);
        setProperties(endpoint, parameters);
        return endpoint;
    }

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
    
    
    
}
