package com.xinleju.platform.sys.res.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface DataPointDtoServiceCustomer extends BaseDtoServiceCustomer{

	/**
	 * 根据条件查询控制点
	 * @return
	 */
	public String queryDataPointByPram(String userInfo, String paramater);
	/**
	 * 根据IDs逻辑删除控制点
	 * @return
	 */
	public String deleteByIds(String userInfo, String paramater);

	/**
	 * 查询数据权限相关人员列表
	 * @param userInfo
	 * @param parameter
	 * @return
	 */
	public String queryUserIdForDataPoint(String userInfo,String parameter);
	/**
	 * 批量保存控制点排序号
	 * @param userInfo
	 * @param parameter
	 * @return
	 */
	public String savePointSort(String userInfo,String parameter);
}
