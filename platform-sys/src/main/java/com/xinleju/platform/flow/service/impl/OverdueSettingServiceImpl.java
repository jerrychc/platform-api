package com.xinleju.platform.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.OverdueSettingDao;
import com.xinleju.platform.flow.entity.OverdueSetting;
import com.xinleju.platform.flow.service.OverdueSettingService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class OverdueSettingServiceImpl extends  BaseServiceImpl<String,OverdueSetting> implements OverdueSettingService{
	

	@Autowired
	private OverdueSettingDao overdueSettingDao;
	

}
