package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class ParticipantDto extends BaseDto{

		
	//环节参与者类型: 1:审批,2:抄送,3:模板可阅
	private String type;
    
  		
	//流程模板Id
	private String flId;
    
	//流程模板名称
	private String flowName;
  		
	//环节id
	private String acId;
    
  		
	//组织机构id
	private String participantId;
	//组织机构名称
	private String participantName;
  		
	//组织机构类型: 1:人员,2: 岗位,3:角色,4:相对参与人
	private String participantType;
    
  		
	//角色参与者计算范围: 11:指定人员，12:表单人员 ;21:指定岗位，22:表单岗位 ;31:指定角色（逻辑表示），311:集团，312：本公司，313：本部部门，314：本项目，315:本分期 ，316:指定机构  ，317:表单机构41：发起人直接领导，42：发起人顶级部门领导，43：上一环节审批人直接领导，44：上一环节审批人顶级部门领导
	private String participantScope;
  		
	private String paramValue;//只在participant_scope为316或317时指定组织或表单组织时使用
	//序号
	private Long sort;
	
	//替换审批人时备用的字段
	//组织机构id
	private String oldParticipantId;
	private String oldParticipantType;
		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getFlId() {
		return flId;
	}
	public void setFlId(String flId) {
		this.flId = flId;
	}
    
  		
	public String getAcId() {
		return acId;
	}
	public void setAcId(String acId) {
		this.acId = acId;
	}
    
  		
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
    
  		
	public String getParticipantType() {
		return participantType;
	}
	public void setParticipantType(String participantType) {
		this.participantType = participantType;
	}
    
  		
	public String getParticipantScope() {
		return participantScope;
	}
	public void setParticipantScope(String participantScope) {
		this.participantScope = participantScope;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getParticipantName() {
		return participantName;
	}
	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}
	public String getOldParticipantId() {
		return oldParticipantId;
	}
	public void setOldParticipantId(String oldParticipantId) {
		this.oldParticipantId = oldParticipantId;
	}
	public String getOldParticipantType() {
		return oldParticipantType;
	}
	public void setOldParticipantType(String oldParticipantType) {
		this.oldParticipantType = oldParticipantType;
	}
}
