package elasticsearch.search;
/**
 * @author shilei
 * @date  2017年1月10日14:23:14 
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import APP.config;
import Utils.ElasticSearchUtils;
import Utils.MongoManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/search")
@Api(value = "search")
public class searchAPI {


	public static void main(String[] args) {
		get("中国汽车", "新浪新闻", "", 1, 10, "测试ip","测试-西安市");
	}

	
	@Path("/get")
	@GET
	@ApiOperation(value = "检索入口", notes = "输入检索关键字，展示结果列表")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "正常返回结果", response = searchResultBean.class)})
	@Consumes("application/x-www-form-urlencoded" + ";charset=" + "UTF-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=" + "UTF-8")
	public static Response get(
			@DefaultValue("") @ApiParam(value = "key", required = true) @QueryParam("q") String q, // 搜索关键词
			@DefaultValue("") @ApiParam(value = "新闻来源", required = false) @QueryParam("newsSource") String newsSource,
			@DefaultValue("") @ApiParam(value = "新闻分类", required = false) @QueryParam("newsType") String newsType,
			@DefaultValue("1") @ApiParam(value = "分页功能，页数", required = false) @QueryParam("page") int page,
			@DefaultValue("10") @ApiParam(value = "分页功能，每页大小", required = false) @QueryParam("pagesize") int pagesize,
			@DefaultValue("0.0.0.0") @ApiParam(value = "ip", required = false) @QueryParam("ip") String ip,
			@DefaultValue("西安") @ApiParam(value = "ip所在地", required = false) @QueryParam("city") String city) {
		/*
		 * 获取ip，并记录查询记录
		 */
//		System.out.println(q);
//		System.out.println(newsSource);
//		System.out.println(newsType);
//		System.out.println(page);
//		System.out.println(pagesize);
//		System.out.println(ip);
//		System.out.println(city);
		
		
		TransportClient  client = new ElasticSearchUtils().getClient();
		BoolQueryBuilder boolq = new BoolQueryBuilder();
		if (q.equals("")) {
			boolq.must(QueryBuilders.matchAllQuery());
			// multiMatchQuery(q, "newsTitle", "newsContent")
		} else {
			boolq.must(QueryBuilders.multiMatchQuery(q, "newsTitle","newsContent"));
//			boolq.must(QueryBuilders.matchQuery("newsTitle", q));
//			boolq.must(QueryBuilders.matchQuery("newsContent", q));
		}
		if (!newsType.equals("")) {
			boolq.must(QueryBuilders.matchPhraseQuery("newsType", newsType));
		}
		if (!newsSource.equals("")) {
			boolq.must(QueryBuilders.matchPhraseQuery("newsSource", newsSource));
		}
		
		/**
		 * 高亮设置
		 */
		HighlightBuilder hiBuilder = new HighlightBuilder();
		hiBuilder.preTags("<span class=\'pointKey\'>");
		hiBuilder.postTags("</span>");
		hiBuilder.field("newsTitle", 10);
		hiBuilder.field("newsContent", 50);

		
		/**
		 * 聚类.统计新闻来源和新闻的类型
		 */
		TermsAggregationBuilder AggnewsSource = AggregationBuilders.terms("newsSource").field("newsSource");
		TermsAggregationBuilder AggnewsType = AggregationBuilders.terms("newsType").field("newsType");

		/**
		 * 开始搜索
		 */
		SearchResponse response = client.prepareSearch("news").setTypes("article").setSearchType(SearchType.DEFAULT)
				.setQuery(boolq) // Query
				.setFrom(pagesize * (page - 1)).setSize(pagesize).addAggregation(AggnewsType)
				.addAggregation(AggnewsSource).highlighter(hiBuilder)  
				// .addSort(sortbuilder)
				.setExplain(true) // 设置是否按查询匹配度排序
				.get();
		SearchHits myhits = response.getHits();
//		logger.info(response.toString());

		
		Aggregations aggs = response.getAggregations();
		Map<String, Long> newsSourceAggregation = new HashMap<>();
		for (Terms.Bucket entry : ((Terms) aggs.get("newsSource")).getBuckets()) {
			String key = (String) entry.getKey(); // Term
			long count = entry.getDocCount(); // Doc count
			newsSourceAggregation.put(key, count);
		}

		
		Map<String, Long> newsTypeAggregation = new HashMap<>();
		for (Terms.Bucket entry : ((Terms) aggs.get("newsType")).getBuckets()) {
			String key = (String) entry.getKey(); // Term
			long count = entry.getDocCount(); // Doc count
			newsTypeAggregation.put(key, count);
		}

		
		LinkedList<newsBean> newsList = new LinkedList<>();
		ArrayList<Map<String, Object>> logNewsList=new ArrayList<>();
		for (SearchHit hit : myhits) {
			Map<String, Object> map = hit.sourceAsMap();
			newsBean newsBean = new newsBean();

			// 获取title
			if (hit.getHighlightFields().containsKey("newsTitle")) {
				Text[] text = hit.getHighlightFields().get("newsTitle").getFragments();
				String content = "";
				for (Text str : text) {
					content = content + str;
				}
				if (content.length()>100) {
					content=content.substring(0,100);
				}
				newsBean.setNewsTitle(content);
			} else {
				String title=map.get("newsTitle")==null?"新闻"+q:map.get("newsTitle").toString();;
				if (title.length()>40) {
					title=title.substring(0, 40);
				}
				newsBean.setNewsTitle(title);
			}
//			System.out.println(newsBean.getNewsTitle());
			// 获取content
			if (hit.getHighlightFields().containsKey("newsContent")) {
				Text[] text = hit.getHighlightFields().get("newsContent").getFragments();
				String content = "";
				for (Text str : text) {
					content = content + str;
				}
				if (content.length()>300) {
					content=content.substring(0,300);
				}
				newsBean.setNewsContent(content);
			} else {
				String content=map.get("newsContent")==null?"新闻"+q:map.get("newsContent").toString();
				if (content.length()>300) {
					content=content.substring(0,300);
				}
				newsBean.setNewsContent(content);
			}
			//获取其他
			newsBean.setNewsScratchTime(map.get("newsScratchTime")==null?"2017-01-07 22:21:01":map.get("newsScratchTime").toString());
			newsBean.setNewsType(map.get("newsType")==null?"未知":map.get("newsType").toString());
			newsBean.setNewsURL(map.get("newsURL").toString());
			newsBean.setNewsSource(map.get("newsSource").toString());
			
			newsList.add(newsBean);
			
			logNewsList.add(map);
		}
		
		/**
		 * 开始存储结果
		 */
		searchResultBean searchResult = new searchResultBean();
		searchResult.setPage(page);
		searchResult.setPagesize(pagesize);
		searchResult.setTotal(myhits.getTotalHits());
		searchResult.setUsetime(response.getTook().toString());
		searchResult.setNewsList(newsList);
		searchResult.setNewsSourceAggregation(newsSourceAggregation);
		searchResult.setNewsTypeAggregation(newsTypeAggregation);

		/**
		 * 开始处理日志！
		 */
		if (page==1) {
			Map<String, Object> logMap=new HashMap<>();
			logMap.put("time",new Date());
			logMap.put("q", q);
			logMap.put("newsSource", newsSource);
			logMap.put("newsType", newsType);
			logMap.put("pagesize", pagesize);
			logMap.put("page", page);
			logMap.put("city", city);
			logMap.put("ip", ip);
			logMap.put("usetime", response.getTookInMillis());
			logMap.put("total",myhits.getTotalHits());
			logMap.put("newsList", logNewsList);
			ObjectMapper mapper = new ObjectMapper();  
			try {
				String json=mapper.writeValueAsString(logMap);
//				System.out.println(mapper.writeValueAsString(searchResult));
				//创建芒果DB的驱动 		
				MongoManager manager = new MongoManager(config.MongoDB_IP, config.MongoDB_Port, config.MongoDB_DataBase_logs);
				manager.insertOneDocument("Searchlog", json);
				manager.close();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		
		return Response.status(200).entity(searchResult).build();
		

	}

}
