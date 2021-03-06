package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_BUSINESS_OBJECT",desc="业务表单数据注册")
public class BusinessObject extends BaseEntity{
	
		
	@Column(value="code",desc="业务对象编号")
	private String code;
    
  		
	@Column(value="remark",desc="描述")
	private String remark;
    
  		
	@Column(value="name",desc="业务对象名称")
	private String name;
    
  		
	@Column(value="app_code",desc="所属系统编号")
	private String appCode;
    
  		
	@Column(value="type",desc="业务对象类型")
	private String type;
    
  		
	@Column(value="fetch_class",desc="数据获取接口类")
	private String fetchClass;
    
  		
	@Column(value="fetch_method",desc="数据获取接口方法")
	private String fetchMethod;
    
  		
	@Column(value="callback_class",desc="数据回调接口类")
	private String callbackClass;
    
  		
	@Column(value="callback_method",desc="数据回调接口方法")
	private String callbackMethod;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getAppCode() {
		return appCode;
	}
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getFetchClass() {
		return fetchClass;
	}
	public void setFetchClass(String fetchClass) {
		this.fetchClass = fetchClass;
	}
    
  		
	public String getFetchMethod() {
		return fetchMethod;
	}
	public void setFetchMethod(String fetchMethod) {
		this.fetchMethod = fetchMethod;
	}
    
  		
	public String getCallbackClass() {
		return callbackClass;
	}
	public void setCallbackClass(String callbackClass) {
		this.callbackClass = callbackClass;
	}
    
  		
	public String getCallbackMethod() {
		return callbackMethod;
	}
	public void setCallbackMethod(String callbackMethod) {
		this.callbackMethod = callbackMethod;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	
}
