package com.xinleju.platform.generation;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.generation.entity.GenerationScheme;
import com.xinleju.platform.generation.entity.GenerationSystable;
import com.xinleju.platform.generation.entity.GenerationSystableColumn;
import com.xinleju.platform.generation.service.GenSchemeService;
import com.xinleju.platform.generation.service.GenSystableColumnService;
import com.xinleju.platform.generation.service.GenSystableService;

public class GenerationTest {
	
	private String staticPath="";//"E:/uuid"
	
	private static Logger log = Logger.getLogger(GenerationTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		GenerationTest t = new GenerationTest();
		
		ApplicationContext context =new ClassPathXmlApplicationContext(new String[] {"applicationContext.xml"});
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
			map.put("g_systable_id", "000fbb2eef694532ab9df8c836311111");
			generationSystable = genSystableService.getObjectById("000fbb2eef694532ab9df8c836311111");
			list_column = genSystableColumnService.queryList(map);
			list_scheme = genSchemeService.queryList(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		try {
			t.downloadFile(generationSystable, list_column, list_scheme);
		} catch (Exception e) {
			System.out.println("发生错误");
			e.printStackTrace();
		}
		
		//生成文件（带entity，service和dao）
//		t.printFile(generationSystable,list_column,list_scheme);
		
//		//生成Entity
//		t.printEntity(generationSystable,list_column,list_scheme);
//		//生成Service
//		t.printService(generationSystable,list_column,list_scheme);
//		//生成ServiceImpl
//		t.printServiceImpl(generationSystable,list_column,list_scheme);
//		//生成Dao
//		t.printDao(generationSystable,list_column,list_scheme);
//		//生成DaoImpl
//		t.printDaoImpl(generationSystable,list_column,list_scheme);
//		//生成Controller
//		t.printController(generationSystable,list_column,list_scheme);
		
//		t.print2();
		
	}
	
	/**
	 * 生成File
	 * @param g_systable
	 * @param l_column
	 * @param l_scheme
	 */
	public void printFile(GenerationSystable g_systable,List<GenerationSystableColumn> l_column,List<GenerationScheme> l_scheme){
		//初始化参数  
        Properties properties=new Properties();  
        //设置velocity资源加载方式为file  
        properties.setProperty("resource.loader", "file");  
        //设置velocity资源加载方式为file时的处理类  
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");  
        //实例化一个VelocityEngine对象  
        VelocityEngine velocityEngine=new VelocityEngine(properties);  
        //实例化一个VelocityContext  
        VelocityContext context=new VelocityContext();  
        //向VelocityContext中放入键值  
        GenerationScheme g_scheme = l_scheme.get(0);
        //包路径
        String package_name = g_scheme.getPackageName();
        context.put("package_name", package_name);
        //模块名
        String modulename = g_scheme.getModuleName();
        context.put("modulename", modulename);
        //作者
        String author = g_scheme.getAuthor();
        context.put("author", author); 
        //表名
        String table_name = g_systable.getTableName();
        context.put("table_name", table_name.toUpperCase()); 
        //表描述
        String comments = g_systable.getComments();
        context.put("comments", comments); 
        //生成entity类名
        String class_name = g_systable.getClassName();
        context.put("class_name", class_name); 
        
        context.put("g_systable", g_systable);
        context.put("g_scheme", g_scheme);
        context.put("l_column", l_column);
        
        //实例化StringWriter  
        StringWriter writerController=new StringWriter();  
        StringWriter writerEntity=new StringWriter();  
        StringWriter writerService=new StringWriter();  
        StringWriter writerServiceImpl=new StringWriter();  
        StringWriter writerDao=new StringWriter();  
        StringWriter writerDaoImpl=new StringWriter();  
        //从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级  
        velocityEngine.mergeTemplate("vm/Controller_template.vm", "utf-8", context, writerController);
//        System.out.println(writerController.toString());         
        velocityEngine.mergeTemplate("vm/Entity_template.vm", "utf-8", context, writerEntity);
//        System.out.println(writerEntity.toString());       
        velocityEngine.mergeTemplate("vm/Service_template.vm", "utf-8", context, writerService);
//        System.out.println(writerService.toString());       
        velocityEngine.mergeTemplate("vm/ServiceImpl_template.vm", "utf-8", context, writerServiceImpl);
//        System.out.println(writerServiceImpl.toString());       
        velocityEngine.mergeTemplate("vm/Dao_template.vm", "utf-8", context, writerDao);
//        System.out.println(writerDao.toString());       
        velocityEngine.mergeTemplate("vm/DaoImpl_template.vm", "utf-8", context, writerDaoImpl);
//        System.out.println(writerDaoImpl.toString());       
        
        //替换包名下的“。”换成文件路径的“/”
        String packagePath = package_name.replaceAll("\\.", "/");
        
        //生成不同entity和service以及dao的地址路径
        String path0 = g_scheme.getLocalUrl() + "/" + packagePath + "/controller/";
        String path1 = g_scheme.getLocalUrl() + "/" + packagePath + "/entity/";
        String path2 = g_scheme.getLocalUrl() + "/" + packagePath + "/service/";
        String path3 = g_scheme.getLocalUrl() + "/" + packagePath + "/service/impl/";
        String path4 = g_scheme.getLocalUrl() + "/" + packagePath + "/dao/";
        String path5 = g_scheme.getLocalUrl() + "/" + packagePath + "/dao/impl/";
        //创建文件夹
        CreateFilePath(path0);
        CreateFilePath(path1);
        CreateFilePath(path2);
        CreateFilePath(path3);
        CreateFilePath(path4);
        CreateFilePath(path5);
        //生成带路径以及文件名源文件
        String fileName0 = path0 + class_name + "Controller.java";
        String fileName1 = path1 + class_name + ".java";
        String fileName2 = path2 + class_name + "Service.java";
        String fileName3 = path3 + class_name + "ServiceImpl.java";
        String fileName4 = path4 + class_name + "Dao.java";
        String fileName5 = path5 + class_name + "DaoImpl.java";
        //创建文件
        CreateFile(fileName0,writerController.toString());
        CreateFile(fileName1,writerEntity.toString());
        CreateFile(fileName2,writerService.toString());
        CreateFile(fileName3,writerServiceImpl.toString());
        CreateFile(fileName4,writerDao.toString());
        CreateFile(fileName5,writerDaoImpl.toString());
        
	}
	
	/**
	* 创建文件目录
	* @param path
	*/
	public void CreateFilePath(String path){
		File filePath = new File(path);
		if(!filePath.exists()){
			System.out.println("创建["+filePath.getAbsolutePath()+"]情况："+filePath.mkdirs());
		}else{
			System.out.println("存在目录："+filePath.getAbsolutePath());
		}
	}
	
	/**
	* 创建文件
	* @param filename
	*/
	public void CreateFile(String fileName,String writerBody){
		File file = new File(fileName);
        FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(writerBody);
		} catch (IOException e) {
			e.printStackTrace();
		}
        finally{
        	if(null != fw){
				try {
	    			fw.flush();
	    			fw.close();
	    		} catch (IOException ee) {
	    			ee.printStackTrace();
	    		}
			}
        }
		System.out.println("创建["+fileName+"]成功");
	}
	
	/**
	 * 生成entity
	 * @param g_systable
	 * @param l_column
	 * @param l_scheme
	 */
	public void printEntity(GenerationSystable g_systable,List<GenerationSystableColumn> l_column,List<GenerationScheme> l_scheme){
		//初始化参数  
        Properties properties=new Properties();  
        //设置velocity资源加载方式为file  
        properties.setProperty("resource.loader", "file");  
        //设置velocity资源加载方式为file时的处理类  
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");  
        //实例化一个VelocityEngine对象  
        VelocityEngine velocityEngine=new VelocityEngine(properties);  
        //实例化一个VelocityContext  
        VelocityContext context=new VelocityContext();  
        //向VelocityContext中放入键值  
        GenerationScheme g_scheme = l_scheme.get(0);
        //包路径
        String package_name = g_scheme.getPackageName();
        context.put("package_name", package_name);
        //模块名
        String modulename = g_scheme.getModuleName();
        context.put("modulename", modulename);
        //作者
        String author = g_scheme.getAuthor();
        context.put("author", author); 
        //表名
        String table_name = g_systable.getTableName();
        context.put("table_name", table_name.toUpperCase()); 
        //表描述
        String comments = g_systable.getComments();
        context.put("comments", comments); 
        //生成entity类名
        String class_name = g_systable.getClassName();
        context.put("class_name", class_name); 
        
        context.put("g_systable", g_systable);
        context.put("g_scheme", g_scheme);
        context.put("l_column", l_column);
        
        //实例化一个StringWriter  
        StringWriter writer=new StringWriter();  
        //从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级  
        velocityEngine.mergeTemplate("vm/Entity_template.vm", "utf-8", context, writer); 
        System.out.println(writer.toString());         
	}
	
	/**
	 * 生成Service
	 * @param g_systable
	 * @param l_column
	 * @param l_scheme
	 */
	public void printService(GenerationSystable g_systable,List<GenerationSystableColumn> l_column,List<GenerationScheme> l_scheme){
		//初始化参数  
        Properties properties=new Properties();  
        //设置velocity资源加载方式为file  
        properties.setProperty("resource.loader", "file");  
        //设置velocity资源加载方式为file时的处理类  
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");  
        //实例化一个VelocityEngine对象  
        VelocityEngine velocityEngine=new VelocityEngine(properties);  
        //实例化一个VelocityContext  
        VelocityContext context=new VelocityContext();  
        //向VelocityContext中放入键值  
        
        
        GenerationScheme g_scheme = l_scheme.get(0);
        //包路径
        String package_name = g_scheme.getPackageName();
        context.put("package_name", package_name);
        //模块名
        String modulename = g_scheme.getModuleName();
        context.put("modulename", modulename);
        //作者
        String author = g_scheme.getAuthor();
        context.put("author", author); 
        //表名
        String table_name = g_systable.getTableName();
        context.put("table_name", table_name.toUpperCase()); 
        //表描述
        String comments = g_systable.getComments();
        context.put("comments", comments); 
        //生成entity类名
        String class_name = g_systable.getClassName();
        context.put("class_name", class_name); 
        
        context.put("g_systable", g_systable);
        context.put("g_scheme", g_scheme);
        context.put("l_column", l_column);
        
        //实例化一个StringWriter  
        StringWriter writer=new StringWriter();  
        //从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级  
        velocityEngine.mergeTemplate("vm/Service_template.vm", "utf-8", context, writer);  
        System.out.println(writer.toString());  
	}
	
	/**
	 * 生成ServiceImpl
	 * @param g_systable
	 * @param l_column
	 * @param l_scheme
	 */
	public void printServiceImpl(GenerationSystable g_systable,List<GenerationSystableColumn> l_column,List<GenerationScheme> l_scheme){
		//初始化参数  
        Properties properties=new Properties();  
        //设置velocity资源加载方式为file  
        properties.setProperty("resource.loader", "file");  
        //设置velocity资源加载方式为file时的处理类  
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");  
        //实例化一个VelocityEngine对象  
        VelocityEngine velocityEngine=new VelocityEngine(properties);  
        //实例化一个VelocityContext  
        VelocityContext context=new VelocityContext();  
        //向VelocityContext中放入键值  
        
        
        GenerationScheme g_scheme = l_scheme.get(0);
        //包路径
        String package_name = g_scheme.getPackageName();
        context.put("package_name", package_name);
        //模块名
        String modulename = g_scheme.getModuleName();
        context.put("modulename", modulename);
        //作者
        String author = g_scheme.getAuthor();
        context.put("author", author); 
        //表名
        String table_name = g_systable.getTableName();
        context.put("table_name", table_name.toUpperCase()); 
        //表描述
        String comments = g_systable.getComments();
        context.put("comments", comments); 
        //生成entity类名
        String class_name = g_systable.getClassName();
        context.put("class_name", class_name); 
        
        context.put("g_systable", g_systable);
        context.put("g_scheme", g_scheme);
        context.put("l_column", l_column);
        
        //实例化一个StringWriter  
        StringWriter writer=new StringWriter();  
        //从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级  
        velocityEngine.mergeTemplate("vm/ServiceImpl_template.vm", "utf-8", context, writer);  
        System.out.println(writer.toString());  
	}
	
	/**
	 * 生成Dao
	 * @param g_systable
	 * @param l_column
	 * @param l_scheme
	 */
	public void printDao(GenerationSystable g_systable,List<GenerationSystableColumn> l_column,List<GenerationScheme> l_scheme){
		//初始化参数  
        Properties properties=new Properties();  
        //设置velocity资源加载方式为file  
        properties.setProperty("resource.loader", "file");  
        //设置velocity资源加载方式为file时的处理类  
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");  
        //实例化一个VelocityEngine对象  
        VelocityEngine velocityEngine=new VelocityEngine(properties);  
        //实例化一个VelocityContext  
        VelocityContext context=new VelocityContext();  
        //向VelocityContext中放入键值  
        
        
        GenerationScheme g_scheme = l_scheme.get(0);
        //包路径
        String package_name = g_scheme.getPackageName();
        context.put("package_name", package_name);
        //模块名
        String modulename = g_scheme.getModuleName();
        context.put("modulename", modulename);
        //作者
        String author = g_scheme.getAuthor();
        context.put("author", author); 
        //表名
        String table_name = g_systable.getTableName();
        context.put("table_name", table_name.toUpperCase()); 
        //表描述
        String comments = g_systable.getComments();
        context.put("comments", comments); 
        //生成entity类名
        String class_name = g_systable.getClassName();
        context.put("class_name", class_name); 
        
        context.put("g_systable", g_systable);
        context.put("g_scheme", g_scheme);
        context.put("l_column", l_column);
        
        //实例化一个StringWriter  
        StringWriter writer=new StringWriter();  
        //从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级  
        velocityEngine.mergeTemplate("vm/Dao_template.vm", "utf-8", context, writer);  
        System.out.println(writer.toString());  
	}
	
	/**
	 * 生成DaoImpl
	 * @param g_systable
	 * @param l_column
	 * @param l_scheme
	 */
	public void printDaoImpl(GenerationSystable g_systable,List<GenerationSystableColumn> l_column,List<GenerationScheme> l_scheme){
		//初始化参数  
        Properties properties=new Properties();  
        //设置velocity资源加载方式为file  
        properties.setProperty("resource.loader", "file");  
        //设置velocity资源加载方式为file时的处理类  
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");  
        //实例化一个VelocityEngine对象  
        VelocityEngine velocityEngine=new VelocityEngine(properties);  
        //实例化一个VelocityContext  
        VelocityContext context=new VelocityContext();  
        //向VelocityContext中放入键值  
        
        
        GenerationScheme g_scheme = l_scheme.get(0);
        //包路径
        String package_name = g_scheme.getPackageName();
        context.put("package_name", package_name);
        //作者
        String author = g_scheme.getAuthor();
        context.put("author", author); 
        //表名
        String table_name = g_systable.getTableName();
        context.put("table_name", table_name.toUpperCase()); 
        //表描述
        String comments = g_systable.getComments();
        context.put("comments", comments); 
        //生成entity类名
        String class_name = g_systable.getClassName();
        context.put("class_name", class_name); 
        
        context.put("g_systable", g_systable);
        context.put("g_scheme", g_scheme);
        context.put("l_column", l_column);
        
        //实例化一个StringWriter  
        StringWriter writer=new StringWriter();  
        //从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级  
        velocityEngine.mergeTemplate("vm/DaoImpl_template.vm", "utf-8", context, writer);  
        System.out.println(writer.toString());  
	}
	
	/**
	 * 生成Controller
	 * @param g_systable
	 * @param l_column
	 * @param l_scheme
	 */
	public void printController(GenerationSystable g_systable,List<GenerationSystableColumn> l_column,List<GenerationScheme> l_scheme){
		//初始化参数  
        Properties properties=new Properties();  
        //设置velocity资源加载方式为file  
        properties.setProperty("resource.loader", "file");  
        //设置velocity资源加载方式为file时的处理类  
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");  
        //实例化一个VelocityEngine对象  
        VelocityEngine velocityEngine=new VelocityEngine(properties);  
        //实例化一个VelocityContext  
        VelocityContext context=new VelocityContext();  
        //向VelocityContext中放入键值  
        
        
        GenerationScheme g_scheme = l_scheme.get(0);
        //包路径
        String package_name = g_scheme.getPackageName();
        context.put("package_name", package_name);
        //模块名
        String modulename = g_scheme.getModuleName();
        context.put("modulename", modulename);
        //作者
        String author = g_scheme.getAuthor();
        context.put("author", author); 
        //表名
        String table_name = g_systable.getTableName();
        context.put("table_name", table_name.toUpperCase()); 
        //表描述
        String comments = g_systable.getComments();
        context.put("comments", comments); 
        //生成entity类名
        String class_name = g_systable.getClassName();
        context.put("class_name", class_name); 
        
        context.put("g_systable", g_systable);
        context.put("g_scheme", g_scheme);
        context.put("l_column", l_column);
        
        //实例化一个StringWriter  
        StringWriter writer=new StringWriter();  
        //从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级  
        velocityEngine.mergeTemplate("vm/Controller_template.vm", "utf-8", context, writer);  
        System.out.println(writer.toString());  
	}
	
	public void print2(){
		//初始化参数  
        Properties properties=new Properties();  
        //设置velocity资源加载方式为file  
        properties.setProperty("resource.loader", "file");  
        //设置velocity资源加载方式为file时的处理类  
        properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");  
        //实例化一个VelocityEngine对象  
        VelocityEngine velocityEngine=new VelocityEngine(properties);  
          
        //实例化一个VelocityContext  
        VelocityContext context=new VelocityContext();  
        //向VelocityContext中放入键值  
        context.put("name", "张三");  
        //实例化一个StringWriter  
        StringWriter writer=new StringWriter();  
        //从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级  
        velocityEngine.mergeTemplate("vm/Entity_template.vm", "utf-8", context, writer);  
        System.out.println(writer.toString());  
	}
	public void print1(){
		Properties p = new Properties();
		p.put("file.resource.loader.class",
		"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		//初始化  
		Velocity.init();  
		//取得VelocityContext对象  
		VelocityContext context = new VelocityContext();  
		//向context中放入要在模板中用到的数据对象  
		context.put( "name", new String("Velocity") );  
		Template template = null;  
		//选择要用到的模板  
		try  
		{  
//		   template = Velocity.getTemplate("mytemplate.vm");
			template = Velocity.getTemplate("vm/Entity_template.vm");  
		}  
		catch( ResourceNotFoundException rnfe )  
		{  
			System.out.println("couldn't find the template  ");
		}  
		catch( ParseErrorException pee )  
		{  
			System.out.println("syntax error : problem parsing the template "); 
		}  
		catch( MethodInvocationException mie )  
		{  
			System.out.println("something invoked in the template "); 
		// threw an exception  
		}  
		catch( Exception e )  
		{}  
		  
		StringWriter sw = new StringWriter();  
		//合并输出  
		template.merge( context, sw ); 
		 System.out.println(sw.toString());  
	}
	
	
	
	/**
	 * 生成并下载Filennnnnnnnnnnnnnnn
	 * 
	 * @param g_systable
	 * @param l_column
	 * @param l_scheme
	 */
	public ResponseEntity<byte[]> downloadFile(GenerationSystable g_systable,
			List<GenerationSystableColumn> l_column,
			List<GenerationScheme> l_scheme) throws Exception {
		staticPath = "E:/"+IDGenerator.getUUID();
		Properties properties = new Properties();
		properties
				.put("file.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
		properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		// 初始化
		Velocity.init(properties);
		// 取得VelocityContext对象
		VelocityContext context = new VelocityContext();
		// 向VelocityContext中放入键值
		GenerationScheme g_scheme = l_scheme.get(0);
		// 包路径
		String package_name = g_scheme.getPackageName();
		context.put("package_name", package_name);
		// 模块名
		String modulename = g_scheme.getModuleName();
		context.put("modulename", modulename);
		// 作者
		String author = g_scheme.getAuthor();
		context.put("author", author);
		// 表名
		String table_name = g_systable.getTableName();
		context.put("table_name", table_name.toUpperCase());
		// 表描述
		String comments = g_systable.getComments();
		context.put("comments", comments);
		// 生成entity类名
		String class_name = g_systable.getClassName();
		context.put("class_name", class_name);

		context.put("g_systable", g_systable);
		context.put("g_scheme", g_scheme);
		context.put("l_column", l_column);

		// 实例化StringWriter
		//app：controller
		StringWriter writerController = new StringWriter();
		//模块：
		StringWriter writerEntity = new StringWriter();
		StringWriter writerService = new StringWriter();
		StringWriter writerServiceImpl = new StringWriter();
		StringWriter writerDao = new StringWriter();
		StringWriter writerDaoImpl = new StringWriter();
		StringWriter writerMapper = new StringWriter();
		//模块：dtoserviceproduct
		StringWriter writerDtoServiceProduct = new StringWriter();
		//api：dto和dtoservicecustomer
		StringWriter writerDto = new StringWriter();
		StringWriter writerDtoServiceCustomer = new StringWriter();
		
		// 从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级

		Velocity.getTemplate("Controller_template.vm", "utf-8").merge(context,
				writerController);
		Velocity.getTemplate("Entity_template.vm", "utf-8").merge(context,
				writerEntity);
		Velocity.getTemplate("Service_template.vm", "utf-8").merge(context,
				writerService);
		Velocity.getTemplate("ServiceImpl_template.vm", "utf-8").merge(context,
				writerServiceImpl);
		Velocity.getTemplate("Dao_template.vm", "utf-8").merge(context,
				writerDao);
		Velocity.getTemplate("DaoImpl_template.vm", "utf-8").merge(context,
				writerDaoImpl);
		Velocity.getTemplate("MapperXml_template.vm", "utf-8").merge(context,
				writerMapper);
		Velocity.getTemplate("DtoServiceProduct_template.vm", "utf-8").merge(context,
				writerDtoServiceProduct);
		
		Velocity.getTemplate("Dto_template.vm", "utf-8").merge(context,
				writerDto);
		Velocity.getTemplate("DtoServiceCustomer_template.vm", "utf-8").merge(context,
				writerDtoServiceCustomer);

		// 替换包名下的“。”换成文件路径的“/”
		String packagePath = package_name.replaceAll("\\.", "/");

		// 生成不同entity和service以及dao的地址路径
		
//		String path0 = staticPath + "/" + modulename+ "/" + packagePath + "/controller/";
		String path1 = staticPath + "/" + modulename+ "/" + packagePath + "/entity/";
		String path2 = staticPath + "/" + modulename+ "/" + packagePath + "/service/";
		String path3 = staticPath + "/" + modulename+ "/" + packagePath + "/service/impl/";
		String path4 = staticPath + "/" + modulename+ "/" + packagePath + "/dao/";
		String path5 = staticPath + "/" + modulename+ "/" + packagePath + "/dao/impl/";
		String pathDto = staticPath + "/" + modulename+ "/" + packagePath + "/dto/service/impl/";
		
		//app:controller地址
		String pathAppController = staticPath + "/app/" + packagePath + "/controller/";
		//api:dto和dtoservice
		String pathApiDto        = staticPath + "/api/" + packagePath + "/dto/";
		String pathApiDtoService = staticPath + "/api/" + packagePath + "/dto/service/";

		// "/entity/";
		// String path6 = g_scheme.getLocalUrl() + "/" + packagePath +
		
		// 创建文件夹
//		CreateFilePath(path0);
		CreateFilePath(path1);
		CreateFilePath(path2);
		CreateFilePath(path3);
		CreateFilePath(path4);
		CreateFilePath(path5);
		CreateFilePath(pathDto);
		CreateFilePath(pathAppController);
		CreateFilePath(pathApiDto);
		CreateFilePath(pathApiDtoService);
		// 生成带路径以及文件名源文件
//		String fileName0 = path0 + class_name + "Controller.java";
		String fileName1 = path1 + class_name + ".java";
		String fileName2 = path2 + class_name + "Service.java";
		String fileName3 = path3 + class_name + "ServiceImpl.java";
		String fileName4 = path4 + class_name + "Dao.java";
		String fileName5 = path5 + class_name + "DaoImpl.java";
		String fileName6 = path1 + class_name + "Mapper.xml";
		
		String filepathAppController = pathAppController + class_name + "Controller.java";
		String filepathDto = pathDto + class_name + "DtoServiceProduct.java";
		String filepathApiDto = pathApiDto + class_name + "Dto.java";
		String filepathApiDtoService = pathApiDtoService + class_name + "DtoServiceCustomer.java";
		List<File> files = new ArrayList<File>();
		// 创建文件
//		File file0 = CreateFileResult(fileName0, writerController.toString());
		File file1 = CreateFileResult(fileName1, writerEntity.toString());
		File file2 = CreateFileResult(fileName2, writerService.toString());
		File file3 = CreateFileResult(fileName3, writerServiceImpl.toString());
		File file4 = CreateFileResult(fileName4, writerDao.toString());
		File file5 = CreateFileResult(fileName5, writerDaoImpl.toString());
		
		File fileAppController = CreateFileResult(filepathAppController, writerController.toString());
		File fileDto = CreateFileResult(filepathDto, writerDtoServiceProduct.toString());
		File fileApiDto = CreateFileResult(filepathApiDto, writerDto.toString());
		File fileApiDtoService = CreateFileResult(filepathApiDtoService, writerDtoServiceCustomer.toString());
		// 解决第一行是<?xml version="1.0" encoding="UTF-8"?>这种情况的乱码
		File file6 = CreateFile2Result(fileName6, writerMapper.toString());
		//把要打包的所有文件放到list中
//		files.add(file0);
		files.add(file1);
		files.add(file2);
		files.add(file3);
		files.add(file4);
		files.add(file5);
		files.add(file6);
		
		files.add(fileAppController);
		files.add(fileDto);
		files.add(fileApiDto);
		files.add(fileApiDtoService);
		
		return downLoadFiles(files);
//		return null;

	}
	
	/**
	 * 创建文件
	 * 
	 * @param filenamennnnnnnnnnnnnnnn
	 * @param writerBody
	 */
	public File CreateFileResult(String fileName, String writerBody) throws Exception {
		File file = new File(fileName);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(writerBody);
			System.out.println("创建[" + fileName + "]成功");
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			log.error("异常--调用CreateFileResult方法:  【参数"+fileName+"】======"+"【"+e.getMessage()+"】");
			return null;
		} finally {
			if (null != fw) {
				try {
					fw.flush();
					fw.close();
				} catch (IOException ee) {
					ee.printStackTrace();
					log.error("异常--调用CreateFileResult方法:  【参数"+fileName+"】======"+"【"+ee.getMessage()+"】");
					
				}
			}
		}

	}
	
	/**
	 * 创建文件2
	 * 
	 * @param filenamennnnnnnnnnnnnnnn
	 * @param writerBody
	 */
	public File CreateFile2Result(String fileName, String writerBody)
			throws Exception {
		File file = new File(fileName);
		FileOutputStream fos = null;
		PrintStream ps = null;

		try {
			fos = new FileOutputStream(file);
			ps = new PrintStream(fos, true, "UTF-8");// 这里我们就可以设置编码了
			ps.print(writerBody.toString());
			System.out.println("创建[" + fileName + "]成功");
			return file;
		} catch (IOException e) {
			e.printStackTrace();
			log.error("异常--调用CreateFile2Result方法:  【参数"+fileName+"】======"+"【"+e.getMessage()+"】");
			return null;
		} finally {
			if (null != ps) {
				try {
					ps.flush();
					ps.close();
				} catch (Exception ee) {
					ee.printStackTrace();
					log.error("异常--调用CreateFile2Result方法:  【参数"+fileName+"】======"+"【"+ee.getMessage()+"】");
				}
			}
			if (null != fos) {
				try {
					fos.close();
				} catch (IOException ee) {
					ee.printStackTrace();
					log.error("异常--调用CreateFile2Result方法:  【参数"+fileName+"】======"+"【"+ee.getMessage()+"】");
				}
			}

		}
	}
	
	/**
	 * 下载downLoadFilesnnnnnnnnn
	 * 
	 * @param files
	 * @return ResponseEntity
	 */
    public ResponseEntity<byte[]> downLoadFiles(List<File> files)
            throws Exception {
    	ByteArrayOutputStream bos = null;
    	ZipOutputStream zipOut  = null;
    	FileOutputStream fous = null;
    	FileInputStream fis = null;
    	HttpHeaders headers = new HttpHeaders(); 
    	ResponseEntity<byte[]> rEntiry = null;
        try {
     
            /**创建一个临时压缩文件，
             * 把文件流全部注入到这个文件中
             * 这里的文件你可以自定义是.rar还是.zip*/
        	
        	String zipPath = staticPath+"/源码.zip";//"E:/uuid/源码.zip"
        	//目录应该是存在的，因为要打包文件也在这个目录下
        	File filePath = new File(staticPath+"/");
    		if (!filePath.exists()) {
    			log.info("创建[" + filePath.getAbsolutePath() + "]情况："
    					+ filePath.mkdirs());
    		} else {
    			log.info("存在目录：" + filePath.getAbsolutePath());
    		}
    		
            File file = new File(zipPath);
            if (!file.exists()){   
                file.createNewFile();   
            }
            
            //创建文件输出流
            fous = new FileOutputStream(file);   
            /**打包的方法我们会用到ZipOutputStream这样一个输出流,
             * 所以这里我们把输出流转换一下*/
            zipOut  = new ZipOutputStream(fous);
            /**这个方法接受的就是一个所要打包文件的集合，
             * 还有一个ZipOutputStream*/
            zipFile(files, zipOut);
            
            //打包文件后要关闭掉流，否则后续进行下载时，不会报错，但是下载的打包文件，会发生最后一个文件不成功，打开下载的压缩包会有不可预料的文件末端的错误
            zipOut.flush();
        	zipOut.close();
        	fous.flush();
        	fous.close();
        	
//        	//创建要下载的流
//            fis = new FileInputStream(file);  
//            bos = new ByteArrayOutputStream(fis.available());  
//            byte[] b = new byte[1024];  
//            int n = 0;  
//            while ((n = fis.read(b)) > 0) {
//                bos.write(b, 0, n);
//            }
//            
//            //试着headers的属性
//            
//            String dfileName = new String("源码.zip".getBytes("gb2312"), "iso8859-1");
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            headers.setContentDispositionFormData("attachment", dfileName);
//           //要用HttpStatus.OK，不要用HttpStatus.CREATED,后面的谷歌浏览器好使，IE会有错误
//            rEntiry = new ResponseEntity<byte[]>(bos.toByteArray(), headers, HttpStatus.OK);
//            
        }catch (Exception e) {
            e.printStackTrace();
            log.error("异常--调用downLoadFiles方法:  【参数"+files+"】======"+"【"+e.getMessage()+"】");
            return null;
        }
        finally{
//        	bos.flush();
//        	bos.close();
//            fis.close();
            //不管成功与否，最后删除生成的文件
//            File filePath = new File(staticPath+"/");
//            this.deleteAllFilesOfDir(filePath);
        }
        return rEntiry;
     
    }
    
    /**
     * 把接受的全部文件打成压缩包 
     * @param List<File>;  
     * @param org.apache.tools.zip.ZipOutputStream  
     */
    public void zipFile
            (List<File> files,ZipOutputStream outputStream) {
        int size = files.size();
        for(int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile(file, outputStream);
        }
    }
    
    /**  
     * 根据输入的文件与输出流对文件进行打包
     * @param File
     * @param org.apache.tools.zip.ZipOutputStream
     */
    public void zipFile(File inputFile,
            ZipOutputStream ouputStream) {
    	
        try {
            if(inputFile.exists()) {
                /**如果是目录的话这里是不采取操作的，
                 * 至于目录的打包正在研究中*/
                if (inputFile.isFile()) {
                    FileInputStream IN = new FileInputStream(inputFile);
                    BufferedInputStream bins = new BufferedInputStream(IN, 512);
                    ZipEntry entry = new ZipEntry(inputFile.getPath().split(staticPath.replaceAll("/", "\\\\\\\\"))[1].substring(1));//(new   String(inputFile.getName().getBytes("")));
                    ouputStream.putNextEntry(entry);
                    // 向压缩文件中输出数据   
                    int nNumber;
                    byte[] buffer = new byte[512];
                    while ((nNumber = bins.read(buffer)) != -1) {
                        ouputStream.write(buffer, 0, nNumber);
                    }
                    // 关闭创建的流对象   
                    bins.close();
                    IN.close();
                } else {
                    try {
                        File[] files = inputFile.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            zipFile(files[i], ouputStream);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("异常--调用zipFile方法:  【参数"+inputFile+"】======"+"【"+e.getMessage()+"】");
                    }
                }
            }
        } catch (Exception e) {
        	log.error("异常--调用zipFile方法:  【参数"+inputFile+"】======"+"【"+e.getMessage()+"】");
            e.printStackTrace();
        }
    }
    
    /**  
     * 删除文件或者删除文件夹
     * @param File
     * 
     */
    public void deleteAllFilesOfDir(File path) {  
        if (!path.exists())  
            return;  
        if (path.isFile()) {
            log.info("删除[" + path.getAbsolutePath() + "]情况："
					+ path.delete());
            return;  
        }  
        File[] files = path.listFiles(); 
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);  
        }
        log.info("删除[" + path.getAbsolutePath() + "]情况："
				+ path.delete());
    }
	

}
