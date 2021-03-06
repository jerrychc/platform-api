package com.xinleju.platform.univ.attachment.dto.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.univ.attachment.dto.service.AttachmentCategoryDtoServiceCustomer;
import com.xinleju.platform.univ.attachment.entity.AttachmentCategory;
import com.xinleju.platform.univ.attachment.service.AttachmentCategoryService;
import com.xinleju.platform.univ.attachment.service.AttachmentService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author haoqp
 * 
 *
 */
 
public class AttachmentCategoryDtoServiceProducer implements AttachmentCategoryDtoServiceCustomer{
	private static Logger log = Logger.getLogger(AttachmentCategoryDtoServiceProducer.class);
	@Autowired
	private AttachmentCategoryService attachmentCategoryService;
	
	@Autowired
	private AttachmentService attachmentService;
	

	public String save(String userInfo, String saveJson){
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   AttachmentCategory attachmentCategory=JacksonUtils.fromJson(saveJson, AttachmentCategory.class);
		   // 编码唯一性验证
		   Map<String, Object> parameter = new HashMap<>();
		   parameter.put("code", attachmentCategory.getCode());
		   List<AttachmentCategory> acList = attachmentCategoryService.queryList(parameter);
		   if (acList != null && acList.size() > 0) {
			   info.setSucess(false);
			   info.setMsg("附件分类编码重复，请重新输入");
		   } else {
			   // 名称唯一性验证
			   parameter.clear();
			   parameter.put("name", attachmentCategory.getName());
			   acList = attachmentCategoryService.queryList(parameter);
			   if (acList != null && acList.size() > 0) {
				   info.setSucess(false);
				   info.setMsg("附件分类名称重复，请重新输入");
			   } else {
				   attachmentCategoryService.save(attachmentCategory);
				   info.setResult(JacksonUtils.toJson(attachmentCategory));
				   info.setSucess(true);
				   info.setMsg("保存对象成功!");
			   }
			   
		   }
		   
		} catch (Exception e) {
		 log.error("保存对象失败!"+e.getMessage());
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
			   AttachmentCategory attachmentCategory=JacksonUtils.fromJson(updateJson, AttachmentCategory.class);
			   
			   // 编码唯一性验证
			   Map<String, Object> parameter = new HashMap<>();
			   
			   parameter.put("id", attachmentCategory.getId());
			   parameter.put("code", attachmentCategory.getCode());
			   int count = attachmentCategoryService.getCountOfCodeForUpdate(parameter);
			   if (count > 0) {
				   info.setSucess(false);
				   info.setMsg("附件分类编码重复，请重新输入");
			   } else {
				   
				   parameter.remove("code");
				   parameter.put("name", attachmentCategory.getName());
				   count = attachmentCategoryService.getCountOfCodeForUpdate(parameter);
				   if (count > 0) {
					   info.setSucess(false);
					   info.setMsg("附件分类名称重复，请重新输入");
				   } else {
					   int result= attachmentCategoryService.update(attachmentCategory);
					   info.setResult(JacksonUtils.toJson(result));
					   info.setSucess(true);
					   info.setMsg("更新对象成功!");
				   }
			   }
			   
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
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
			   AttachmentCategory attachmentCategory=JacksonUtils.fromJson(deleteJson, AttachmentCategory.class);
			   Map<String, Object> countParam = new HashMap<String, Object>();
			   countParam.put("categoryId", attachmentCategory.getId());
			   int acount = attachmentService.getCount(countParam);
			   if (acount > 0) {
				   info.setSucess(false);
				   info.setMsg("该分类下已存在附件，不能删除!");
			   } else {
				   int result= attachmentCategoryService.deleteObjectById(attachmentCategory.getId());
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除附件分类成功!");
			   }
			} catch (Exception e) {
			 log.error("删除附件分类失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除附件分类失败!");
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
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= attachmentCategoryService.deleteAllObjectByIds(list);
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除对象成功!");
				}
			} catch (Exception e) {
			 log.error("删除对象失败!"+e.getMessage());
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
			AttachmentCategory attachmentCategory=JacksonUtils.fromJson(getJson, AttachmentCategory.class);
			AttachmentCategory	result = attachmentCategoryService.getObjectById(attachmentCategory.getId());
			info.setResult(JacksonUtils.toJson(result));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取对象失败!"+e.getMessage());
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
				Page page=attachmentCategoryService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=attachmentCategoryService.getPage(new HashMap(), null, null);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取分页对象失败!"+e.getMessage());
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
				List list=attachmentCategoryService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=attachmentCategoryService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
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
	public String deletePseudoObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   AttachmentCategory attachmentCategory=JacksonUtils.fromJson(deleteJson, AttachmentCategory.class);
			   int result= attachmentCategoryService.deletePseudoObjectById(attachmentCategory.getId());
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deletePseudoAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= attachmentCategoryService.deletePseudoAllObjectByIds(list);
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除对象成功!");
				}
			} catch (Exception e) {
			 log.error("删除对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}


}
