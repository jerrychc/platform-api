package com.xinleju.platform.out.app.org.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.sys.org.dto.StandardRoleDto;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.org.entity.StandardRole;
import com.xinleju.platform.sys.org.service.StandardRoleService;
import com.xinleju.platform.sys.org.service.UserService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;
import com.xinleju.platform.tools.data.JacksonUtils;

public class UserOutServiceProducer implements UserOutServiceCustomer {
	private static Logger log = Logger.getLogger(UserOutServiceProducer.class);

	@Autowired
	private UserService userService;
	@Autowired
	private StandardRoleService standardRoleService;
	/**
	 * 根据人员Ids获取userDto
	 * @param userJson
	 * @param paramJson:{userIds}
	 * @return
	 */
	@Override
	public String getUserByUserIds(String userJson, String paramJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			String userIds = (String)param.get("userIds");
			if(null == userIds || userIds.equals("")){
				throw new InvalidCustomException("参数人员Id为空!");
			}else{
				List<UserDto> list=userService.getUserByUserIds(param);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取用户成功!");
			}
			
		} catch (Exception e) {
			 log.error("获取用户失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取用户失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 根据人员姓名获取userDto
	 * @param userJson
	 * @param paramJson:{userIds}
	 * @return
	 */
	@Override
	public String getUserByUserName(String userJson, String paramJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			String userIds = (String)param.get("userName");
			if(null == userIds || userIds.equals("")){
				throw new InvalidCustomException("参数人员Id为空!");
			}else{
				List<UserDto> list=userService.getUserByUserName(param);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取用户成功!");
			}
			
		} catch (Exception e) {
			 log.error("获取用户失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取用户失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 根据人员loginNames获取userDto
	 * @param userJson
	 * @param paramJson:{loginNames}
	 * @return
	 */
	@Override
	public String getUserByUserLoginNames(String userJson, String paramJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			String loginNames = (String)param.get("loginNames");
			if(null == loginNames || loginNames.equals("")){
				throw new InvalidCustomException("参数人员登录账号为空!");
			}else{
				List<UserDto> list=userService.getUserByUserLoginNames(param);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取用户成功!");
			}
			
		} catch (Exception e) {
			 log.error("获取用户失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取用户失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	/**
	 * 获取指定组织下的人员
	 * @param userJson
	 * @param paramJson:{orgIds:"指定组织Ids",isLeaf:true(如果true,返回指定组织包括下级，如果是false，返回指定组织下)}
	 * @return
	 */
	@Override
	public String getUserListByOrgId(String userJson, String paramJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			if(!param.containsKey("orgIds")|| param.get("orgIds")==null || StringUtils.isBlank(param.get("orgIds").toString())){
				throw new InvalidCustomException("参数组织机构为空");
			}
			Map<String, Object> res=userService.getUserListByOrgId(param);
			info.setResult(JacksonUtils.toJson(res));
			info.setSucess(true);
			info.setMsg("获取指定组织下的人员成功!");
		} catch (Exception e) {
			log.error("获取指定组织下的人员失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取指定组织下的人员失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 根据岗位Id获取userDto
	 * @param userJson
	 * @param paramJson:{postIds}
	 * @return
	 */
	@Override
	public String getUserListByPostIds(String userJson, String paramJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			if(!param.containsKey("postIds")|| param.get("postIds")==null || StringUtils.isBlank(param.get("postIds").toString())){
				throw new InvalidCustomException("参数岗位为空");
			}
			Map<String, Object> res=userService.getUserListByPostIds(param);
			info.setResult(JacksonUtils.toJson(res));
			info.setSucess(true);
			info.setMsg("获取岗位下的人员成功!");
		} catch (Exception e) {
			log.error("获取岗位下的人员失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取岗位下的人员失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	/**
	 * 根据标准岗位Id和组织机构id（项目、公司、集团）获取userDto
	 * @param userJson
	 * @param paramJson:{postIds}
	 * @return
	 */
	@Override
	public String getUserListByStandardpostIdAndOrgIds(String userJson, String paramJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			if(!param.containsKey("standardpostId")|| param.get("standardpostId")==null || StringUtils.isBlank(param.get("standardpostId").toString())){
				throw new InvalidCustomException("参数标准岗位为空");
			}
			Map<String, Object> res = userService.getUserListByStandardpostIdAndOrgIds(param);
			info.setResult(JacksonUtils.toJson(res));
			info.setSucess(true);
			info.setMsg("获取岗位下的人员成功!");
		} catch (Exception e) {
			log.error("获取岗位下的人员失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取岗位下的人员失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	
	/**
	 * 根据id获取标准角色
	 * @param userJson
	 * @param paramJson:{}
	 * @return
	 */
	@Override
	public String getAllStandRoleList(String userJson, String paramJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			param.put("delflag", false);
			List<StandardRole> res=standardRoleService.queryList(param);
			info.setResult(JacksonUtils.toJson(res));
			info.setSucess(true);
			info.setMsg("获取所有标准角色成功!");
		} catch (Exception e) {
			log.error("获取所有标准角色失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取所有标准角色失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	/**
	 * 根据id获取标准角色
	 * @param userJson
	 * @param paramJson:{roleIds:"角色id"}
	 * @return
	 */
	@Override
	public String getStandRoleListByIds(String userJson, String paramJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			if(!param.containsKey("roleIds")|| param.get("roleIds")==null || StringUtils.isBlank(param.get("roleIds").toString())){
				throw new InvalidCustomException("参数角色为空");
			}
			param.put("ids", param.get("roleIds").toString().split(","));
			List<Map<String,String>> res=standardRoleService.queryRolesByIds(param);
			info.setResult(JacksonUtils.toJson(res));
			info.setSucess(true);
			info.setMsg("获取标准角色成功!");
		} catch (Exception e) {
			log.error("获取标准角色失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("获取标准角色失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
	/**
	 * 根据角色id获取用户
	 * @param userJson
	 * @param paramJson:{roleIds:"角色id"}
	 * @return
	 */
	@Override
	public String getUserListByRoleIds(String userJson, String paramJson){
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String,Object> param=JacksonUtils.fromJson(paramJson,HashMap.class);
			if(!param.containsKey("roleIds")|| param.get("roleIds")==null || StringUtils.isBlank(param.get("roleIds").toString())){
				throw new InvalidCustomException("参数角色为空");
			}
			Map<String, Object> res=userService.getUserListByRoleIds(param);
			info.setResult(JacksonUtils.toJson(res));
			info.setSucess(true);
			info.setMsg("根据角色id获取用户成功!");
		} catch (Exception e) {
			log.error("根据角色id获取用户失败!"+e.getMessage());
			info.setSucess(false);
			info.setMsg("根据角色id获取用户失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}
	
}
