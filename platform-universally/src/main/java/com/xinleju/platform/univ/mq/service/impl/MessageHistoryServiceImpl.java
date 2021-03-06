package com.xinleju.platform.univ.mq.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.univ.mq.dao.MessageHistoryDao;
import com.xinleju.platform.univ.mq.entity.MessageHistory;
import com.xinleju.platform.univ.mq.service.MessageHistoryService;

/**
 * @author xubaoyong
 * 
 * 
 */

@Service
public class MessageHistoryServiceImpl extends  BaseServiceImpl<String,MessageHistory> implements MessageHistoryService{
	

	@Autowired
	private MessageHistoryDao mqMessageHistoryDao;
	

}
