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

/**
 * 执行建表语句的的工具类
 * @author zhengjiajie
 *
 */
public class ExcuteCreateTableUtil {

	public static void main(String[] args) {
		
		/*String idItems[] = {"000fbb2eef694532ab9df8c836391160",
				"000fbb2eef694532ab9df8c836391161",
				"000fbb2eef694532ab9df8c836391162",
				"000fbb2eef694532ab9df8c836391163",
				"000fbb2eef694532ab9df8c836391164",
				"000fbb2eef694532ab9df8c836391165",
				"000fbb2eef694532ab9df8c836391166",
				"000fbb2eef694532ab9df8c836391167",
				"000fbb2eef694532ab9df8c836391168",
				"000fbb2eef694532ab9df8c836391169"};*/
		
		//String idItems[] = {"000fbb2eef694532ab9df8c836391170"};
		//String idItems[] = {"abcdefghijklmn-flow-101"};
		String idItems[] = {"platform-zhengjj-flow-0218-001","platform-zhengjj-flow-0218-002","platform-zhengjj-flow-0218-003","platform-zhengjj-flow-0218-004","platform-zhengjj-flow-0218-005"};

		createTableFromArray(idItems);
	}

	public static void createTableFromArray(String idItems[]){
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
		for(String idText : idItems){
			try {
				Map<String,Object> map = new HashMap<String,Object>();
				//map.put("systableId", "000fbb2eef694532ab9df8c836311150");
				map.put("systableId", idText);
				//generationSystable = genSystableService.getObjectById("000fbb2eef694532ab9df8c836311150");
				generationSystable = genSystableService.getObjectById(idText);
				list_column = genSystableColumnService.queryList(map);
				list_scheme = genSchemeService.queryList(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
			genDbinfoService.SaveTable(generationSystable, list_column, list_scheme);
//			t.CreateTable(generationSystable,list_column,list_scheme);
		}
	}
}
