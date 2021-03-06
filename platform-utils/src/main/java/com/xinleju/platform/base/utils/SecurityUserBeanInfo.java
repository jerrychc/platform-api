package com.xinleju.platform.base.utils;

import java.io.Serializable;
import java.util.List;


public class SecurityUserBeanInfo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * token
	 */
	public static final String TOKEN_TEND_USER="TOKEN_TEND_USER";
	
	
	/**
	 * 用户信息
	 */
	private SecurityUserDto securityUserDto;
	
	
	/**
	 * 当前用户对应标准岗位
	 * 
	 */
	private List<SecurityStandardRoleDto> securityStandardRoleDtoList;
	
	/**
	 * 当前用户对应通用角色
	 * 
	 */
	private List<SecurityStandardRoleDto> securityCurrencyRoleDtoList;
	
	/**
	 * 当前用户对应岗位
	 * 
	 */
	private List<SecurityPostDto> securityPostDtoList;
	
	
	/**
	 * 当前用户所在组织的类型
	 * 
	 */
	private String securityOrganizationType;
	
	/**
	 * 当前用户的一级公司
	 * 
	 */
	private SecurityOrganizationDto securityTopCompanyDto;
	
	/**
	 * 当前用户的一级部门
	 * 
	 */
	private SecurityOrganizationDto securityTopDeptDto;
	
	/**
	 * 当前用户的直属公司
	 * 
	 */
	private SecurityOrganizationDto securityDirectCompanyDto;
	
	/**
	 * 当前用户的直属部门
	 * 
	 */
	private SecurityOrganizationDto securityDirectDeptDto;
	
	/**
	 * 当前用户的项目
	 * 
	 */
	private SecurityOrganizationDto securityGroupDto;
	
	/**
	 * 当前用户的分期
	 * 
	 */
	private SecurityOrganizationDto securityBranchDto;
	
	/**
	 * 租户
	 */
	private String tendId;
	
	/**
	 * 租户编号
	 */
	private String tendCode;
	/**
	 * 令牌
	 */
	private String tokenId;
	
	//当前浏览器cookies
	private String cookies;
	
	/**
	 * webapp baseUrl
	 */
	private String baseUrl;

	public String getTendId() {
		return tendId;
	}
	public void setTendId(String tendId) {
		this.tendId = tendId;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public SecurityUserDto getSecurityUserDto() {
		return securityUserDto;
	}
	public void setSecurityUserDto(SecurityUserDto securityUserDto) {
		this.securityUserDto = securityUserDto;
	}
	public List<SecurityStandardRoleDto> getSecurityStandardRoleDtoList() {
		return securityStandardRoleDtoList;
	}
	public void setSecurityStandardRoleDtoList(
			List<SecurityStandardRoleDto> securityStandardRoleDtoList) {
		this.securityStandardRoleDtoList = securityStandardRoleDtoList;
	}
	public List<SecurityPostDto> getSecurityPostDtoList() {
		return securityPostDtoList;
	}
	public void setSecurityPostDtoList(List<SecurityPostDto> securityPostDtoList) {
		this.securityPostDtoList = securityPostDtoList;
	}
	public String getSecurityOrganizationType() {
		return securityOrganizationType;
	}
	public void setSecurityOrganizationType(String securityOrganizationType) {
		this.securityOrganizationType = securityOrganizationType;
	}
	public SecurityOrganizationDto getSecurityTopCompanyDto() {
		return securityTopCompanyDto;
	}
	public void setSecurityTopCompanyDto(
			SecurityOrganizationDto securityTopCompanyDto) {
		this.securityTopCompanyDto = securityTopCompanyDto;
	}
	public SecurityOrganizationDto getSecurityTopDeptDto() {
		return securityTopDeptDto;
	}
	public void setSecurityTopDeptDto(SecurityOrganizationDto securityTopDeptDto) {
		this.securityTopDeptDto = securityTopDeptDto;
	}
	public SecurityOrganizationDto getSecurityDirectCompanyDto() {
		return securityDirectCompanyDto;
	}
	public void setSecurityDirectCompanyDto(
			SecurityOrganizationDto securityDirectCompanyDto) {
		this.securityDirectCompanyDto = securityDirectCompanyDto;
	}
	public SecurityOrganizationDto getSecurityDirectDeptDto() {
		return securityDirectDeptDto;
	}
	public void setSecurityDirectDeptDto(
			SecurityOrganizationDto securityDirectDeptDto) {
		this.securityDirectDeptDto = securityDirectDeptDto;
	}
	public SecurityOrganizationDto getSecurityGroupDto() {
		return securityGroupDto;
	}
	public void setSecurityGroupDto(SecurityOrganizationDto securityGroupDto) {
		this.securityGroupDto = securityGroupDto;
	}
	public SecurityOrganizationDto getSecurityBranchDto() {
		return securityBranchDto;
	}
	public void setSecurityBranchDto(SecurityOrganizationDto securityBranchDto) {
		this.securityBranchDto = securityBranchDto;
	}
	public String getTendCode() {
		return tendCode;
	}
	public void setTendCode(String tendCode) {
		this.tendCode = tendCode;
	}
	public String getCookies() {
		return cookies;
	}
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}
	public List<SecurityStandardRoleDto> getSecurityCurrencyRoleDtoList() {
		return securityCurrencyRoleDtoList;
	}
	public void setSecurityCurrencyRoleDtoList(
			List<SecurityStandardRoleDto> securityCurrencyRoleDtoList) {
		this.securityCurrencyRoleDtoList = securityCurrencyRoleDtoList;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	
	
}
