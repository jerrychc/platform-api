package com.xinleju.platform.sys.base.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dao.CustomFormVersionHistoryDao;
import com.xinleju.platform.sys.base.entity.CustomFormVersionHistory;
import com.xinleju.platform.sys.base.service.CustomFormVersionHistoryService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class CustomFormVersionHistoryServiceImpl extends  BaseServiceImpl<String,CustomFormVersionHistory> implements CustomFormVersionHistoryService{
	

	@Autowired
	private CustomFormVersionHistoryDao customFormVersionHistoryDao;

	@Override
	public Page getPageSort(String userInfo, Map map) {
		// 分页显示
		Page page=new Page();
		// 获取分页list 数据
		List<CustomFormVersionHistory> list = customFormVersionHistoryDao.getPageSort(map);
		// 获取条件的总数据量
		Integer count = customFormVersionHistoryDao.getPageSortCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		//封装成page对象 传到前台
		return page;
	}
	

}
