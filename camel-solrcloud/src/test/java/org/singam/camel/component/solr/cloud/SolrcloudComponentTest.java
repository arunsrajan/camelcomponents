package org.singam.camel.component.solr.cloud;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class SolrcloudComponentTest extends CamelTestSupport {

    @Test
    public void testSolrcloud() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(100);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("timer:solrcloud?delay=5000")
                .setHeader("SolrOperation", constant("SolrCreateCollection"))
                .setHeader("NumberOfShards",constant("2"))
                .setHeader("NumberOfReplicas",constant("2"))
                .setHeader("MaxShardsPerNode",constant("4"))
                .setBody(constant("collection3"))
                  .to("solrcloud://test?host=localhost&port=8983&context=solr")
                  .convertBodyTo(String.class)
                  .to("mock:result");
            	from("timer:solrcloud?delay=5000")
                .setHeader("SolrOperation", constant("SolrAddReplicas"))
                .setHeader("ShardName",constant("shard1"))
                .setBody(constant("collection3"))
                .to("solrcloud://test?host=localhost&port=8983&context=solr")
                .convertBodyTo(String.class)
                .to("mock:result");
            	
            	from("timer:solrcloud?delay=5000")
                .setHeader("SolrOperation", constant("SolrCreateShards"))
                .setHeader("ShardName",constant("shard2"))
                .setBody(constant("collection5"))
                .to("solrcloud://test?host=localhost&port=8983&context=solr")
                .convertBodyTo(String.class)
                .to("mock:result");
            	from("timer:solrcloud?delay=5000")
                .setHeader("SolrOperation", constant("SolrCreateCollection"))
                .setHeader("NumberOfShards",constant("2"))
                .setHeader("NumberOfReplicas",constant("2"))
                .setHeader("MaxShardsPerNode",constant("4"))
                .setBody(constant("collection7"))
                  .to("solrcloud://test?host=localhost&port=8983&context=solr")
                  .to("mock:result");
            	from("timer:solrcloud?delay=5000")
                .setHeader("SolrOperation", constant("SolrAddReplicas"))
                .setHeader("ShardName",constant("shard3"))
                .setBody(constant("collection7"))
                .to("solrcloud://test?host=localhost&port=8983&context=solr")
                .convertBodyTo(String.class)
                .to("mock:result");
            	from("timer:solrcloud?delay=5000")
                .setHeader("SolrOperation", constant("SplitShard"))
                .setHeader("ShardName", constant("shard1"))
                .setBody(constant("collection"))
                .to("solrcloud://test?host=localhost&port=8983&context=solr")
                .convertBodyTo(String.class)
                .to("mock:result");
            	from("timer:solrcloud?delay=5000")
                .setHeader("SolrOperation", constant("CreateAlias"))
                .setHeader("Alias", constant("collections"))
                .setBody(constant("collection"))
                .to("solrcloud://test?host=localhost&port=8983&context=solr")
                .convertBodyTo(String.class)
                .to("mock:result");
            	from("timer:solrcloud?delay=3000")
                .setHeader("SolrOperation", constant("DeleteShard"))
                .setHeader("ShardName", constant("shard2"))
                .setBody(constant("collection7"))
                .to("solrcloud://test?host=localhost&port=8983&context=solr")
                .convertBodyTo(String.class)
                .to("mock:result");
            	from("timer:solrcloud?delay=3000")
                .setHeader("SolrOperation", constant("DeleteReplica"))
                .setHeader("ShardName", constant("shard2"))
                .setHeader("ReplicaName", constant("core_node7"))
                .setBody(constant("collection6"))
                .to("solrcloud://test?host=localhost&port=8983&context=solr")
                .convertBodyTo(String.class)
                .to("mock:result");
            	from("solrcloud://test1?host=localhost&port=9983&context=solr&solrOperation=SolrReplicaDelete&collectionName=collection&shardName=shard1_0")
            	.log("${body}").to("mock:result");
            	from("solrcloud://test2?host=localhost&port=9983&context=solr&solrOperation=SolrShardDelete&collectionName=collection5")
            	.log("${body}").to("mock:result");
            	from("solrcloud://test3?host=localhost&port=9983&context=solr&solrOperation=SolrCollectionCreate")
            	.log("${body}").to("mock:result");
            	from("solrcloud://test4?host=localhost&port=9983&context=solr&solrOperation=SolrCollectionRemove")
            	.log("${body}").to("mock:result");
            	from("solrcloud://test5?host=localhost&port=9983&context=solr&solrOperation=SolrCollectionRemove")
            	.log("${body}").to("mock:result");
            }
        };
    }
}
