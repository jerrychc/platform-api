package com.xinleju.platform.flow.entity;

public class SortedActor implements Comparable<SortedActor> {

	private String id, type;//环节参与者类型: 1:审批,2:抄送,3:模板可阅
	private String flId; //流程模板Id
	private String acId; //环节id	
	
	private String participantId; //组织机构id
	
	private String participantType; //组织机构类型: 1:人员,2: 岗位,3:角色,4:相对参与人
	private String participantScope;
	//角色参与者计算范围: 11:指定人员，12:表单人员 ;21:指定岗位，22:表单岗位 ;31:指定角色（逻辑表示），
	//311:集团，312：本公司，313：本部部门，314：本项目，315:本分期 ，316:指定机构  ，317:表单机构
	// 41：发起人直接领导，42：发起人顶级部门领导，43：上一环节审批人直接领导，44：上一环节审批人顶级部门领导
	private Long sort;//序号
	private String paramValue;//只在participant_scope为316或317时指定组织或表单组织时使用
	private String paramValueName;
    @Override
    public String toString() {
		/* 北京公司/成本部/张三（人员）；  经办人（表单人员）；    北京公司/财务经理（岗位）； 部门会计（表单岗位）
		成本经理/本公司（角色）； 成本经理/指定组织/北京公司（角色）； 成本经理/单据组织/北京公司（角色）；*/      
    	String actorName = "";
    	actorName = participantId;
    	if("11".equals(participantScope)){
    		return actorName+ "(人员)";
    	}else if("12".equals(participantScope)){
    		return actorName + "(表单人员)";
    	}else if("21".equals(participantScope)){
    		return actorName + "(岗位)";
    	}else if("22".equals(participantScope)){
    		return actorName + "(表单岗位)";
    		
    	}else if("31".equals(participantScope)){
    		return actorName + "/本集团(角色)";
    	}else if("32".equals(participantScope)){
    		return actorName + "/本公司(角色)";
    	}else if("33".equals(participantScope)){
    		return actorName + "/本部门(角色)";
    	}else if("34".equals(participantScope)){
    		return actorName + "/本项目(角色)";
    	}else if("34".equals(participantScope)){
    		return actorName + "/本分期(角色)";
    	}else if("36".equals(participantScope)){
    		return actorName+"/指定组织/"+paramValueName + "(角色)";
    	}else if("37".equals(participantScope)){
    		return actorName+"/表单组织/"+paramValueName + "(角色)";
    		
    	}else if("41".equals(participantScope)){
    		return  "发起人直接领导(相对参与人)";
    	}else if("42".equals(participantScope)){
    		return "发起人顶级部门领导(相对参与人)";
    	}else if("43".equals(participantScope)){
    		return  "上一环节审批人直接领导(相对参与人)";
    	}else if("44".equals(participantScope)){
    		return "上一环节审批人顶级部门领导(相对参与人)";
    	}
    	return "";
    }
    
	 //此方法是提供给Collections.sort方法使用的。
	@Override
	public int compareTo(SortedActor m) {
		// 只能对一个字段做比较，如果做整个对象的比较就实现不了按指定字段排序了。 
		int aSort = this.getSort().intValue();
		int mSort = m.getSort().intValue();
        return aSort-mSort;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getParamValueName() {
		return paramValueName;
	}

	public void setParamValueName(String paramValueName) {
		this.paramValueName = paramValueName;
	}

}
