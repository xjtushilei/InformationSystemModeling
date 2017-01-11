package Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import APP.config;

public class ElasticSearchUtils {
	

	
	@SuppressWarnings("resource")
	public TransportClient  getClient() {
		TransportClient  client=null;
		Settings settings = Settings.builder()
		        .put("cluster.name", config.ES_clusterName)
		        .put("client.transport.sniff", true)
		        .build();
		try {
			client = new PreBuiltTransportClient(settings)
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(config.ES_ip), config.ES_port));
		} catch (UnknownHostException e) {
		}
		return client;
	}


}
