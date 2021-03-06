package com.xinleju.platform.finance.service;

import java.util.List;

import com.xinleju.platform.base.service.BaseService;
import com.xinleju.platform.finance.entity.VoucherTemplate;

/**
 * @author admin
 * 
 * 
 */

public interface VoucherTemplateService extends  BaseService <String,VoucherTemplate>{

	public String saveVoucher(String paramaterJson,String saveVoucher,String companyId) throws Exception;
	public List queryListByTypeIds(List<String> paramList);
}
