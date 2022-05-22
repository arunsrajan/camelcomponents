package org.singam.camel.component.pulsar;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.util.FileUtil;
import org.junit.Test;

import java.io.File;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class PulsarComponentChunkingTest extends CamelTestSupport {

    private final EventBusHelper eventBusHelper = EventBusHelper.getInstance();

    @Test
    public void testChunkingFiles() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(5);

        // Trigger events to subscribers
        simulateEventTrigger();

        mock.await();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        FileUtil.removeDir(new File("target/chunkfiledir"));
        File chunkfiledir = new File("target/chunkfiledir");
        chunkfiledir.mkdirs();
        FileUtil.copyFile(new File("src/test/resources/1987.csv"), new File("target/chunkfiledir/chunkfile.csv"));
        return new RouteBuilder() {
            public void configure() {
                from("pulsar://localhost:6650?topic=mytopic1&subscription=mysub&failOverDelay=30&switchBackDelay=30&receiverQueueSize=5&subscriptionType=Shared")
                  .log("In Consumer message = ${body.size()} ${body.getMessageId()} ${body.getSequenceId()}")
                  .to("mock:result");

                from("file:target/chunkfiledir?fileName=chunkfile.csv")
                        .to("pulsar://localhost:6650?topic=mytopic1&subscription=mysub&failOverDelay=30&switchBackDelay=30&enableChunking=true&enableBatching=false");

            }
        };
    }

    private void simulateEventTrigger() {
        final TimerTask task = new TimerTask() {
            @Override
            public void run() {
                final Date now = new Date();
                // publish events to the event bus
                eventBusHelper.publish(now);
            }
        };

        new Timer().scheduleAtFixedRate(task, 1000L, 1000L);
    }
}
