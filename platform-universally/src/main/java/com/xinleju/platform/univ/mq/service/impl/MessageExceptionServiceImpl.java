package com.xinleju.platform.univ.mq.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.univ.mq.dao.MessageExceptionDao;
import com.xinleju.platform.univ.mq.entity.MessageException;
import com.xinleju.platform.univ.mq.service.MessageExceptionService;

/**
 * @author xubaoyong
 * 
 * 
 */

@Service
public class MessageExceptionServiceImpl extends  BaseServiceImpl<String,MessageException> implements MessageExceptionService{
	

	@Autowired
	private MessageExceptionDao messageExceptionDao;
	

}
