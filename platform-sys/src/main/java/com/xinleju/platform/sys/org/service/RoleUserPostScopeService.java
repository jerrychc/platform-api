package com.xinleju.platform.sys.org.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.org.entity.RoleUserPostScope;

/**
 * @author admin
 * 
 * 
 */

public interface RoleUserPostScopeService extends  BaseService <String,RoleUserPostScope>{

	/**
	 *批量保存roleuserPostScope
	 * @return
	 */
	public Integer saveBatchRoleUserPostScope(Map<String, Object> paramater)  throws Exception;
	/**
	 * 查询管辖范围列表
	 * @return
	 */
	public List<Map<String,Object>> queryScopeByRefId(Map<String, Object> paramater)  throws Exception;
}
