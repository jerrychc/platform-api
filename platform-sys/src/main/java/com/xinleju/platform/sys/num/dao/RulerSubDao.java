package com.xinleju.platform.sys.num.dao;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.num.entity.RulerSub;

/**
 * @author admin
 *
 */

public interface RulerSubDao extends BaseDao<String, RulerSub> {

	public RulerSub getCurrentByRulerId(String rulerId);
	
	

}
