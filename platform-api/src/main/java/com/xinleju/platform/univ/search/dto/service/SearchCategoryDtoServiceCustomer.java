package com.xinleju.platform.univ.search.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface SearchCategoryDtoServiceCustomer extends BaseDtoServiceCustomer{
	
	/**
	 * 
	 * 启用检索分类，建立全文检索映射
	 * 
	 * @param userInfo
	 * @param updateJson
	 * @return
	 */
	String updateStatus(String userInfo,String updateJson);

}
