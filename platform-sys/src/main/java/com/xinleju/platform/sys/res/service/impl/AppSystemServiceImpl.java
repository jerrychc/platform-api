package com.xinleju.platform.sys.res.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.sys.res.dao.AppSystemDao;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.entity.AppSystem;
import com.xinleju.platform.sys.res.entity.DataCtrl;
import com.xinleju.platform.sys.res.entity.Operation;
import com.xinleju.platform.sys.res.entity.Resource;
import com.xinleju.platform.sys.res.service.AppSystemService;
import com.xinleju.platform.sys.res.service.OperationService;
import com.xinleju.platform.sys.res.service.ResourceService;
import com.xinleju.platform.sys.res.utils.InvalidCustomException;

/**
 * @author admin
 * 
 * 
 */
@Service
public class AppSystemServiceImpl extends  BaseServiceImpl<String,AppSystem> implements AppSystemService{
	

	@Autowired
	private AppSystemDao appSystemDao;
	@Autowired
	private OperationService operationService;
	@Autowired
	private ResourceService  resourceService;
	@Override
	public List<DataNodeDto> queryListDataCtrl(String paramater)throws Exception {
		return appSystemDao.queryListDataCtrl(paramater);
	}

	@Override
	public List<DataNodeDto> queryResourceListByAppId(String paramater)throws Exception {
		return appSystemDao.queryResourceListByAppId(paramater);
	}

	@Override
	public List<DataNodeDto> queryOperationListByResourceId(String paramater)throws Exception {
		return appSystemDao.queryOperationList(paramater);
	}

	@Override
	public List<DataNodeDto> queryOperationListAll(String paramater) throws Exception{
		return appSystemDao.queryOperationListAll(paramater);
	}

	@Override
	public List<DataNodeDto> queryResourceListByParentId(String paramater) throws Exception{
		return appSystemDao.queryResourceListByParentId(paramater);
	}

	@Override
	public List<Map<String, Object>> querySystemList(String userType) throws Exception {
		return appSystemDao.querySystemList(userType);
	}
	
	@Override
	public List<AppSystem> queryListByCondition(Map<String, Object> map) throws Exception{
		return appSystemDao.queryListByCondition(map);
	}
	
	/**
	 * 校验编码重复
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer getCodeCount(Map<String, Object> map) throws Exception{
		return appSystemDao.getCodeCount(map);
	}
	/**
	 * 获取最大排序号
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	public Integer getMaxSort(Map<String, Object> map) throws Exception{
		return appSystemDao.getMaxSort(map);
	}
	/**
	 * 维护相关表全路径
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	
	public Integer updateAllPreFix(Map<String, Object> map) throws Exception{
		return appSystemDao.updateAllPreFix(map);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.res.service.AppSystemService#queryListData()
	 */
	@Override
	public List<Map<String,Object>> queryListData() throws Exception {
		// TODO Auto-generated method stub
		return appSystemDao.queryListData();
	}
	/**
	 * 维护相关表全路径
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public Integer upOrDown(Map<String, Object> map) throws Exception{
		Integer res=0;
		try {
			String appId1=(String)map.get("appId1");
			String sort1=(String)map.get("sort1");
			String appId2=(String)map.get("appId2");
			String sort2=(String)map.get("sort2");
			AppSystem app1=getObjectById(appId1);
			AppSystem app2=getObjectById(appId2);
			String prefixSortOld1=app1.getPrefixSort();
			String prefixSortOld2=app2.getPrefixSort();
			//业务排序
			app1.setSort(Long.valueOf(sort2));
			app2.setSort(Long.valueOf(sort1));
			app1.setPrefixSort(prefixSortOld2);
			app2.setPrefixSort(prefixSortOld1);
			Map<String,Object> param=new HashMap<String,Object>();
			//修改排序全路径-app1
			param.put("prefixIdOld", app1.getPrefixId());
			param.put("prefixSortOld", prefixSortOld1);
			param.put("prefixSortNew", app1.getPrefixSort());
			param.put("menuFlag", true);
			updateAllPreFix(param);		
			update(app1);
			//修改排序全路径-app2
			param.put("prefixIdOld", app2.getPrefixId());
			param.put("prefixSortOld", prefixSortOld2);
			param.put("prefixSortNew", app2.getPrefixSort());
			param.put("menuFlag", true);
			updateAllPreFix(param);		
			update(app2);
			res = 1;
		} catch (Exception e) {
			throw new InvalidCustomException("排序出错");
		}
		return res;
	}
	/**
	 * 修改业务系统信息
	 * @param map 参数
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	public int updateApp(AppSystem appSystem) throws Exception{
		Integer res=0;
		try {
			//排序或名称修改，维护全路径级下级菜单按钮全路径
		   AppSystem oldApp=getObjectById(appSystem.getId());
		   if(oldApp==null){
			   throw new InvalidCustomException("不存在此业务系统");
		   }
		   //业务名称修改，维护全路径
		   if(!oldApp.getName().equals(appSystem.getName())){
			   appSystem.setPrefixName(appSystem.getName());
			   Map<String,Object> map=new HashMap<String,Object>();
			   map.put("prefixIdOld", oldApp.getPrefixId());
			   map.put("prefixNameOld", oldApp.getPrefixName()+"/");
			   map.put("prefixNameNew", appSystem.getPrefixName()+"/");
			   map.put("menuFlag", true);
			   updateAllPreFix(map);
		   }
		   update(appSystem);
		   res = 1;
		} catch (Exception e) {
			throw new InvalidCustomException("排序出错");
		}
		return res;
	}
	@Override
	public List<AppSystem> queryListForSelect()throws Exception{
		return appSystemDao.queryListForSelect();
	}
}
