package com.xinleju.platform.sys.org.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dao.RoleUserPostScopeDao;
import com.xinleju.platform.sys.org.entity.RoleUserPostScope;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class RoleUserPostScopeDaoImpl extends BaseDaoImpl<String,RoleUserPostScope> implements RoleUserPostScopeDao{

	public RoleUserPostScopeDaoImpl() {
		super();
	}
	/**
	 *批量保存roleuserPostScope
	 * @return
	 */
	public Integer deleteByRefId(Map<String, Object> paramater)  throws Exception{
		return getSqlSession().delete("com.xinleju.platform.sys.org.entity.RoleUserPostScope.deleteByRefId", paramater);
	}
	/**
	 * 批量保存引用
	 * @return
	 */
	public Integer insertRoleUserPostScopeBatch(Map<String, Object> paramater)  throws Exception{
		return getSqlSession().insert("com.xinleju.platform.sys.org.entity.RoleUserPostScope.insertRoleUserPostScopeBatch", paramater);
	}
	/**
	 * 查询管辖范围列表
	 * @return
	 */
	public List<Map<String, Object>> queryScopeByRefId(Map<String, Object> paramater) throws Exception {
		return getSqlSession().selectList("com.xinleju.platform.sys.org.entity.RoleUserPostScope.queryScopeByRefId", paramater);
	}
	
	
	
}
