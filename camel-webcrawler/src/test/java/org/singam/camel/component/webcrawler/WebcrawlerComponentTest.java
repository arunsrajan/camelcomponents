package org.singam.camel.component.webcrawler;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

public class WebcrawlerComponentTest extends CamelTestSupport {

    @Test
    public void testWebcrawler() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(3);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
    	Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    	root.setLevel(Level.INFO);
        return new RouteBuilder() {
            public void configure() {
                from("webcrawler://camel?seed=https://www.google.com/&filterPattern=css|js|gif|jpg|png|mp3|mp4|zip|gz&storageFolder=/data/crawl/root&batchSize=2")
                .log("\n\n\n\n\n${body}\n\n\n\n")
                  .to("mock:result");
            }
        };
    }
    
    @Override
	public int getShutdownTimeout() {
    	return 60000;
    }
}
