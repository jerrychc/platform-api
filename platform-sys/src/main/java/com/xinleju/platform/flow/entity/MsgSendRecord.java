package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_MSG_SEND_RECORD",desc="消息发送历史表, 用于记录代办消息发送历史记录，不论成功与否，发送过之后不再次发送")
public class MsgSendRecord extends BaseEntity{
	
		
	@Column(value="target",desc="发送目标端: 0-微信; 1-安卓; 2-iOS; 3-PC")
	private String target;
    
  		
	@Column(value="msg_id",desc="消息主键")
	private String msgId;
    
  		
	@Column(value="user_id",desc="消息接收人Id")
	private String userId;
    
	@Column(value="login_name",desc="消息接收人登录账号")
	private String loginName;
  		
	@Column(value="op_type",desc="操作类型")
	private String opType;
    
  		
	@Column(value="msg_title",desc="消息标题")
	private String msgTitle;
    
  		
	@Column(value="app_code",desc="所属模块")
	private String appCode;
    
  		
	@Column(value="send_date",desc="发起时间")
	private String sendDate;
    
  		
	@Column(value="err_code",desc="返回code")
	private String errCode;
    
  		
	@Column(value="err_msg",desc="返回消息")
	private String errMsg;
    
  		
		
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
    
  		
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}    
  		
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
    
  		
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
    
  		
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
    
  		
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
    
  		
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
    
  		
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
    
  		
	
}
