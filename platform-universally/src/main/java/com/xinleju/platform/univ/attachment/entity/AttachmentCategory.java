package com.xinleju.platform.univ.attachment.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author haoqp
 * 
 * 
 */

@Table(value="PT_UNIV_FILE_CATEGORY",desc="附件分类表")
public class AttachmentCategory extends BaseEntity{
	
		
	@Column(value="app_id",desc="系统id")
	private String appId;
    
  		
	@Column(value="name",desc="分类名称")
	private String name;
    
  		
	@Column(value="code",desc="分类编码")
	private String code;
    
  		
	@Column(value="parent_id",desc="父分类id")
	private String parentId;
    
  		
		
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
    
  		
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
		this.parentId = parentId;
	}
    
  		
	
}
