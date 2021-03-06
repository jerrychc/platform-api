package com.xinleju.platform.party.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_PARTY_USER_CONFIG",desc="用户配置表")
public class UserConfig extends BaseEntity{
	
		
	@Column(value="app_id",desc="第三方应用id")
	private String appId;
    
  		
	@Column(value="user_id",desc="用户ID")
	private String userId;
    
  		
	@Column(value="token",desc="token")
	private String token;
    
  		
	@Column(value="start_time",desc="token开始时间")
	private String startTime;
    
  		
	@Column(value="end_time",desc="token结束时间")
	private String endTime;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
	
	@Column(value="remark",desc="状态")
	private String remark;
	
  		
		
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
    
  		
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
    
  		
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
    
  		
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
    
  		
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	
}
