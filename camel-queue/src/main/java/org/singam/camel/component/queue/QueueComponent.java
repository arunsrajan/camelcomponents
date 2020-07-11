package org.singam.camel.component.queue;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link QueueEndpoint}.
 */
public class QueueComponent extends DefaultComponent {
	private long polldelay = 1000;
	public static Map<String,QueueEndpoint> map = new Hashtable<String,QueueEndpoint>();
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
    	Map<String,Object> propertyMap = new HashMap<>();
        propertyMap.put("polldelay", polldelay);        
    	int index = uri.indexOf("?");
        String urlpattern;
        if(index!=-1) {
        	urlpattern = uri.substring(0, index);
        }
        else {
        	urlpattern = uri;
        }
        QueueEndpoint endpoint = new QueueEndpoint(urlpattern, uri, this);
        setProperties(endpoint, propertyMap);
        setProperties(endpoint, parameters);
        return endpoint;
       
    }
	public long getPolldelay() {
		return polldelay;
	}
	public void setPolldelay(long polldelay) {
		this.polldelay = polldelay;
	}
    
    
    
    
}
