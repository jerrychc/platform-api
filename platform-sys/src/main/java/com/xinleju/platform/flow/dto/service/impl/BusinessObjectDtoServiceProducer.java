package com.xinleju.platform.flow.dto.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.IDGenerator;
import com.xinleju.platform.base.utils.Page;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserDto;
import com.xinleju.platform.flow.dto.BusinessObjectDto;
import com.xinleju.platform.flow.dto.BusinessObjectVariableDto;
import com.xinleju.platform.flow.dto.service.BusinessObjectDtoServiceCustomer;
import com.xinleju.platform.flow.entity.BusinessObject;
import com.xinleju.platform.flow.service.BusinessObjectService;
import com.xinleju.platform.flow.service.BusinessObjectVariableService;
import com.xinleju.platform.flow.utils.ContentFlowTreeData;
import com.xinleju.platform.sys.base.dto.CustomFormDto;
import com.xinleju.platform.sys.base.dto.service.CustomFormDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;

/**
 * @author admin
 * 
 *
 */
 
public class BusinessObjectDtoServiceProducer implements BusinessObjectDtoServiceCustomer{
	private static Logger log = Logger.getLogger(BusinessObjectDtoServiceProducer.class);
	@Autowired
	private BusinessObjectService businessObjectService;
	
	@Autowired
	private BusinessObjectVariableService variableService;
	
	@Autowired
	private CustomFormDtoServiceCustomer  customFormServiceCustomer;

	public String save(String userInfo, String saveJson){
		// TODO Auto-generated method stub
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   BusinessObject businessObject=JacksonUtils.fromJson(saveJson, BusinessObject.class);
		   businessObject.setId(IDGenerator.getUUID());
		   int result = businessObjectService.saveBusiObjectAndDefaultVariables(businessObject);
		   info.setResult(JacksonUtils.toJson(businessObject));
		   info.setSucess(true);
		   info.setMsg("保存对象成功!");
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
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			BusinessObject businessObject = JacksonUtils.fromJson(updateJson, BusinessObject.class);
			int result = businessObjectService.updateBusinessObject(businessObject);
			info.setResult(JacksonUtils.toJson(result));
			if(result==5){
				info.setSucess(false);
				info.setMsg("更新对象失败，数据库已存在"); 
			}else{
				info.setSucess(true);
				info.setMsg("更新对象成功!");
			}
			   
			/*BusinessObject businessObject=JacksonUtils.fromJson(updateJson, BusinessObject.class);
			String parentId = businessObject.getParentId();
			System.out.println("saveBusiObjectAndDefaultVariables>>> parentId="+parentId);
			String parentPrefixId = businessObject.getAppId();
			String parentPrefixSort = businessObject.getAppId();
			if(parentId!=null && !"".equals(parentId) && parentId.length()>=32){//parentId的值有效
				BusinessObject parentObj = businessObjectService.getObjectById(parentId);
				parentPrefixId = parentObj.getPrefixId();
				parentPrefixSort = parentObj.getPrefixSort();
			 } else {//增加对parentId为空的处理
					 //businessObject.setParentId(businessObject.getAppId());
			 }
			 businessObject.setPrefixId(parentPrefixId+"-"+businessObject.getId());
			 businessObject.setPrefixSort(parentPrefixSort+"-"+businessObject.getName());
			 int result= businessObjectService.updateObjectByDataType(businessObject);
			 //int result= businessObjectService.update(businessObject);
			 info.setResult(JacksonUtils.toJson(businessObject));
			 info.setSucess(true);
			 info.setMsg("更新对象成功!");*/
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
			   BusinessObject businessObject=JacksonUtils.fromJson(deleteJson, BusinessObject.class);
			   int result= businessObjectService.deleteObjectById(businessObject.getId());
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
				   int result= businessObjectService.deleteAllObjectByIds(list);
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
			BusinessObject businessObject=JacksonUtils.fromJson(getJson, BusinessObject.class);
			BusinessObject	result = businessObjectService.getObjectById(businessObject.getId());
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
				Page page=businessObjectService.getPage(map, (Integer)map.get("start"),  (Integer)map.get("limit"));
				info.setResult(JacksonUtils.toJson(page));
			    info.setSucess(true);
			    info.setMsg("获取分页对象成功!");
			}else{
				Page page=businessObjectService.getPage(new HashMap(), null, null);
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
				List list=businessObjectService.queryList(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=businessObjectService.queryList(null);
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
	public String deletePseudoObjectById(String userInfo, String deleteJson)
	{
		// TODO Auto-generated method stub
		   DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BusinessObject businessObject=JacksonUtils.fromJson(deleteJson, BusinessObject.class);
			   //int result= businessObjectService.deletePseudoObjectById(businessObject.getId());
			   Map<String, String> paramMap = new HashMap<String,String>();
			   SecurityUserBeanInfo userBean = JacksonUtils.fromJson(userInfo,SecurityUserBeanInfo.class);
			   SecurityUserDto userDto = userBean.getSecurityUserDto();
			   paramMap.put("prefixId", businessObject.getId());
			   paramMap.put("updatePersonId", userDto.getId());
			   paramMap.put("updatePersonName", userDto.getRealName());
			   //update_person_id='', update_person_name
			   int result= businessObjectService.deleteObjectAndChileren(paramMap);
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
	public String deletePseudoAllObjectByIds(String userInfo, String deleteJsonList)
   {
		// TODO Auto-generated method stub
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   if (StringUtils.isNotBlank(deleteJsonList)) {
				   Map map=JacksonUtils.fromJson(deleteJsonList, HashMap.class);
				   List<String> list=Arrays.asList(map.get("id").toString().split(","));
				   int result= businessObjectService.deletePseudoAllObjectByIds(list);
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
	public String getTree(String userInfo, String paramater) {
		// TODO Auto-generated method stub
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		List<ContentFlowTreeData> resultList=new ArrayList<ContentFlowTreeData>();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<BusinessObjectDto> list=businessObjectService.getTree(map);
				if(list.size()>0){
					for (BusinessObjectDto businessObjectDto : list) {
						ContentFlowTreeData contentFlowTreeData = new ContentFlowTreeData();
						  String parentId = businessObjectDto.getpId();
						  if(parentId!=null&&(!"".equals(parentId))){
							  contentFlowTreeData.setpId(parentId);
							  contentFlowTreeData.setName(businessObjectDto.getName());
							  contentFlowTreeData.setId(businessObjectDto.getId());
							  contentFlowTreeData.setParentId(parentId);
							  contentFlowTreeData.setDataType(businessObjectDto.getDataType());
						  }else{
							  contentFlowTreeData.setpId("0");
							  contentFlowTreeData.setName(businessObjectDto.getName());
							  contentFlowTreeData.setId(businessObjectDto.getId());
							  contentFlowTreeData.setParentId("0");
							  contentFlowTreeData.setDataType(businessObjectDto.getDataType());
							  
						  }
						  resultList.add(contentFlowTreeData);
					}
				}
				
				info.setResult(JacksonUtils.toJson(resultList));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List<BusinessObjectDto> list=businessObjectService.getTree(null);
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
	public String seachKeyword(String userInfo, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			System.out.println("seachKeyword() paramater="+paramater);
			Map map=JacksonUtils.fromJson(paramater, HashMap.class);
			List list=businessObjectService.seachKeyword(map);
			info.setResult(JacksonUtils.toJson(list));
		    info.setSucess(true);
		    info.setMsg("获取列表对象成功!");
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
	public String saveObjectAndVariableData(String userInfo, String saveJson) {
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   System.out.println("saveObjectAndVariableData saveJson="+saveJson);
		   BusinessObjectDto objectDto=JacksonUtils.fromJson(saveJson, BusinessObjectDto.class);
		   String resultMsg = businessObjectService.saveObjectAndVariableData(userInfo, objectDto);
		   if("保存成功".equals(resultMsg)){
			   info.setResult(JacksonUtils.toJson(objectDto));
			   info.setSucess(true);
			   info.setMsg("保存对象成功!");
		   }else{
			   info.setResult(resultMsg);
			   info.setSucess(false);
			   info.setMsg(resultMsg);	
			   info.setExceptionMsg(resultMsg);
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
	public String queryListByCondition(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map paramMap=JacksonUtils.fromJson(paramater, HashMap.class);
//				List list=resourceService.queryList(map);
				System.out.println("----------------queryListByCondition() will be called...");
				List<BusinessObjectDto> list=businessObjectService.queryListByCondition(paramMap);
				System.out.println("----------------queryListByCondition() is called...");
				List<String> pIds=businessObjectService.selectAllParentId(paramMap);
				if(list!=null&&list.size()>0){
					for (BusinessObjectDto res : list) {
						//String prefixSort = res.getPrefixSort();
						String prefixId = res.getPrefixId();
						/*res.setMenuIcon(res.getIcon());
						res.setIcon("");*/
						
						if(prefixId!=null){
							//String[] split = prefixSort.split("-");
							String[] split = prefixId.split("-");
							Long i=(long) split.length-1;
							res.setLevel(i);
							res.setExpanded(true);
							res.setLoaded(true);
							if(pIds.contains(res.getId())){
								res.setIsLeaf(false);
							}else{
								res.setIsLeaf(true); 
							};
						}
					}
				}

				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List list=businessObjectService.queryList(null);
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
	public String getTreeBySystemApp(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map<String, String> map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<BusinessObjectDto> list=businessObjectService.getTreeBySystemApp(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List<BusinessObjectDto> list=businessObjectService.getTree(null);
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
	public String queryCountLikePrefixMap(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map paramMap=JacksonUtils.fromJson(paramater, HashMap.class);
			Integer countSum = businessObjectService.queryCountLikePrefixMap(paramMap);
			info.setResult(countSum.toString());
		    info.setSucess(true);
		    info.setMsg("获取列表对象成功!");
			
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
	public String getCategoryTreeBySystemApp(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			if(StringUtils.isNotBlank(paramater)){
				Map<String, String> map=JacksonUtils.fromJson(paramater, HashMap.class);
				List<BusinessObjectDto> list=businessObjectService.getCategoryTreeBySystemApp(map);
				info.setResult(JacksonUtils.toJson(list));
			    info.setSucess(true);
			    info.setMsg("获取列表对象成功!");
			}else{
				List<BusinessObjectDto> list=businessObjectService.getTree(null);
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
	public String queryRelatedCountByPrefixMap(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map paramMap=JacksonUtils.fromJson(paramater, HashMap.class);
			Integer countSum = businessObjectService.queryRelatedCountByPrefixMap(paramMap);
			info.setResult(countSum.toString());
		    info.setSucess(true);
		    info.setMsg("获取列表对象成功!");
			
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
	public String copyAndSaveBusiObjectData(String userJson, String paramater) {
		DubboServiceResultInfo info=new DubboServiceResultInfo();
		try {
			Map<String, String> map=JacksonUtils.fromJson(paramater, HashMap.class);
			//{"busiObjectId":"a9ab2f55fbbf457ca8448790660f8b14", "keyword":"AABB", "IPText":"123456"}
			String busiObjectId = map.get("busiObjectId");
			String keyword = map.get("keyword");
			BusinessObject businessObject = businessObjectService.getObjectById(busiObjectId);
			
			Map<String, Object> variableMap = new HashMap<String, Object>();
			variableMap.put("businessObjectId", busiObjectId);
			variableMap.put("codeText", "cmp_");
			List<BusinessObjectVariableDto> variableList = variableService.queryListByParamMap(variableMap);
			
			Map<String, Object> customMap = new HashMap<String, Object>();
			customMap.put("createCompanyName", keyword);
			String dubboResultInfo = customFormServiceCustomer.queryList(userJson, JacksonUtils.toJson(customMap));
			System.out.println("\n\n dubboResultInfo="+dubboResultInfo);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				System.out.println("\n\n 开始准备复制的业务数据......");
				String resultInfo= dubboServiceResultInfo.getResult();
				List<CustomFormDto> formList=JacksonUtils.fromJson(resultInfo, ArrayList.class,CustomFormDto.class);
				for(CustomFormDto customFormDto : formList){
					
					BusinessObjectDto objectDto = new BusinessObjectDto();
					BeanUtils.copyProperties(businessObject, objectDto);
					objectDto.setAppCode("OA");
					objectDto.setCode(customFormDto.getCode());//张方志提供
					objectDto.setName(customFormDto.getName());//张方志提供
					objectDto.setPcUrl("");//张方志提供
					String approveClass =  objectDto.getApproveClass();
					objectDto.setApproveClass(approveClass);//替换IP地址
					objectDto.setBusidataClass("");//替换IP地址
					objectDto.setCallbackClass("");//替换IP地址
					objectDto.setComment("COPY_AND_SAVE");
					//JacksonUtils.fromJson(userJson, BusinessObjectDto.class);
					System.out.println("objectDto="+JacksonUtils.toJson(objectDto));
					//String resultMsg = businessObjectService.saveObjectAndVariableData(userJson, objectDto);
				}
			}
			
			info.setResult("复制业务对象及变量操作完成!");
		    info.setSucess(true);
		    info.setMsg("获取列表对象成功!");
		} catch (Exception e) {
			 log.error("获取列表对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("获取列表对象失败!");
			 info.setExceptionMsg(e.getMessage());
		}
		return JacksonUtils.toJson(info);
	}

	@Override
	public String saveBusinessObject(String userJson, String saveJson) {
		 DubboServiceResultInfo info=new DubboServiceResultInfo();
		   try {
			   BusinessObject businessObject=JacksonUtils.fromJson(saveJson, BusinessObject.class);
			   businessObject = businessObjectService.saveBusinessObject(businessObject);
			   info.setResult(JacksonUtils.toJson(businessObject));
			   info.setSucess(true);
			   info.setMsg("保存或更新对象成功!");
			} catch (Exception e) {
			 log.error("保存对象失败!"+e.getMessage());
			 info.setSucess(false);
			 info.setMsg("保存对象失败!");
			 info.setExceptionMsg(e.getMessage());
			}
		   return JacksonUtils.toJson(info);
	}

	@Override
	public String updateSort(String userinfo, String updateJson, Map<String, Object> map) {
	   DubboServiceResultInfo info=new DubboServiceResultInfo();
	   try {
		   BusinessObject baseBusinessObject = JacksonUtils.fromJson(updateJson, BusinessObject.class);
		   BusinessObject object = businessObjectService.getObjectById(baseBusinessObject.getId());
		   int result= businessObjectService.updateSort(object, map);
		   info.setResult(JacksonUtils.toJson(result));
		   info.setSucess(true);
		   info.setMsg("更新对象成功!");
		} catch (Exception e) {
		   log.error("更新对象失败!"+e.getMessage());
		   info.setSucess(false);
		   info.setMsg("更新对象失败!");
		   info.setExceptionMsg(e.getMessage());
	   }
	   return JacksonUtils.toJson(info);
	}

}
