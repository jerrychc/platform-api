package com.xinleju.platform.sys.base.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface SettlementTradesDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * @param userJson
	 * @param paramaterJson
	 * @return
	 */
	public String getSettlementTradesPage(String userJson, String paramaterJson);

	/**
	 * @param userJson
	 * @param string
	 * @return
	 */
	public String updateStatus(String userJson, String string);

}
