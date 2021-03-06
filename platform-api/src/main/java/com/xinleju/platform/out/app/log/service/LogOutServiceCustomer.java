package com.xinleju.platform.out.app.log.service;

public interface LogOutServiceCustomer {

	/**
	 * dubbo服务记录日志 
	 * @param userJson
	 * @param paramaterJson(json格式Map:{ sysCode=系统Code ,
	  									dubboMethod=调用方法,
									 	startTime=开始时间（Timestamp）,
									  	endTime=结束时间（Timestamp）,
	  									executeTime=耗时(Long),
	  									resFlag=运行结果（0失败，1成功）,
	 									info=信息描述,
	 									returnContent=返回内容})
	 * @return json格式 DubboServiceResultInfo
	 * @author gyh
	 */
	String saveDubboLog(String userJson, String paramaterJson);
	
	
	/**
	 * 操作业务日志 
	 * @param userJson
	 * @param paramaterJson(json格式Map:{ sysCode=系统Code ,
	  									loginIp=用户ip,
									 	loginBrowser=用户浏览器,
									  	node=操作内容,
	  									note=操作结果})
	 * @return json格式 DubboServiceResultInfo
	 * @author gyh
	 */
	String saveOpeLog(String userJson, String paramaterJson);
	/**
	 * 自动任务日志 
	 * @param userJson
	 * @param paramaterJson(json格式Map:{ sysCode=系统Code ,
	  									taskCode=自动任务编码,
									 	taskName=自动任务名称,
									  	startTime=开始时间（Timestamp）,
									  	endTime=结束时间（Timestamp）,
									  	executeTime=耗时(Long),
									  	executeStatus=是否成功（ture是，false否）
	  									runInfo=执行信息描述})
	 * @return json格式 DubboServiceResultInfo
	 * @author gyh
	 */
	String saveTaskLog(String userJson, String paramaterJson);
}
