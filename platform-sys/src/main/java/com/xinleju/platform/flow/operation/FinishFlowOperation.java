package com.xinleju.platform.flow.operation;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.xinleju.platform.flow.dto.ApprovalSubmitDto;
import com.xinleju.platform.flow.enumeration.InstanceStatus;
import com.xinleju.platform.flow.enumeration.TaskStatus;
import com.xinleju.platform.flow.model.ACUnit;
import com.xinleju.platform.flow.model.ApproverUnit;
import com.xinleju.platform.flow.model.InstanceUnit;
import com.xinleju.platform.flow.model.PostUnit;
import com.xinleju.platform.flow.model.TaskUnit;

/**
 * 管理员功能：流程审结
 * 
 * 将所有待办消息删除，即审批人和后面所有未审批的人没有查看流程的入口了
 * 
 * @author daoqi
 *
 */
public class FinishFlowOperation extends DefaultOperation implements Operation{

	public FinishFlowOperation() {
		super(OperationType.FINISHFLOW);
	}

	/**
	 * 流程审结
	 */
	@Override
	protected void operate(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto) throws Exception {
		//查找点亮行并设置为当行审批人
		for(ACUnit acUnit : instanceUnit.getAcList()) {
			List<PostUnit> posts = acUnit.getPosts();
			if(CollectionUtils.isEmpty(posts)) {
				continue;
			}
			for(PostUnit postUnit : posts) {
				List<ApproverUnit> approvers = postUnit.getApprovers();
				if(CollectionUtils.isEmpty(approvers)) {
					continue;
				}
				for(ApproverUnit approverUnit : approvers) {
					TaskUnit task = approverUnit.getTask();
					if(task == null || approverUnit.getDbAction() == 2) {	//当前人可能在其他人审结时跳过被删除
						continue;
					}
					
					if(TaskStatus.RUNNING.getValue().equals(task.getTaskStatus())) {
						setCurrentLocation(acUnit, postUnit, approverUnit);		//此时action中的设置无效！
						
						//收集待消除待办
						instanceUnit.getMessagesToDel().add(task.getMsgId());
						
						
						next(instanceUnit, approvalDto);			//多人之间的跳过可能存在影响？？？
					}
				}
			}
		}
		
		instanceUnit.setStatus(InstanceStatus.FINISHED.getValue());
		instanceUnit.setEndDate(new Timestamp(System.currentTimeMillis()));
		
		//撤回此流程实例中其他管理员没有处理的待办
		service.getMsgService().deleteMsgOfAdminBy(instanceUnit.getId());
	}

	@Override
	public void handleMessages(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto) throws Exception {
		//管理员审结流程时不发送消息！！！
		super.withDrawMessage(instanceUnit);
	}
}
