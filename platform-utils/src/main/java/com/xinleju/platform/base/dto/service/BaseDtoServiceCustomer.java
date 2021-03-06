package com.xinleju.platform.base.dto.service;



public interface BaseDtoServiceCustomer {

	/**
	 * 新增业务对象
	 * @param object  实体对象
	 */
	public String save(String userInfo,String saveJson) ;
	
	/**
	 * 批量保存对象
	 * @param objectList
	 */
	public String saveBatch(String userInfo,String  saveJsonList) ;

	/**
	 * 
	 * 批量修改对象
	 * @param objectList
	 */
	public String updateBatch(String userInfo,String updateJsonList) ;
	
	/**
	 * 修改业务对象
	 * 
	 * @param object 业务实体对象
	 * 			
	 * @return 更新的记录数
	 */
	public String update(String userInfo,String updateJson)  ;
	
	
	
	/**
	 * 通过Id删除业务对象
	 * 
	 * @param id 业务实体对象id
	 * 			
	 * @return 删除的记录数
	 */
	public String deleteObjectById(String userInfo,String deleteJson) ;
	
	
	/**
	 * 通过Ids批量删除业务对象
	 * 
	 * @param ids 业务实体对象ids列表
	 * 			
	 * @return 删除的记录数
	 */
	public String deleteAllObjectByIds(String userInfo,String deleteJsonList) ;
	
	
	/**
	 * 根据Id获取业务对象
	 * 
	 * @param id  业务对象主键
	 * 
	 * @return     业务对象
	 */
	public String getObjectById(String userInfo,String  getJson) ;
	
	
	/**
	 * 查询符合条件的对象Page
	 * @param paramater 参数map
	 * @param start 开始记录数
	 * @param pageSize 每页记录数
	 * @return 返回符合条件的对象Page
	 */
	public String getPage(String userInfo,String paramater) ;
	
	
	/**
	 * 查询符合条件的对象List列表 
	 * @param map
	 * @return
	 */
	public String queryList(String userInfo,String paramater)  ;
	
	/**
	 * 返回符合条件总数
	 * @param paramater
	 * @return
	 * @throws Exception
	 */
	public String getCount(String userInfo,String paramater) ;
	
	
	/**
	 * 通过Id删除业务对象
	 * 
	 * @param id 业务实体对象id
	 * 			
	 * @return 删除的记录数
	 */
	public String deletePseudoObjectById(String userInfo,String deleteJson) ;
	
	
	/**
	 * 通过Ids批量删除业务对象
	 * 
	 * @param ids 业务实体对象ids列表
	 * 			
	 * @return 删除的记录数
	 */
	public String deletePseudoAllObjectByIds(String userInfo,String deleteJsonList) ;
	
}
