package com.xinleju.platform.flow.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.MobileFormDao;
import com.xinleju.platform.flow.dto.MobileFormDto;
import com.xinleju.platform.flow.entity.MobileForm;
import com.xinleju.platform.flow.service.MobileFormService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class MobileFormServiceImpl extends  BaseServiceImpl<String,MobileForm> implements MobileFormService{

	@Autowired
	private MobileFormDao mobileFormDao;

	@Override
	public List<MobileFormDto> queryMobileFormBy(String flCode, String businessId) {
		return mobileFormDao.queryMobileFormBy(flCode, businessId);
	}
	
	
}
