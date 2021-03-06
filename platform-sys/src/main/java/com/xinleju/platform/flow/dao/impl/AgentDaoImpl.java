package com.xinleju.platform.flow.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.AgentDao;
import com.xinleju.platform.flow.dto.AgentDto;
import com.xinleju.platform.flow.entity.Agent;
import com.xinleju.platform.flow.entity.Instance;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class AgentDaoImpl extends BaseDaoImpl<String,Agent> implements AgentDao{

	public AgentDaoImpl() {
		super();
	}

	@Override
	public int deleteAllFlowDateByAgentId(String agentId) {
		String objectName = "com.xinleju.platform.flow.entity.Agent.deleteAllFlowDateByAgentId";
		return getSqlSession().update(objectName, agentId);
	}

	@Override
	public int deleteAllPostDateByAgentId(String agentId) {
		String objectName = "com.xinleju.platform.flow.entity.Agent.deleteAllPostDateByAgentId";
		return getSqlSession().update(objectName, agentId);
	}
	
	@Override
	public List<Map<String,Object>> queryAgentInstanceListByParam(Map<String, Object> paramMap) {
		String appId = (String)paramMap.get("appId");
	/*	if(appId == null || "-1".equals(appId) ){
			return new ArrayList();
		}*/
		 List<Map<String,Object>> list = getSqlSession().selectList(Agent.class.getName()+".queryAgentInstanceListByParam", paramMap);
		 return list;
	}

	@Override
	public Integer queryAgentInstanceCountByParam(Map<String, Object> paramMap) {
		String appId = (String)paramMap.get("appId");
		/*if(appId == null || "-1".equals(appId) ){
			return 0;
		}*/
		return getSqlSession().selectOne(Agent.class.getName()+".queryAgentInstanceCountByParam", paramMap);
	}
	

	@Override
	public List<Agent> queryAgentBy(String userId, Timestamp currentTime) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("currentTime", currentTime);
		return getSqlSession().selectList(Agent.class.getName()+".queryAgentByUserAndTime", paramMap);
	}

	@Override
	public List<Agent> queryAgentByUserAndTimeIntersection(String userId,String agentId, Timestamp start_date,Timestamp end_date){
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId);
		paramMap.put("agentId",agentId);
		paramMap.put("start_date", start_date);
		paramMap.put("end_date", end_date);
		return getSqlSession().selectList(Agent.class.getName()+".queryAgentByUserAndTimeIntersection", paramMap);
	}
	
	
}
