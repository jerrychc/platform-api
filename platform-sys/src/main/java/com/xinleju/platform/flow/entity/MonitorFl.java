package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;

/**
 * @author admin
 * 
 * 
 */
@Table(value = "PT_FLOW_MONITOR_FL", desc = "监控流程模板")
public class MonitorFl extends BaseEntity {

	@Column(value = "monitor_setting_id", desc = "监控设置id")
	private String monitorSettingId;

	@Column(value = "fl_id", desc = "流程模板id")
	private String flId;

	@Column(value = "fl_name", desc = "流程模板名称")
	private String flName;

	public String getFlId() {
		return flId;
	}

	public void setFlId(String flId) {
		this.flId = flId;
	}

	public String getMonitorSettingId() {
		return monitorSettingId;
	}

	public void setMonitorSettingId(String monitorSettingId) {
		this.monitorSettingId = monitorSettingId;
	}

	public String getFlName() {
		return flName;
	}

	public void setFlName(String flName) {
		this.flName = flName;
	}

}
