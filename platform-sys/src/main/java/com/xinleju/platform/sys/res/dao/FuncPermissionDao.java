package com.xinleju.platform.sys.res.dao;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.res.entity.FuncPermission;

/**
 * @author admin
 *
 */

public interface FuncPermissionDao extends BaseDao<String, FuncPermission> {

	/**
	 * 根据角色查询系统权限
	 * @param roleId
	 * @return
	 */
	public List<Map<String,Object>> querySystemListByRole(String roleId);
	
	/**
	 * 根据系统查询按钮树
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryAuthorizationListByAppIds(Map<String,Object> map) throws Exception;
	
	/**
	 * 根据系统查询按钮树(授权)
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryAuthorizationListByAppIdsPermission(Map<String,Object> map) throws Exception;
	
	/**
	 * 查询角色树（标准岗位）（动作点-角色）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryAuthorizationListAllRoles() throws Exception;
	/**
	 * 查询角色树（通用角色）（动作点-角色）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryAuthorizationListAllCurrencyRoles() throws Exception;
	
	/**
	 * 查询对象树（岗位）（动作点-角色）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryAuthorizationListAllPost() throws Exception;
	
	/**
	 * 查询对象树（人员）（动作点-角色）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryAuthorizationListAllUser(Map<String,Object> map) throws Exception;
	
	/**
	 * 根据系统和角色查询已授权数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryAuthDataByappIdsAndroleIds(Map<String,Object> map) throws Exception;
	/**
	 * 根据对象Ids查询已授权数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryAuthDataByobjectIds(Map<String,Object> map) throws Exception;
	/**
	 * 根据对象Ids查询已授权数据（返回数据带Id）
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryAuthDataByobjectIdsReturnId(Map<String,Object> map) throws Exception;
	/**
	 * 根据系统和角色查询已授权菜单prefixId
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<String> queryAuthFunIds(Map<String,Object> map) throws Exception;
	/**
	 * 根据系统和角色查询所有app和菜单
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryAuthFuns(Map<String,Object> map) throws Exception;
	public List<Map<String,Object>> queryApps(Map<String,Object> map) throws Exception;
	public List<Map<String,Object>> queryFuns(Map<String,Object> map) throws Exception;

	/**
	 * 根据动作点查询已授权数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryAuthDataByOperationIds(Map<String,Object> map) throws Exception;
	/**
	 * 根据动作点查询已授权数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryAuthDataPostByOperationIds(Map<String,Object> map) throws Exception;
	/**
	 * 根据动作点查询已授权数据
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryAuthDataUserByOperationIds(Map<String,Object> map) throws Exception;
	public List<Map<String,Object>> queryAuthDataOrgByOperationIds(Map<String,Object> map) throws Exception;
	/**
	 * 查询角色授权控制点编码
	 * @param map
	 * @return
	 */
	public String selectUserRoleAuthDataCode(Map<String,Object> map) throws Exception;
	/**
	 * 查询授权值 
	 * @param map
	 * @return
	 */
	public List<String> selectAuthValIds(Map<String,Object> map) throws Exception;
	
	/**
	 * 根据用户登录账号和系统code和菜单code获取有权限的按钮(对外接口)
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getFuncButtonAuthByUserLoginNameAndAppCodeAndMenuCode(Map<String,Object> map) throws Exception;
	/**
	 * 获取有权限的菜单(根据用户账号和系统code)(对外接口)
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getFuncMenuAuthByUserLoginNameAndAppCode(Map<String,Object> map) throws Exception;
	/**
	 * 查询用户四种授权id：用户，标准岗位，岗位，通用角色
	 * @param map
	 * @return
	 */
	public List<String> selectAuthTypeId(Map<String,Object> map) throws Exception;

	/**
	 * 根据菜单id查找拥有当前菜单权限的用户id集合
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryUsersByMenuId(Map<String,Object> map) throws Exception;

	/**
	 * 根据系统和角色查询按钮权限
	 * @param map
	 * @return
	 * @throws Exception
	 */
    List<String> queryAuthPrefixIds(Map<String, Object> mapCon)throws Exception;
	/**
	 * 根据授权对象查询授权记录id
	 * @param map
	 * @return
	 * @throws Exception
	 */
    List<String> selectFuncIdByRole(Map<String, Object> map)throws Exception;

	/**
	 * 批量保存授权信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
    Integer savebatchAuth(Map<String, Object> map)throws Exception;
}
