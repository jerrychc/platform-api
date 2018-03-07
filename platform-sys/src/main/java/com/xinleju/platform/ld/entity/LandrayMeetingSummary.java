package com.xinleju.platform.ld.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 *
 *
 */

@Table(value="PT_LANDRAY_MEETING_SUMMARY",desc="会议纪要")
public class LandrayMeetingSummary extends BaseEntity{


	@Column(value="doc_subject",desc="会议名称")
	private String docSubject;


	@Column(value="type_of_meeting",desc="会议类型")
	private String typeOfMeeting;


	@Column(value="doc_status",desc="状态")
	private String docStatus;


	@Column(value="fd_plan_hold_date",desc="计划召开日期")
	private String fdPlanHoldDate;


	@Column(value="fd_plan_hold_time",desc="实际召开日期")
	private String fdPlanHoldTime;


	@Column(value="fd_plan_other_hold_place",desc="计划召开地点")
	private String fdPlanOtherHoldPlace;


	@Column(value="fd_other_hold_place",desc="实际召开地点")
	private String fdOtherHoldPlace;


	@Column(value="fd_other_host",desc="主持人")
	private String fdOtherHost;


	@Column(value="fd_host_id",desc="组织人")
	private String fdHostId;


	@Column(value="fd_template_id",desc="所属模板id")
	private String fdTemplateId;


	@Column(value="fd_template_name",desc="所属模板名称")
	private String fdTemplateName;


	@Column(value="url",desc="url")
	private String url;



	public String getDocSubject() {
		return docSubject;
	}
	public void setDocSubject(String docSubject) {
		this.docSubject = docSubject;
	}


	public String getTypeOfMeeting() {
		return typeOfMeeting;
	}
	public void setTypeOfMeeting(String typeOfMeeting) {
		this.typeOfMeeting = typeOfMeeting;
	}


	public String getDocStatus() {
		return docStatus;
	}
	public void setDocStatus(String docStatus) {
		this.docStatus = docStatus;
	}


	public String getFdPlanHoldDate() {
		return fdPlanHoldDate;
	}
	public void setFdPlanHoldDate(String fdPlanHoldDate) {
		this.fdPlanHoldDate = fdPlanHoldDate;
	}


	public String getFdPlanHoldTime() {
		return fdPlanHoldTime;
	}
	public void setFdPlanHoldTime(String fdPlanHoldTime) {
		this.fdPlanHoldTime = fdPlanHoldTime;
	}


	public String getFdPlanOtherHoldPlace() {
		return fdPlanOtherHoldPlace;
	}
	public void setFdPlanOtherHoldPlace(String fdPlanOtherHoldPlace) {
		this.fdPlanOtherHoldPlace = fdPlanOtherHoldPlace;
	}


	public String getFdOtherHoldPlace() {
		return fdOtherHoldPlace;
	}
	public void setFdOtherHoldPlace(String fdOtherHoldPlace) {
		this.fdOtherHoldPlace = fdOtherHoldPlace;
	}


	public String getFdOtherHost() {
		return fdOtherHost;
	}
	public void setFdOtherHost(String fdOtherHost) {
		this.fdOtherHost = fdOtherHost;
	}


	public String getFdHostId() {
		return fdHostId;
	}
	public void setFdHostId(String fdHostId) {
		this.fdHostId = fdHostId;
	}


	public String getFdTemplateId() {
		return fdTemplateId;
	}
	public void setFdTemplateId(String fdTemplateId) {
		this.fdTemplateId = fdTemplateId;
	}


	public String getFdTemplateName() {
		return fdTemplateName;
	}
	public void setFdTemplateName(String fdTemplateName) {
		this.fdTemplateName = fdTemplateName;
	}


	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}



}
