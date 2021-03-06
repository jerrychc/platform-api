package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_INSTANCE_STAT",desc="流程实例统计中间表")
public class InstanceStat extends BaseEntity{
	
		
	@Column(value="instance_id",desc="流程实例id")
	private String instanceId;
    
  		
	@Column(value="operate_type",desc="操作类型")
	private String operateType;
    
  		
	@Column(value="operate_time",desc="操作时间")
	private Timestamp operateTime;
    
  		
		
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
    
  		
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
    
  		
	public Timestamp getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Timestamp operateTime) {
		this.operateTime = operateTime;
	}
    
  		
	
}
