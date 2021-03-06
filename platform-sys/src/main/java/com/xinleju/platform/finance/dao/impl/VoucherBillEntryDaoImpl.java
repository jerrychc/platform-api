package com.xinleju.platform.finance.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.finance.dao.VoucherBillEntryDao;
import com.xinleju.platform.finance.entity.VoucherBillEntry;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class VoucherBillEntryDaoImpl extends BaseDaoImpl<String,VoucherBillEntry> implements VoucherBillEntryDao{

	public VoucherBillEntryDaoImpl() {
		super();
	}

	/**
	 * @param map
	 * @return
	 */
	@Override
	public List<VoucherBillEntry> getpageList(Map map){
		return getSqlSession().selectList("com.xinleju.platform.finance.entity.VoucherBillEntry.getpageList", map);
	}

	/**
	 * @param map
	 * @return
	 */
	@Override
	public  Integer getpageListCount(Map map){
		return getSqlSession().selectOne("com.xinleju.platform.finance.entity.VoucherBillEntry.getpageListCount", map);
	}
	
}
