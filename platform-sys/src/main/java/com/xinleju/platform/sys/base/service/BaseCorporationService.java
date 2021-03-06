package com.xinleju.platform.sys.base.service;

import java.util.List;
import java.util.Map;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dto.BaseCorporationDto;
import com.xinleju.platform.sys.base.entity.BaseCorporation;

/**
 * @author admin
 * 
 * 
 */

public interface BaseCorporationService extends  BaseService <String,BaseCorporation>{
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 * 根据公司法人id 查询公司法人和法人账号
	 */
	public BaseCorporationDto getCorporationAndAccontById(String id) throws Exception;
	/**
	 * 
	 * @param baseCorporationDto
	 * @throws Exception
	 * 保存公司法人和法人账号
	 */
	public void saveCorporationAndAccont(BaseCorporationDto baseCorporationDto) throws Exception;
	/**
	 * 
	 * @param baseCorporationDto
	 * @throws Exception
	 * 删除公司法人和法人账号
	 */
	public int deleteCorporationAndAccontByCorporationId(String id)throws Exception;
	/**
	 * 
	 * @param baseCorporationDto
	 * @throws Exception
	 * 修改公司法人和法人账号
	 */
	public int updateCorporationAndAccont(BaseCorporationDto baseCorporationDto)throws Exception;
	/**
	 * 
	 * @param baseCorporationDto
	 * @throws Exception
	 * 修改公司法人的启用 禁用
	 */
	public int updateStatus(BaseCorporationDto baseCorporationDtoBean)throws Exception;
	/**
	 * 
	 * @param baseCorporationDto
	 * @throws Exception
	 * 删除公司法人和法人账号 多个删除
	 */
	public int deleteAllByIds(String id)throws Exception;
	
	
	public Page getAllListByPage(Map map)throws Exception;
	/**
	 * @return
	 */
	public  List<Map<String,Object>> getBaseCorporationList(Map map)throws Exception;

	
}
