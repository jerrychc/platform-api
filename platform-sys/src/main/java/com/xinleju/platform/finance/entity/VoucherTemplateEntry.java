package com.xinleju.platform.finance.entity;

import com.xinleju.platform.base.annotation.Column;
import com.xinleju.platform.base.annotation.Table;
import com.xinleju.platform.base.entity.BaseEntity;


/**
 * @author admin
 * 
 * 
 */

@Table(value="PT_FI_VOUCHER_TEMPLATE_ENTRY",desc="凭证模板分录")
public class VoucherTemplateEntry extends BaseEntity{
	
		
	@Column(value="summary",desc="摘要")
	private String summary;
    
  		
	@Column(value="crmnyexpr",desc="贷方金额")
	private String crmnyexpr;
    
  		
	@Column(value="drmnyexpr",desc="借方金额")
	private String drmnyexpr;
    
  		
	@Column(value="voucher_template_id",desc="凭证模板id")
	private String voucherTemplateId;
    
  		
	@Column(value="remark",desc="备注")
	private String remark;
    
  		
	@Column(value="filter",desc="筛选条件")
	private String filter;
    
  		
	@Column(value="caption_id",desc="会计科目id")
	private String captionId;
    
  		
	@Column(value="caption_name",desc="会计科目名称")
	private String captionName;
    
  		
	@Column(value="ass_code",desc="辅助核算代码")
	private String assCode;
    
  		
	@Column(value="ass_name",desc="辅助核算名称")
	private String assName;
    
  		
	@Column(value="cash_flow_code",desc="现金流量code")
	private String cashFlowCode;
    
  		
	@Column(value="cash_flow_name",desc="现金流量名称")
	private String cashFlowName;
    
  		
	@Column(value="cash_flow_id",desc="现金流量id")
	private String cashFlowId;
    
	@Column(value="sort",desc="排序号")
	private Integer sort;

	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
    
  		
	public String getCrmnyexpr() {
		return crmnyexpr;
	}
	public void setCrmnyexpr(String crmnyexpr) {
		this.crmnyexpr = crmnyexpr;
	}
    
  		
	public String getDrmnyexpr() {
		return drmnyexpr;
	}
	public void setDrmnyexpr(String drmnyexpr) {
		this.drmnyexpr = drmnyexpr;
	}
    
  		
	public String getVoucherTemplateId() {
		return voucherTemplateId;
	}
	public void setVoucherTemplateId(String voucherTemplateId) {
		this.voucherTemplateId = voucherTemplateId;
	}
    
  		
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    
  		
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
    
  		
	public String getCaptionId() {
		return captionId;
	}
	public void setCaptionId(String captionId) {
		this.captionId = captionId;
	}
    
  		
	public String getCaptionName() {
		return captionName;
	}
	public void setCaptionName(String captionName) {
		this.captionName = captionName;
	}
    
  		
	public String getAssCode() {
		return assCode;
	}
	public void setAssCode(String assCode) {
		this.assCode = assCode;
	}
    
  		
	public String getAssName() {
		return assName;
	}
	public void setAssName(String assName) {
		this.assName = assName;
	}
    
  		
	public String getCashFlowCode() {
		return cashFlowCode;
	}
	public void setCashFlowCode(String cashFlowCode) {
		this.cashFlowCode = cashFlowCode;
	}
    
  		
	public String getCashFlowName() {
		return cashFlowName;
	}
	public void setCashFlowName(String cashFlowName) {
		this.cashFlowName = cashFlowName;
	}
    
  		
	public String getCashFlowId() {
		return cashFlowId;
	}
	public void setCashFlowId(String cashFlowId) {
		this.cashFlowId = cashFlowId;
	}
	
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
