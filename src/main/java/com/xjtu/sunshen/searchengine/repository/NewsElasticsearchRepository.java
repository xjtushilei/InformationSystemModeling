package com.xjtu.sunshen.searchengine.repository;

import com.xjtu.sunshen.searchengine.entity.News;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface NewsElasticsearchRepository extends ElasticsearchRepository<News, String> {

}