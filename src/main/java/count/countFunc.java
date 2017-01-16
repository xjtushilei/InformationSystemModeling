package count;
/**
 * 这是数据采集的统计界面。没有用在线统计的方式。懒得写了。人工写一下
 * @author shilei
 * @date 2017年1月6日
 */
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import Utils.ElasticSearchUtils;

public class countFunc {
	public static void main(String[] args) {
		newsTypeAndSource();
	}
	
	public static void newsTypeAndSource() {
		TransportClient  client = new ElasticSearchUtils().getClient();
		TermsAggregationBuilder AggnewsSource = AggregationBuilders.terms("newsSource").field("newsSource").size(100);
		TermsAggregationBuilder AggnewsType = AggregationBuilders.terms("newsType").field("newsType").size(100);
		SearchResponse response = client.prepareSearch("news").setTypes("article").setSearchType(SearchType.DEFAULT)
				.setQuery(QueryBuilders.matchAllQuery()) // Query
				.setFrom(0).setSize(0).addAggregation(AggnewsType)
				.addAggregation(AggnewsSource)
				.get();
		
		Aggregations aggs = response.getAggregations();
		LinkedHashMap<String, Long> newsSourceAggregation = new LinkedHashMap<>();
		for (Terms.Bucket entry : ((Terms) aggs.get("newsSource")).getBuckets()) {
			String key = (String) entry.getKey(); // Term
			long count = entry.getDocCount(); // Doc count
			newsSourceAggregation.put(key, count);
		}
		ArrayList<String> sourcename=new ArrayList<>();
		ArrayList<Long> sourcecount=new ArrayList<>();
		for (Entry<String, Long> entry : newsSourceAggregation.entrySet()) {
			sourcename.add(entry.getKey());
			sourcecount.add(entry.getValue());
		}
		System.out.println(sourcecount.size());
		System.out.print("['");
		for (String str : sourcename) {
			System.out.print(str+"','");
		}
		System.out.println("]");
	
		System.out.println(sourcecount);
		sourcecount.forEach(a->System.out.println(a));
		
		
		LinkedHashMap<String, Long> newsTypeAggregation = new LinkedHashMap<>();
		for (Terms.Bucket entry : ((Terms) aggs.get("newsType")).getBuckets()) {
			String key = (String) entry.getKey(); // Term
			long count = entry.getDocCount(); // Doc count
			newsTypeAggregation.put(key, count);
		}
		ArrayList<String> Typename=new ArrayList<>();
		ArrayList<Long> Typecount=new ArrayList<>();
		for (Entry<String, Long> entry : newsTypeAggregation.entrySet()) {
			Typename.add(entry.getKey());
			Typecount.add(entry.getValue());
		}
		System.out.println(Typecount.size());
		System.out.print("['");
		for (String str : Typename) {
			System.out.print(str+"','");
		}
		System.out.println("]");
		System.out.println(Typecount);
	}
}
