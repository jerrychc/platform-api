package com.xinleju.platform.sys.num.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_NUM_BILL",desc="编号规则类别")
public class Bill extends BaseEntity{
	
		
	@Column(value="code",desc="编号")
	private String code;
    
  		
	@Column(value="name",desc="名称")
	private String name;
    
  		
	@Column(value="connector",desc="连接符")
	private String connector;
    
  		
	@Column(value="remark",desc="备注")
	private String remark;
    
  		
	@Column(value="status",desc="状态")
	private String status;
    
  		
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    
  		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
  		
	public String getConnector() {
		return connector;
	}
	public void setConnector(String connector) {
		this.connector = connector;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
  		
	
}
