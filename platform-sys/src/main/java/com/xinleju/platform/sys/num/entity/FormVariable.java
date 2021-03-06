package com.xinleju.platform.sys.num.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_SYS_NUM_FORM_VARIABLE",desc="表单变量")
public class FormVariable extends BaseEntity{
	
		
	@Column(value="code",desc="编号")
	private String code;
    
	@Column(value="bill_id",desc="规则类型id")
	private String billId;
  		
	@Column(value="name",desc="名称")
	private String name;
	
	@Column(value="sort",desc="排序")
	private String sort;
  		
		
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
    
  		
	
}
