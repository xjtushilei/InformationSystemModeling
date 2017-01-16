package APP;


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

@Path("/test")
@Api(value = "test")
public class test {

	public static void main(String[] args) {
		System.out.println(get("1").getEntity().toString());
	}

	@GET
	@Path("/ceshi")
	@ApiOperation(value = "获取一门课程所有知识点", notes = "输入课程名称，获得该课程的所有知识点")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "正常返回结果", response = HashMap.class),
			@ApiResponse(code = 401, message = "错误", response = String.class)
	})
	@Consumes("application/x-www-form-urlencoded" + ";charset=" + "UTF-8")
	@Produces(MediaType.APPLICATION_JSON + ";charset=" + "UTF-8")
	public static Response get(
			@DefaultValue("高等数学") @ApiParam(value = "课程名称", required = true) @QueryParam("domain") String domain) {
		
			if (domain.equals("1")) {
				return Response.status(401).entity("状态信息。不能输出1").build();

			}
			else
			{
				HashMap<String , String > HashMap=new HashMap<>();
				HashMap.put("自己的json", domain);
				HashMap.put("123", "自21313json");
				return Response.status(200).entity(HashMap).build();
			}

	}

	

}