package com.xinleju.platform.sys.org.dto;

import java.util.List;

public class OrgnazationNodeDto {
	
	 //主键
	  private String id ;
	  //名称
	  private String name;
	  //名称
	  private String loginName;
	  //类型
	  private String type;
	  //排序
	  private Long sort;
	  //父节点ID
	  private String parentId;
	 //目录ID
	  private String rootId;
	  //子节点对象
	  private List<OrgnazationNodeDto> children;
	  
	 //状态
	  private String status;
	  //全路径名称
	  private String prefixName;
	  //全路径Id
	  private String prefixId;
	  //用户所属组织名称
	  private String orgName;
	  //用户主岗id
	  private String mainPostId;
	  //用户主岗name
	  private String mainPostName;
	
	 public String getId() {
		return id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public void setId(String id) {
		this.id = id;
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
	public Long getSort() {
		return sort;
	}
	public void setSort(Long sort) {
		this.sort = sort;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public List<OrgnazationNodeDto> getChildren() {
		return children;
	}
	public void setChildren(List<OrgnazationNodeDto> children) {
		this.children = children;
	}
	public String getRootId() {
		return rootId;
	}
	public void setRootId(String rootId) {
		this.rootId = rootId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPrefixName() {
		return prefixName;
	}
	public void setPrefixName(String prefixName) {
		this.prefixName = prefixName;
	}
	public String getPrefixId() {
		return prefixId;
	}
	public void setPrefixId(String prefixId) {
		this.prefixId = prefixId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getMainPostId() {
		return mainPostId;
	}
	public void setMainPostId(String mainPostId) {
		this.mainPostId = mainPostId;
	}
	public String getMainPostName() {
		return mainPostName;
	}
	public void setMainPostName(String mainPostName) {
		this.mainPostName = mainPostName;
	}

}
