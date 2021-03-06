package com.xinleju.platform.flow.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.flow.dao.CalendarDetailDao;
import com.xinleju.platform.flow.entity.CalendarBasic;
import com.xinleju.platform.flow.entity.CalendarDetail;
import com.xinleju.platform.flow.entity.InstanceStat;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class CalendarDetailDaoImpl extends BaseDaoImpl<String,CalendarDetail> implements CalendarDetailDao{

	public CalendarDetailDaoImpl() {
		super();
	}

	@Override
	public void deleteByParamMap(CalendarDetail detail) {
		String method = CalendarDetail.class.getName() + ".deleteByParamMap";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("year", detail.getYear());
		map.put("month", detail.getMonth());
		map.put("day", detail.getDay());
		getSqlSession().update(method, map);
		
	}

	@Override
	public void deleteDataForInitAction(Map<String, Integer> map) {
		String method = CalendarDetail.class.getName() + ".deleteDataForInitAction";
		getSqlSession().delete(method, map);
	}

	@Override
	public void setYearAsHolidayByParamMap(Map<String, Object> detailMap) {
		String method = CalendarDetail.class.getName() + ".setYearAsHolidayByParamMap";
		getSqlSession().update(method, detailMap);
	}
	
	@Override
	public void updateWorkDayByParamMap(Map<String, Object> detailMap) {
		String method = CalendarDetail.class.getName() + ".updateWorkDayByParamMap";
		getSqlSession().update(method, detailMap);
	}

	@Override
	public void updateDetailInfo(Map<String, Object> detailMap) {
		String method = CalendarDetail.class.getName() + ".updateDetailInfo";
		getSqlSession().update(method, detailMap);
	}
	@Override
	public List<Map<String, String>> selectAllDays(){
		String method = CalendarDetail.class.getName() + ".selectAllDays";
		return  getSqlSession().selectList(method);
	}
}
