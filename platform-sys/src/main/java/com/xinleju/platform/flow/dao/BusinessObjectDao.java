package com.xinleju.platform.flow.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.flow.dto.BusinessObjectDto;
import com.xinleju.platform.flow.entity.BusinessObject;

/**
 * @author admin
 *
 */

public interface BusinessObjectDao extends BaseDao<String, BusinessObject> {
	
	/**
	 * 获取业务对象树
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public List<BusinessObjectDto> getTree(Map<String, Object> paramater)  throws Exception;

	public List<BusinessObjectDto> seachKeyword(Map<String, String> paramater);

	public List<BusinessObjectDto> queryListByCondition(Map<String, Object> map);

	public List<String> selectAllParentId(Map<String, Object> map);

	public List<BusinessObjectDto> getTreeBySystemApp(Map<String, String> paramMap);
	
	public BusinessObjectDto getObjectByCode(String businessObjectCode);
	
	public BusinessObjectDto getObjectByFlCode(String flCode);
	
	public int updateObjectPrefixIdByParamMap(Map<String, String> paramMap);

	public Integer queryCountLikePrefixMap(Map paramMap);

	public List<BusinessObjectDto> getCategoryTreeBySystemApp(Map<String, String> paramMap);

	public Integer queryRelatedCountByPrefixMap(Map paramMap);

	public int deleteObjectAndChileren(Map<String, String> paramMap);

	public List<BusinessObjectDto> queryBusiObjectTypeByParamMap(Map<String, Object> paramMap);

	//更新业务对象分类本身的name和prefixSort
	public void updateNameAndPrefixSort(Map<String, Object> updateMap0);
	//更新业务对象下面的子对象的的prefixSort
	public void updateSubObjectsPrefixSortByParamMap(Map<String, Object> updateMap);

	public List<BusinessObject> queryBusiObjectListByParam(Map<String, Object> param);

	public void updateAllNodes(Map<String, Object> params1);

	public void updateAllNodesSortAndPrefix(Map<String, Object> updateMap);
	
	/**
	 * 根据流程实例查询业务对象callbackUrl 
	 */
	public String selectCallBackUrlByInstanceId(Map<String, Object> param)throws Exception;
}
