package elasticsearch.search;

import java.util.ArrayList;
import java.util.Map;

public class searchResultBean
{
	private String usetime;
	private long total;
	private int page;
	private int pagesize;
	private ArrayList<newsBean> newsList;
	private Map<String , Long> newsSourceAggregation;
	private Map<String , Long> newsTypeAggregation;
	public searchResultBean() {
		super();
	}
	public searchResultBean(String usetime, long total, int page, int pagesize, ArrayList<newsBean> newsList,
			Map<String, Long> newsSourceAggregation, Map<String, Long> newsTypeAggregation) {
		super();
		this.usetime = usetime;
		this.total = total;
		this.page = page;
		this.pagesize = pagesize;
		this.newsList = newsList;
		this.newsSourceAggregation = newsSourceAggregation;
		this.newsTypeAggregation = newsTypeAggregation;
	}
	public String getUsetime() {
		return usetime;
	}
	public void setUsetime(String usetime) {
		this.usetime = usetime;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPagesize() {
		return pagesize;
	}
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
	public ArrayList<newsBean> getNewsList() {
		return newsList;
	}
	public void setNewsList(ArrayList<newsBean> newsList) {
		this.newsList = newsList;
	}
	public Map<String, Long> getNewsSourceAggregation() {
		return newsSourceAggregation;
	}
	public void setNewsSourceAggregation(Map<String, Long> newsSourceAggregation) {
		this.newsSourceAggregation = newsSourceAggregation;
	}
	public Map<String, Long> getNewsTypeAggregation() {
		return newsTypeAggregation;
	}
	public void setNewsTypeAggregation(Map<String, Long> newsTypeAggregation) {
		this.newsTypeAggregation = newsTypeAggregation;
	}
	
	
	
}
