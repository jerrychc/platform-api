package com.xinleju.platform.generation;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.xinleju.platform.generation.entity.GenerationDbinfo;
import com.xinleju.platform.generation.entity.GenerationScheme;
import com.xinleju.platform.generation.entity.GenerationSystable;
import com.xinleju.platform.generation.entity.GenerationSystableColumn;
import com.xinleju.platform.generation.service.GenDbinfoService;
import com.xinleju.platform.generation.service.GenSchemeService;
import com.xinleju.platform.generation.service.GenSystableColumnService;
import com.xinleju.platform.generation.service.GenSystableService;

/**
 * @author shiyong
 *
 */
public class Test {
	
	
	
	public static void main(String[] args) {
		Test g = new Test();
//		g.InsertGenerationDbinfo();
//		g.InsertGenerationSystable();
//		g.InsertGenerationSystableColumn();
		g.InsertGenerationScheme();
		
	}
	public void InsertGenerationSystable(){
		GenerationSystable generationSystable = new GenerationSystable();
		generationSystable.setId("000fbb2eef694532ab9df8c836311111");
		generationSystable.setCreatePersonId("admin");
		generationSystable.setCreatePersonName("管理员");
		generationSystable.setDelflag(true);
		generationSystable.setCreateDate(new Timestamp(new Date().getTime()));
		generationSystable.setIssync("0");
		generationSystable.setClassName("Employee");
		generationSystable.setTableType("0");
		generationSystable.setComments("员工表");
		generationSystable.setShowName("员工");
		generationSystable.setTableName("employee");
		generationSystable.setDbinfoId("000fbb2eef694532ab9df8c83632cba6");
		
		ApplicationContext context =new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
		GenSystableService genSystableService=context.getBean(GenSystableService.class);
		try {
			genSystableService.save(generationSystable);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void InsertGenerationSystableColumn(){
		GenerationSystableColumn generationSystableColumn = new GenerationSystableColumn();
		generationSystableColumn.setId("000fbb2eef694532ab9df8c836322222");
		generationSystableColumn.setCreatePersonId("admin");
		generationSystableColumn.setCreatePersonName("管理员");
		generationSystableColumn.setDelflag(true);
		generationSystableColumn.setCreateDate(new Timestamp(new Date().getTime()));
		
		generationSystableColumn.setSystableId("000fbb2eef694532ab9df8c836311111");
		generationSystableColumn.setName("年龄");
		generationSystableColumn.setShowName("年龄");
		generationSystableColumn.setColumnName("emp_age");
		generationSystableColumn.setColumnType("int");
		generationSystableColumn.setColumnLength("32");
		generationSystableColumn.setComments("员工年龄");
		generationSystableColumn.setJavaType("Integer");
		generationSystableColumn.setJavaField("EmpAge");
		generationSystableColumn.setIsInsert("0");
		generationSystableColumn.setIsUpdate("0");
		generationSystableColumn.setIsList("0");
		generationSystableColumn.setIsQuery("0");
		generationSystableColumn.setIsShow("0");
		generationSystableColumn.setQueryType("范围");
		generationSystableColumn.setShowType("文本框");
		generationSystableColumn.setSort("2");
		
//		generationSystableColumn.setGuuid("000fbb2eef694532ab9df8c836311112");
//		generationSystableColumn.setCreatePersonId("admin");
//		generationSystableColumn.setCreatePersonName("管理员");
//		generationSystableColumn.setDelflag("0");
//		generationSystableColumn.setCreateDate(new Date());
//		
//		generationSystableColumn.setgSystableId("000fbb2eef694532ab9df8c836311111");
//		generationSystableColumn.setName("姓名");
//		generationSystableColumn.setShowName("姓名");
//		generationSystableColumn.setColumnName("emp_name");
//		generationSystableColumn.setColumnType("varchar");
//		generationSystableColumn.setColumnLength("64");
//		generationSystableColumn.setComments("员工姓名");
//		generationSystableColumn.setJavaType("String");
//		generationSystableColumn.setJavaField("EmpName");
//		generationSystableColumn.setIsInsert("0");
//		generationSystableColumn.setIsUpdate("0");
//		generationSystableColumn.setIsList("0");
//		generationSystableColumn.setIsQuery("0");
//		generationSystableColumn.setIsShow("0");
//		generationSystableColumn.setQueryType("左右LIKE");
//		generationSystableColumn.setShowType("文本框");
//		generationSystableColumn.setSort("1");
		
		
		ApplicationContext context =new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
		GenSystableColumnService genSystableColumnService=context.getBean(GenSystableColumnService.class);
		try {
			genSystableColumnService.save(generationSystableColumn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void InsertGenerationScheme(){
		GenerationScheme generationScheme = new GenerationScheme();
		generationScheme.setId("000fbb2eef694532ab9df8c836311113");
		generationScheme.setCreatePersonId("admin");
		generationScheme.setCreatePersonName("管理员");
		generationScheme.setDelflag(true);
		generationScheme.setCreateDate(new Timestamp(new Date().getTime()));
		generationScheme.setSystableId("000fbb2eef694532ab9df8c836311111");
		generationScheme.setSchemeName("生成员工类方案");
		generationScheme.setCodeStyle("0");
		generationScheme.setPackageName("com.xinleju.platform.generation");
		generationScheme.setLocalUrl("C://code");
		generationScheme.setModuleName("Employee");
		generationScheme.setFunctionName("functionName");
		generationScheme.setAuthor("sy");
		generationScheme.setDescribes("员工信息");
		
		ApplicationContext context =new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
		GenSchemeService genSchemeService=context.getBean(GenSchemeService.class);
		try {
			genSchemeService.save(generationScheme);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void InsertGenerationDbinfo(){
		GenerationDbinfo gdbinfo = new GenerationDbinfo();
		gdbinfo.setId("000fbb2eef694532ab9df8c83632cba6");
		gdbinfo.setCreatePersonId("admin");
		gdbinfo.setCreatePersonName("管理员");
		gdbinfo.setSyscode("GT");
		gdbinfo.setSysname("代码自动生成");
		gdbinfo.setDbType("mysql");
		gdbinfo.setDbInstanceName("generationtest");
		gdbinfo.setDbIp("192.168.3.81");
		gdbinfo.setDbPort("3306");
		gdbinfo.setDbUsername("putest");
		gdbinfo.setDbPassword("putest123");
		
		
		ApplicationContext context =new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
		GenDbinfoService genDbinfoService=context.getBean(GenDbinfoService.class);
		try {
			genDbinfoService.save(gdbinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
