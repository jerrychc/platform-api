package com.xinleju.platform.ld.service;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.ld.entity.LandrayNewsInfo;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

public interface LandrayNewsInfoService extends  BaseService <String,LandrayNewsInfo>{

    Page getNewsDataByPage(Map map)throws Exception;

    List<LandrayNewsInfo> portalList(Map map)throws Exception;
}
