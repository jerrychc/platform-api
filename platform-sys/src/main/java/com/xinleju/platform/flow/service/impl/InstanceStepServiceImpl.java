package com.xinleju.platform.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.InstanceStepDao;
import com.xinleju.platform.flow.entity.InstanceStep;
import com.xinleju.platform.flow.service.InstanceStepService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class InstanceStepServiceImpl extends  BaseServiceImpl<String,InstanceStep> implements InstanceStepService{
	

	@Autowired
	private InstanceStepDao instanceStepDao;
	

}
