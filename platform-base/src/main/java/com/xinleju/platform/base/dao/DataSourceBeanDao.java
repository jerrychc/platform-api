package com.xinleju.platform.base.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.vo.TendDataSourceVo;

public interface DataSourceBeanDao extends  BaseDao<String, TendDataSourceVo>{
	
	public List<TendDataSourceVo> getTendDataSourceVoList() ;
	
    /**
     * 
     * 获取租户信息
     * @param map
     * @return
     */
    public Map<String,Object> queryTendDemainInfo(Map map);
}