package com.xinleju.platform.univ.task.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.datasource.DataSourceContextHolder;
import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.LoginUtils;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.univ.task.dao.TaskInfoDao;
import com.xinleju.platform.univ.task.entity.TaskInfo;
import com.xinleju.platform.univ.task.service.TaskInfoService;
import com.xinleju.platform.univ.task.utils.QuartzManager;

/**
 * @author admin
 * 
 * 
 */
@Transactional
@Service
public class TaskInfoServiceImpl extends  BaseServiceImpl<String,TaskInfo> implements TaskInfoService{
	

	@Autowired
	private TaskInfoDao taskInfoDao;

	@SuppressWarnings("unchecked")
	@Override
	public Page getPage(Map<String, Object> paramater, Integer start, Integer pageSize) throws Exception {
		Page page = taskInfoDao.getPage(paramater, start, pageSize);
		QuartzManager.getBatchNextFireTime(page.getList());
		return page;
	}

	@Override
	public String getDatabaseKey(String tendId) throws Exception {
		Map<String, Object> params = new HashMap<>();
		params.put("tendId", tendId);
		String oldDataSource = DataSourceContextHolder.getDataSourceType();
		DataSourceContextHolder.clearDataSourceType();
		String database = taskInfoDao.getDataBaseKey(TaskInfo.class.getName() + ".getDatabaseKey", params);
		DataSourceContextHolder.setDataSourceType(oldDataSource);
		return database;
	}

	@Transactional
	@Override
	public int save(TaskInfo task) throws Exception {
		String tendCode = LoginUtils.getSecurityUserBeanInfo().getTendCode();
		QuartzManager.addTask(task, tendCode);
		int count = taskInfoDao.save(task);
		return count;
	}

	@Transactional
	@Override
	public int update(TaskInfo task) throws Exception {
		String tendCode = LoginUtils.getSecurityUserBeanInfo().getTendCode();
		TaskInfo oldTask = taskInfoDao.getObjectById(task.getId());
		// 先删除旧任务，再添加任务
		QuartzManager.deleteTask(oldTask, tendCode);
		QuartzManager.addTask(task, tendCode);
		int count = taskInfoDao.update(task);
		return count;
	}

	@Override
	public int deleteObjectById(String id) throws Exception {
		TaskInfo task = taskInfoDao.getObjectById(id);
		int count = taskInfoDao.deleteObjectById(id);
		String tendCode = LoginUtils.getSecurityUserBeanInfo().getTendCode();
		QuartzManager.deleteTask(task, tendCode);
		return count;
	}

	@Override
	public int deleteAllObjectByIds(List<String> ids) throws Exception {
		String tendCode = LoginUtils.getSecurityUserBeanInfo().getTendCode();
		int count = 0;
		TaskInfo task = null;
		for (String id : ids) {
			task = taskInfoDao.getObjectById(id);
			count += taskInfoDao.deleteObjectById(id);
			QuartzManager.deleteTask(task, tendCode);
		}
		return count;
	}

	@Override
	public int updateTaskState(List<String> list, Map<String, Object> map) throws Exception  {
		Map<String, Object> param = new HashMap<>();
		param.put("idsList", list);
		List<TaskInfo> taskList = taskInfoDao.queryList(TaskInfo.class.getName() + ".queryListByIds", param);
		Boolean active = (Boolean) map.get("active");
		String tendCode = LoginUtils.getSecurityUserBeanInfo().getTendCode();
		for (TaskInfo taskInfo : taskList) {
			taskInfo.setActive(active);
			if (active) {
				QuartzManager.resumeTask(taskInfo, tendCode);
			} else {
				QuartzManager.pauseTask(taskInfo, tendCode);
			}
			taskInfoDao.update(taskInfo);
		}
		return 0;
	}

	@Override
	public int executeNow(List<String> list) throws Exception {
		Map<String, Object> param = new HashMap<>();
		param.put("idsList", list);
		List<TaskInfo> taskList = taskInfoDao.queryList(TaskInfo.class.getName() + ".queryListByIds", param);
		String tendCode = LoginUtils.getSecurityUserBeanInfo().getTendCode();
		for (TaskInfo taskInfo : taskList) {
			QuartzManager.triggerTask(taskInfo, tendCode);
		}
		return 0;
	}

}
