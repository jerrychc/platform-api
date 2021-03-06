package com.xinleju.platform.flow.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.FlowUserOpinionDao;
import com.xinleju.platform.flow.entity.FlowUserOpinion;

/**
 * @author 
 * 
 * 
 */

@Repository
public class FlowUserOpinionDaoImpl extends BaseDaoImpl<String,FlowUserOpinion> implements FlowUserOpinionDao{

	public FlowUserOpinionDaoImpl() {
		super();
	}

	/**
	 * 获取用户自定义意见
	 * @param param
	 * @return
	 */
	public List<FlowUserOpinion> queryUserOpinion(Map<String, Object> param) {
		return getSqlSession().selectList("com.xinleju.platform.flow.entity.FlowUserOpinion.queryUserOpinion", param);
	}

	@Override
	public int delUserOpinion(Map<String, Object> param) {
		return getSqlSession().delete("com.xinleju.platform.flow.entity.FlowUserOpinion.delUserOpinion", param);
	}
	
}
