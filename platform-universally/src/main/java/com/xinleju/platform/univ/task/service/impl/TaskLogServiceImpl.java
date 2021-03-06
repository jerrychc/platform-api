package com.xinleju.platform.univ.task.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.univ.task.dao.TaskLogDao;
import com.xinleju.platform.univ.task.entity.TaskLog;
import com.xinleju.platform.univ.task.service.TaskLogService;

/**
 * @author admin
 * 
 * 
 */
@Transactional
@Service
public class TaskLogServiceImpl extends  BaseServiceImpl<String,TaskLog> implements TaskLogService{
	

	@Autowired
	private TaskLogDao taskLogDao;
	

}
