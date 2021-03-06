package com.xinleju.platform.flow.operation;

import com.xinleju.platform.flow.dto.ApprovalSubmitDto;
import com.xinleju.platform.flow.exception.FlowException;
import com.xinleju.platform.flow.model.InstanceUnit;

/**
 * 审批操作
 * 
 * @author daoqi
 */
public interface Operation {

	/**
	 * 1)、审批列表的状态变更、增项、排序
	 * 2)、任务处理：任务新增、状态变更 
	 * 3)、环节及实例的状态变更
	 * 
	 * @param instanceUnit
	 * @param approvalDto
	 * @param service
	 * @return
	 * @throws Exception
	 */
	public String action(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto) throws Exception; 
	
	public OperationType getType();
}
