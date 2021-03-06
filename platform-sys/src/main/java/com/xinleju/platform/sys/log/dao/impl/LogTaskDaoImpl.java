package com.xinleju.platform.sys.log.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xinleju.platform.base.dao.impl.BaseDaoImpl;
import com.xinleju.platform.sys.log.dao.LogTaskDao;
import com.xinleju.platform.sys.log.entity.LogTask;

/**
 * @author admin
 * 
 * 
 */

@Repository
public class LogTaskDaoImpl extends BaseDaoImpl<String,LogTask> implements LogTaskDao{

	public LogTaskDaoImpl() {
		super();
	}

	/**
	 * 分页数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<LogTask> getPageData(Map<String, Object> map){
		return  getSqlSession().selectList("com.xinleju.platform.sys.log.entity.LogTask.getPageData", map);
	}
	/**
	 * 总条数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer getPageDataCount(Map<String, Object> map){
		return  getSqlSession().selectOne("com.xinleju.platform.sys.log.entity.LogTask.getPageDataCount", map);
	}
	
}
