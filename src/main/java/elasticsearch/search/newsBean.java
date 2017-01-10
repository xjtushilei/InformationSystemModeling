package elasticsearch.search;

public class newsBean {
	private String newsContent;
	private String newsSource;
	private String newsURL;
	private String newsScratchTime;
	private String newsType;
	public newsBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public newsBean(String newsContent, String newsSource, String newsURL, String newsScratchTime, String newsType) {
		super();
		this.newsContent = newsContent;
		this.newsSource = newsSource;
		this.newsURL = newsURL;
		this.newsScratchTime = newsScratchTime;
		this.newsType = newsType;
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
	public String getNewsURL() {
		return newsURL;
	}
	public void setNewsURL(String newsURL) {
		this.newsURL = newsURL;
	}
	public String getNewsScratchTime() {
		return newsScratchTime;
	}
	public void setNewsScratchTime(String newsScratchTime) {
		this.newsScratchTime = newsScratchTime;
	}
	public String getNewsType() {
		return newsType;
	}
	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}
	
}
