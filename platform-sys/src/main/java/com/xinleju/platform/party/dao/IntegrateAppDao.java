package com.xinleju.platform.party.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.party.entity.IntegrateApp;
import com.xinleju.platform.sys.base.entity.SettlementTrades;

/**
 * @author admin
 *
 */

public interface IntegrateAppDao extends BaseDao<String, IntegrateApp> {

	/**
	 * @param map
	 * @return
	 */
	Integer getIntegrateAppCount(Map map);

	/**
	 * @param map
	 * @return
	 */
	List<IntegrateApp> getIntegrateAppPage(Map map);

	/**
	 * @param map
	 * @return
	 */
	Integer getIntegrateAppCountByName(Map<String, Object> map);
	
	

}