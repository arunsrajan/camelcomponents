package org.singam.camel.component.pulsar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.camel.Endpoint;

import org.apache.camel.support.DefaultComponent;

import static org.singam.camel.component.pulsar.PulsarConstants.PULSARURI;

@org.apache.camel.spi.annotations.Component("pulsar")
public class PulsarComponent extends DefaultComponent {
    
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new PulsarEndpoint(uri, this);
        String[] hostports = remaining.split(",");
        List<String> hostportsList = Arrays.asList(hostports);
        hostportsList = hostportsList.stream().map(hp->PULSARURI+hp).collect(Collectors.toList());
        parameters.put(PulsarConstants.PRIMARYHOSTPORT, hostportsList.get(0));

        parameters.put(PulsarConstants.SECONDARYHOSTPORT, hostportsList);

        setProperties(endpoint, parameters);
        return endpoint;
    }
}
