package com.xinleju.platform.sys.org.dto.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.org.dto.service.RoleUserPostScopeDtoServiceCustomer;
import com.xinleju.platform.sys.org.entity.RoleUserPostScope;
import com.xinleju.platform.sys.org.service.RoleUserPostScopeService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class RoleUserPostScopeDtoServiceProducer implements RoleUserPostScopeDtoServiceCustomer{
	private static Logger log = Logger.getLogger(RoleUserPostScopeDtoServiceProducer.class);
	@Autowired
	private RoleUserPostScopeService roleUserPostScopeService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   RoleUserPostScope roleUserPostScope=JacksonUtils.fromJson(saveJson, RoleUserPostScope.class);
		   roleUserPostScopeService.save(roleUserPostScope);
		   info.setResult(JacksonUtils.toJson(roleUserPostScope));
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
			   RoleUserPostScope roleUserPostScope=JacksonUtils.fromJson(updateJson, RoleUserPostScope.class);
			   int result=   roleUserPostScopeService.update(roleUserPostScope);
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
			   RoleUserPostScope roleUserPostScope=JacksonUtils.fromJson(deleteJson, RoleUserPostScope.class);
			   int result= roleUserPostScopeService.deleteObjectById(roleUserPostScope.getId());
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
				   int result= roleUserPostScopeService.deleteAllObjectByIds(list);
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
			RoleUserPostScope roleUserPostScope=JacksonUtils.fromJson(getJson, RoleUserPostScope.class);
			RoleUserPostScope	result = roleUserPostScopeService.getObjectById(roleUserPostScope.getId());
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
				Page page=roleUserPostScopeService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=roleUserPostScopeService.getPage(new HashMap(), null, null);
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
				List list=roleUserPostScopeService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=roleUserPostScopeService.queryList(null);
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
			   RoleUserPostScope roleUserPostScope=JacksonUtils.fromJson(deleteJson, RoleUserPostScope.class);
			   int result= roleUserPostScopeService.deletePseudoObjectById(roleUserPostScope.getId());
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
				   int result= roleUserPostScopeService.deletePseudoAllObjectByIds(list);
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
	//批量保存roleUserPostScope
	public String saveBatchRoleUserPostScope(String userInfo, String saveJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String, Object> param=JacksonUtils.fromJson(saveJson, HashMap.class);
			if(!param.containsKey("roleUserId")||param.get("roleUserId")==null||StringUtils.isBlank(param.get("roleUserId").toString())){
				throw new InvalidCustomException("roleUserId不可为空");
			}
			List<Map<String, Object>> list=(List<Map<String, Object>>)param.get("list");
			if (list==null ||list.size()<=0) {
				throw new InvalidCustomException("请选择管辖范围");
			}
			roleUserPostScopeService.saveBatchRoleUserPostScope(param);
			info.setResult(JacksonUtils.toJson(list));
			info.setSucess(true);
			info.setMsg("保存对象成功!");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("保存对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("保存对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	/**
	 * 查询管辖范围列表
	 * @return
	 */
	@Override
	public String queryScopeByRefId(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map map=JacksonUtils.fromJson(paramater, HashMap.class);
			List<Map<String,Object>> list=roleUserPostScopeService.queryScopeByRefId(map);
			info.setResult(JacksonUtils.toJson(list));
			info.setSucess(true);
			info.setMsg("获取列表对象成功!");
		} catch (Exception e) {
			log.error("获取列表对象失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}


}
