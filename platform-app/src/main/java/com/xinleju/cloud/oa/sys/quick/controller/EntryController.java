package com.xinleju.cloud.oa.sys.quick.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.xinleju.platform.sys.res.dto.ResourceDto;
import com.xinleju.platform.sys.res.dto.service.ResourceDtoServiceCustomer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.cloud.oa.sys.quick.dto.EntryDto;
import com.xinleju.cloud.oa.sys.quick.dto.EntryPortalDto;
import com.xinleju.cloud.oa.sys.quick.dto.service.EntryDtoServiceCustomer;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityResourceDto;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanRelationInfo;
import com.xinleju.platform.sys.base.dto.service.CustomFormDtoServiceCustomer;
import com.xinleju.platform.sys.base.dto.service.CustomFormGroupDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 快速入口表控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/sys/quick/entry")
public class EntryController {

	private static Logger log = Logger.getLogger(EntryController.class);
	
	@Autowired
	private EntryDtoServiceCustomer entryDtoServiceCustomer;
	@Autowired
	private CustomFormDtoServiceCustomer customFormDtoServiceCustomer;
	@Autowired
	private CustomFormGroupDtoServiceCustomer customFormGroupDtoServiceCustomer;
	@Autowired
	private ResourceDtoServiceCustomer resourceDtoServiceCustomer;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	/**
	 * 根据Id获取业务对象
	 * 
	 * @param id  业务对象主键
	 * 
	 * @return     业务对象
	 */
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult get(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=entryDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				EntryDto entryDto=JacksonUtils.fromJson(resultInfo, EntryDto.class);
				result.setResult(entryDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setCode(dubboServiceResultInfo.getResult());
			    result.setSuccess(dubboServiceResultInfo.isSucess());
			    result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	
	
	/**
	 * 返回分页对象
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
		    String dubboResultInfo=entryDtoServiceCustomer.getPage(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				@SuppressWarnings("rawtypes")
				PageBeanInfo pageInfo=JacksonUtils.fromJson(resultInfo, PageBeanInfo.class);
				result.setResult(pageInfo);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用page方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 返回符合条件的列表
	 * @param paramaterJson
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody String paramaterJson){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=entryDtoServiceCustomer.queryList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<EntryDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,EntryDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}
	
	/**
	 * 返回符合条件的列表
	 * @param paramaterJson
	 * @return
	 */
	@RequestMapping(value="/queryPortalList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryPortalList(@RequestBody String paramaterJson){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			String dubboResultInfo=customFormGroupDtoServiceCustomer.queryListForQuickEntry(userJson, null);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
		    	List<EntryPortalDto>  rtnList = new ArrayList<EntryPortalDto>();
				String resultInfo= dubboServiceResultInfo.getResult();
				List<EntryPortalDto> groupFormList=JacksonUtils.fromJson(resultInfo, ArrayList.class,EntryPortalDto.class);
				List<EntryPortalDto>  oneGroupFormList = new ArrayList<EntryPortalDto>();//1级菜单
				List<EntryPortalDto>  twoGroupFormList = new ArrayList<EntryPortalDto>();//2级菜单
				if(groupFormList.size()>0){
					for(int i=0;i<groupFormList.size();i++){
						if("0".equals(groupFormList.get(i).getParentId())){
							oneGroupFormList.add(groupFormList.get(i));
						}else{
							twoGroupFormList.add(groupFormList.get(i));
						}
					}
					String dubboResultInfo2=customFormDtoServiceCustomer.queryListForQuickEntry(userJson, paramaterJson);
					DubboServiceResultInfo dubboServiceResultInfo2= JacksonUtils.fromJson(dubboResultInfo2, DubboServiceResultInfo.class);
				    if(dubboServiceResultInfo2.isSucess()){
						List<EntryPortalDto> formList=JacksonUtils.fromJson(dubboServiceResultInfo2.getResult(), ArrayList.class,EntryPortalDto.class);
						if(formList.size()>0){
							//2级菜单
							List<EntryPortalDto> authorityTwoList = new ArrayList<EntryPortalDto>();
							//当前登录的所有信息
						    SecurityUserBeanRelationInfo beanInfo=LoginUtils.getSecurityUserBeanRelationInfo();
						    List<SecurityResourceDto>  ResourcList = beanInfo.getResourceDtoList();
						    StringBuffer srDtoIds = new StringBuffer();
						    //组装权限id
						    for(SecurityResourceDto srDto:ResourcList){
			    				if(srDto.getIsAuth().equals("1") && srDto.getType().equals("RESOURCE")){
			    					srDtoIds.append(srDto.getId()).append(",");
			    				}
			    			}
						    //匹配权限id并分组
						    Map<String, List<EntryPortalDto>> formResult = new HashMap<>();
						    for (EntryPortalDto e : formList) {
						    	if(e.getResourceId()!=null){
					    			if(srDtoIds.toString().indexOf(e.getResourceId())>=0){
					    				String pId = e.getParentId();
					    				List<EntryPortalDto> children = formResult.get(pId);
					    				if (children == null) {
					    					children = new ArrayList<EntryPortalDto>();
					    					formResult.put(pId, children);
					    				}
					    				children.add(e);
					    			}
					    		}
						    }
						    //遍历
							for(EntryPortalDto e : twoGroupFormList){
						    	for (Entry<String, List<EntryPortalDto>> entry : formResult.entrySet()) {
						    		if(e.getId().equals(entry.getKey())){
						    			e.setChildren(entry.getValue());
						    			authorityTwoList.add(e);
						    		}
						    	}
						    }
						  //匹配权限id并分组
						    Map<String, List<EntryPortalDto>> groupFormResult = new HashMap<>();
						    for (EntryPortalDto e : authorityTwoList) {
						    	if(e.getResourceId()!=null){
				    				String pId = e.getParentId();
				    				List<EntryPortalDto> children = groupFormResult.get(pId);
				    				if (children == null) {
				    					children = new ArrayList<EntryPortalDto>();
				    					groupFormResult.put(pId, children);
				    				}
				    				children.add(e);
					    		}
						    }
						    //组装返回对象
							for(EntryPortalDto group2Entry : oneGroupFormList){
								for(Entry<String, List<EntryPortalDto>> entry : groupFormResult.entrySet()){
									if(group2Entry.getId().equals(entry.getKey())){
										group2Entry.setChildren(entry.getValue());
										rtnList.add(group2Entry);
									}
								}
							}
						}
				    }
				}
				result.setResult(rtnList);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }
		} catch (Exception e) {
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}


	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody EntryDto t){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=entryDtoServiceCustomer.save(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				EntryDto entryDto=JacksonUtils.fromJson(resultInfo, EntryDto.class);
				result.setResult(entryDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			} catch (JsonProcessingException e1) {
				//e1.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 删除实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=entryDtoServiceCustomer.deleteObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				EntryDto entryDto=JacksonUtils.fromJson(resultInfo, EntryDto.class);
				result.setResult(entryDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}
	
	
	/**
	 * 删除实体对象
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=entryDtoServiceCustomer.deleteAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				EntryDto entryDto=JacksonUtils.fromJson(resultInfo, EntryDto.class);
				result.setResult(entryDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}
	
	/**
	 * 修改实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		EntryDto entryDto=null;
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=entryDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 @SuppressWarnings("unchecked")
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=entryDtoServiceCustomer.update(userJson, updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setCode(updateDubboServiceResultInfo.getResult());
				     result.setSuccess(updateDubboServiceResultInfo.isSucess());
				     result.setMsg(updateDubboServiceResultInfo.getMsg());
				 }
			}else{
				 result.setCode(ErrorInfoCode.NULL_ERROR.getValue());
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		}catch (Exception e) {
			try{
			 ////e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(entryDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			}catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 修改修改实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/updateBatchStatus/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult updateBatchStatus(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		EntryDto entryDto=null;
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=entryDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				 String resultInfo= dubboServiceResultInfo.getResult();
				 @SuppressWarnings("unchecked")
				 Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				 
				 //判断是否更改父级节点   是则重新获取序号
				 if(!oldMap.get("parentId").equals(map.get("parentId"))){
					   Map<String, Object> pMap = new HashMap<String,Object>();
					   pMap.put("parentId",map.get("parentId"));
					   Integer sortNum = entryDtoServiceCustomer.getRulerSortNum(userJson,pMap);
					   map.put("sortNumber",sortNum);
				 }
				 
				 oldMap.putAll(map);
				 String updateJson= JacksonUtils.toJson(oldMap);
				 String updateDubboResultInfo=entryDtoServiceCustomer.updateBatchStatus(userJson, updateJson);
				 DubboServiceResultInfo updateDubboServiceResultInfo= JacksonUtils.fromJson(updateDubboResultInfo, DubboServiceResultInfo.class);
				 if(updateDubboServiceResultInfo.isSucess()){
					 Integer i=JacksonUtils.fromJson(updateDubboServiceResultInfo.getResult(), Integer.class);
					 result.setResult(i);
					 result.setSuccess(MessageInfo.UPDATESUCCESS.isResult());
					 result.setMsg(MessageInfo.UPDATESUCCESS.getMsg());
				 }else{
					 result.setCode(updateDubboServiceResultInfo.getResult());
				     result.setSuccess(updateDubboServiceResultInfo.isSucess());
				     result.setMsg(updateDubboServiceResultInfo.getMsg());
				 }
			}else{
				 result.setCode(ErrorInfoCode.NULL_ERROR.getValue());
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		}catch (Exception e) {
			try{
			 ////e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(entryDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			}catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 伪删除实体对象
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=entryDtoServiceCustomer.deletePseudoObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				EntryDto entryDto=JacksonUtils.fromJson(resultInfo, EntryDto.class);
				result.setResult(entryDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用deletePseudo方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 伪删除实体对象
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=entryDtoServiceCustomer.deletePseudoAllObjectByIds(userJson, "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				EntryDto entryDto=JacksonUtils.fromJson(resultInfo, EntryDto.class);
				result.setResult(entryDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
				result.setCode(dubboServiceResultInfo.getResult());
			    result.setSuccess(dubboServiceResultInfo.isSucess());
			    result.setMsg(dubboServiceResultInfo.getMsg());
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数ids"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	/**
	 * 获取树列表数据
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/queryTreeList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryTreeList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=entryDtoServiceCustomer.queryTreeList(userJson, paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<EntryDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,EntryDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
				result.setCode(dubboServiceResultInfo.getResult());
			    result.setSuccess(dubboServiceResultInfo.isSucess());
			    result.setMsg(dubboServiceResultInfo.getMsg());
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	
	/**
	 * 对业务对象设置启用 禁用
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/updateStatus/{id}",method=RequestMethod.PUT)
	public @ResponseBody MessageResult updateStatus(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=entryDtoServiceCustomer.updateStatus(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				int i=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), Integer.class);
				result.setResult(i);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
				result.setCode(dubboServiceResultInfo.getResult());
			    result.setSuccess(dubboServiceResultInfo.isSucess());
			    result.setMsg(dubboServiceResultInfo.getMsg());
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用updateStatus方法:  【参数id"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		
		return result;
	}
	
	/**
	 * 模糊查询
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/fuzzySearch",method=RequestMethod.POST,consumes="application/json")
	public @ResponseBody MessageResult fuzzySearch(@RequestBody Map<String,Object> map){
		String paramaterJson = JacksonUtils.toJson(map);
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo= entryDtoServiceCustomer.fuzzySearch(userJson,paramaterJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<EntryDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,EntryDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
				result.setCode(dubboServiceResultInfo.getResult());
			    result.setSuccess(dubboServiceResultInfo.isSucess());
			    result.setMsg(dubboServiceResultInfo.getMsg());
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用updateStatus方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;
	}
	
	/**
	 * 对规则排序(上移/下移/置顶/置底)
	 * @return
	 */
	@RequestMapping(value="/updateSort/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult updateSort(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			String dubboResultInfo=entryDtoServiceCustomer.updateSort(userJson, "{\"id\":\""+id+"\"}",map);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				int i=JacksonUtils.fromJson(dubboServiceResultInfo.getResult(), Integer.class);
				result.setResult(i);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.UPDATESORTSUCCESS.getMsg());
		    }else{
				result.setCode(dubboServiceResultInfo.getResult());
			    result.setSuccess(dubboServiceResultInfo.isSucess());
			    result.setMsg(dubboServiceResultInfo.getMsg());
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用updateStatus方法:  【参数id"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
		}
		return result;	
	}
	
	/**
	 * 保存实体对象
	 * @return
	 */
	@RequestMapping(value="/saveEntryAndAuthor")
	public @ResponseBody MessageResult save(@RequestParam(value = "linkIcon") MultipartFile uploadfile,
			HttpServletRequest request, HttpServletResponse response){
		MessageResult result=new MessageResult();
		long length = uploadfile.getSize();
		if(length>1*1024*1024){
			result.setSuccess(false);
			result.setMsg("图片尺寸不能大于1M");
			return result;
		}
		Map<String,Object> t = new HashMap<String,Object>();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			//用户对象转json
			String userJson = JacksonUtils.toJson(user);
			
			InputStream is = uploadfile.getInputStream();
			byte[] headpic = new byte[is.available()];  
	        is.read(headpic);  
	        is.close();  

	        t.put("id",request.getParameter("id"));
	        t.put("code",request.getParameter("code"));
	        t.put("name",request.getParameter("name"));
	        t.put("delflag",false);
	        t.put("parentId",request.getParameter("parentId"));
	        t.put("isInner",Integer.parseInt(request.getParameter("isInner")));
	        t.put("resourceId",request.getParameter("resourceId"));
	        t.put("resourceName",request.getParameter("resourceName"));
	        t.put("url",request.getParameter("url"));
	        t.put("status",Integer.parseInt(request.getParameter("status")));
	        if(headpic.length>0){
	        	t.put("linkIcon",headpic);
	        }
			
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=entryDtoServiceCustomer.saveEntryAndAuthor(userJson, saveJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				EntryDto entryDto=JacksonUtils.fromJson(resultInfo, EntryDto.class);
				result.setResult(entryDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setCode(dubboServiceResultInfo.getResult());
		    	result.setSuccess(dubboServiceResultInfo.isSucess());
				result.setMsg(dubboServiceResultInfo.getMsg());
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			} catch (JsonProcessingException e1) {
				//e1.printStackTrace();
			}
		}
		return result;
	}

	/**
	 *获取菜单按钮code及用户、sessionId
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/getUserAndSessionId/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult getUserAndSessionId(@PathVariable("id")  String id,HttpServletRequest request){
		MessageResult result=new MessageResult();
		try {
			SecurityUserBeanInfo userBeanInfo = LoginUtils.getSecurityUserBeanInfo();
			String userJson = JacksonUtils.toJson(userBeanInfo);
			String dubboResultInfo=resourceDtoServiceCustomer.getObjectById(userJson, "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				ResourceDto resourceDto=JacksonUtils.fromJson(resultInfo, ResourceDto.class);
				//获取当前登陆用户的username及sessionId
				resourceDto.setName(userBeanInfo.getSecurityUserDto().getLoginName());
				resourceDto.setRoleId(request.getSession().getId());
				result.setResult(resourceDto);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用get方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}

	/**
	 * 获取最近搜索内容
	 * @param paramaterJson
	 * @return
	 */
	@RequestMapping(value="/getRecentSearchContent",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getRecentSearchContent(@RequestBody String paramaterJson){
		MessageResult result=new MessageResult();
		try {
				//获取用户对象
				SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
				String tendId = user.getTendId();
				String userId = user.getSecurityUserDto().getId();

				List<String> rtnList=new ArrayList();
				ListOperations<String,String> listOperations = redisTemplate.opsForList();
				List<String> list = listOperations.range(userId+"@"+tendId+"_search",0,-1);
				for(String str : list){
					rtnList.add(str);
				}
				result.setResult(rtnList);
				result.setSuccess(MessageInfo.CREATESUCCESS.isResult());
				result.setMsg(MessageInfo.CREATESUCCESS.getMsg());
		} catch (Exception e) {
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.CREATERROR.isResult());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}
	/**
	 * 更新最近搜索内容
	 * @param paramaterJson
	 * @return
	 */
	@RequestMapping(value="/setRecentSearchContent",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult setRecentSearchContent(@RequestBody String paramaterJson){
		MessageResult result=new MessageResult();
		try {
			if(!paramaterJson.isEmpty()){
				//获取用户对象
				SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
				String tendId = user.getTendId();
				String userId = user.getSecurityUserDto().getId();

				ListOperations<String,String> listOperations = redisTemplate.opsForList();
				listOperations.leftPush(userId+"@"+tendId+"_search",paramaterJson);
				int num = listOperations.size(userId+"@"+tendId+"_search").intValue();
				if(num>5){
					listOperations.trim(userId+"@"+tendId+"_search",0,4);
				}
				result.setResult(paramaterJson);
				result.setSuccess(MessageInfo.CREATESUCCESS.isResult());
				result.setMsg(MessageInfo.CREATESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.CREATERROR.isResult());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			}
		} catch (Exception e) {
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.CREATERROR.isResult());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}
	/**
	 * 清空最近搜索内容
	 * @param paramaterJson
	 * @return
	 */
	@RequestMapping(value="/delRecentSearchContent",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult delRecentSearchContent(@RequestBody String paramaterJson){
		MessageResult result=new MessageResult();
		try {
			if(!paramaterJson.isEmpty()){
				//获取用户对象
				SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
				String tendId = user.getTendId();
				String userId = user.getSecurityUserDto().getId();
				redisTemplate.delete(userId+"@"+tendId+"_search");
				result.setResult(paramaterJson);
				result.setSuccess(MessageInfo.CREATESUCCESS.isResult());
				result.setMsg(MessageInfo.CREATESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.CREATERROR.isResult());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			}
		} catch (Exception e) {
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.CREATERROR.isResult());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}

	/**
	 * 获取常用快速入口
	 * @param paramaterJson
	 * @return
	 */
	@RequestMapping(value="/queryFavouriteEntry",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryFavouriteEntry(@RequestBody String paramaterJson){
		MessageResult result=new MessageResult();
		try {
			//获取用户对象
			SecurityUserBeanInfo user=LoginUtils.getSecurityUserBeanInfo();
			String tendId = user.getTendId();
			String userId = user.getSecurityUserDto().getId();

			List<String> rtnList=new ArrayList();
			ListOperations<String,String> listOperations = redisTemplate.opsForList();
			List<String> list = listOperations.range(userId+"@"+tendId+"_entry",0,-1);
			for(String str : list){
				rtnList.add(str);
			}
			result.setResult(rtnList);
			result.setSuccess(MessageInfo.GETSUCCESS.isResult());
			result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		} catch (Exception e) {
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}
	/**
	 * 保存常用快速入口
	 * @param paramaterJson
	 * @return
	 */
	@RequestMapping(value="/saveFavouriteEntry",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult saveFavouriteEntry(@RequestBody String paramaterJson){
		MessageResult result=new MessageResult();
		try {
			if(!paramaterJson.isEmpty()) {
				//获取用户对象
				SecurityUserBeanInfo user = LoginUtils.getSecurityUserBeanInfo();
				String tendId = user.getTendId();
				String userId = user.getSecurityUserDto().getId();

				ListOperations<String, String> listOperations = redisTemplate.opsForList();
				redisTemplate.delete(userId + "@" + tendId + "_entry");

				List<Map<String,String>> list = JacksonUtils.fromJson(paramaterJson, ArrayList.class, Map.class);
				for(Map<String,String> map : list){
					String jsMap = JacksonUtils.toJson(map);
					listOperations.rightPush(userId + "@" + tendId + "_entry", jsMap);
				}
				result.setResult(list);
				result.setSuccess(MessageInfo.CREATESUCCESS.isResult());
				result.setMsg(MessageInfo.CREATESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.CREATERROR.isResult());
				result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
				result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
			}
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.CREATERROR.isResult());
			result.setCode(ErrorInfoCode.SYSTEM_ERROR.getValue());
			result.setMsg(ErrorInfoCode.SYSTEM_ERROR.getName());
		}
		return result;
	}
}
