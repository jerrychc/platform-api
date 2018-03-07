package com.xinleju.platform.finance.service;

import java.util.List;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.finance.entity.AssMapping;

/**
 * @author admin
 * 
 * 
 */

public interface AssMappingService extends  BaseService <String,AssMapping>{

	int saveAllAssMapp(String saveJson)throws Exception;

	public List queryListByAssTypeIds(List<String> paramList);
	
	/**
	 * 推送到NC 
	 * @param map
	 * @return
	 */
	public String createSyncXml2NC(String createJson,String sender);
}