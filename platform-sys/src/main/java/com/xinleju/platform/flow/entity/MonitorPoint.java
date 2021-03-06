package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * @author admin
 * 
 * 
 */
@Table(value = "PT_FLOW_MONITOR_POINT", desc = "流程监控点")
public class MonitorPoint extends BaseEntity {

	@Column(value = "monitor_setting_id", desc = "监控设置id")
	private String monitorSettingId;

	@Column(value = "monitor_point_id", desc = "监控点id")
	private String pointId;

	@Column(value = "monitor_point_name", desc = "监控点名称")
	private String pointName;

	@Column(value = "handle", desc = "监控点处理方式")
	private String handle;
	
	public String getMonitorSettingId() {
		return monitorSettingId;
	}

	public void setMonitorSettingId(String monitorSettingId) {
		this.monitorSettingId = monitorSettingId;
	}

	public String getPointId() {
		return pointId;
	}

	public void setPointId(String pointId) {
		this.pointId = pointId;
	}

	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

}
