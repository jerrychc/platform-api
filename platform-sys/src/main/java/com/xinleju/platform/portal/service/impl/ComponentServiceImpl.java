package com.xinleju.platform.portal.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.portal.dao.ComponentDao;
import com.xinleju.platform.portal.entity.Component;
import com.xinleju.platform.portal.service.ComponentService;

/**
 * @author admin
 */

@Service
public class ComponentServiceImpl extends BaseServiceImpl<String, Component> implements ComponentService {


    @Autowired
    private ComponentDao componentDao;

    /**
     * searchType 查询类型 0 全量查询， 1：模糊查询
     */
    @Override
    public Page getSearchPage(Map map) {
        Page page = componentDao.queryObjectsByPage(map);
        return page;
    }

    /**
     * 校验唯一性 根据编码
     */
    @Override
    public Component getComponentBySerialNo(Component param) {
        return componentDao.getComponentBySerialNo(param);
    }

    @Override
    public List<Map<String, Object>> queryAllList() {
        return componentDao.queryAllList();
    }
}
