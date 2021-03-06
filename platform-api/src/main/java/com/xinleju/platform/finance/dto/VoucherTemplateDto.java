package com.xinleju.platform.finance.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class VoucherTemplateDto extends BaseDto{

		
	//凭证模板业务类型id
	private String typeId;
    
  		
	//业务对象id
	private String bizObjectId;
    
  		
	//业务对象名称
	private String bizObjectName;
    
  		
	//筛选条件
	private String filter;
    
  		
	//凭证字
	private String word;
    
  		
	//状态
	private String status;
    
  		
	//业务类型说明
	private String remark;

	//名称
	private String name;


	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
    
  		
	public String getBizObjectId() {
		return bizObjectId;
	}
	public void setBizObjectId(String bizObjectId) {
		this.bizObjectId = bizObjectId;
	}
    
  		
	public String getBizObjectName() {
		return bizObjectName;
	}
	public void setBizObjectName(String bizObjectName) {
		this.bizObjectName = bizObjectName;
	}
    
  		
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
    
  		
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
}
