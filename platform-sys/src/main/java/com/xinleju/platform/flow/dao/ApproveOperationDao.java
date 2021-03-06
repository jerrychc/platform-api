package com.xinleju.platform.flow.dao;

import java.util.List;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.dto.ApproveOperationDto;
import com.xinleju.platform.flow.entity.ApproveOperation;

/**
 * @author admin
 *
 */

public interface ApproveOperationDao extends BaseDao<String, ApproveOperation> {

	int deletePseudoAllObjectByApproveTypeIds(String approveId);

	List<ApproveOperationDto> queryListByApproveRoleCode(String typeCode, String approveRole);
	
}
