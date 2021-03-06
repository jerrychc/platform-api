package com.xinleju.platform.sys.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinleju.platform.base.utils.DubboServiceResultInfo;
import com.xinleju.platform.base.utils.ErrorInfoCode;
import com.xinleju.platform.base.utils.MessageInfo;
import com.xinleju.platform.base.utils.MessageResult;
import com.xinleju.platform.base.utils.PageBeanInfo;
import com.xinleju.platform.base.utils.SecurityUserBeanInfo;
import com.xinleju.platform.sys.base.dto.CustomFormGroupDto;
import com.xinleju.platform.sys.base.dto.CustomFormNodeDto;
import com.xinleju.platform.sys.base.dto.service.CustomFormGroupDtoServiceCustomer;
import com.xinleju.platform.tools.data.JacksonUtils;
import com.xinleju.platform.uitls.LoginUtils;


/**
 * 自定义表单分类控制层
 * @author admin
 *
 */
@Controller
@RequestMapping("/sys/base/customFormGroup")
public class CustomFormGroupController {

	private static Logger log = Logger.getLogger(CustomFormGroupController.class);
	
	@Autowired
	private CustomFormGroupDtoServiceCustomer customFormGroupDtoServiceCustomer;
	
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
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormGroupDtoServiceCustomer.getObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormGroupDto customFormGroupDto=JacksonUtils.fromJson(resultInfo, CustomFormGroupDto.class);
				result.setResult(customFormGroupDto);
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
	 * 返回分页对象
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/page",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult page(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
		    String dubboResultInfo=customFormGroupDtoServiceCustomer.getPage(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
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
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryListForQuickEntry",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryListForQuickEntry(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormGroupDtoServiceCustomer.queryListForQuickEntry(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<CustomFormGroupDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,CustomFormGroupDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}
	
	/**
	 * 返回符合条件的列表
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/queryList",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult queryList(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormGroupDtoServiceCustomer.queryList(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<CustomFormGroupDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,CustomFormGroupDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用queryList方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}


	/**
	 * 保存实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/save",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult save(@RequestBody CustomFormGroupDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			//保存当前节点
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=customFormGroupDtoServiceCustomer.validateBeforeSave(JacksonUtils.toJson(securityUserBeanInfo),saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormGroupDto customFormGroupDto=JacksonUtils.fromJson(resultInfo, CustomFormGroupDto.class);
				result.setResult(customFormGroupDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	 * 生成数据方法一级分类
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/saveGenerateDataLevelOne",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult saveGenerateDataLevelOne(@RequestBody CustomFormGroupDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			//保存当前节点
			t.setCode("1");
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=customFormGroupDtoServiceCustomer.saveGenerateData(JacksonUtils.toJson(securityUserBeanInfo),saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormGroupDto customFormGroupDto=JacksonUtils.fromJson(resultInfo, CustomFormGroupDto.class);
				result.setResult(customFormGroupDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	 * 生成数据方法
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/saveGenerateData",method=RequestMethod.POST, consumes="application/json")
	public @ResponseBody MessageResult saveGenerateData(@RequestBody CustomFormGroupDto t){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			//保存当前节点
			t.setCode("2");
			String saveJson= JacksonUtils.toJson(t);
			String dubboResultInfo=customFormGroupDtoServiceCustomer.saveGenerateData(JacksonUtils.toJson(securityUserBeanInfo),saveJson);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormGroupDto customFormGroupDto=JacksonUtils.fromJson(resultInfo, CustomFormGroupDto.class);
				result.setResult(customFormGroupDto);
				result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
				result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
		} catch (Exception e) {
			try {
				////e.printStackTrace();
			    ObjectMapper mapper = new ObjectMapper();
				String  paramJson = mapper.writeValueAsString(t);
				log.error("调用save方法:  【参数"+paramJson+"】======"+"【"+e.getMessage()+"】");
				result.setSuccess(MessageInfo.SAVEERROR.isResult());
				result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+e.getMessage()+"】");
			} catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
		}
		return result;
	}
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult delete(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormGroupDtoServiceCustomer.deleteObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormGroupDto customFormGroupDto=JacksonUtils.fromJson(resultInfo, CustomFormGroupDto.class);
				result.setResult(customFormGroupDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+id+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	
	/**
	 * 删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deleteBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deleteBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormGroupDtoServiceCustomer.deleteAllObjectByIds(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormGroupDto customFormGroupDto=JacksonUtils.fromJson(resultInfo, CustomFormGroupDto.class);
				result.setResult(customFormGroupDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用delete方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 修改修改实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT,consumes="application/json")
	public @ResponseBody MessageResult update(@PathVariable("id")  String id,   @RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		CustomFormGroupDto customFormGroupDto=null;
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormGroupDtoServiceCustomer.getObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				Map<String,Object> oldMap=JacksonUtils.fromJson(resultInfo, HashMap.class);
				oldMap.putAll(map);
				String updateJson= JacksonUtils.toJson(oldMap);
				 
				String dubboResultInfoVal=customFormGroupDtoServiceCustomer.validateBeforeUpdate(JacksonUtils.toJson(securityUserBeanInfo),updateJson);
				DubboServiceResultInfo dubboServiceResultInfoVal= JacksonUtils.fromJson(dubboResultInfoVal, DubboServiceResultInfo.class);
			    if(dubboServiceResultInfoVal.isSucess()){
					String resultInfoVal= dubboServiceResultInfoVal.getResult();
					customFormGroupDto=JacksonUtils.fromJson(resultInfoVal, CustomFormGroupDto.class);
					result.setResult(customFormGroupDto);
					result.setSuccess(MessageInfo.SAVESUCCESS.isResult());
					result.setMsg(MessageInfo.SAVESUCCESS.getMsg());
			    }else{
			    	result.setSuccess(MessageInfo.SAVEERROR.isResult());
					result.setMsg(MessageInfo.SAVEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			    }
			}else{
				 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
				 result.setMsg("不存在更新的对象");
			}
		} catch (Exception e) {
			try{
			 ////e.printStackTrace();
			 ObjectMapper mapper = new ObjectMapper();
			 String  paramJson = mapper.writeValueAsString(customFormGroupDto);
			 log.error("调用update方法:  【参数"+id+","+paramJson+"】======"+"【"+e.getMessage()+"】");
			 result.setSuccess(MessageInfo.UPDATEERROR.isResult());
			 result.setMsg(MessageInfo.UPDATEERROR.getMsg()+"【"+e.getMessage()+"】");
			}catch (JsonProcessingException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			
		}
		return result;
	}

	/**
	 * 伪删除实体对象
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudo/{id}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudo(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormGroupDtoServiceCustomer.deletePseudoObjectById(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+id+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormGroupDto customFormGroupDto=JacksonUtils.fromJson(resultInfo, CustomFormGroupDto.class);
				result.setResult(customFormGroupDto);
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
	 * @param t
	 * @return
	 */
	@RequestMapping(value="/deletePseudoBatch/{ids}",method=RequestMethod.DELETE)
	public @ResponseBody MessageResult deletePseudoBatch(@PathVariable("ids")  String ids){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormGroupDtoServiceCustomer.deletePseudoAllObjectByIds(JacksonUtils.toJson(securityUserBeanInfo), "{\"id\":\""+ids+"\"}");
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				CustomFormGroupDto customFormGroupDto=JacksonUtils.fromJson(resultInfo, CustomFormGroupDto.class);
				result.setResult(customFormGroupDto);
				result.setSuccess(MessageInfo.DELETESUCCESS.isResult());
				result.setMsg(MessageInfo.DELETESUCCESS.getMsg());
			}else{
				result.setSuccess(MessageInfo.DELETEERROR.isResult());
				result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
			}
		} catch (Exception e) {
			////e.printStackTrace();
		    log.error("调用deletePseudoBatch方法:  【参数"+ids+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.DELETEERROR.isResult());
			result.setMsg(MessageInfo.DELETEERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		
		return result;
	}
	
	/**
	 * 获取业务对象树
	 * @param paramater
	 * @return
	 */
	@RequestMapping(value="/getTree",method={RequestMethod.POST}, consumes="application/json")
	public @ResponseBody MessageResult getTree(@RequestBody Map<String,Object> map){
		MessageResult result=new MessageResult();
		String paramaterJson = JacksonUtils.toJson(map);
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormGroupDtoServiceCustomer.getTree(JacksonUtils.toJson(securityUserBeanInfo), paramaterJson);
		    DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
		    if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				List<CustomFormNodeDto> list=JacksonUtils.fromJson(resultInfo, ArrayList.class,CustomFormNodeDto.class);
				result.setResult(list);
				result.setSuccess(MessageInfo.GETSUCCESS.isResult());
				result.setMsg(MessageInfo.GETSUCCESS.getMsg());
		    }else{
		    	result.setSuccess(MessageInfo.GETERROR.isResult());
				result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+dubboServiceResultInfo.getExceptionMsg()+"】");
		    }
			
		} catch (Exception e) {
			////e.printStackTrace();
			log.error("调用getTree方法:  【参数"+paramaterJson+"】======"+"【"+e.getMessage()+"】");
			result.setSuccess(MessageInfo.GETERROR.isResult());
			result.setMsg(MessageInfo.GETERROR.getMsg()+"【"+e.getMessage()+"】");
		}
		return result;
	}	
	

	/**
	  * @Description:查询一级表单分类下是否存在二级分类
	  * @author:zhangfangzhi
	  * @date 2017年3月17日 下午5:30:20
	  * @version V1.0
	 */
	@RequestMapping(value="/getGroupCount/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult getGroupCount(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
		String dubboResultInfo=customFormGroupDtoServiceCustomer.getCustomFormGroupCountByPID(JacksonUtils.toJson(securityUserBeanInfo),id);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
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
	  * @Description:查询该分类下是否有表单
	  * @author:zhangfangzhi
	  * @date 2017年3月17日 下午5:30:20
	  * @version V1.0
	 */
	@RequestMapping(value="/getCount/{id}",method=RequestMethod.GET)
	public @ResponseBody MessageResult getCount(@PathVariable("id")  String id){
		MessageResult result=new MessageResult();
		SecurityUserBeanInfo securityUserBeanInfo = LoginUtils.getSecurityUserBeanInfo();
		try {
			String dubboResultInfo=customFormGroupDtoServiceCustomer.getCustomFormCountByGroupId(JacksonUtils.toJson(securityUserBeanInfo),id);
			DubboServiceResultInfo dubboServiceResultInfo= JacksonUtils.fromJson(dubboResultInfo, DubboServiceResultInfo.class);
			if(dubboServiceResultInfo.isSucess()){
				String resultInfo= dubboServiceResultInfo.getResult();
				result.setResult(resultInfo);
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
	 * 对规则排序(上移/下移/置顶/置底)
	 * @param t
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
			String dubboResultInfo=customFormGroupDtoServiceCustomer.updateSort(userJson, "{\"id\":\""+id+"\"}",map);
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
}
