package elasticsearch.search;

import java.util.HashMap;
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
import org.elasticsearch.client.Client;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;


import Utils.ElasticSearchUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/search")
@Api(value="search")
public class search {
	public static void main(String[] args) {
		get("野马", "新浪新闻", "汽车", 1, 10, "desc");
	}
	
	@Path("/get")
	@GET
	@ApiOperation(value = "检索入口", notes = "输入检索关键字，展示结果列表")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "正常返回结果", response = HashMap.class),
			@ApiResponse(code = 601, message = "错误", response = String.class)
	})
    @Consumes("application/x-www-form-urlencoded"+ ";charset=" + "UTF-8")
    @Produces(MediaType.APPLICATION_JSON + ";charset=" + "UTF-8")
	public static Response get(
			@DefaultValue("") @ApiParam(value = "key", required = true) @QueryParam("q") String q,     //搜索关键词
			@DefaultValue("新浪新闻") @ApiParam(value = "新闻来源", required = false) @QueryParam("newsSource") String newsSource,  
			@DefaultValue("军事") @ApiParam(value = "新闻分类", required = false) @QueryParam("newsType") String newsType,     
			@DefaultValue("1") @ApiParam(value = "分页功能，页数", required = false) @QueryParam("page") int page,       
    		@DefaultValue("10") @ApiParam(value = "分页功能，每页大小", required = false) @QueryParam("pagesize") int pagesize,   
    		@DefaultValue("desc") @ApiParam(value = "时间排序，默认降序", required = false) @QueryParam("sort") String sort)    
	{
		
		Client client = new ElasticSearchUtils().getClient();
	
		
		BoolQueryBuilder boolq = new BoolQueryBuilder(); 
		if(q.equals(""))
		{
			boolq.must(QueryBuilders.matchAllQuery());
		}
		else {
			boolq.must(QueryBuilders.multiMatchQuery(q, "newsTitle", "newsContent"));
		}
		if(!newsType.equals("")) 
		{
			boolq.must(QueryBuilders.matchPhraseQuery("newsType", newsType));
		}
		if(!newsSource.equals("")) 
		{
			boolq.must(QueryBuilders.matchPhraseQuery("newsSource", newsSource));
		}
		
		/**
		 * 排序
		 */
		SortBuilder<?> sortbuilder=SortBuilders.scoreSort();
		if(!sort.equals(""))
		{
			if(sort.equals("desc")) {
				sortbuilder=SortBuilders.fieldSort("newsScratchTime").order(SortOrder.DESC);
			}
			else {
				sortbuilder=SortBuilders.fieldSort("newsScratchTime").order(SortOrder.ASC);
			}
			
		}
		
		/**
		 * 高亮设置
		 */
		HighlightBuilder hiBuilder=new HighlightBuilder();
        hiBuilder.preTags("<em>");
        hiBuilder.postTags("</em>");
        hiBuilder.field("newsTitle",50);
//        hiBuilder.field("newsContent",150);
		
		/**
		 * 聚类.统计新闻来源和新闻的类型
		 */
		TermsAggregationBuilder AggnewsSource =AggregationBuilders.terms("newsSource").field("newsSource");
		TermsAggregationBuilder AggnewsType =AggregationBuilders.terms("newsType").field("newsType");
		
		
		SearchResponse response = client.prepareSearch("news")
		        .setTypes("article")
		        .setSearchType(SearchType.DEFAULT)
		        .setQuery(boolq)                 // Query
		        .setFrom(pagesize*(page-1))
				.setSize(pagesize)
				.addAggregation(AggnewsType)
				.addAggregation(AggnewsSource)
				.highlighter(hiBuilder)
				.addSort(sortbuilder)
				.setExplain(true)	// 设置是否按查询匹配度排序
		        .get();
		
		SearchHits myhits = response.getHits();
		System.out.println("一共:" + myhits.getTotalHits() + " 结果");
		System.out.println("耗时"+response.getTook());
		System.out.println(response.toString());
		
		Aggregations aggs = response.getAggregations();
		for (Aggregation agg : aggs) {
			
			String aggname=agg.getName();
			Terms genders = aggs.get(aggname);
			for (Terms.Bucket entry : genders.getBuckets()) {
			    String key=(String) entry.getKey();      // Term
			    long count=entry.getDocCount(); // Doc count
			    System.out.println(key+"-"+count);
			}
			
		}
		for(SearchHit hit:myhits){
            System.out.println("Map方式打印高亮内容");
            System.out.println(hit.getHighlightFields());

            System.out.println("遍历高亮集合，打印高亮片段:");
            Text[] text = hit.getHighlightFields().get("newsTitle").getFragments();
            for (Text str : text) {
                System.out.println(str.string());
            }
        }
		return null;
		
		
	}


}
