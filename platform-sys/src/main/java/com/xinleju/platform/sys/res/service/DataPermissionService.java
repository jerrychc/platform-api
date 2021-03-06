package com.xinleju.platform.sys.res.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.res.entity.DataPermission;

/**
 * @author admin
 * 
 * 
 */

public interface DataPermissionService extends  BaseService <String,DataPermission>{

	/**
	 * 保存数据授权和授权值
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void saveDataAuthAndVal(Map<String,Object> param)throws Exception;
	/**
	 * 保存数据授权和授权值（角色到数据）
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void saveDataAuthRoleToData(Map<String,Object> param)throws Exception;
	/**
	 * 保存数据授权和授权值（数据到角色）
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void saveDataAuthDataToRole(Map<String,Object> param)throws Exception;
	
	/**
	 * 根据（控制项Id和角色Ids）查询已授权数据
	 * @param map
	 * @return
	 */
	public Map<String,Object> queryAuthDataByitemIdAndroleIds(Map<String,Object> map) throws Exception;
	
	/**
	 * 根据（控制项Id和控制点Id或者指定数据ID（类型判断如果类型是dataPoint是控制点ID））查询已授权数据
	 * @param map
	 * @return
	 */
	public Map<String,Object> queryAuthDataByitemIdAndPointId(Map<String,Object> map) throws Exception;
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
}
