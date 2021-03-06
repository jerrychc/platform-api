package com.xinleju.platform.sys.base.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_SETTLEMENT_TRADES",desc="结算方式")
public class SettlementTrades extends BaseEntity{
	
		
	@Column(value="name",desc="结算方式名称")
	private String name;
    
  		
	@Column(value="code",desc="结算方式编码")
	private String code;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
	@Column(value="remark",desc="备注")
	private String remark;
    
  		
		
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
    
  		
	
}
