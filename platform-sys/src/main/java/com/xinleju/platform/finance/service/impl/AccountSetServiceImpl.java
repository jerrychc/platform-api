package com.xinleju.platform.finance.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.dao.AccountSetDao;
import com.xinleju.platform.finance.entity.AccountSet;
import com.xinleju.platform.finance.entity.SysRegister;
import com.xinleju.platform.finance.service.AccountSetService;

/**
 * @author admin
 * 
 * 
 */

@Service
public class AccountSetServiceImpl extends  BaseServiceImpl<String,AccountSet> implements AccountSetService{
	

	@Autowired
	private AccountSetDao accountSetDao;
	
	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.service.SysRegisterService#getSystemRegisterpage(java.util.Map)
	 */
	@Override
	public Page getSystemRegisterpage(Map map) throws Exception {
	   Page page =new Page();
	   List<SysRegister> list=accountSetDao.getpageList(map);
	   Integer total=accountSetDao.getpageListCount(map);
	   page.setLimit((Integer) map.get("limit"));
	   page.setList(list);
	   page.setStart((Integer) map.get("start"));
	   page.setTotal(total);
	   return page;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.finance.service.AccountSetService#queryAccounSetList(java.util.Map)
	 */
	@Override
	public List queryAccounSetList(Map map) throws Exception {
		return accountSetDao.getpageList(map);
	}
}
