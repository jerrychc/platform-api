package com.xinleju.platform.ld.dao.impl;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.ld.dao.LandrayFlowTypeDao;
import com.xinleju.platform.ld.entity.LandrayFlowInstance;
import com.xinleju.platform.ld.entity.LandrayFlowType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class LandrayFlowTypeDaoImpl extends BaseDaoImpl<String,LandrayFlowType> implements LandrayFlowTypeDao {

	public LandrayFlowTypeDaoImpl() {
		super();
	}


	@Override
	public List<LandrayFlowType> queryFLTypeTree(Map<String, Object> pMap) {
			return  getSqlSession().selectList("com.xinleju.platform.ld.entity.LandrayFlowType.queryFLTypeTree", pMap);
	}
}
