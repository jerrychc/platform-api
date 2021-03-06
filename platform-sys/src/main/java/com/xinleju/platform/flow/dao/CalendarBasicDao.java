package com.xinleju.platform.flow.dao;

import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.entity.CalendarBasic;

/**
 * @author admin
 *
 */

public interface CalendarBasicDao extends BaseDao<String, CalendarBasic> {

	void deleteByParamMap(CalendarBasic calendarBasic);

	void deleteDataForInitAction(Map<String, Integer> map);

	void upateBasicInfoByParamMap(Map<String, Object> map);
	
	

}
