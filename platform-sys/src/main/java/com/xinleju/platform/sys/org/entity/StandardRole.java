package com.xinleju.platform.sys.org.entity;

import java.sql.Timestamp;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_ORG_STANDARD_ROLE",desc="标准角色")
public class StandardRole extends BaseEntity{
	
		
	@Column(value="name",desc="名称")
	private String name;
    
  		
	@Column(value="code",desc="编号")
	private String code;
    
  		
	@Column(value="catalog_id",desc="目录")
	private String catalogId;
  		
	@Column(value="sort",desc="排序")
	private Long sort;
    
  		
	@Column(value="type",desc="类型")
	private Boolean type;
    
  		
	@Column(value="icon",desc="图标")
	private String icon;
    
  		
	@Column(value="status",desc="状态")
	private String status;
	
	@Column(value="remark",desc="描述")
	private String remark;
	
	@Column(value="disabled_id",desc="禁用人")
	private String disabledId;
  		
	@Column(value="disabled_date",desc="禁用日期")
	private Timestamp disabledDate;	
	
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
