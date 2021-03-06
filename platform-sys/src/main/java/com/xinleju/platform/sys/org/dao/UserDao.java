package com.xinleju.platform.sys.org.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.xinleju.platform.base.dao.BaseDao;
import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.org.dto.UserDto;
import com.xinleju.platform.sys.org.entity.User;

/**
 * @author sy
 *
 */

public interface UserDao extends BaseDao<String, User> {
	
	
	/**
	 * 根据组织结构查询所有人
	 * @param parentId
	 * @return
	 */
	public List<UserDto> queryUserListByOrgId(Map<String, Object> paramater)  throws Exception;

	/**
	 * 根据岗位查询用户列表
	 * 
	 * @param map
	 * @return
	 */
	public List<UserDto> queryUserListByPostId(Map<String, Object> paramater) throws Exception;
	/**
	 * 根据标准岗位Id和组织机构id（项目、公司、集团）获取userDto
	 * 
	 * @param map
	 * @return
	 */
	public List<UserDto> getUserListByStandardpostIdAndOrgIds(Map<String, Object> paramater) throws Exception;
	/**
	 * 根据标准岗位Id和组织机构id（公司、集团往下找）获取userDto
	 * 
	 * @param map
	 * @return
	 */
	public List<UserDto> getUserListByStandardpostIdAndOrgIdsUnder(Map<String, Object> paramater) throws Exception;
	
	/**
	 * 根据角色查询用户列表
	 * 
	 * @param map
	 * @return
	 */
	public List<UserDto> queryUserListByRoleId(Map<String, Object> paramater) throws Exception;
	/**
	 * 根据虚拟角色查询用户列表
	 * @param map
	 * @return
	 */
	public List<UserDto> queryRoleUserByRoleId(Map<String, Object> paramater) throws Exception;	
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<OrgnazationNodeDto> queryAllUserList(Map<String,Object> map)  throws Exception;
	
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<UserDto> queryAllUserListReturnUser(Map<String,Object> map)  throws Exception;
	/**
	 * 根据IDs获取结果集
	 * @param paramater
	 * @return
	 */
	public List<Map<String,String>> queryUsersByIds(Map map)throws Exception;
	/**
	 * 根据IDs获取人员DTO
	 * @param paramater
	 * @return
	 */
	public List<UserDto> getUserByUserIds(Map map)throws Exception;
	/**
	 * 根据人员姓名获取人员DTO
	 * @param paramater
	 * @return
	 */
	public List<UserDto> getUserByUserName(Map map)throws Exception;
	/**
	 * 根据loginNames获取人员DTO
	 * @param paramater
	 * @return
	 */
	public List<UserDto> getUserByUserLoginNames(Map map)throws Exception;
	/**
	 * 批量保存用户排序号
	 * @param paramater
	 * @return
	 */
	public Integer saveUsersSort(Map map)throws Exception;
	/**
	 * 获取用户详情
	 * @param paramater
	 * @return
	 */
	public UserDto selectUserInfoById(Map map)throws Exception;
	
	/**
	 * 校验登录名是否重复
	 * @param paramater
	 * @return
	 */
	public Integer checkLoginName(Map map)throws Exception;
	
	/** 
	 *  根据角色id获取用户
	 * @param paramater
	 * @return
	 */
	public List<Map<String, Object>> queryUserAndPostsByUname(Map<String, Object> paramater)throws Exception;
	public List<Map<String, Object>> queryUserAndBeLongOrgsByUname(Map<String, Object> paramater)throws Exception;

	//查询组织及其下级是否存在岗位
	public Integer selectSonRefCount(Map map)throws Exception;
	
	/**
	 * 获取多个人员岗位id
	 * @param paramater
	 * @return
	 */
	public List<String> selectPuIds(Map<String, Object> paramater)throws Exception;
	/**
	 * 获取多个人员
	 * @param paramater
	 * @return
	 */
	public List<Map<String, String>> queryPostUsersByIds(Map<String, Object> paramater)throws Exception;
	
	/**
	 * 根据条件查询用户
	 * @param paramater
	 * @return
	 */
	public List<UserDto> selectUserByQuery(Map<String, Object> paramater)throws Exception;
	/**
	 * 除去物业下的所有人
	 * @param paramater
	 * @return
	 */
	public List<User> queryListOutWuye(Map<String, Object> paramater)throws Exception;
	/**
	 * 修改用户密码按组织机构查询
	 * @param paramater
	 * @return
	 */
	public List<User> queryListUpdatePwdUserByOrgId(Map<String, Object> paramater)throws Exception;
	
	/**
	 * 根据条件查询用户
	 * @param paramater
	 * @return
	 */
	public int selectUserByQueryCount(Map<String, Object> paramater)throws Exception;

	/**
	 * 查询数据库已存在的账号
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public List<String> selectHaveLoginName(Map<String, Object> paramater)throws Exception;
/**
	 * 查询数据库已存在的组织编码
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectHaveOrgCode(Map<String, Object> paramater)throws Exception;
	public int insertExcelData(Map<String, Object> param)throws Exception;
	/**
	 * 批量禁用或启用用户
	 * @param param
	 * @return
	 */
	public int lockUsersOrNot(Map<String, Object> param)throws Exception;

	/**
	 * 标准岗位下的人
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<String> getUsersFromStandardPost(Map<String, Object> param)throws Exception;
	/**
	 * 角色下的人
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<String> getUsersFromRole(Map<String, Object> param)throws Exception;
	/**
	 * 岗位下的人
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<String> getUsersFromPost(Map<String, Object> param)throws Exception;
	/**
	 * 组织下的人
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<String> getUsersFromOrg(Map<String, Object> param)throws Exception;
}
