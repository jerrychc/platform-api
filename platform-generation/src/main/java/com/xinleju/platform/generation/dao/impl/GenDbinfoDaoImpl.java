package com.xinleju.platform.generation.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.generation.dao.GenDbinfoDao;
import com.xinleju.platform.generation.entity.GenerationDbinfo;

@Repository
public class GenDbinfoDaoImpl extends BaseDaoImpl<String,GenerationDbinfo> implements GenDbinfoDao{

	public GenDbinfoDaoImpl() {
		super();
	}

	
	
}
