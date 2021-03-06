package com.xinleju.platform.univ.search.service;

import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.univ.search.entity.SearchProperty;

/**
 * @author haoqp
 * 
 * 
 */

public interface SearchPropertyService extends  BaseService <String,SearchProperty>{

	/**
	 * 查询记录条数，用于更新时验证名称编码重复
	 * 
	 * @param paramMap
	 * @return
	 */
	int getCountForUpdate(Map<String, Object> paramMap);

	
}
