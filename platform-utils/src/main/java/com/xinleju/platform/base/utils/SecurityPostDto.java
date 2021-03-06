package com.xinleju.platform.base.utils;

import java.io.Serializable;






/**
 * @author admin
 * 
 *
 */
public class SecurityPostDto  implements Serializable{

	private static final long serialVersionUID = 1L;
	//主键 
	private String id;
	
	//编号
	private String code;
    
  		
	//类型
	private String type;
    
  		
	//关联主键
	private String refId;
    
  		
	//角色id
	private String roleId;
    
	//排序
	private Long sort;
    
  		
	//图标
	private String incon;
    
  		
	//状态
	private String status;
    
  		
	//领导岗位
	private String leaderId;
	
	//名称
	private String name;
	
	//是否主岗
		private String isDefault;
    
  		
		
	public String getIsDefault() {
			return isDefault;
		}
		public void setIsDefault(String isDefault) {
			this.isDefault = isDefault;
		}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
}
