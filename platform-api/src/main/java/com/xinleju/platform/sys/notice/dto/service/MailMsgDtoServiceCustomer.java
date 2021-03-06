package com.xinleju.platform.sys.notice.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface MailMsgDtoServiceCustomer extends BaseDtoServiceCustomer{
	/**
	 * 发送邮件
	 * @return
	 */
	public String sendMailMsg(String userInfo, String paramater);
	/**
	 * 模糊查询-分页
	 * @return
	 */
	public String vaguePage(String userInfo, String paramater);
}
