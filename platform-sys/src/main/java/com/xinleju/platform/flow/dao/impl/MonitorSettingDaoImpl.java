package com.xinleju.platform.flow.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.MonitorSettingDao;
import com.xinleju.platform.flow.entity.MonitorSetting;
import com.xinleju.platform.flow.model.FlowMonitorBean;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class MonitorSettingDaoImpl extends BaseDaoImpl<String,MonitorSetting> implements MonitorSettingDao{

	public MonitorSettingDaoImpl() {
		super();
	}

	@Override
	public int deleteAllFlowBySettingId(String setttingId) {
		String method = MonitorSetting.class.getName()+".deleteAllFlowBySettingId";
		return this.getSqlSession().delete(method, setttingId);
	}
	 
	@Override
	public int deleteAllMonitorBySettingId(String setttingId) {
		String method = MonitorSetting.class.getName()+".deleteAllMonitorBySettingId";
		return this.getSqlSession().delete(method, setttingId);
	}

	@Override
	public int deleteAllMonitoredBySettingId(String setttingId) {
		String method = MonitorSetting.class.getName()+".deleteAllMonitoredBySettingId";
		return this.getSqlSession().delete(method, setttingId);
	}

	@Override
	public int deleteAllMonitorPoint(String setttingId) {
		String method = MonitorSetting.class.getName()+".deleteAllMonitorPointBySettingId";
		return this.getSqlSession().delete(method, setttingId);
	}

	@Override
	public List<Map<String, String>> queryMonitorList(String settingId) {
		String method = MonitorSetting.class.getName()+".queryMonitorList";
		return this.getSqlSession().selectList(method, settingId);
	}

	@Override
	public List<Map<String, String>> queryMonitoredList(String settingId) {
		String method = MonitorSetting.class.getName()+".queryMonitoredList";
		return this.getSqlSession().selectList(method, settingId);
	}

	@Override
	public List<Map<String, String>> queryFlowList(String settingId) {
		String method = MonitorSetting.class.getName()+".queryFlowList";
		return this.getSqlSession().selectList(method, settingId);
	}

	@Override
	public List<Map<String, String>> queryPointList(String settingId) {
		String method = MonitorSetting.class.getName()+".queryPointList";
		return this.getSqlSession().selectList(method, settingId);
	}

	@Override
	public List<FlowMonitorBean> queryMonitorByUser(Map<String, Object> params) {
		String method = MonitorSetting.class.getName()+".queryMonitorByUser";
		return this.getSqlSession().selectList(method, params);
	}

	@Override
	public List<FlowMonitorBean> queryMonitorByFlow(Map<String, Object> params) {
		String method = MonitorSetting.class.getName()+".queryMonitorByFlow";
		return this.getSqlSession().selectList(method, params);
	}

	@Override
	public List<FlowMonitorBean> queryMonitorWhenException(Map<String, Object> params) {
		String method = MonitorSetting.class.getName()+".queryMonitorWhenException";
		return this.getSqlSession().selectList(method, params);
	}
}
