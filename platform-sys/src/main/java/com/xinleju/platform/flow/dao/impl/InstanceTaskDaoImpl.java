package com.xinleju.platform.flow.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.InstanceTaskDao;
import com.xinleju.platform.flow.dto.InstanceTaskDto;
import com.xinleju.platform.flow.entity.InstanceTask;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class InstanceTaskDaoImpl extends BaseDaoImpl<String,InstanceTask> implements InstanceTaskDao{

	public InstanceTaskDaoImpl() {
		super();
	}

	@Override
	public void completeTask(Map<String, Object> approvalInfos) {
		getSqlSession().update(InstanceTask.class.getName() + ".completeTask", approvalInfos);
	}

	@Override
	public List<InstanceTaskDto> queryCurrentPersonList(Map<String, String> paramMap) {
		return getSqlSession().selectList(InstanceTask.class.getName() + ".queryCurrentPersonList", paramMap);
	}

	@Override
	public int queryFinishedTaskCount(String instanceId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("instanceId", instanceId);
		int finishedCount = getSqlSession().selectOne(InstanceTask.class.getName() + ".queryFinishedTaskCount", params);
		return finishedCount;
	}

	@Override
	public void updateMsgId(Map<String, Object> params) {
		getSqlSession().update(InstanceTask.class.getName() + ".updateMsgId", params);
	}

	@Override
	public void updateComment(Map<String, Object> params) {
		getSqlSession().update(InstanceTask.class.getName() + ".updateComment", params);
	}

	@Override
	public List<InstanceTaskDto> queryTasksBy(String instanceId, String taskId) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("instanceId", instanceId);
		params.put("taskId", taskId);
		return getSqlSession().selectList(InstanceTask.class.getName() + ".queryTasksByInstanceId", params);
	}

	@Override
	public List<InstanceTaskDto> queryTaskIdByInstanceId(Map<String, String> paramMap) {
		String method = InstanceTask.class.getName() + ".queryTaskIdByInstanceId";
		return getSqlSession().selectList(method, paramMap);
	}

	@Override
	public Integer queryListCountByFiId(Map<String, Object> paramMap) {
		return getSqlSession().selectOne(InstanceTask.class.getName() + ".queryListCountByFiId", paramMap);
	}
}
