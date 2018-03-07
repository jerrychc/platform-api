package com.xinleju.platform.ld.service.impl;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.ld.dao.LandrayFlowInstanceDao;
import com.xinleju.platform.ld.dao.LandraySysUserDao;
import com.xinleju.platform.ld.entity.LandrayFlowInstance;
import com.xinleju.platform.ld.entity.LandraySysUser;
import com.xinleju.platform.ld.service.LandrayFlowInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 * 
 * 
 */

@Service
public class LandrayFlowInstanceServiceImpl extends  BaseServiceImpl<String,LandrayFlowInstance> implements LandrayFlowInstanceService {
	

	@Autowired
	private LandrayFlowInstanceDao landrayFlowInstanceDao;
	@Autowired
	private LandraySysUserDao landraySysUserDao;


	@Override
	public Page getFlDataByPage(Map map) throws Exception {
		Map<String,Object> pMap = new HashMap();
		Page page=new Page();
		String startUserId = map.get("startUserId").toString();
		if(null!=startUserId&&!"".equals(startUserId)){
			pMap.clear();
			pMap.put("oaId",startUserId);
			List<LandraySysUser> userList =  landraySysUserDao.queryList(pMap);
			if(userList.size()>0){
				map.put("startUserId",userList.get(0).getLandrayId());
			}
			LandraySysUser user =  landraySysUserDao.getObjectById(startUserId);
		}
		List<Map<String,Object>> list = landrayFlowInstanceDao.getFlData(map);
		for(Map<String,Object> m : list){
			pMap.clear();
			pMap.put("landrayId",m.get("startUserId").toString());
			List<LandraySysUser> userList =  landraySysUserDao.queryList(pMap);
			if(userList.size()>0){
				m.put("oaRealName",userList.get(0).getLandrayRealName());
			}
		}
		Integer count = landrayFlowInstanceDao.getFlDataCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		return page;
	}

	@Override
	public List<LandrayFlowInstance> portalList(Map map) throws Exception {
		return landrayFlowInstanceDao.portalList(map);
	}
}
