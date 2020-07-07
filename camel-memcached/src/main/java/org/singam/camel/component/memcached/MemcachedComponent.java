package org.singam.camel.component.memcached;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link MemcachedEndpoint}.
 */
public class MemcachedComponent extends DefaultComponent {
	private String host = "127.0.0.1";
    private int port = 11211;
    private String memcachedDefaultOperation = MemcachedOperations.MEMCACHED_GET;
    private String memcachedKey = null;
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new MemcachedEndpoint(uri, this);
        Map<String,Object> propertyMap = new HashMap<>();
        propertyMap.put("host", host);
        propertyMap.put("port", port);       
        propertyMap.put("memcachedDefaultOperation", memcachedDefaultOperation);
        propertyMap.put("memcachedKey", memcachedKey);
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
	public String getMemcachedDefaultOperation() {
		return memcachedDefaultOperation;
	}
	public void setMemcachedDefaultOperation(String memcachedDefaultOperation) {
		this.memcachedDefaultOperation = memcachedDefaultOperation;
	}
	public String getMemcachedKey() {
		return memcachedKey;
	}
	public void setMemcachedKey(String memcachedKey) {
		this.memcachedKey = memcachedKey;
	}
    
    
    
    
}
