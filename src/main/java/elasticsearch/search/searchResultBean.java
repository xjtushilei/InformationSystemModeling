package elasticsearch.search;

import java.util.ArrayList;

public class searchResultBean
{
	private String usetime;
	private long total;
	private int page;
	private int pagesize;
	private ArrayList<newsBean> newsList;
	private ArrayList<facetBean> facets;
	public searchResultBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public searchResultBean(String usetime, long total, int page, int pagesize, ArrayList<newsBean> newsList,
			ArrayList<facetBean> facets) {
		super();
		this.usetime = usetime;
		this.total = total;
		this.page = page;
		this.pagesize = pagesize;
		this.newsList = newsList;
		this.facets = facets;
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
	public ArrayList<facetBean> getFacets() {
		return facets;
	}
	public void setFacets(ArrayList<facetBean> facets) {
		this.facets = facets;
	}
	
	
	
	
}
