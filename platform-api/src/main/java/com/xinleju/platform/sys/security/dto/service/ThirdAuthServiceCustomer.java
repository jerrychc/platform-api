package com.xinleju.platform.sys.security.dto.service;

public interface ThirdAuthServiceCustomer {
	
	/**
	 * 更新第三方授权信息
	 * @param userInfo
	 * @param paramater
	 * @return
     */
	public void resetThirdUserAuthData(String userInfo, String paramater)throws Exception;
}

