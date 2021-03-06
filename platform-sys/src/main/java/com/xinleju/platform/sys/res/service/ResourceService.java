package com.xinleju.platform.sys.res.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.dto.ResourceDto;
import com.xinleju.platform.sys.res.entity.Resource;

/**
 * @author admin
 * 
 * 
 */

public interface ResourceService extends  BaseService <String,Resource>{

	
	/**
	 * 根据系统id获取资源一级集合
	 * @param  paramater(系统id)
	 */
	public List<DataNodeDto> queryResourceListByAppId(Map<String, Object> paramater)throws Exception;
	
	/**
	 * 根据资源id获取功能操作点一级集合
	 * @param  paramater(资源id)
	 */
	public List<DataNodeDto> queryResourceListByParentId(String paramater)throws Exception;
	
	/**
	 * 根据条件查询数据
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<ResourceDto> queryListByCondition(Map<String, Object> map) throws Exception;
	/**
	 * 校验编码重复
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer getCodeCount(Map<String, Object> map) throws Exception;
	/**
	 * 修改菜单，并维护全路径
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer updateMenu(Resource res) throws Exception;
	/**
	 * 查询所有父节点
	 * @param map appId系统ID
	 * @return
	 * @throws Exception
	 */
	public List<String> selectAllParentId(Map<String, Object> map) throws Exception;
	/**
	 * 上移下移
	 * @return
	 */
	public Integer upOrDown(Map<String,Object> map)throws Exception;
	/**
	 * 判断菜单是否存在下级菜单或按钮
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer selectSonNum(Map<String,Object> map) throws Exception;
	
	
	/**
	 * 获取用户的菜单
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<ResourceDto> queryAuthMenu(Map<String,Object> map) throws Exception;
	/**
	 * 删除菜单及其下级
	 * @param resource
	 * @return
	 * @throws Exception 
	 */
	public Integer deleteMeneAllSon(Resource resource) throws Exception ;
	
}
