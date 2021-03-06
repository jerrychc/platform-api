package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class InstanceAccessibleDto extends BaseDto{

		
	//流程实例id
	private String fiId;
    
  		
	//可阅者类型: 1:角色,2:岗位,3:人员
	private String type;
    
  	//可阅者ID
	private String accessibleId;
	
	//可阅者名称
	private String accessibleName;
    
	//实例ID
	private String instanceId;
		
	//可阅者名称
	private String instanceName;
  		
		
	public String getFiId() {
		return fiId;
	}
	public void setFiId(String fiId) {
		this.fiId = fiId;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAccessibleId() {
		return accessibleId;
	}
	public void setAccessibleId(String accessibleId) {
		this.accessibleId = accessibleId;
	}
	public String getAccessibleName() {
		return accessibleName;
	}
	public void setAccessibleName(String accessibleName) {
		this.accessibleName = accessibleName;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
}
