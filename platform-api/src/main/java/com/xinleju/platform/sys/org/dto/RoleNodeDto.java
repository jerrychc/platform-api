package com.xinleju.platform.sys.org.dto;

import java.util.List;

public class RoleNodeDto {
	
	  //主键
	  private String id ;
	  //名称
	  private String code;
	  //名称
	  private String name;
	  //类型
	  private String type;
	  //排序
	  private Long sort;
	  //父节点ID
	  private String parentId;
	  //状态
	  private String status;
	  //目录还是角色区分
	  private String mold;
	  //子节点对象
	  private List<RoleNodeDto> children;
	  
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getMold() {
		return mold;
	}
	public void setMold(String mold) {
		this.mold = mold;
	}
	public List<RoleNodeDto> getChildren() {
		return children;
	}
	public void setChildren(List<RoleNodeDto> children) {
		this.children = children;
	}
	  
	  
	  
	  
}
