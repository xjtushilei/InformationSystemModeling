package elasticsearch.mapping;
/**
 * 这是一个elasticsearch的mapping文件。后面的json里有拼音的配置和混合的配置
 * @author shilei
 * @date 2017-1-16 
 */
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.Client;
import Utils.ElasticSearchUtils;

public class indexAndMapping {

	public static void main(String[] args) throws IOException {
		mapping("news", "article");
	}
	
	/**
	 * 创建index
	 * 创建mapping
	 * 
	 */
	public static void mapping(String indexname,String indextypename) throws IOException
	{
		
		Client client = new ElasticSearchUtils().getClient();
		
		String indexSettings=FileUtils.readFileToString(new File("src/main/java/elasticsearch/mapping/index.json"));
		String mapping=FileUtils.readFileToString(new File("src/main/java/elasticsearch/mapping/mapping.json"));

		CreateIndexResponse createIndexResponse =client.admin().indices().prepareCreate(indexname).setSettings(indexSettings).addMapping(indextypename, mapping).execute().actionGet();//先创建空索引库
		if (createIndexResponse.isAcknowledged()) {
			System.err.println("mapping成功！");
		}
		else{
			System.err.println("mapping失败！");
		}
		
		client.close();
		
	}
	

}
