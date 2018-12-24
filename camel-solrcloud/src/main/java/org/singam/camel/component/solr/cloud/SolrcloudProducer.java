package org.singam.camel.component.solr.cloud;

import java.util.Arrays;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultProducer;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.request.CollectionAdminRequest;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.AddReplica;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.Create;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.CreateAlias;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.CreateShard;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.DeleteReplica;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.DeleteShard;
import org.apache.solr.client.solrj.request.CollectionAdminRequest.SplitShard;
import org.apache.solr.client.solrj.response.CollectionAdminResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Solrcloud producer.
 */
public class SolrcloudProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(SolrcloudProducer.class);
    private SolrcloudEndpoint endpoint;
    private CloudSolrClient cloudSolrClient;
    public SolrcloudProducer(SolrcloudEndpoint endpoint,String host,String port,String solrContext) {
        super(endpoint);
        this.endpoint = endpoint;
        String solrHostUrl = "http://"+host+":"+port+"/"+solrContext;
        cloudSolrClient = new CloudSolrClient.Builder(Arrays.asList(solrHostUrl)).build();
    }

    public void process(Exchange exchange) throws Exception {
        Message message = exchange.getIn();
        CollectionAdminResponse adminResponse = null;
        String solrOperation = (String) message.getHeader(SolrcloudOperations.SOLROPERATION);
        if(!(solrOperation.equals(SolrcloudOperations.SOLRCREATECOLLECTION) ||
        		solrOperation.equals(SolrcloudOperations.SOLRCREATESHARDS) ||
        		solrOperation.equals(SolrcloudOperations.SOLRADDREPLICAS)||
        		solrOperation.equals(SolrcloudOperations.SPLITSHARD)||
        		solrOperation.equals(SolrcloudOperations.CREATEALIAS)||
        		solrOperation.equals(SolrcloudOperations.DELETESHARD)||
        		solrOperation.equals(SolrcloudOperations.DELETEREPLICA))){
        	throw new Exception("Solr Producer Cloud Operation "+solrOperation+" Not Supported");
        }
        else if(solrOperation.equals(SolrcloudOperations.SOLRCREATECOLLECTION)) {
        	String collection = (String) message.getBody();
        	int numShards = Integer.parseInt((String) message.getHeader(SolrcloudOperations.NUMSHARDS));
        	int numReplicas = Integer.parseInt((String) message.getHeader(SolrcloudOperations.NUMREPLICAS));
        	int maxShardsPerNode = Integer.parseInt((String) message.getHeader(SolrcloudOperations.MAXSHARDSPERNODE));
        	Create collectionCreate = CollectionAdminRequest.createCollection(collection, numShards, numReplicas);
        	collectionCreate.setMaxShardsPerNode(maxShardsPerNode);
        	adminResponse = collectionCreate.process(cloudSolrClient);
        }
        else if(solrOperation.equals(SolrcloudOperations.SOLRADDREPLICAS)) {
        	String collection = (String) message.getBody();
        	String shard = (String) message.getHeader(SolrcloudOperations.SHARDNAME);
        	AddReplica addReplica = CollectionAdminRequest.addReplicaToShard(collection, shard);
        	adminResponse = addReplica.process(cloudSolrClient);
        }
        else if(solrOperation.equals(SolrcloudOperations.SOLRCREATESHARDS)) {
        	String collection = (String) message.getBody();
        	String shard = (String) message.getHeader(SolrcloudOperations.SHARDNAME);
        	CreateShard createShard = CollectionAdminRequest.createShard(collection, shard);
        	adminResponse = createShard.process(cloudSolrClient);
        }
        else if(solrOperation.equals(SolrcloudOperations.SPLITSHARD)) {
        	String collection = (String) message.getBody();
        	SplitShard splitShard = CollectionAdminRequest.splitShard(collection);
        	String shard = (String) message.getHeader(SolrcloudOperations.SHARDNAME);
        	splitShard.setShardName(shard);
        	adminResponse = splitShard.process(cloudSolrClient);
        }
        else if(solrOperation.equals(SolrcloudOperations.DELETESHARD)) {
        	String collection = (String) message.getBody();
        	String shard = (String) message.getHeader(SolrcloudOperations.SHARDNAME);
        	DeleteShard deleteShard = CollectionAdminRequest.deleteShard(collection, shard);
        	adminResponse = deleteShard.process(cloudSolrClient);
        }
        else if(solrOperation.equals(SolrcloudOperations.DELETEREPLICA)) {
        	String collection = (String) message.getBody();
        	String shard = (String) message.getHeader(SolrcloudOperations.SHARDNAME);
        	String replica = (String) message.getHeader(SolrcloudOperations.REPLICANAME);
        	DeleteReplica deleteReplica = CollectionAdminRequest.deleteReplica(collection, shard, replica);
        	adminResponse = deleteReplica.process(cloudSolrClient);
        }
        else if(solrOperation.equals(SolrcloudOperations.CREATEALIAS)) {
        	String collection = (String) message.getBody();
        	String alias = (String) message.getHeader(SolrcloudOperations.ALIAS);
        	CreateAlias createAlias = CollectionAdminRequest.createAlias(alias, collection);
        	adminResponse = createAlias.process(cloudSolrClient);
        }
        exchange.getIn().setBody(adminResponse);
    }

}
