package org.singam.camel.component.queue;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class QueueComponentTest extends CamelTestSupport {

    @Test
    public void testQueue() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);       
        
        assertMockEndpointsSatisfied();
        
        mock = getMockEndpoint("mock:result1");
        mock.expectedMinimumMessageCount(2);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("timer:queue?delay=2000")
                .setBody(constant("This route produces the message"))
                .log("${body}")
                  .to("queue:bar2?polldelay=100")
                  .to("queue:bar3?polldelay=100")
                  .to("mock:result");
                
                
                from("queue:bar?polldelay=2000")
                .log("${body}1")
                .to("mock:result1");
                from("queue:bar1?polldelay=2000")
                .log("${body}2")
                .to("mock:result1");
                
            }
        };
    }
}
