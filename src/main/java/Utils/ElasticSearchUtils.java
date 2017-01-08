package Utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElasticSearchUtils {
	
	private static String clusterName="shilei-application";
	private static String ip="202.117.54.85";
	private static int port=9300;
	private Client client=null;
	
	public Client getClient() {
		return client;
	}


	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@SuppressWarnings("resource")
	public ElasticSearchUtils() {
		super();
		Settings settings = Settings.builder()
		        .put("cluster.name", clusterName).build();
		try {
			this.client = new PreBuiltTransportClient(settings)
			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ip), port));
		} catch (UnknownHostException e) {
			logger.info("创建client失败");
			this.client.close();
		}
	}
	
	
	public void close() {
		client.close();
	}
}
