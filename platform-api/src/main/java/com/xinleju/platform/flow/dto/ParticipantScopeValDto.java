package com.xinleju.platform.flow.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class ParticipantScopeValDto extends BaseDto{

		
	//值
	private String val;
    
  		
	//主表id
	private String participantId;
    
  		
		
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
    
  		
	public String getParticipantId() {
		return participantId;
	}
	public void setParticipantId(String participantId) {
		this.participantId = participantId;
	}
    
  		
}