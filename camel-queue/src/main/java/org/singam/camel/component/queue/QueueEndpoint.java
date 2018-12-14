package org.singam.camel.component.queue;

import java.util.Queue;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Queue endpoint.
 */
@UriEndpoint(firstVersion = "1.0.0", scheme = "queue", title = "Queue", syntax="queue:name", 
             consumerClass = QueueConsumer.class, label = "custom")
public class QueueEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;

    public Queue queue = new java.util.concurrent.LinkedBlockingQueue<>();
    
    public QueueEndpoint() {
    }

    public QueueEndpoint(String uri, QueueComponent component) {
        super(uri, component);
    }

    public QueueEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new QueueProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new QueueConsumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Some description of this option, and what it does
     */
    public void setOption(int option) {
        this.option = option;
    }

    public int getOption() {
        return option;
    }
}
