package com.xinleju.platform.flow.entity;

import java.sql.Timestamp;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * 流转日志
 * 
 * @author admin
 */
@Table(value = "PT_FLOW_INSTANCE_TRANSITION_RECORD", desc = "流程流转记录")
public class InstanceTransitionRecord extends BaseEntity {

	@Column(value = "fi_id", desc = "流程实例id")
	private String fiId;

	@Column(value = "ac_id", desc = "环节id")
	private String acId;

	@Column(value = "ac_name", desc = "环节名称")
	private String acName;

	@Column(value = "transation_user_id", desc = "流转人id")
	private String transationUserId;

	@Column(value = "transation_user_name", desc = "流转人")
	private String transationUserName;

	@Column(value = "to_user_id", desc = "被流转人id")
	private String toUserId;

	@Column(value = "to_user_name", desc = "被流转人")
	private String toUserName;

	@Column(value = "transation_date", desc = "流转日期")
	private Timestamp transationDate;

	// 流转动作
	@Column(value = "action_name", desc = "流转日期")
	private String actionName;

	public String getFiId() {
		return fiId;
	}

	public void setFiId(String fiId) {
		this.fiId = fiId;
	}

	public String getAcId() {
		return acId;
	}

	public void setAcId(String acId) {
		this.acId = acId;
	}

	public String getAcName() {
		return acName;
	}

	public void setAcName(String acName) {
		this.acName = acName;
	}

	public String getTransationUserId() {
		return transationUserId;
	}

	public void setTransationUserId(String transationUserId) {
		this.transationUserId = transationUserId;
	}

	public String getTransationUserName() {
		return transationUserName;
	}

	public void setTransationUserName(String transationUserName) {
		this.transationUserName = transationUserName;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public Timestamp getTransationDate() {
		return transationDate;
	}

	public void setTransationDate(Timestamp transationDate) {
		this.transationDate = transationDate;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
}
