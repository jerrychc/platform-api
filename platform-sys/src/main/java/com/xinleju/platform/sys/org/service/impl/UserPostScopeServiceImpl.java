package com.xinleju.platform.sys.org.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.org.dao.UserPostScopeDao;
import com.xinleju.platform.sys.org.entity.UserPostScope;
import com.xinleju.platform.sys.org.service.UserPostScopeService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class UserPostScopeServiceImpl extends  BaseServiceImpl<String,UserPostScope> implements UserPostScopeService{
	

	@Autowired
	private UserPostScopeDao userPostScopeDao;
	
	@Override
	public List<Map<String,Object>> queryUserPostScopeList(Map<String, Object> paramater)  throws Exception{
		return userPostScopeDao.queryUserPostScopeList(paramater);
	}

}
