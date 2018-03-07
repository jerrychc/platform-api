package com.xinleju.platform.generation;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.generation.dto.service.GenerationSchemeDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;

public class ReferenceGenerationSchemeDtoTest {

	public static void main(String[] args){
		 
	        try {
	        	 ApplicationContext ctx = new  ClassPathXmlApplicationContext(new String[]{"dubbo-customer.xml"});
	        	 GenerationSchemeDtoServiceCustomer generationSchemeDtoServiceCustomer= (GenerationSchemeDtoServiceCustomer) ctx.getBean("generationSchemeDtoServiceCustomer");
	        	 
	     		String guuid = "000fbb2eef694532ab9df8c836311111";
	     		
	     		// 设置外键参数
	     		Map<String, Object> map = new HashMap<String, Object>();
	     		map.put("g_systable_id", guuid);
	        	 
	     		String dubboResultInfo = generationSchemeDtoServiceCustomer.downloadFile(null,JacksonUtils.toJson(map));
				DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
				if(dubboServiceResultInfo.isSucess()){
					String resultInfo= dubboServiceResultInfo.getResult();
					Base64 b64 = new Base64();  
				    byte[] buffer = b64.decode(resultInfo);  
					//试着headers的属性
		            System.out.println(buffer);
				}
	   		  //为保证服务一直开着，利用输入流的阻塞来模拟
				//System.in.read();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 


	}
}
