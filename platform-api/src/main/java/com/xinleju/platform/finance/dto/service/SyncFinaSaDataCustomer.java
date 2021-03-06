package com.xinleju.platform.finance.dto.service;

import com.xinleju.platform.base.dto.service.BaseDtoServiceCustomer;

public interface SyncFinaSaDataCustomer  extends BaseDtoServiceCustomer{
	public String getFinaData(String userjson,String paramater) ;
	
	/**
	 * 入账银行，销售系统
		返回数据,id,name,code
		Id:主键
		Name: 银行名字
		Code: 银行代码
	 * @param userjson
	 * @param paramater
	 * @return
	 */
	public String getAccountBlank(String userjson,String paramater);
	
	/**
	 * 款项名称
	 * @param userjson
	 * @param paramater
	 * @return
	 */
	public String getFunName(String userjson,String paramater);
	
	/**
	 * 支付方式，销售系统
		返回数据,id,name,code
		Id:主键
		Name: 支付名字
		Code: 支付代码
	 * @param userjson
	 * @param paramater
	 * @return
	 */
	public String getPayType(String userjson,String paramater);
	
	/**
	 * 房间信息，销售系统
		返回数据,id,name,code,pid(所属项目id)
		Id:主键
		Name:房间名字
		Code:房间代码
	 * @param userjson
	 * @param paramater
	 * @return
	 */
	public String getRoomInfo(String userjson,String paramater);
	
	/**
	 * 楼栋信息，销售系统
		返回数据,id,name,code,pid(所属项目id)
		Id:主键
		Name:房间名字
		Code:房间代码
	 * @param userjson
	 * @param paramater
	 * @return
	 */
	public String getRoomBuilInfo(String userjson,String paramater);
	
	/**
	 * 获取所有的属性
	 * type的取值为:领借款,日常报销,合同付款
	 * 假如传参数为领借款,那么返回类型为Map<String,Map<String,List<String>>>
	 * 最外层map中key值为所传参数领借款和科目分摊,冲账明细,value为所封装的属性集合
	 * 里层map中为封装属性集合,key为属性编码,value为list,次序依次为属性编码、名称、类型
	 * @param userjson
	 * @param paramater
	 * @return
	 */
	public String getAllAttr(String userjson,String paramater);
	
	/**
	 * 回写更新凭证回写信息
	 * @param userjson
	 * @param paramater
	 * @return
	 */
	public String rewriteResult(String userjson,String paramater);
}
