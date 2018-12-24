package org.singam.camel.component.solr.cloud;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;

/**
 * The Solrcloud consumer.
 */
public class SolrcloudConsumer extends ScheduledPollConsumer {
    private final SolrcloudEndpoint endpoint;
	private CloudSolrClient cloudSolrClient;
	private String solrOperation;
	
	
    public SolrcloudConsumer(SolrcloudEndpoint endpoint, Processor processor, String host, String port, String solrContext, String solrOperation) {
        super(endpoint, processor);
        this.endpoint = endpoint;
        this.solrOperation = solrOperation; 
        String solrHostUrl = "http://"+host+":"+port+"/"+solrContext;
        cloudSolrClient = new CloudSolrClient.Builder(Arrays.asList(solrHostUrl)).build();
        setDelay(4000);
    }

    @Override
    protected int poll() throws Exception {
    	Exchange exchange = null;

        try {
            if(!(solrOperation.equals(SolrcloudOperations.SOLRLISTCOLLECTIONS))){
            	throw new Exception("Solr Consumer Cloud Operation "+solrOperation+" Not Supported");
            }
            else if(solrOperation.equals(SolrcloudOperations.SOLRLISTCOLLECTIONS)) {
            	List<String> collections = CollectionAdminRequest.listCollections(cloudSolrClient);
            	exchange = endpoint.createExchange();
            	exchange.getIn().setBody(collections);
            	getProcessor().process(exchange);
            }
            return 1; 
        } finally {
            if (exchange != null &&exchange.getException() != null) {
                getExceptionHandler().handleException("Error processing exchange", exchange, exchange.getException());
            }
        }
    }
}
