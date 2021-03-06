package com.xinleju.platform.flow.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.dto.MonitorSettingDto;
import com.xinleju.platform.flow.entity.MonitorSetting;
import com.xinleju.platform.flow.model.FlowMonitorBean;

/**
 * @author admin
 * 
 * 
 */

public interface MonitorSettingService extends  BaseService <String,MonitorSetting>{

	void saveAllSettingData(MonitorSettingDto monitorSettingDto) throws Exception ;

	int changeStatusBySettingId(String settingId, String status) throws Exception;

	/**
	 * 查询指定ID的监控者集合
	 * @param settingId
	 * @return
	 */
	List<Map<String, String>> queryMonitorList(String settingId);

	/**
	 * 查询指定ID的被监控者集合
	 * @param settingId
	 * @return
	 */
	List<Map<String, String>> queryMonitoredList(String settingId);

	/**
	 * 查询指定ID的模板集合
	 * @param settingId
	 * @return
	 */
	List<Map<String, String>> queryFlowList(String settingId);

	/**
	 * 查询指定ID的监控点集合
	 * @param settingId
	 * @return
	 */
	List<Map<String, String>> queryPointList(String settingId);
	
	/**
	 * 根据被监控人ID及监控点类型查询监控配置，包括监控人及处理方式(不去重)
	 * @param monitoredId
	 * @param monitoryPointType
	 * @return
	 */
	List<FlowMonitorBean> queryMonitorByUser(String monitoredId, String postId, String monitoryPointType);
	
	/**
	 * 根据流程模板ID及监控点类型查询监控配置(不去重)
	 * @param flId
	 * @param monitoryPointType
	 * @return
	 */
	List<FlowMonitorBean> queryMonitorByFlow(String flId, String monitoryPointType);
	
	/**
	 * 查询指定模板的异常监控配置
	 * @param flId
	 * @return
	 */
	List<FlowMonitorBean> queryMonitorWhenException(String monitoryPointType);
}
