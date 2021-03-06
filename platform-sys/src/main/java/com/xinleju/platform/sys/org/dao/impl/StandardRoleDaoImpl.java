package com.xinleju.platform.sys.org.dao.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dao.StandardRoleDao;
import com.xinleju.platform.sys.org.dto.RoleNodeDto;
import com.xinleju.platform.sys.org.dto.StandardRoleDto;
import com.xinleju.platform.sys.org.entity.StandardRole;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class StandardRoleDaoImpl extends BaseDaoImpl<String,StandardRole> implements StandardRoleDao{

	public StandardRoleDaoImpl() {
		super();
	}

	/**
	 * 获取目录子节点标准角色
	 * @param catalogId
	 * @return
	 */
	@Override
	public List<RoleNodeDto> queryRoleListByCataId(String catalogId)  throws Exception{
		List<RoleNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.StandardRole.queryRoleListByCataId", catalogId);
		return list;
	}
	
	/**
	 * 根据组织机构获取角色
	 * @param catalogId
	 * @return
	 */
	@Override
	public List<RoleNodeDto> queryRoleListByOrgId(Map<String, Object> paramater)  throws Exception{
		String OrgId = "";
		if (paramater != null) {
			Set<String> set = paramater.keySet();
			for (String key : set) {
				OrgId = (String) paramater.get(key);
			}
		}
		List<RoleNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.StandardRole.queryRoleListByOrgId", OrgId);
		return list;
	}
	
	/**
	 * 根据用户Id查询角色
	 * @param catalogId
	 * @return
	 */
	@Override
	public List<StandardRoleDto> queryRoleListByUserId(Map<String, Object> paramater)  throws Exception{
		List<StandardRoleDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.StandardRole.queryRoleListByUserId", paramater);
		return list;
	}
	
	/**
	 * 根据用户Id查询通用角色
	 * @param catalogId
	 * @return
	 */
	@Override
	public List<StandardRoleDto> queryCurrencyRoleListByUserId(Map<String, Object> paramater)  throws Exception{
		List<StandardRoleDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.StandardRole.queryCurrencyRoleListByUserId", paramater);
		return list;
	}
	/**
	 * 根据岗位Id查询通用角色
	 * @param catalogId
	 * @return
	 */
	@Override
	public List<StandardRoleDto> queryCurrencyRoleListByPostId(Map<String, Object> paramater)  throws Exception{
		List<StandardRoleDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.StandardRole.queryCurrencyRoleListByPostId", paramater);
		return list;
	}
	/**
	 * 根据岗位Id查询标准岗位
	 * @param catalogId
	 * @return
	 */
	@Override
	public List<StandardRoleDto> queryStandardPostListByPostId(Map<String, Object> paramater)  throws Exception{
		List<StandardRoleDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.StandardRole.queryStandardPostListByPostId", paramater);
		return list;
	}
	
	@Override
	public List<Map<String,String>> queryRolesByIds(Map map)throws Exception{
		List<Map<String, String>> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.StandardRole.queryRolesByIds", map);
		return list;
	}
	
}
