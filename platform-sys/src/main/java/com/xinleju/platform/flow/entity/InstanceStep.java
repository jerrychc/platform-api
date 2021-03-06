package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_INSTANCE_STEP",desc="流程节点连接")
public class InstanceStep extends BaseEntity{
	
		
	@Column(value="fi_id",desc="流程实例id")
	private String fiId;
    
  		
	@Column(value="name",desc="名称")
	private String name;
    
  		
	@Column(value="step_id",desc="模板连线id")
	private String stepId;
    
  		
	@Column(value="source_id",desc="源节点")
	private String sourceId;
    
  		
	@Column(value="target_id",desc="目标结点")
	private String targetId;
    
  		
	@Column(value="start_x",desc="开始点x坐标")
	private Long startX;
    
  		
	@Column(value="start_y",desc="开始点y坐标")
	private Long startY;
    
  		
	@Column(value="target_x",desc="目标点x坐标")
	private Long targetX;
    
  		
	@Column(value="target_y",desc="目标点y坐标")
	private Long targetY;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
	@Column(value="disable",desc="是否有效")
	private Boolean disable;
    
  		
		
	public String getFiId() {
		return fiId;
	}
	public void setFiId(String fiId) {
		this.fiId = fiId;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
   
    
  		
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
    
  		
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
    
  		
	public Long getStartX() {
		return startX;
	}
	public void setStartX(Long startX) {
		this.startX = startX;
	}
    
  		
	public Long getStartY() {
		return startY;
	}
	public void setStartY(Long startY) {
		this.startY = startY;
	}
    
  		
	public Long getTargetX() {
		return targetX;
	}
	public void setTargetX(Long targetX) {
		this.targetX = targetX;
	}
    
  		
	public Long getTargetY() {
		return targetY;
	}
	public void setTargetY(Long targetY) {
		this.targetY = targetY;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public Boolean getDisable() {
		return disable;
	}
	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
	public String getStepId() {
		return stepId;
	}
	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
    
  		
	
}
