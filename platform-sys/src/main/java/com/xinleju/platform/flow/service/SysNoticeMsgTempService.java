package com.xinleju.platform.flow.service;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.flow.entity.SysNoticeMsgTemp;

import java.util.List;
import java.util.Map;

/**
 * Created by luoro on 2017/9/24.
 */
public interface SysNoticeMsgTempService extends BaseService<String,SysNoticeMsgTemp> {
    List<SysNoticeMsgTemp> queryMsgTempList(Map map);
}
