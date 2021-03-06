package com.xinleju.platform.finance.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.entity.AccountCaption;
import com.xinleju.platform.finance.entity.SysBizItem;

/**
 * @author admin
 * 
 * 
 */

public interface SysBizItemService extends  BaseService <String,SysBizItem>{

	/**
	 * @param map
	 * @return
	 */
	public Page getSysBizItempage(Map map) throws Exception;

	/**
	 * @param map
	 * @return
	 */
	public List<SysBizItem> getSysBizItemList(Map map) throws Exception;

	
}
