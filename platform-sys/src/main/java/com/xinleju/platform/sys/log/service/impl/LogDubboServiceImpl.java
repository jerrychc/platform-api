package com.xinleju.platform.sys.log.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.log.dao.LogDubboDao;
import com.xinleju.platform.sys.log.entity.LogDubbo;
import com.xinleju.platform.sys.log.entity.LogOperation;
import com.xinleju.platform.sys.log.service.LogDubboService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class LogDubboServiceImpl extends  BaseServiceImpl<String,LogDubbo> implements LogDubboService{
	

	@Autowired
	private LogDubboDao logDubboDao;
	/**
	 * 模糊查询-分页
	 * @return
	 */
	public Page vaguePage(Map<String, Object> map)throws Exception{
		Page page=new Page();
		List<LogDubbo> list = logDubboDao.getPageData(map);
		Integer count = logDubboDao.getPageDataCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		return page;
	}

}
