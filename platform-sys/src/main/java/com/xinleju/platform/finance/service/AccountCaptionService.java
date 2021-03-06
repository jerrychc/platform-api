package com.xinleju.platform.finance.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.finance.dto.AccountCaptionDto;
import com.xinleju.platform.finance.entity.AccountCaption;

/**
 * @author admin
 * 
 * 
 */

public interface AccountCaptionService extends  BaseService <String,AccountCaption>{

	/**
	 * @return
	 */
	List<AccountCaptionDto> getAccountCaptionTree(Map map) throws Exception;

	/**
	 * @param object
	 * @return
	 */
	List queryCaptionList(Map map)throws Exception;

	/**
	 * @param id
	 * @return
	 */
	int deleteAccountCaptionById(String id)throws Exception;

	/**
	 * @param accountCaption
	 */
	int saveAccountCaption(AccountCaption accountCaption)throws Exception;

	/**
	 * @param accountCaption
	 * @return
	 */
	int updateAccountCaption(AccountCaption accountCaption)throws Exception;

	List queryByBudCodeList(Map map)throws Exception;
}
