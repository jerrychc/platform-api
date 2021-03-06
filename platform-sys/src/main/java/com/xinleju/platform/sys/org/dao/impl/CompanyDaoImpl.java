package com.xinleju.platform.sys.org.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.org.dao.CompanyDao;
import com.xinleju.platform.sys.org.entity.Company;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class CompanyDaoImpl extends BaseDaoImpl<String,Company> implements CompanyDao{

	public CompanyDaoImpl() {
		super();
	}
	
}
