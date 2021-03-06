package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * 流程收藏实体
 *  
 * @author admin
 * 
 */

@Table(value="PT_FLOW_COLLECTION",desc="流程收藏")
public class InstanceCollection extends BaseEntity{

	@Column(value="user_id",desc="用户ID")
	private String userId;
    
	@Column(value="instance_id",desc="流程实例ID")
	private String instanceId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
}
