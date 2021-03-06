package com.xinleju.platform.sys.log.service.impl;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.sys.log.dto.LogOperationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.log.dao.LogOperationDao;
import com.xinleju.platform.sys.log.entity.LogOperation;
import com.xinleju.platform.sys.log.service.LogOperationService;
import com.xinleju.platform.sys.notice.entity.MailMsg;

/**
 * @author admin
 * 
 * 
 */

@Service
public class LogOperationServiceImpl extends  BaseServiceImpl<String,LogOperation> implements LogOperationService{
	

	@Autowired
	private LogOperationDao logOperationDao;
	/**
	 * 模糊查询-分页
	 * @return
	 */
	public Page vaguePage(Map<String, Object> map)throws Exception{
		Page page=new Page();
		List<Map<String,String>> list = logOperationDao.getPageData(map);
		Integer count = logOperationDao.getPageDataCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		return page;
	}


}
