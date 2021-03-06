package com.xinleju.platform.sys.res.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_RES_DATA_PERMISSION",desc="数据授权")
public class DataPermission extends BaseEntity{
	
		
	@Column(value="role_id",desc="角色id")
	private String roleId;
    
  		
	@Column(value="data_point_id",desc="控制点id")
	private String dataPointId;
    
	@Column(value="app_id",desc="系统id")
	private String appId;	
		
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
    
  		
	public String getDataPointId() {
		return dataPointId;
	}
	public void setDataPointId(String dataPointId) {
		this.dataPointId = dataPointId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
    
  		
	
}
