package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface SysBizItemDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String getSysBizItempage(String userJson, String paramaterJson);

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String getSysBizItemList(String userJson, String paramaterJson);

}
