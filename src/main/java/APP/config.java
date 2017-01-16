package APP;
/**
 * 这是一个全局的配置文件，制定了elasticsearch的ip和芒果DB的ip等
 * @author shilei
 * @date 2017年1月16日14:29:40 
 */

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
	public static String ES_ip="202.117.54.85";
	public static int ES_port=9300;
}
