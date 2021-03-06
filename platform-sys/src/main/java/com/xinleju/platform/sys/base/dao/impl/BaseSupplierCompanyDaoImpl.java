package com.xinleju.platform.sys.base.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.BaseSupplierCompanyDao;
import com.xinleju.platform.sys.base.entity.BaseSupplierCompany;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class BaseSupplierCompanyDaoImpl extends BaseDaoImpl<String,BaseSupplierCompany> implements BaseSupplierCompanyDao{

	public BaseSupplierCompanyDaoImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.BaseSupplierCompanyDao#getObjectBySupplierId(java.lang.String)
	 */
	@Override
	public List<String> getObjectBySupplierId(String id) {
		Map<String,Object>map=new HashMap<String,Object>();
		map.put("supplierId",id);
	  return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseSupplierCompany.getObjectBySupplierId", map);
	}
	@Override
	public List<String> getIdsBySupplierId(String id) {
		Map<String,Object>map=new HashMap<String,Object>();
		map.put("supplierId",id);
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseSupplierCompany.getIdsBySupplierId", map);
	}

	
	
}
