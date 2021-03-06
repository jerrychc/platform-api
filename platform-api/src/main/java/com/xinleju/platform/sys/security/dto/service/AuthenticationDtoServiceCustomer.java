package com.xinleju.platform.sys.security.dto.service;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;

public interface AuthenticationDtoServiceCustomer {
	
	/**
	 * 登陆用户
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String login(String userInfo, String paramater)throws Exception;
	
	/**
	 * 用户与预验证
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String preCheck(String userInfo, String paramater)throws Exception;
	
	
	/**
	 * 获取用户相关授权信息
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String getUserAuthenticationInfo(String userInfo, String paramater) throws Exception;
	/**
	 *  重置密码
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String resetPwd(String userInfo, String paramater) throws Exception;
	
	/**
	 * 登录日志
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String loginInLog(String userInfo, String paramater)throws Exception;
	/**
	 * 登出日志
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String loginOutLog(String userInfo, String paramater)throws Exception;
	/**
	 * 注册日志
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String registerLog(String userInfo, String paramater)throws Exception;

	/**
	 * 获取用户域
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String getDomain(String userInfo, String paramater)throws Exception;
	/**
	 * 打开菜单日志
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String menuLog(String userInfo, String paramater)throws Exception;
	/**
	 * 操作业务日志
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String opeLog(String userInfo, String paramater)throws Exception;
	/**
	 * dubbo日志
	 * @param userInfo
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String dubboLog(String userInfo, String paramater)throws Exception;

	/**
	 * 获取用户角色权限信息
	 * @param userInfo
	 * @param paramater
	 * @return
     */
	public String getUserAuthenticationInfoWithoutResource(String userInfo, String paramater)throws Exception;

	/**
	 * 根据用户ID获取用户角色/标准岗位/岗位信息
	 * @param userInfo
	 * @param parameter
	 * @return
	 */
	;public String queryUserPostRoleList(String userInfo,String parameter);
}

