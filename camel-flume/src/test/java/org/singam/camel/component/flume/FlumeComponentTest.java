package org.singam.camel.component.flume;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class FlumeComponentTest extends CamelTestSupport {

    @Test
    public void testFlume() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(5);       
        
        assertMockEndpointsSatisfied();
        
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("timer:flume?delay=1000").setBody(constant("Trasfer")).
                to("flume://endpoint?host=127.0.0.1&port=34343&protocol=thrift")
                  ;
                from("flume://endpoint2?host=127.0.0.1&port=34344&protocol=avro").log("${body}");
            }
        };
    }
}
