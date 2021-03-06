package com.xinleju.platform.univ.task.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.univ.task.dao.TaskInfoDao;
import com.xinleju.platform.univ.task.entity.TaskInfo;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class TaskInfoDaoImpl extends BaseDaoImpl<String,TaskInfo> implements TaskInfoDao{

	public TaskInfoDaoImpl() {
		super();
	}

	@Override
	public String getDataBaseKey(String sql, Map<String, Object> params) {
		String result = getSqlSession().selectOne(sql, params);
		return result;
	}

	
	
}
