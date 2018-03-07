package com.xinleju.platform.ld.dto.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.ld.dto.service.LandraySysUserDtoServiceCustomer;
import com.xinleju.platform.ld.entity.LandraySysUser;
import com.xinleju.platform.ld.service.LandraySysUserService;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 *
 *
 */

public class LandraySysUserDtoServiceProducer implements LandraySysUserDtoServiceCustomer{
	private static Logger log = Logger.getLogger(LandraySysUserDtoServiceProducer.class);
	@Autowired
	private LandraySysUserService landraySysUserService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			LandraySysUser landraySysUser=JacksonUtils.fromJson(saveJson, LandraySysUser.class);
			landraySysUserService.save(landraySysUser);
			info.setResult(JacksonUtils.toJson(landraySysUser));
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
			LandraySysUser landraySysUser=JacksonUtils.fromJson(updateJson, LandraySysUser.class);
			int result=   landraySysUserService.update(landraySysUser);
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
			LandraySysUser landraySysUser=JacksonUtils.fromJson(deleteJson, LandraySysUser.class);
			int result= landraySysUserService.deleteObjectById(landraySysUser.getId());
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
				int result= landraySysUserService.deleteAllObjectByIds(list);
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
			LandraySysUser landraySysUser=JacksonUtils.fromJson(getJson, LandraySysUser.class);
			LandraySysUser	result = landraySysUserService.getObjectById(landraySysUser.getId());
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
				Page page=landraySysUserService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			}else{
				Page page=landraySysUserService.getPage(new HashMap(), null, null);
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
				List list=landraySysUserService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=landraySysUserService.queryList(null);
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
			LandraySysUser landraySysUser=JacksonUtils.fromJson(deleteJson, LandraySysUser.class);
			int result= landraySysUserService.deletePseudoObjectById(landraySysUser.getId());
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
				int result= landraySysUserService.deletePseudoAllObjectByIds(list);
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
