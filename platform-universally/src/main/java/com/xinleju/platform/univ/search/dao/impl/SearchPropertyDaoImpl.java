package com.xinleju.platform.univ.search.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.univ.search.dao.SearchPropertyDao;
import com.xinleju.platform.univ.search.entity.SearchProperty;

/**
 * @author haoqp
 * 
 * 
 */

@Repository
public class SearchPropertyDaoImpl extends BaseDaoImpl<String,SearchProperty> implements SearchPropertyDao{

	public SearchPropertyDaoImpl() {
		super();
	}

	
	
}
