package com.xinleju.platform.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.MobileProcessInitiatingRecordDao;
import com.xinleju.platform.flow.entity.MobileProcessInitiatingRecord;
import com.xinleju.platform.flow.service.MobileProcessInitiatingRecordService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class MobileProcessInitiatingRecordServiceImpl extends  BaseServiceImpl<String,MobileProcessInitiatingRecord> implements MobileProcessInitiatingRecordService{
	

	@Autowired
	private MobileProcessInitiatingRecordDao mobileProcessInitiatingRecordDao;
	

}
