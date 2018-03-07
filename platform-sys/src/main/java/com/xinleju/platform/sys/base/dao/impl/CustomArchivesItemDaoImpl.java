package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.CustomArchivesItemDao;
import com.xinleju.platform.sys.base.entity.CustomArchivesItem;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class CustomArchivesItemDaoImpl extends BaseDaoImpl<String,CustomArchivesItem> implements CustomArchivesItemDao{

	public CustomArchivesItemDaoImpl() {
		super();
	}

	@Override
	public Integer getCurrentMaxSortByMainId(String mainId) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomArchivesItem.getMaxSortByMainId",mainId);
	}

	@Override
	public List queryListSort(Map<String, Object> map) {
		return getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomArchivesItem.queryListSort",map);
	}

	@Override
	public Integer validateIsExist(CustomArchivesItem customArchivesItem, String type) {
		if("code".equals(type)){
			return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomArchivesItem.validateIsExistCode",customArchivesItem);
		}else if("name".equals(type)){
			return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomArchivesItem.validateIsExistName",customArchivesItem);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getPageSort(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CustomArchivesItem.getPageSort", map);
	}

	@Override
	public Integer getPageSortCount(Map map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CustomArchivesItem.getPageSortCount", map);
	}

	@Override
	public int updateCustomArchives(String id) {
		return getSqlSession().update("com.xinleju.platform.sys.base.entity.CustomArchivesItem.updateDelFlag", id);
	}
	
}