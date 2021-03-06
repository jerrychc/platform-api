package com.xinleju.platform.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinleju.platform.base.service.impl.BaseServiceImpl;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.finance.dao.BusinessFieldDao;
import com.xinleju.platform.finance.dao.BusinessObjectDao;
import com.xinleju.platform.finance.entity.BusinessField;
import com.xinleju.platform.finance.entity.BusinessObject;
import com.xinleju.platform.finance.service.BusinessObjectService;
import com.xinleju.platform.finance.utils.DataType;
import com.xinleju.platform.finance.utils.StatusType;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 * 
 */

@Service
public class BusinessFinanceObjectServiceImpl extends  BaseServiceImpl<String,BusinessObject> implements BusinessObjectService{
	

	@Autowired
	private BusinessObjectDao businessObjectDao;
	@Autowired
	private BusinessFieldDao businessFieldDao;

	@Override
	public int updateStatus(BusinessObject bean) throws Exception {
		int i=0;
		String status = bean.getStatus();
		if(StatusType.StatusOpen.getCode().equals(status)){//启用状态改为禁用
			bean.setStatus(StatusType.StatusClosed.getCode());
			 i = businessObjectDao.update(bean);
		}else if(StatusType.StatusClosed.getCode().equals(status)){//禁用状态改为启用
			bean.setStatus(StatusType.StatusOpen.getCode());
			i=businessObjectDao.update(bean);
		}
		return i;
	}

	@Override
	public int saveMasterTable(String saveJson) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String,Object> objDtoMap = JacksonUtils.fromJson(saveJson, HashMap.class);//转Map
		
		String objId = (String) objDtoMap.get("id");//获取主键
		BusinessObject billEntity = businessObjectDao.getObjectById(objId);//获取数据库bill对象
		if(null==billEntity){
			//验证编号
			Integer num  = businessObjectDao.getCountByCode(objDtoMap);
			if(num>0){
				throw new  Exception ("【编码】重复,请修改！");
			}
		}else{
			//验证编号
			Integer num  = businessObjectDao.getCountByCode(objDtoMap);
			if(num>0){
				throw new  Exception ("【编码】重复,请修改！");
			}
		}
		
		List<Map<String, Object>> rulerEntityList = businessFieldDao.getMapListByBillId(objId);//获取数据库ruler对象列表
		
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> rulerDtoList = (List<Map<String,Object>>) objDtoMap.get("fieldList");//获取页面规则子表数据
		
		if(null==billEntity){//数据为空
			BusinessObject newObjEntity = JacksonUtils.fromJson(JacksonUtils.toJson(objDtoMap), BusinessObject.class);
			businessObjectDao.save(newObjEntity);//新增bill对象
			@SuppressWarnings("unchecked")
			List<Map<String,Object>> fieldList = JacksonUtils.fromJson(JacksonUtils.toJson(rulerDtoList),List.class);
			for(int i=0;i<fieldList.size();i++){
				businessFieldDao.save(JacksonUtils.fromJson(JacksonUtils.toJson(fieldList.get(i)), BusinessField.class));//新增RulerList对象
			}
		}else{//数据为修改
			String billJson = JacksonUtils.toJson(billEntity);//对象转换
			@SuppressWarnings("unchecked")
			Map<String,Object> objMap = JacksonUtils.fromJson(billJson, HashMap.class);
			objMap.putAll(objDtoMap);
			BusinessObject newObjEntity = JacksonUtils.fromJson(JacksonUtils.toJson(objMap), BusinessObject.class);
			businessObjectDao.update(newObjEntity);//更新bill对象
			
			for(int i=0;i<rulerDtoList.size();i++){
				if(DataType.DATA_ADD.getCode()==rulerDtoList.get(i).get("dataType")){
					businessFieldDao.save(JacksonUtils.fromJson(JacksonUtils.toJson(rulerDtoList.get(i)),BusinessField.class));//新增方法
				}else if(DataType.DATA_UPDATE.getCode()==rulerDtoList.get(i).get("dataType")){
					//匹配数据库已存在对象
					for(int j=0;j<rulerEntityList.size();j++){
						if(rulerEntityList.get(j).get("id").equals(rulerDtoList.get(i).get("id"))){
							@SuppressWarnings("unchecked")
							Map<String,Object> fieldMap = JacksonUtils.fromJson(JacksonUtils.toJson(rulerDtoList.get(i)), HashMap.class);
							Map<String,Object> oldFieldMap = rulerEntityList.get(j);
							oldFieldMap.putAll(fieldMap);
							businessFieldDao.update(JacksonUtils.fromJson(JacksonUtils.toJson(oldFieldMap), BusinessField.class));//修改方法
						}
					}
					
				}else if(DataType.DATA_DELETE.getCode()==rulerDtoList.get(i).get("dataType")){
					businessFieldDao.deletePseudoObjectById((String)rulerDtoList.get(i).get("id"));//伪删除方法
				}
			}
		}
		return 1;//返回成功
	}

	@Override
	public Page getGridList(Map<String, Object> map) throws Exception {
		Page page=new Page();
		List<Map<String,Object>> list = businessObjectDao.getPageData(map);
		Integer count = businessObjectDao.getPageDataCount(map);
		page.setLimit((Integer) map.get("limit") );
		page.setList(list);
		page.setStart((Integer) map.get("start"));
		page.setTotal(count);
		return page;
	}
	

}
