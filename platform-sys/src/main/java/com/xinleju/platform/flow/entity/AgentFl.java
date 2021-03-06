package com.xinleju.platform.flow.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FLOW_AGENT_FL",desc="流程代理模板")
public class AgentFl extends BaseEntity{
	
		
	@Column(value="fl_id",desc="流程模板id")
	private String flId;
    
  		
	@Column(value="fl_name",desc="流程模板名称")
	private String flName;
    
  		
	@Column(value="agent_id",desc="流程代理id")
	private String agentId;
    
  		
		
	public String getFlId() {
		return flId;
	}
	public void setFlId(String flId) {
		this.flId = flId;
	}
    
  		
	public String getFlName() {
		return flName;
	}
	public void setFlName(String flName) {
		this.flName = flName;
	}
    
  		
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
    
  		
	
}
