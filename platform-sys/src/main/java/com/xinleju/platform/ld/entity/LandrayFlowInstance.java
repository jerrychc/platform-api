package com.xinleju.platform.ld.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 *
 *
 */

@Table(value="PT_LANDRAY_FLOW_INSTANCE",desc="流程实例")
public class LandrayFlowInstance extends BaseEntity{


	@Column(value="code",desc="编号")
	private String code;


	@Column(value="name",desc="主题")
	private String name;


	@Column(value="start_user_id",desc="申请人id")
	private String startUserId;


	@Column(value="start_dept_name",desc="发起部门名称")
	private String startDeptName;


	@Column(value="start_company_name",desc="发起公司名称")
	private String startCompanyName;


	@Column(value="fl_type_id",desc="所属流程分类")
	private String flTypeId;


	@Column(value="templet_name",desc="模板名称")
	private String templetName;


	@Column(value="status",desc="文档状态")
	private String status;


	@Column(value="start_date",desc="发布时间")
	private String startDate;


	@Column(value="url",desc="静态页面url")
	private String url;



	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public String getStartUserId() {
		return startUserId;
	}
	public void setStartUserId(String startUserId) {
		this.startUserId = startUserId;
	}


	public String getStartDeptName() {
		return startDeptName;
	}
	public void setStartDeptName(String startDeptName) {
		this.startDeptName = startDeptName;
	}


	public String getStartCompanyName() {
		return startCompanyName;
	}
	public void setStartCompanyName(String startCompanyName) {
		this.startCompanyName = startCompanyName;
	}


	public String getFlTypeId() {
		return flTypeId;
	}
	public void setFlTypeId(String flTypeId) {
		this.flTypeId = flTypeId;
	}


	public String getTempletName() {
		return templetName;
	}
	public void setTempletName(String templetName) {
		this.templetName = templetName;
	}


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}


	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}



}
