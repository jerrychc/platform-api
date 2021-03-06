package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.dto.InstanceTaskDto;
import com.xinleju.platform.flow.entity.InstanceTask;

/**
 * @author admin
 *
 */

public interface InstanceTaskDao extends BaseDao<String, InstanceTask> {

	void completeTask(Map<String, Object> approvalInfos);

	List<InstanceTaskDto> queryCurrentPersonList(Map<String, String> paramMap);

	int queryFinishedTaskCount(String instanceId);

	void updateMsgId(Map<String, Object> params);

	void updateComment(Map<String, Object> params);

	/**
	 * 根据流程实例ID查询任务
	 * 
	 * @param instanceId
	 * @return
	 */
	List<InstanceTaskDto> queryTasksBy(String instanceId, String taskId);

	List<InstanceTaskDto> queryTaskIdByInstanceId(Map<String, String> paramMap);

	/**
	  * @Description:查询同一流程同一个审批人有多个任务处于审批中
	  * @author:zhangfangzhi
	  * @date 2018年1月16日 下午4:43:13
	  * @version V1.0
	 */
	Integer queryListCountByFiId(Map<String, Object> paramMap);

}
