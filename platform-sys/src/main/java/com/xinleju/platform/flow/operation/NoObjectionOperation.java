package com.xinleju.platform.flow.operation;

import com.xinleju.platform.flow.dto.ApprovalSubmitDto;
import com.xinleju.platform.flow.model.InstanceUnit;

/**
 * 无异议（与同意类似）
 * 
 * @author daoqi
 *
 */
public class NoObjectionOperation extends DefaultOperation implements Operation{

	NoObjectionOperation() {
		super(OperationType.NOOBJECTION);
	}

//	@Override
//	public String action(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto)
//			throws Exception {
//		
//		next(instanceUnit, approvalDto);
//		
//		//4、保存DB
//		save(instanceUnit);
//		
//		//5、发送待办消息
//		handleMessages(instanceUnit, approvalDto);
//		
//		//删除待办消息
////		completeMessage(approvalDto.getMsgId());
//		
//		return "success";
//	}
	
	@Override
	public void operate(InstanceUnit instanceUnit, 
			ApprovalSubmitDto approvalDto) throws Exception {
		//下一步
		next(instanceUnit, approvalDto);
	}
}
