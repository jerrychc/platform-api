package com.xinleju.platform.sysmanager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.org.service.OrgnazationService;
import com.xinleju.platform.sys.org.service.RootService;
import com.xinleju.platform.tools.data.JacksonUtils;

public class RootTest {
	public static RootService rootService = null;
	
	public static OrgnazationService orgnazationService = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ApplicationContext context =new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
		rootService = context.getBean(RootService.class);
		orgnazationService = context.getBean(OrgnazationService.class);
		
		try {
			List<OrgnazationNodeDto> list  = rootService.queryListRoot(null);
			
			for(OrgnazationNodeDto orgnazationNodeDto:list){
				getRootList(orgnazationNodeDto);
			}
			System.out.println(JacksonUtils.toJson(list));
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static OrgnazationNodeDto getRootList(OrgnazationNodeDto orgnazationNodeDto ) throws Exception{
		Map<String,Object> map =new HashMap<String,Object>();
		map.put("parentId", orgnazationNodeDto.getId());
			List<OrgnazationNodeDto> list1  = rootService.queryListRoot(map);
			orgnazationNodeDto.setChildren(list1);
			if(list1!=null && list1.size()>0){
				for(OrgnazationNodeDto orgnazationNodeDto1:list1){
					getRootList(orgnazationNodeDto1);
				}
			}else{
				List<OrgnazationNodeDto> ll= getOrgRootList(orgnazationNodeDto);
				for(OrgnazationNodeDto orgnazationNodeDto2:ll){
					getOrgList(orgnazationNodeDto2);
				}
				return orgnazationNodeDto;
			}
			getOrgList(orgnazationNodeDto);
			return orgnazationNodeDto;
	}
	
	public static List<OrgnazationNodeDto> getOrgRootList(OrgnazationNodeDto orgnazationNodeDto ) throws Exception{
		List<OrgnazationNodeDto> list1  = orgnazationService.queryOrgListRoot(orgnazationNodeDto.getId());
		orgnazationNodeDto.setChildren(list1);
		return list1;
	}
	
	public static OrgnazationNodeDto getOrgList(OrgnazationNodeDto orgnazationNodeDto ) throws Exception{
		List<OrgnazationNodeDto> list1  = orgnazationService.queryOrgList(orgnazationNodeDto.getId());
		orgnazationNodeDto.setChildren(list1);
		if(list1!=null && list1.size()>0){
			for(OrgnazationNodeDto orgnazationNodeDto1:list1){
				getRootList(orgnazationNodeDto1);
			}
		}else{
			return orgnazationNodeDto;
		}
		return orgnazationNodeDto;
}
	

}
