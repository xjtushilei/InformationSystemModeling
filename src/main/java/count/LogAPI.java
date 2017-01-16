package count;
/**
 * 这是日志的页面的两个功能。
 * 		1、显示历史记录
 * 		2、显示一些统计数据
 * 		3、显示热词。top-n
 * @author shilei 
 * @date 2017年1月16日14:26:54
 */
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

import APP.config;
import Utils.MongoManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/log")
@Api(value = "log")
public class LogAPI {

	public static void main(String[] args) {
		getcount();
	}

	@Path("/getHistoryList")
	@GET
	@ApiOperation(value = "最新日志查询", notes = "返回最近的结果列表")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常返回结果", response = LinkedList.class) })
	@Consumes("application/x-www-form-urlencoded" + ";charset=" + "UTF-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=" + "UTF-8")
	public static Response get(
			@DefaultValue("1") @ApiParam(value = "分页功能，页数", required = false) @QueryParam("pageNumber") int page,
			@DefaultValue("10") @ApiParam(value = "分页功能，每页大小", required = false) @QueryParam("pageSize") int pageSize) {

		LinkedList<Map<String, Object>> result = new LinkedList<>();

		// 创建芒果DB的驱动
		MongoManager manager = new MongoManager(config.MongoDB_IP, config.MongoDB_Port, config.MongoDB_DataBase_logs);
		MongoCollection<Document> collection = manager.getMongoDatabase().getCollection(config.MongoDB_collection_logs);

		Long total = collection.count();

		// 按照日期查询历史记录
		Bson orderByDate = new BasicDBObject("time", -1);
		FindIterable<Document> findIterable = collection.find().sort(orderByDate).limit(pageSize)
				.skip((page - 1) * pageSize);

		// 遍历，得到有用的结果
		MongoCursor<Document> mongoCursor = findIterable.iterator();
		while (mongoCursor.hasNext()) {

			HashMap<String, Object> map = new HashMap<>();
			Document doc = mongoCursor.next();
			map.put("q", doc.get("q"));
			map.put("newsType", doc.get("newsType"));
			map.put("newsSource", doc.get("newsSource"));
			map.put("usetime", doc.get("usetime"));
			map.put("city", doc.get("city"));
			map.put("ip", doc.get("ip"));
			map.put("total", doc.get("total"));
			// 存时间，转换一下
			map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date((long) doc.get("time"))));
			result.add(map);
		}

		HashMap<String, Object> RealReult = new HashMap<>();
		RealReult.put("rows", result);
		RealReult.put("total", total);
		return Response.status(200).entity(RealReult).build();

	}
	
	@Path("/getCount")
	@GET
	@ApiOperation(value = "获取统计数据", notes = "获取统计数据")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常返回结果", response = LinkedList.class) })
	@Consumes("application/x-www-form-urlencoded" + ";charset=" + "UTF-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=" + "UTF-8")
	public static Response getcount() {

		HashMap<String, Object> RealReult = new HashMap<>();

		// 创建芒果DB的驱动
		MongoManager manager = new MongoManager(config.MongoDB_IP, config.MongoDB_Port, config.MongoDB_DataBase_logs);
		MongoCollection<Document> collection = manager.getMongoDatabase().getCollection(config.MongoDB_collection_logs);

		/**
		 * 访问次数
		 */
		Long total = collection.count();
		RealReult.put("total", total);
		
        /**
         * 统计访问人数
         */
		DistinctIterable<String> it = collection.distinct("ip", String.class);
		ArrayList<String> list = new ArrayList<>();
		it.into(list);
		RealReult.put("people",list.size());
		
		/**
		 * 统计平均用时
		 */
		List<Bson> pipeline=new ArrayList<Bson>();
		String AvgAgg = "{$group:{_id:'1','avg':{$avg:'$usetime'}}}";
		pipeline.add(Document.parse(AvgAgg));
		AggregateIterable<Document> agg=collection.aggregate(pipeline);
		RealReult.put("avgUsetime",agg.first().get("avg"));
		
		/**
		 * 统计最多搜索的词
		 */
		List<Bson> countpipeline=new ArrayList<Bson>();
		String countAgg = "{$group:{_id:'$q','count':{$sum:1}}}";
		String countSort="{$sort:{count:-1}}";
		String countLimit="{$limit:20}";   //最热的20个词

		
		countpipeline.add(Document.parse(countAgg));
		countpipeline.add(Document.parse(countSort));
		countpipeline.add(Document.parse(countLimit));
		AggregateIterable<Document> Countagg=collection.aggregate(countpipeline);
		
		
		LinkedList<Document> result = new LinkedList<>();
		Countagg.into(result);
//		System.out.println(result.size());
		RealReult.put("Top",result);

		return Response.status(200).entity(RealReult).build();

	}


}
