package com.xinleju.platform.sys.base.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_BASE_DATA_DICTIONARY_ITEM",desc="数据字典项")
public class BaseDataDictionaryItem extends BaseEntity{
	
		
	@Column(value="name",desc="名称")
	private String name;
    
  		
	@Column(value="val",desc="值")
	private String val;
    
	@Column(value="parent_id",desc="父级id")
	private String parentId;	
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
  		
	
}
