package com.xinleju.platform.ld.service.impl;

import com.xinleju.platform.base.utils.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.ld.dao.LandrayMeetingSummaryDao;
import com.xinleju.platform.ld.entity.LandrayMeetingSummary;
import com.xinleju.platform.ld.service.LandrayMeetingSummaryService;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

@Service
public class LandrayMeetingSummaryServiceImpl extends  BaseServiceImpl<String,LandrayMeetingSummary> implements LandrayMeetingSummaryService{
	

	@Autowired
	private LandrayMeetingSummaryDao landrayMeetingSummaryDao;

	@Override
	public Page getDataByPage(Map map) throws Exception {
		Page page=new Page();
		List<Map<String,Object>> list = landrayMeetingSummaryDao.getData(map);
		Integer count = landrayMeetingSummaryDao.getDataCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		return page;
	}

	@Override
	public List<LandrayMeetingSummary> portalList(Map map) throws Exception {
		return landrayMeetingSummaryDao.portalList(map);
	}
}
