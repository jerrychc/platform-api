package com.xinleju.platform.flow.dao;

import java.util.List;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.entity.Step;

/**
 * @author admin
 *
 */

public interface StepDao extends BaseDao<String, Step> {

	/**
	 * 查询业务变量所在表达式的流程模板
	 * 
	 * @param businessObjectId
	 * @param varCode
	 * @return
	 */
	public List<String> bizVarBeUsedInFlow(String businessObjectId, String varCode);

	public List<Step> queryStepsBy(String flId);
	
}
