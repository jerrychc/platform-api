package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_INSTANCE_RETURN",desc="打回日志记录表")
public class InstanceReturn extends BaseEntity{
		
	@Column(value="instance_id",desc="流程实例id")
	private String instanceId;
    
	@Column(value="fl_id",desc="流程模板id")
	private String flId;

	@Column(value="fl_code",desc="流程模板编码")
	private String flCode;
	
	@Column(value="ac_id",desc="环节id")
	private String acId;
	
	@Column(value="post_id",desc="岗位id")
	private String postId;

	@Column(value="group_id",desc="人id")
	private String groupId;
	
	@Column(value="task_id",desc="任务id")
	private String taskId;

	@Column(value="ac_px",desc="环节顺序")
	private Long acPx;

	@Column(value="return_px",desc="打回顺序标识")
	private Long returnPx;

	@Column(value="pre_ac_ids",desc="来源环节ID")
	private String preAcIds;
	
	@Column(value="next_ac_ids",desc="后续环节ID")
	private String nextAcIds;

	@Column(value="approver_id",desc="审批人id")
	private String approverId;

	@Column(value="approver_name",desc="审批人名称")
	private String approverName;

	@Column(value="task_status",desc="任务状态")
	private String taskStatus;

	@Column(value="business_id",desc="单据id")
	private String businessId;
	
	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getFlId() {
		return flId;
	}

	public void setFlId(String flId) {
		this.flId = flId;
	}

	public String getFlCode() {
		return flCode;
	}

	public void setFlCode(String flCode) {
		this.flCode = flCode;
	}

	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Long getAcPx() {
		return acPx;
	}

	public void setAcPx(Long acPx) {
		this.acPx = acPx;
	}

	public Long getReturnPx() {
		return returnPx;
	}

	public void setReturnPx(Long returnPx) {
		this.returnPx = returnPx;
	}

	public String getPreAcIds() {
		return preAcIds;
	}

	public void setPreAcIds(String preAcIds) {
		this.preAcIds = preAcIds;
	}

	public String getNextAcIds() {
		return nextAcIds;
	}

	public void setNextAcIds(String nextAcIds) {
		this.nextAcIds = nextAcIds;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}
    
  		
	
}
