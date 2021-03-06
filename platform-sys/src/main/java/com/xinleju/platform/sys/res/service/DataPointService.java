package com.xinleju.platform.sys.res.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.res.entity.DataPoint;

/**
 * @author admin
 * 
 * 
 */

public interface DataPointService extends  BaseService <String,DataPoint>{

	/**
	 * 校验itemId+code是否重复 add gyh
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer checkItemIdAndPointCode(Map<String,Object> param)throws Exception;
	
	/**
	 * 通过条件查询控制点
	 * @param param
	 * @return
	 */
	public List<DataPoint> queryDataPointByPram(Map<String,Object> param)throws Exception ;
	/**
	 * 通过条件查询控制点及授权情况
	 * @param param
	 * @return
	 */
	public Map<String,Object> queryDataPointSelByPram(Map<String,Object> param)throws Exception ;
	/**
	 * 根据IDs逻辑删除控制点
	 * @param ids
	 * @return
	 */
	public Integer deleteByIds(String[] ids)throws Exception ;
	/**
	 * 引用类型数量
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer checkYyTypeCount(Map<String,Object> param)throws Exception;

	/**
	 * 根据数据授权项查找数据授权列表
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryDataPointListByDataItem(Map<String,Object> paramMap) throws Exception;

	/**
	 *  获取各个授权点用户列表
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String,Map<String,Object>> queryUserIdForDataPoint(Map<String,Object> param) throws Exception;
	/**
	 * 批量保存排序号
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer savePointSort(Map<String,Object> param) throws Exception;
}
