package org.singam.camel.component.hornetq;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link HornetqEndpoint}.
 */
public class HornetqComponent extends DefaultComponent {
    private static final String QUEUENAME = "name";
    
    private String type = "queue";

    private String prefix = "jms.queue.";
    
    
    private String host = "localhost";

    
    private int port = 5445;
	
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new HornetqEndpoint(uri, this);
        Map<String,Object> propertyMap = new HashMap<>();
        propertyMap.put("host", host);
        propertyMap.put("port", port);       
        propertyMap.put("prefix", prefix);
        propertyMap.put("type", type);
        setProperties(endpoint, propertyMap);
        if(remaining!=null) {
        	parameters.put(QUEUENAME, remaining);
        }
        setProperties(endpoint, parameters);
        return endpoint;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
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
    
    
    
}
