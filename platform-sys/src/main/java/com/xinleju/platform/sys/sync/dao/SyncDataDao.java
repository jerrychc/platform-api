package com.xinleju.platform.sys.sync.dao;

import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.sync.entity.SyncData;

/**
 * @author admin
 *
 */

public interface SyncDataDao extends BaseDao<String, SyncData> {
	
	Page getBeanPage(Map<String, Object> map);

}
