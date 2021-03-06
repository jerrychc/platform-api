package com.xinleju.platform.sys.org.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface RootDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * 获取目录和组织机构树对象
	 * 
	 * @param guuid-map
	 */
	public String getTree(String userInfo, String paramaterJson);
	
	/**
	 * 根据条件获取不同组织机构的tree
	 * type:all全部 company公司  dept部门 group项目 branch分期
	 * 
	 * @param guuid-map
	 */
	public String getOrgTreeByType(String userInfo, String paramaterJson);
}
