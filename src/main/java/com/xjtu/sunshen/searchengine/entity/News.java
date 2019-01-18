package com.xjtu.sunshen.searchengine.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(type = "news", indexName = "news", shards = 4, replicas = 0)
public class News {

    @Id
    private String newsURL;

    private String newsContent;

    @Field(type = FieldType.Keyword)
    private String newsSource;

    @Field(type = FieldType.Keyword)
    private String newsTime;

    @Field(type = FieldType.Keyword)
    private String newsType;

    private String newsTitle;

    @Override
    public String toString() {
        return "News{" +
                "newsURL='" + newsURL + '\'' +
                ", newsContent='" + newsContent + '\'' +
                ", newsSource='" + newsSource + '\'' +
                ", newsTime=" + newsTime +
                ", newsType='" + newsType + '\'' +
                ", newsTitle='" + newsTitle + '\'' +
                '}';
    }

    public News() {
        super();
    }

    public News(String newsURL, String newsContent, String newsSource, String newsTime, String newsType, String newsTitle) {
        this.newsURL = newsURL;
        this.newsContent = newsContent;
        this.newsSource = newsSource;
        this.newsTime = newsTime;
        this.newsType = newsType;
        this.newsTitle = newsTitle;
    }

    public String getNewsURL() {
        return newsURL;
    }

    public void setNewsURL(String newsURL) {
        this.newsURL = newsURL;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getNewsSource() {
        return newsSource;
    }

    public void setNewsSource(String newsSource) {
        this.newsSource = newsSource;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getNewsType() {
        return newsType;
    }

    public void setNewsType(String newsType) {
        this.newsType = newsType;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }
}
