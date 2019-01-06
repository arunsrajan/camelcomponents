package org.singam.camel.component.memcached;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class MemcachedComponentTest extends CamelTestSupport {

    @Test
    public void testMemcached() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("timer://foo?delay=1000")
                .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_SET))
                .setHeader(MemcachedOperations.MEMCACHED_EXPIRES,constant("900"))
                .setBody(constant("Camel Memcached component testing1"))
                  .to("memcached://bar?MemcachedKey=arun")
                  .log("${body}")
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_GET))
                  .to("memcached://bar?MemcachedKey=arun")
                  .log("${body}")
                  .setBody(constant("Prepend"))
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_PREPEND))
                  .to("memcached://bar?MemcachedKey=arun")
                  .log("${body}")
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_GET))
                  .to("memcached://bar?MemcachedKey=arun")
                  .log("${body}")
                  .setBody(constant("Append"))
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_APPEND))
                  .to("memcached://bar?MemcachedKey=arun")
                  .log("${body}")
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_GET))
                  .to("memcached://bar?MemcachedKey=arun")
                  .log("${body}")
                  .to("mock:result");
            }
        };
    }
}
