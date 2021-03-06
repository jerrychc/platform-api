package com.xinleju.platform.univ.search.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface SearchIndexDtoServiceCustomer extends BaseDtoServiceCustomer{
	
	/**
	 * 批量删除索引
	 * @param esDocIndex ES 索引
	 * @param esDocType ES 类型
	 * @param businessIds 业务主键列表
	 * @return
	 * @throws Exception
	 */
	String deleteAllSearchIndex(String userInfo, String esDocIndex, String esDocType, String businessIds) throws Exception;

	/**
	 * 全文检索
	 * @param userInfo
	 * @param paramater
	 * @return
	 */
	String getPageFullTextQuery(String userInfo, String paramater) throws Exception;
	
	/**
	 * 初始化索引映射
	 * 
	 * @param userInfo
	 * @param tendId
	 * @throws Exception
	 */
	String initIndexMapping(String userInfo, String tendId) throws Exception;

	/**
	 * 更新索引映射
	 * 
	 * @param userInfo
	 * @param tendId
	 * @throws Exception
	 */
	String updateIndexMapping(String userInfo, String tendId) throws Exception;

}
