package com.xinleju.platform.sys.org.dao;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.org.entity.User;
import com.xinleju.platform.sys.org.entity.UserTrial;

import java.util.List;
import java.util.Map;

/**
 * @author sy
 *
 */

public interface UserTrialDao extends BaseDao<String, UserTrial> {


	UserTrial isExistTrialOrgUser(Map map);

	User obtainOrgUserTrial(Map map);
}
