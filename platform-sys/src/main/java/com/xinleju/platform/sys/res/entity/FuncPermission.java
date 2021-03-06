package com.xinleju.platform.sys.res.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_RES_FUNC_PERMISSION",desc="功能授权")
public class FuncPermission extends BaseEntity{
	
		
	@Column(value="role_id",desc="角色id")
	private String roleId;
    
	@Column(value="operation_id",desc="操作点id")
	private String operationId;
	
	@Column(value="app_id",desc="系统id")
	private String appId;
	
	@Column(value="resource_id",desc="资源id")
	private String resourceId;
	
  		
		
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getOperationId() {
		return operationId;
	}
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
    
  		
	
}
