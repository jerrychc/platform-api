package com.xinleju.platform.finance.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.finance.entity.VoucherBill;

/**
 * @author admin
 *
 */

public interface VoucherBillDao extends BaseDao<String, VoucherBill> {
	
	/**
	 * @param map
	 * @return
	 */
	public List<VoucherBill> getVoucherBillPage(Map map);

	/**
	 * @param map
	 * @return
	 */
	public  Integer getVoucherBillListCount(Map map);

}
