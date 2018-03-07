package com.xinleju.platform.base.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.vo.TendDataSourceVo;

public interface DataSourceBeanService extends BaseService<String,TendDataSourceVo> {

	
    /**
     * 
     * 数据源信息
     * @return
     */
    public List<TendDataSourceVo>  getTendDataSourceVoList();
    
    
    /**
     * 
     * 获取租户信息
     * @param map
     * @return
     */
    public Map<String,Object> queryTendDemainInfo(Map map);
 }
