package org.singam.camel.component.solr.cloud;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

/**
 * Represents a Solrcloud endpoint.
 */
@UriEndpoint(firstVersion = "1.0.0", scheme = "solrcloud", title = "Solrcloud", syntax="solrcloud:name", 
             consumerClass = SolrcloudConsumer.class, label = "custom")
public class SolrcloudEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;
    @UriParam
    private String host = "127.0.0.1";
    
    @UriParam
    private String port = "8980";

    @UriParam
    private String context = "solr";
    
    @UriParam
    private String solrOperation = SolrcloudOperations.COLLECTIONCREATE;
    
    @UriParam
    protected String collectionName = null;
    
    @UriParam
    protected String shardName = null;
    
    public SolrcloudEndpoint() {
    }

    public SolrcloudEndpoint(String uri, SolrcloudComponent component) {
        super(uri, component);
    }

    public SolrcloudEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new SolrcloudProducer(this,host,port,context);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new SolrcloudConsumer(this, processor, host, port, context, solrOperation);
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

	public String getHost() {
		return host;
	}

	/**
	 * Host Name Of Solr Server
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	/**
	 * Port of Solr Server
	 * @param port
	 */
	public void setPort(String port) {
		this.port = port;
	}

	public String getContext() {
		return context;
	}

	/**
	 * Context of solr server
	 * @param context
	 */
	public void setContext(String context) {
		this.context = context;
	}

	public String getSolrOperation() {
		return solrOperation;
	}

	/**
	 * Supportive Consumer SolrOperation 
	 * @param solrOperation
	 */
	public void setSolrOperation(String solrOperation) {
		this.solrOperation = solrOperation;
	}

	public String getCollectionName() {
		return collectionName;
	}

	/**
	 * Solr Collection name for consumers
	 * @param collectionName
	 */
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getShardName() {
		return shardName;
	}

	/**
	 * Solr Shard Name for consumers
	 * @param shardName
	 */
	public void setShardName(String shardName) {
		this.shardName = shardName;
	}
    
    
    
}
