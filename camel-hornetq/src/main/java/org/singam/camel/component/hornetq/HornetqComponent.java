package org.singam.camel.component.hornetq;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link HornetqEndpoint}.
 */
public class HornetqComponent extends DefaultComponent {
    private static final String QUEUENAME = "name";
	
	
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new HornetqEndpoint(uri, this);
        if(remaining!=null) {
        	parameters.put(QUEUENAME, remaining);
        }
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
