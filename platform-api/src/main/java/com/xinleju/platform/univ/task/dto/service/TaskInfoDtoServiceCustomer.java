package com.xinleju.platform.univ.task.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface TaskInfoDtoServiceCustomer  extends BaseDtoServiceCustomer{

	/**
	 * 暂停、恢复任务操作
	 * 
	 * @param userInfo
	 * @param ids
	 * @param updateMapJson
	 * @return
	 */
	String updateTaskState(String userInfo, String ids, String updateMapJson);

	/**
	 * 立即执行任务操作
	 * @param userInfo
	 * @param ids
	 * @return
	 */
	String executeNow(String userInfo, String ids);
	
	String taskTest(String userInfo, String parameter);

	/**
	 * 为租户系统提供初始化数据库是quartz初始定时任务
	 * @param userInfo
	 * @param paramJson
	 * @return
     */
	String initQuartzJob(String userInfo,String paramJson);
}
