package com.xinleju.platform.sys.base.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_BASE_COMMON_DRAFT",desc="通用草稿")
public class CommonDraft extends BaseEntity{
	
		
	@Column(value="content",desc="内容")
	private String content;
    
  		
	@Column(value="custom_form_id",desc="表单ID")
	private String customFormId;
    
  		
	@Column(value="operator_id",desc="经办人ID")
	private String operatorId;
    
  		
	@Column(value="operator_name",desc="经办人名称")
	private String operatorName;
    
  		
	@Column(value="operate_department_id",desc="经办部门ID")
	private String operateDepartmentId;
    
  		
	@Column(value="operate_department_name",desc="经办部门名称")
	private String operateDepartmentName;
    
  		
	@Column(value="operate_company_id",desc="经办公司id")
	private String operateCompanyId;
    
  		
	@Column(value="operate_company_name",desc="经办公司名称")
	private String operateCompanyName;
    
  		
	@Column(value="operate_project_id",desc="经办项目ID")
	private String operateProjectId;
    
  		
	@Column(value="operate_project_name",desc="经办项目名称")
	private String operateProjectName;
    
  		
	@Column(value="operate_qi_id",desc="经办分期ID")
	private String operateQiId;
    
  		
	@Column(value="operate_qi_name",desc="经办分期名称")
	private String operateQiName;
    
  		
	@Column(value="title",desc="标题")
	private String title;
    
  		
	@Column(value="backup1",desc="备用字段1")
	private String backup1;
    
  		
	@Column(value="backup2",desc="备用字段2")
	private String backup2;
    
  		
	@Column(value="backup3",desc="备用字段3")
	private String backup3;
    
  		
	@Column(value="backup4",desc="备用字段4")
	private String backup4;
    
  		
	@Column(value="backup5",desc="备用字段5")
	private String backup5;
    
  		
	@Column(value="custom_form_his_id",desc="历史版本id")
	private String customFormHisId;
    
  		
	@Column(value="business_type",desc="业务类型")
	private String businessType;

	@Column(value="business_object_name",desc="业务对象名称")
	private String businessOjbectName;
		
	public String getBusinessOjbectName() {
		return businessOjbectName;
	}
	public void setBusinessOjbectName(String businessOjbectName) {
		this.businessOjbectName = businessOjbectName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
    
  		
	public String getCustomFormId() {
		return customFormId;
	}
	public void setCustomFormId(String customFormId) {
		this.customFormId = customFormId;
	}
    
  		
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
    
  		
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
    
  		
	public String getOperateDepartmentId() {
		return operateDepartmentId;
	}
	public void setOperateDepartmentId(String operateDepartmentId) {
		this.operateDepartmentId = operateDepartmentId;
	}
    
  		
	public String getOperateDepartmentName() {
		return operateDepartmentName;
	}
	public void setOperateDepartmentName(String operateDepartmentName) {
		this.operateDepartmentName = operateDepartmentName;
	}
    
  		
	public String getOperateCompanyId() {
		return operateCompanyId;
	}
	public void setOperateCompanyId(String operateCompanyId) {
		this.operateCompanyId = operateCompanyId;
	}
    
  		
	public String getOperateCompanyName() {
		return operateCompanyName;
	}
	public void setOperateCompanyName(String operateCompanyName) {
		this.operateCompanyName = operateCompanyName;
	}
    
  		
	public String getOperateProjectId() {
		return operateProjectId;
	}
	public void setOperateProjectId(String operateProjectId) {
		this.operateProjectId = operateProjectId;
	}
    
  		
	public String getOperateProjectName() {
		return operateProjectName;
	}
	public void setOperateProjectName(String operateProjectName) {
		this.operateProjectName = operateProjectName;
	}
    
  		
	public String getOperateQiId() {
		return operateQiId;
	}
	public void setOperateQiId(String operateQiId) {
		this.operateQiId = operateQiId;
	}
    
  		
	public String getOperateQiName() {
		return operateQiName;
	}
	public void setOperateQiName(String operateQiName) {
		this.operateQiName = operateQiName;
	}
    
  		
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    
  		
	public String getBackup1() {
		return backup1;
	}
	public void setBackup1(String backup1) {
		this.backup1 = backup1;
	}
    
  		
	public String getBackup2() {
		return backup2;
	}
	public void setBackup2(String backup2) {
		this.backup2 = backup2;
	}
    
  		
	public String getBackup3() {
		return backup3;
	}
	public void setBackup3(String backup3) {
		this.backup3 = backup3;
	}
    
  		
	public String getBackup4() {
		return backup4;
	}
	public void setBackup4(String backup4) {
		this.backup4 = backup4;
	}
    
  		
	public String getBackup5() {
		return backup5;
	}
	public void setBackup5(String backup5) {
		this.backup5 = backup5;
	}
    
  		
	public String getCustomFormHisId() {
		return customFormHisId;
	}
	public void setCustomFormHisId(String customFormHisId) {
		this.customFormHisId = customFormHisId;
	}
    
  		
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
    
  		
	
}
