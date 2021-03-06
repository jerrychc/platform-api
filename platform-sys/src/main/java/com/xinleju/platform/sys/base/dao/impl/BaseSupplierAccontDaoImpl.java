package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.BaseSupplierAccontDao;
import com.xinleju.platform.sys.base.dto.BaseSupplierAccontDto;
import com.xinleju.platform.sys.base.entity.BaseSupplierAccont;
import com.xinleju.platform.sys.res.dto.DataNodeDto;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class BaseSupplierAccontDaoImpl extends BaseDaoImpl<String,BaseSupplierAccont> implements BaseSupplierAccontDao{

	public BaseSupplierAccontDaoImpl() {
		super();
	}

	@Override
	public List<Map<String,Object>> getSupplierAccontMapBySupplierId(String paramater) {
		List<Map<String,Object>> list = getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseSupplierAccont.queryListBySupplierId", paramater);
		return list;
	}
	
	@Override
	public List<BaseSupplierAccont> getSupplierAccontBySupplierId(String paramater) {
		List<BaseSupplierAccont> list = getSqlSession().selectList("com.xinleju.platform.sys.base.entity.BaseSupplierAccont.queryAccontListBySupplierId", paramater);
		return list;
	}

	
	
}
