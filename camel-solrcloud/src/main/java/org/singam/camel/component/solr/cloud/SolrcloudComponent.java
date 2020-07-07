package org.singam.camel.component.solr.cloud;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link SolrcloudEndpoint}.
 */
public class SolrcloudComponent extends DefaultComponent {
    
	private String name;

	private int option = 10;
	
    private String host = "127.0.0.1";
    
    private String port = "8980";

    private String context = "solr";
    
    private String solrOperation;
    
    protected String collectionName = null;
    
    protected String shardName = null;
	
	
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new SolrcloudEndpoint(uri, this);
        Map<String,Object> propertyMap = new HashMap<>();
        propertyMap.put("name", name);
        propertyMap.put("host", host);
        propertyMap.put("port", port);
        propertyMap.put("context", context);
        propertyMap.put("solrOperation", solrOperation);
        propertyMap.put("collectionName", collectionName);
        propertyMap.put("shardName", shardName);
        setProperties(endpoint, propertyMap);
        setProperties(endpoint, parameters);
        return endpoint;
    }


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getOption() {
		return option;
	}


	public void setOption(int option) {
		this.option = option;
	}


	public String getHost() {
		return host;
	}


	public void setHost(String host) {
		this.host = host;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}


	public String getSolrOperation() {
		return solrOperation;
	}


	public void setSolrOperation(String solrOperation) {
		this.solrOperation = solrOperation;
	}


	public String getCollectionName() {
		return collectionName;
	}


	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}


	public String getShardName() {
		return shardName;
	}


	public void setShardName(String shardName) {
		this.shardName = shardName;
	}


	public String getContext() {
		return context;
	}


	public void setContext(String context) {
		this.context = context;
	}
    
    
    
    
}
