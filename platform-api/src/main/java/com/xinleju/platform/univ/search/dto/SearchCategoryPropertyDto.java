package com.xinleju.platform.univ.search.dto;

import java.util.List;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author haoqp
 * 
 *
 */
public class SearchCategoryPropertyDto extends BaseDto{

		
	//属性编码
	private String code;
    
  		
	//属性名称
	private String name;
    
  		
	//属性类别：0=系统;1=自定义
	private Integer category;
	
	// 索引分类
	private String searchIndexCategoryCode;
    
  		
	//属性数据类型
	private String type;
    
  		
	//默认值
	private String defaultValue;
    
  		
	//是否显示
	private Boolean isDisplay;
    
  		
	//是否参数检索
	private Boolean isSearchItem;
    
	
	// 检索分类主键ID
	private String categoryId;
	
	// 是否参与排序
	private Boolean isOrderItem;
	
	// 状态：0-启用;1-禁用
	private Boolean status;
	
		
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
    
  	/**
  	 * 属性类别：0=系统;1=自定义
  	 * 	
  	 * @return
  	 */
	public Integer getCategory() {
		return category;
	}
	
	/**
	 * 属性类别：0=系统;1=自定义
	 * 
	 * @param category
	 */
	public void setCategory(Integer category) {
		this.category = category;
	}
    
  	/**
  	 * 索引分类
  	 * 	
  	 * @return
  	 */
	public String getSearchIndexCategoryCode() {
		return searchIndexCategoryCode;
	}
	
	/**
  	 * 索引分类
  	 * 	
  	 * @return
  	 */
	public void setSearchIndexCategoryCode(String searchIndexCategoryCode) {
		this.searchIndexCategoryCode = searchIndexCategoryCode;
	}
	
	/**
	 * 属性数据类型
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * 属性数据类型
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
    
  		
	public Boolean getIsDisplay() {
		return isDisplay;
	}
	public void setIsDisplay(Boolean isDisplay) {
		this.isDisplay = isDisplay;
	}
    
  		
	public Boolean getIsSearchItem() {
		return isSearchItem;
	}
	public void setIsSearchItem(Boolean isSearchItem) {
		this.isSearchItem = isSearchItem;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public Boolean getIsOrderItem() {
		return isOrderItem;
	}
	public void setIsOrderItem(Boolean isOrderItem) {
		this.isOrderItem = isOrderItem;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
    
  		
}
