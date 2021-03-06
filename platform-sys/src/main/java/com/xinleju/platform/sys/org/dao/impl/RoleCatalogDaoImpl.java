package com.xinleju.platform.sys.org.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dao.RoleCatalogDao;
import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.org.dto.RoleNodeDto;
import com.xinleju.platform.sys.org.entity.RoleCatalog;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class RoleCatalogDaoImpl extends BaseDaoImpl<String,RoleCatalog> implements RoleCatalogDao{

	public RoleCatalogDaoImpl() {
		super();
	}

	/**
	 * 获取目录子节点目录
	 * @param parentId
	 * @return
	 */
	@Override
	public List<RoleNodeDto> queryRoleCatalogList(String parentId)  throws Exception{
		List<RoleNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.RoleCatalog.queryRoleCatalogList", parentId);
		return list;
	}
	/**
	 * 获取根目录
	 * @param map
	 * @return
	 */
	@Override
	public List<RoleNodeDto> queryRoleCatalogRoot(Map<String,Object> map) throws Exception {
		List<RoleNodeDto> list = getSqlSession().selectList("com.xinleju.platform.sys.org.entity.RoleCatalog.queryRoleCatalogRoot", map);
		return list;
	}
	
	/**
	 * 维护相关表全路径
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer updateCataAndRoleAllPreFix(Map<String, Object> map) throws Exception{
		return getSqlSession().update("com.xinleju.platform.sys.org.entity.RoleCatalog.updateCataAndRoleAllPreFix", map);
	}
	/**
	 * 禁用角色
	 * @param paramater
	 * @return
	 */
	public Integer lockRole(Map map)throws Exception{
		return getSqlSession().update("com.xinleju.platform.sys.org.entity.RoleCatalog.lockRole", map);
	}
	
	/**
	 * 启用角色
	 * @param paramater
	 * @return
	 */
	public Integer unLockRole(Map map)throws Exception{
		return getSqlSession().update("com.xinleju.platform.sys.org.entity.RoleCatalog.unLockRole", map);
	}
	/**
	 * 查询所有下级id 
	 * @param paramater
	 * @return
	 */
	@Override
	public List<String> selectSunById(Map map)throws Exception{
		return getSqlSession().selectList("com.xinleju.platform.sys.org.entity.RoleCatalog.selectSunById", map);
	}
	
	/**
	 * 校验名字是否重复
	 * @param paramater
	 * @return
	 */
	public Integer checkName(Map map)throws Exception{
		String type = (String)map.get("type");
		if(type.equals("cata")){
			return getSqlSession().selectOne("com.xinleju.platform.sys.org.entity.RoleCatalog.checkCataName", map);
		}else{
			return getSqlSession().selectOne("com.xinleju.platform.sys.org.entity.RoleCatalog.checkRoleName", map);
		}
		
	}
}
