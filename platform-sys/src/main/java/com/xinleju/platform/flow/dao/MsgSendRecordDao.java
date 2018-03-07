package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.entity.MsgSendRecord;

/**
 * @author admin
 *
 */

public interface MsgSendRecordDao extends BaseDao<String, MsgSendRecord> {

	List<String> queryMsgIdList(Map<String, Object> queryMap);
	
	

}
