package com.xinleju.platform.sys.org.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.sys.org.dto.OrgnazationNodeDto;
import com.xinleju.platform.sys.org.entity.Root;

/**
 * @author admin
 * 
 * 
 */

public interface RootService extends  BaseService <String,Root>{

	
	/**
	 * 获取目录子节点目录
	 * @param parentId
	 * @return
	 */
	public List<OrgnazationNodeDto> queryListRoot(Map<String,Object> map)  throws Exception;
	
	/**
	 * 获取所有目录节点
	 * @param parentId
	 * @return
	 */
	public List<OrgnazationNodeDto> queryAllRoot(Map<String,Object> map)  throws Exception;
}
