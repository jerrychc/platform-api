package com.xinleju.platform.ld.dto.service.impl;

import java.util.*;

import com.xinleju.cloud.oa.content.dto.ContentChildTreeData;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.sys.org.dto.OrgnazationDto;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.ld.dto.service.LandrayFlowTypeDtoServiceCustomer;
import com.xinleju.platform.ld.entity.LandrayFlowType;
import com.xinleju.platform.ld.service.LandrayFlowTypeService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 *
 *
 */

public class LandrayFlowTypeDtoServiceProducer implements LandrayFlowTypeDtoServiceCustomer{
	private static Logger log = Logger.getLogger(LandrayFlowTypeDtoServiceProducer.class);
	@Autowired
	private LandrayFlowTypeService landrayFlowTypeService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			LandrayFlowType landrayFlowType=JacksonUtils.fromJson(saveJson, LandrayFlowType.class);
			landrayFlowTypeService.save(landrayFlowType);
			info.setResult(JacksonUtils.toJson(landrayFlowType));
			info.setSucess(true);
			info.setMsg("保存对象成功!");
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
			LandrayFlowType landrayFlowType=JacksonUtils.fromJson(updateJson, LandrayFlowType.class);
			int result=   landrayFlowTypeService.update(landrayFlowType);
			info.setResult(JacksonUtils.toJson(result));
			info.setSucess(true);
			info.setMsg("更新对象成功!");
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
			LandrayFlowType landrayFlowType=JacksonUtils.fromJson(deleteJson, LandrayFlowType.class);
			int result= landrayFlowTypeService.deleteObjectById(landrayFlowType.getId());
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
	public String deleteAllObjectByIds(String userInfo, String deleteJsonList)
	{
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if (StringUtils.isNotBlank(deleteJsonList)) {
				Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				List<String> list=Arrays.asList(map.get("id").toString().split(","));
				int result= landrayFlowTypeService.deleteAllObjectByIds(list);
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
			LandrayFlowType landrayFlowType=JacksonUtils.fromJson(getJson, LandrayFlowType.class);
			LandrayFlowType	result = landrayFlowTypeService.getObjectById(landrayFlowType.getId());
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
				Page page=landrayFlowTypeService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			}else{
				Page page=landrayFlowTypeService.getPage(new HashMap(), null, null);
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
				List list=landrayFlowTypeService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=landrayFlowTypeService.queryList(null);
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
			LandrayFlowType landrayFlowType=JacksonUtils.fromJson(deleteJson, LandrayFlowType.class);
			int result= landrayFlowTypeService.deletePseudoObjectById(landrayFlowType.getId());
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
				int result= landrayFlowTypeService.deletePseudoAllObjectByIds(list);
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
	public String getFlTypeTreeById(String userInfo, String pMap) {
		//实例返回结果集对象
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try{
			Map map=JacksonUtils.fromJson(pMap, HashMap.class);
			List<LandrayFlowType> landrayFlowTypeList = landrayFlowTypeService.queryFLTypeTree(map);
			List<ContentChildTreeData> officeHouseResultList = new ArrayList();
			if(landrayFlowTypeList.size() > 0){
				for (LandrayFlowType landrayFlowType : landrayFlowTypeList) {
					ContentChildTreeData bean = new ContentChildTreeData();
					bean.setId(landrayFlowType.getId());
					bean.setName(landrayFlowType.getName());
					bean.setpId(landrayFlowType.getParentId());
					bean.setParentId(landrayFlowType.getParentId());
					bean.setContentTypeId(landrayFlowType.getType());
					officeHouseResultList.add(bean);
				}
			}
			info.setResult(JacksonUtils.toJson(officeHouseResultList));
			info.setSucess(true);
			info.setMsg("获取知识目录结构树成功!");
		}catch(Exception e){
			log.error("获取知识目录结构树失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取知识目录结构树失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
}
