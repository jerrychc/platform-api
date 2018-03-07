package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.CommonDraftDao;
import com.xinleju.platform.sys.base.entity.CommonDraft;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class CommonDraftDaoImpl extends BaseDaoImpl<String,CommonDraft> implements CommonDraftDao{

	public CommonDraftDaoImpl() {
		super();
	}

	@Override
	public List<Map<String, Object>> getMyFormPageSort(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.CommonDraft.getMyFormPageSort", map);
	}

	@Override
	public Integer getMyFormPageCount(Map map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.CommonDraft.getMyFormPageCount", map);
	}

	
	
}
