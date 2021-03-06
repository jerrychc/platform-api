package com.xinleju.platform.sys.org.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_ORG_POST",desc="岗位")
public class Post extends BaseEntity{
	
		
	@Column(value="code",desc="编号")
	private String code;
    
  		
	@Column(value="type",desc="类型")
	private String type;
    
  		
	@Column(value="ref_id",desc="关联主键")
	private String refId;
    
  		
	@Column(value="role_id",desc="角色id")
	private String roleId;
  		
	@Column(value="sort",desc="排序")
	private Long sort;
    
  		
	@Column(value="incon",desc="图标")
	private String incon;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
	@Column(value="leader_id",desc="领导岗位")
	private String leaderId;
    
  		
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
    
  		
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
    
  		
	public String getIncon() {
		return incon;
	}
	public void setIncon(String incon) {
		this.incon = incon;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public String getLeaderId() {
		return leaderId;
	}
	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
    
  		
	
}
