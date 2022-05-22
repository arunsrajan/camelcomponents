package org.singam.camel.component.pulsar;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.support.DefaultConsumer;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.impl.AutoClusterFailover;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

public class PulsarConsumer extends DefaultConsumer {
    private final PulsarEndpoint endpoint;
    private final EventBusHelper eventBusHelper;

    private final PulsarClient client;
    private final Consumer consumer;

    private ExecutorService executorService;

    public PulsarConsumer(PulsarEndpoint endpoint, Processor processor) throws PulsarClientException {
        super(endpoint, processor);
        this.endpoint = endpoint;
        eventBusHelper = EventBusHelper.getInstance();
        AutoClusterFailoverBuilder autoClusterFailoverBuilder = AutoClusterFailover.builder()
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
        ConsumerBuilder builder = client.newConsumer()
                .topic(endpoint.getTopic());

        if (nonNull(endpoint.getSubscriptionType())) {
            builder = builder.subscriptionType(endpoint.getSubscriptionType());
        }
        if (nonNull(endpoint.getReceiverQueueSize())) {
            builder = builder.receiverQueueSize(endpoint.getReceiverQueueSize());
        }
        if (nonNull(endpoint.getAcknowledgementsGroupTime())) {
            builder = builder.acknowledgmentGroupTime(endpoint.getAcknowledgementsGroupTime(), TimeUnit.MICROSECONDS);
        }
        if (nonNull(endpoint.getNegativeAckRedeliveryDelay())) {
            builder = builder.negativeAckRedeliveryDelay(endpoint.getNegativeAckRedeliveryDelay(), TimeUnit.MICROSECONDS);
        }
        if (nonNull(endpoint.getMaxTotalReceiverQueueSizeAcrossPartitions())) {
            builder = builder.maxTotalReceiverQueueSizeAcrossPartitions(endpoint.getMaxTotalReceiverQueueSizeAcrossPartitions());
        }
        if (nonNull(endpoint.getAckTimeoutMillis())) {
            builder = builder.ackTimeout(endpoint.getAckTimeoutMillis(), TimeUnit.MILLISECONDS);
        }
        if (nonNull(endpoint.getTickDurationMillis())) {
            builder = builder.ackTimeoutTickTime(endpoint.getTickDurationMillis(), TimeUnit.MILLISECONDS);
        }
        if (nonNull(endpoint.getPriorityLevel())) {
            builder = builder.priorityLevel(endpoint.getPriorityLevel());
        }
        if (nonNull(endpoint.getCryptoFailureAction())) {
            builder = builder.cryptoFailureAction(endpoint.getCryptoFailureAction());
        }
        if (nonNull(endpoint.getReadCompacted())) {
            builder = builder.readCompacted(endpoint.getReadCompacted());
        }
        if (nonNull(endpoint.getSubscriptionInitialPosition())) {
            builder = builder.subscriptionInitialPosition(endpoint.getSubscriptionInitialPosition());
        }
        if (nonNull(endpoint.getPatternAutoDiscoveryPeriod())) {
            builder = builder.patternAutoDiscoveryPeriod(endpoint.getPatternAutoDiscoveryPeriod());
        }
        if (nonNull(endpoint.getSubscriptionMode())) {
            builder = builder.subscriptionMode(endpoint.getSubscriptionMode());
        }
        if (nonNull(endpoint.getDeadLetterPolicy())) {
            builder = builder.deadLetterPolicy(endpoint.getDeadLetterPolicy());
        }
        if (nonNull(endpoint.getAutoUpdatePartitions())) {
            builder = builder.autoUpdatePartitions(endpoint.getAutoUpdatePartitions());
        }
        if (nonNull(endpoint.getReplicateSubscriptionState())) {
            builder = builder.replicateSubscriptionState(endpoint.getReplicateSubscriptionState());
        }
        if (nonNull(endpoint.getNegativeAckRedeliveryBackoff())) {
            builder = builder.negativeAckRedeliveryBackoff(endpoint.getNegativeAckRedeliveryBackoff());
        }
        if (nonNull(endpoint.getAckTimeoutRedeliveryBackoff())) {
            builder = builder.ackTimeoutRedeliveryBackoff(endpoint.getAckTimeoutRedeliveryBackoff());
        }
        if (nonNull(endpoint.getAutoAckOldestChunkedMessageOnQueueFull())) {
            builder = builder.autoAckOldestChunkedMessageOnQueueFull(endpoint.getAutoAckOldestChunkedMessageOnQueueFull());
        }
        if (nonNull(endpoint.getMaxPendingChunkedMessage())) {
            builder = builder.maxPendingChunkedMessage(endpoint.getMaxPendingChunkedMessage());
        }
        if (nonNull(endpoint.getExpireTimeOfIncompleteChunkedMessageMillis())) {
            builder = builder.expireTimeOfIncompleteChunkedMessage(endpoint.getExpireTimeOfIncompleteChunkedMessageMillis(), TimeUnit.MILLISECONDS);
        }
        if (nonNull(endpoint.getSubscription())) {
            builder = builder.subscriptionName(endpoint.getSubscription());
        }
        consumer = builder.subscribe();
    }

    @Override
    protected void doStart() throws Exception {
        super.doStart();

        // start a single threaded pool to monitor events
        executorService = endpoint.createExecutor();

        // submit task to the thread pool
        executorService.submit(() -> {
            // subscribe to an event
            eventBusHelper.subscribe(this::onEventListener);
        });
    }

    @Override
    protected void doStop() throws Exception {
        super.doStop();

        // shutdown the thread pool gracefully
        getEndpoint().getCamelContext().getExecutorServiceManager().shutdownGraceful(executorService);
    }

    private void onEventListener(final Object event) {
        final Exchange exchange = createExchange(false);

        try {
            // Wait for a message
            CompletableFuture<Message> future = consumer.receiveAsync();
            // Acknowledge the message so that it can be deleted by the message broker
            Message message = future.get();
            exchange.getIn().setBody(message);
            // send message to next processor in the route
            getProcessor().process(exchange);
            consumer.acknowledge(message);
        } catch (Exception e) {
            exchange.setException(e);
        } finally {
            if (exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
            releaseExchange(exchange, false);
        }
    }
}
