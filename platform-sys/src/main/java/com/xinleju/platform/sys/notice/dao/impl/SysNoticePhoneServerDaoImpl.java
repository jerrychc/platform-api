package com.xinleju.platform.sys.notice.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.notice.dao.SysNoticePhoneServerDao;
import com.xinleju.platform.sys.notice.entity.SysNoticePhoneServer;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class SysNoticePhoneServerDaoImpl extends BaseDaoImpl<String,SysNoticePhoneServer> implements SysNoticePhoneServerDao{

	public SysNoticePhoneServerDaoImpl() {
		super();
	}

	@Override
	public List<Map<String, Object>> getPageData(Map<String, Object> map) {
		return  getSqlSession().selectList("com.xinleju.platform.sys.notice.entity.SysNoticePhoneServer.getPageData", map);
	}

	@Override
	public Integer getPageDataCount(Map<String, Object> map) {
		return  getSqlSession().selectOne("com.xinleju.platform.sys.notice.entity.SysNoticePhoneServer.getPageDataCount", map);
	}

	@Override
	public void disableAllData(Map<String, Object> map) {
		getSqlSession().selectOne("com.xinleju.platform.sys.notice.entity.SysNoticePhoneServer.disableAllData", map);
	}

	@Override
	public SysNoticePhoneServer getObjectByDefault(Map<String, Object> map) {
		return getSqlSession().selectOne("com.xinleju.platform.sys.notice.entity.SysNoticePhoneServer.getObjectByDefault", map);
	}
	
}
