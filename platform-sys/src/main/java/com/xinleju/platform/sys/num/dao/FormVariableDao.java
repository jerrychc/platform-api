package com.xinleju.platform.sys.num.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.num.entity.FormVariable;

/**
 * @author admin
 *
 */

public interface FormVariableDao extends BaseDao<String, FormVariable> {

	List<Map<String, Object>> getFormVariableData(Map<String, Object> map);

	Integer getFormVariableDataCount(Map<String, Object> map);
	
	

}
