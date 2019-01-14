package org.singam.camel.component.webcrawler;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * Represents a Webcrawler endpoint.
 */
@UriEndpoint(firstVersion = "1.0.0", scheme = "webcrawler", title = "Webcrawler", syntax="webcrawler:name", 
             consumerClass = WebcrawlerConsumer.class, label = "custom")
public class WebcrawlerEndpoint extends DefaultEndpoint {
    @UriPath @Metadata(required = "true")
    private String name;
    @UriParam(defaultValue = "10")
    private int option = 10;

    @UriParam
    private int numCrawlers = 3;
    
    
    @UriParam
    private int batchSize = 10;
    
    @UriParam
    private String seed;
    
    @UriParam
    private String filterPattern;
    
    @UriParam
    private String storageFolder;
    
    
    public WebcrawlerEndpoint() {
    }

    public WebcrawlerEndpoint(String uri, WebcrawlerComponent component) {
        super(uri, component);
    }

    public WebcrawlerEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        throw new UnsupportedOperationException("Camel-webcrawler doesn't support producer endpoint");
    }

    public Consumer createConsumer(Processor processor) throws Exception {

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(storageFolder);
        config.setIncludeBinaryContentInCrawling(true);
        config.setIncludeHttpsPages(true);
        config.setProcessBinaryContentInCrawling(true);
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

       
        controller.addSeed(seed);

        
        return new WebcrawlerConsumer(this, processor, controller, numCrawlers, filterPattern, batchSize);
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

	public int getNumCrawlers() {
		return numCrawlers;
	}

	/**
	 * Number of crawler Threads
	 * @param numCrawlers
	 */
	public void setNumCrawlers(int numCrawlers) {
		this.numCrawlers = numCrawlers;
	}

	public String getSeed() {
		return seed;
	}

	/**
	 * Seed Url to Crawl
	 * @param seed
	 */
	public void setSeed(String seed) {
		this.seed = seed;
	}

	public String getFilterPattern() {
		return filterPattern;
	}

	/**
	 * Filter Pattern
	 * @param filterPattern
	 */
	public void setFilterPattern(String filterPattern) {
		this.filterPattern = filterPattern;
	}

	public String getStorageFolder() {
		return storageFolder;
	}

	/**
	 * Storage folder to re-crawl
	 * @param storageFolder
	 */
	public void setStorageFolder(String storageFolder) {
		this.storageFolder = storageFolder;
	}

	public int getBatchSize() {
		return batchSize;
	}

	/**
	 * Batch Size of Html to Poll
	 * @param batchSize
	 */
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}
    
    
    
}
