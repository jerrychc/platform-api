package com.xinleju.platform.finance.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class CashFlowItemDto extends BaseDto{

		
	//财务系统公司id
	private String accountSetId;
    
  		
	//现金流量名称
	private String name;
    
  		
	//现金流量编码
	private String code;
    
	//现金流量科目对照
	private String subjectNames;
	
	//现金流量科目对照
	private String subjectCodes;
  		
	//父级现金流量id
	private String parentId;
    
	//节点等级
	private String nodeLevel;
	
	//上级节点
	private Long level;
	//加载是否完成
	private Boolean loaded;
	
	private Boolean isLeaf;
	
	private Boolean expanded;
		
	private Long lft;
	
	private Long rgt;
  	
	//父级名称
	private String parentName;
		
	public String getAccountSetId() {
		return accountSetId;
	}
	public void setAccountSetId(String accountSetId) {
		this.accountSetId = accountSetId;
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
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getSubjectNames() {
		return subjectNames;
	}
	public void setSubjectNames(String subjectNames) {
		this.subjectNames = subjectNames;
	}
	public String getSubjectCodes() {
		return subjectCodes;
	}
	public void setSubjectCodes(String subjectCodes) {
		this.subjectCodes = subjectCodes;
	}
	public String getNodeLevel() {
		return nodeLevel;
	}
	public void setNodeLevel(String nodeLevel) {
		this.nodeLevel = nodeLevel;
	}
    
  		
}
