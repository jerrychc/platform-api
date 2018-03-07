package com.xinleju.platform.sys.base.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.base.entity.CommonDraft;

/**
 * @author admin
 *
 */

public interface CommonDraftDao extends BaseDao<String, CommonDraft> {

	List<Map<String, Object>> getMyFormPageSort(Map map);

	Integer getMyFormPageCount(Map map);
	
	

}
