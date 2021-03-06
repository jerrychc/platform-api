package com.xinleju.platform.sys.num.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.num.entity.Ruler;

/**
 * @author admin
 *
 */

public interface RulerDao extends BaseDao<String, Ruler> {

	public List<Map<String, Object>> getMapListByBillId(String paramater);

	public List<Map<String, Object>> getRulerSortNum(Map<String,Object> paramater);
	
	
	public List<Ruler> getRuleListByBillId(String paramater);
	
	

}
