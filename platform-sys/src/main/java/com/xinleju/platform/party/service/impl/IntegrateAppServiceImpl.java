package com.xinleju.platform.party.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.party.dao.IntegrateAppDao;
import com.xinleju.platform.party.entity.IntegrateApp;
import com.xinleju.platform.party.service.IntegrateAppService;
import com.xinleju.platform.sys.base.entity.SettlementTrades;

/**
 * @author admin
 * 
 * 
 */

@Service
public class IntegrateAppServiceImpl extends  BaseServiceImpl<String,IntegrateApp> implements IntegrateAppService{
	

	@Autowired
	private IntegrateAppDao integrateAppDao;

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.service.IntegrateAppService#getIntegratePage(java.util.Map)
	 */
	@Override
	public Page getIntegratePage(Map map) throws Exception {
		 Page page = new Page();
		 List<IntegrateApp> list=integrateAppDao.getIntegrateAppPage(map);
		 Integer count=integrateAppDao.getIntegrateAppCount(map);
		 page.setLimit((Integer) map.get("limit"));
		 page.setStart((Integer) map.get("start"));
		 page.setList(list);
		 page.setTotal(count);
		 return page;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.service.IntegrateAppService#saveIntegrateApp(com.xinleju.platform.party.entity.IntegrateApp)
	 */
	@Override
	public int saveIntegrateApp(IntegrateApp integrateApp) {
		String name = integrateApp.getName();
		Map<String,Object> map= new HashMap<String, Object>();
		map.put("name", name);
		Integer count = integrateAppDao.getIntegrateAppCountByName(map);
		if(count>0){
			return 5;
		}else{
			return integrateAppDao.save(integrateApp);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.service.IntegrateAppService#updateIntegrateApp(com.xinleju.platform.party.entity.IntegrateApp)
	 */
	@Override
	public int updateIntegrateApp(IntegrateApp integrateApp) {
		String name = integrateApp.getName();
		String id = integrateApp.getId();
		IntegrateApp app = integrateAppDao.getObjectById(id);
		String oldName = app.getName();
		Map<String,Object> map= new HashMap<String, Object>();
		map.put("name", name);
		Integer count = integrateAppDao.getIntegrateAppCountByName(map);
		if(oldName.equals(name)&&count>1||(!oldName.equals(name))&&count>0){
			return 5;
		}else{
			return integrateAppDao.update(integrateApp);
		}
	}
	

}
