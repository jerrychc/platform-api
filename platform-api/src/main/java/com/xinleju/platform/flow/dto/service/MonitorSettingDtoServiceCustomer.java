package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface MonitorSettingDtoServiceCustomer extends BaseDtoServiceCustomer{

	String saveAllSettingData(String userInfo, String saveJson);

	String changeStatus(String userInfo, String string);

}
