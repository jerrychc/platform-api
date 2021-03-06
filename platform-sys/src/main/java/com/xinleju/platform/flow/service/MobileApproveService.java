package com.xinleju.platform.flow.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.dto.MobileApproveDto;
import com.xinleju.platform.flow.entity.Instance;

/**
 * @author admin
 * 
 * 
 */

public interface MobileApproveService extends  BaseService <String,Instance>{

	public MobileApproveDto queryMobileApproveByParamMap(Map<String, Object> paramMap) throws Exception;
}
