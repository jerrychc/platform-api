package com.xinleju.platform.univ.search.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.univ.search.dao.SearchCategoryDao;
import com.xinleju.platform.univ.search.entity.SearchCategory;

/**
 * @author haoqp
 * 
 * 
 */

@Repository
public class SearchCategoryDaoImpl extends BaseDaoImpl<String,SearchCategory> implements SearchCategoryDao{

	public SearchCategoryDaoImpl() {
		super();
	}

	
	
}
