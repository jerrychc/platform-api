package com.xinleju.platform.sys.notice.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface SysNoticePhoneMsgDtoServiceCustomer extends BaseDtoServiceCustomer{
	
	String vaguePage(String userJson, String paramaterJson);

	String sendMsg(String userJson, String paramaterJson);

	String sendMsgTest(String userJson, String paramaterJson);
	
	String againSendMsg(String userJson, String paramaterJson);
}
