package APP;
import java.util.HashSet;
import java.util.Set;

import org.glassfish.jersey.server.ResourceConfig;

import io.swagger.jaxrs.config.BeanConfig;

public class MyRregister extends ResourceConfig {  
    public MyRregister() {  
    	/**
    	 * swagger文件信息
    	 */
    	BeanConfig beanConfig = new BeanConfig();
		beanConfig.setVersion("1.0.2");
		beanConfig.setSchemes(new String[] { "http" });
		beanConfig.setHost("localhost:8080");
		beanConfig.setBasePath("/InformationSystemModeling");
		beanConfig.setResourcePackage("APP,API");
		beanConfig.setScan(true);
		
	
    	//加载拦截器
    	register(CorsFilter.class);//自己实现过滤器来实现
    	
    	
    	
    	//swagger  注册服务
        Set<Class<?>> resources = new HashSet<>();  
        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);  
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);  
        registerClasses(resources);  
        
        //下面的是个人资源类
    	register(test.class);
//    	register(api.class);
    }  
}  