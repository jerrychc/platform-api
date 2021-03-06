package com.xinleju.platform.finance.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class BusinessFieldDto extends BaseDto{

		
	//数据项编码
	private String code;
    
  		
	//数据项名称
	private String name;
    
	private String nodeIcon;
  		
	//数据类型
	private String type;
    
  		
	//数据项上级
	private String parentId;
    
	//父级名称
	private String parentName;
  		
	//业务对象id
	private String bizObjectId;
    
  		
	//是否查询
	private String isQuery;
    
  		
	//是否显示
	private String display;
    
  		
	//是否url类型标识
	private String urlTypeFlag;
    
	//排序
	private String sort;
		
	//上级节点
	private Long level;
	//加载是否完成
	private Boolean loaded;
	
	private Boolean isLeaf;
	
	private Boolean expanded;
		
	private Long lft;
	
	private Long rgt;
	
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
    
  		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
    
  		
	public String getBizObjectId() {
		return bizObjectId;
	}
	public void setBizObjectId(String bizObjectId) {
		this.bizObjectId = bizObjectId;
	}
    
  		
	public String getIsQuery() {
		return isQuery;
	}
	public void setIsQuery(String isQuery) {
		this.isQuery = isQuery;
	}
    
  		
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
    
  		
	public String getUrlTypeFlag() {
		return urlTypeFlag;
	}
	public void setUrlTypeFlag(String urlTypeFlag) {
		this.urlTypeFlag = urlTypeFlag;
	}
	
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public Long getLevel() {
		return level;
	}
	public void setLevel(Long level) {
		this.level = level;
	}
	public Boolean getLoaded() {
		return loaded;
	}
	public void setLoaded(Boolean loaded) {
		this.loaded = loaded;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}
	public Long getLft() {
		return lft;
	}
	public void setLft(Long lft) {
		this.lft = lft;
	}
	public Long getRgt() {
		return rgt;
	}
	public void setRgt(Long rgt) {
		this.rgt = rgt;
	}
	public String getNodeIcon() {
		return nodeIcon;
	}
	public void setNodeIcon(String nodeIcon) {
		this.nodeIcon = nodeIcon;
	}
    
  		
}
