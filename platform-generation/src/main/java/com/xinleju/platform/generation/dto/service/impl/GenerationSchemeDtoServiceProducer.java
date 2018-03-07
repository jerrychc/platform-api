package com.xinleju.platform.generation.dto.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.generation.dto.service.GenerationSchemeDtoServiceCustomer;
import com.xinleju.platform.generation.entity.GenerationScheme;
import com.xinleju.platform.generation.entity.GenerationSystable;
import com.xinleju.platform.generation.entity.GenerationSystableColumn;
import com.xinleju.platform.generation.service.GenDbinfoService;
import com.xinleju.platform.generation.service.GenSchemeService;
import com.xinleju.platform.generation.service.GenSystableColumnService;
import com.xinleju.platform.generation.service.GenSystableService;
import com.xinleju.platform.tools.data.JacksonUtils;


public class GenerationSchemeDtoServiceProducer implements GenerationSchemeDtoServiceCustomer{
	private static Logger log = Logger.getLogger(GenerationSchemeDtoServiceProducer.class);
	@Autowired
	private GenSchemeService genSchemeService;
	
	@Autowired
	private GenSystableService genSystableService;

	@Autowired
	private GenSystableColumnService genSystableColumnService;
	
	@Autowired
	private GenDbinfoService genDbinfoService;


	@Override
	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   GenerationScheme generationScheme=JacksonUtils.fromJson(saveJson, GenerationScheme.class);
		   genSchemeService.save(generationScheme);
		   info.setResult(JacksonUtils.toJson(generationScheme));
		   info.setSucess(true);
		   info.setMsg("保存对象成功!");
		} catch (Exception e) {
		 info.setSucess(false);
		 info.setMsg("保存对象失败!");
		 info.setExceptionMsg(e.getMessage());
		}
	   return JacksonUtils.toJson(info);
	}

	@Override
	public String saveBatch(String userInfo, String saveJsonList)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateBatch(String userInfo, String updateJsonList)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(String userInfo, String updateJson)  {
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   GenerationScheme generationScheme=JacksonUtils.fromJson(updateJson, GenerationScheme.class);
			   int result=   genSchemeService.update(generationScheme);
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("更新对象成功!");
			} catch (Exception e) {
			 info.setSucess(false);
			 info.setMsg("更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   GenerationScheme generationScheme=JacksonUtils.fromJson(deleteJson, GenerationScheme.class);
			   int result= genSchemeService.deleteObjectById(generationScheme.getId());
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   List<String> list=Arrays.asList(deleteJsonList.split(","));
			   int result= genSchemeService.deleteAllObjectByIds(list);
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String getObjectById(String userInfo, String getJson)
	 {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			GenerationScheme generationScheme=JacksonUtils.fromJson(getJson, GenerationScheme.class);
			GenerationScheme	result = genSchemeService.getObjectById(generationScheme.getId());
			info.setResult(JacksonUtils.toJson(result));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 info.setSucess(false);
			 info.setMsg("获取对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPage(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=genSchemeService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=genSchemeService.getPage(new HashMap(), null, null);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 info.setSucess(false);
			 info.setMsg("获取分页对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryList(String userInfo, String paramater){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List list=genSchemeService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=genSchemeService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getCount(String userInfo, String paramater)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String downloadFile(String userInfo, String saveJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		Date t = new Date();
//		System.out.println("======"+new Date().getTime());
		Map<String, Object> map = JacksonUtils.fromJson(saveJson, HashMap.class);
		
		// 查询业务表信息
		try {
			GenerationSystable generationSystable = genSystableService
					.getObjectById(map.get("systableId").toString());
		
			// 通过业务表外键查询业务表对应的字段信息
			List<GenerationSystableColumn> list_column = genSystableColumnService
					.queryList(map);
			// 通过业务表外键查询业务表生成方案内容
			List<GenerationScheme> list_scheme = genSchemeService
					.queryList(map);
			
			byte[] bresult =  genSchemeService.downloadFile(generationSystable, list_column, list_scheme);
			Base64 b64 = new Base64();
			info.setSucess(true);
			info.setResult(b64.encodeToString(bresult));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("===yongshi==="+(new Date().getTime()-t.getTime()));
		return JacksonUtils.toJson(info);
	}
	
	@Override
	public String createTable(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		GenerationSystable generationSystable = new GenerationSystable();
//		GenerationSystableColumn generationSystableColumn = new GenerationSystableColumn();
//		GenerationScheme generationScheme = new GenerationScheme();
		List<GenerationSystableColumn> list_column = null;
		List<GenerationScheme> list_scheme = null;
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
//				map.put("systableId", "000fbb2eef694532ab9df8c836311141");
				generationSystable = genSystableService.getObjectById((String)map.get("systableId"));
				list_column = genSystableColumnService.queryList(map);
				list_scheme = genSchemeService.queryList(map);
				
				genDbinfoService.SaveTable(generationSystable, list_column, list_scheme);
				
				info.setResult("建表成功!");
			    info.setSucess(true);
			    info.setMsg("建表成功!");
			}else{
				info.setResult("建表失败");
			    info.setSucess(false);
			    info.setMsg("建表失败，参数为空");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 info.setSucess(false);
			 info.setMsg("建表失败");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo,
			String deleteJsonList) {
		// TODO Auto-generated method stub
		return null;
	}


}
