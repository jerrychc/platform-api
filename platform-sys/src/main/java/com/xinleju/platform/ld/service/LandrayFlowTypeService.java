package com.xinleju.platform.ld.service;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.ld.entity.LandrayFlowType;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

public interface LandrayFlowTypeService extends  BaseService <String,LandrayFlowType>{


    List<LandrayFlowType> queryFLTypeTree(Map<String, Object> pMap);
}
