package com.xinleju.platform.flow.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dto.BaseDto;

/**
 * @author admin
 * 
 *
 */
public class MonitorSettingDto extends BaseDto {

	// 监控编号
	private String code;
	
	//监控类型
	private String monitorType;
	
	// 监控名称
	private String name;

	// 状态
	private String status;
	
	// 监控开始日期
	private Timestamp startDate;

	// 监控结束日期
	private Timestamp endDate;

	// 备注
	private String remark;

	private List<Map<String, String>> monitorList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> monitoredList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> flowList = new ArrayList<Map<String, String>>();
	private List<Map<String, String>> pointList = new ArrayList<Map<String, String>>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Map<String, String>> getMonitorList() {
		return monitorList;
	}

	public void setMonitorList(List<Map<String, String>> monitorList) {
		this.monitorList = monitorList;
	}

	public List<Map<String, String>> getMonitoredList() {
		return monitoredList;
	}

	public void setMonitoredList(List<Map<String, String>> monitoredList) {
		this.monitoredList = monitoredList;
	}

	public List<Map<String, String>> getFlowList() {
		return flowList;
	}

	public void setFlowList(List<Map<String, String>> flowList) {
		this.flowList = flowList;
	}

	public List<Map<String, String>> getPointList() {
		return pointList;
	}

	public void setPointList(List<Map<String, String>> pointList) {
		this.pointList = pointList;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

}
