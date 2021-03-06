package com.xinleju.platform.sys.base.dto;

import com.xinleju.platform.base.dto.BaseDto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;



/**
 * @author admin
 * 
 *
 */
public class BaseSupplierDto extends BaseDto implements Serializable{

		
	//名称
	private String name;
    
  		
	//编码
	private String code;
    
  		
	//省份
	private String provinceId;
    
  		
	//城市
	private String cityId;
    
  		
	//地址
	private String address;
    
  		
	//联系人
	private String relationPerson;
    
  		
	//联系方式
	private String phone;
    
  		
	//营业执照号
	private String license;
    
  		
	//法人代表
	private String representative;
    
  		
	//注册时间
	private Timestamp registrationDate;
    
  		
	//公司地址
	private String companyAddress;
    
  		
	//备注
	private String remark;
    
  	//供方账号
	private   List<BaseSupplierAccontDto> list;
	
	//禁用人
	private String disabledId;
  		
	//禁用日期
	private Timestamp disabledDate;	
	
	//注册电话
	private String workPhone;
	
	//状态
	private String status;
	
	//供方来源
	private  String  supplierResoure;
	
	//供方所属系统
	private  String  supplierApp;
	
	//所属公司
	private  String companyId;
	
	private  String companyName;
	
	private String financeCode;
	
	private String messageInfo;
	
	private String messageType;
		
	public String getWorkPhone() {
		return workPhone;
	}
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
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
    
  		
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
    
  		
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
    
  		
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
    
  		
	public String getRelationPerson() {
		return relationPerson;
	}
	public void setRelationPerson(String relationPerson) {
		this.relationPerson = relationPerson;
	}
    
  		
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
    
  		
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
    
  		
	public String getRepresentative() {
		return representative;
	}
	public void setRepresentative(String representative) {
		this.representative = representative;
	}
    
  		
	public Timestamp getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Timestamp registrationDate) {
		this.registrationDate = registrationDate;
	}
    
  		
	public String getCompanyAddress() {
		return companyAddress;
	}
	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<BaseSupplierAccontDto> getList() {
		return list;
	}
	public void setList(List<BaseSupplierAccontDto> list) {
		this.list = list;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSupplierResoure() {
		return supplierResoure;
	}
	public void setSupplierResoure(String supplierResoure) {
		this.supplierResoure = supplierResoure;
	}
	public String getSupplierApp() {
		return supplierApp;
	}
	public void setSupplierApp(String supplierApp) {
		this.supplierApp = supplierApp;
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
	public String getFinanceCode() {
		return financeCode;
	}
	public void setFinanceCode(String financeCode) {
		this.financeCode = financeCode;
	}
	public String getMessageInfo() {
		return messageInfo;
	}
	public void setMessageInfo(String messageInfo) {
		this.messageInfo = messageInfo;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
    
  		
}
