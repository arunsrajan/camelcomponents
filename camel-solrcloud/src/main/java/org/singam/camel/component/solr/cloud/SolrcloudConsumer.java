package org.singam.camel.component.solr.cloud;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.utils.ZKPaths;

/**
 * The Solrcloud consumer.
 */
public class SolrcloudConsumer extends DefaultConsumer {
	private final SolrcloudEndpoint endpoint;
	private CuratorFramework cf;
	private String solrOperation;
	PathChildrenCache pathChildrenCache = null;

	public SolrcloudConsumer(SolrcloudEndpoint endpoint, Processor processor, String host, String port,
			String solrContext, String solrOperation) throws Exception {
		super(endpoint, processor);
		this.endpoint = endpoint;
		this.solrOperation = solrOperation;
		cf = CuratorFrameworkFactory.newClient(host + ":" + port, new RetryForever(3000));
		cf.start();
		cf.blockUntilConnected();
	}

	@Override
	protected void doStart() throws Exception {
		super.doStart();

		try {
			if (!(solrOperation.equals(SolrcloudOperations.SHARDCREATE)
					|| solrOperation.equals(SolrcloudOperations.SHARDDELETE)
					|| solrOperation.equals(SolrcloudOperations.COLLECTIONCREATE)
					|| solrOperation.equals(SolrcloudOperations.COLLECTIONREMOVE)
					|| solrOperation.equals(SolrcloudOperations.REPLICACREATE)
					|| solrOperation.equals(SolrcloudOperations.REPLICADELETE)
					|| solrOperation.equals(SolrcloudOperations.ALLCREATEREMOVE))) {
				throw new Exception("Solr Consumer Cloud Operation " + solrOperation + " Not Supported");
			} else if (solrOperation.equals(SolrcloudOperations.COLLECTIONCREATE)||
					solrOperation.equals(SolrcloudOperations.COLLECTIONREMOVE)) {
				pathChildrenCache = new PathChildrenCache(cf,
						SolrcloudOperations.ZNODESEPERATOR + SolrcloudOperations.COLLECTIONS, false);

				pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {

					@Override
					public void childEvent(CuratorFramework cf, PathChildrenCacheEvent evt) throws Exception {
						try {
							switch (evt.getType()) {
								case CHILD_ADDED: {
									if(solrOperation.equals(SolrcloudOperations.COLLECTIONCREATE)&& evt.getData()!=null) {
										ProcessNode(evt, " Created", " Collection ");
									}
									break;
								}
								case CHILD_REMOVED: {
									if(solrOperation.equals(SolrcloudOperations.COLLECTIONREMOVE) && evt.getData()!=null) {
										ProcessNode(evt, " Removed", " Collection ");
									}
									break;
								}
								default:
									break;
								}
						} catch (Exception ex) {
						}
					}

				});
			}
			else if (solrOperation.equals(SolrcloudOperations.SHARDCREATE) ||
					solrOperation.equals(SolrcloudOperations.SHARDDELETE)) {
				pathChildrenCache = new PathChildrenCache(cf,
						SolrcloudOperations.ZNODESEPERATOR + SolrcloudOperations.COLLECTIONS +
						SolrcloudOperations.ZNODESEPERATOR + endpoint.collectionName + 
						SolrcloudOperations.ZNODESEPERATOR + SolrcloudOperations.LEADERELECT, false);

				pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {

					@Override
					public void childEvent(CuratorFramework cf, PathChildrenCacheEvent evt) throws Exception {
						try {
							switch (evt.getType()) {
								case CHILD_ADDED: {
									if(solrOperation.equals(SolrcloudOperations.SHARDCREATE) && evt.getData()!=null) {
										ProcessNode(evt, " Created", "In Collection "+endpoint.collectionName+" Shard ");
									}
									break;
								}
								case CHILD_REMOVED: {
									if(solrOperation.equals(SolrcloudOperations.SHARDDELETE) && evt.getData()!=null) {
										ProcessNode(evt, " Removed", "In Collection "+endpoint.collectionName+" Shard ");
									}
									break;
								}
								default:
									break;
								}
						} catch (Exception ex) {
						}
					}

				});
			}
			else if (solrOperation.equals(SolrcloudOperations.REPLICACREATE)||
					solrOperation.equals(SolrcloudOperations.REPLICADELETE)) {
				pathChildrenCache = new PathChildrenCache(cf,
						SolrcloudOperations.ZNODESEPERATOR + SolrcloudOperations.COLLECTIONS +
						SolrcloudOperations.ZNODESEPERATOR + endpoint.collectionName + 
						SolrcloudOperations.ZNODESEPERATOR + SolrcloudOperations.LEADERELECT + 
						SolrcloudOperations.ZNODESEPERATOR + endpoint.shardName + 
						SolrcloudOperations.ZNODESEPERATOR + SolrcloudOperations.ELECTION, false);

				pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {

					@Override
					public void childEvent(CuratorFramework cf, PathChildrenCacheEvent evt) throws Exception {
						try {
							switch (evt.getType()) {
								case CHILD_ADDED: {
									if(solrOperation.equals(SolrcloudOperations.REPLICACREATE) && evt.getData()!=null) {
										ProcessNode(evt, " Created", "In Collection "+endpoint.collectionName+" In Shard "+endpoint.shardName+" Replica ");
									}
									break;
								}
								case CHILD_REMOVED: {
									if(solrOperation.equals(SolrcloudOperations.REPLICADELETE) && evt.getData()!=null) {
										ProcessNode(evt, " Removed", "In Collection "+endpoint.collectionName+" In Shard "+endpoint.shardName+" Replica ");
									}
									break;
								}
								default:
									break;
								}
						} catch (Exception ex) {
						}
					}

				});
			}
			pathChildrenCache.start();
		} finally {

		}
	}

	private void ProcessNode(PathChildrenCacheEvent evt, String message, String entity) throws Exception {
		Exchange exchange = endpoint.createExchange();
		exchange.getIn().setBody(entity + ZKPaths.getNodeFromPath(evt.getData().getPath())+ message);
		getProcessor().process(exchange);
	}
	
	@Override
	protected void doStop() throws Exception {
		super.doStop();
		pathChildrenCache.close();
	}
}
