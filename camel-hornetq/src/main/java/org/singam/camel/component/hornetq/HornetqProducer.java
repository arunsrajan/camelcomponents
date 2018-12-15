package org.singam.camel.component.hornetq;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientProducer;
import org.hornetq.api.core.client.ClientSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Hornetq producer.
 */
public class HornetqProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(HornetqProducer.class);
    private HornetqEndpoint endpoint;
    private final ClientSession session;
    private final ClientProducer producer;
    private final ClientMessage message;
    public HornetqProducer(HornetqEndpoint endpoint, ClientSession session, String queueOrTopicName, String type) throws Exception {
        super(endpoint);
        this.endpoint = endpoint;
        this.session = session;
        if (type == null) {
        	throw new Exception("Type "+type+" Not Supported");
        }
        else if(type.equalsIgnoreCase("queue")){
        	producer = session.createProducer(queueOrTopicName);
        }
        else if(type.equalsIgnoreCase("topic")){
        	producer = session.createProducer(queueOrTopicName);
        }
        else {
        	throw new Exception("Type "+type+" Not Supported");
        }
        message = session.createMessage(false);
        session.start();
    }

    public void process(Exchange exchange) throws Exception {

        final String propName = "JmsText";

        message.putStringProperty(propName, (String) exchange.getIn().getBody());

        //LOG.info(""+session);
        
        producer.send(message);
    }

}
