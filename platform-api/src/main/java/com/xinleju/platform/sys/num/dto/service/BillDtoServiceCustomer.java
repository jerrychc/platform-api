package com.xinleju.platform.sys.num.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface BillDtoServiceCustomer extends BaseDtoServiceCustomer{

	String updateStatus(String userinfo, String string);

	String saveBillAndRuler(String userJson, String saveJson);

}
