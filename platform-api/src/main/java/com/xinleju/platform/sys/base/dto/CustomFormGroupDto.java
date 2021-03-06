package com.xinleju.platform.sys.base.dto;

import com.xinleju.platform.base.dto.BaseDto;

/**
 * @author admin
 * 
 *
 */
public class CustomFormGroupDto extends BaseDto{

		
	//编码
	private String code;
    
  		
	//名称
	private String name;
    
  		
	//序号
	private Long sort;
    
  		
	//说明
	private String description;
    
	//编码校验标识字段
	private boolean codeExist=false;
	
	//名称校验标识字段
	private boolean nameExist=false;
	
	//父id
	private String parentId;

	//资源id
	private String resourceId;
		
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
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isCodeExist() {
		return codeExist;
	}
	public void setCodeExist(boolean codeExist) {
		this.codeExist = codeExist;
	}
	public boolean isNameExist() {
		return nameExist;
	}
	public void setNameExist(boolean nameExist) {
		this.nameExist = nameExist;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
  		
}
