package com.xinleju.platform.sys.org.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface PostDtoServiceCustomer extends BaseDtoServiceCustomer {
	/**
	 * 根据用户查询用户所有岗位
	 * 
	 * @param userId
	 * @return
	 */
	public String queryPostListByUserId(String userInfo, String paramater);
	
	/**
	 * 根据用户查询岗位
	 * @param userInfo
	 * @param paramater
	 * @return
	 */
	public String queryPostRoleListByUserId(String userInfo, String paramater);

	/**
	 * 根据组织结构查询岗位列表
	 * 
	 * @param map
	 * @return
	 */
	public String queryPostListByOrgId(String userInfo, String paramater);
	
	/**
	 * 根据角色查询岗位列表
	 * 
	 * @param map
	 * @return
	 */
	public String queryPostListByRoleId(String userInfo, String paramater);
	
	/**
	 * 获取岗位树对象
	 * 
	 * @param guuid-map
	 */
	public String getPostTree(String userInfo, String paramater);
	/**
	 * 获取流程岗位人员列表
	 * 
	 * @param guuid-map
	 */
	public String getFlowPostData(String userInfo, String paramater);

	/**
	 * 批量设置领导岗位
	 */
	public String updateBatchLeaderId(String userInfo, String paramater);
}
