package org.singam.camel.component.memcached;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Endpoint;

import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link MemcachedEndpoint}.
 */
public class MemcachedComponent extends DefaultComponent {
    
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new MemcachedEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
