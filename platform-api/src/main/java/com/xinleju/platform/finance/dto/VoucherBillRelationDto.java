package com.xinleju.platform.finance.dto;

import com.xinleju.platform.base.dto.BaseDto;





/**
 * @author admin
 * 
 *
 */
public class VoucherBillRelationDto extends BaseDto{
	private String code;
  		
	private String voucherBillId;
  		
	private String bizId;
    
	private String billName;
	
	private String url;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getVoucherBillId() {
		return voucherBillId;
	}

	public void setVoucherBillId(String voucherBillId) {
		this.voucherBillId = voucherBillId;
	}

	public String getBizId() {
		return bizId;
	}

	public void setBizId(String bizId) {
		this.bizId = bizId;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
