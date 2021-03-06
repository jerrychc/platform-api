package com.xinleju.platform.finance.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.finance.dao.AccountCaptionDao;
import com.xinleju.platform.finance.entity.AccountCaption;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class AccountCaptionDaoImpl extends BaseDaoImpl<String,AccountCaption> implements AccountCaptionDao{

	public AccountCaptionDaoImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.dao.AccountCaptionDao#getAccountCaptionList()
	 */
	@Override
	public List<AccountCaption> getAccountCaptionList(Map map) {
		return getSqlSession().selectList("com.xinleju.platform.finance.entity.AccountCaption.getAccountCaptionList",map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.dao.AccountCaptionDao#getAccountCaptionParentIdsList()
	 */
	@Override
	public List<String> getAccountCaptionParentIdsList(Map map) {
		return getSqlSession().selectList("com.xinleju.platform.finance.entity.AccountCaption.getAccountCaptionParentIdsList",map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.dao.AccountCaptionDao#getAccountCaptionListById(java.lang.String)
	 */
	@Override
	public List<String> getAccountCaptionListById(String id) {
		Map<String,Object> map =new HashMap<String, Object>();
		map.put("id", id);
		return getSqlSession().selectList("com.xinleju.platform.finance.entity.AccountCaption.getAccountCaptionListById",map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.dao.AccountCaptionDao#getAccountCaptionListByParentId(java.util.Map)
	 */
	@Override
	public List<AccountCaption> getAccountCaptionListByParentId(
			Map<String, Object> map) {
		return getSqlSession().selectList("com.xinleju.platform.finance.entity.AccountCaption.getAccountCaptionListByParentId",map);
	}

	public List<String> queryByBudCodeList(Map map){
		return getSqlSession().selectList("com.xinleju.platform.finance.entity.AccountCaption.queryByBudCodeList",map);
	}
	
}
