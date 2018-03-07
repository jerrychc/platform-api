package com.xinleju.platform.base.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.DataSourceBeanDao;
import com.xinleju.platform.base.vo.TendDataSourceVo;

@Repository
public class DataSourceBeanDaoImpl extends BaseDaoImpl<String,TendDataSourceVo> implements DataSourceBeanDao {
	 private static Logger log = Logger.getLogger(DataSourceBeanDaoImpl.class);
	 @Override
	 public List<TendDataSourceVo> getTendDataSourceVoList() {
		// TODO Auto-generated method stub
		return getSqlSession().selectList("com.xinleju.platform.base.vo.TendDataSourceVo.queryList");
	}
	@Override
	public Map<String, Object> queryTendDemainInfo(Map map) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne("com.xinleju.platform.base.vo.TendDataSourceVo.queryTendInfo",map);
	}
}
