package org.singam.camel.component.pulsar;

import org.apache.camel.Category;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.support.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;
import org.apache.pulsar.client.api.*;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Pulsar component which does fetching messages and sending
 * messages via consumer and producer respectively
 *
 * TODO: Update one line description above what the component does.
 */
@UriEndpoint(firstVersion = "0.0.1-SNAPSHOT", scheme = "pulsar", title = "Pulsar", syntax="pulsar:hostport",
             category = {Category.JAVA})
public class PulsarEndpoint extends DefaultEndpoint {

    /**
     * Pulsar url
     */
    @UriPath
    @Metadata(required = true)
    private String hostport;

    private String primaryhostport; //Common options both producer and consumer

    private List<String> secondaryhostport; //Common options both producer and consumer

    @UriParam
    private Boolean receiveInBatches; //Consumer option

    @UriParam
    private String subscription; //Consumer option

    @UriParam
    private SubscriptionType subscriptionType; //Consumer option

    @UriParam
    private Integer receiverQueueSize; //Consumer option

    @UriParam
    private Long acknowledgementsGroupTime; //Consumer option

    @UriParam
    private Long negativeAckRedeliveryDelay;//Consumer option

    @UriParam
    private Integer maxTotalReceiverQueueSizeAcrossPartitions;//Consumer option

    @UriParam
    private Long ackTimeoutMillis;//Consumer option

    @UriParam
    private Long tickDurationMillis;//Consumer option

    @UriParam
    private Integer priorityLevel;//Consumer option

    @UriParam
    private ConsumerCryptoFailureAction cryptoFailureAction;//Consumer option

    @UriParam
    private Boolean readCompacted;//Consumer option

    @UriParam
    private SubscriptionInitialPosition subscriptionInitialPosition;//Consumer option

    @UriParam
    private Integer patternAutoDiscoveryPeriod;//Consumer option

    @UriParam
    private SubscriptionMode subscriptionMode;//Consumer option

    @UriParam
    private DeadLetterPolicy deadLetterPolicy;//Consumer option

    @UriParam
    private Boolean autoUpdatePartitions;//Consumer option

    @UriParam
    private Boolean replicateSubscriptionState;//Consumer option

    @UriParam
    private RedeliveryBackoff negativeAckRedeliveryBackoff;//Consumer option

    @UriParam
    private RedeliveryBackoff ackTimeoutRedeliveryBackoff;//Consumer option

    @UriParam
    private Boolean autoAckOldestChunkedMessageOnQueueFull;//Consumer option

    @UriParam
    private Integer maxPendingChunkedMessage;//Consumer option;

    @UriParam
    private Long expireTimeOfIncompleteChunkedMessageMillis;//Consumer option

    @UriParam
    private String topic; //Common options both producer and consumer

    @UriParam
    private Long failOverDelay; //Common options both producer and consumer

    @UriParam
    private Long switchBackDelay; //Common options both producer and consumer

    @UriParam
    private Boolean enableChunking; //Producer Option

    @UriParam
    private Long batchingMaxPublishDelay;//Producer Option

    @UriParam
    private Integer sendTimeout;//Producer Option

    @UriParam
    private Boolean blockIfQueueFull;//Producer Option

    @UriParam
    private Boolean enableBatching;//Producer Option

    @UriParam
    private Integer maxPendingMessages;//Producer Option

    @UriParam
    private Integer maxPendingMessagesAcrossPartitions;//Producer Option

    @UriParam
    private MessageRoutingMode messageRoutingMode;//Producer Option

    @UriParam
    private HashingScheme hashingScheme;//Producer Option

    @UriParam
    private ProducerCryptoFailureAction producerCryptoFailureAction;//Producer Option

    @UriParam
    private Integer batchingMaxMessages;//Producer Option

    @UriParam
    private CompressionType compressionType;//Producer Option


    public PulsarEndpoint() {
    }

    public PulsarEndpoint(String uri, PulsarComponent component) {
        super(uri, component);
    }

    public Producer createProducer() throws Exception {
        return new PulsarProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        Consumer consumer = new PulsarConsumer(this, processor);
        configureConsumer(consumer);
        return consumer;
    }

    public String getPrimaryhostport() {
        return primaryhostport;
    }

    /**
     * primaryhostport
     * @param primaryhostport
     */
    public void setPrimaryhostport(String primaryhostport) {
        this.primaryhostport = primaryhostport;
    }


    public List<String> getSecondaryhostport() {
        return secondaryhostport;
    }

    /**
     * secondaryhostport
     * @param secondaryhostport
     */
    public void setSecondaryhostport(List<String> secondaryhostport) {
        this.secondaryhostport = secondaryhostport;
    }

    public String getSubscription() {
        return subscription;
    }

    /**
     * Subscription
     * @param subscription
     */
    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }

    public String getTopic() {
        return topic;
    }

    /**
     * topic
     * @param topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Long getFailOverDelay() {
        return failOverDelay;
    }

    /**
     * failOverDelay
     * @param failOverDelay
     */
    public void setFailOverDelay(Long failOverDelay) {
        this.failOverDelay = failOverDelay;
    }

    public Long getSwitchBackDelay() {
        return switchBackDelay;
    }

    /**
     * switchBackDelay
     * @param switchBackDelay
     */
    public void setSwitchBackDelay(Long switchBackDelay) {
        this.switchBackDelay = switchBackDelay;
    }

    public Boolean getEnableChunking() {
        return enableChunking;
    }

    /**
     * enableChunking
     * @param enableChunking
     */
    public void setEnableChunking(Boolean enableChunking) {
        this.enableChunking = enableChunking;
    }

    public SubscriptionType getSubscriptionType() {
        return subscriptionType;
    }

    /**
     * subscriptionType
     * @param subscriptionType
     */
    public void setSubscriptionType(SubscriptionType subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Integer getReceiverQueueSize() {
        return receiverQueueSize;
    }

    /**
     * receiverQueueSize
     * @param receiverQueueSize
     */
    public void setReceiverQueueSize(Integer receiverQueueSize) {
        this.receiverQueueSize = receiverQueueSize;
    }

    public Long getAcknowledgementsGroupTime() {
        return acknowledgementsGroupTime;
    }

    /**
     * acknowledgementsGroupTime
     * @param acknowledgementsGroupTime
     */
    public void setAcknowledgementsGroupTime(Long acknowledgementsGroupTime) {
        this.acknowledgementsGroupTime = acknowledgementsGroupTime;
    }

    public Long getNegativeAckRedeliveryDelay() {
        return negativeAckRedeliveryDelay;
    }

    /**
     * negativeAckRedeliveryDelay
     * @param negativeAckRedeliveryDelay
     */
    public void setNegativeAckRedeliveryDelay(Long negativeAckRedeliveryDelay) {
        this.negativeAckRedeliveryDelay = negativeAckRedeliveryDelay;
    }

    public Integer getMaxTotalReceiverQueueSizeAcrossPartitions() {
        return maxTotalReceiverQueueSizeAcrossPartitions;
    }

    /**
     * maxTotalReceiverQueueSizeAcrossPartitions
     * @param maxTotalReceiverQueueSizeAcrossPartitions
     */
    public void setMaxTotalReceiverQueueSizeAcrossPartitions(Integer maxTotalReceiverQueueSizeAcrossPartitions) {
        this.maxTotalReceiverQueueSizeAcrossPartitions = maxTotalReceiverQueueSizeAcrossPartitions;
    }

    public Long getAckTimeoutMillis() {
        return ackTimeoutMillis;
    }

    /**
     * ackTimeoutMillis
     * @param ackTimeoutMillis
     */
    public void setAckTimeoutMillis(Long ackTimeoutMillis) {
        this.ackTimeoutMillis = ackTimeoutMillis;
    }

    public Long getTickDurationMillis() {
        return tickDurationMillis;
    }

    /**
     * tickDurationMillis
     * @param tickDurationMillis
     */
    public void setTickDurationMillis(Long tickDurationMillis) {
        this.tickDurationMillis = tickDurationMillis;
    }

    public Integer getPriorityLevel() {
        return priorityLevel;
    }

    /**
     * priorityLevel
     * @param priorityLevel
     */
    public void setPriorityLevel(Integer priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public ConsumerCryptoFailureAction getCryptoFailureAction() {
        return cryptoFailureAction;
    }

    /**
     * cryptoFailureAction
     * @param cryptoFailureAction
     */
    public void setCryptoFailureAction(ConsumerCryptoFailureAction cryptoFailureAction) {
        this.cryptoFailureAction = cryptoFailureAction;
    }

    public Boolean getReadCompacted() {
        return readCompacted;
    }

    /**
     * readCompacted
     * @param readCompacted
     */
    public void setReadCompacted(Boolean readCompacted) {
        this.readCompacted = readCompacted;
    }

    public SubscriptionInitialPosition getSubscriptionInitialPosition() {
        return subscriptionInitialPosition;
    }

    /**
     * subscriptionInitialPosition
     * @param subscriptionInitialPosition
     */
    public void setSubscriptionInitialPosition(SubscriptionInitialPosition subscriptionInitialPosition) {
        this.subscriptionInitialPosition = subscriptionInitialPosition;
    }

    public Integer getPatternAutoDiscoveryPeriod() {
        return patternAutoDiscoveryPeriod;
    }

    /**
     * patternAutoDiscoveryPeriod
     * @param patternAutoDiscoveryPeriod
     */
    public void setPatternAutoDiscoveryPeriod(Integer patternAutoDiscoveryPeriod) {
        this.patternAutoDiscoveryPeriod = patternAutoDiscoveryPeriod;
    }

    public SubscriptionMode getSubscriptionMode() {
        return subscriptionMode;
    }

    /**
     * subscriptionMode
     * @param subscriptionMode
     */
    public void setSubscriptionMode(SubscriptionMode subscriptionMode) {
        this.subscriptionMode = subscriptionMode;
    }

    public DeadLetterPolicy getDeadLetterPolicy() {
        return deadLetterPolicy;
    }

    /**
     * deadLetterPolicy
     * @param deadLetterPolicy
     */
    public void setDeadLetterPolicy(DeadLetterPolicy deadLetterPolicy) {
        this.deadLetterPolicy = deadLetterPolicy;
    }

    public Boolean getAutoUpdatePartitions() {
        return autoUpdatePartitions;
    }

    /**
     * autoUpdatePartitions
     * @param autoUpdatePartitions
     */
    public void setAutoUpdatePartitions(Boolean autoUpdatePartitions) {
        this.autoUpdatePartitions = autoUpdatePartitions;
    }

    public Boolean getReplicateSubscriptionState() {
        return replicateSubscriptionState;
    }

    /**
     * replicateSubscriptionState
     * @param replicateSubscriptionState
     */
    public void setReplicateSubscriptionState(Boolean replicateSubscriptionState) {
        this.replicateSubscriptionState = replicateSubscriptionState;
    }

    public RedeliveryBackoff getNegativeAckRedeliveryBackoff() {
        return negativeAckRedeliveryBackoff;
    }

    /**
     * negativeAckRedeliveryBackoff
     * @param negativeAckRedeliveryBackoff
     */
    public void setNegativeAckRedeliveryBackoff(RedeliveryBackoff negativeAckRedeliveryBackoff) {
        this.negativeAckRedeliveryBackoff = negativeAckRedeliveryBackoff;
    }

    public RedeliveryBackoff getAckTimeoutRedeliveryBackoff() {
        return ackTimeoutRedeliveryBackoff;
    }

    /**
     * ackTimeoutRedeliveryBackoff
     * @param ackTimeoutRedeliveryBackoff
     */
    public void setAckTimeoutRedeliveryBackoff(RedeliveryBackoff ackTimeoutRedeliveryBackoff) {
        this.ackTimeoutRedeliveryBackoff = ackTimeoutRedeliveryBackoff;
    }

    public Boolean getAutoAckOldestChunkedMessageOnQueueFull() {
        return autoAckOldestChunkedMessageOnQueueFull;
    }

    /**
     * autoAckOldestChunkedMessageOnQueueFull
     * @param autoAckOldestChunkedMessageOnQueueFull
     */
    public void setAutoAckOldestChunkedMessageOnQueueFull(Boolean autoAckOldestChunkedMessageOnQueueFull) {
        this.autoAckOldestChunkedMessageOnQueueFull = autoAckOldestChunkedMessageOnQueueFull;
    }

    public Integer getMaxPendingChunkedMessage() {
        return maxPendingChunkedMessage;
    }

    /**
     * maxPendingChunkedMessage
     * @param maxPendingChunkedMessage
     */
    public void setMaxPendingChunkedMessage(Integer maxPendingChunkedMessage) {
        this.maxPendingChunkedMessage = maxPendingChunkedMessage;
    }

    public Long getExpireTimeOfIncompleteChunkedMessageMillis() {
        return expireTimeOfIncompleteChunkedMessageMillis;
    }

    /**
     * expireTimeOfIncompleteChunkedMessageMillis
     * @param expireTimeOfIncompleteChunkedMessageMillis
     */
    public void setExpireTimeOfIncompleteChunkedMessageMillis(Long expireTimeOfIncompleteChunkedMessageMillis) {
        this.expireTimeOfIncompleteChunkedMessageMillis = expireTimeOfIncompleteChunkedMessageMillis;
    }

    public Long getBatchingMaxPublishDelay() {
        return batchingMaxPublishDelay;
    }

    /**
     * batchingMaxPublishDelay
     * @param batchingMaxPublishDelay
     */
    public void setBatchingMaxPublishDelay(Long batchingMaxPublishDelay) {
        this.batchingMaxPublishDelay = batchingMaxPublishDelay;
    }

    public Integer getSendTimeout() {
        return sendTimeout;
    }

    /**
     * sendTimeout
     * @param sendTimeout
     */
    public void setSendTimeout(Integer sendTimeout) {
        this.sendTimeout = sendTimeout;
    }

    public Boolean getBlockIfQueueFull() {
        return blockIfQueueFull;
    }

    /**
     * blockIfQueueFull
     * @param blockIfQueueFull
     */
    public void setBlockIfQueueFull(Boolean blockIfQueueFull) {
        this.blockIfQueueFull = blockIfQueueFull;
    }

    public Boolean getEnableBatching() {
        return enableBatching;
    }

    /**
     * enableBatching
     * @param enableBatching
     */
    public void setEnableBatching(Boolean enableBatching) {
        this.enableBatching = enableBatching;
    }

    public Integer getMaxPendingMessages() {
        return maxPendingMessages;
    }

    /**
     * maxPendingMessages
     * @param maxPendingMessages
     */
    public void setMaxPendingMessages(Integer maxPendingMessages) {
        this.maxPendingMessages = maxPendingMessages;
    }

    public Integer getMaxPendingMessagesAcrossPartitions() {
        return maxPendingMessagesAcrossPartitions;
    }

    /**
     * maxPendingMessagesAcrossPartitions
     * @param maxPendingMessagesAcrossPartitions
     */
    public void setMaxPendingMessagesAcrossPartitions(Integer maxPendingMessagesAcrossPartitions) {
        this.maxPendingMessagesAcrossPartitions = maxPendingMessagesAcrossPartitions;
    }

    public MessageRoutingMode getMessageRoutingMode() {
        return messageRoutingMode;
    }

    /**
     * messageRoutingMode
     * @param messageRoutingMode
     */
    public void setMessageRoutingMode(MessageRoutingMode messageRoutingMode) {
        this.messageRoutingMode = messageRoutingMode;
    }

    public HashingScheme getHashingScheme() {
        return hashingScheme;
    }

    /**
     * hashingScheme
     * @param hashingScheme
     */
    public void setHashingScheme(HashingScheme hashingScheme) {
        this.hashingScheme = hashingScheme;
    }

    public ProducerCryptoFailureAction getProducerCryptoFailureAction() {
        return producerCryptoFailureAction;
    }

    /**
     * producerCryptoFailureAction
     * @param producerCryptoFailureAction
     */
    public void setProducerCryptoFailureAction(ProducerCryptoFailureAction producerCryptoFailureAction) {
        this.producerCryptoFailureAction = producerCryptoFailureAction;
    }

    public Integer getBatchingMaxMessages() {
        return batchingMaxMessages;
    }

    /**
     * batchingMaxMessages
     * @param batchingMaxMessages
     */
    public void setBatchingMaxMessages(Integer batchingMaxMessages) {
        this.batchingMaxMessages = batchingMaxMessages;
    }

    public CompressionType getCompressionType() {
        return compressionType;
    }

    /**
     * compressionType
     * @param compressionType
     */
    public void setCompressionType(CompressionType compressionType) {
        this.compressionType = compressionType;
    }

    public Boolean getReceiveInBatches() {
        return receiveInBatches;
    }

    /**
     * receiveInBatches
     * @param receiveInBatches
     */
    public void setReceiveInBatches(Boolean receiveInBatches) {
        this.receiveInBatches = receiveInBatches;
    }

    public ExecutorService createExecutor() {
        // TODO: Delete me when you implemented your custom component
        return getCamelContext().getExecutorServiceManager().newSingleThreadExecutor(this, "PulsarConsumer");
    }
}
