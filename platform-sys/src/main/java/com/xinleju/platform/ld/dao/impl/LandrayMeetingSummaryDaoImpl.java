package com.xinleju.platform.ld.dao.impl;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.ld.dao.LandrayMeetingSummaryDao;
import com.xinleju.platform.ld.entity.LandrayMeetingSummary;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class LandrayMeetingSummaryDaoImpl extends BaseDaoImpl<String,LandrayMeetingSummary> implements LandrayMeetingSummaryDao{

	public LandrayMeetingSummaryDaoImpl() {
		super();
	}


	@Override
	public List<Map<String, Object>> getData(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.ld.entity.LandrayMeetingSummary.getData", map);
	}

	@Override
	public Integer getDataCount(Map map) {
		return  getSqlSession().selectOne("com.xinleju.platform.ld.entity.LandrayMeetingSummary.getDataCount", map);
	}

	@Override
	public List<LandrayMeetingSummary> portalList(Map map) {
		return  getSqlSession().selectList("com.xinleju.platform.ld.entity.LandrayMeetingSummary.getPortalData", map);
	}
}
