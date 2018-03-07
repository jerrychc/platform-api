package com.xinleju.platform.ld.service.impl;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.ld.dao.LandrayFlowTypeDao;
import com.xinleju.platform.ld.entity.LandrayFlowType;
import com.xinleju.platform.ld.service.LandrayFlowTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

@Service
public class LandrayFlowTypeServiceImpl extends  BaseServiceImpl<String,LandrayFlowType> implements LandrayFlowTypeService {
	

	@Autowired
	private LandrayFlowTypeDao landrayFlowTypeDao;


	@Override
	public List<LandrayFlowType> queryFLTypeTree(Map<String, Object> pMap) {
		return landrayFlowTypeDao.queryFLTypeTree(pMap);
	}
}
