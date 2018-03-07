package com.xinleju.platform.sys.org.service.impl;

import com.xinleju.platform.base.annotation.Description;
import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.*;
import com.xinleju.platform.sys.org.dao.UserTrialDao;
import com.xinleju.platform.sys.org.entity.User;
import com.xinleju.platform.sys.org.entity.UserTrial;
import com.xinleju.platform.sys.org.service.UserTrialService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luorx
 * 
 * 
 */

@Service
public class UserTrialServiceImpl extends  BaseServiceImpl<String,UserTrial> implements UserTrialService {
	

	@Autowired
	private UserTrialDao userTrialDao;

	@Override
	public UserTrial isExistTrialOrgUser(Map map) {
		return userTrialDao.isExistTrialOrgUser(map);
	}

	@Override
	public User obtainOrgUserTrial(Map map) {
		return userTrialDao.obtainOrgUserTrial(map);
	}
}