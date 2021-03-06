package com.xinleju.platform.sys.base.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_BASE_REGION",desc="区域")
public class BaseRegion extends BaseEntity{
	
		
	@Column(value="name",desc="名称")
	private String name;
    
  		
	@Column(value="code",desc="编号")
	private String code;
    
  		
	@Column(value="parent_id",desc="上级")
	private String parentId;
	
	@Column(value="parent_name",desc="上级名称")
	private String parentName;
	
	
	@Column(value="sort",desc="排序")
	private String sort;
	
	
	
	@Column(value="prefix_id",desc="父级Id")
	private String prefixId;
    
  		
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId= parentId;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
    
  		
	
}
