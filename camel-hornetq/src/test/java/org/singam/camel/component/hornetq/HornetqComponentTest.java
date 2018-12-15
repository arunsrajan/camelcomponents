package org.singam.camel.component.hornetq;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class HornetqComponentTest extends CamelTestSupport {
	
    @Test
    public void testHornetq() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(3);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("hornetq:ExampleQueue1?type=queue")
                  .log("\n\n\n${body}1\n\n\n")
                  .to("mock:result");
                
               from("timer:horne?delay=500")
                .setBody(constant("This is test message")).log("${body}").to("hornetq:ExampleQueue1");
            }
        };
    }
}
