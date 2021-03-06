package com.xinleju.platform.flow.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.ApproveOperationDao;
import com.xinleju.platform.flow.dto.ApproveOperationDto;
import com.xinleju.platform.flow.entity.ApproveOperation;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class ApproveOperationDaoImpl extends BaseDaoImpl<String,ApproveOperation> implements ApproveOperationDao{

	public ApproveOperationDaoImpl() {
		super();
	}

	@Override
	public int deletePseudoAllObjectByApproveTypeIds(String approveId) {
		String objectName = "com.xinleju.platform.flow.entity.ApproveOperation.deletePseudoAllObjectByApproveTypeIds";
		//"com.xinleju.platform.sys.res.entity.DataPoint.queryDataPointList"
		return getSqlSession().update(objectName, approveId);
	}

	@Override
	public List<ApproveOperationDto> queryListByApproveRoleCode(String typeCode, String approveRole) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("typeCode", typeCode);
		params.put("approveRole", approveRole);
		String objectName = "com.xinleju.platform.flow.entity.ApproveOperation.queryListByApproveRoleCode";
		return getSqlSession().selectList(objectName, params);
	}

	
	
}
