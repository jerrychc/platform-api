package com.xinleju.platform.sys.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.base.dao.PayTypeDao;
import com.xinleju.platform.sys.base.entity.PayType;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class PayTypeDaoImpl extends BaseDaoImpl<String,PayType> implements PayTypeDao{

	public PayTypeDaoImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.PayTypeDao#payTypeParanetList()
	 */
	@Override
	public List<PayType> payTypeParanetList(Map<String,Object> map) {
		 List<PayType> list=	  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.PayType.getPayTypeParanetList",map);
	return list;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.PayTypeDao#getPayTypeCountByCode(java.util.Map)
	 */
	@Override
	public Integer getPayTypeCountByCode(Map<String, Object> hmap) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.base.entity.PayType.getPayTypeCountByCode",hmap);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dao.PayTypeDao#queryListOrderBySort()
	 */
	@Override
	public List<PayType> queryListOrderBySort() {
		return  getSqlSession().selectList("com.xinleju.platform.sys.base.entity.PayType.queryListOrderBySort");

	}
	
	@Override
	public List<Map<String, Object>> getAllPayType(Map<String, Object> param) throws Exception {
		return getSqlSession().selectList("com.xinleju.platform.sys.base.entity.PayType.getAllPayType",param);
	}
}
