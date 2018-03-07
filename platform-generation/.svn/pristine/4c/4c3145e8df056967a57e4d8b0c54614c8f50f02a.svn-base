package com.xinleju.platform.generation.service.impl;

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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.generation.dao.GenSchemeDao;
import com.xinleju.platform.generation.entity.GenerationScheme;
import com.xinleju.platform.generation.entity.GenerationSystable;
import com.xinleju.platform.generation.entity.GenerationSystableColumn;
import com.xinleju.platform.generation.service.GenSchemeService;

@Service
public class GenSchemeServiceImpl extends  BaseServiceImpl<String,GenerationScheme> implements GenSchemeService{
	

	@Autowired
	private GenSchemeDao genSchemeDao;
	private String staticPath="";//"E:/uuid"
	
	private static Logger log = Logger.getLogger(GenSchemeServiceImpl.class);

	
	/**
	 * 生成并下载File
	 * 
	 * @param g_systable
	 * @param l_column
	 * @param l_scheme
	 */
	public byte[] downloadFile(GenerationSystable g_systable,
			List<GenerationSystableColumn> l_column,
			List<GenerationScheme> l_scheme) throws Exception {
		Date tt = new Date();
		log.error("downloadFileStart::staticPath::"+staticPath);
		staticPath = "/tmp/"+IDGenerator.getUUID();
		log.error("downloadFile1::staticPath::"+staticPath);
		Properties properties = new Properties();
		properties
				.put("file.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		properties.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
		properties.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
		System.out.println("downloadFileyongshi 1::::"+(new Date().getTime()-tt.getTime()));
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
		//sql语句
		String tablesql = generationTablSql(g_systable,l_column);
		
		context.put("tablesql", tablesql);
		
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
		
		//创建表
		StringWriter writerTableSql = new StringWriter();
		
		// 从vm目录下加载hello.vm模板,在eclipse工程中该vm目录与src目录平级
		System.out.println("downloadFileyongshi 2::::"+(new Date().getTime()-tt.getTime()));
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
		Velocity.getTemplate("TableSql_template.vm", "utf-8").merge(context,
				writerTableSql);
		System.out.println("downloadFileyongshi 3::::"+(new Date().getTime()-tt.getTime()));
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
		
		//生成表
		String pathTableSql = staticPath + "/tablesql/";

		// "/entity/";
		// String path6 = g_scheme.getLocalUrl() + "/" + packagePath +
		log.error("downloadFile2::staticPath::"+staticPath);
		System.out.println("downloadFileyongshi 4::::"+(new Date().getTime()-tt.getTime()));
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
		CreateFilePath(pathTableSql);
		log.error("downloadFile3::staticPath::"+staticPath);
		System.out.println("downloadFileyongshi 5::::"+(new Date().getTime()-tt.getTime()));
		// 生成带路径以及文件名源文件
//		String fileName0 = path0 + class_name + "Controller.java";
		String fileName1 = path1 + class_name + ".java";
		String fileName2 = path2 + class_name + "Service.java";
		String fileName3 = path3 + class_name + "ServiceImpl.java";
		String fileName4 = path4 + class_name + "Dao.java";
		String fileName5 = path5 + class_name + "DaoImpl.java";
		String fileName6 = path1 + class_name + "Mapper.xml";
		
		String filepathAppController = pathAppController + class_name + "Controller.java";
		String filepathDto = pathDto + class_name + "DtoServiceProducer.java";
		String filepathApiDto = pathApiDto + class_name + "Dto.java";
		String filepathApiDtoService = pathApiDtoService + class_name + "DtoServiceCustomer.java";
		
		String filepathTableSql = pathTableSql + class_name + "CreatTable.sql";
		List<File> files = new ArrayList<File>();
		System.out.println("downloadFileyongshi 6::::"+(new Date().getTime()-tt.getTime()));
		// 创建文件
//		File file0 = CreateFileResult(fileName0, writerController.toString());
		File file1 = CreateFileResult(fileName1, writerEntity.toString());
		System.out.println("downloadFileyongshi 61::::"+(new Date().getTime()-tt.getTime()));
		File file2 = CreateFileResult(fileName2, writerService.toString());
		System.out.println("downloadFileyongshi 62::::"+(new Date().getTime()-tt.getTime()));
		File file3 = CreateFileResult(fileName3, writerServiceImpl.toString());
		System.out.println("downloadFileyongshi 63::::"+(new Date().getTime()-tt.getTime()));
		File file4 = CreateFileResult(fileName4, writerDao.toString());
		System.out.println("downloadFileyongshi 64::::"+(new Date().getTime()-tt.getTime()));
		File file5 = CreateFileResult(fileName5, writerDaoImpl.toString());
		System.out.println("downloadFileyongshi 65::::"+(new Date().getTime()-tt.getTime()));
		File fileAppController = CreateFileResult(filepathAppController, writerController.toString());
		System.out.println("downloadFileyongshi 66::::"+(new Date().getTime()-tt.getTime()));
		File fileDto = CreateFileResult(filepathDto, writerDtoServiceProduct.toString());
		System.out.println("downloadFileyongshi 67::::"+(new Date().getTime()-tt.getTime()));
		File fileApiDto = CreateFileResult(filepathApiDto, writerDto.toString());
		System.out.println("downloadFileyongshi 68::::"+(new Date().getTime()-tt.getTime()));
		File fileApiDtoService = CreateFileResult(filepathApiDtoService, writerDtoServiceCustomer.toString());
		File fileTableSql = CreateFileResult(filepathTableSql, writerTableSql.toString());
		System.out.println("downloadFileyongshi 69::::"+(new Date().getTime()-tt.getTime()));
		// 解决第一行是<?xml version="1.0" encoding="UTF-8"?>这种情况的乱码
		File file6 = CreateFile2Result(fileName6, writerMapper.toString());
		System.out.println("downloadFileyongshi 7::::"+(new Date().getTime()-tt.getTime()));
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
		//添加生成表文件
		files.add(fileTableSql);
		log.error("downloadFile5::staticPath::"+staticPath);
		System.out.println("downloadFileyongshi 8::::"+(new Date().getTime()-tt.getTime()));
		byte[] bb = downLoadFiles(files);
		System.out.println("downloadFileyongshi 9::::"+(new Date().getTime()-tt.getTime()));
		return bb;
//		return null;

	}
	
	/**
	 * 创建文件目录
	 * 
	 * @param path
	 */
	public void CreateFilePath(String path) throws Exception {
		File filePath = new File(path);
		if (!filePath.exists()) {
			log.info("创建[" + filePath.getAbsolutePath() + "]情况："
					+ filePath.mkdirs());
		} else {
			log.info("存在目录：" + filePath.getAbsolutePath());
		}
	}
	
	/**
	 * 创建文件
	 * 
	 * @param fileName
	 * @param writerBody
	 */
	public File CreateFileResult(String fileName, String writerBody) throws Exception {
		File file = new File(fileName);
		FileWriter fw = null;
		try {
			fw = new FileWriter(file);
			fw.write(writerBody);
			log.info("创建[" + fileName + "]成功");
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
//	 * @param filename
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
			log.info("创建[" + fileName + "]成功");
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
	 * 下载downLoadFiles
	 * 
	 * @param files
	 * @return ResponseEntity
	 */
    public byte[] downLoadFiles(List<File> files)
            throws Exception {
    	ByteArrayOutputStream bos = null;
    	ZipOutputStream zipOut  = null;
    	FileOutputStream fous = null;
    	FileInputStream fis = null;
    	HttpHeaders headers = new HttpHeaders(); 
    	ResponseEntity<byte[]> rEntiry = null;
    	byte[] bresult = null;
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
        	
        	//创建要下载的流
            fis = new FileInputStream(file);  
            bos = new ByteArrayOutputStream(fis.available());  
            byte[] b = new byte[1024];  
            int n = 0;  
            while ((n = fis.read(b)) > 0) {
                bos.write(b, 0, n);
            }
            
            //试着headers的属性
            
            bresult =  bos.toByteArray();
//            String dfileName = new String("源码.zip".getBytes("gb2312"), "iso8859-1");
//            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//            headers.setContentDispositionFormData("attachment", dfileName);
//           //要用HttpStatus.OK，不要用HttpStatus.CREATED,后面的谷歌浏览器好使，IE会有错误
//            rEntiry = new ResponseEntity<byte[]>(bos.toByteArray(), headers, HttpStatus.OK);
            
        }catch (Exception e) {
            e.printStackTrace();
            log.error("异常--调用downLoadFiles方法:  【参数"+files+"】======"+"【"+e.getMessage()+"】");
            return null;
        }
        finally{
        	bos.flush();
        	bos.close();
            fis.close();
            //不管成功与否，最后删除生成的文件
            File filePath = new File(staticPath+"/");
            this.deleteAllFilesOfDir(filePath);
        }
        return bresult;
     
    }
    
    /**
     * 把接受的全部文件打成压缩包 
     * @param files;
     * @param outputStream
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
     * @param inputFile
     * @param ouputStream
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
                    log.error("****---++++::::"+staticPath);
                    log.error("****---++++::::"+staticPath.replaceAll("/", "\\\\\\\\"));
                    log.error("****---++++::::"+inputFile.getPath());
//                    log.error("**-**---++++::::"+inputFile.getPath().split(staticPath.replaceAll("/", "\\\\\\\\"))[0]);
//                    log.error("**-**---++++::::"+inputFile.getPath().split(staticPath.replaceAll("/", "\\\\\\\\"))[1]);
//                    ZipEntry entry = new ZipEntry(inputFile.getPath().split(staticPath.replaceAll("/", "\\\\\\\\"))[1].substring(1));//(new   String(inputFile.getName().getBytes("")));
					ZipEntry entry = new ZipEntry(inputFile.getPath().split(staticPath)[1].substring(1));
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
     * @param path
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
    
    /**  
     * 生成表语句
     * @param
     * 
     */
    public String generationTablSql(GenerationSystable g_systable,List<GenerationSystableColumn> l_column){
    	StringBuffer sb = new StringBuffer();
    	try{
    		sb.append(" create table "+ g_systable.getTableName() +" ");
    		sb.append(" ( ");
    		sb.append(" id                   varchar(64) not null comment '主键', ");
    		sb.append(" create_person_id     varchar(64) comment '创建者的ID或者CODE，唯一标识', ");
    		sb.append(" create_person_name   varchar(64) comment '创建者姓名', ");
    		sb.append(" create_org_id        varchar(64) comment '创建人部门ID', ");
    		sb.append(" create_org_name      varchar(64) comment '创建者部门名称', ");
    		sb.append(" create_company_id    varchar(64) comment '创建者单位ID', ");
    		sb.append(" create_company_name  varchar(64) comment '创建这单位名称', ");
    		sb.append(" create_date          timestamp comment '创建时间', ");
    		sb.append(" update_person_id     varchar(64) comment '更新者ID或者CODE', ");
    		sb.append(" update_person_name   varchar(64) comment '更新者姓名', ");
    		sb.append(" update_date          timestamp comment '更新时间', ");
    		sb.append(" delflag              varchar(32) comment '逻辑删除标识（0：正常；1：删除）', ");
    		sb.append(" concurrency_version  int comment '并发版本', ");
    		sb.append(" tend_id              varchar(32) comment '租户', ");
    		
    		for(GenerationSystableColumn gsc:l_column){
    			//判断是否有列类型的长度
    			if(null!=gsc.getColumnLength() && !gsc.getColumnLength().equals("")){
    				//判断是否为空
    				if(null!=gsc.getIsNull() && gsc.getIsNull().equals("0")){
    					sb.append(" "+gsc.getColumnName()+" "+gsc.getColumnType()+"("+gsc.getColumnLength()+") not null comment '"+gsc.getComments()+"', ");
    				}else{
    					sb.append(" "+gsc.getColumnName()+" "+gsc.getColumnType()+"("+gsc.getColumnLength()+") comment '"+gsc.getComments()+"', ");
    				}
    				
    			}else{
    				//判断是否为空
    				if(null!=gsc.getIsNull() && gsc.getIsNull().equals("0")){
    					sb.append(" "+gsc.getColumnName()+" "+gsc.getColumnType()+" not null comment '"+gsc.getComments()+"', ");
    				}else{
    					sb.append(" "+gsc.getColumnName()+" "+gsc.getColumnType()+" comment '"+gsc.getComments()+"', ");
    				}
    			}
    			
    		}
    		sb.append(" primary key (id) ");
    		sb.append(" ) comment '"+g_systable.getComments()+"'; ");
    	}catch(Exception e){
    		e.printStackTrace();
    		sb.append("创建表sql失败：失败异常："+e.getMessage());
    	}
		System.out.println("创建表sql:"+sb.toString());
		return sb.toString();
	}
	
}
