package com.xinleju.platform.univ.attachment.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface AttachmentDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * 根据附件分类ID数组查询附件列表
	 * 
	 * @param userInfo
	 * @param paramaterJson
	 * @return
	 */
	String queryListByCategoryIds(String userInfo, String paramaterJson);
	
	/**
	 * 根据附件分类ID数组分页查询附件列表
	 * @param userInfo
	 * @param paramaterJson
	 * @return
	 */
	String getPageByCategoryIds(String userInfo, String paramaterJson);
	
	
	/**
	 * 根据appId，businessId（列表），categoryId查询附件列表
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	String queryListByObject(String userInfo, String paramaterJson);

	/**
	 * 返回符合条件的列表-流程专用
	 * @param paramaterJson
	 * @param s
	 * @return
	 */
    String queryFlowList(String s, String paramaterJson);
}
