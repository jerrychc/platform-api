package com.xinleju.platform.flow.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.flow.dao.OverdueNoticeExceptionDao;
import com.xinleju.platform.flow.entity.OverdueNoticeException;
import com.xinleju.platform.flow.service.OverdueNoticeExceptionService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class OverdueNoticeExceptionServiceImpl extends  BaseServiceImpl<String,OverdueNoticeException> implements OverdueNoticeExceptionService{
	

	@Autowired
	private OverdueNoticeExceptionDao overdueNoticeExceptionDao;
	

}