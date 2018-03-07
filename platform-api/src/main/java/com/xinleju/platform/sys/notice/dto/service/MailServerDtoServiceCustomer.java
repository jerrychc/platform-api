package com.xinleju.platform.sys.notice.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface MailServerDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * 模糊查询
	 * @param userinfo(用户信息) param 
	 * @return
	 */
	public String queryListLike(String userinfo, String paramaterJson);
}
