package com.xinleju.platform.sys.num.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class RulerSubDto extends BaseDto{

		
	//规则id
	private String rulerId;
    
  		
	//当前编号字符串
	private String currentSerial;
    
		
	public String getRulerId() {
		return rulerId;
	}
	public void setRulerId(String rulerId) {
		this.rulerId = rulerId;
	}
    
  		
	public String getCurrentSerial() {
		return currentSerial;
	}
	public void setCurrentSerial(String currentSerial) {
		this.currentSerial = currentSerial;
	}

  		
}
