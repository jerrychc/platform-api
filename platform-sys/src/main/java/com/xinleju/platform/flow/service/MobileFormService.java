package com.xinleju.platform.flow.service;

import java.util.List;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.dto.MobileFormDto;
import com.xinleju.platform.flow.entity.MobileForm;

/**
 * @author admin
 * 
 * 
 */

public interface MobileFormService extends  BaseService <String,MobileForm>{

	public List<MobileFormDto> queryMobileFormBy(String flCode, String businessId);
}
