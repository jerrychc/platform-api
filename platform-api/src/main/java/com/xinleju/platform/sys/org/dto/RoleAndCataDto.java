package com.xinleju.platform.sys.org.dto;

import java.sql.Timestamp;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class RoleAndCataDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//名称
	private String name;
    
  		
	//类型
	private Boolean type;
    
  		
	//上级
	private String parentId;
    
  		
	//排序
	private Long sort;
    
  		
	//图标
	private String icon;
    
  		
	//状态
	private String status;
    
  	//区分角色还是目录
	private String mold;
	
	//上级（目录）
	private String catalogId;
	
	//禁用人
	private String disabledId;
		
	//禁用日期
	private Timestamp disabledDate;
	
	//描述
	private String remark;
	
	//全路径Id
	private String prefixId;
	//全路径排序
	private String prefixSort;
	//全路径名称
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
	public String getMold() {
		return mold;
	}
	public void setMold(String mold) {
		this.mold = mold;
	}
	public String getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
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
    
  		
	public Boolean getType() {
		return type;
	}
	public void setType(Boolean type) {
		this.type = type;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
  		
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
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
    
  		
}
