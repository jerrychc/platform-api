package com.xinleju.platform.flow.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface ApproveTypeDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * 根据ApproveType的ID查询ApproveType的单个对象、与之相关联的Operation数据
	 * 主要是 operationTypeList 和  approvOperationList的数据
	 * @param userinfo
	 * @param id
	 * @return 返回字符串
	 */ 
	public String getApproveTypeAndOperationData(String userinfo, String id);
	
	
	/**
	 * 根据ApproveType的ID去更新 name数据和 与之相关联的Operation数据
	 * 主要是  approvOperationList的数据
	 * @param userinfo
	 * @param id
	 * @return 返回字符串
	 */ 
	public String updateApproveTypeAndOperationData(String userinfo, String id);

	public String resetBasicTypeData(String userInfo, String updateJson);
	
}
