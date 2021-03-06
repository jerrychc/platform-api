package com.xinleju.platform.flow.dao;

import java.util.List;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.dto.InstanceTransitionRecordDto;
import com.xinleju.platform.flow.entity.InstanceTransitionRecord;

/**
 * @author admin
 *
 */

public interface InstanceTransitionRecordDao extends BaseDao<String, InstanceTransitionRecord> {

	List<InstanceTransitionRecordDto> queryTransferList(String instanceId);
	

}
