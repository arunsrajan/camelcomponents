package org.singam.camel.component.topic;

import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class TopicComponentTest extends CamelTestSupport {

	@Test
	public void testQueue() throws Exception {
		int numConsumer = 3;
		int numMessages = 10;
		for(int count=0;count<numMessages;count++) {
			ProducerTemplate template = context.createProducerTemplate();
			Exchange exchange = new DefaultExchange(context);
			exchange.getIn().setBody(count);
			template.send("direct:testtopic", exchange);
		}
		MockEndpoint mock = getMockEndpoint("mock:result");
		mock.expectedMinimumMessageCount(numMessages);

		assertMockEndpointsSatisfied();

		mock = getMockEndpoint("mock:result1");
		mock.expectedMinimumMessageCount(numConsumer*numMessages);

		assertMockEndpointsSatisfied();
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			public void configure() {
				from("direct:testtopic").log("${body}").to("topic:bar?polldelay=100")
				.to("topic:bar1?polldelay=170").to("mock:result");

				from("topic:bar?polldelay=160").log("${body}1").to("mock:result1");
				from("topic:bar?polldelay=200").log("${body}2").to("mock:result1");
				
				from("topic:bar1?polldelay=150").log("${body}3").to("mock:result1");
				//from("topic:bar1?polldelay=130").log("${body}4").to("mock:result1");
				//from("topic:bar1?polldelay=120").log("${body}5").to("mock:result1");

			}
		};
	}
}
