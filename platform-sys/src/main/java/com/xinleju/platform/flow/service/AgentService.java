package com.xinleju.platform.flow.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dto.AgentDto;
import com.xinleju.platform.flow.entity.Agent;

/**
 * @author admin
 * 
 * 
 */

public interface AgentService extends  BaseService <String,Agent>{

	int changeStatusByAgentId(String agentId, String status) throws Exception;

	void saveAgentAllData(AgentDto agentDto) throws Exception;

	int updateAgentAllData(AgentDto agentDto) throws Exception;

	//List<AgentDto> queryAgentInstanceList(Map<String, String> map) throws Exception;
	//把List查询改为分页查询
	Page queryAgentInstanceListByPageParam(Map<String, Object> map) throws Exception;
	/**
	 * 查询指定用户的流程代理
	 * 当用户的岗位范围、流程范围、代理时间符合时返回用户的代理用户, 不符合时返回NULL
	 * 
	 * @param userId：用户ID
	 * @param postId：岗位ID
	 * @param flId：流程定义Code(flId在修改模板是会变化，flCode不会变化)
	 */
	public Agent queryAgentBy(String userId, String postId, String flCode);


	/**
	 * 查询指定用户指定时间有交集的流程代理
	 * 当用户的岗位范围、流程范围、代理时间符合时返回用户的代理用户, 不符合时返回NULL
	 *
	 *
	 */
	public List<Agent> queryAgentByUserAndTimeIntersection(String userinfo ,AgentDto agentDto);

	
}
