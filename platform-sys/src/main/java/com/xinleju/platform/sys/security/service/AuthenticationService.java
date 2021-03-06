package com.xinleju.platform.sys.security.service;

import java.util.List;

import com.xinleju.platform.sys.org.entity.User;

public interface AuthenticationService {
	
	/**
	 * 用户登录
	 * @param map(资源ID)
	 * @return
	 */
	public List<User> login(User user) throws Exception;
	
}
