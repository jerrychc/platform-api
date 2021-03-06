package com.xinleju.platform.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.MonitorPersonDao;
import com.xinleju.platform.flow.entity.MonitorPerson;
import com.xinleju.platform.flow.service.MonitorPersonService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class MonitorPersonServiceImpl extends  BaseServiceImpl<String,MonitorPerson> implements MonitorPersonService{
	

	@Autowired
	private MonitorPersonDao monitorPersonDao;
	

}
