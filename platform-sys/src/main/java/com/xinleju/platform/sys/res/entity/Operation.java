package com.xinleju.platform.sys.res.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * operation
 */

@Table(value="PT_SYS_RES_OPERATION",desc="操作点")
public class Operation extends BaseEntity{
	
		
	@Column(value="code",desc="编号")
	private String code;
    
  		
	@Column(value="name",desc="名称")
	private String name;
    
  		
	@Column(value="parent_id",desc="上级")
	private String parentId;
    
  		
	@Column(value="icon",desc="图标")
	private String icon;
    
	@Column(value="app_id",desc="系统")
	private String appId;
  		
	@Column(value="resource_id",desc="资源")
	private String resourceId;
    
  		
	@Column(value="url",desc="地址")
	private String url;
    
	@Column(value="sort",desc="排序")
	private Long sort;	
		
	@Column(value="type",desc="按钮类别")
	private String type;
	
	@Column(value="is_valid",desc="是否有效")
	private String isValid;
	
	@Column(value="remark",desc="说明")
	private String remark;
	
	@Column(value="prefix_id",desc="全路径Id")
	private String prefixId;
	@Column(value="prefix_sort",desc="全路径排序")
	private String prefixSort;
	@Column(value="prefix_name",desc="全路径名称")
	private String prefixName;
	public String getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	public String getPrefixSort() {
		return prefixSort;
	}
	public void setPrefixSort(String prefixSort) {
		this.prefixSort = prefixSort;
	}
	public String getPrefixName() {
		return prefixName;
	}
	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}

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
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
  		
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
    
  		
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
    
  		
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	
}
