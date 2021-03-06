package com.xinleju.platform.finance.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.entity.SysBizItem;
import com.xinleju.platform.finance.entity.SysRegister;

/**
 * @author admin
 *
 */

public interface SysBizItemDao extends BaseDao<String, SysBizItem> {

	/**
	 * @param map
	 * @return
	 */
	List<SysBizItem> getSysBizItempageList(Map map);

	/**
	 * @param map
	 * @return
	 */
	Integer getSysBizItempageListCount(Map map);
	
	

}
