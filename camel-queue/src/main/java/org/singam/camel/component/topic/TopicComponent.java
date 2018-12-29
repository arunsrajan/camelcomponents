package org.singam.camel.component.topic;

import java.util.Hashtable;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link TopicEndpoint}.
 */
public class TopicComponent extends DefaultComponent {
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
    	int index = uri.indexOf("?");
        String urlpattern;
        if(index!=-1) {
        	urlpattern = uri.substring(0, index);
        }
        else {
        	urlpattern = uri;
        }
        TopicEndpoint endpoint = new TopicEndpoint(urlpattern, uri, this);
	    setProperties(endpoint, parameters);
	    return endpoint;
    }
}
