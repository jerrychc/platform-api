package com.xinleju.platform.party.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.party.dao.UserConfigDao;
import com.xinleju.platform.party.entity.UserConfig;
import com.xinleju.platform.party.service.UserConfigService;
import com.xinleju.platform.sys.base.entity.SettlementTrades;
import com.xinleju.platform.sys.base.utils.StatusType;

/**
 * @author admin
 * 
 * 
 */

@Service
public class UserConfigServiceImpl extends  BaseServiceImpl<String,UserConfig> implements UserConfigService{
	

	@Autowired
	private UserConfigDao userConfigDao;

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.service.UserConfigService#getUserConfigPage(java.util.Map)
	 */
	@Override
	public Page getUserConfigPage(Map map) throws Exception {
		 Page page = new Page();
		 List<Map<String,Object>> list=userConfigDao.getUserConfigPage(map);
		 Integer count=userConfigDao.getUserConfigCount(map);
		 page.setLimit((Integer) map.get("limit"));
		 page.setStart((Integer) map.get("start"));
		 page.setList(list);
		 page.setTotal(count);
		 return page;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.service.UserConfigService#getUserConfig(java.lang.String)
	 */
	@Override
	public  List<Map<String,Object>> getUserConfig(String id) throws Exception {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("userConfigId", id);
		return userConfigDao.getUserConfig(map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.service.UserConfigService#updateStatus(com.xinleju.platform.party.entity.UserConfig)
	 */
	@Override
	public int updateStatus(UserConfig userConfig) throws Exception {
		int i=0;
		String status = userConfig.getStatus();
		if(StatusType.StatusOpen.getCode().equals(status)){//启用状态改为禁用
			userConfig.setStatus(StatusType.StatusClosed.getCode());
			 i = userConfigDao.update(userConfig);
		}else if(StatusType.StatusClosed.getCode().equals(status)){//禁用状态改为启用
			userConfig.setStatus(StatusType.StatusOpen.getCode());
			i=userConfigDao.update(userConfig);
		}
		return i;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.service.UserConfigService#saveUserConfig(com.xinleju.platform.party.entity.UserConfig)
	 */
	@Override
	public int saveUserConfig(UserConfig userConfig) throws Exception {
		int i=0;
		String appId = userConfig.getAppId();
		String userId = userConfig.getUserId();
		Map<String,Object>map =new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("userId", userId);
		String byAppId =userConfigDao.getUserConfigByAppId(map);
		if(StringUtils.isBlank (byAppId)){
			i = userConfigDao.save(userConfig);
		}else{
			i=5;
		}
		return i;
//		return 0;
	}	

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.service.UserConfigService#updateUserConfig(com.xinleju.platform.party.entity.UserConfig)
	 */
	@Override
	public int updateUserConfig(UserConfig userConfig) throws Exception {
	/*	String appId = userConfig.getAppId();
		String userId = userConfig.getUserId();
		Map<String,Object>map =new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("userId", userId);
		String  oldUserConfigId= userConfigDao.getUserConfigByAppId(map);
		userConfigDao.deleteObjectById(oldUserConfigId);*/
		int i = userConfigDao.update(userConfig);
		return i;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.service.UserConfigService#saveBatchList(java.util.List)
	 */
	@Override
	public void saveBatchList(List<UserConfig> userConfigList)throws Exception {
	    for (UserConfig userConfig : userConfigList) {
	    	String appId = userConfig.getAppId();
			String userId = userConfig.getUserId();
			userConfig.setToken(IDGenerator.getUUID());
			Map<String,Object>map =new HashMap<String, Object>();
			map.put("appId", appId);
			map.put("userId", userId);
			String oldUserConfigId = userConfigDao.getUserConfigByAppId(map);
			if(oldUserConfigId!=null&&(!"".equals(oldUserConfigId))){
				userConfigDao.deleteObjectById(oldUserConfigId);
			}
			userConfigDao.save(userConfig);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.party.service.UserConfigService#updateUserConfigBatch(com.xinleju.platform.party.entity.UserConfig)
	 */
	@Override
	public void updateUserConfigBatch(UserConfig userConfig) throws Exception {
		String appId = userConfig.getAppId();
		Map<String,Object>map=new HashMap<String, Object>();
		map.put("appId", appId);
		List<UserConfig> list=userConfigDao.getUserConfigListByAppId(map);
		if(list!=null&&list.size()>0){
			for (UserConfig user : list) {
				user.setStartTime(userConfig.getStartTime());
				user.setEndTime(userConfig.getEndTime());
				user.setStatus(userConfig.getStatus());
				user.setRemark(userConfig.getRemark());
				userConfigDao.update(user);
			}
		}
	}
}
