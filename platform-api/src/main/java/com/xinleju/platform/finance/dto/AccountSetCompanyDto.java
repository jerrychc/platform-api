package com.xinleju.platform.finance.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class AccountSetCompanyDto extends BaseDto{

		
	//财务系统公司id
	private String accountSetId;
    
  		
	//公司id
	private String companyId;
    
  		
	//公司名称
	private String companyName;
    
  		
	//付款单位id
	private String paymentOrganId;
    
  		
	//付款单位名称
	private String paymentOrganName;
    
  		
	//项目分期id
	private String projectBranchId;
    
  		
	//分期名称
	private String projectBranchName;
    
  		
		
	public String getAccountSetId() {
		return accountSetId;
	}
	public void setAccountSetId(String accountSetId) {
		this.accountSetId = accountSetId;
	}
    
  		
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
    
  		
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
    
  		
	public String getPaymentOrganId() {
		return paymentOrganId;
	}
	public void setPaymentOrganId(String paymentOrganId) {
		this.paymentOrganId = paymentOrganId;
	}
    
  		
	public String getPaymentOrganName() {
		return paymentOrganName;
	}
	public void setPaymentOrganName(String paymentOrganName) {
		this.paymentOrganName = paymentOrganName;
	}
    
  		
	public String getProjectBranchId() {
		return projectBranchId;
	}
	public void setProjectBranchId(String projectBranchId) {
		this.projectBranchId = projectBranchId;
	}
    
  		
	public String getProjectBranchName() {
		return projectBranchName;
	}
	public void setProjectBranchName(String projectBranchName) {
		this.projectBranchName = projectBranchName;
	}
    
  		
}
