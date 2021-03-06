package com.xinleju.platform.finance.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.dao.VoucherFormSettingDao;
import com.xinleju.platform.finance.entity.VoucherFormSetting;
import com.xinleju.platform.finance.service.VoucherFormSettingService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class VoucherFormSettingServiceImpl extends  BaseServiceImpl<String,VoucherFormSetting> implements VoucherFormSettingService{
	

	@Autowired
	private VoucherFormSettingDao voucherFormSettingDao;
	
	@Override
	public Page getFormSettingPage(Map map) throws Exception {
	   Page page =new Page();
	   List<VoucherFormSetting> list=voucherFormSettingDao.getFormSettingList(map);
	   Integer total=voucherFormSettingDao.getFormSettingListCount(map);
	   page.setLimit((Integer) map.get("limit"));
	   page.setList(list);
	   page.setStart((Integer) map.get("start"));
	   page.setTotal(total);
	   return page;
	}
}
