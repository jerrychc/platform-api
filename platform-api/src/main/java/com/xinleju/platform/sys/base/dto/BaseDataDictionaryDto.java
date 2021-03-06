package com.xinleju.platform.sys.base.dto;

import java.util.List;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class BaseDataDictionaryDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//名称
	private String name;
    
  		
	//应用
	private String appId;
    
  		
	//状态
	private String status;
    
	//数据项
	private List<BaseDataDictionaryItemDto> list;
  		
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<BaseDataDictionaryItemDto> getList() {
		return list;
	}
	public void setList(List<BaseDataDictionaryItemDto> list) {
		this.list = list;
	}
    
  		
}
