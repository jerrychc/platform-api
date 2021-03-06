package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface InstanceOperateLogDtoServiceCustomer extends BaseDtoServiceCustomer{

	String deleteDataByParamMap(String userInfo, String paramaterJson);

	String changeToReadIntoHaveRead(String userInfo, String paramaterJson);

	String changeToDoIntoHaveDone(String userInfo, String paramaterJson);

	
	String batchSetOperateLogHaveDone(String userInfo, String paramaterJson);

	String batchDeleteOperateLog(String userJson, String paramaterJson);

	String queryRelatedInstanceListByKeyword(String userInfo, String paramaterJson);

}
