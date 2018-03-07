package com.xinleju.platform.sys.notice.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.notice.dao.MailMsgHistoryDao;
import com.xinleju.platform.sys.notice.entity.MailMsgHistory;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class MailMsgHistoryDaoImpl extends BaseDaoImpl<String,MailMsgHistory> implements MailMsgHistoryDao{

	public MailMsgHistoryDaoImpl() {
		super();
	}

	
	
}
