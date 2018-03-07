package com.xinleju.platform.sys.org.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;
import com.xinleju.platform.sys.org.dto.OrgnazationExcelDto;
import com.xinleju.platform.sys.org.dto.UserDto;

import java.util.List;

public interface UserDtoServiceCustomer extends BaseDtoServiceCustomer {
	/**
	 * 根据组织结构查询所有人
	 * 
	 * @param parentId
	 * @return
	 */
	public String queryUserListByOrgId(String userInfo, String paramater);
	
	/**
	 * 修改IM人员密码
	 * 
	 * @param parentId
	 * @return
	 */
	public String updateIMuser(String userInfo, String paramater);

	/**
	 * 根据岗位查询用户列表
	 * 
	 * @param paramater
	 * @return
	 */
	public String queryUserListByPostId(String userInfo, String paramater);
	
	/**
	 * 根据角色查询用户列表
	 * 
	 * @param paramater
	 * @return
	 */
	public String queryUserListByRoleId(String userInfo, String paramater);
	/**
	 * 根据虚拟角色查询用户列表
	 * @param paramater
	 * @return
	 */
	public String queryRoleUserByRoleId(String userInfo, String paramater);
	
	/**
	 * 获取用户树对象
	 * 
	 * @param guuid-map
	 */
	public String getUserTree(String userInfo, String paramater);
	/**
	 * 批量保存用户排序号
	 * @param guuid-map
	 */
	public String saveUsersSort(String userInfo, String paramater);
	/**
	 * 修改密码
	 * @param guuid-map
	 */
	public String updateMyPwd(String userInfo, String paramater);
	/**
	 * 获取我的详情
	 * @param guuid-map
	 */
	public String getMyInfo(String userInfo, String paramater);
	/**
	 * 根据用户Id，获取用户的公司，部门，项目，分期
	 * @param userInfo
	 * @param paramater
	 */
	public String getOrgInfoByUserId(String userInfo, String paramater);
	
	/**
	 * 根据用户Id返回用户信息（可以多个）
	 * @param userInfo
	 * @param paramater
	 */
	public String getUserInfoByUserIds(String userInfo, String paramater);

	/**
	 * 查询系统管理员列表
	 * @param userInfo
	 * @return
	 */
	String queryAdminList(String userInfo);
	/**
	 *根据搜索条件查询用户及其岗位
	 * @param userInfo
	 * @return
	 */
	public String queryUserAndPostsByUname(String userInfo, String paramater);
	/**
	 *重置密码
	 * @param userInfo
	 * @return
	 */
	public String resetPassword(String userInfo, String paramJson);
	/**
	 * 根据条件查询用户
	 * @param userInfo
	 * @return
	 */
	public String selectUserByQuery(String userInfo, String paramJson);
	
	/**
	 * 根据条件查询用户
	 * @param userInfo
	 * @return
	 */
	public String selectUserByQueryPage(String userInfo, String paramJson);
	/**
	 * 读excel并插入db
	 * @param userJson
	 * @param orgList
	 * @return
	 */
	public String readExcelAndInsert(String userJson, List<UserDto> orgList);
	/**
	 * 批量禁用或启用用户
	 * @param userJson
	 * @param paramJson
	 * @return
	 */
	public String lockUsersOrNot(String userJson,  String paramJson);
}