package org.bitvault.appstore.cloud.config;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.node.NodeBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.stereotype.Component;

//@Configuration
//@Component
//public class ElasticSearchConfiguration {

//	@Bean
//	public NodeBuilder nodeBuilder() {
//		return new NodeBuilder();
//	}
//
//	@Bean
//	public ElasticsearchOperations elasticsearchTemplate() throws IOException {
//
//		Settings.Builder elasticsearchSettings = Settings.settingsBuilder().put("http.enabled", "true") // 1
//				.put("index.number_of_shards", "1");
//
//		return new ElasticsearchTemplate(
//				nodeBuilder().local(true).settings(elasticsearchSettings.build()).node().client());
//	}

	//@Value("${elasticsearch.host}")
//    private String EsHost;
//
//    //@Value("${elasticsearch.port}")
//    private int EsPort;

//    @Value("${elasticsearch.clustername}")
//    private String EsClusterName;

//    @Bean
//    public Client client() throws Exception {
//
//        Settings esSettings = Settings.settingsBuilder()
//               //.put("cluster.name", EsClusterName)
//                .put("http.enabled", "true")
//                .put("client.transport.ping_timeout", "20s")
//                .put("client.transport.nodes_sampler_interval", "20s")
//                .put("client.transport.sniff", false)
//                .build();
//
//        //https://www.elastic.co/guide/en/elasticsearch/guide/current/_transport_client_versus_node_client.html
//        return TransportClient.builder()
//                .settings(esSettings)
//                .build()
//                .addTransportAddress(
//				  new InetSocketTransportAddress(InetAddress.getByName("192.168.11.144"), 9300));
//    }
//}
//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
//        return new ElasticsearchTemplate(client());
//    }
//
//	
//	
////	
//	 @Value("${spring.data.elasticsearch.cluster-name}")
//	    private String clusterName;
//	    @Value("${spring.data.elasticsearch.cluster-nodes}")
//	    private String clusterNodes;
//	    @Bean
//	    public ElasticsearchTemplate elasticsearchTemplate() throws UnknownHostException {
//	            String server = clusterNodes.split(":")[0];
//	            Integer port = Integer.parseInt(clusterNodes.split(":")[1]);
//	            InetSocketTransportAddress inetSocketTransportAddress = new InetSocketTransportAddress(InetAddress.getByName(server), port);
//	            Settings settings = Settings.settingsBuilder()
//	            		 .put("client.transport.sniff", false)
//	                .put("cluster.name", clusterName).build();
//   TransportClient  client = TransportClient.builder().settings(settings).build()
//	                .addTransportAddress(inetSocketTransportAddress);
//	            return new ElasticsearchTemplate(client);
//	            
//	    }
	   // }
