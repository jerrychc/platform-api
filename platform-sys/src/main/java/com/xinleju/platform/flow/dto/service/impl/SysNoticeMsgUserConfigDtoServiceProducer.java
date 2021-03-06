package com.xinleju.platform.flow.dto.service.impl;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dto.service.SysNoticeMsgUserConfigDtoServiceCustomer;
import com.xinleju.platform.flow.entity.SysNoticeMsgUserConfig;
import com.xinleju.platform.flow.entity.SysNoticeMsgUserConfig;
import com.xinleju.platform.flow.service.SysNoticeMsgUserConfigService;
import com.xinleju.platform.tools.data.JacksonUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;


/**
 * @author admin
 * 
 *
 */
 
public class SysNoticeMsgUserConfigDtoServiceProducer implements SysNoticeMsgUserConfigDtoServiceCustomer {

	private static Logger log = Logger.getLogger(SysNoticeMsgUserConfigDtoServiceProducer.class);
    @Autowired
	private SysNoticeMsgUserConfigService sysNoticeMsgUserConfigService;
	public String save(String userInfo, String saveJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			SysNoticeMsgUserConfig sysNoticeMsgUserConfig= JacksonUtils.fromJson(saveJson, SysNoticeMsgUserConfig.class);
			sysNoticeMsgUserConfig.setDelflag(false);
			sysNoticeMsgUserConfigService.save(sysNoticeMsgUserConfig);
			info.setResult(JacksonUtils.toJson(sysNoticeMsgUserConfig));
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
			SysNoticeMsgUserConfig sysNoticeMsgUserConfig=JacksonUtils.fromJson(updateJson, SysNoticeMsgUserConfig.class);
			int result=   sysNoticeMsgUserConfigService.update(sysNoticeMsgUserConfig);
			info.setResult(JacksonUtils.toJson(sysNoticeMsgUserConfig));
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
			SysNoticeMsgUserConfig sysNoticeMsgUserConfig=JacksonUtils.fromJson(deleteJson, SysNoticeMsgUserConfig.class);
			int result= sysNoticeMsgUserConfigService.deleteObjectById(sysNoticeMsgUserConfig.getId());
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
				List<String> list= Arrays.asList(map.get("id").toString().split(","));
				int result= sysNoticeMsgUserConfigService.deleteAllObjectByIds(list);
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
			SysNoticeMsgUserConfig sysNoticeMsgUserConfig=JacksonUtils.fromJson(getJson, SysNoticeMsgUserConfig.class);
			if(Objects.equals (sysNoticeMsgUserConfig.getId (),"setting")){
				List<SysNoticeMsgUserConfig> list = sysNoticeMsgUserConfigService.queryList (null);
				if(list!=null&&!list.isEmpty ()){
					info.setResult(JacksonUtils.toJson(list.get (0)));
					info.setSucess(true);
					info.setMsg("获取对象成功!");
				}else{
					info.setResult(null);
					info.setSucess(false);
					info.setMsg("获取对象失败!");
				}
			}else {
				SysNoticeMsgUserConfig result = sysNoticeMsgUserConfigService.getObjectById (sysNoticeMsgUserConfig.getId ());
				info.setResult(JacksonUtils.toJson(result));
				info.setSucess(true);
				info.setMsg("获取对象成功!");
			}

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
				Page page=sysNoticeMsgUserConfigService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
				info.setSucess(true);
				info.setMsg("获取分页对象成功!");
			}else{
				Page page=sysNoticeMsgUserConfigService.getPage(new HashMap(), null, null);
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
				List list=sysNoticeMsgUserConfigService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}else{
				List list=sysNoticeMsgUserConfigService.queryList(null);
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
			SysNoticeMsgUserConfig sysNoticeMsgUserConfig=JacksonUtils.fromJson(deleteJson, SysNoticeMsgUserConfig.class);
			int result= sysNoticeMsgUserConfigService.deletePseudoObjectById(sysNoticeMsgUserConfig.getId());
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
				int result= sysNoticeMsgUserConfigService.deletePseudoAllObjectByIds(list);
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
