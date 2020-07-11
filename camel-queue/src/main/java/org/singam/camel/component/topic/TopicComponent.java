package org.singam.camel.component.topic;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link TopicEndpoint}.
 */
public class TopicComponent extends DefaultComponent {
	private long polldelay = 1000;
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
        TopicEndpoint endpoint = new TopicEndpoint(urlpattern, uri, this);
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
