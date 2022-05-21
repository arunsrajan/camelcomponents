package org.singam.camel.component.pulsar;

import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultProducer;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.AutoClusterFailover;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

public class PulsarProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(PulsarProducer.class);
    private final PulsarClient client;
    private final Producer<String> producer;
    private PulsarEndpoint endpoint;

    public PulsarProducer(PulsarEndpoint endpoint) throws PulsarClientException {
        super(endpoint);
        this.endpoint = endpoint;
        AutoClusterFailoverBuilder autoClusterFailoverBuilder =AutoClusterFailover.builder()
                .primary(endpoint.getPrimaryhostport());
        ServiceUrlProvider failover = autoClusterFailoverBuilder
                .secondary(endpoint.getSecondaryhostport())
                .failoverDelay(endpoint.getFailOverDelay(), TimeUnit.SECONDS)
                .switchBackDelay(endpoint.getSwitchBackDelay(), TimeUnit.SECONDS)
                .build();
        client = PulsarClient.builder()
                .serviceUrl(endpoint.getPrimaryhostport())
                .build();
        failover.initialize(client);
        ProducerBuilder builder = client.newProducer(Schema.STRING)
                .topic(endpoint.getTopic());
        if(nonNull(endpoint.getSendTimeout())){
            builder.sendTimeout(endpoint.getSendTimeout(), TimeUnit.MILLISECONDS);
        }
        if(nonNull(endpoint.getBlockIfQueueFull())){
            builder.blockIfQueueFull(endpoint.getBlockIfQueueFull());
        }
        if(nonNull(endpoint.getMaxPendingMessages())){
            builder.maxPendingMessages(endpoint.getMaxPendingMessages());
        }
        if(nonNull(endpoint.getBatchingMaxMessages())){
            builder.batchingMaxMessages(endpoint.getBatchingMaxMessages());
        }
        if(nonNull(endpoint.getMaxPendingMessagesAcrossPartitions())){
            builder.maxPendingMessagesAcrossPartitions(endpoint.getMaxPendingMessagesAcrossPartitions());
        }
        if(nonNull(endpoint.getMessageRoutingMode())){
            builder.messageRoutingMode(endpoint.getMessageRoutingMode());
        }
        if(nonNull(endpoint.getHashingScheme())){
            builder.hashingScheme(endpoint.getHashingScheme());
        }
        if(nonNull(endpoint.getCryptoFailureAction())){
            builder.cryptoFailureAction(endpoint.getProducerCryptoFailureAction());
        }
        if(nonNull(endpoint.getBatchingMaxPublishDelay())){
            builder.batchingMaxPublishDelay(endpoint.getBatchingMaxPublishDelay(), TimeUnit.MILLISECONDS);
        }
        if(nonNull(endpoint.getEnableBatching())){
            builder.enableBatching(endpoint.getEnableBatching());
        }
        if(nonNull(endpoint.getEnableChunking())){
            builder.enableChunking(endpoint.getEnableChunking());
        }
        if(nonNull(endpoint.getCompressionType())){
            builder.compressionType(endpoint.getCompressionType());
        }
        producer = builder.create();
    }

    public void process(Exchange exchange) throws Exception {
        producer.send(exchange.getIn().getBody(String.class));
    }

}
