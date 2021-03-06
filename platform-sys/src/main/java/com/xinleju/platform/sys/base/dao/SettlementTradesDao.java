package com.xinleju.platform.sys.base.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.base.entity.SettlementTrades;

/**
 * @author admin
 *
 */

public interface SettlementTradesDao extends BaseDao<String, SettlementTrades> {

	/**
	 * @param map
	 * @return
	 */
	List<SettlementTrades> getSettlementTradesPage(Map<String, Object> map);

	/**
	 * @param map
	 * @return
	 */
	Integer getSettlementTradesPageCount(Map<String, Object> map);


	/**
	 * @param map
	 * @return
	 */
	Integer getRepeatCountName(Map<String, Object> map);

	/**
	 * @param map
	 * @return
	 */
	Integer getRepeatCountCode(Map<String, Object> map);

	
	

}
