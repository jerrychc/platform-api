package com.xinleju.platform.ld.dao;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.ld.entity.LandrayMeetingSummary;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 *
 */

public interface LandrayMeetingSummaryDao extends BaseDao<String, LandrayMeetingSummary> {


    List<Map<String,Object>> getData(Map map);

    Integer getDataCount(Map map);

    List<LandrayMeetingSummary> portalList(Map map);
}
