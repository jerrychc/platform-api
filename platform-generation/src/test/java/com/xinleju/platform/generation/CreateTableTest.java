package com.xinleju.platform.generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xinleju.platform.generation.entity.GenerationScheme;
import com.xinleju.platform.generation.entity.GenerationSystable;
import com.xinleju.platform.generation.entity.GenerationSystableColumn;
import com.xinleju.platform.generation.service.GenDbinfoService;
import com.xinleju.platform.generation.service.GenSchemeService;
import com.xinleju.platform.generation.service.GenSystableColumnService;
import com.xinleju.platform.generation.service.GenSystableService;

public class CreateTableTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CreateTableTest t = new CreateTableTest();
		ApplicationContext context =new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
		GenDbinfoService genDbinfoService=context.getBean(GenDbinfoService.class);
		GenSystableService genSystableService=context.getBean(GenSystableService.class);
		GenSystableColumnService genSystableColumnService=context.getBean(GenSystableColumnService.class);
		GenSchemeService genSchemeService=context.getBean(GenSchemeService.class);
		
		GenerationSystable generationSystable = new GenerationSystable();
//		GenerationSystableColumn generationSystableColumn = new GenerationSystableColumn();
//		GenerationScheme generationScheme = new GenerationScheme();
		List<GenerationSystableColumn> list_column = null;
		List<GenerationScheme> list_scheme = null;
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("systableId", "000fbb2eef694532ab9df8c836311150");
			generationSystable = genSystableService.getObjectById("000fbb2eef694532ab9df8c836311150");
			list_column = genSystableColumnService.queryList(map);
			list_scheme = genSchemeService.queryList(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		genDbinfoService.SaveTable(generationSystable, list_column, list_scheme);
//		t.CreateTable(generationSystable,list_column,list_scheme);
		
	}
	 
	/**
	 * 创建表
	 * @param g_systable
	 * @param l_column
	 * @param l_scheme
	 */
	public void CreateTable(GenerationSystable g_systable,List<GenerationSystableColumn> l_column,List<GenerationScheme> l_scheme){
        
	}

}
