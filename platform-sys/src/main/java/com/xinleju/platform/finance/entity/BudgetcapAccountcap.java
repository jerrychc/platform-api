package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_BUDGETCAP_ACCOUNTCAP",desc="现金流量项目对应费用、成本预算科目")
public class BudgetcapAccountcap extends BaseEntity{
	
		
	@Column(value="name",desc="预算（成本）科目名称")
	private String name;
    
  		
	@Column(value="code",desc="预算（成本）科目编号")
	private String code;
    
  		
	@Column(value="cash_flow_id",desc="现金流量id")
	private String cashFlowId;
    
  		
		
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
    
  		
	public String getCashFlowId() {
		return cashFlowId;
	}
	public void setCashFlowId(String cashFlowId) {
		this.cashFlowId = cashFlowId;
	}
    
  		
	
}
