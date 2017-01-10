package elasticsearch.search;

import java.io.IOException;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/search")
@Api(value="search")
public class search {
	public static void main(String[] args) throws IOException {
	}
	
	@Path("/get")
	@GET
	@ApiOperation(value = "检索入口", notes = "输入检索关键字，展示结果列表")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "正常返回结果", response = HashMap.class),
			@ApiResponse(code = 601, message = "错误", response = String.class)
	})
    @Consumes("application/x-www-form-urlencoded"+ ";charset=" + "UTF-8")
    @Produces(MediaType.APPLICATION_JSON + ";charset=" + "UTF-8")
	public static Response search(@DefaultValue("") @QueryParam("q") String q,     //搜索关键词
			@DefaultValue("新浪新闻") @ApiParam(value = "新闻来源", required = false) @QueryParam("newsSource") String Language,  
			@DefaultValue("军事") @ApiParam(value = "新闻来源", required = false) @QueryParam("newsType") String Weeks,     
			@DefaultValue("1") @ApiParam(value = "分页功能，页数", required = false) @QueryParam("page") int page,       
    		@DefaultValue("10") @ApiParam(value = "分页功能，每页大小", required = false) @QueryParam("pagesize") int pagesize,   
    		@DefaultValue("desc") @ApiParam(value = "时间排序，默认降序", required = false) @QueryParam("order") String sortorder)    
	{
		
		
		
		
		
		
		
		
		return null;
		
		
	}


}
