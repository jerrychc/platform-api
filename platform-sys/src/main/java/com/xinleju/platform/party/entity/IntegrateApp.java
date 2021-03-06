package com.xinleju.platform.party.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_PARTY_INTEGRATE_APP",desc="第三方集成应用表")
public class IntegrateApp extends BaseEntity{
	
		
	@Column(value="name",desc="名称")
	private String name;
    
  		
	@Column(value="code",desc="编码")
	private String code;
    
  		
	@Column(value="secret",desc="秘钥")
	private String secret;
    
  		
	@Column(value="addr",desc="地址")
	private String addr;
    
  		
	@Column(value="dcount",desc="日访问量")
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
