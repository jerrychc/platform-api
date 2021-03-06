package com.xinleju.platform.univ.search.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.univ.search.entity.SearchCategoryProperty;

/**
 * @author haoqp
 *
 */

public interface SearchCategoryPropertyDao extends BaseDao<String, SearchCategoryProperty> {

	/**
	 * 根据分类ID列表删除
	 * 
	 * @param ids 分类列表
	 * @return
	 * @throws DataAccessException
	 */
	int deleteAllObjectByCategoryIds(List<String> ids) throws DataAccessException;
	
	/**
	 * 查询Map列表
	 * @param statementName
	 * @param parameters
	 * @return
	 * @throws DataAccessException
	 */
	List<Map<String, Object>> queryMapList(String statementName, Map<String, Object> parameters) throws DataAccessException;

}
