package com.xinleju.platform.flow.operation;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.xinleju.platform.flow.dto.ApprovalSubmitDto;
import com.xinleju.platform.flow.enumeration.InstanceOperateType;
import com.xinleju.platform.flow.enumeration.InstanceStatus;
import com.xinleju.platform.flow.enumeration.TaskStatus;
import com.xinleju.platform.flow.exception.FlowException;
import com.xinleju.platform.flow.model.ACUnit;
import com.xinleju.platform.flow.model.ApproverUnit;
import com.xinleju.platform.flow.model.InstanceUnit;
import com.xinleju.platform.flow.model.PostUnit;
import com.xinleju.platform.flow.model.TaskUnit;

/**
 * 接受操作
 * 
 * @author daoqi
 *
 */
public class EmptyOperation extends DefaultOperation implements Operation{

	public EmptyOperation() {
		super(OperationType.ACCEPT);
	}

	@Override
	public String action(InstanceUnit instanceUnit, ApprovalSubmitDto approvalDto)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 撤回流程 TODO zhangdaoqiang
	 * @param instanceUnit
	 * @throws Exception 
	 */
	public void withDrawFlow(InstanceUnit instanceUnit) throws Exception {
		
		//流程实例置撤回标志
		instanceUnit.setStatus(InstanceStatus.WITHDRAW.getValue());
		instanceUnit.setEndDate(new Timestamp(System.currentTimeMillis()));
		
		//实例中运行中的任务置为跳过
		int delPostCnt = 0;
		for(ACUnit acUnit : instanceUnit.getAcList()) {
			if(CollectionUtils.isEmpty(acUnit.getPosts())) {
				continue;
			}
			
			for(PostUnit post : acUnit.getPosts()) {
				
				int delPersonCnt = 0;
				List<ApproverUnit> approvers = post.getApprovers();
				if(CollectionUtils.isEmpty(approvers)) {
					continue;
				}
				for(ApproverUnit approver : approvers) {
					TaskUnit task = approver.getTask();
					if(task != null && TaskStatus.RUNNING.getValue().equals(task.getTaskStatus())) {
						task.setTaskStatus(TaskStatus.SKIP.getValue());
						task.setEndTime(new Timestamp(System.currentTimeMillis()));
						task.setDbAction(2);
						
						approver.setDbAction(2);
						delPersonCnt++;
						
						//收集待撤回消息ID
						instanceUnit.getMessagesToDel().add(task.getMsgId());
					}
				}
				if(delPersonCnt == approvers.size()) {
					post.setDbAction(2);
					delPostCnt++; 
				}
			}
			
			if(delPostCnt == acUnit.getPosts().size()) {
				acUnit.setDbAction(2);
			}
		}
		
		//保存模型
		save(instanceUnit);
		
		//删除消息
		withDrawMessage(instanceUnit.getMessagesToDel());
		
		//记录操作日志
		String starter = instanceUnit.getAcList().get(0).getPosts().get(0).getApprovers().get(0).getApproverId();
		service.getInstanceLogService().saveLogData(instanceUnit.getId(), null, null, null, 
				InstanceOperateType.DRAW_BACK_INSTANCE.getValue(), starter, null, null);
		
	}

	/**
	 * 第二个人没有审批，无痕撤回
	 * 
	 * @param instanceUnit
	 */
	public void withDrawFlowNoTrace(InstanceUnit instanceUnit) {
		this.setInstanceUnit(instanceUnit);
		
		//流程实例置撤回标志
		instanceUnit.setStatus(InstanceStatus.WITHDRAW.getValue());
		instanceUnit.setEndDate(new Timestamp(System.currentTimeMillis()));
		
		for(ACUnit acUnit : instanceUnit.getAcList()) {
			acUnit.setDbAction(2);
			if(CollectionUtils.isEmpty(acUnit.getPosts())) {
				continue;
			}
			for(PostUnit post : acUnit.getPosts()) {
				List<ApproverUnit> approvers = post.getApprovers();
				if(CollectionUtils.isEmpty(approvers)) {
					continue;
				}
				for(ApproverUnit approver : approvers) {
					approver.setDbAction(2);
					TaskUnit task = approver.getTask();
					task.setDbAction(2);
					post.setDbAction(2);
					
					if(task != null && TaskStatus.RUNNING.getValue().equals(task.getTaskStatus())) {
						//收集待撤回消息ID
						instanceUnit.getMessagesToDel().add(task.getMsgId());
					}
				}
			}
		}
		
		try {
			save(instanceUnit);
		} catch (Exception e) {
			throw new FlowException("无痕撤回时数据保存失败： " + e.getMessage(), e);
		}
		
		//删除消息
		try {
			withDrawMessage(instanceUnit.getMessagesToDel());
		} catch (Exception e) {
			throw new FlowException("无痕撤回时删除消息失败：消息ID=" + instanceUnit.getMessagesToDel() + e.getMessage(), e);
		}
	}
	
	protected void withDrawMessage(List<String> msgIds) throws Exception {
		if(CollectionUtils.isNotEmpty(msgIds))  {
			getService().getMsgService().deletePseudoAllObjectByIds(msgIds);
			
			//TODO zhangdaoqiang 记录操作日志
			try {
				service.getInstanceLogService().deleteOperateLogBySpecialAction(instanceUnit.getId(),
						null, null, null, null);
				
			} catch (Exception e) {
				throw new FlowException("记录流程日志异常：", e);
			}
		}
	}

}
