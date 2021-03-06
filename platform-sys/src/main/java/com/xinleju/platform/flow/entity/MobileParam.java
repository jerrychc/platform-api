package com.xinleju.platform.flow.entity;

import java.io.Serializable;

public class MobileParam implements Serializable {

	private static final long serialVersionUID = -2882190255825140847L;
	/*{"instanceId":"a31968cd6ad34268a17b60fda30e5a8d",
		 "businessId":"05d9b86121ad4e45adc1c66a2152b927", 
		 "taskId":"taskId1234567890", 
		 "appId":"9d6cba61c4b24a5699c339a49471a0e7",
		 "typeCode":"SP","approveRole":"2"}*/
	private String instanceId;//流程实例Id, 例如"a31968cd6ad34268a17b60fda30e5a8d"
	private String businessId;//业务Id, 例如05d9hgfdhb86121ad4e45adc1c66a2152b927
	private String taskId;//流程任务Id, 例如 12366erwgfgfdhgfsdgfd
	private String appId;//应用系统Id, 例如9d6cba61c4b24a5699c339a49471a0e7
	private String typeCode;//操作类型编码, 例如SP
	private String approveRole;//审批角色, 例如2
	
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getApproveRole() {
		return approveRole;
	}
	public void setApproveRole(String approveRole) {
		this.approveRole = approveRole;
	}
	
	
}
