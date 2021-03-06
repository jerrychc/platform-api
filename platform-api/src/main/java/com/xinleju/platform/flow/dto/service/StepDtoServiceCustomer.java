package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface StepDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * 检查变更是否在流程模板表达式中使用
	 * 
	 * @param businessObjectId：业务对象ID
	 * @param varCode：业务对象变量编码
	 * @return
	 */
	public String bizVarBeUsedInFlow(String userInfo, String businessObjectId, String varCode);
	
}
