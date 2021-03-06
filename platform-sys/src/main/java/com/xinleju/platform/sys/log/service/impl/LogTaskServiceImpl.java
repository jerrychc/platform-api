package com.xinleju.platform.sys.log.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.log.dao.LogTaskDao;
import com.xinleju.platform.sys.log.entity.LogDubbo;
import com.xinleju.platform.sys.log.entity.LogTask;
import com.xinleju.platform.sys.log.service.LogTaskService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class LogTaskServiceImpl extends  BaseServiceImpl<String,LogTask> implements LogTaskService{
	

	@Autowired
	private LogTaskDao logTaskDao;
	/**
	 * 模糊查询-分页
	 * @return
	 */
	public Page vaguePage(Map<String, Object> map)throws Exception{
		Page page=new Page();
		List<LogTask> list = logTaskDao.getPageData(map);
		Integer count = logTaskDao.getPageDataCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		return page;
	}

}
