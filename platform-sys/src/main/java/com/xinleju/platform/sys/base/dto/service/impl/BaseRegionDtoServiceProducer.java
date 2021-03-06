package com.xinleju.platform.sys.base.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.cloud.oa.content.dto.ContentChildTreeData;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.sys.base.dto.BaseRegionDto;
import com.xinleju.platform.sys.base.dto.service.BaseRegionDtoServiceCustomer;
import com.xinleju.platform.sys.base.entity.BaseRegion;
import com.xinleju.platform.sys.base.entity.SettlementTrades;
import com.xinleju.platform.sys.base.service.BaseRegionService;
import com.xinleju.platform.sys.res.dto.DataNodeDto;
import com.xinleju.platform.sys.res.utils.ResourceType;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class BaseRegionDtoServiceProducer implements BaseRegionDtoServiceCustomer{
	private static Logger log = Logger.getLogger(BaseRegionDtoServiceProducer.class);
	@Autowired
	private BaseRegionService baseRegionService;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   BaseRegion baseRegion=JacksonUtils.fromJson(saveJson, BaseRegion.class);
		   int i = baseRegionService.saveBaseRegion(baseRegion);
		   if(i==4){
			   info.setSucess(false);
			   info.setMsg("区域名称重复，保存对象失败!");
		   }else if(i==5){
			   info.setSucess(false);
			   info.setMsg("区域代码重复，保存对象失败!");
		   }else if(i==6){
			   info.setSucess(false);
			   info.setMsg("区域名称和代码重复，保存对象失败!");
		   }else{
			   info.setResult(JacksonUtils.toJson(baseRegion));
			   info.setSucess(true);
			   info.setMsg("保存对象成功!");
		   }
		} catch (Exception e) {
		 log.error("保存对象失败!"+e.getMessage());
		 info.setSucess(false);
		 info.setMsg("保存对象失败!");
		 info.setExceptionMsg(e.getMessage());
		}
	   return JacksonUtils.toJson(info);
	}

	@Override
	public String saveBatch(String userInfo, String saveJsonList)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String updateBatch(String userInfo, String updateJsonList)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(String userInfo, String updateJson)  {
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseRegion baseRegion=JacksonUtils.fromJson(updateJson, BaseRegion.class);
			   int result=   baseRegionService.updateBaseRegion(baseRegion);
			   if(result==4){
				   info.setSucess(false);
				   info.setMsg("区域名称重复，更新对象失败!");
			   }else if(result==5){
				   info.setSucess(false);
				   info.setMsg("区域代码重复，更新对象失败!");
			   }else{
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("更新对象成功!");
			   }
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseRegion baseRegion=JacksonUtils.fromJson(deleteJson, BaseRegion.class);
			   int result= baseRegionService.deleteObjectById(baseRegion.getId());
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String deleteAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= baseRegionService.deleteAllObjectByIds(list);
				   info.setResult(JacksonUtils.toJson(result));
				   info.setSucess(true);
				   info.setMsg("删除对象成功!");
				}
			} catch (Exception e) {
			 log.error("删除对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String getObjectById(String userInfo, String getJson)
	 {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			BaseRegion baseRegion=JacksonUtils.fromJson(getJson, BaseRegion.class);
			BaseRegion	result = baseRegionService.getObjectById(baseRegion.getId());
			info.setResult(JacksonUtils.toJson(result));
		    info.setSucess(true);
		    info.setMsg("获取对象成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getPage(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				Page page=baseRegionService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=baseRegionService.getPage(new HashMap(), null, null);
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取分页对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取分页对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String queryList(String userInfo, String paramater){
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List list=baseRegionService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=baseRegionService.queryList(null);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String getCount(String userInfo, String paramater)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBaseRegionData(String userinfo, String paramaterJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
        	Map map = JacksonUtils.fromJson(paramaterJson, HashMap.class);
        List<BaseRegion> list=baseRegionService.getBaseRegionData();
    	info.setResult(JacksonUtils.toJson(list));
	    info.setSucess(true);
	    info.setMsg("获取树对象成功!");
		System.out.println(JacksonUtils.toJson(list));
	}catch(Exception e){
		log.error("获取树对象失败!"+e.getMessage());
		 info.setSucess(false);
		 info.setMsg("获取树对象失败!");
		 info.setExceptionMsg(e.getMessage());
	}
	return JacksonUtils.toJson(info);
	}
	@Override
	public String deletePseudoObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BaseRegion baseRegion=JacksonUtils.fromJson(deleteJson, BaseRegion.class);
			   int result= baseRegionService.deletePseudo(baseRegion);
			   info.setResult(JacksonUtils.toJson(result));
			   info.setSucess(true);
			   info.setMsg("删除对象成功!");
			} catch (Exception e) {
			 log.error("更新对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("删除更新对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	
	@Override
	public String deletePseudoAllObjectByIds(String userInfo,
			String deleteJsonList) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dto.service.BaseRegionDtoServiceCustomer#getTypetree(java.lang.String, java.lang.String)
	 */
	@Override
	public String getTypetree(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<BaseRegionDto> list=baseRegionService.getTypetree();
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List<BaseRegionDto> list=baseRegionService.getTypetree();
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	/* (non-Javadoc)
	 * @see com.xinleju.platform.sys.base.dto.service.BaseRegionDtoServiceCustomer#getSelectTree(java.lang.String, java.lang.String)
	 */
	@Override
	public String getSelectTree(String userJson, String paramater) {
		DubboServiceResultInfo info = new DubboServiceResultInfo();
		List<ContentChildTreeData> resultList = new ArrayList<ContentChildTreeData>();
		try {
			if (StringUtils.isNotBlank(paramater)) {
				Map map = JacksonUtils.fromJson(paramater, HashMap.class);
				String selectId = (String) map.get("selectId");
				if(selectId==null){
					selectId="selectId";
				}
				List<BaseRegion> list = baseRegionService.queryBaseRegionList();
				if (list.size() > 0) {
					for (BaseRegion baseRegion : list) {
						if(baseRegion.getPrefixId().indexOf(selectId)>-1){
							continue;
						}else{
							ContentChildTreeData contentChildTreeData = new ContentChildTreeData();
							contentChildTreeData.setpId(baseRegion.getParentId());
							contentChildTreeData.setName(String.valueOf(baseRegion.getName()));
							contentChildTreeData.setId(String.valueOf(baseRegion.getId()));
							contentChildTreeData.setParentId(baseRegion.getParentId());
							resultList.add(contentChildTreeData);	
						}
					}
				}
				info.setResult(JacksonUtils.toJson(resultList));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			} else {
				List list = baseRegionService.queryBaseRegionList();
				info.setResult(JacksonUtils.toJson(list));
				info.setSucess(true);
				info.setMsg("获取列表对象成功!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("获取列表对象失败!" + e.getMessage());
			info.setSucess(false);
			info.setMsg("获取列表对象失败!");
			info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);	
	}

	@Override
	public String updateSort(String userJson, String paramaterJson) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
        try {
        	Map map = JacksonUtils.fromJson(paramaterJson, HashMap.class);
        	int i=baseRegionService.updateSort(map);
    	info.setResult(JacksonUtils.toJson(i));
	    info.setSucess(true);
	    info.setMsg("获取树对象成功!");
		System.out.println(JacksonUtils.toJson(i));
	}catch(Exception e){
		log.error("获取树对象失败!"+e.getMessage());
		 info.setSucess(false);
		 info.setMsg("获取树对象失败!");
		 info.setExceptionMsg(e.getMessage());
	}
	return JacksonUtils.toJson(info);
	}

}
