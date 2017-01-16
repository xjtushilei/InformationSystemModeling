package Utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.AggregationOutput;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import APP.config;

@SuppressWarnings("unused")
public class test {

	public static void main(String[] args) {
		test3();
	}
	public void test1() {
		final List<BigDecimal> prices =Arrays.asList(
			    new BigDecimal("10"), new BigDecimal("30"), new BigDecimal("17"),
			    new BigDecimal("20"), new BigDecimal("15"), new BigDecimal("18"),
			    new BigDecimal("45"), new BigDecimal("12"));
		
		final BigDecimal totalOfDiscountedPrices = 
			    prices.stream()
			          .filter(price -> price.compareTo(BigDecimal.valueOf(20)) > 0)
			          .map(price -> price.multiply(BigDecimal.valueOf(0.9)))
			          .reduce(BigDecimal.ZERO, BigDecimal::add);
			System.out.println("Total of discounted prices: " + totalOfDiscountedPrices);
	}
	
	
	@SuppressWarnings({ "rawtypes" })
	public static void test2() throws IOException {
		MongoManager manager = new MongoManager(config.MongoDB_IP, config.MongoDB_Port, "logs");
		MongoDatabase database=manager.getMongoDatabase();
		System.out.println(manager.size("Searchlog"));
		 String groupStr = "{$group:{_id:{'eval':'$eval'},docsNum:{$sum:1}}}";
	        DBObject group = (DBObject) JSON.parse(groupStr);
	        String matchStr = "{$match:{docsNum:{$gte:85}}}";
	        DBObject match = (DBObject) JSON.parse(matchStr);
	        String sortStr = "{$sort:{_id.docsNum:-1}}";
	        DBObject sort = (DBObject) JSON.parse(sortStr);
//	   	 { 
//	 	 	"$group" : {
//	 	 					 "_id": "avgUsetime",
//	 	 					 	"my":{ "$avg": "$usetime" }
//	 				 	}
//	 }
	        /**
	         * 统计访问人数
	         */
//	        		 DistinctIterable<String> it=collection.distinct("ip",String.class);
//	        		 ArrayList<String> list=new ArrayList<>();
//	        		 it.into(list);
//	        		 System.out.println(list.size());
	        		 
	        		 /**
	        		  * 次数
	        		  */
//	        		System.out.println(collection.count());
	        		String avg=FileUtils.readFileToString(new File("a.txt"));
	        		System.out.println(avg);
	        		MongoCollection<Document> collection = manager.getMongoDatabase().getCollection(config.MongoDB_collection_logs);

	        			
	        			
	        		@SuppressWarnings("unchecked")
					List<Bson> pipeline=new ArrayList();

	        		pipeline.add(Document.parse(avg));
	        		AggregateIterable<Document> agg=collection.aggregate(pipeline);
	        		System.out.println(agg.first().get("my"));
	        	
	        		
	}
	public static void test3() {
		
		System.out.println(new Date());
	}
}
