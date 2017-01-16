package APP;
/**
 * 可以通过cors-filter包来实现跨域，也可以自己实现过滤器来实现
 * @author shilei
 * @date 2016年9月10日
 */

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
 
public class CorsFilter implements ContainerResponseFilter {
 
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
        throws IOException {
 
            responseContext.getHeaders().add("X-Powered-By", "shlei :-)");

            responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
            responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma,"
            		+ " Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
//            responseContext.getHeaders().add("Access-Control-Max-Age", "1209600");
    }
}
