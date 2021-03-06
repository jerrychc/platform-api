package com.xinleju.platform.sys.log.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.log.dao.LogDubboDao;
import com.xinleju.platform.sys.log.entity.LogDubbo;
import com.xinleju.platform.sys.log.entity.LogOperation;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class LogDubboDaoImpl extends BaseDaoImpl<String,LogDubbo> implements LogDubboDao{

	public LogDubboDaoImpl() {
		super();
	}

	/**
	 * 分页数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<LogDubbo> getPageData(Map<String, Object> map){
		return  getSqlSession().selectList("com.xinleju.platform.sys.log.entity.LogDubbo.getPageData", map);
	}
	/**
	 * 总条数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer getPageDataCount(Map<String, Object> map){
		return  getSqlSession().selectOne("com.xinleju.platform.sys.log.entity.LogDubbo.getPageDataCount", map);
	}
	
	
}
