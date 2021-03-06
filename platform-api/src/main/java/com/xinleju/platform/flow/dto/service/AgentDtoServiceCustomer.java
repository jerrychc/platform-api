package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;
import com.xinleju.platform.flow.dto.AgentDto;

public interface AgentDtoServiceCustomer extends BaseDtoServiceCustomer{

	String changeStatus(String userinfo, String string);

	String queryAgentInstanceList(String userinfo, String paramaterJson);

	String queryAgentByUserAndTimeIntersection(String userinfo, AgentDto agentDto);

}
