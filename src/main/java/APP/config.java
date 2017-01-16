package APP;

public class config {
	/**
	 * 芒果DB的配置
	 */
	public static String MongoDB_IP="202.117.54.85";
	public static int MongoDB_Port=27017;
	public static String MongoDB_DataBase="SpiderNews";
	public static String MongoDB_DataBase_logs="logs";
	public static String MongoDB_collection_logs="Searchlog";
	
	
	/**
	 * elasticsearch 配置
	 */
	public static String ES_clusterName="shilei-application";
	public static String ES_ip="localhost";
	public static int ES_port=9300;
}
