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
	public static final String COLLECTIONCREATE = "SolrCollectionCreate";
	public static final String COLLECTIONREMOVE = "SolrCollectionRemove";
	public static final String SHARDCREATE = "SolrShardCreate";
	public static final String SHARDDELETE = "SolrShardDelete";
	public static final String REPLICACREATE = "SolrReplicaCreate";
	public static final String REPLICADELETE = "SolrReplicaDelete";
	public static final String ALLCREATEREMOVE = "SolrCreateRemove";
	
	
	public static final String COLLECTIONS = "collections";
	public static final String ZNODESEPERATOR = "/";
	public static final String LEADERELECT = "leader_elect";
	public static final String ELECTION = "election";
}
