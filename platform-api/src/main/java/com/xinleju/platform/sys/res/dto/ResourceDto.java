package com.xinleju.platform.sys.res.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class ResourceDto extends BaseDto{

		
	//编号
	private String code;
    
  		
	//名称
	private String name;
    
	//地址
	private String url;
	
	//手机地址
	private String mobileUrl;
	
	//resourceUrl
	private String resourceUrl;
	
	//类型
	private String type;
    
	//resourceId
	private String resourceId;	
	
	//角色Id
	private String roleId;
	
	//模块
	private String appId;
    
  		
	//状态
	private String status;
    
  		
	//图标
	private String icon;
	//图标2
	private String menuIcon;
  		
	//排序
	private Long sort;
    
  	//父id
	private String parentId;
	
	//是否虚拟菜单
	private String isinventedmenu;

	//是否第三方菜单
	private String isoutmenu;
	
	//说明
	private String remark;
	
	//系统名称
	private String appName;
	
	//上级资源名称
	private String parentName;
	
	//打开方式
	private String openmode;
		
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
	
	//是否授权
	private String isAuth;
	
	
	public String getIsAuth() {
		return isAuth;
	}
	public void setIsAuth(String isAuth) {
		this.isAuth = isAuth;
	}
	public String getMenuIcon() {
		return menuIcon;
	}
	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}
	public Boolean getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(Boolean isLeaf) {
		this.isLeaf = isLeaf;
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
  		
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    
	public String getResourceUrl() {
		return resourceUrl;
	}
	public void setResourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
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
	public String getIsinventedmenu() {
		return isinventedmenu;
	}
	public void setIsinventedmenu(String isinventedmenu) {
		this.isinventedmenu = isinventedmenu;
	}
	public String getIsoutmenu() {
		return isoutmenu;
	}
	public void setIsoutmenu(String isoutmenu) {
		this.isoutmenu = isoutmenu;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getOpenmode() {
		return openmode;
	}
	public void setOpenmode(String openmode) {
		this.openmode = openmode;
	}
	public String getMobileUrl() {
		return mobileUrl;
	}
	public void setMobileUrl(String mobileUrl) {
		this.mobileUrl = mobileUrl;
	}
    
  		
}
