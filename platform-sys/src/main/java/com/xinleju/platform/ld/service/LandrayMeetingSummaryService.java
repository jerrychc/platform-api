package com.xinleju.platform.ld.service;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.ld.entity.LandrayMeetingSummary;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

public interface LandrayMeetingSummaryService extends  BaseService <String,LandrayMeetingSummary>{

    Page getDataByPage(Map map)throws Exception;

    List<LandrayMeetingSummary> portalList(Map map)throws Exception;
}
