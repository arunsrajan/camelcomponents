package org.singam.camel.component.pulsar;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class PulsarComponentBatchingTest extends CamelTestSupport {

    @Test
    public void testBatchingFiles() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(10);
        mock.await();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("pulsar://localhost:6650?topic=mytopic2&subscription=mysub&failOverDelay=30&switchBackDelay=30&receiverQueueSize=100&subscriptionType=Exclusive&receiveInBatches=true")
                        .log("Total Messages: ${body.size()}")
                        .split(body())
                  .log("In Consumer message = ${body.size()} ${body.getMessageId()} ${body.getSequenceId()}")
                  .to("mock:result");

                from("timer:foo?delay=100")
                        .setBody(constant("My Topic Produced"))
                        .to("pulsar://localhost:6650?topic=mytopic2&subscription=mysub&failOverDelay=30&switchBackDelay=30&enableChunking=false&enableBatching=true");

            }
        };
    }
}
