package com.xinleju.platform.sys.org.entity;

public class DataNodeDto {
	  //主键
	 private String id;
	 //名称
	 private String name;
	 //类型
	 private String type;
	 
	 private String code;
	 
	 //是否可以操作
	 private Boolean isValid;
	 //排序
	 private Long sort;
	 //上级
	 private String parentId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	 
	 
}
