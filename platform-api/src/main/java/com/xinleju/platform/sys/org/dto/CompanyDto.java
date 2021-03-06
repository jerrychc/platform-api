package com.xinleju.platform.sys.org.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class CompanyDto extends BaseDto{

		
	//公司id
	private String refId;
    
  		
	//地区
	private String areaId;
    
  		
	//公司地址
	private String address;
    
  		
	//网址
	private String webUrl;
    
  		
	//传真
	private String fax;
    
  		
	//法人代表
	private String legal;
    
  		
		
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
    
  		
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
    
  		
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
    
  		
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
    
  		
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
    
  		
	public String getLegal() {
		return legal;
	}
	public void setLegal(String legal) {
		this.legal = legal;
	}
    
  		
}
