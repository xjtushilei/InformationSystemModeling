package elasticsearch.index;
/**
 * 这是一个elasticsearch的索引程序。从我的芒果DB和女票的芒果DB里把数据索引到我的elasticsearch里区去。
 * @author shilei
 * @date 2017-1-16 
 */
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;


import APP.config;
import Utils.ElasticSearchUtils;
import Utils.MongoManager;

public class indexFromMongoDB {
	public static void main(String[] args) throws IOException {
//		indexFromMongo();
		test();
	}
	
	public static void test() {
		//创建芒果DB的驱动 		
		MongoManager manager = new MongoManager("202.117.54.81", config.MongoDB_Port, "SpiderNews");
		for (String name : manager.getMongoDatabase().listCollectionNames()) {
			indexFromMongo(name,"202.117.54.81","SpiderNews");
		}
	}
	
	public static void indexFromMongo(String CollectionName,String ip, String DataBaseName) {
		
		//创建芒果DB的驱动 		
		MongoManager manager = new MongoManager(ip, config.MongoDB_Port, DataBaseName);
		
		long totalDocs=manager.size(CollectionName); 
		System.err.println("总个数:"+totalDocs);
		//索引时候，每次索引 pageSize个，分page次数完成
		int pageSize=1500;
		int page=(int) (totalDocs%pageSize==0?totalDocs/pageSize:totalDocs/pageSize+1);
		//创建 Elasticsearch的连接
		Client client = new ElasticSearchUtils().getClient();
		HashSet<String> total=new HashSet<>();
		
		for(int i=1;i<=page;i++)
		{
			try {
//				创建 Elasticsearch的批量索引
				BulkRequestBuilder bulkRequest = client.prepareBulk();
				List<Document> docs=manager.find(CollectionName, pageSize, i);
				System.out.println(docs.size());
				for (Document doc : docs) {
//					if (doc.get("newsTitle").toString().length()>60) {
//						continue;
//					}
					Map<String, Object> sourceMap=new HashMap<String, Object>();
					sourceMap.put("newsContent", Utils.HtmlSpirit.delHTMLTag(doc.get("newsContent").toString()));
					sourceMap.put("newsSource", doc.get("newsSource"));
					sourceMap.put("newsURL", doc.get("newsURL"));
					sourceMap.put("newsTitle", doc.get("newsTitle"));
					sourceMap.put("newsScratchTime", doc.get("newsScratchTime"));
					sourceMap.put("newsType", doc.get("newsType"));
					total.add(doc.getString("_id"));
					bulkRequest.add(client.prepareIndex("news", "article",doc.getString("_id")).setSource(sourceMap));
				}
				BulkResponse bulkResponse = bulkRequest.get();
				if (bulkResponse.hasFailures()) {
					System.err.println("第"+i+"次   批量索引failed！");
				}
				else{
					System.err.println("第"+i+"次   批量索引success！");
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("第"+i+"次   批量索引failed！");
			}
			System.err.println(totalDocs+"->"+total.size());
			//格式化输出 进度
			DecimalFormat df2 = new DecimalFormat("00.00");
			System.out.println(CollectionName+"索引完成: "+ df2.format(((float) i) / page * 100)+ " %");
		}
		System.err.println(CollectionName+totalDocs+"->"+total.size());
		manager.close();
		client.close();
	}
	
	
	
	
	
	public static void indexTest() {
		Client client = new ElasticSearchUtils().getClient();
		BulkRequestBuilder bulkRequest = client.prepareBulk();

		try {
			bulkRequest.add(client.prepareIndex("news", "article", "1")
			        .setSource(jsonBuilder()
			                    .startObject()
			                        .field("newsTitle", "习近平在中央纪委全会上发表重要讲话")
//			                        .field("newsScratchTime", new Date())
			                        .field("newsContent", "新华社北京1月6日电中共中央总书记、国家主席、中央军委主席习近平6日上午在中国共产党第十八届中央纪律检查委员会第七次全体会议上发表重要讲话。他强调，全面贯彻落实党的十八届六中全会精神，以新的认识指导新的实践，继续在常和长、严和实、深和细上下功夫，坚持共产党人价值观，依靠文化自信坚定理想信念，严肃党内政治生活，强化党内监督，推进标本兼治，全面加强纪律建设，持之以恒抓好作风建设，把反腐败斗争引向深入，不断增强全面从严治党的系统性、创造性、实效性"
			                        		+ "习近平强调，管党治党不仅关系党的前途命运，而且关系国家和民族的前途命运，必须以更大的决心、更大的气力、更大的勇气抓紧抓好。只有把党建设好，我们才能带领人民成功应对重大挑战、抵御重大风险、克服重大阻力、解决重大矛盾，不断从胜利走向新的胜利。党的十八大以来，我们把全面从严治党纳入战略布局、着力从严从细抓管党治党，加强和规范党内政治生活、着力净化党内政治生态，严抓中央八项规定精神落实、着力从作风建设这个环节突破，严明党的政治纪律和政治规矩、着力真管真严、敢管敢严、长管长严，坚持反腐败无禁区、全覆盖、零容忍，着力遏制腐败滋生蔓延势头，惩治群众身边的不正之风和腐败问题、着力增强人民群众获得感，全面强化党内监督、着力发挥巡视利剑作用，推动全面从严治党不断向纵深发展。"
			                        		+ "习近平强调，党的十八大以来，全面从严治党取得显著成效，但仍然任重道远。落实中央八项规定精神是一场攻坚战、持久战，要坚定不移做好工作。要做到惩治腐败力度决不减弱、零容忍态度决不改变，坚决打赢反腐败这场正义之战。要敢于坚持原则，完善配套措施，推动问责制度落地生根。要积极稳妥推进国家监察体制改革，加强统筹协调，做好政策把握和工作衔接。各级纪委要强化自我监督，自觉接受党内和社会监督，建设一支让党放心、人民信赖的纪检干部队伍，为全党全社会树起严格自律的标杆。各级党委要认真落实党中央关于换届工作的部署，坚持党管干部原则不动摇，加强领导，严格把关，严肃纪律，确保换届工作正确方向"
			                        		+ "王岐山在主持会议时指出，习近平总书记的重要讲话，站在时代发展和战略全局的高度，充分肯定全面从严治党取得的显著成效，明确提出今后工作的总体要求和主要任务，强调要冷静清醒判断形势，着眼实现“两个一百年”奋斗目标和中华民族伟大复兴的历史使命，凝聚党心民心，坚定不移推进全面从严治党。讲话既直面问题，又充满自信，蕴含着质朴真挚的为民情怀、许党许国的担当精神。学习贯彻习近平总书记重要讲话是全党的政治任务，要同深入贯彻系列重要讲话精神结合起来，密切联系实际，全面、科学、系统、准确地学习领会，学思践悟、融会贯通，提高政治站位，坚定政治方向，真正内化于心、外化于行。要牢固树立“四个意识”，全面从严治党，严肃党内政治生活，维护好党内政治生态，以优异成绩迎接党的十九大召开")
			                        .field("newsSource", "新浪")
			                    .endObject()
			                  )
			        );
			String source=FileUtils.readFileToString(new File("a.json"));
			System.out.println(source);
			bulkRequest.add(client.prepareIndex("news", "article")
			        .setSource(source));
			BulkResponse bulkResponse = bulkRequest.get();
			if (bulkResponse.hasFailures()) {
				System.out.println("索引失败");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.close();
		
	}

}
