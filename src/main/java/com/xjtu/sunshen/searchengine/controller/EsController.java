package com.xjtu.sunshen.searchengine.controller;


import com.xjtu.sunshen.searchengine.entity.News;
import com.xjtu.sunshen.searchengine.entity.SearchResult;
import com.xjtu.sunshen.searchengine.repository.NewsElasticsearchRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@RestController
public class EsController {

    @Autowired
    private NewsElasticsearchRepository newsElasticsearchRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @PostMapping("index")
    public News index(String newsURL, String newsContent, String newsSource, String newsTime, String newsType, String newsTitle) {
        return newsElasticsearchRepository.index(new News(newsURL, newsContent, newsSource, newsTime, newsType, newsTitle));
    }

    @GetMapping("search")
    public SearchResult search(
            String q,
            @RequestParam(required = false, defaultValue = "") String newsSource,
            @RequestParam(required = false, defaultValue = "") String newsType,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pagesize) {


        BoolQueryBuilder boolq = new BoolQueryBuilder();
        if (q == null || "".equals(q)) {
            boolq.must(QueryBuilders.matchAllQuery());
        } else {
            boolq.must(QueryBuilders.multiMatchQuery(q, "newsTitle", "newsContent"));
        }
        if (!newsType.equals("")) {
            boolq.must(QueryBuilders.matchPhraseQuery("newsType", newsType));
        }
        if (!newsSource.equals("")) {
            boolq.must(QueryBuilders.matchPhraseQuery("newsSource", newsSource));
        }
        /*
         * 高亮设置
         */
        HighlightBuilder hiBuilder = new HighlightBuilder();
        hiBuilder.preTags("<span style=\'color: red\'>");
        hiBuilder.postTags("</span>");
        hiBuilder.field("newsTitle", 10);
        hiBuilder.field("newsContent", 50);


        /*
         * 聚类.统计新闻来源和新闻的类型
         */
        TermsAggregationBuilder AggnewsSource = AggregationBuilders.terms("newsSource").field("newsSource");
        TermsAggregationBuilder AggnewsType = AggregationBuilders.terms("newsType").field("newsType");
        /*
         * 开始搜索
         */
        SearchResponse response = elasticsearchTemplate.getClient().prepareSearch("news").setTypes("news").setSearchType(SearchType.DEFAULT)
                .setQuery(boolq) // Query
                .setFrom(pagesize * (page - 1)).setSize(pagesize).addAggregation(AggnewsType)
                .addAggregation(AggnewsSource).highlighter(hiBuilder)
                // .addSort(sortbuilder)
                .setExplain(true) // 设置是否按查询匹配度排序
                .get();
        SearchHits myhits = response.getHits();


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


        LinkedList<News> newsList = new LinkedList<>();
        ArrayList<Map<String, Object>> logNewsList = new ArrayList<>();
        for (SearchHit hit : myhits) {
            Map<String, Object> map = hit.getSourceAsMap();
            News News = new News();

            // 获取title
            if (hit.getHighlightFields().containsKey("newsTitle")) {
                Text[] text = hit.getHighlightFields().get("newsTitle").getFragments();
                String content = "";
                for (Text str : text) {
                    content = content + str;
                }
                if (content.length() > 100) {
                    content = content.substring(0, 100);
                }
                News.setNewsTitle(content);
            } else {
                String title = map.get("newsTitle") == null ? "新闻" + q : map.get("newsTitle").toString();
                ;
                if (title.length() > 40) {
                    title = title.substring(0, 40);
                }
                News.setNewsTitle(title);
            }
//			System.out.println(News.getNewsTitle());
            // 获取content
            if (hit.getHighlightFields().containsKey("newsContent")) {
                Text[] text = hit.getHighlightFields().get("newsContent").getFragments();
                String content = "";
                for (Text str : text) {
                    content = content + str;
                }
                if (content.length() > 300) {
                    content = content.substring(0, 300);
                }
                News.setNewsContent(content);
            } else {
                String content = map.get("newsContent") == null ? "新闻" + q : map.get("newsContent").toString();
                if (content.length() > 300) {
                    content = content.substring(0, 300);
                }
                News.setNewsContent(content);
            }
            //获取其他
            News.setNewsTime(map.get("newsTime").toString());
            News.setNewsType(map.get("newsType") == null ? "未知" : map.get("newsType").toString());
            News.setNewsURL(map.get("newsURL").toString());
            News.setNewsSource(map.get("newsSource").toString());

            newsList.add(News);

            logNewsList.add(map);
        }

        /**
         * 开始存储结果
         */
        SearchResult searchResult = new SearchResult();
        searchResult.setPage(page);
        searchResult.setPagesize(pagesize);
        searchResult.setTotal(myhits.getTotalHits());
        searchResult.setUsetime(response.getTook().toString());
        searchResult.setNewsList(newsList);
        searchResult.setNewsSourceAggregation(newsSourceAggregation);
        searchResult.setNewsTypeAggregation(newsTypeAggregation);

        return searchResult;
    }


}
