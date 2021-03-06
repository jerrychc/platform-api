package com.xinleju.platform.flow.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.MsgSendRecordDao;
import com.xinleju.platform.flow.entity.MsgSendRecord;
import com.xinleju.platform.flow.service.MsgSendRecordService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class MsgSendRecordServiceImpl extends  BaseServiceImpl<String,MsgSendRecord> implements MsgSendRecordService{
	

	@Autowired
	private MsgSendRecordDao msgSendRecordDao;

	@Override
	public List<String> queryMsgIdList(Map<String, Object> queryMap) {
		return msgSendRecordDao.queryMsgIdList(queryMap);
	}
	

}
