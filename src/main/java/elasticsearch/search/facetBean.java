package elasticsearch.search;

import java.util.Map;

public class facetBean {
	private String facetname;
	private Map<String , Long> facetlist;
	public String getFacetname() {
		return facetname;
	}
	public void setFacetname(String facetname) {
		this.facetname = facetname;
	}
	public Map<String , Long> getFacetlist() {
		return facetlist;
	}
	public void setFacetlist(Map<String , Long> facetlist) {
		this.facetlist = facetlist;
	}
}
