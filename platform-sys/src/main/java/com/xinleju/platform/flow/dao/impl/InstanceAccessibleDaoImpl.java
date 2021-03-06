package com.xinleju.platform.flow.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.InstanceAccessibleDao;
import com.xinleju.platform.flow.dto.InstanceAccessibleDto;
import com.xinleju.platform.flow.entity.InstanceAccessible;

/**
 * @author admin
 * 
 */

@Repository
public class InstanceAccessibleDaoImpl extends BaseDaoImpl<String,InstanceAccessible> implements InstanceAccessibleDao{

	public InstanceAccessibleDaoImpl() {
		super();
	}

	@Override
	public void deleteByInstanceId(String instanceId) {
		Map<String, String> parameter = new HashMap<String, String>();
		parameter.put("instanceId", instanceId);
		getSqlSession().update(InstanceAccessible.class.getName()  + ".deleteByInstanceId", parameter);
	}

	@Override
	public void deleteReaderDataByParamMap(Map<String, Object> paramMap) {
		String method = InstanceAccessible.class.getName()  + ".deleteReaderDataByParamMap";
		getSqlSession().update(method, paramMap);
	}

	@Override
	public List<InstanceAccessibleDto> queryInstanceReaderList(Map<String, Object> paramMap) {
		String method = InstanceAccessible.class.getName()  + ".queryInstanceReaderList";
		return getSqlSession().selectList(method, paramMap);
	}
}
