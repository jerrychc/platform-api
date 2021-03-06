package com.xinleju.platform.flow.service;

import java.util.List;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.dto.InstanceTransitionRecordDto;
import com.xinleju.platform.flow.entity.InstanceTransitionRecord;

/**
 * @author admin
 * 
 * 
 */

public interface InstanceTransitionRecordService extends BaseService<String, InstanceTransitionRecord> {

	List<InstanceTransitionRecordDto> queryTransferList(String instanceId);

}
