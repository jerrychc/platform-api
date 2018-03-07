package com.xinleju.platform.base.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.dao.DataSourceBeanDao;
import com.xinleju.platform.base.service.DataSourceBeanService;
import com.xinleju.platform.base.vo.TendDataSourceVo;

@Service
public class DataSourceBeanServiceImpl extends  BaseServiceImpl<String,TendDataSourceVo> implements DataSourceBeanService{
	private static Logger log = Logger.getLogger(DataSourceBeanServiceImpl.class);

	@Autowired
	private DataSourceBeanDao dataSourceBeanDao;
	@Override
	public List<TendDataSourceVo> getTendDataSourceVoList() {
		// TODO Auto-generated method stub
		return dataSourceBeanDao.getTendDataSourceVoList();
	}
	@Override
	public Map<String, Object> queryTendDemainInfo(Map map) {
		// TODO Auto-generated method stub
		return dataSourceBeanDao.queryTendDemainInfo(map);
	}
}
