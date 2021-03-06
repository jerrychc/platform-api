package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;


import java.sql.Timestamp;



/**
 * @author admin
 * 
 *
 */
public class InstanceTransitionRecordDto extends BaseDto{

		
	//流程实例id
	private String fiId;
    
  		
	//环节id
	private String acId;
    
  		
	//环节名称
	private String acName;
    
  		
	//流转人id
	private String transationUserId;
    
  		
	//流转人
	private String transationUserName;
    
  		
	//被流转人id
	private String toUserId;
    
  		
	//被流转人
	private String toUserName;
    
  		
	//流转日期
	private Timestamp transationDate;
    
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
