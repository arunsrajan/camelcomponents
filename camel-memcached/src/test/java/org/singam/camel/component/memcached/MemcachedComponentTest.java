package org.singam.camel.component.memcached;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import net.spy.memcached.internal.OperationFuture;

public class MemcachedComponentTest extends CamelTestSupport {

    @SuppressWarnings("unchecked")
	@Test
    public void testMemcached() throws Exception {
    	Thread.sleep(5000);
        MockEndpoint mock = getMockEndpoint("mock:set");
        mock.expectedMinimumMessageCount(1);
        OperationFuture<Boolean> future = (OperationFuture<Boolean>) mock.getExchanges().get(0).getIn().getBody();
        assertEquals(true, future.get());
        assertMockEndpointsSatisfied();
        mock = getMockEndpoint("mock:setget");
        mock.expectedMinimumMessageCount(1);
        String result = (String) mock.getExchanges().get(0).getIn().getBody();
        assertEquals("Camel Memcached component testing", result);
        assertMockEndpointsSatisfied();
        mock = getMockEndpoint("mock:prepend");
        mock.expectedMinimumMessageCount(1);
        future = (OperationFuture<Boolean>) mock.getExchanges().get(0).getIn().getBody();
        assertEquals(true, future.get());
        assertMockEndpointsSatisfied();
        mock = getMockEndpoint("mock:prependget");
        mock.expectedMinimumMessageCount(1);
        result = (String) mock.getExchanges().get(0).getIn().getBody();
        assertEquals("PrependCamel Memcached component testing", result);
        assertMockEndpointsSatisfied();
        mock = getMockEndpoint("mock:append");
        mock.expectedMinimumMessageCount(1);
        future = (OperationFuture<Boolean>) mock.getExchanges().get(0).getIn().getBody();
        assertEquals(true, future.get());
        assertMockEndpointsSatisfied();
        mock = getMockEndpoint("mock:appendget");
        mock.expectedMinimumMessageCount(1);
        result = (String) mock.getExchanges().get(0).getIn().getBody();
        assertEquals("PrependCamel Memcached component testingAppend", result);
        assertMockEndpointsSatisfied();
        mock = getMockEndpoint("mock:delete");
        mock.expectedMinimumMessageCount(1);
        future = (OperationFuture<Boolean>) mock.getExchanges().get(0).getIn().getBody();
        assertEquals(true, future.get());
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("timer://foo?delay=100")
                .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_SET))
                .setHeader(MemcachedOperations.MEMCACHED_EXPIRES,constant("900"))
                .setBody(constant("Camel Memcached component testing"))
                  .to("memcached://bar?MemcachedKey=cacheKey")
                  .log("${body}")
                  .to("mock:set")
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_GET))
                  .to("memcached://bar?MemcachedKey=cacheKey")
                  .log("${body}")
                  .to("mock:setget")
                  .setBody(constant("Prepend"))
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_PREPEND))
                  .to("memcached://bar?MemcachedKey=cacheKey")
                  .log("${body}")
                  .to("mock:prepend")
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_GET))
                  .to("memcached://bar?MemcachedKey=cacheKey")
                  .log("${body}")
                  .to("mock:prependget")
                  .setBody(constant("Append"))
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_APPEND))
                  .to("memcached://bar?MemcachedKey=cacheKey")
                  .log("${body}")
                  .to("mock:append")
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_GET))
                  .to("memcached://bar?MemcachedKey=cacheKey")
                  .log("${body}")
                  .to("mock:appendget")
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_DELETE))
                  .to("memcached://bar?MemcachedKey=cacheKey")
                  .log("Deleted Key ${body}")
                  .to("mock:delete")
                  .setHeader(MemcachedOperations.MEMCACHED_OPERATION,constant(MemcachedOperations.MEMCACHED_STATUS))
                  .to("memcached://bar?MemcachedKey=cacheKey")
                  .log("Memcached Status ${body}");
            }
        };
    }
}
