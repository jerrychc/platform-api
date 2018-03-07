package com.xinleju.platform.flow.entity;

import java.sql.Timestamp;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_PASS_READ_RECORD",desc="传阅日志")
public class PassReadRecord extends BaseEntity{
	
		
	@Column(value="fi_id",desc="流程实例id")
	private String fiId;
    
  		
	@Column(value="transation_user_id",desc="流转人id")
	private String transationUserId;
    
  		
	@Column(value="transation_user_name",desc="流转人")
	private String transationUserName;
    
  		
	@Column(value="to_user_id",desc="被流转人id")
	private String toUserId;
    
  		
	@Column(value="to_user_name",desc="被流转人")
	private String toUserName;
    
	@Column(value = "transation_date", desc = "流转日期")
	private Timestamp transationDate;
  		
	@Column(value="action_name",desc="操作类型")
	private String actionName;

	@Column(value="msg_id",desc="消息id")
	private String msgId;

	@Column(value = "cancel_pass_read_time", desc = "传阅取消日期")
	private Timestamp cancelPassReadTime;
	
	@Column(value="cancel_pass_read_user_id",desc="取消传阅人id")
	private String cancelPassReadUserId;
  		
	@Column(value="cancel_pass_read_user_name",desc="取消传阅人")
	private String cancelPassReadUserName;
		
	public String getCancelPassReadUserId() {
		return cancelPassReadUserId;
	}
	public void setCancelPassReadUserId(String cancelPassReadUserId) {
		this.cancelPassReadUserId = cancelPassReadUserId;
	}
	public String getCancelPassReadUserName() {
		return cancelPassReadUserName;
	}
	public void setCancelPassReadUserName(String cancelPassReadUserName) {
		this.cancelPassReadUserName = cancelPassReadUserName;
	}
	public Timestamp getCancelPassReadTime() {
		return cancelPassReadTime;
	}
	public void setCancelPassReadTime(Timestamp cancelPassReadTime) {
		this.cancelPassReadTime = cancelPassReadTime;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getFiId() {
		return fiId;
	}
	public void setFiId(String fiId) {
		this.fiId = fiId;
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
