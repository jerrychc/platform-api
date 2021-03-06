package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.dto.InstanceDto;
import com.xinleju.platform.flow.entity.InstanceOperateLog;

/**
 * @author admin
 *
 */

public interface InstanceOperateLogDao extends BaseDao<String, InstanceOperateLog> {

	int deleteDataByParamMap(Map<String, String> paramMap);

	int deleteOperateLogBySpecialAction(Map<String, String> paramMap);

	List<InstanceOperateLog> queryLogListByParamMap(Map<String, String> paramMap);

	List<InstanceDto> queryRelatedInstanceListByKeyword(Map<String, Object> paramMap);

	Integer queryRelatedInstanceCountByKeyword(Map<String, Object> map);
	
	

}
