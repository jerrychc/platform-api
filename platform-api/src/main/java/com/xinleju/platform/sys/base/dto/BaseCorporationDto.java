package com.xinleju.platform.sys.base.dto;

import java.sql.Timestamp;
import java.util.List;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class BaseCorporationDto extends BaseDto{

		
	//法人名称
	private String name;
    
  		
	//法人编码
	private String code;
    
  		
	//所属公司
	private String companyId;
	
	//所属公司
	private String companyName;
    
  		
	//省份
	private String provinceId;
    
  		
	//城市
	private String cityId;
    
  		
	//法人代表
	private String representative;
    
  		
	//状态
	private String status;
    
  		
	//备注
	private String remark;
    

	//禁用人
	private String disabledId;
  		
	//禁用日期
	private Timestamp disabledDate;	
	
	//法人账号
	private List<BaseCorporationAccontDto> list;
	
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
    
  		
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
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
    
  		
	public String getRepresentative() {
		return representative;
	}
	public void setRepresentative(String representative) {
		this.representative = representative;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public List<BaseCorporationAccontDto> getList() {
		return list;
	}
	public void setList(List<BaseCorporationAccontDto> list) {
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
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
    
  		
}
