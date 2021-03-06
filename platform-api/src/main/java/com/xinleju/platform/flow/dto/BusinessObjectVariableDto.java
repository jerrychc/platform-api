package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class BusinessObjectVariableDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//名称
	private String name;
    
  		
	//变量类型: 1:字符串，2:整数，3:浮点数,4:布尔，5:日期，6:对象
	private String type;
    
  		
	//关联组件编号
	private String relationCode;
    
  		
	//业务对象id
	private String businessObjectId;
    
  		
	//上级
	private String parentId;
    
	//以下是0307@zhengjiajie新增的
	private String keyName;//键名
	private String systemAppId;//所属系统
	private String relateBusinessObject;//关联业务对象
	private Boolean forFlowBranch;//是否用于流程分支
	private Boolean forFinance;//是否用于财务接口
	private String comment;//说明
	
	//构造树形列表展示需要用到的
	//排序号
	private String sort;
	//全路径Id
	private String prefixId;
	//全路径排序
	private String prefixSort;
	//全路径名称
	private String prefixName;
	//层级
	private Long level;
	//是否展开
	private Boolean expanded;
	//是否加载
	private Boolean loaded;
	//是否叶子节点
	private Boolean isLeaf;
	
	private String parentName, busiObjectName;
		
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
    
  		
	public String getRelationCode() {
		return relationCode;
	}
	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}
    
  		
	public String getBusinessObjectId() {
		return businessObjectId;
	}
	public void setBusinessObjectId(String businessObjectId) {
		this.businessObjectId = businessObjectId;
	}
    
  		
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getSystemAppId() {
		return systemAppId;
	}
	public void setSystemAppId(String systemAppId) {
		this.systemAppId = systemAppId;
	}
	public String getRelateBusinessObject() {
		return relateBusinessObject;
	}
	public void setRelateBusinessObject(String relateBusinessObject) {
		this.relateBusinessObject = relateBusinessObject;
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Boolean getForFlowBranch() {
		return forFlowBranch;
	}
	public void setForFlowBranch(Boolean forFlowBranch) {
		this.forFlowBranch = forFlowBranch;
	}
	public Boolean getForFinance() {
		return forFinance;
	}
	public void setForFinance(Boolean forFinance) {
		this.forFinance = forFinance;
	}
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
	public Long getLevel() {
		return level;
	}
	public void setLevel(Long level) {
		this.level = level;
	}
	public Boolean getExpanded() {
		return expanded;
	}
	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
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
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getBusiObjectName() {
		return busiObjectName;
	}
	public void setBusiObjectName(String busiObjectName) {
		this.busiObjectName = busiObjectName;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
    
  		
}
