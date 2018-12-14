package org.singam.camel.component.queue;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link QueueEndpoint}.
 */
public class QueueComponent extends DefaultComponent {
    
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new QueueEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
