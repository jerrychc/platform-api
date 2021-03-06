package com.xinleju.platform.sys.org.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface RoleUserPostScopeDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * 批量保存roleUserPostScope
	 * @param parentId
	 * @return
	 */
	public String saveBatchRoleUserPostScope(String userInfo, String paramater);
	/**
	 * 查询管辖范围
	 * @param parentId
	 * @return
	 */
	public String queryScopeByRefId(String userInfo, String paramater);
}
