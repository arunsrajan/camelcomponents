package org.singam.camel.component.flume;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link FlumeEndpoint}.
 */
public class FlumeComponent extends DefaultComponent {
    
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
    	FlumeEndpoint endpoint = new FlumeEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
