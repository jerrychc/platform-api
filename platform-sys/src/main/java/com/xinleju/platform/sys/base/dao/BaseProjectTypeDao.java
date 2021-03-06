package com.xinleju.platform.sys.base.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.base.entity.BaseProjectType;

/**
 * @author admin
 *
 */

public interface BaseProjectTypeDao extends BaseDao<String, BaseProjectType> {
/*
	public List<TypeNodeDto> queryPostTypeList(Map<String,Object> paramaterJson);*/

	public BaseProjectType getBaseProjectTypeBySort(Map<String,Object> map);

	public List<BaseProjectType> getBaseProjectList(Map<String, Object> param);

	/**
	 * @param map
	 * @return
	 */
	public  List<BaseProjectType> getRepeatObject(Map<String, Object> map);

	/**
	 * @param map
	 * @return
	 */
	public List<BaseProjectType> getBaseProjectListToView(
			Map<String, Object> map);

	/**
	 * @return
	 */
	public List<String> getBaseProjectParentIdList();

	/**
	 * @param sort2
	 * @return
	 */
	public void updateAllNodes(Map<String,Object> map);

	/**
	 * @param prefixId
	 * @return
	 */
	public List<String> getDeleteBaseProjectList(Map<String,Object> map);
	
	/**
	 * 查询所有产品类型
	 * @return
	 */
	public List<Map<String,Object>> getAllProductType()throws Exception;

	public List<BaseProjectType> queryListByIds( Set setIds);

	public List<Map<String, Object>> getLeafBaseProjectType();

}
