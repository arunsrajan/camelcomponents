package org.singam.camel.component.webcrawler;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link WebcrawlerEndpoint}.
 */
public class WebcrawlerComponent extends DefaultComponent {
private int numCrawlers = 3;
    
    private int batchSize = 10;
    
    private String seed;
    
    private String filterPattern;
    
    private String storageFolder;
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new WebcrawlerEndpoint(uri, this);
        Map<String,Object> webCrawlerProperties = new HashMap<>();
        webCrawlerProperties.put("numCrawlers", numCrawlers);
        webCrawlerProperties.put("batchSize", batchSize);
        webCrawlerProperties.put("seed", seed);
        webCrawlerProperties.put("filterPattern", filterPattern);
        webCrawlerProperties.put("storageFolder", storageFolder);
        setProperties(endpoint, webCrawlerProperties);
        setProperties(endpoint, parameters);
        return endpoint;
    }
	public int getNumCrawlers() {
		return numCrawlers;
	}
	public void setNumCrawlers(int numCrawlers) {
		this.numCrawlers = numCrawlers;
	}
	public int getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
	public String getSeed() {
		return seed;
	}
	public void setSeed(String seed) {
		this.seed = seed;
	}
	public String getFilterPattern() {
		return filterPattern;
	}
	public void setFilterPattern(String filterPattern) {
		this.filterPattern = filterPattern;
	}
	public String getStorageFolder() {
		return storageFolder;
	}
	public void setStorageFolder(String storageFolder) {
		this.storageFolder = storageFolder;
	}
    
    
    
}
