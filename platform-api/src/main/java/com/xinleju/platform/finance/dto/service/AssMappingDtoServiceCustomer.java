package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;
import com.xinleju.platform.finance.dto.AssMappingDto;

public interface AssMappingDtoServiceCustomer extends BaseDtoServiceCustomer{

	String saveAllAssMapp(String userJson, String saveJson);
	
	/**
	 * 推送到NC 
	 * @param map
	 * @return
	 */
	public String createSyncXml2NC(String createJson,String sender);
	
	public String queryListByAssTypeIds(String userJson, String paramater);
}
