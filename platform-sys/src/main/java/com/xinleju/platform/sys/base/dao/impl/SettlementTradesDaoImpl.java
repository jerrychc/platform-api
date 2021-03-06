package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.SettlementTradesDao;
import com.xinleju.platform.sys.base.entity.SettlementTrades;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class SettlementTradesDaoImpl extends BaseDaoImpl<String,SettlementTrades> implements SettlementTradesDao{

	public SettlementTradesDaoImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.SettlementTradesDao#getSettlementTradesPage(java.util.Map)
	 */
	@Override
	public List<SettlementTrades> getSettlementTradesPage(Map<String, Object> map) {
		return    getSqlSession().selectList("com.xinleju.platform.sys.base.entity.SettlementTrades.getSettlementTradesPage", map);
	
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.SettlementTradesDao#getSettlementTradesPageCount(java.util.Map)
	 */
	@Override
	public Integer getSettlementTradesPageCount(Map<String, Object> map) {
		
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.SettlementTrades.getSettlementTradesPageCount", map);
	}
	@Override
	public Integer getRepeatCountCode(Map<String, Object> map) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.SettlementTrades.getRepeatCodeCount", map);
	}
	@Override
	public Integer getRepeatCountName(Map<String, Object> map) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.SettlementTrades.getRepeatNameCount", map);
	}



	
	
}
