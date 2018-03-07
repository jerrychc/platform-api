package com.xinleju.platform.flow.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.InstanceVariableDao;
import com.xinleju.platform.flow.entity.InstanceVariable;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class InstanceVariableDaoImpl extends BaseDaoImpl<String,InstanceVariable> implements InstanceVariableDao{

	public InstanceVariableDaoImpl() {
		super();
	}

	@Override
	public List<InstanceVariable> queryVariableByFlId(Map<String, Object> paramMap) {
		return getSqlSession().selectList(InstanceVariable.class.getName()+".queryVariableByFlId",paramMap);
	}
}
