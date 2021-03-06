package com.xinleju.platform.flow.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.ApproveTypeDao;
import com.xinleju.platform.flow.entity.ApproveType;
import com.xinleju.platform.flow.entity.BusinessObject;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class ApproveTypeDaoImpl extends BaseDaoImpl<String,ApproveType> implements ApproveTypeDao{

	public ApproveTypeDaoImpl() {
		super();
	}

	@Override
	public int resetApproveTypeData() {
		return getSqlSession().update(ApproveType.class.getName()+".resetApproveTypeData");
	}

	@Override
	public int resetOperationTypeData() {
		return getSqlSession().update(ApproveType.class.getName()+".resetOperationTypeData");
	}

	@Override
	public int deleteAllMapDataByDelflag() {
		return getSqlSession().update(ApproveType.class.getName()+".deleteAllMapDataByDelflag");
		
	}

	
	
}
