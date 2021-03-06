package com.xinleju.platform.sys.org.dto;

import java.sql.Timestamp;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class StandardRoleDto extends BaseDto{

		
	//名称
	private String name;
    
  		
	//编号
	private String code;
    
  		
	//目录
	private String catalogId;
    
  		
	//排序
	private Long sort;
    
  		
	//类型
	private Boolean type;
    
  		
	//图标
	private String icon;
    
  		
	//状态
	private String status;
    
	//描述
	private String remark;	
	
	//禁用人
	private String disabledId;
		
	//禁用日期
	private Timestamp disabledDate;
	//全路径
	private String prefixName;
	
	
	public String getPrefixName() {
		return prefixName;
	}
	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
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
    
  		
	public String getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
    
  		
	public Boolean getType() {
		return type;
	}
	public void setType(Boolean type) {
		this.type = type;
	}
  		
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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
	public String getDisabledId() {
		return disabledId;
	}
	public void setDisabledId(String disabledId) {
		this.disabledId = disabledId;
	}
	public Timestamp getDisabledDate() {
		return disabledDate;
	}
	public void setDisabledDate(Timestamp disabledDate) {
		this.disabledDate = disabledDate;
	}
	
}
