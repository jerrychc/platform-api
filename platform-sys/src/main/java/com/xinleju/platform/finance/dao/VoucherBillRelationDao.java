package com.xinleju.platform.finance.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.entity.VoucherBillRelation;

/**
 * @author admin
 *
 */

public interface VoucherBillRelationDao extends BaseDao<String, VoucherBillRelation> {
	
	/**
	 * @param map
	 * @return
	 */
	public List<VoucherBillRelation> getpageList(Map map);

	/**
	 * @param map
	 * @return
	 */
	public  Integer getpageListCount(Map map);

}
