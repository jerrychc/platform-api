package com.xinleju.platform.flow.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.MonitorPersonDao;
import com.xinleju.platform.flow.entity.MonitorPerson;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class MonitorPersonDaoImpl extends BaseDaoImpl<String,MonitorPerson> implements MonitorPersonDao{

	public MonitorPersonDaoImpl() {
		super();
	}

	
	
}
