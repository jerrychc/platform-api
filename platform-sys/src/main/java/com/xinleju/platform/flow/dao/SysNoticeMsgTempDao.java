package com.xinleju.platform.flow.dao;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.flow.dto.SysNoticeMsgDto;
import com.xinleju.platform.flow.dto.SysNoticeMsgStatDto;
import com.xinleju.platform.flow.entity.SysNoticeMsg;
import com.xinleju.platform.flow.entity.SysNoticeMsgTemp;

import java.util.List;
import java.util.Map;

/**
 * @author admin
 *
 */

public interface SysNoticeMsgTempDao extends BaseDao<String, SysNoticeMsgTemp> {

    List<SysNoticeMsgTemp> queryMsgTempList(Map map);
}
