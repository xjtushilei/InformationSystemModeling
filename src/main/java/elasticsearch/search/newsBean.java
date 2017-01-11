package elasticsearch.search;

public class newsBean {
	
	private String newsContent;
	private String newsSource;
	private String newsURL;
	private String newsScratchTime;
	private String newsType;
	private String newsTitle;
	public newsBean() {
		super();
	}
	public newsBean(String newsContent, String newsSource, String newsURL, String newsScratchTime, String newsType,
			String newsTitle) {
		super();
		this.newsContent = newsContent;
		this.newsSource = newsSource;
		this.newsURL = newsURL;
		this.newsScratchTime = newsScratchTime;
		this.newsType = newsType;
		this.newsTitle = newsTitle;
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
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}
	
	
}
