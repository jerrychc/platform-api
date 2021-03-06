package com.xinleju.platform.finance.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.finance.dao.VoucherTemplateEntryDao;
import com.xinleju.platform.finance.entity.VoucherTemplateEntry;
import com.xinleju.platform.finance.service.VoucherTemplateEntryService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class VoucherTemplateEntryServiceImpl extends  BaseServiceImpl<String,VoucherTemplateEntry> implements VoucherTemplateEntryService{
	

	@Autowired
	private VoucherTemplateEntryDao voucherTemplateEntryDao;
	
	@Override
	public List queryListByTemplateIds(List<String> paramList){
		return voucherTemplateEntryDao.queryListByTemplateIds(paramList);
	}
}
