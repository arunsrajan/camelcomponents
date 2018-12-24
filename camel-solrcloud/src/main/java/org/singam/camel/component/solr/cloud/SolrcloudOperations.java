package org.singam.camel.component.solr.cloud;

public class SolrcloudOperations {
	public static final String SOLROPERATION = "SolrOperation";
	
	//Producer Operations
	public static final String SOLRCREATECOLLECTION = "SolrCreateCollection";
	public static final String SOLRCREATESHARDS = "SolrCreateShards";
	public static final String SOLRADDREPLICAS = "SolrAddReplicas";
	public static final String SPLITSHARD = "SplitShard";
	public static final String CREATEALIAS = "CreateAlias";
	public static final String DELETEALIAS = "DeleteAlias";
	public static final String DELETESHARD = "DeleteShard";
	public static final String DELETEREPLICA = "DeleteReplica";
	//Constants
	public static final String NUMSHARDS = "NumberOfShards";
	public static final String NUMREPLICAS = "NumberOfReplicas";
	public static final String SHARDNAME = "ShardName";
	public static final String REPLICANAME = "ReplicaName";
	public static final String MAXSHARDSPERNODE = "MaxShardsPerNode";
	public static final String IMPLICIT = "implicit";
	public static final String ALIAS = "Alias";
	
	//Consumer Operations
	public static final String SOLRLISTCOLLECTIONS = "SolrListCollections";
	
}
