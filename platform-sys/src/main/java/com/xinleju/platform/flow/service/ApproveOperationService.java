package com.xinleju.platform.flow.service;

import java.util.List;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.dto.ApproveOperationDto;
import com.xinleju.platform.flow.entity.ApproveOperation;

/**
 * @author admin
 * 
 * 
 */

public interface ApproveOperationService extends  BaseService <String,ApproveOperation>{

	List<ApproveOperationDto> queryObjectListByApproveId(String approveTypeId);

	int deletePseudoAllObjectByApproveTypeIds(String id);

	List<ApproveOperationDto> queryListByApproveRoleCode(String typeCode, String approveRole);

	
}
