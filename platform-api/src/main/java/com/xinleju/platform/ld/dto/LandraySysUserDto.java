package com.xinleju.platform.ld.dto;

import com.xinleju.platform.base.dto.BaseDto;


/**
 * @author admin
 * 
 *
 */
public class LandraySysUserDto extends BaseDto{

		
	//oa��Աid
	private String oaId;
    
  		
	//oa��Ա��ʵ����
	private String oaRealName;
    
  		
	//��Ա��½�û���
	private String loginName;
    
  		
	//������Աid
	private String landrayId;
    
  		
	//������Ա��ʵ����
	private String landrayRealName;
    
  		
		
	public String getOaId() {
		return oaId;
	}
	public void setOaId(String oaId) {
		this.oaId = oaId;
	}
    
  		
	public String getOaRealName() {
		return oaRealName;
	}
	public void setOaRealName(String oaRealName) {
		this.oaRealName = oaRealName;
	}
    
  		
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
    
  		
	public String getLandrayId() {
		return landrayId;
	}
	public void setLandrayId(String landrayId) {
		this.landrayId = landrayId;
	}
    
  		
	public String getLandrayRealName() {
		return landrayRealName;
	}
	public void setLandrayRealName(String landrayRealName) {
		this.landrayRealName = landrayRealName;
	}
    
  		
}
