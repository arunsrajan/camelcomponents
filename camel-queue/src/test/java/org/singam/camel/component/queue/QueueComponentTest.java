package org.singam.camel.component.queue;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class QueueComponentTest extends CamelTestSupport {

	@Test
	public void testQueue() throws Exception {
		for(int count=0;count<2;count++) {
			ProducerTemplate template = context.createProducerTemplate();
			Exchange exchange = new DefaultExchange(context);
			exchange.getIn().setBody(count);
			template.send("direct:testqueue", exchange);
		}
		MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedMinimumMessageCount(2);

		assertMockEndpointsSatisfied();

		mock = getMockEndpoint("mock:result1");
		mock.expectedMinimumMessageCount(4);

		assertMockEndpointsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			public void configure() {
				from("direct:testqueue").log("${body}").to("queue:bar?polldelay=100")
				.to("queue:bar1?polldelay=160").to("mock:result");

				from("queue:bar?polldelay=100").log("${body}1").to("mock:result1");
				from("queue:bar1?polldelay=120").log("${body}3").to("mock:result1");
				from("queue:bar1?polldelay=100").log("${body}4").to("mock:result1");
				from("queue:bar?polldelay=150").log("${body}2").to("mock:result1");

			}
		};
	}
}
