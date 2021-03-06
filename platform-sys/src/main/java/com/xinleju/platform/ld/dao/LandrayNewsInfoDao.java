package com.xinleju.platform.ld.dao;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.ld.entity.LandrayNewsInfo;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 *
 */

public interface LandrayNewsInfoDao extends BaseDao<String, LandrayNewsInfo> {


    List<Map<String,Object>> getBillData(Map map);

    Integer getBillDataCount(Map map);

    List<LandrayNewsInfo> portalList(Map map);
}
