package com.xinleju.platform.party.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class IntegrateAppDto extends BaseDto{

		
	//名称
	private String name;
    
  		
	//名称
	private String code;
    
  		
	//秘钥
	private String secret;
    
  		
	//地址
	private String addr;
    
  		
	//日访问量
	private Integer dcount;
    
  		
		
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
    
  		
	public String getSecret() {
		return secret;
	}
	public void setSecret(String secret) {
		this.secret = secret;
	}
    
  		
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
    
  		
	public Integer getDcount() {
		return dcount;
	}
	public void setDcount(Integer dcount) {
		this.dcount = dcount;
	}
    
  		
}
