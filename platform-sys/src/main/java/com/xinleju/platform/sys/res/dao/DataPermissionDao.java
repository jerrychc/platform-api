package com.xinleju.platform.sys.res.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.res.entity.DataPermission;

/**
 * @author admin
 *
 */

public interface DataPermissionDao extends BaseDao<String, DataPermission> {
	
	/**
	 * 保存数据授权
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer saveDataAuth(Map<String,Object> param)throws Exception;
	/**
	 * 删除旧授权数据
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer delDataAuth(Map<String,Object> param)throws Exception;
	
	/**
	 * 保存数据授权（角色到数据）
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer saveDataAuthRoleToData(Map<String,Object> param)throws Exception;
	/**
	 * 删除旧授权数据（角色到数据）
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Integer delDataAuthRoleToData(Map<String,Object> param)throws Exception;
	
	/**
	 * 根据控制项Id和角色Ids查询已授权数据(控制点)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryDataPermission(Map<String,Object> map) throws Exception;
	/**
	 * 根据控制项Id和角色Ids查询已授权数据(指定数据)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryDataPointPermission(Map<String,Object> map) throws Exception;
	
	/**
	 * 根据控制项Id和控制点Ids查询已授权数据(控制点)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryDataPermissionByPointId(Map<String,Object> map) throws Exception;
	/**
	 * 根据控制项Id和valIds查询已授权数据(指定数据)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryDataPointPermissionByvalId(Map<String,Object> map) throws Exception;
	
	/**
	 * 获取有权限的数据授权(对外接口)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getDataPointAuthByUserLoginNameAndAppCodeAndItemCode(Map<String,Object> map) throws Exception;
	
	/**
	 * 根据数据授权ID查询值(对外接口)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getDataPointValAuthByDataPermissionId(Map<String,Object> map) throws Exception;
	
	/**
	 * 根据对象Id获取已授权控制点的主键ID（为了删除）
	 * @param map
	 * @return
	 */
	public List<String> queryAuthDataIdByobjectIds(Map<String,Object> map) throws Exception;
	/**
	 * 根据对象Id获取已授权控制点val的主键ID（为了删除）
	 * @param map
	 * @return
	 */
	public List<String> queryAuthDataValIdByobjectIds(Map<String,Object> map) throws Exception;
	/**
	 * 根据对象Id获取已授权数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryAuthDataByobjectIds(Map<String,Object> map) throws Exception;
	/**
	 * 根据已授权的主键ID获取已授权val数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryAuthValDataBydataPermissionId(Map<String,Object> map) throws Exception;

}
