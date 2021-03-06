package com.xinleju.platform.sys.org.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface RoleCatalogDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * 获取目录和组织机构树对象
	 * 
	 * @param guuid-map
	 */
	public String getRoleTree(String userInfo, String saveJson);
	
	
	/**
	 * 保存角色目录或者角色
	 * 
	 * @param guuid-map
	 */
	public String saveRoleOrCata(String userInfo, String saveJson);
}
