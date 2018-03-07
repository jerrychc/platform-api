package com.xinleju.platform.base.utils;

import java.io.Serializable;
import java.util.List;


public class SecurityUserBeanRelationInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * token
	 */
	public static final String TOKEN_TEND_USER_MENU="TOKEN_TEND_USER_MENU";
	
	/**
	 * 当前用户对应的菜单
	 * 
	 */
	private List<SecurityResourceDto> resourceDtoList;

	public List<SecurityResourceDto> getResourceDtoList() {
		return resourceDtoList;
	}

	public void setResourceDtoList(List<SecurityResourceDto> resourceDtoList) {
		this.resourceDtoList = resourceDtoList;
	}
	
	
}