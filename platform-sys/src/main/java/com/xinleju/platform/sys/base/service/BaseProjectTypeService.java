package com.xinleju.platform.sys.base.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.base.dto.BaseProjectTypeDto;
import com.xinleju.platform.sys.base.entity.BaseProjectType;

/**
 * @author admin
 * 
 * 
 */

public interface BaseProjectTypeService extends  BaseService <String,BaseProjectType>{
	/**
	 * 
	 * @param paramaterJson
	 * @return
	 * @throws Exception
	 * 查询产品类型树
	 */
/*	public List<TypeNodeDto> queryPostTypeList(Map<String,Object> paramaterJson)  throws Exception;*/
	/**
	 * 
	 * @param baseProjectType
	 * @param map
	 * @return
	 * @throws Exception
	 * 修改排序号
	 */
	public int updateSort(BaseProjectType baseProjectType,
			Map<String, Object> map)  throws Exception;
	/**
	 * 
	 * @param object
	 * @param map
	 * @return
	 * @throws Exception
	 * 重命名
	 */
	public int updateName(BaseProjectType object, Map<String, Object> map) throws Exception;
	/**
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 * 启用禁用
	 */
	public int updateStatus(BaseProjectType object,Map<String,Object> paramMap) throws Exception;
	/**
	 * 
	 * @return
	 * @throws Exception
	 * 伪删除
	 */
	public int deletePseudoProjectTypeById(String id)throws Exception;
	/**
	 * 查询树形结构数据
	 * @return
	 */
	
	public List<BaseProjectTypeDto> queryBaseProjecType(Map map)throws Exception;
	/**
	 * 添加的时候后台设置排序号
	 * @return
	 */
	
	
	public int saveBaseProjectType(BaseProjectType baseProjectType)throws Exception;
	/**
	 * @param baseProjectType
	 * @return
	 */
	public int updateProjectType(BaseProjectType baseProjectType)throws Exception;

	
	/**
	 * 查询所有产品类型
	 * @return
	 */
	public List<Map<String,Object>> getAllProductType()throws Exception;
	
	public List<Map<String,Object>> getLeafBaseProjectType()throws Exception;
}
